package pl.epsi.settings;

public class OldPistonSettings {

    private static OldPistonSettings instance;

    private OldPistonSettings() {
        this.setToDefault();
    }

    public static OldPistonSettings getInstance() {
        if (instance == null) instance = new OldPistonSettings();
        return instance;
    }

    public void setToDefault() {
        this.profile = "default";
        cutoffPistons = defaultCutoffPistons;
        modifyPistonPitch = defaultModifyPistonPitch;
        cutoffSmoothLastPiston = defaultCutoffSmoothLastPiston;
        pistonSoundThreshold = defaultPistonSoundThreshold;
        cutoffTime = defaultCutoffTime;
    }

    public void setCustomProfile() {
        this.profile = "custom";
    }

    public boolean cutoffPistons = true;
    public boolean modifyPistonPitch = true;
    public boolean cutoffSmoothLastPiston = false;
    public int pistonSoundThreshold = 8;
    public int cutoffTime = 2;
    public String profile = "default";

    public transient boolean defaultCutoffPistons = true;
    public transient boolean defaultModifyPistonPitch = true;
    public transient boolean defaultCutoffSmoothLastPiston = false;
    public transient int defaultPistonSoundThreshold = 8;
    public transient int defaultCutoffTime = 2;

}
