package net.yadsoja.lifemod.curse;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;

public class FreezeCurseHandler {
    private static int damageTickCounter = 0;
    private static final int MAX_LIGHT = 5;

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(FreezeCurseHandler::onTick);
    }

    private static void onTick(MinecraftServer server) {
        damageTickCounter++;
        if (!CurseManager.activeCurses.contains("freeze")) return;

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {

            int light = server.getOverworld().getLightLevel(player.getBlockPos());

            int frozen = player.getFrozenTicks();

            if (light < 5) {

                player.setInPowderSnow(true);
                player.setFrozenTicks(Math.min(player.getMinFreezeDamageTicks(), player.getFrozenTicks() + 3));

            } else {

                player.setInPowderSnow(false);
                player.setFrozenTicks(Math.min(player.getMinFreezeDamageTicks(), player.getFrozenTicks() - 3));
            }


            if (player.getFrozenTicks() >= 140 && damageTickCounter >= 60) {

                player.damage(
                        server.getOverworld(),
                        player.getDamageSources().freeze(),
                        1.0f
                );
                damageTickCounter = 0;
            }
        }
    }
}