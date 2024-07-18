package be.isach.ultracosmetics.version;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.util.Vector;

import java.util.function.Predicate;

/**
 * Created by Sacha on 14/03/16.
 */
public interface IEntityUtil {

    void resetWitherSize(Wither wither);

    void sendBlizzard(final Player player, Location loc, Predicate<Entity> canAffectFunc, Vector v);

    void clearBlizzard(final Player player);

    void sendDestroyPacket(Player player, Entity entity);

    void setStepHeight(Entity entity);

    void sendTeleportPacket(Player player, Entity entity);
}
