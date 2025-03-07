package pl.epsi.settings;

public class OldPistonSettings {

    private static OldPistonSettings instance;

    private OldPistonSettings() {}

    public static OldPistonSettings getInstance() {
        if (instance == null) instance = new OldPistonSettings();
        return instance;
    }

    public boolean cutoffPistons = true;
    public boolean modifyPistonPitch = true;
    public boolean cutoffSmoothLastPiston = true;
    public int pistonSoundThreshold = 16;
    public int cutoffTime = 2;

}
