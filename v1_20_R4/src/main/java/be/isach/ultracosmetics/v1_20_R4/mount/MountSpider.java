<<<<<<<< HEAD:v1_20_R3/src/main/java/be/isach/ultracosmetics/v1_20_R3/mount/MountSpider.java
package be.isach.ultracosmetics.v1_20_R3.mount;
========
package be.isach.ultracosmetics.v1_20_R4.mount;
>>>>>>>> upstream/master:v1_20_R4/src/main/java/be/isach/ultracosmetics/v1_20_R4/mount/MountSpider.java

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.MountType;
import be.isach.ultracosmetics.player.UltraPlayer;
<<<<<<<< HEAD:v1_20_R3/src/main/java/be/isach/ultracosmetics/v1_20_R3/mount/MountSpider.java
import be.isach.ultracosmetics.v1_20_R3.customentities.RideableSpider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
========
import be.isach.ultracosmetics.v1_20_R4.customentities.RideableSpider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.bukkit.craftbukkit.v1_20_R4.CraftWorld;
>>>>>>>> upstream/master:v1_20_R4/src/main/java/be/isach/ultracosmetics/v1_20_R4/mount/MountSpider.java

/**
 * @author RadBuilder
 */
public class MountSpider extends MountCustomEntity {
    public MountSpider(UltraPlayer owner, MountType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public LivingEntity getNewEntity() {
        return new RideableSpider(EntityType.SPIDER, ((CraftWorld) getPlayer().getWorld()).getHandle());
    }
}
