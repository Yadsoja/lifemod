package net.yadsoja.lifemod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.yadsoja.lifemod.command.CurseCommand;

import net.yadsoja.lifemod.curse.CurseScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lifemod implements ModInitializer {
	public static final String MOD_ID = "lifemod";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			CurseCommand.register(dispatcher);
		});

		CurseScheduler.init();
		FreezeCurseHandler.init();
	}
}