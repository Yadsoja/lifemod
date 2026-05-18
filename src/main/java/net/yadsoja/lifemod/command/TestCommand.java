package net.yadsoja.lifemod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class TestCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {

        dispatcher.register(
                CommandManager.literal("curse")

                        // /curse <name>
                        .then(CommandManager.argument("name", StringArgumentType.word())
                                .executes(ctx -> {

                                    String name = StringArgumentType.getString(ctx, "name");

                                    ctx.getSource().sendFeedback(
                                            () -> Text.literal("Hello " + name + "!"),
                                            false
                                    );

                                    return 1;
                                })

                                // /curse <name> <age>
                                .then(CommandManager.argument("time", IntegerArgumentType.integer(0, 120))
                                        .executes(ctx -> {

                                            String name = StringArgumentType.getString(ctx, "name");
                                            int time = IntegerArgumentType.getInteger(ctx, "time");

                                            ctx.getSource().sendFeedback(
                                                    () -> Text.literal("Hello " + name + ", you are " + time + " years old!"),
                                                    false
                                            );

                                            return 1;
                                        })

                                        // /hello <name> <age> <message>
                                        .then(CommandManager.argument("message", StringArgumentType.greedyString())
                                                .executes(ctx -> {

                                                    String name = StringArgumentType.getString(ctx, "name");
                                                    int age = IntegerArgumentType.getInteger(ctx, "age");
                                                    String message = StringArgumentType.getString(ctx, "message");

                                                    ctx.getSource().sendFeedback(
                                                            () -> Text.literal(
                                                                    "Hello " + name +
                                                                            " (" + age + ") says: " + message
                                                            ),
                                                            false
                                                    );

                                                    return 1;
                                                })
                                        )
                                )
                        )
        );
    }
}