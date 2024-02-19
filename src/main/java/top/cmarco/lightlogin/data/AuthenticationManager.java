package top.cmarco.lightlogin.data;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import top.cmarco.lightlogin.api.PlayerAuthenticateEvent;

import java.util.UUID;

public interface AuthenticationManager {

    void startLoginNotifyTask();

    void startRegisterNotifyTask();

    default boolean isAuthenticated(@NotNull Player player) {
        return this.isAuthenticated(player.getUniqueId());
    }

    boolean isAuthenticated(@NotNull UUID playerUuid);

    default void authenticate(@NotNull final Player player) {
        this.authenticate(player.getUniqueId());
    }

    void addUnregistered(final Player player);

    void removeUnregistered(final Player player);

    void addUnloginned(final Player player);

    void removeUnloginned(final Player player);

    void addUnregistered(final UUID uuid);

    void removeUnregistered(final UUID uuid);

    void addUnloginned(final UUID uuid);

    void removeUnloginned(final UUID uuid);

    void authenticate(@NotNull UUID playerUuid);

    default void unauthenticate(@NotNull Player player) {
        this.unauthenticate(player.getUniqueId());
    }

    void unauthenticate(@NotNull UUID playerUuid);
}
