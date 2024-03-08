package top.cmarco.lightlogin.data;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

public final class StartupLoginsManager {

    private final Set<UUID> firstLoginSinceStartup = new CopyOnWriteArraySet<>();

    public void addPlayer(@NotNull Player player) {
        if (!contains(player)) {
            this.firstLoginSinceStartup.add(player.getUniqueId());
        }
    }

    public boolean contains(@NotNull Player player) {
        return this.contains(player.getUniqueId());
    }

    public boolean contains(@NotNull UUID uuid) {
        return this.firstLoginSinceStartup.contains(uuid);
    }

}
