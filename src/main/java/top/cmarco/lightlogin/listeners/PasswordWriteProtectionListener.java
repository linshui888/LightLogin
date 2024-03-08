package top.cmarco.lightlogin.listeners;

import org.jetbrains.annotations.NotNull;
import top.cmarco.lightlogin.LightLoginPlugin;

public final class PasswordWriteProtectionListener {
    private final LightLoginPlugin plugin;

    public PasswordWriteProtectionListener(@NotNull LightLoginPlugin plugin) {
        this.plugin = plugin;
    }
}
