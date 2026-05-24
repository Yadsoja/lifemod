package net.yadsoja.lifemod.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.yadsoja.lifemod.network.FreezePacket;

public class ClientNetworking {

    public static void register() {

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