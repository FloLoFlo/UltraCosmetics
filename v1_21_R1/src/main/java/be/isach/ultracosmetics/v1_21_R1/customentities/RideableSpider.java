package be.isach.ultracosmetics.v1_21_R1.customentities;

import be.isach.ultracosmetics.v1_21_R1.EntityBase;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * @author iSach
 */
public class RideableSpider extends Spider implements EntityBase {

    public RideableSpider(EntityType<? extends Spider> entitytypes, Level world) {
        super(entitytypes, world);
    }

    @Override
    public void travel(Vec3 vec3D) {
        if (!CustomEntities.isCustomEntity(this)) {
            super.tickHeadTurn((float) vec3D.x, (float) vec3D.y);
            return;
        }
        Player passenger = null;
        if (!getPassengers().isEmpty()) {
            passenger = (Player) getPassengers().get(0);
        }
        CustomEntities.ride((float) vec3D.x, (float) vec3D.y, passenger, this);
    }

    @Override
    public void travel_(float sideMot, float forMot) {
        super.travel(new Vec3(sideMot, 0, forMot));
    }

    @Override
    public Component getName() {
        return CustomEntities.toComponent(Language.getInstance().getOrDefault("entity.minecraft.spider"));
    }
}
