package be.isach.ultracosmetics.cosmetics.pets;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.config.SettingsManager;
import be.isach.ultracosmetics.cosmetics.type.PetType;
import be.isach.ultracosmetics.player.UltraPlayer;

import org.bukkit.boss.BossBar;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;

/**
 * Represents an instance of a wither pet summoned by a player.
 *
 * @author iSach
 * @since 08-12-2015
 */
public class PetWither extends Pet {

    public PetWither(UltraPlayer owner, PetType type, UltraCosmetics ultraCosmetics) {
        super(owner, type, ultraCosmetics);
    }

    @Override
    public void onUpdate() {
        // Do not call super.onUpdate(), wither does not drop items.
        // This runs onUpdate because if players walk in range, the bossbar reappears
        String setting = SettingsManager.getConfig().getString(getOptionPath("Bossbar"), "in range");
        BossBar bar = ((Wither) entity).getBossBar();
        if (setting.equalsIgnoreCase("owner")) {
            bar.getPlayers().stream().filter(p -> p != getPlayer()).forEach(bar::removePlayer);
        } else if (setting.equalsIgnoreCase("none")) {
            bar.getPlayers().forEach(bar::removePlayer);
        }

        if (!SettingsManager.getConfig().getBoolean("Pets-Are-Babies")) return;
        UltraCosmeticsData.get().getVersionManager().getEntityUtil().resetWitherSize((Wither) entity);
    }

    @EventHandler
    public void onShoot(ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() == entity) {
            event.setCancelled(true);
        }
    }
}
