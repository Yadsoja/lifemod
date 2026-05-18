package net.yadsoja.lifemod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import net.yadsoja.lifemod.curse.CurseManager;

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
                                            /curse add
                                            /curse add <name>
                                            /curse remove
                                            /curse remove <name>
                                            /curse timer <minutes>
                                            /curse toggle
                                            """
                                    ),
                                    false
                            );
                            return 1;
                        })

                        // /curse add
                        .then(CommandManager.literal("add")

                                // /curse add => list curses
                                .executes(ctx -> {
                                    ctx.getSource().sendFeedback(
                                            () -> Text.literal("Available curses: " + CurseManager.availableCurses),
                                            false
                                    );
                                    return 1;
                                })

                                // /curse add freeze
                                .then(CommandManager.argument("curse", StringArgumentType.word())
                                        .suggests((context, builder) -> {

                                            for (String curse : CurseManager.availableCurses) {
                                                builder.suggest(curse);
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

                                            ctx.getSource().sendFeedback(
                                                    () -> Text.literal("Curse activated: " + curse),
                                                    false
                                            );

                                            return 1;
                                        })
                                )
                        )

                        // /curse remove
                        .then(CommandManager.literal("remove")

                                // /curse remove => list active curses
                                .executes(ctx -> {
                                    ctx.getSource().sendFeedback(
                                            () -> Text.literal("Active curses: " + CurseManager.activeCurses),
                                            false
                                    );
                                    return 1;
                                })

                                // /curse remove freeze
                                .then(CommandManager.argument("curse", StringArgumentType.word())
                                        .suggests((context, builder) -> {

                                            for (String curse : CurseManager.activeCurses) {
                                                builder.suggest(curse);
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
                                                    () -> Text.literal("Curse removed: " + curse),
                                                    false
                                            );

                                            return 1;
                                        })
                                )
                        )

                        // /curse timer <minutes>
                        .then(CommandManager.literal("timer")

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
        );
    }
}