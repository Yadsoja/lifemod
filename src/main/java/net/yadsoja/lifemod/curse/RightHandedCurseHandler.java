package net.yadsoja.lifemod.curse;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.yadsoja.lifemod.curse.manager.CurseManager;

public class RightHandedCurseHandler {

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(RightHandedCurseHandler::onTick);
    }

    private static void onTick(MinecraftServer server) {

        if (!CurseManager.has("righthanded")) return;

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {

            ItemStack offhand = player.getOffHandStack();

            if (!offhand.isEmpty()) {

                // drop item from offhand
                player.dropItem(offhand.copy(), true);

                // clear offhand
                player.getOffHandStack().decrement(offhand.getCount());
                // sync inventory client-side
                player.playerScreenHandler.sendContentUpdates();
            }
        }
    }
}