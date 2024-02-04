package top.cmarco.lightlogin.data;

import org.jetbrains.annotations.NotNull;
import top.cmarco.lightlogin.LightLoginPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

public class BasicAuthenticationManager implements AuthenticationManager {

    protected final LightLoginPlugin plugin;

    protected final Set<UUID> authenticatedSet = new CopyOnWriteArraySet<>();

    public BasicAuthenticationManager(@NotNull LightLoginPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isAuthenticated(@NotNull UUID playerUuid) {
        return this.authenticatedSet.contains(playerUuid);
    }

    @Override
    public void authenticate(@NotNull UUID playerUuid) {
        this.authenticatedSet.add(playerUuid);
    }

    @Override
    public void unauthenticate(@NotNull UUID playerUuid) {
        this.authenticatedSet.remove(playerUuid);
    }
}
