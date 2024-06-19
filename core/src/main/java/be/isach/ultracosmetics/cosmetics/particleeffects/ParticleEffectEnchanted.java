package be.isach.ultracosmetics.cosmetics.particleeffects;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.type.ParticleEffectType;
import be.isach.ultracosmetics.player.UltraPlayer;
import be.isach.ultracosmetics.util.MathUtils;
import be.isach.ultracosmetics.util.Particles;

/**
 * Represents an instance of enchanted particles summoned by a player.
 *
 * @author iSach
 * @since 10-12-2015
 */
public class ParticleEffectEnchanted extends ParticleEffect {

    public ParticleEffectEnchanted(UltraPlayer owner, ParticleEffectType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @Override
    public void onUpdate() {
        Particles.ENCHANT.display(getPlayer().getLocation().add(0, MathUtils.randomDouble(0.1, 2), 0), getModifiedAmount(60), 8f);
    }
}
