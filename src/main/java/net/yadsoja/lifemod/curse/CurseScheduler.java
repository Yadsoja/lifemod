package net.yadsoja.lifemod.curse;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CurseScheduler {

    private static final Random random = new Random();
    private static int tickCounter = 0;

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(CurseScheduler::onTick);
    }

    private static void onTick(MinecraftServer server) {

        if (!CurseManager.autoEnabled) return;

        tickCounter++;

        int intervalTicks = CurseManager.timerMinutes * 60 * 20;

        if (tickCounter >= intervalTicks) {

            tickCounter = 0;
            activateRandomCurse(server);
        }
    }

    private static void activateRandomCurse(MinecraftServer server) {

        List<String> available = new ArrayList<>(CurseManager.availableCurses);
        available.removeAll(CurseManager.activeCurses);

        if (available.isEmpty()) {

            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {

                player.sendMessage(Text.literal("Aucune malédiction disponible."), true);
            }

            return;
        }

        String curse = available.get(random.nextInt(available.size()));

        CurseManager.add(curse);

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            player.playSound(net.minecraft.sound.SoundEvents.ENTITY_WITHER_SPAWN, 1f, 1f);
            // TITLE
            player.networkHandler.sendPacket(
                    new TitleS2CPacket(Text.literal("Une MALEDICTION s'est abattue"))
            );

            // SUBTITLE
            player.networkHandler.sendPacket(
                    new SubtitleS2CPacket(Text.literal(curse))
            );
        }
    }
}