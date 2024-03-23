package be.isach.ultracosmetics.v1_20_R3.pets;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.PetType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.Particles;
import be.isach.ultracosmetics.v1_20_R3.customentities.Pumpling;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

/**
 * @author RadBuilder
 */
public class PetPumpling extends CustomEntityPet {
    public PetPumpling(UltraPlayer owner, PetType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @Override
    public LivingEntity getNewEntity() {
        return new Pumpling(EntityType.ZOMBIE, ((CraftPlayer) getPlayer()).getHandle().level());
    }

    @Override
    public void setupEntity() {
        // use API when we can
        Zombie zombie = (Zombie) getEntity();
        zombie.setBaby();
        zombie.getEquipment().setHelmet(new ItemStack(Material.CARVED_PUMPKIN));
    }

    @Override
    public void onUpdate() {
        // Is this necesssary?
        getNMSEntity().setRemainingFireTicks(0);
        Particles.FLAME.display(0.2f, 0.2f, 0.2f, ((Zombie) getEntity()).getEyeLocation(), 3);
        // this doesn't seem to work when just in setupEntity()
        getNMSEntity().setInvisible(true);
    }
}
