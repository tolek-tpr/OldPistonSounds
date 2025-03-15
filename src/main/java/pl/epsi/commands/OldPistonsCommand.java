package pl.epsi.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import pl.epsi.settings.OldPistonSettings;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*;

public class OldPistonsCommand implements ClientModInitializer {

    private final OldPistonSettings settings = OldPistonSettings.getInstance();

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, access) -> {
            dispatcher.register(literal("oldpistons")
                    .then(literal("cutoffPistons")
                            .then(argument("On/Off", BoolArgumentType.bool())
                                    .executes(ctx -> cutoffPistonsCommand(ctx, true)))
                            .executes(ctx -> cutoffPistonsCommand(ctx, false)))
                    .then(literal("changePistonPitch")
                            .then(argument("On/Off", BoolArgumentType.bool())
                                    .executes(ctx -> changePistonPitchCommand(ctx, true)))
                            .executes(ctx -> changePistonPitchCommand(ctx, false)))
                    .then(literal("cutoffSmoothLastPiston")
                            .then(argument("On/Off", BoolArgumentType.bool())
                                    .executes(ctx -> cutoffSmoothLastPistonCommand(ctx, true)))
                            .executes(ctx -> cutoffSmoothLastPistonCommand(ctx, false)))
                    .then(literal("pistonSoundThreshold")
                            .then(argument("Value", IntegerArgumentType.integer())
                                    .executes(ctx -> pistonSoundThreshold(ctx, true)))
                            .executes(ctx -> pistonSoundThreshold(ctx, false)))
                    .then(literal("cutoffTime")
                            .then(argument("Value", IntegerArgumentType.integer())
                                    .executes(ctx -> cutoffTime(ctx, true)))
                            .executes(ctx -> cutoffTime(ctx, false)))
                    .then(literal("default")
                            .executes(this::setToDefault))
            );

        });
    }

    public int setToDefault(CommandContext<FabricClientCommandSource> ctx) {
        settings.setToDefault();
        ctx.getSource().sendFeedback(Text.translatable("oldpistons.defaultCommand.default"));

        return 1;
    }

    public int pistonSoundThreshold(CommandContext<FabricClientCommandSource> ctx, boolean bl) {
        if (bl) {
            settings.pistonSoundThreshold = IntegerArgumentType.getInteger(ctx, "Value");
            settings.setCustomProfile();
            ctx.getSource().sendFeedback(Text.translatable("oldpistons.pistonSoundThresholdCommand.set", settings.pistonSoundThreshold));
        } else {
            ctx.getSource().sendFeedback(Text.translatable("oldpistons.pistonSoundThresholdCommand.get", settings.pistonSoundThreshold));
        }

        return 1;
    }

    public int cutoffTime(CommandContext<FabricClientCommandSource> ctx, boolean bl) {
        if (bl) {
            settings.cutoffTime = IntegerArgumentType.getInteger(ctx, "Value");
            settings.setCustomProfile();
            ctx.getSource().sendFeedback(Text.translatable("oldpistons.cutoffTimeCommand.set", settings.cutoffTime));
        } else {
            ctx.getSource().sendFeedback(Text.translatable("oldpistons.cutoffTimeCommand.get", settings.cutoffTime));
        }

        return 1;
    }

    public int cutoffSmoothLastPistonCommand(CommandContext<FabricClientCommandSource> ctx, boolean bl) {
        if (bl) {
            settings.cutoffSmoothLastPiston = BoolArgumentType.getBool(ctx, "On/Off");
            settings.setCustomProfile();
            ctx.getSource().sendFeedback(Text.translatable("oldpistons.cutoffSmoothLastPistonCommand.set", settings.cutoffSmoothLastPiston));
        } else {
            ctx.getSource().sendFeedback(Text.translatable("oldpistons.cutoffSmoothLastPistonCommand.get", settings.cutoffSmoothLastPiston));
        }

        return 1;
    }

    public int cutoffPistonsCommand(CommandContext<FabricClientCommandSource> ctx, boolean bl) {
        if (bl) {
            settings.cutoffPistons = BoolArgumentType.getBool(ctx, "On/Off");
            settings.setCustomProfile();
            ctx.getSource().sendFeedback(Text.translatable("oldpistons.cutoffPistonsCommand.set", settings.cutoffPistons));
        } else {
            ctx.getSource().sendFeedback(Text.translatable("oldpistons.cutoffPistonsCommand.get", settings.cutoffPistons));
        }

        return 1;
    }

    public int changePistonPitchCommand(CommandContext<FabricClientCommandSource> ctx, boolean bl) {
        if (bl) {
            settings.modifyPistonPitch = BoolArgumentType.getBool(ctx, "On/Off");
            settings.setCustomProfile();
            ctx.getSource().sendFeedback(Text.translatable("oldpistons.changePistonPitchCommand.set", settings.modifyPistonPitch));
        } else {
            ctx.getSource().sendFeedback(Text.translatable("oldpistons.changePistonPitchCommand.get", settings.modifyPistonPitch));
        }

        return 1;
    }

}
