package dk.tij.jchessfx.struct;

public class KeyInfo {
    private final String name;
    private final Runnable action;
    private double cooldown;

    public KeyInfo(String name, Runnable action) {
        this.name = name;
        this.action = action;
        cooldown = 0;
    }

    public String getName() {
        return name;
    }

    public Runnable getAction() {
        return action;
    }

    public double getCooldown() {
        return cooldown;
    }

    public void setCooldown(double cooldown) {
        this.cooldown = cooldown;
    }
}
