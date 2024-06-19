<<<<<<<< HEAD:v1_20_R3/src/main/java/be/isach/ultracosmetics/v1_20_R3/mount/MountCustomEntity.java
package be.isach.ultracosmetics.v1_20_R3.mount;
========
package be.isach.ultracosmetics.v1_20_R4.mount;
>>>>>>>> upstream/master:v1_20_R4/src/main/java/be/isach/ultracosmetics/v1_20_R4/mount/MountCustomEntity.java

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.mounts.Mount;
import be.isach.ultracosmetics.cosmetics.type.MountType;
import be.isach.ultracosmetics.player.UltraPlayer;
<<<<<<<< HEAD:v1_20_R3/src/main/java/be/isach/ultracosmetics/v1_20_R3/mount/MountCustomEntity.java
import be.isach.ultracosmetics.v1_20_R3.customentities.CustomEntities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity;
========
import be.isach.ultracosmetics.v1_20_R4.customentities.CustomEntities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftEntity;
>>>>>>>> upstream/master:v1_20_R4/src/main/java/be/isach/ultracosmetics/v1_20_R4/mount/MountCustomEntity.java

/**
 * @author RadBuilder
 */
public abstract class MountCustomEntity extends Mount {

    public MountCustomEntity(UltraPlayer owner, MountType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @Override
    public org.bukkit.entity.Entity spawnEntity() {
        entity = CustomEntities.spawnEntity(getNewEntity(), getPlayer().getLocation());
        // must refer to entity as a LivingEntity
        ((LivingEntity) getCustomEntity()).setSpeed((float) getType().getMovementSpeed());
        return getEntity();
    }

    @Override
    protected void removeEntity() {
        CustomEntities.removeCustomEntity(getCustomEntity());
    }

    public Entity getCustomEntity() {
        return ((CraftEntity) entity).getHandle();
    }

    public abstract Entity getNewEntity();
}
