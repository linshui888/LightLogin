package top.cmarco.lightlogin.data;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import top.cmarco.lightlogin.LightLoginPlugin;
import top.cmarco.lightlogin.api.PlayerAuthenticateEvent;
import top.cmarco.lightlogin.command.LightLoginCommand;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

public class BasicAuthenticationManager implements AuthenticationManager {

    protected final LightLoginPlugin plugin;

    protected final Set<UUID> authenticatedSet = new CopyOnWriteArraySet<>();
    protected final Set<UUID> unregisteredSet = new CopyOnWriteArraySet<>();
    protected final Set<UUID> unloginnedSet = new CopyOnWriteArraySet<>();

    private BukkitTask loginMsg = null, registerMsg = null;

    public BasicAuthenticationManager(@NotNull LightLoginPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void startLoginNotifyTask() {
        if (loginMsg != null) {
            loginMsg.cancel();
            loginMsg = null;
        }

        this.loginMsg = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            plugin.getServer().getOnlinePlayers().stream()
                    .filter(p -> unloginnedSet.contains(p.getUniqueId()))
                    .forEach(p -> LightLoginCommand.sendColorPrefixMessages(p, plugin.getLightConfiguration().getLoginMessages(), plugin));
            }
        , 1L, 20L*5L);
    }

    @Override
    public void startRegisterNotifyTask() {
        if (registerMsg != null) {
            registerMsg.cancel();
            registerMsg = null;
        }

        this.registerMsg = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
                    plugin.getServer().getOnlinePlayers().stream()
                            .filter(p -> unregisteredSet.contains(p.getUniqueId()))
                            .forEach(p -> LightLoginCommand.sendColorPrefixMessages(p, plugin.getLightConfiguration().getRegisterMessage(), plugin));
                }
                , 1L, 20L*5L);
    }

    @Override
    public boolean isAuthenticated(@NotNull UUID playerUuid) {
        return this.authenticatedSet.contains(playerUuid);
    }

    @Override
    public void authenticate(@NotNull UUID playerUuid) {
        this.authenticatedSet.add(playerUuid);
        removeUnloginned(playerUuid);
        removeUnregistered(playerUuid);
    }

    @Override
    public void authenticate(@NotNull Player player) {
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            PlayerAuthenticateEvent playerAuthenticateEvent = new PlayerAuthenticateEvent(player);
            this.plugin.getServer().getPluginManager().callEvent(playerAuthenticateEvent);
        });
        this.authenticate(player.getUniqueId());
    }

    @Override
    public void addUnregistered(Player player) {
        addUnregistered(player.getUniqueId());
    }

    @Override
    public void removeUnregistered(Player player) {
        removeUnregistered(player.getUniqueId());
    }

    @Override
    public void addUnloginned(Player player) {
        addUnloginned(player.getUniqueId());
    }

    @Override
    public void removeUnloginned(Player player) {
        removeUnloginned(player.getUniqueId());
    }

    @Override
    public void addUnregistered(UUID uuid) {
        this.unregisteredSet.add(uuid);
        removeUnloginned(uuid);
        unauthenticate(uuid);
    }

    @Override
    public void removeUnregistered(UUID uuid) {
        this.unregisteredSet.remove(uuid);
    }

    @Override
    public void addUnloginned(UUID uuid) {
        this.unloginnedSet.add(uuid);
        removeUnregistered(uuid);
        unauthenticate(uuid);
    }

    @Override
    public void removeUnloginned(UUID uuid) {
        this.unloginnedSet.remove(uuid);
    }

    @Override
    public void unauthenticate(@NotNull UUID playerUuid) {
        this.authenticatedSet.remove(playerUuid);
    }
}
