package pl.epsi.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.epsi.PistonCutoffManager;
import pl.epsi.SoundUtils;
import pl.epsi.settings.FileLoader;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Unique
    private final PistonCutoffManager oldPistonsCutoffManager = PistonCutoffManager.getInstance();

    @Inject(method = "tick", at = @At("TAIL"))
    private void tickTail(CallbackInfo ci) {
        oldPistonsCutoffManager.tick();
        SoundUtils.getInstance().tick();
    }

    @Inject(method = "close", at = @At("HEAD"))
    private void close(CallbackInfo ci) {
        new FileLoader().save();
    }

    @Inject(method = "run", at = @At("HEAD"))
    private void run(CallbackInfo ci) {
        new FileLoader().load();
    }

}
