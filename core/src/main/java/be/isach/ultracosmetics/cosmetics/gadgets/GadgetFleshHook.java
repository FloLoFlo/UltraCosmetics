package be.isach.ultracosmetics.cosmetics.gadgets;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.PlayerAffectingCosmetic;
import be.isach.ultracosmetics.cosmetics.Updatable;
import be.isach.ultracosmetics.cosmetics.type.GadgetType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.ItemFactory;
import be.isach.ultracosmetics.util.MathUtils;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Represents an instance of a fleshhook gadget summoned by a player.
 *
 * @author iSach
 * @since 08-03-2015
 */
public class GadgetFleshHook extends Gadget implements PlayerAffectingCosmetic, Updatable {

    private final Set<Item> active = new HashSet<>();
    private final Set<Item> forRemoval = new HashSet<>();

    public GadgetFleshHook(UltraPlayer owner, GadgetType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onItemPickup(org.bukkit.event.player.PlayerPickupItemEvent event) {
        if (!active.contains(event.getItem()) && !forRemoval.contains(event.getItem())) return;
        event.setCancelled(true);

        UltraPlayer ultraPlayer = getUltraCosmetics().getPlayerManager().getUltraPlayer(event.getPlayer());

        if (ultraPlayer == null) return;

        Player hitter = getPlayer();
        if (event.getPlayer() == hitter || !canAffect(event.getPlayer(), hitter)) return;

        event.getItem().remove();
        active.remove(event.getItem());
        forRemoval.remove(event.getItem());
        final Player HIT = event.getPlayer();
        HIT.playEffect(EntityEffect.HURT);
        double dX = HIT.getLocation().getX() - hitter.getLocation().getX();
        double dY = HIT.getLocation().getY() - hitter.getLocation().getY();
        double dZ = HIT.getLocation().getZ() - hitter.getLocation().getZ();
        double yaw = Math.atan2(dZ, dX);
        double pitch = Math.atan2(Math.sqrt(dZ * dZ + dX * dX), dY) + Math.PI;
        double X = Math.sin(pitch) * Math.cos(yaw);
        double Y = Math.sin(pitch) * Math.sin(yaw);
        double Z = Math.cos(pitch);

        Vector vector = new Vector(X, Z, Y);
        MathUtils.applyVelocity(HIT, vector.multiply(2.5D).add(new Vector(0D, 1.45D, 0D)));
    }

    @Override
    protected void onRightClick() {
        Item item = ItemFactory.createUnpickableItemDirectional(XMaterial.TRIPWIRE_HOOK, getPlayer(), 1.5);
        item.setPickupDelay(0);
        active.add(item);
    }

    @Override
    public void onUpdate() {
        Iterator<Item> it = active.iterator();
        Set<Item> toRemove = new HashSet<>();
        while (it.hasNext()) {
            Item pair = it.next();
            if (pair.isOnGround()) {
                pair.remove();
                toRemove.add(pair);
                forRemoval.add(pair);
            }
        }
        if (!toRemove.isEmpty()) {
            Bukkit.getScheduler().runTaskLater(getUltraCosmetics(), () -> {
                for (Item item : toRemove) {
                    item.remove();
                    forRemoval.remove(item);
                }
            }, 10);
        }
    }

    @Override
    public void onClear() {
        for (Item item : active) {
            item.remove();
        }
        active.clear();
    }
}
