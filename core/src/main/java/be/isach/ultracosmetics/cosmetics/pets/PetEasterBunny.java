package be.isach.ultracosmetics.cosmetics.pets;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.PetType;
import be.isach.ultracosmetics.player.UltraPlayer;

import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Rabbit.Type;

import com.cryptomorin.xseries.XMaterial;

import java.util.Arrays;
import java.util.List;

/**
 * Represents an instance of an easter bunny pet summoned by a player.
 *
 * @author iSach
 * @since 08-12-2015
 */
public class PetEasterBunny extends Pet {
    private static final List<XMaterial> EGGS = Arrays.asList(XMaterial.CREEPER_SPAWN_EGG, XMaterial.BLAZE_SPAWN_EGG, XMaterial.SQUID_SPAWN_EGG,
            XMaterial.ZOMBIE_HORSE_SPAWN_EGG, XMaterial.ENDERMAN_SPAWN_EGG, XMaterial.GHAST_SPAWN_EGG, XMaterial.OCELOT_SPAWN_EGG);

    public PetEasterBunny(UltraPlayer owner, PetType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @Override
    public void onUpdate() {
        dropItem = EGGS.get(RANDOM.nextInt(EGGS.size())).parseItem();
        super.onUpdate();
    }

    @Override
    public boolean customize(String customization) {
        return enumCustomize(Type.class, customization, ((Rabbit) entity)::setRabbitType);
    }
}
