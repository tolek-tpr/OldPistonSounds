package pl.epsi.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
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
                                    .executes(ctx -> {
                settings.cutoffPistons = BoolArgumentType.getBool(ctx, "On/Off");
                ctx.getSource().sendFeedback(Text.literal("Set Cutoff Pistons to " + BoolArgumentType.getBool(ctx, "On/Off")));
                return 1;
            }))).then(literal("changePistonPitch")
                            .then(argument("On/Off", BoolArgumentType.bool())
                                    .executes(ctx -> {
                                        settings.modifyPistonPitch = BoolArgumentType.getBool(ctx, "On/Off");
                                        ctx.getSource().sendFeedback(Text.literal("Set Change Piston Pitch to " + BoolArgumentType.getBool(ctx, "On/Off")));
                                        return 1;
            }))));

        });
    }

}
