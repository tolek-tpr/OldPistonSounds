package pl.epsi;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class PistonUtils {

    public static void playOldPistonSound(World instance, BlockPos pos, SoundEvent sound) {
        instance.playSound((Entity) null, pos, sound, SoundCategory.BLOCKS, 1.5F,
                0.535F + instance.random.nextFloat() * 0.20F);
    }

}
