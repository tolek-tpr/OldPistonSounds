package pl.epsi;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
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

    private final int PISTON_SOUND_THRESHOLD = 16;
    private final int CUTOFF_TIME = 2; // GameTicks
    private final OldPistonSettings settings = OldPistonSettings.getInstance();
    private final ArrayList<Integer> timeSinceOverloadedTick = new ArrayList<>();
    private final ArrayList<PistonSoundEvent> soundEvents = new ArrayList<>();
    private final ArrayList<PistonSoundEvent> tempAddedInTick = new ArrayList<>();

    private MinecraftClient client = MinecraftClient.getInstance();

    private int pistonsFiredInGameTick = 0;
    private int ticksSinceLastPiston = 10;

    public void increasePistonsFired() { this.pistonsFiredInGameTick++; }
    public void resetTicksSinceLastPiston() { this.ticksSinceLastPiston = 0; }

    public void tick() {
        if (client == null) client = MinecraftClient.getInstance();

        if (pistonsFiredInGameTick > PISTON_SOUND_THRESHOLD) {
            timeSinceOverloadedTick.add(0);
        } else {
            tempAddedInTick.forEach(soundEvents::remove);
        }

        if (ticksSinceLastPiston == 3 && client != null && client.player != null && client.player.getWorld() != null && settings.cutoffPistons) {
            soundEvents.forEach(e -> {
                if (e.ticksSince >= 3) e.playSound(client.player.getWorld(), client.player);
            });

            timeSinceOverloadedTick.clear();
            soundEvents.clear();
        }

        for (int i = 0; i < timeSinceOverloadedTick.size(); i++) {
            int tick = timeSinceOverloadedTick.get(i);

            if (tick >= CUTOFF_TIME && client != null && settings.cutoffPistons && ticksSinceLastPiston != 3) {
                client.getSoundManager().stopSounds(Identifier.of("block.piston.extend"), SoundCategory.BLOCKS);
                client.getSoundManager().stopSounds(Identifier.of("block.piston.contract"), SoundCategory.BLOCKS);
                timeSinceOverloadedTick.remove(i);
            } else {
                // Increment
                timeSinceOverloadedTick.remove(i);
                timeSinceOverloadedTick.add(i, ++tick);
            }
        }

        pistonsFiredInGameTick = 0;
        ticksSinceLastPiston++;
        soundEvents.forEach(PistonSoundEvent::increment);
        tempAddedInTick.clear();
    }

    public void addPistonSoundEvent(PistonSoundEvent e) {
        this.soundEvents.add(e);
        this.tempAddedInTick.add(e);
    }

    public static class PistonSoundEvent {

        private final BlockPos pos;
        private int ticksSince;

        public PistonSoundEvent(BlockPos pos) {
            this.pos = pos;
            this.ticksSince = 0;
        }

        public void increment() { this.ticksSince++; }

        public void playSound(World w, PlayerEntity player) {
            w.playSound(player, this.pos, SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.BLOCKS, 0.45F,
                0.635F + w.random.nextFloat() * 0.20F);
        }

    }

}
