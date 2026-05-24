package net.yadsoja.lifemod;

import net.fabricmc.api.ClientModInitializer;
import net.yadsoja.lifemod.client.ClientNetworking;

public class LifemodClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientNetworking.register();
    }
}
