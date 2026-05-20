package net.yadsoja.lifemod.curse;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.rule.GameRules;

public class MidnightCurseHandler {
    public static boolean midnightLocked = false;
    private static final long MIDNIGHT = 18000;

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(MidnightCurseHandler::onTick);
    }

    private static void onTick(MinecraftServer server) {
        ServerWorld world = server.getOverworld();
        long time = world.getTimeOfDay() % 24000;
        boolean active = CurseManager.has("midnight");

        if (!active && !world.getGameRules().getValue(GameRules.ADVANCE_TIME)) {
            world.getGameRules().setValue(GameRules.ADVANCE_TIME, true, server);
        }

        if (active && world.getGameRules().getValue(GameRules.ADVANCE_TIME)&&time >= 18000 && time <= 22000) {
            world.getGameRules().setValue(GameRules.ADVANCE_TIME, false, server);
        }
    }
}