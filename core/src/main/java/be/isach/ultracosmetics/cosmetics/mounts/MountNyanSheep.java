package be.isach.ultracosmetics.cosmetics.mounts;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.MountType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.Particles;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.Sheep;
import org.bukkit.util.Vector;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import me.gamercoder215.mobchip.EntityBrain;
import me.gamercoder215.mobchip.bukkit.BukkitBrain;

/**
 * Represents an instance of a nyansheep mount.
 *
 * @author iSach
 * @since 08-17-2015
 */
public class MountNyanSheep extends Mount {
    private static final List<Color> COLORS = new ArrayList<>();
    private EntityBrain brain;

    static {
        COLORS.add(new Color(255, 0, 0));
        COLORS.add(new Color(255, 165, 0));
        COLORS.add(new Color(255, 255, 0));
        COLORS.add(new Color(154, 205, 50));
        COLORS.add(new Color(30, 144, 255));
        COLORS.add(new Color(148, 0, 211));
    }

    public MountNyanSheep(UltraPlayer owner, MountType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @Override
    public void setupEntity() {
        Sheep sheep = (Sheep) entity;
        sheep.setNoDamageTicks(Integer.MAX_VALUE);
        brain = BukkitBrain.getBrain(sheep);
        brain.getGoalAI().clear();
        brain.getTargetAI().clear();
    }

    @Override
    public void onUpdate() {
        move();

        ((Sheep) entity).setColor(DyeColor.values()[RANDOM.nextInt(16)]);

        Location particleLoc = entity.getLocation().add(entity.getLocation().getDirection().normalize().multiply(-2)).add(0, 1.2, 0);
        for (Color rgbColor : COLORS) {
            for (int i = 0; i < 10; i++) {
                Particles.DUST.display(rgbColor.getRed(), rgbColor.getGreen(), rgbColor.getBlue(), particleLoc);
            }
            particleLoc.subtract(0, 0.2, 0);
        }
    }

    private void move() {
        Location playerLoc = getPlayer().getLocation();
        Vector vel = playerLoc.getDirection().setY(0).normalize().multiply(4);
        playerLoc.add(vel);

        brain.getController().moveTo(playerLoc, 2);
    }
}
