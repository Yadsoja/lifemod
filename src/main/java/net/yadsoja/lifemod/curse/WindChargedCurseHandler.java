package net.yadsoja.lifemod.curse;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.entity.Entity;
import net.yadsoja.lifemod.curse.manager.CurseManager;

public class WindChargedCurseHandler {

    // 5 seconds = 100 ticks
    private static int tickCounter = 0;

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(WindChargedCurseHandler::onTick);
    }

    private static void onTick(MinecraftServer server) {

        if (!CurseManager.has("windcharged")) {
            return;
        }

        tickCounter++;

        // every 5 seconds
        if (tickCounter < 100) {
            return;
        }

        tickCounter = 0;

        for (ServerWorld world : server.getWorlds()) {

            for (Entity entity : world.iterateEntities()) {

                if (!(entity instanceof LivingEntity living)) {
                    continue;
                }

                if (entity instanceof ServerPlayerEntity) {
                    continue;
                }

                living.addStatusEffect(
                        new StatusEffectInstance(
                                StatusEffects.WIND_CHARGED,
                                140,
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