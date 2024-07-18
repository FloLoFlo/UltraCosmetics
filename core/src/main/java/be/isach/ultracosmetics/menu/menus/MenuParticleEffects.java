package be.isach.ultracosmetics.menu.menus;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.type.ParticleEffectType;
import be.isach.ultracosmetics.menu.CosmeticMenu;

/**
 * Particle Effect {@link be.isach.ultracosmetics.menu.Menu Menu}.
 *
 * @author iSach
 * @since 08-23-2016
 */
public class MenuParticleEffects extends CosmeticMenu<ParticleEffectType> {

    public MenuParticleEffects(UltraCosmetics ultraCosmetics) {
        super(ultraCosmetics, Category.EFFECTS);
    }
}
