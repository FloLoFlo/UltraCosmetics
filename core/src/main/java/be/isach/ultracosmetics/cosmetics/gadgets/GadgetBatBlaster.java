package be.isach.ultracosmetics.cosmetics.gadgets;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.PlayerAffectingCosmetic;
import be.isach.ultracosmetics.cosmetics.Updatable;
import be.isach.ultracosmetics.cosmetics.type.GadgetType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.EntitySpawner;
import be.isach.ultracosmetics.util.MathUtils;
import com.cryptomorin.xseries.XSound;
import com.cryptomorin.xseries.particles.ParticleDisplay;
import com.cryptomorin.xseries.particles.XParticle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Bat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Iterator;

/**
 * Represents an instance of a bat blaster gadget summoned by a player.
 *
 * @author iSach
 * @since 08-03-2015
 */
public class GadgetBatBlaster extends Gadget implements PlayerAffectingCosmetic, Updatable {
    private static final ParticleDisplay PARTICLES = ParticleDisplay.of(XParticle.SMOKE);
    private boolean active = false;
    private Location playerStartLoc;
    private EntitySpawner<Bat> bats;
    private final XSound.SoundPlayer bounceSound = XSound.ENTITY_BAT_HURT.record().soundPlayer();

    public GadgetBatBlaster(UltraPlayer owner, GadgetType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @Override
    protected void onRightClick() {
        this.active = true;
        this.playerStartLoc = getPlayer().getEyeLocation();
        this.bats = new EntitySpawner<>(EntityType.BAT, getPlayer().getEyeLocation(), 16, getUltraCosmetics());

        Bukkit.getScheduler().runTaskLater(getUltraCosmetics(), this::clean, 60);
    }

    public boolean hitPlayer(Location location, Player player) {
        Vector locVec = location.add(0, -location.getY(), 0).toVector();
        Vector playerVec = player.getLocation().add(0, -player.getLocation().getY(), 0).toVector();
        double vecLength = locVec.subtract(playerVec).length();

        if (vecLength < 0.8) {
            return true;
        }

        if (vecLength < 1.2) {
            return (location.getY() > player.getLocation().getY()) && (location.getY() < player.getEyeLocation().getY());
        }

        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onUpdate() {
        if (bats == null || bats.isEmpty()) return;

        if (!active) {
            clean();
            return;
        }
        Iterator<Bat> iter = bats.getEntities().iterator();
        while (iter.hasNext()) {
            Bat bat = iter.next();
            if (!bat.isValid()) {
                continue;
            }
            Vector rand = new Vector((Math.random() - 0.5D) / 3.0D, (Math.random() - 0.5D) / 3.0D,
                    (Math.random() - 0.5D) / 3.0D);
            if (playerStartLoc != null) {
                bat.setVelocity(playerStartLoc.getDirection().clone().multiply(0.5D).add(rand));
            }
            Player player = getPlayer();
            for (Player other : player.getWorld().getPlayers()) {
                if (other == player || !canAffect(other, player) || !hitPlayer(bat.getLocation(), other)) {
                    continue;
                }
                Vector v = bat.getLocation().getDirection();
                v.normalize();
                v.multiply(0.4d);
                v.setY(v.getY() + 0.2d);

                if (v.getY() > 7.5) {
                    v.setY(7.5);
                }

                if (other.isOnGround()) {
                    v.setY(v.getY() + 0.4d);
                }

                other.setFallDistance(0);
                MathUtils.applyVelocity(other, bat.getLocation().getDirection().add(new Vector(0, 0.4f, 0)));

                bounceSound.atLocation(bat.getLocation()).play();
                PARTICLES.spawn(bat.getLocation());

                bat.remove();
                iter.remove();
                break;
            }
        }
    }

    private void clean() {
        active = false;
        playerStartLoc = null;
        if (bats != null) {
            bats.removeEntities();
        }
    }

    @Override
    public void onClear() {
        clean();
    }
}
