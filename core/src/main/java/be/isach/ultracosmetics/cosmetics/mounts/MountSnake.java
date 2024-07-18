package be.isach.ultracosmetics.cosmetics.mounts;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.MountType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.MathUtils;
import me.gamercoder215.mobchip.bukkit.BukkitBrain;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an instance of a snake mount.
 *
 * @author iSach
 * @since 08-10-2015
 */
public class MountSnake extends Mount {

    private final List<Creature> tail = new ArrayList<>();
    private int color = 1;

    public MountSnake(UltraPlayer owner, MountType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @Override
    public void setupEntity() {
        color = MathUtils.randomRangeInt(0, 14);
        Sheep sheep = (Sheep) entity;
        sheep.setNoDamageTicks(Integer.MAX_VALUE);
        sheep.setColor(DyeColor.values()[color]);
        tail.add(sheep);
        addSheepToTail(4);
    }

    @Override
    public void onClear() {
        super.onClear();
        for (Entity ent : tail) {
            ent.remove();
        }
        tail.clear();
    }

    @Override
    public void onUpdate() {
        if (getPlayer() == null) return;
        Vector vel = getPlayer().getLocation().getDirection().setY(0).normalize().multiply(16);

        Creature before = null;
        for (int i = 0; i < tail.size(); i++) {
            Creature tailEnt = tail.get(i);
            Location loc;
            if (before == null) {
                loc = tailEnt.getLocation().add(vel);
            } else {
                loc = before.getLocation();
                Location tp = before.getLocation().add(traj(before, tailEnt).multiply(1.4D));
                tp.setPitch(tailEnt.getLocation().getPitch());
                tp.setYaw(tailEnt.getLocation().getYaw());
                tailEnt.teleport(tp);
            }

            BukkitBrain.getBrain(tailEnt).getController().moveTo(loc);

            before = tailEnt;
        }
    }

    public Vector traj(Location a, Location b) {
        return b.toVector().subtract(a.toVector()).setY(0).normalize();
    }

    public Vector traj(Entity a, Entity b) {
        return traj(a.getLocation(), b.getLocation());
    }

    public void addSheepToTail(int amount) {
        Player player = getPlayer();
        for (int i = 0; i < amount; i++) {
            Location loc = player.getLocation();
            if (!tail.isEmpty()) {
                loc = lastTail().getLocation();
            }
            if (tail.size() > 1) {
                loc.add(traj(tail.get(tail.size() - 2), lastTail()));
            } else {
                loc.subtract(player.getLocation().getDirection().setY(0));
            }
            Sheep tailEnt = loc.getWorld().spawn(loc, Sheep.class);
            tailEnt.setNoDamageTicks(Integer.MAX_VALUE);
            tailEnt.setRemoveWhenFarAway(false);
            tail.add(tailEnt);
            tailEnt.setColor(DyeColor.values()[color]);
            tailEnt.setPersistent(false);
        }
    }

    @EventHandler
    public void onSnakeDamage(EntityDamageEvent event) {
        if (tail.contains(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    private Creature lastTail() {
        return tail.get(tail.size() - 1);
    }
}
