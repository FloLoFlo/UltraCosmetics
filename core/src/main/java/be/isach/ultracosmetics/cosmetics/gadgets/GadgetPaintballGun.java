package be.isach.ultracosmetics.cosmetics.gadgets;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.type.GadgetType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.BlockUtils;
import be.isach.ultracosmetics.util.Particles;
import be.isach.ultracosmetics.util.SmartLogger.LogLevel;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents an instance of a paintball gun gadget summoned by a player.
 *
 * @author iSach
 * @since 08-03-2015
 */
public class GadgetPaintballGun extends Gadget {

    private static final List<XMaterial> PAINT_BLOCKS = new ArrayList<>();

    static {
        String ending = SettingsManager.getConfig().getString("Gadgets.PaintballGun.Block-Type", "_TERRACOTTA").toUpperCase();
        for (XMaterial mat : XMaterial.VALUES) {
            if (mat.isSupported() && mat.name().endsWith(ending)) {
                PAINT_BLOCKS.add(mat);
            }
        }
        if (PAINT_BLOCKS.isEmpty()) {
            UltraCosmeticsData.get().getPlugin().getSmartLogger().write(LogLevel.ERROR, "Paintball Gun setting 'Block-Type' does not match any known blocks.");
            PAINT_BLOCKS.add(XMaterial.BEDROCK);
        }
    }

    private final Set<Projectile> projectiles = new HashSet<>();
    private final int radius;
    private final Particles effect;
    private final int particleCount;

    public GadgetPaintballGun(UltraPlayer owner, GadgetType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
        radius = SettingsManager.getConfig().getInt(getOptionPath("Radius"), 2);
        displayCooldownMessage = false;
        if (!SettingsManager.getConfig().getBoolean(getOptionPath("Particle.Enabled"))) {
            particleCount = 0;
            effect = null;
            return;
        }
        Particles effect = null;
        try {
            effect = Particles.valueOf(SettingsManager.getConfig().getString(getOptionPath("Particle.Effect")));
        } catch (IllegalArgumentException ignored) {
            this.particleCount = 0;
            this.effect = null;
            return;
        }
        this.effect = effect;
        particleCount = SettingsManager.getConfig().getInt(getOptionPath("Particle.Count"), 50);
    }

    @Override
    protected void onRightClick() {
        Projectile projectile = getPlayer().launchProjectile(EnderPearl.class, getPlayer().getLocation().getDirection().multiply(2));
        projectiles.add(projectile);
        play(XSound.ENTITY_CHICKEN_EGG, getPlayer(), 1.5f, 1.2f);
    }

    @EventHandler
    public void onItemFrameBreak(HangingBreakByEntityEvent event) {
        if (projectiles.contains(event.getRemover())) {
            event.setCancelled(true);
            // TODO: do we really want to prevent players from breaking hanging things while this gadget is equipped??
            // or is this required to prevent ender pearls from breaking things?
        } else if (event.getRemover() == getPlayer()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!projectiles.remove(event.getEntity())) return;

        Location center = event.getEntity().getLocation().add(event.getEntity().getVelocity());
        Map<Block, XMaterial> updates = new HashMap<>();
        for (Block block : BlockUtils.getBlocksInRadius(center.getBlock().getLocation(), radius, false)) {
            updates.put(block, PAINT_BLOCKS.get(RANDOM.nextInt(PAINT_BLOCKS.size())));
        }
        BlockUtils.setToRestore(updates, 20 * 3);
        if (particleCount > 0) {
            effect.display(2.5, 0.2f, 2.5f, center.clone().add(0.5f, 1.2f, 0.5F), particleCount);
        }
        event.getEntity().remove();
        try {
            // ProjectileHitEvent is only cancellable 1.16.5 and up
            event.setCancelled(true);
        } catch (NoSuchMethodError ignored) {
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() != EntityType.ENDER_PEARL) return;
        if (projectiles.contains(event.getDamager())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityTeleport(PlayerTeleportEvent event) {
        if (event.getPlayer() == getPlayer() && event.getCause() == TeleportCause.ENDER_PEARL) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onClear() {
        for (Projectile projectile : projectiles) {
            projectile.remove();
        }
        projectiles.clear();
    }
}
