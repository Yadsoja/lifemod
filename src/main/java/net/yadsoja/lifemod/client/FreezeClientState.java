package net.yadsoja.lifemod.client;

public class FreezeClientState {

    private static boolean frozen = false;

    public static void setFrozen(boolean value) {
        frozen = value;
    }

    public static boolean isFrozen() {
        return frozen;
    }
}