package net.yadsoja.lifemod.curse;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.yadsoja.lifemod.curse.manager.CurseManager;

public class InfestedCurseHandler {

    private static int tickCounter = 0;

    private static final int INTERVAL = 100; // 5 seconds
    private static final int DURATION = 140;  // 7 seconds

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(InfestedCurseHandler::onTick);
    }

    private static void onTick(MinecraftServer server) {

        if (!CurseManager.has("infested")) {
            return;
        }

        tickCounter++;

        if (tickCounter < INTERVAL) {
            return;
        }

        tickCounter = 0;

        for (ServerWorld world : server.getWorlds()) {

            long time = world.getTimeOfDay() % 24000;
            boolean isDay = time >= 0 && time < 12000;

            boolean isNether = world.getRegistryKey() == ServerWorld.NETHER;

            // Only apply during day OR always in Nether
            if (!isNether && isDay) {
                continue;
            }

            for (Entity entity : world.iterateEntities()) {

                if (!(entity instanceof LivingEntity living)) continue;
                if (entity instanceof ServerPlayerEntity) continue;
                living.addStatusEffect(
                        new StatusEffectInstance(
                                StatusEffects.INFESTED,
                                DURATION,
                                0,
                                false,
                                false,
                                false
                        )
                );

            }
        }
    }
}