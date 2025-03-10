package pl.epsi;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class SoundUtils {

    private static SoundUtils instance;

    private SoundUtils() {}

    public static SoundUtils getInstance() {
        if (instance == null) instance = new SoundUtils();
        return instance;
    }

    private MinecraftClient client = MinecraftClient.getInstance();
    public final Random random = Random.create();

    public SoundInstance playSound(double x, double y, double z, SoundEvent event, SoundCategory category, float volume, float pitch, boolean useDistance, long seed) {
        if (client == null) client = MinecraftClient.getInstance();
        if (client == null) return null;
        double d = this.client.gameRenderer.getCamera().getPos().squaredDistanceTo(x, y, z);
        PositionedSoundInstance positionedSoundInstance = new PositionedSoundInstance(event, category, volume, pitch, Random.create(seed), x, y, z);
        if (useDistance && d > 100.0) {
            double e = Math.sqrt(d) / 40.0;
            this.client.getSoundManager().play(positionedSoundInstance, (int)(e * 20.0));
        } else {
            this.client.getSoundManager().play(positionedSoundInstance);
        }

        return positionedSoundInstance;
    }

    public SoundInstance playSound(double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch, boolean useDistance) {
        return this.playSound(x, y, z, sound, category, volume, pitch, useDistance, this.random.nextLong());
    }

    public SoundInstance playSound(BlockPos pos, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        return this.playSound(pos.getX(), pos.getY(), pos.getZ(), sound, category, volume, pitch, false);
    }

}
