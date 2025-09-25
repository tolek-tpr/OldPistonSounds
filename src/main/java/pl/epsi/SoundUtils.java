package pl.epsi;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

import java.util.ArrayList;

public class SoundUtils {

    private static SoundUtils instance;

    private SoundUtils() {}

    public static SoundUtils getInstance() {
        if (instance == null) instance = new SoundUtils();
        return instance;
    }

    private MinecraftClient client = MinecraftClient.getInstance();
    //public final Random random = Random.create();

    private final ArrayList<WrappedSound> scheduledSounds = new ArrayList<>();

    public void tick() {
        for (int i = 0; i < scheduledSounds.size(); i++) {
            this.play(scheduledSounds.get(i));
        }

        scheduledSounds.clear();
    }

    private void play(WrappedSound ws) {
        if (client == null) client = MinecraftClient.getInstance();
        if (client == null) return;

        double d = this.client.gameRenderer.getCamera().getPos().squaredDistanceTo(ws.sound.getX(), ws.sound.getY(), ws.sound.getZ());
        if (ws.distance && d > 100.0) {
            double e = Math.sqrt(d) / 40.0;
            this.client.getSoundManager().play(ws.sound, (int)(e * 20.0));
        } else {
            this.client.getSoundManager().play(ws.sound);
        }
    }

    public SoundInstance scheduleSound(double x, double y, double z, SoundEvent event, SoundCategory category, float volume, float pitch, boolean useDistance, long seed) {
        PositionedSoundInstance positionedSoundInstance = new PositionedSoundInstance(event, category, volume, pitch, Random.create(seed), x, y, z);

        this.scheduledSounds.add(new WrappedSound(positionedSoundInstance, useDistance));

        return positionedSoundInstance;
    }

    public SoundInstance scheduleSound(double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch, boolean useDistance) {
        return this.scheduleSound(x, y, z, sound, category, volume, pitch, useDistance, 10);
    }

    public SoundInstance scheduleSound(BlockPos pos, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        return this.scheduleSound(pos.getX(), pos.getY(), pos.getZ(), sound, category, volume, pitch, false);
    }

    record WrappedSound(SoundInstance sound, boolean distance) {}

}
