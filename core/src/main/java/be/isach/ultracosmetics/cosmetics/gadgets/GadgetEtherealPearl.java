package be.isach.ultracosmetics.cosmetics.gadgets;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.Updatable;
import be.isach.ultracosmetics.cosmetics.type.GadgetType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.EntitySpawner;
import be.isach.ultracosmetics.util.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

/**
 * Represents an instance of a ethereal pearl gadget summoned by a player.
 *
 * @author iSach
 * @since 08-03-2015
 */
public class GadgetEtherealPearl extends Gadget implements Updatable {
    private static final Color PRIMARY_EFFECT = Color.fromRGB(100, 0, 100);
    private static final Color SECONDARY_EFFECT = Color.fromRGB(30, 0, 30);
    private EnderPearl pearl;
    private boolean running;
    private Location lastLoc = null;

    public GadgetEtherealPearl(UltraPlayer owner, GadgetType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
        running = false;
    }

    @Override
    public void onClear() {
        if (pearl != null) {
            pearl.remove();
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onRightClick() {
        getOwner().removeCosmetic(Category.MOUNTS);

        Entity vehicle = getPlayer().getVehicle();
        if (vehicle instanceof EnderPearl) {
            getPlayer().eject();
            vehicle.remove();
        }

        pearl = getPlayer().launchProjectile(EnderPearl.class);
        pearl.setVelocity(getPlayer().getEyeLocation().getDirection().multiply(1.53d));
        getPlayer().teleport(getPlayer().getLocation().add(0, 5, 0));
        // Teleportation can cause the pearl to hit the player in the same tick
        if (pearl == null) return;
        if (!pearl.setPassenger(getPlayer())) {
            pearl.remove();
            return;
        }
        if (!getPlayer().getAllowFlight()) {
            getPlayer().setAllowFlight(true);
        }
        running = true;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (running && event.getCause() == DamageCause.FALL && event.getEntity() == getPlayer()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDismount(EntityDismountEvent event) {
        if (pearl == null) return;
        if (event.getEntity() != getPlayer()) return;
        if (UltraCosmeticsData.get().getServerVersion() == ServerVersion.v1_8) {
            // 1.8.8 has weird timing problems with dismounting
            Bukkit.getScheduler().runTask(getUltraCosmetics(), this::endRide);
        } else {
            endRide();
        }
    }

    @EventHandler
    public void onItemFrameBreak(HangingBreakByEntityEvent event) {
        if (event.getRemover() == pearl) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        // Player is (at least sometimes) blamed for the pearl hitting a painting
        if (event.getEntity() == pearl || event.getEntity() == getPlayer()) {
            event.getEntity().remove();
            pearl = null;
        }
    }

    private void endRide() {
        if (getPlayer().getGameMode() != GameMode.CREATIVE) {
            getPlayer().setAllowFlight(false);
        }

        // Remove the pearl before we attempt to teleport or we get stuck in a loop on 1.8.8
        if (pearl != null) {
            pearl.remove();
            pearl = null;
        }

        // Don't get stuck in the ground or in a wall
        if (lastLoc != null) {
            getPlayer().teleport(lastLoc.add(0, 1, 0));
        }
        EntitySpawner.spawnFireworks(getPlayer().getLocation(), PRIMARY_EFFECT, SECONDARY_EFFECT);
        running = false;
    }

    @Override
    public void onUpdate() {
        if (running && (pearl == null || !pearl.isValid())) {
            endRide();
        } else {
            lastLoc = getPlayer().getLocation();
        }
    }
}
