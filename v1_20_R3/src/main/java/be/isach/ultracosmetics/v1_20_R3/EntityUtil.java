package be.isach.ultracosmetics.v1_20_R3;

import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.util.MathUtils;
import be.isach.ultracosmetics.util.Particles;
import be.isach.ultracosmetics.version.IEntityUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Rotations;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftWither;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author RadBuilder
 * @author iSach
 */
public class EntityUtil implements IEntityUtil {

    private final Random r = new Random();
    private Map<Player, Set<ArmorStand>> fakeArmorStandsMap = new HashMap<>();
    private Map<Player, Set<org.bukkit.entity.Entity>> cooldownJumpMap = new HashMap<>();

    @Override
    public void resetWitherSize(Wither wither) {
        ((CraftWither) wither).getHandle().setInvulnerableTicks(600);
    }

    @Override
    public void sendBlizzard(final Player player, Location loc, Predicate<org.bukkit.entity.Entity> canAffectFunc, Vector v) {
        final Set<ArmorStand> fakeArmorStands = fakeArmorStandsMap.computeIfAbsent(player, k -> new HashSet<>());
        final Set<org.bukkit.entity.Entity> cooldownJump = cooldownJumpMap.computeIfAbsent(player, k -> new HashSet<>());

        final ArmorStand as = new ArmorStand(EntityType.ARMOR_STAND, ((CraftWorld) player.getWorld()).getHandle());
        as.setInvisible(true);
        as.setSharedFlag(5, true);
        as.setSmall(true);
        as.setNoGravity(true);
        as.setShowArms(true);
        as.setHeadPose(new Rotations(r.nextInt(360), r.nextInt(360), r.nextInt(360)));
        as.absMoveTo(loc.getX() + MathUtils.randomDouble(-1.5, 1.5), loc.getY() + MathUtils.randomDouble(0, 0.5) - 0.75,
                loc.getZ() + MathUtils.randomDouble(-1.5, 1.5), 0, 0);
        fakeArmorStands.add(as);
        ClientboundAddEntityPacket addPacket = new ClientboundAddEntityPacket(as);
        ClientboundSetEntityDataPacket dataPacket = new ClientboundSetEntityDataPacket(as.getId(), as.getEntityData().packDirty());
        List<Pair<EquipmentSlot, ItemStack>> equipment = new ArrayList<>();
        equipment.add(new Pair<>(EquipmentSlot.HEAD, CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(org.bukkit.Material.PACKED_ICE))));
        ClientboundSetEquipmentPacket equipmentPacket = new ClientboundSetEquipmentPacket(as.getId(), equipment);
        for (Player loopPlayer : player.getWorld().getPlayers()) {
            sendPacket(loopPlayer, addPacket);
            sendPacket(loopPlayer, dataPacket);
            sendPacket(loopPlayer, equipmentPacket);
        }
        Particles.CLOUD.display(loc.clone().add(MathUtils.randomDouble(-1.5, 1.5), MathUtils.randomDouble(0, 0.5) - 0.75,
                MathUtils.randomDouble(-1.5, 1.5)), 2, 0.4f);
        Bukkit.getScheduler().runTaskLater(UltraCosmeticsData.get().getPlugin(), () -> {
            for (Player pl : player.getWorld().getPlayers()) {
                sendPacket(pl, new ClientboundRemoveEntitiesPacket(as.getId()));
            }
            fakeArmorStands.remove(as);
        }, 20);
        as.getBukkitEntity().getNearbyEntities(0.5, 0.5, 0.5).stream()
                .filter(ent -> !cooldownJump.contains(ent) && ent != player && canAffectFunc.test(ent))
                .forEachOrdered(ent -> {
                    MathUtils.applyVelocity(ent, new Vector(0, 1, 0).add(v));
                    cooldownJump.add(ent);
                    Bukkit.getScheduler().runTaskLater(UltraCosmeticsData.get().getPlugin(),
                            () -> cooldownJump.remove(ent), 20);
                });
    }

    @Override
    public void clearBlizzard(Player player) {
        if (!fakeArmorStandsMap.containsKey(player)) return;

        for (ArmorStand as : fakeArmorStandsMap.get(player)) {
            if (as == null) {
                continue;
            }
            for (Player pl : player.getWorld().getPlayers()) {
                sendPacket(pl, new ClientboundRemoveEntitiesPacket(as.getId()));
            }
        }

        fakeArmorStandsMap.remove(player);
        cooldownJumpMap.remove(player);
    }

    @Override
    public void sendDestroyPacket(Player player, org.bukkit.entity.Entity entity) {
        sendPacket(player, new ClientboundRemoveEntitiesPacket(((CraftEntity) entity).getHandle().getId()));
    }

    @Override
    public void setStepHeight(org.bukkit.entity.Entity entity) {
        ((CraftEntity) entity).getHandle().setMaxUpStep(1);
    }

    @Override
    public void sendTeleportPacket(Player player, org.bukkit.entity.Entity entity) {
        sendPacket(player, new ClientboundTeleportEntityPacket(((CraftEntity) entity).getHandle()));
    }

    private void sendPacket(Player player, Packet<?> packet) {
        ((CraftPlayer) player).getHandle().connection.send(packet);
    }
}
