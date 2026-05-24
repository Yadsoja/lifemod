package net.yadsoja.lifemod.command;

import net.minecraft.server.network.ServerPlayerEntity;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import java.util.ArrayList;
import java.util.List;

import net.yadsoja.lifemod.curse.PickyCurseHandler;
import net.yadsoja.lifemod.curse.manager.CurseManager;

import static net.yadsoja.lifemod.curse.manager.CurseScheduler.*;
import static net.yadsoja.lifemod.utils.ModContext.server;

public class CurseCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {

        dispatcher.register(
                CommandManager.literal("curse")

                        // /curse => info
                        .executes(ctx -> {
                            ctx.getSource().sendFeedback(
                                    () -> Text.literal(
                                            """
                                            §6=== Curse System ===
                                            /curse                  -> affiche l'aide
                                            /curse add             -> liste des curses disponibles
                                            /curse add <name>      -> active une curse
                                            /curse add amount <n>  -> active plusieurs curses aléatoires
                                            /curse remove          -> liste les curses actives
                                            /curse remove type <name> -> retire une curse spécifique
                                            /curse remove amount <n> -> retire n curses aléatoires
                                            /curse timer           -> affiche le timer actuel
                                            /curse timer <minutes> -> définit le timer
                                            /curse toggle          -> active/désactive l'automatisation
                                            """
                                    ),
                                    false
                            );
                            server.getPlayerManager().broadcast(Text.literal("[DEBUG] curseState=" + PickyCurseHandler.curseState), false);
                            return 1;
                        })

                        // /curse add
                        .then(CommandManager.literal("add")
                                .requires(CommandManager.requirePermissionLevel(CommandManager.GAMEMASTERS_CHECK))

                                // /curse add -> list curses
                                .executes(ctx -> {

                                    ctx.getSource().sendFeedback(
                                            () -> Text.literal("Available curses: " + CurseManager.availableCurses),
                                            false
                                    );

                                    return 1;
                                })

                                // /curse add amount <number>
                                .then(CommandManager.literal("amount")
                                        .executes(ctx -> {

                                            ctx.getSource().sendFeedback(
                                                    () -> Text.literal("Available curses: " + CurseManager.availableCurses),
                                                    false
                                            );

                                            return 1;
                                        })
                                        .then(CommandManager.argument("count", IntegerArgumentType.integer(1))
                                                .executes(ctx -> {

                                                    int count = IntegerArgumentType.getInteger(ctx, "count");

                                                    List<String> available = new ArrayList<>(CurseManager.availableCurses);
                                                    available.removeAll(CurseManager.activeCurses);

                                                    java.util.Collections.shuffle(available);

                                                    int added = 0;

                                                    for (int i = 0; i < count && i < available.size(); i++) {

                                                        String curse = available.get(i);

                                                        CurseManager.add(curse);
                                                        playCurseIntro(curse);
                                                        added++;
                                                    }

                                                    final int finalAdded = added;

                                                    ctx.getSource().sendFeedback(
                                                            () -> Text.literal("Added " + finalAdded + " random curses."),
                                                            false
                                                    );

                                                    return 1;
                                                })
                                        )
                                )

                                // /curse add type <curse>
                                .then(CommandManager.literal("type")
                                        .executes(ctx -> {

                                            ctx.getSource().sendFeedback(
                                                    () -> Text.literal("Available curses: " + CurseManager.availableCurses),
                                                    false
                                            );

                                            return 1;
                                        })
                                        .then(CommandManager.argument("curse", StringArgumentType.word())
                                                .suggests((ctx, builder) -> {

                                                    for (String c : CurseManager.availableCurses) {
                                                        builder.suggest(c);
                                                    }

                                                    return builder.buildFuture();
                                                })
                                                .executes(ctx -> {

                                                    String curse = StringArgumentType.getString(ctx, "curse").toLowerCase();

                                                    if (!CurseManager.availableCurses.contains(curse)) {
                                                        ctx.getSource().sendFeedback(
                                                                () -> Text.literal("Unknown curse: " + curse),
                                                                false
                                                        );
                                                        return 0;
                                                    }

                                                    CurseManager.add(curse);
                                                    playCurseIntro(curse);
                                                    ctx.getSource().sendFeedback(
                                                            () -> Text.literal("Added curse: " + curse),
                                                            false
                                                    );

                                                    return 1;
                                                })
                                        )
                                )
                        )

                        // /curse remove
                        .then(CommandManager.literal("remove")
                                .requires(CommandManager.requirePermissionLevel(CommandManager.GAMEMASTERS_CHECK))

                                // /curse remove -> list
                                .executes(ctx -> {

                                    ctx.getSource().sendFeedback(
                                            () -> Text.literal("Active curses: " + CurseManager.activeCurses),
                                            false
                                    );

                                    return 1;
                                })

                                // /curse remove amount <number>
                                .then(CommandManager.literal("amount")
                                        .executes(ctx -> {

                                            ctx.getSource().sendFeedback(
                                                    () -> Text.literal("Active curses: " + CurseManager.activeCurses),
                                                    false
                                            );

                                            return 1;
                                        })
                                        .then(CommandManager.argument("count", IntegerArgumentType.integer(1))
                                                .executes(ctx -> {

                                                    int count = IntegerArgumentType.getInteger(ctx, "count");

                                                    List<String> active = new ArrayList<>(CurseManager.activeCurses);

                                                    java.util.Collections.shuffle(active);

                                                    int removed = 0;

                                                    for (int i = 0; i < count && i < active.size(); i++) {
                                                        CurseManager.remove(active.get(i));
                                                        removed++;
                                                    }

                                                    final int finalRemoved = removed;

                                                    ctx.getSource().sendFeedback(
                                                            () -> Text.literal("Removed " + finalRemoved + " random curses."),
                                                            false
                                                    );

                                                    return 1;
                                                })
                                        )
                                )

                                // /curse remove type <curse>
                                .then(CommandManager.literal("type")
                                        .executes(ctx -> {

                                            ctx.getSource().sendFeedback(
                                                    () -> Text.literal("Active curses: " + CurseManager.activeCurses),
                                                    false
                                            );

                                            return 1;
                                        })
                                        .then(CommandManager.argument("curse", StringArgumentType.word())
                                                .suggests((ctx, builder) -> {
                                                    for (String c : CurseManager.activeCurses) {
                                                        builder.suggest(c);
                                                    }
                                                    return builder.buildFuture();
                                                })
                                                .executes(ctx -> {

                                                    String curse = StringArgumentType.getString(ctx, "curse").toLowerCase();

                                                    if (!CurseManager.activeCurses.contains(curse)) {
                                                        ctx.getSource().sendFeedback(
                                                                () -> Text.literal("This curse is not active: " + curse),
                                                                false
                                                        );
                                                        return 0;
                                                    }

                                                    CurseManager.remove(curse);

                                                    ctx.getSource().sendFeedback(
                                                            () -> Text.literal("Removed curse: " + curse),
                                                            false
                                                    );

                                                    return 1;
                                                })
                                        )
                                )
                        )

                        // /curse timer <minutes>
                        .then(CommandManager.literal("timer")
                                .requires(CommandManager.requirePermissionLevel(CommandManager.GAMEMASTERS_CHECK))

                                // /curse timer  -> show current value
                                .executes(ctx -> {

                                    ctx.getSource().sendFeedback(
                                            () -> Text.literal("Current curse timer: " + CurseManager.timerMinutes + " minutes"),
                                            false
                                    );

                                    return 1;
                                })

                                // /curse timer <minutes>
                                .then(CommandManager.argument("minutes", IntegerArgumentType.integer(1, 1440))
                                        .executes(ctx -> {

                                            int minutes = IntegerArgumentType.getInteger(ctx, "minutes");

                                            CurseManager.timerMinutes = minutes;

                                            ctx.getSource().sendFeedback(
                                                    () -> Text.literal("Curse timer set to " + minutes + " minutes"),
                                                    false
                                            );

                                            return 1;
                                        })
                                )
                        )

                        // /curse toggle
                        .then(CommandManager.literal("toggle")
                                .requires(CommandManager.requirePermissionLevel(CommandManager.GAMEMASTERS_CHECK))
                                .executes(ctx -> {

                                    CurseManager.autoEnabled = !CurseManager.autoEnabled;

                                    ctx.getSource().sendFeedback(
                                            () -> Text.literal(
                                                    "Auto curses: " +
                                                            (CurseManager.autoEnabled ? "§aENABLED" : "§cDISABLED")
                                            ),
                                            false
                                    );

                                    return 1;
                                })
                        )
                        .then(CommandManager.literal("show")
                                .requires(source -> PickyCurseHandler.curseState == PickyCurseHandler.ACTIVE)
                                .then(CommandManager.literal("eaten")
                                        .executes(ctx -> {

                                            ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();

                                            PickyCurseHandler.showEaten(player);

                                            return 1;
                                        })
                                )
                        )
        );
    }
}