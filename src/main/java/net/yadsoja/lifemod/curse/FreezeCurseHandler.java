package net.yadsoja.lifemod.curse;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;

public class FreezeCurseHandler {

    private static final int MAX_LIGHT = 5;

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(FreezeCurseHandler::onTick);
    }

    private static void onTick(MinecraftServer server) {

        if (!CurseManager.activeCurses.contains("freeze")) return;

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {

            int light = server.getOverworld().getLightLevel(player.getBlockPos());

            int frozen = player.getFrozenTicks();

            if (light < 5) {
                player.setFrozenTicks(Math.min(frozen + 2, 150));
            } else {
                player.setFrozenTicks(Math.max(frozen - 4, 0));
            }

            if (player.getFrozenTicks() >= 140) {

                player.damage(
                        server.getOverworld(),
                        player.getDamageSources().freeze(),
                        1.0f
                );
            }
        }
    }
}