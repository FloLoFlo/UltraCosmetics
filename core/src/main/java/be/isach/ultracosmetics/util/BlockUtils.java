package be.isach.ultracosmetics.util;

import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.util.SmartLogger.LogLevel;
import be.isach.ultracosmetics.version.VersionManager;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XTag;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

/**
 * Created by sacha on 03/08/15.
 */
public class BlockUtils {

    /**
     * Set containing all materials that shouldn't
     * be used with player.sendBlockChange
     */
    private static final Set<Material> badMaterials = new HashSet<>();
    private static final Set<Material> AIRS = new HashSet<>();

    static {
        for (String name : SettingsManager.getConfig().getStringList("Air-Materials")) {
            Optional<XMaterial> mat = XMaterial.matchXMaterial(name);
            if (!mat.isPresent()) {
                UltraCosmeticsData.get().getPlugin().getSmartLogger().write(LogLevel.WARNING, "Failed to parse 'Air-Materials' item: " + name);
                continue;
            }
            Material parsed = mat.get().parseMaterial();
            // silently ignore materials that are valid
            // but not present in this MC version
            if (parsed != null) {
                AIRS.add(parsed);
            }
        }
        Set<XMaterial> badXMaterials = new HashSet<>();
        badXMaterials.add(XMaterial.CHEST);
        badXMaterials.add(XMaterial.ENDER_CHEST);
        badXMaterials.add(XMaterial.WHEAT);
        badXMaterials.add(XMaterial.SHORT_GRASS);
        badXMaterials.add(XMaterial.DEAD_BUSH);
        badXMaterials.add(XMaterial.POPPY);
        badXMaterials.add(XMaterial.RED_MUSHROOM);
        badXMaterials.add(XMaterial.BROWN_MUSHROOM);
        badXMaterials.add(XMaterial.TORCH);
        badXMaterials.add(XMaterial.LADDER);
        badXMaterials.add(XMaterial.VINE);
        badXMaterials.add(XMaterial.LARGE_FERN);
        badXMaterials.add(XMaterial.LILAC);
        badXMaterials.add(XMaterial.PEONY);
        badXMaterials.add(XMaterial.ROSE_BUSH);
        badXMaterials.add(XMaterial.SUNFLOWER);
        badXMaterials.add(XMaterial.NETHER_PORTAL);
        badXMaterials.add(XMaterial.CACTUS);
        // I don't think we need to check FLOWING_ variants separately
        badXMaterials.add(XMaterial.WATER);
        badXMaterials.add(XMaterial.LAVA);
        badXMaterials.add(XMaterial.END_PORTAL);
        badXMaterials.add(XMaterial.FARMLAND);
        badXMaterials.add(XMaterial.BARRIER);
        badXMaterials.add(XMaterial.COMMAND_BLOCK);
        badXMaterials.add(XMaterial.DROPPER);
        badXMaterials.add(XMaterial.DISPENSER);

        badXMaterials.addAll(XTag.WOODEN_SLABS.getValues());
        badXMaterials.addAll(XTag.NON_WOODEN_SLABS.getValues());
        badXMaterials.addAll(XTag.DOORS.getValues());
        badXMaterials.addAll(XTag.SIGNS.getValues());
        badXMaterials.addAll(XTag.BEDS.getValues());
        badXMaterials.addAll(XTag.PRESSURE_PLATES.getValues());
        badXMaterials.addAll(XTag.BANNERS.getValues());
        badXMaterials.addAll(XTag.SAPLINGS.getValues());

        for (XMaterial mat : badXMaterials) {
            badMaterials.add(mat.parseMaterial());
        }
        badMaterials.addAll(AIRS);
    }

    /**
     * Gets BLOCKS in radius.
     *
     * @param location The center.
     * @param radius   The radius.
     * @param hollow   if the sphere of BLOCKS should be hollow.
     * @return The list of all the BLOCKS in the given radius.
     */
    public static List<Block> getBlocksInRadius(Location location, int radius, boolean hollow) {
        List<Block> blocks = new ArrayList<>();
        int bX = location.getBlockX(),
                bY = location.getBlockY(),
                bZ = location.getBlockZ();
        for (int x = bX - radius; x <= bX + radius; x++) {
            for (int y = bY - radius; y <= bY + radius; y++) {
                for (int z = bZ - radius; z <= bZ + radius; z++) {
                    double distance = ((bX - x) * (bX - x) + (bY - y) * (bY - y) + (bZ - z) * (bZ - z));
                    if (distance < radius * radius
                            && !(hollow && distance < ((radius - 1) * (radius - 1)))) {
                        Location l = new Location(location.getWorld(), x, y, z);
                        if (l.getBlock().getType() != Material.BARRIER) {
                            blocks.add(l.getBlock());
                        }
                    }
                }
            }
        }
        return blocks;
    }

    /**
     * Checks if an entity is on ground.
     *
     * @param entity The entity to check.
     * @return {@code true} if entity is on ground, otherwise {@code false}.
     */
    public static boolean isOnGround(Entity entity) {
        Block block = entity.getLocation().getBlock().getRelative(BlockFace.DOWN);
        return block.getType().isSolid();
    }

    /**
     * Sends packets to replace each block in a Map of blocks with the new material it's mapped to
     * and after delay, send packets to restore it. No blocks are actually changed on the server side.
     *
     * @param blocks    The blocks to restore mapped to the materials to change them to.
     * @param tickDelay The delay after which the blocks are restored.
     */
    @SuppressWarnings("deprecation")
    public static void setToRestoreIgnoring(final Map<Block, XMaterial> blocks, final int tickDelay) {
        Bukkit.getScheduler().runTaskAsynchronously(UltraCosmeticsData.get().getPlugin(), () -> {
            blocks.keySet().removeIf(BlockViewUpdater::isUpdating);
            if (blocks.size() == 0) return;
            World world = blocks.keySet().iterator().next().getWorld();
            for (Entry<Block, XMaterial> entry : blocks.entrySet()) {
                for (Player player : world.getPlayers()) {
                    if (VersionManager.IS_VERSION_1_13) {
                        // we have to do this when we can or we enable legacy material support which is evil sometimes
                        player.sendBlockChange(entry.getKey().getLocation(), Bukkit.createBlockData(entry.getValue().parseMaterial()));
                    } else {
                        player.sendBlockChange(entry.getKey().getLocation(), entry.getValue().parseMaterial(), entry.getValue().getData());
                    }
                }
            }
            new BlockViewUpdater(blocks.keySet()).runTaskLaterAsynchronously(UltraCosmeticsData.get().getPlugin(), tickDelay);
        });
    }

    public static void setToRestore(final Map<Block, XMaterial> blocks, final int tickDelay) {
        blocks.keySet().removeIf(b -> !canRestore(b));
        setToRestoreIgnoring(blocks, tickDelay);
    }

    public static boolean canRestore(Block block) {
        if (badMaterials.contains(block.getType())
                || SettingsManager.getConfig().getStringList("Gadgets.PaintballGun.BlackList").contains(block.getType().name())
                || isPortalBlock(block)
                || StructureRollback.isBlockRollingBack(block)
                || !block.getType().isSolid()
                || !okAboveBlock(block.getRelative(BlockFace.UP).getType())) {
            return false;
        }
        return true;
    }

    private static boolean okAboveBlock(Material mat) {
        return isAir(mat) || mat.isSolid();
    }

    /**
     * Checks if a block is part of a Nether Portal.
     *
     * @param b The block to check
     * @return {@code true} if a block is part of a Nether Portal, otherwise {@code false}.
     */
    public static boolean isPortalBlock(Block b) {
        for (BlockFace face : BlockFace.values()) {
            if (b.getRelative(face).getType() == XMaterial.NETHER_PORTAL.parseMaterial()) {
                return true;
            }
        }
        return false;
    }

    // Returns true if mat should not be used with player.sendBlockChange per badMaterials Set
    public static boolean isBadMaterial(Material mat) {
        return badMaterials.contains(mat);
    }

    public static boolean isAir(Material mat) {
        return AIRS.contains(mat);
    }

    @SuppressWarnings("deprecation")
    public static FallingBlock spawnFallingBlock(Location loc, Material type) {
        if (VersionManager.IS_VERSION_1_13) {
            return loc.getWorld().spawnFallingBlock(loc, type.createBlockData());
        } else {
            return loc.getWorld().spawnFallingBlock(loc, type, (byte) 0);
        }
    }

    @SuppressWarnings("deprecation")
    public static FallingBlock spawnFallingBlock(Location loc, Block source) {
        if (VersionManager.IS_VERSION_1_13) {
            return loc.getWorld().spawnFallingBlock(loc, source.getBlockData());
        } else {
            return loc.getWorld().spawnFallingBlock(loc, source.getType(), source.getData());
        }
    }
}
