<<<<<<<< HEAD:v1_20_R3/src/main/java/be/isach/ultracosmetics/v1_20_R3/pets/CustomEntityPet.java
package be.isach.ultracosmetics.v1_20_R3.pets;
========
package be.isach.ultracosmetics.v1_20_R4.pets;
>>>>>>>> upstream/master:v1_20_R4/src/main/java/be/isach/ultracosmetics/v1_20_R4/pets/CustomEntityPet.java

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.pets.Pet;
import be.isach.ultracosmetics.cosmetics.type.PetType;
import be.isach.ultracosmetics.player.UltraPlayer;
<<<<<<<< HEAD:v1_20_R3/src/main/java/be/isach/ultracosmetics/v1_20_R3/pets/CustomEntityPet.java
import be.isach.ultracosmetics.v1_20_R3.customentities.CustomEntities;
import net.minecraft.world.entity.Entity;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity;
========
import be.isach.ultracosmetics.v1_20_R4.customentities.CustomEntities;
import net.minecraft.world.entity.Entity;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftEntity;
>>>>>>>> upstream/master:v1_20_R4/src/main/java/be/isach/ultracosmetics/v1_20_R4/pets/CustomEntityPet.java
import org.bukkit.entity.Mob;

/**
 * @author RadBuilder
 */
public abstract class CustomEntityPet extends Pet {

    public CustomEntityPet(UltraPlayer owner, PetType petType, UltraCosmetics ultraCosmetics) {
        super(owner, petType, ultraCosmetics);
    }

    @Override
    public Mob spawnEntity() {
        entity = (Mob) CustomEntities.spawnEntity(getNewEntity(), getPlayer().getLocation());
        return entity;
    }

    @Override
    protected void removeEntity() {
        CustomEntities.removeCustomEntity(getNMSEntity());
    }

    @Override
    public boolean isCustomEntity() {
        return true;
    }

    public Entity getNMSEntity() {
        return ((CraftEntity) entity).getHandle();
    }

    public abstract Entity getNewEntity();
}
