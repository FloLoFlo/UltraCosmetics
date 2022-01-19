package be.isach.ultracosmetics.cosmetics.mounts.abstracthorse;

import be.isach.ultracosmetics.cosmetics.mounts.Mount;
import be.isach.ultracosmetics.version.IMounts;
import org.bukkit.entity.EntityType;

/**
 * @author RadBuilder
 */
public class AbstractHorseMounts implements IMounts {
    @Override
    public Class<? extends Mount> getHorrorClass() {
        return MountInfernalHorror.class;
    }

    @Override
    public Class<? extends Mount> getWalkingDeadClass() {
        return MountWalkingDead.class;
    }

    @Override
    public Class<? extends Mount> getRudolphClass() {
        return MountRudolph.class;
    }

    @Override
    public EntityType getHorrorType() {
        return EntityType.SKELETON_HORSE;
    }

    @Override
    public EntityType getWalkingDeadType() {
        return EntityType.ZOMBIE_HORSE;
    }

    @Override
    public EntityType getRudolphType() {
        return EntityType.MULE;
    }
}