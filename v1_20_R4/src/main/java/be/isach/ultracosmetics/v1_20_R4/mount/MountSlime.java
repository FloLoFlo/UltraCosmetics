<<<<<<<< HEAD:v1_20_R3/src/main/java/be/isach/ultracosmetics/v1_20_R3/mount/MountSlime.java
package be.isach.ultracosmetics.v1_20_R3.mount;
========
package be.isach.ultracosmetics.v1_20_R4.mount;
>>>>>>>> upstream/master:v1_20_R4/src/main/java/be/isach/ultracosmetics/v1_20_R4/mount/MountSlime.java

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.MountType;
import be.isach.ultracosmetics.player.UltraPlayer;
<<<<<<<< HEAD:v1_20_R3/src/main/java/be/isach/ultracosmetics/v1_20_R3/mount/MountSlime.java
import be.isach.ultracosmetics.v1_20_R3.customentities.CustomSlime;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
========
import be.isach.ultracosmetics.v1_20_R4.customentities.CustomSlime;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
>>>>>>>> upstream/master:v1_20_R4/src/main/java/be/isach/ultracosmetics/v1_20_R4/mount/MountSlime.java

/**
 * @author RadBuilder
 */
public class MountSlime extends MountCustomEntity {

    public MountSlime(UltraPlayer owner, MountType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public LivingEntity getNewEntity() {
        return new CustomSlime(EntityType.SLIME, ((CraftPlayer) getPlayer()).getHandle().level());
    }
}
