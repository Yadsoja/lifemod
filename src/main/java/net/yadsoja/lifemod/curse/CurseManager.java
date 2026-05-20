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
    public static void tick() {

        java.util.List<String> toActivate = new java.util.ArrayList<>();

        for (String curse : pendingCurses.keySet()) {

            int ticks = pendingCurses.get(curse) - 1;

            if (ticks <= 0) {
                toActivate.add(curse);
            } else {
                pendingCurses.put(curse, ticks);
            }
        }

        for (String curse : toActivate) {
            pendingCurses.remove(curse);
            activeCurses.add(curse);
        }
    }
    public static Set<String> activeCurses = new HashSet<>();

    public static Set<String> availableCurses = Set.of(
            "freeze",
            "righthanded",
            "midnight",
            "growth",
            "diet",
            "phantom",
            "infested",
            "windcharged",
            "colorblind",
            "picky",
            "blind"
    );
    private static final java.util.Map<String, Integer> pendingCurses = new java.util.HashMap<>();
    public static void add(String curse) {

        // prevent duplicates
        if (activeCurses.contains(curse) || pendingCurses.containsKey(curse)) {
            return;
        }

        // 110 ticks delay
        pendingCurses.put(curse, 110);
    }

    public static void remove(String curse) {
        activeCurses.remove(curse);
    }

    public static boolean has(String curse) {
        return activeCurses.contains(curse);
    }
}