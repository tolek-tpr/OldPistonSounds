package pl.epsi.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.epsi.PistonCutoffManager;
import pl.epsi.settings.FileLoader;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Unique
    private final PistonCutoffManager soundManager = PistonCutoffManager.getInstance();

    @Inject(method = "tick", at = @At("RETURN"))
    private void tickTail(CallbackInfo ci) {
        soundManager.tick();
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
