package be.isach.ultracosmetics.cosmetics.mounts;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.type.MountType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.run.FallDamageManager;
import me.gamercoder215.mobchip.EntityBrain;
import me.gamercoder215.mobchip.ai.controller.NaturalMoveType;
import me.gamercoder215.mobchip.bukkit.BukkitBrain;
import org.bukkit.entity.EnderDragonPart;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

/**
 * Represents an instance of a enderdragon mount.
 *
 * @author iSach
 * @since 08-17-2015
 */
public class MountDragon extends Mount {
    private EntityBrain brain;

    public MountDragon(UltraPlayer owner, MountType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @Override
    protected void setupEntity() {
        brain = BukkitBrain.getBrain((Mob) entity);
    }

    @Override
    public void onUpdate() {
        brain.getBody().setHurtTime(20);
        if (SettingsManager.getConfig().getBoolean("Mounts." + getType().getConfigName() + ".Stationary")) return;

        float yaw = getPlayer().getLocation().getYaw();
        brain.getBody().setPitch(getPlayer().getLocation().getPitch());
        brain.getBody().setYaw(yaw - 180);

        double angleInRadians = toRadians(-yaw);

        double x = sin(angleInRadians);
        double z = cos(angleInRadians);

        Vector v = entity.getLocation().getDirection();

        brain.getController().naturalMoveTo(x, v.getY(), z, NaturalMoveType.SELF);

    }

    @SuppressWarnings("deprecation")
    @Override
    public void onClear() {
        super.onClear();
        if (!getPlayer().isOnGround()) {
            FallDamageManager.addNoFall(getPlayer());
        }
    }

    @EventHandler
    public void stopDragonDamage(EntityExplodeEvent event) {
        Entity e = event.getEntity();
        if (e instanceof EnderDragonPart) e = ((EnderDragonPart) e).getParent();
        if (e == entity) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity e = event.getDamager();
        if (e instanceof EnderDragonPart) {
            e = ((EnderDragonPart) e).getParent();
        }
        if (e == entity) {
            event.setCancelled(true);
        }
    }
}
