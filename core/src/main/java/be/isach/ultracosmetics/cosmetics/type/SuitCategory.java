package be.isach.ultracosmetics.cosmetics.type;

import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.config.CustomConfiguration;
import be.isach.ultracosmetics.config.MessageManager;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.suits.ArmorSlot;
import be.isach.ultracosmetics.cosmetics.suits.Suit;
import be.isach.ultracosmetics.cosmetics.suits.SuitRave;
import be.isach.ultracosmetics.util.MathUtils;
import be.isach.ultracosmetics.util.SmartLogger;
import com.cryptomorin.xseries.XItemStack;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class SuitCategory {

    public static void register() {
        new SuitCategory("Rave",
                XMaterial.LEATHER_HELMET, XMaterial.LEATHER_CHESTPLATE, XMaterial.LEATHER_LEGGINGS, XMaterial.LEATHER_BOOTS,
                // MathUtils.random(int) is inclusive
                s -> Color.fromRGB(MathUtils.random(255), MathUtils.random(255), MathUtils.random(255)), SuitRave.class) {
            @Override
            public void setupConfig(CustomConfiguration config, String path) {
                config.addDefault(path + ".Update-Delay-In-Creative", 10,
                        "How many ticks UC should wait between updating the rave suit for creative players.",
                        "Setting this to a higher value allows more time between updates,",
                        "meaning players shouldn't have their inventories close immediately after opening.",
                        "Set to 1 or less to update every tick.");
            }
        };
        new SuitCategory("Astronaut",
                XMaterial.GLASS, XMaterial.GOLDEN_CHESTPLATE, XMaterial.GOLDEN_LEGGINGS, XMaterial.GOLDEN_BOOTS, s -> null, Suit.class);
        new SuitCategory("Diamond",
                XMaterial.DIAMOND_HELMET, XMaterial.DIAMOND_CHESTPLATE, XMaterial.DIAMOND_LEGGINGS, XMaterial.DIAMOND_BOOTS, s -> null, Suit.class);
        new SuitCategory("Santa",
                XMaterial.LEATHER_HELMET, XMaterial.LEATHER_CHESTPLATE, XMaterial.LEATHER_LEGGINGS, XMaterial.LEATHER_BOOTS, s -> Color.RED, Suit.class);
        new SuitCategory("Frozen",
                XMaterial.PACKED_ICE, XMaterial.LEATHER_CHESTPLATE, XMaterial.LEATHER_LEGGINGS, XMaterial.LEATHER_BOOTS,
                s -> (s == ArmorSlot.HELMET ? null : Color.AQUA), Suit.class);
        new SuitCategory("Cursed",
                XMaterial.JACK_O_LANTERN, XMaterial.LEATHER_CHESTPLATE, XMaterial.LEATHER_LEGGINGS, XMaterial.LEATHER_BOOTS,
                s -> (s == ArmorSlot.HELMET ? null : Color.fromRGB(35, 30, 42)), Suit.class);
        new SuitCategory("Slime",
                XMaterial.SLIME_BLOCK, XMaterial.LEATHER_CHESTPLATE, XMaterial.LEATHER_LEGGINGS, XMaterial.LEATHER_BOOTS,
                s -> (s == ArmorSlot.HELMET ? null : Color.fromRGB(128, 241, 95)), Suit.class);
        ConfigurationSection customSection = CosmeticType.getCustomConfig(Category.SUITS_HELMET);
        if (customSection != null) {
            loadCustom(customSection);
        }
    }

    private static void loadCustom(ConfigurationSection custom) {
        SmartLogger log = UltraCosmeticsData.get().getPlugin().getSmartLogger();
        for (String key : custom.getKeys(false)) {
            if (!custom.isConfigurationSection(key)) continue;
            ConfigurationSection section = custom.getConfigurationSection(key);
            ItemStack helmetItem = loadPart(section, "Helmet");
            ItemStack chestplateItem = loadPart(section, "Chestplate");
            ItemStack leggingsItem = loadPart(section, "Leggings");
            ItemStack bootsItem = loadPart(section, "Boots");
            if (helmetItem == null || chestplateItem == null || leggingsItem == null || bootsItem == null) {
                log.write(SmartLogger.LogLevel.WARNING, "Incomplete custom suit '" + key +
                        "' (one or more of [helmet, chestplate, leggings, boots] is missing)");
                continue;
            }
            addDefaultStrings(key);
            new SuitCategory(key, helmetItem, chestplateItem, leggingsItem, bootsItem, Suit.class);
        }
    }

    private static ItemStack loadPart(ConfigurationSection section, String key) {
        if (!section.isConfigurationSection(key)) {
            return null;
        }
        return XItemStack.deserialize(section.getConfigurationSection(key));
    }

    private static void addDefaultStrings(String key) {
        String configPath = Category.SUITS_HELMET.getConfigPath() + "." + key;
        MessageManager.addMessage(configPath + ".whole-equip", "<bold>whole</bold> " + key + " <bold>suit");
        MessageManager.addMessage(configPath + ".helmet-name", key + " <bold>Helmet");
        MessageManager.addMessage(configPath + ".chestplate-name", key + " <bold>Chestplate");
        MessageManager.addMessage(configPath + ".leggings-name", key + " <bold>Leggings");
        MessageManager.addMessage(configPath + ".boots-name", key + " <bold>Boots");
        MessageManager.addMessage(configPath + ".Description", "A custom suit!");
    }

    private static final List<SuitCategory> VALUES = new ArrayList<>();
    private final String configName;
    private final ItemStack helmetItem;
    private final ItemStack chestplateItem;
    private final ItemStack leggingsItem;
    private final ItemStack bootsItem;
    private SuitType helmet;
    private SuitType chestplate;
    private SuitType leggings;
    private SuitType boots;
    private final Class<? extends Suit> clazz;

    private SuitCategory(String configName, XMaterial helmet, XMaterial chestplate,
                         XMaterial leggings, XMaterial boots, Function<ArmorSlot, Color> colorFunc, Class<? extends Suit> clazz) {
        this.configName = configName;
        this.clazz = clazz;
        this.helmetItem = colorize(helmet.parseItem(), colorFunc.apply(ArmorSlot.HELMET));
        this.chestplateItem = colorize(chestplate.parseItem(), colorFunc.apply(ArmorSlot.CHESTPLATE));
        this.leggingsItem = colorize(leggings.parseItem(), colorFunc.apply(ArmorSlot.LEGGINGS));
        this.bootsItem = colorize(boots.parseItem(), colorFunc.apply(ArmorSlot.BOOTS));
        VALUES.add(this);
    }

    private SuitCategory(String configName, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots, Class<? extends Suit> clazz) {
        this.configName = configName;
        this.clazz = clazz;
        this.helmetItem = helmet;
        this.chestplateItem = chestplate;
        this.leggingsItem = leggings;
        this.bootsItem = boots;
        VALUES.add(this);
    }

    public void initializeSuitParts() {
        this.helmet = new SuitType(getHelmetItem(), ArmorSlot.HELMET, this);
        this.chestplate = new SuitType(getChestplateItem(), ArmorSlot.CHESTPLATE, this);
        this.leggings = new SuitType(getLeggingsItem(), ArmorSlot.LEGGINGS, this);
        this.boots = new SuitType(getBootsItem(), ArmorSlot.BOOTS, this);
        if (CosmeticType.GENERATE_MISSING_MESSAGES) {
            addDefaultStrings(configName);
        }
    }

    public String getConfigName() {
        return configName;
    }

    public String getConfigPath() {
        return helmet.getConfigPath();
    }

    public Class<? extends Suit> getSuitClass() {
        return clazz;
    }

    public SuitType getHelmet() {
        return helmet;
    }

    public SuitType getChestplate() {
        return chestplate;
    }

    public SuitType getLeggings() {
        return leggings;
    }

    public SuitType getBoots() {
        return boots;
    }

    protected ItemStack getHelmetItem() {
        return helmetItem;
    }

    protected ItemStack getChestplateItem() {
        return chestplateItem;
    }

    protected ItemStack getLeggingsItem() {
        return leggingsItem;
    }

    protected ItemStack getBootsItem() {
        return bootsItem;
    }

    public boolean isEnabled() {
        return SettingsManager.getConfig().getBoolean("Suits." + configName + ".Enabled");
    }

    public SuitType getPiece(ArmorSlot slot) {
        return switch (slot) {
            case HELMET -> getHelmet();
            case CHESTPLATE -> getChestplate();
            case LEGGINGS -> getLeggings();
            case BOOTS -> getBoots();
        };
    }

    protected ItemStack colorize(ItemStack stack, Color color) {
        if (color != null && stack.getItemMeta() instanceof LeatherArmorMeta colorMeta) {
            colorMeta.setColor(color);
            stack.setItemMeta(colorMeta);
        }
        return stack;
    }

    public List<SuitType> getPieces() {
        return Arrays.asList(getHelmet(), getChestplate(), getLeggings(), getBoots());
    }

    public void setupConfig(CustomConfiguration config, String path) {
    }

    public static List<SuitCategory> values() {
        return VALUES;
    }

    public static List<SuitCategory> enabled() {
        return values();
    }
}
