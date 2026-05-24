package net.yadsoja.lifemod.curse;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.yadsoja.lifemod.curse.manager.CurseManager;

public class BlindCurseHandler {

    private static int tickCounter = 0;

    private static final int INTERVAL = 100; // 5s
    private static final int DURATION = 600;  // 30s

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(BlindCurseHandler::onTick);
    }

    private static void onTick(MinecraftServer server) {

        if (!CurseManager.has("blind")) {
            return;
        }

        tickCounter++;

        if (tickCounter < INTERVAL) {
            return;
        }

        tickCounter = 0;

        for (ServerWorld world : server.getWorlds()) {

            for (CreeperEntity creeper : world.getEntitiesByClass(
                    CreeperEntity.class,
                    new Box(
                            -3.0E7, -3.0E7, -3.0E7,
                            3.0E7,  3.0E7,  3.0E7
                    ),
                    entity -> true
            )) {

                creeper.addStatusEffect(
                        new StatusEffectInstance(
                                StatusEffects.BLINDNESS,
                                DURATION,
                                9,
                                false,
                                true,
                                true
                        )
                );
            }
        }
    }
}