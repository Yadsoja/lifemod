package net.yadsoja.lifemod.debug;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
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
            player.networkHandler.sendPacket(
                    new PlaySoundS2CPacket(
                            RegistryEntry.of(SoundEvents.ENTITY_PLAYER_LEVELUP),
                            SoundCategory.MASTER,
                            player.getX(),
                            player.getY(),
                            player.getZ(),
                            10f,
                            1f,
                            System.currentTimeMillis()
                    )
            );
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