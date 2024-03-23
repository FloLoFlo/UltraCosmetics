package be.isach.ultracosmetics.cosmetics.type;

import be.isach.ultracosmetics.config.MessageManager;
import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.particleeffects.*;
import be.isach.ultracosmetics.util.Particles;
import be.isach.ultracosmetics.version.ServerVersion;
import com.cryptomorin.xseries.XMaterial;

/**
 * Particle effect types.
 *
 * @author iSach
 * @since 12-18-2015
 */
public class ParticleEffectType extends CosmeticParticleType<ParticleEffect> {

    private ParticleEffectType(String configName, int repeatDelay, Particles effect, XMaterial material, Class<? extends ParticleEffect> clazz, boolean supportsParticleMultiplier) {
        super(Category.EFFECTS, configName, repeatDelay, effect, material, clazz, supportsParticleMultiplier);
        if (GENERATE_MISSING_MESSAGES) {
            MessageManager.addMessage(getConfigPath() + ".name", configName);
        }
    }

    public static void register(ServerVersion version) {
        new ParticleEffectType("SnowCloud", 1, Particles.SNOW_SHOVEL, XMaterial.SNOWBALL, ParticleEffectSnowCloud.class, true);
        new ParticleEffectType("BloodHelix", 1, Particles.REDSTONE, XMaterial.REDSTONE, ParticleEffectBloodHelix.class, true);
        new ParticleEffectType("FrostLord", 1, Particles.SNOW_SHOVEL, XMaterial.PACKED_ICE, ParticleEffectFrostLord.class, true);
        new ParticleEffectType("FlameRings", 1, Particles.FLAME, XMaterial.BLAZE_POWDER, ParticleEffectFlameRings.class, false);
        new ParticleEffectType("GreenSparks", 1, Particles.VILLAGER_HAPPY, XMaterial.EMERALD, ParticleEffectGreenSparks.class, false);
        new ParticleEffectType("FrozenWalk", 1, Particles.SNOW_SHOVEL, XMaterial.SNOWBALL, ParticleEffectFrozenWalk.class, false);
        new ParticleEffectType("Music", 4, Particles.FLAME, XMaterial.MUSIC_DISC_MALL, ParticleEffectMusic.class, true);
        new ParticleEffectType("Enchanted", 1, Particles.ENCHANTMENT_TABLE, XMaterial.BOOK, ParticleEffectEnchanted.class, true);
        new ParticleEffectType("Inferno", 1, Particles.FLAME, XMaterial.NETHER_WART, ParticleEffectInferno.class, true);
        new ParticleEffectType("AngelWings", 2, Particles.REDSTONE, XMaterial.FEATHER, ParticleEffectAngelWings.class, true);
        new ParticleEffectType("RainbowWings", 2, Particles.REDSTONE, XMaterial.YELLOW_DYE, ParticleEffectRainbowWings.class, true);
        new ParticleEffectType("SuperHero", 2, Particles.REDSTONE, XMaterial.GLOWSTONE_DUST, ParticleEffectSuperHero.class, true);
        new ParticleEffectType("SantaHat", 2, Particles.REDSTONE, XMaterial.BEACON, ParticleEffectSantaHat.class, true);
        new ParticleEffectType("EnderAura", 1, Particles.PORTAL, XMaterial.ENDER_EYE, ParticleEffectEnderAura.class, true);
        new ParticleEffectType("FlameFairy", 1, Particles.FLAME, XMaterial.BLAZE_POWDER, ParticleEffectFlameFairy.class, false);
        new ParticleEffectType("RainCloud", 1, Particles.DRIP_WATER, XMaterial.LAPIS_LAZULI, ParticleEffectRainCloud.class, true);
        new ParticleEffectType("CrushedCandyCane", 1, Particles.ITEM_CRACK, XMaterial.WHITE_DYE, ParticleEffectCrushedCandyCane.class, true);
        new ParticleEffectType("InLove", 1, Particles.HEART, XMaterial.RED_DYE, ParticleEffectInLove.class, true);
        new ParticleEffectType("MagicalRods", 3, Particles.REDSTONE, XMaterial.BLAZE_ROD, ParticleEffectMagicalRods.class, true);
        new ParticleEffectType("FireWaves", 4, Particles.FLAME, XMaterial.GOLD_NUGGET, ParticleEffectFireWaves.class, true);
        new ParticleEffectType("CursedHalo", 2, Particles.SPELL_WITCH, XMaterial.PURPLE_DYE, ParticleEffectCursedHalo.class, false);
        new ParticleEffectType("VolcanicHalo", 2, Particles.FLAME, XMaterial.MAGMA_CREAM, ParticleEffectHalo.class, false);
        new ParticleEffectType("CursedFootprints", 1, Particles.SPELL_WITCH, XMaterial.JACK_O_LANTERN, ParticleEffectFootprints.class, false);
        new ParticleEffectType("SpringFootprints", 1, Particles.VILLAGER_HAPPY, XMaterial.POPPY, ParticleEffectFootprints.class, false);
        new ParticleEffectType("ShadowFootprints", 2, Particles.SMOKE_LARGE, XMaterial.BLACK_WOOL, ParticleEffectFootprints.class, false);
        new ParticleEffectType("Notes", 6, Particles.NOTE, XMaterial.NOTE_BLOCK, ParticleEffectAboveHead.class, false);
        new ParticleEffectType("Hearts", 6, Particles.HEART, XMaterial.RED_WOOL, ParticleEffectAboveHead.class, false);
        if (version.isAtLeast(ServerVersion.v1_9)) {
            new ParticleEffectType("EnderFootprints", 1, Particles.DRAGON_BREATH, XMaterial.DRAGON_BREATH, ParticleEffectFootprints.class, false);
        }
        if (version.isAtLeast(ServerVersion.v1_16)) {
            new ParticleEffectType("ArcaneFlame", 3, Particles.SOUL_FIRE_FLAME, XMaterial.SOUL_TORCH, ParticleEffectAboveHead.class, false);
        }
        if (version.isAtLeast(ServerVersion.v1_17)) {
            new ParticleEffectType("DivineHalo", 2, Particles.WAX_OFF, XMaterial.GLOWSTONE, ParticleEffectHalo.class, false);
            new ParticleEffectType("SnowFootprints", 1, Particles.SNOWFLAKE, XMaterial.POWDER_SNOW_BUCKET, ParticleEffectFootprints.class, false);
        }
    }
}
