package be.isach.ultracosmetics.cosmetics.particleeffects;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.ParticleEffectType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.MathUtils;
import com.cryptomorin.xseries.particles.ParticleDisplay;
import com.cryptomorin.xseries.particles.XParticle;
import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * Represents an instance of flame fairy particles summoned by a player.
 *
 * @author iSach
 * @since 12-23-2015
 */
public class ParticleEffectFlameFairy extends ParticleEffect {
    private final ParticleDisplay lavaDisplay = ParticleDisplay.of(XParticle.LAVA);
    private final ParticleDisplay flameDisplay = ParticleDisplay.of(XParticle.FLAME);
    private Vector targetDirection = new Vector(1, 0, 0);

    private Location currentLocation, targetLocation;

    private double noMoveTime = 0;
    private double movementSpeed = 0.2d;

    public ParticleEffectFlameFairy(UltraPlayer owner, ParticleEffectType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);

        currentLocation = getPlayer().getLocation();
        targetLocation = generateNewTarget();
        lavaDisplay.withLocationCaller(() -> currentLocation);
        flameDisplay.withLocationCaller(() -> currentLocation);
    }

    @Override
    public void onUpdate() {
        if (getPlayer().getWorld() != currentLocation.getWorld()
                || getPlayer().getWorld() != targetLocation.getWorld()) {
            currentLocation = getPlayer().getLocation();
            targetLocation = generateNewTarget();
        }

        double distanceBtw = getPlayer().getEyeLocation().distance(currentLocation);
        double distTarget = currentLocation.distanceSquared(targetLocation);

        if (distTarget < 1 || distanceBtw > 3) {
            targetLocation = generateNewTarget();
            distTarget = currentLocation.distanceSquared(targetLocation);
        }

        if (RANDOM.nextDouble() > 0.98) {
            noMoveTime = System.currentTimeMillis() + MathUtils.randomDouble(0, 2000);
        }

        if (getPlayer().getEyeLocation().distanceSquared(currentLocation) < 3 * 3) {
            movementSpeed = noMoveTime > System.currentTimeMillis() ? Math.max(0, movementSpeed - 0.0075)
                    : Math.min(0.1, movementSpeed + 0.0075);
        } else {
            noMoveTime = 0;
            movementSpeed = Math.min(0.15 + distanceBtw * 0.05, movementSpeed + 0.02);
        }

        targetDirection.add(targetLocation.toVector().subtract(currentLocation.toVector()).multiply(0.2));

        if (targetDirection.length() < 1) {
            movementSpeed *= targetDirection.length();
        }

        targetDirection = targetDirection.normalize();

        if (distTarget > 0.1 * 0.1) {
            currentLocation.add(targetDirection.clone().multiply(movementSpeed));
        }

        lavaDisplay.spawn();
        flameDisplay.spawn();
    }

    private Location generateNewTarget() {
        return getPlayer().getEyeLocation()
                .add(RANDOM.nextInt(6) - 3,
                        RANDOM.nextDouble() * 1.5,
                        RANDOM.nextInt(6) - 3);
    }
}
