package be.isach.ultracosmetics.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// to be used in 1.17.1 and below, before Spigot supported comment preservation
public class ManualCommentConfiguration extends CustomConfiguration {

    private Map<String,List<String>> comments = null;
    private static final boolean newLineAfterHeader = false;
    private static final boolean newLinePerKey = false;

    public ManualCommentConfiguration() {
        this.comments = new LinkedHashMap<>();
    }

    private static String getPathToComment(List<String> configContents, int lineIndex, String configLine) {
        if (configContents != null && lineIndex >= 0 && lineIndex < configContents.size() - 1 && configLine != null) {
            String trimmedConfigLine = trimPrefixSpaces(configLine);
            if (trimmedConfigLine.startsWith("#")) {
                int currentIndex = lineIndex;
                while (currentIndex < configContents.size() - 1) {
                    // issue here about infinite comments???
                    currentIndex++;
                    String currentLine = configContents.get(currentIndex);
                    String trimmedCurrentLine = trimPrefixSpaces(currentLine);
                    if (!trimmedCurrentLine.startsWith("#")) {
                        if (trimmedCurrentLine.contains(":")) {
                            return getPathToKey(configContents, currentIndex, currentLine);
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        return null;
    }

    private static String getPathToKey(List<String> configContents, int lineIndex, String configLine) {
        if (configContents != null && lineIndex >= 0 && lineIndex < configContents.size() && configLine != null) {
            if (!configLine.startsWith("#") && configLine.contains(":")) {
                int spacesBeforeKey = getPrefixSpaceCount(configLine);
                String key = trimPrefixSpaces(configLine.substring(0, configLine.indexOf(':')));
                if (spacesBeforeKey > 0) {
                    int currentIndex = lineIndex;
                    int previousSpacesBeforeCurrentLine = -1;
                    boolean atZeroSpaces = false;

                    while (currentIndex > 0) {
                        currentIndex--;
                        String currentLine = configContents.get(currentIndex);
                        int spacesBeforeCurrentLine = getPrefixSpaceCount(currentLine);

                        if (trim(currentLine).isEmpty()) break;
                        if (!trim(currentLine).startsWith("#")) {
                            if (spacesBeforeCurrentLine < spacesBeforeKey) {
                                if (currentLine.contains(":")) {
                                    if (spacesBeforeCurrentLine > 0 || !atZeroSpaces) {
                                        if (spacesBeforeCurrentLine == 0) atZeroSpaces = true;
                                        if (previousSpacesBeforeCurrentLine == -1 || spacesBeforeCurrentLine < previousSpacesBeforeCurrentLine) {
                                            previousSpacesBeforeCurrentLine = spacesBeforeCurrentLine;
                                            key = trimPrefixSpaces(currentLine.substring(0, currentLine.indexOf(":"))) + "." + key;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                return key;
            }
        }
        return null;
    }

    private static int getPrefixSpaceCount(String aString) {
        int spaceCount = 0;
        if (aString != null && aString.contains(" ")) {
            for (char aCharacter : aString.toCharArray()) {
                if (aCharacter == ' ')
                    spaceCount++;
                else
                    break;
            }
        }
        return spaceCount;
    }

    private static String trim(String aString) {
        return aString != null ? aString.trim().replace(System.lineSeparator(), "") : "";
    }

    private static String trimPrefixSpaces(String aString) {
        if (aString != null) {
            while (aString.startsWith(" "))
                aString = aString.substring(1);
        }
        return aString;
    }

    @Override
    public ConfigurationSection createSection(String path, String... comments) {
        if (path != null && comments != null && comments.length > 0) {
            List<String> commentsList = new ArrayList<>();
            for (String comment : comments) {
                if (comment != null)
                    commentsList.add(comment);
                else
                    commentsList.add("");
            }
            this.comments.put(path, commentsList);
        }
        return super.createSection(path);
    }

    @Override
    public void load(File file) throws IOException, InvalidConfigurationException {
        super.load(file);

        BufferedReader configReader = null;
        List<String> configLines = new ArrayList<>();
        try {
            configReader = new BufferedReader(new FileReader(file));
            String configReadLine;
            while ((configReadLine = configReader.readLine()) != null)
                configLines.add(configReadLine);
        } finally {
            if (configReader != null) configReader.close();
        }

        boolean hasHeader = configLines.size() < 2 || !trim(configLines.get(1)).isEmpty();

        Map<String,List<String>> configComments = new LinkedHashMap<>();
        for (int lineIndex = 0; lineIndex < configLines.size(); lineIndex++) {
            String configLine = configLines.get(lineIndex);
            String trimmedLine = trimPrefixSpaces(configLine);
            if (trimmedLine.startsWith("#") && (lineIndex > 0 || !hasHeader)) {
                String configKey = getPathToComment(configLines, lineIndex, configLine);
                if (configKey != null) {
                    List<String> keyComments = configComments.get(configKey);
                    if (keyComments == null) keyComments = new ArrayList<>();
                    keyComments.add(trimmedLine.substring(trimmedLine.startsWith("# ") ? 2 : 1));
                    configComments.put(configKey, keyComments);
                }
            }
        }
        comments = configComments;
    }

    @Override
    public void save(File file) throws IOException {
        super.save(file);

        List<String> configContent = new ArrayList<>();
        try (BufferedReader configReader = new BufferedReader(new FileReader(file))) {
            String configReadLine;
            while ((configReadLine = configReader.readLine()) != null)
                configContent.add(configReadLine);
        }

        try (BufferedWriter configWriter = new BufferedWriter(new FileWriter(file))) {
            configWriter.write("");
            for (int lineIndex = 0; lineIndex < configContent.size(); lineIndex++) {
                String configLine = configContent.get(lineIndex);
                String configKey = null;
                if (!configLine.startsWith("#") && configLine.contains(":"))
                    configKey = getPathToKey(configContent, lineIndex, configLine);
                if (configKey != null && this.comments.containsKey(configKey)) {
                    int numOfSpaces = getPrefixSpaceCount(configLine);
                    StringBuilder spacePrefix = new StringBuilder();
                    for (int i = 0; i < numOfSpaces; i++)
                        spacePrefix.append(" ");
                    List<String> configComments = this.comments.get(configKey);
                    if (configComments != null) {
                        for (String comment : configComments) {
                            configWriter.append(spacePrefix.toString()).append("# ").append(comment);
                            configWriter.newLine();
                        }
                    }
                }
                configWriter.append(configLine);
                configWriter.newLine();
                boolean isComment = configLine.startsWith("#");
                if (newLineAfterHeader && lineIndex == 0 && isComment) {
                    configWriter.newLine();
                } else if (newLinePerKey && lineIndex < configContent.size() - 1 && !isComment) {
                    String nextConfigLine = configContent.get(lineIndex + 1);
                    if (nextConfigLine != null && !nextConfigLine.startsWith(" ")) {
                        if (!nextConfigLine.startsWith("'") && !nextConfigLine.startsWith("-")) configWriter.newLine();
                    }
                }
            }
        }
    }

    @Override
    public void set(String key, Object value, List<String> comments) {
        super.set(key, value);
        if (value == null || comments == null || comments.size() == 0) {
            this.comments.remove(key);
            return;
        }
        this.comments.put(key, comments);
    }

    @Override
    public List<String> comments(String path) {
        return comments.getOrDefault(path, new ArrayList<>());
    }
}
