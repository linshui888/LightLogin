package top.cmarco.lightlogin.data;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface AuthenticationManager {

    default boolean isAuthenticated(@NotNull Player player) {
        return this.isAuthenticated(player.getUniqueId());
    }

    boolean isAuthenticated(@NotNull UUID playerUuid);

    default void authenticate(@NotNull Player player) {
        this.authenticate(player.getUniqueId());
    }

    void authenticate(@NotNull UUID playerUuid);

    default void unauthenticate(@NotNull Player player) {
        this.unauthenticate(player.getUniqueId());
    }

    void unauthenticate(@NotNull UUID playerUuid);
}
