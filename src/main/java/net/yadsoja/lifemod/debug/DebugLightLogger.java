package net.yadsoja.lifemod.debug;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class DebugLightLogger {

    private static int tickCounter = 0;

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(DebugLightLogger::onTick);
    }

    private static void onTick(MinecraftServer server) {

        tickCounter++;

        // 10 secondes = 200 ticks
        if (tickCounter < 200) return;

        tickCounter = 0;

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {

            int light = server.getOverworld().getLightLevel(player.getBlockPos());

            player.sendMessage(
                    Text.literal(
                            "[DEBUG] Player: " + player.getName().getString()
                                    + " | Light: " + light
                    ),
                    false
            );
        }
    }
}