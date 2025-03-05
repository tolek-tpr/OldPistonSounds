package pl.epsi;

import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import pl.epsi.settings.OldPistonSettings;

public class PistonCutoffManager {

    private static PistonCutoffManager instance;

    private PistonCutoffManager() {}

    public static PistonCutoffManager getInstance() {
        if (instance == null) instance = new PistonCutoffManager();
        return instance;
    }

    public int threshold = 0;
    public int timeSincePistonFireInTicks = 0;
    public int timeSinceCutout = 0;
    private boolean synced = false;

    private MinecraftClient client = MinecraftClient.getInstance();
    private final OldPistonSettings settings = OldPistonSettings.getInstance();

    public void tick() {
        if (this.client == null) client = MinecraftClient.getInstance();
        if (this.client == null || !settings.cutoffPistons) return;

        timeSincePistonFireInTicks++;
        timeSinceCutout++;
        if (timeSincePistonFireInTicks > 2000000000) timeSincePistonFireInTicks = 2;
        if (timeSinceCutout > 2) timeSinceCutout = 0;

        if (timeSincePistonFireInTicks < 2 && threshold >= 8 && timeSinceCutout > 1) {
            this.client.getSoundManager().stopSounds(Identifier.of("block.piston.extend"), SoundCategory.BLOCKS);
            this.client.getSoundManager().stopSounds(Identifier.of("block.piston.contract"), SoundCategory.BLOCKS);
        } else {
            synced = false;
        }
    }

    public void sync() {
        if (this.client == null) client = MinecraftClient.getInstance();
        if (this.client == null || this.synced || !settings.cutoffPistons) return;

        timeSinceCutout = 0;
        this.synced = true;

        if (timeSincePistonFireInTicks < 2 && threshold >= 8) {
            this.client.getSoundManager().stopSounds(Identifier.of("block.piston.extend"), SoundCategory.BLOCKS);
            this.client.getSoundManager().stopSounds(Identifier.of("block.piston.contract"), SoundCategory.BLOCKS);
        }
    }

}
