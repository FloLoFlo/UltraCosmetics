<<<<<<<< HEAD:v1_20_R3/src/main/java/be/isach/ultracosmetics/v1_20_R3/customentities/CustomGuardian.java
package be.isach.ultracosmetics.v1_20_R3.customentities;
========
package be.isach.ultracosmetics.v1_20_R4.customentities;
>>>>>>>> upstream/master:v1_20_R4/src/main/java/be/isach/ultracosmetics/v1_20_R4/customentities/CustomGuardian.java

import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.level.Level;
<<<<<<<< HEAD:v1_20_R3/src/main/java/be/isach/ultracosmetics/v1_20_R3/customentities/CustomGuardian.java
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftArmorStand;
========
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftArmorStand;
>>>>>>>> upstream/master:v1_20_R4/src/main/java/be/isach/ultracosmetics/v1_20_R4/customentities/CustomGuardian.java
import org.bukkit.entity.ArmorStand;

/**
 * @author RadBuilder
 */
public class CustomGuardian extends Guardian {

    public CustomGuardian(EntityType<? extends Guardian> entitytypes, Level world) {
        super(entitytypes, world);
    }

    private boolean isCustom() {
        return CustomEntities.isCustomEntity(this);
    }

    public void target(ArmorStand armorStand) {
        ((Entity) this).getEntityData().set(EntityDataSerializers.INT.createAccessor(17), armorStand == null ? 0 : ((CraftArmorStand) armorStand).getHandle().getId());
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (isCustom()) {
            return null;
        } else {
            return super.getAmbientSound();
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
        if (isCustom()) {
            return null;
        } else {
            return super.getHurtSound(paramDamageSource);
        }
    }

    @Override
    public Component getName() {
        // TODO: translatable component?
        return CustomEntities.toComponent(Language.getInstance().getOrDefault("entity.minecraft.guardian"));
    }

    @Override
    protected SoundEvent getDeathSound() {
        if (isCustom()) {
            return null;
        } else {
            return super.getDeathSound();
        }
    }

    @Override
    public void tick() {
        if (!isCustom()) {
            super.tick();
        } else {
            ((LivingEntity) this).setHealth(getMaxHealth());
        }
    }
}
