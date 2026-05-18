package net.yadsoja.lifemod.curse;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.util.HashSet;
import java.util.Set;

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