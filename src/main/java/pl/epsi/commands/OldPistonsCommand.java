package pl.epsi.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
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
            );

        });
    }

    public int cutoffSmoothLastPistonCommand(CommandContext<FabricClientCommandSource> ctx, boolean bl) {
        if (bl) {
            settings.cutoffSmoothLastPiston = BoolArgumentType.getBool(ctx, "On/Off");
            ctx.getSource().sendFeedback(Text.translatable("oldpistons.cutoffSmoothLastPistonCommand.set", settings.cutoffSmoothLastPiston));
        } else {
            ctx.getSource().sendFeedback(Text.translatable("oldpistons.cutoffSmoothLastPistonCommand.get", settings.cutoffSmoothLastPiston));
        }

        return 1;
    }

    public int cutoffPistonsCommand(CommandContext<FabricClientCommandSource> ctx, boolean bl) {
        if (bl) {
            settings.cutoffPistons = BoolArgumentType.getBool(ctx, "On/Off");
            ctx.getSource().sendFeedback(Text.translatable("oldpistons.cutoffPistonsCommand.set", settings.cutoffPistons));
        } else {
            ctx.getSource().sendFeedback(Text.translatable("oldpistons.cutoffPistonsCommand.get", settings.cutoffPistons));
        }

        return 1;
    }

    public int changePistonPitchCommand(CommandContext<FabricClientCommandSource> ctx, boolean bl) {
        if (bl) {
            settings.modifyPistonPitch = BoolArgumentType.getBool(ctx, "On/Off");
            ctx.getSource().sendFeedback(Text.translatable("oldpistons.changePistonPitchCommand.set", settings.modifyPistonPitch));
        } else {
            ctx.getSource().sendFeedback(Text.translatable("oldpistons.changePistonPitchCommand.get", settings.modifyPistonPitch));
        }

        return 1;
    }

}
