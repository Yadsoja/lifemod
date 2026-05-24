package net.yadsoja.lifemod.curse;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.rule.GameRules;
import net.yadsoja.lifemod.curse.manager.CurseManager;

public class PhantomCurseHandler {

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(PhantomCurseHandler::onTick);
    }
    private static void onTick(MinecraftServer server) {
        ServerWorld world = server.getOverworld();
        boolean active = CurseManager.has("phantom");

        if (!active && world.getGameRules().getValue(GameRules.SPAWN_PHANTOMS)) {
            world.getGameRules().setValue(GameRules.SPAWN_PHANTOMS, false, server);
        }

        if (active && !world.getGameRules().getValue(GameRules.SPAWN_PHANTOMS)) {
            world.getGameRules().setValue(GameRules.SPAWN_PHANTOMS, true, server);
        }
    }
}