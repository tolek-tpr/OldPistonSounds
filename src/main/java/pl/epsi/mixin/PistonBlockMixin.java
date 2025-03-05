package pl.epsi.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.PistonBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import pl.epsi.settings.OldPistonSettings;
import pl.epsi.OldPistonSounds;
import pl.epsi.PistonCutoffManager;

@Mixin(PistonBlock.class)
public class PistonBlockMixin {

	@Unique
	private PistonCutoffManager manager = PistonCutoffManager.getInstance();
	@Unique
	private final OldPistonSettings settings = OldPistonSettings.getInstance();

	@WrapOperation(method = "onSyncedBlockEvent", at = @At(
			value = "INVOKE", target = "net/minecraft/world/World.playSound (Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V",
			ordinal = 0
	))
	private void overwriteExtensionSound(World instance, PlayerEntity source, BlockPos pos, SoundEvent sound, SoundCategory category,
									   float volume, float pitch, Operation<Void> original) {
		if (manager == null) {
			OldPistonSounds.LOGGER.warn("Old Piston Sound Manager is null!");
			manager = PistonCutoffManager.getInstance();
		}
		if (manager == null) original.call(instance, source, pos, sound, category, volume, pitch);

		manager.increasePistonsFired();
		manager.resetTicksSinceLastPiston();

		if (settings.modifyPistonPitch) {
			instance.playSound((Entity) null, pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 0.9F,
					0.635F + instance.random.nextFloat() * 0.20F);
		} else {
			original.call(instance, source, pos, sound, category, volume, pitch);
		}
    }

	@WrapOperation(method = "onSyncedBlockEvent", at = @At(
			value = "INVOKE", target = "net/minecraft/world/World.playSound (Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V",
			ordinal = 1
	))
	private void overwriteRetractionSound(World instance, PlayerEntity source, BlockPos pos, SoundEvent sound, SoundCategory category,
									   float volume, float pitch, Operation<Void> original) {
		if (manager == null) {
			OldPistonSounds.LOGGER.warn("Old Piston Sound Manager is null!");
			manager = PistonCutoffManager.getInstance();
		}

		if (manager == null) original.call(instance, source, pos, sound, category, volume, pitch);

		manager.increasePistonsFired();
		manager.resetTicksSinceLastPiston();
		manager.addPistonSoundEvent(new PistonCutoffManager.PistonSoundEvent(pos));

		if (settings.modifyPistonPitch) {
			instance.playSound((Entity) null, pos, SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.BLOCKS, 0.9F,
					0.635F + instance.random.nextFloat() * 0.20F);
		} else {
			original.call(instance, source, pos, sound, category, volume, pitch);
		}
	}

}