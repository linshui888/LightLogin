package top.cmarco.lightlogin.data;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class PlaintextPasswordManager {

    private final Map<UUID, String> plaintextPasswordHolder = new ConcurrentHashMap<>();

    public void setPassword(@NotNull Player player, @NotNull String password) {
        this.plaintextPasswordHolder.put(player.getUniqueId(), password);
    }

    @Nullable
    public String getPassword(@NotNull Player player) {
        return this.plaintextPasswordHolder.get(player.getUniqueId());
    }
}
