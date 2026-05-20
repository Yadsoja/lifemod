package net.yadsoja.lifemod.curse;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleFadeS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.yadsoja.lifemod.utils.ModContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CurseScheduler {
    private static boolean animRunning = false;
    private static int animTick = 0;
    private static String animCurse = "";
    private static final Random random = new Random();
    private static int tickCounter = 0;

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(CurseScheduler::onTick);
    }

    public static void playCurseIntro(String curse) {
        animRunning = true;
        animTick = 0;
        animCurse = curse;
    }

    private static void onTick(MinecraftServer server) {
        CurseManager.tick();
        if (animRunning) {

            animTick++;

            switch (animTick) {

                case 1 -> {
                    BroadcastTitle("§a⬤", "");
                    BroadcastSound("ding");
                }

                case 20 -> {
                    BroadcastTitle("§a⬤   §e⬤", "");
                    BroadcastSound("ding");
                }

                case 40 -> {
                    BroadcastTitle("§a⬤   §e⬤   §c⬤", "");
                    BroadcastSound("ding");
                }

                case 100 -> {
                    BroadcastTitle("§4CURSE ACTIVATED", animCurse);
                    BroadcastSound("curse");

                    animRunning = false;
                }
            }

            return; // IMPORTANT : bloque le reste du scheduler
        }
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
        playCurseIntro(curse);
    }

    public static void BroadcastTitle(String title,String subtitle) {

        MinecraftServer server = ModContext.server;

        if (server == null) return;
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            player.networkHandler.sendPacket(
                    new TitleS2CPacket(Text.literal(title))
            );

//            player.networkHandler.sendPacket(
//                    new SubtitleS2CPacket(Text.literal(subtitle))
//            );

            player.networkHandler.sendPacket(
                    new TitleFadeS2CPacket(10, 40, 10)
            );
        }
    }

    public static void BroadcastSound(String sound) {
        MinecraftServer server = ModContext.server;
        if (server == null) return;

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {

            switch (sound.toLowerCase()) {

                case "curse":
                    server.getOverworld().playSound(
                            null,
                            player.getBlockPos(),
                            SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE,
                            SoundCategory.RECORDS,
                            1f,
                            1f
                    );
                    break;

                case "levelup":
                    server.getOverworld().playSound(
                            null,
                            player.getBlockPos(),
                            SoundEvents.ENTITY_PLAYER_LEVELUP,
                            SoundCategory.RECORDS,
                            1f,
                            1f
                    );
                    break;

                case "ding":
                    server.getOverworld().playSound(
                            null,
                            player.getBlockPos(),
                            SoundEvents.ENTITY_ARROW_HIT_PLAYER,
                            SoundCategory.RECORDS,
                            1f,
                            1f
                    );
                    break;
            }
        }
    }
}