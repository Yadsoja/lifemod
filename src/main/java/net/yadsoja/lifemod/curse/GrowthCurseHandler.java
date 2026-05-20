package net.yadsoja.lifemod.curse;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class GrowthCurseHandler {

    private static final double GROWTH_SCALE = 1.25;
    private static final double NORMAL_SCALE = 1.0;

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(GrowthCurseHandler::onTick);
    }

    private static void onTick(MinecraftServer server) {

        boolean active = CurseManager.has("growth");

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {

            var scale = player.getAttributeInstance(EntityAttributes.SCALE);
            if (scale == null) continue;

            if (active) {

                if (scale.getBaseValue() != GROWTH_SCALE) {
                    player.setSneaking(true);
                    scale.setBaseValue(GROWTH_SCALE);
                }

            } else {

                if (scale.getBaseValue() != NORMAL_SCALE) {
                    scale.setBaseValue(NORMAL_SCALE);
                }
            }
        }
    }
}