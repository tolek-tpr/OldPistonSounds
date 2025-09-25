package pl.epsi;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.epsi.settings.OldPistonSettings;

import java.util.ArrayList;

public class PistonCutoffManager {

    private static PistonCutoffManager instance;

    private PistonCutoffManager() {}

    public static PistonCutoffManager getInstance() {
        if (instance == null) instance = new PistonCutoffManager();
        return instance;
    }

    private final OldPistonSettings settings = OldPistonSettings.getInstance();
    private final ArrayList<PistonSoundEvent> overloadedTicks = new ArrayList<>();
    private final ArrayList<PistonSoundEvent> tempSoundEvents = new ArrayList<>();
    private final ArrayList<PistonSoundEvent> tempRemove = new ArrayList<>();

    private MinecraftClient client = MinecraftClient.getInstance();

    private int pistonsFiredInGameTick = 0;
    private int ticksSinceLastPiston = 10;

    public void increasePistonsFired() { this.pistonsFiredInGameTick++; }
    public void resetTicksSinceLastPiston() { this.ticksSinceLastPiston = 0; }

    public void tick() {
        if (client == null) client = MinecraftClient.getInstance();
        int pistonSoundThreshold = settings.pistonSoundThreshold * 2;
        int cutoffTime = settings.cutoffTime;

        if (pistonsFiredInGameTick > pistonSoundThreshold) {
            overloadedTicks.addAll(tempSoundEvents);
        }

        for (int i = 0; i < overloadedTicks.size(); i++) {
            var pistonEvent = overloadedTicks.get(i);
            if (pistonEvent == null) continue;

            if (pistonEvent.ticksSince >= cutoffTime && ticksSinceLastPiston < 3 && settings.cutoffPistons) {
                pistonEvent.cancelSound();
            }

            if (ticksSinceLastPiston == 3 && client != null && client.player != null && client.player.getWorld() != null && settings.cutoffPistons) {
                if (settings.cutoffSmoothLastPiston) {
                    pistonEvent.playSound(client.player.getWorld(), client.player);
                }

                tempRemove.add(pistonEvent);
            }
        }

        for (int i = 0; i < tempRemove.size(); i++) {
            overloadedTicks.remove(tempRemove.get(i));
        }

        overloadedTicks.forEach((a) -> {
            if (a != null) a.increment();
        });

        tempSoundEvents.clear();
        tempRemove.clear();
        this.pistonsFiredInGameTick = 0;
        this.ticksSinceLastPiston++;
    }

    public void addPistonSoundEvent(PistonSoundEvent e) { this.tempSoundEvents.add(e); }

    public static class PistonSoundEvent {

        private final BlockPos pos;
        private final SoundInstance soundInstance;
        private int ticksSince;

        public PistonSoundEvent(BlockPos pos, SoundInstance soundInstance) {
            this.pos = pos;
            this.ticksSince = 0;
            this.soundInstance = soundInstance;
        }

        public void increment() { this.ticksSince++; }
        public SoundInstance getSound() { return this.soundInstance; }

        public void playSound(World w, PlayerEntity player) {
            w.playSound(player, this.pos, SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.BLOCKS, 0.45F,
                0.535F + w.random.nextFloat() * 0.20F);
        }

        public void cancelSound() {
            MinecraftClient.getInstance().getSoundManager().stop(this.soundInstance);
        }

    }

}
