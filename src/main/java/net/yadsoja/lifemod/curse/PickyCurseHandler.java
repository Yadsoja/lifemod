package net.yadsoja.lifemod.curse;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.yadsoja.lifemod.curse.manager.CurseManager;

import java.util.*;

import static net.yadsoja.lifemod.utils.ModContext.server;

public class PickyCurseHandler {

    // =========================
    // CURSE STATE
    // =========================

    public static int curseState = 0;


    // =========================
    // DATA STORAGE
    // =========================
    private static final Map<UUID, Deque<String>> history = new HashMap<>();
    private static final Map<UUID, String> pending = new HashMap<>();
    private static final Map<UUID, Integer> lastFoodLevel = new HashMap<>();
    public static final int ACTIVE = 1;
    public static final int INACTIVE = 0;
    private static final int MAX_HISTORY = 2;

    public static void init() {

        // 🍽️ detect eating start
        UseItemCallback.EVENT.register((player, world, hand) -> {

            if (world.isClient()) return ActionResult.PASS;
            if (!(player instanceof ServerPlayerEntity sp)) return ActionResult.PASS;

            if (curseState != 1) return ActionResult.PASS;

            ItemStack stack = player.getStackInHand(hand);

            if (!stack.contains(DataComponentTypes.FOOD)) {
                return ActionResult.PASS;
            }

            UUID uuid = sp.getUuid();

            history.putIfAbsent(uuid, new ArrayDeque<>());

            String itemId = stack.getItem().toString();
            Deque<String> deque = history.get(uuid);

            // block repeat food
            if (deque.contains(itemId)) {

                sp.sendMessage(
                        Text.literal("§cPicky Curse: Vous ne pouvez pas remanger cette nourriture."),
                        true
                );

                return ActionResult.FAIL;
            }

            pending.put(uuid, itemId);

            return ActionResult.PASS;
        });

        ServerTickEvents.END_SERVER_TICK.register(PickyCurseHandler::tick);
    }

    // =========================
    // STATE SYNC
    // =========================
    private static void updateState() {
        curseState = CurseManager.has("picky") ? ACTIVE : INACTIVE;
        server.getPlayerManager().getPlayerList().forEach(player ->
                server.getCommandManager().sendCommandTree(player)
        );
    }

    // =========================
    // MAIN TICK
    // =========================
    private static void tick(MinecraftServer server) {

        updateState();

        if (curseState != 1) return;

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {

            UUID uuid = player.getUuid();

            int currentFood = player.getHungerManager().getFoodLevel();
            int lastFood = lastFoodLevel.getOrDefault(uuid, currentFood);

            // 🍽️ real consumption detected
            if (currentFood > lastFood) {

                String itemId = pending.get(uuid);

                if (itemId != null) {

                    history.putIfAbsent(uuid, new ArrayDeque<>());
                    Deque<String> deque = history.get(uuid);

                    if (deque.size() >= MAX_HISTORY) {
                        deque.removeFirst();
                    }

                    deque.addLast(itemId);

                    pending.remove(uuid);
                }
            }

            // cleanup if stopped eating
            Hand hand = player.getActiveHand();
            ItemStack used = player.getStackInHand(hand);

            if (!player.isUsingItem() || used.isEmpty()) {
                pending.remove(uuid);
            }

            lastFoodLevel.put(uuid, currentFood);
        }
    }

    // =========================
    // COMMAND FUNCTION
    // =========================
    public static void showEaten(ServerPlayerEntity player) {

        if (curseState != 1) {
            player.sendMessage(Text.literal("§7Picky curse is not active."), false);
            return;
        }

        UUID uuid = player.getUuid();
        Deque<String> eaten = history.get(uuid);

        if (eaten == null || eaten.isEmpty()) {
            player.sendMessage(Text.literal("§7Aucun repas enregistré."), false);
            return;
        }

        player.sendMessage(Text.literal("§6Derniers aliments mangés:"), false);

        for (String food : eaten) {
            player.sendMessage(Text.literal("§e- " + food), false);
        }
    }
}