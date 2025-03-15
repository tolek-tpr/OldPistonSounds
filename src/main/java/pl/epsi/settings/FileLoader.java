package pl.epsi.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import pl.epsi.OldPistonSounds;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileLoader {

    private final Gson gson;
    private final OldPistonSettings settings = OldPistonSettings.getInstance();

    public FileLoader() {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void load() {
        try (FileReader reader = new FileReader("old_pistons.json", StandardCharsets.UTF_8)) {
            ModData loaded = gson.fromJson(reader, ModData.class);

            settings.cutoffPistons = loaded.settings.cutoffPistons;
            settings.modifyPistonPitch = loaded.settings.modifyPistonPitch;
            settings.cutoffSmoothLastPiston = loaded.settings.cutoffSmoothLastPiston;
            settings.pistonSoundThreshold = loaded.settings.pistonSoundThreshold;
            settings.cutoffTime = loaded.settings.cutoffTime;
            settings.profile = loaded.settings.profile;

            if (settings.profile.equalsIgnoreCase("default")) settings.setToDefault();
        } catch (IOException e) {
            OldPistonSounds.LOGGER.warn("Old Pistons save file not found!");
        } catch (JsonIOException e) {
            OldPistonSounds.LOGGER.warn("Json Error while loading Old Pistons save file", e);
        }
    }

    public void save() {
        ModData data = new ModData(OldPistonSettings.getInstance());
        try (FileWriter writer = new FileWriter("old_pistons.json", StandardCharsets.UTF_8)) {
            gson.toJson(data, writer);
            writer.close();
        } catch (IOException e) {
            OldPistonSounds.LOGGER.error("Failed to save!", e);
        }
    }

    public record ModData(OldPistonSettings settings) {}

}
