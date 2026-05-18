package net.yadsoja.lifemod.curse;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CurseScheduler {

    private static final Random random = new Random();

    // tick counter
    private static int tickCounter = 0;

    public static void init() {

        ServerTickEvents.END_SERVER_TICK.register(CurseScheduler::onTick);
    }

    private static void onTick(MinecraftServer server) {

        // system disabled by player
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
            server.getPlayerManager().broadcast(
                    Text.literal("§7No new curses available."),
                    false
            );
            return;
        }

        String curse = available.get(random.nextInt(available.size()));

        CurseManager.add(curse);

        server.getPlayerManager().broadcast(
                Text.literal("§cA random curse has been activated: §6" + curse),
                false
        );
    }
}