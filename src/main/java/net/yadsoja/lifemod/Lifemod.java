package net.yadsoja.lifemod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import net.yadsoja.lifemod.command.CurseCommand;

public class LifeMod implements ModInitializer {

	@Override
	public void onInitialize() {

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			CurseCommand.register(dispatcher);
		});
	}
}