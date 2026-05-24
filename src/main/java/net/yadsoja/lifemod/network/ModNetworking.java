package net.yadsoja.lifemod.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.yadsoja.lifemod.client.FreezeClientState;

public class ModNetworking {

    // 🔥 CALLED IN MOD INITIALIZER (SERVER + CLIENT COMMON)
    public static void register() {

        // IMPORTANT : S2C registration
        PayloadTypeRegistry.playS2C().register(
                FreezePacket.ID,
                FreezePacket.CODEC
        );
    }

    // CLIENT ONLY
    public static void registerClient() {

        ClientPlayNetworking.registerGlobalReceiver(
                FreezePacket.ID,
                (payload, context) -> {

                    context.client().execute(() -> {
                        FreezeClientState.setFrozen(payload.frozen());
                    });
                }
        );
    }
}