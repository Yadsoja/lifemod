package net.yadsoja.lifemod.curse;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.yadsoja.lifemod.curse.manager.CurseManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FreezeCurseHandler {

    // freeze damage timer per player
    private static final Map<UUID, Integer> damageTickCounter = new HashMap<>();

    private static final int MAX_LIGHT = 5;

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(FreezeCurseHandler::onTick);
    }

    private static void onTick(MinecraftServer server) {

        // curse disabled → reset everything
        if (!CurseManager.activeCurses.contains("freeze")) {

            damageTickCounter.clear();

            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                player.setFrozenTicks(0);
            }

            return;
        }

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {

            if (player.isCreative() || player.isSpectator()) {
                continue;
            }

            UUID uuid = player.getUuid();

            // NETHER IMMUNITY
            if (player.getEntityWorld().getRegistryKey() == World.NETHER) {

                player.setFrozenTicks(0);
                damageTickCounter.put(uuid, 0);
                continue;
            }

            int light = player.getEntityWorld().getLightLevel(player.getBlockPos());
            int frozen = player.getFrozenTicks();

            damageTickCounter.putIfAbsent(uuid, 0);

            // ❄️ FREEZE ZONE
            if (light < MAX_LIGHT) {

                player.setFrozenTicks(
                        Math.min(
                                player.getMinFreezeDamageTicks(),
                                frozen + 3
                        )
                );

                // ❄️ ANTI JUMP FOV SNAP (IMPORTANT PART)
                player.setJumping(false);

                if (!player.isOnGround()) {
                    player.setVelocity(
                            player.getVelocity().x,
                            Math.min(player.getVelocity().y, 0), // cancel upward boost
                            player.getVelocity().z
                    );
                }

            } else {

                player.setFrozenTicks(
                        Math.max(0, frozen - 3)
                );
            }

            // ❄️ FREEZE DAMAGE
            if (player.getFrozenTicks() >= player.getMinFreezeDamageTicks()) {

                int ticks = damageTickCounter.get(uuid) + 1;

                if (ticks >= 60) {

                    player.damage(
                            player.getEntityWorld(),
                            player.getDamageSources().freeze(),
                            1.0f
                    );

                    ticks = 0;
                }

                damageTickCounter.put(uuid, ticks);

            } else {
                damageTickCounter.put(uuid, 0);
            }
        }
    }
}