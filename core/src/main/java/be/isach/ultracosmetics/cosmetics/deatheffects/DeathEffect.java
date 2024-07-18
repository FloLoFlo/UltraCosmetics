package be.isach.ultracosmetics.cosmetics.deatheffects;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.Cosmetic;
import be.isach.ultracosmetics.cosmetics.type.DeathEffectType;
import be.isach.ultracosmetics.player.UltraPlayer;
import com.cryptomorin.xseries.particles.ParticleDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

public abstract class DeathEffect extends Cosmetic<DeathEffectType> {
    protected ParticleDisplay display;

    public DeathEffect(UltraPlayer owner, DeathEffectType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
        display = ParticleDisplay.of(getType().getEffect()).withEntity(getPlayer());
    }

    @Override
    protected void onEquip() {
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (event.getEntity() == getPlayer()) {
            displayParticles();
        }
    }

    public void displayParticles() {
        display.spawn();
    }
}
