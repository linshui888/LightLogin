package top.cmarco.lightlogin.data;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class VoidLoginManager {

    private final World loginWorld;

    private final ConcurrentHashMap<UUID, Location> joinLocation = new ConcurrentHashMap<>();

    public VoidLoginManager(@NotNull World loginWorld) {
        this.loginWorld = Objects.requireNonNull(loginWorld);
    }

    public void sendLoginToVoid(@NotNull Player player) {
        this.setJoinLocation(player, player.getLocation());
        player.teleport(this.loginWorld.getSpawnLocation());
    }

    public void sendToLastLocation(@NotNull Player player) {
        Location lastLocation = this.joinLocation.get(player.getUniqueId());
        if (lastLocation != null) {
            player.teleport(lastLocation);
        }
    }

    @Nullable
    public Location getLocation(@NotNull Player player) {
        return this.joinLocation.get(player.getUniqueId());
    }

    public void setJoinLocation(@NotNull Player player, @NotNull Location location) {
        this.joinLocation.put(player.getUniqueId(), location);
    }

    public void clearLastLocation(@NotNull UUID uuid) {
        this.joinLocation.remove(uuid);
    }
}
