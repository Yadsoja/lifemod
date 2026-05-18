package net.yadsoja.lifemod.curse;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;

import java.util.HashSet;
import java.util.Set;

import static net.yadsoja.lifemod.curse.CurseScheduler.BroadcastSound;
import static net.yadsoja.lifemod.curse.CurseScheduler.BroadcastTitle;

public class CurseManager {

    public static boolean autoEnabled = false;
    public static int timerMinutes = 10;

    public static Set<String> activeCurses = new HashSet<>();

    public static Set<String> availableCurses = Set.of(
            "freeze",
            "blind",
            "slow",
            "hunger"
    );

    public static void add(String curse) {
        activeCurses.add(curse);
    }

    public static void remove(String curse) {
        activeCurses.remove(curse);
    }

    public static boolean has(String curse) {
        return activeCurses.contains(curse);
    }
}