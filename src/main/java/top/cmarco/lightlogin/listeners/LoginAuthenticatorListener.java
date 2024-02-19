package top.cmarco.lightlogin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import top.cmarco.lightlogin.LightLoginPlugin;
import top.cmarco.lightlogin.command.LightLoginCommand;
import top.cmarco.lightlogin.configuration.LightConfiguration;
import top.cmarco.lightlogin.data.AuthenticationManager;
import top.cmarco.lightlogin.database.LightLoginColumn;
import top.cmarco.lightlogin.database.PluginDatabase;
import top.cmarco.lightlogin.network.NetworkUtilities;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public final class LoginAuthenticatorListener implements Listener {

    private final LightLoginPlugin plugin;
    private final AuthenticationManager authManager;
    private final PluginDatabase database;
    private final LightConfiguration configuration;

    public LoginAuthenticatorListener(LightLoginPlugin plugin) {
        this.plugin = plugin;
        this.authManager = this.plugin.getAuthenticationManager();
        this.database = this.plugin.getDatabase();
        this.configuration = this.plugin.getLightConfiguration();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void preJoin(final AsyncPlayerPreLoginEvent event) {
        final InetAddress inetAddress = event.getAddress();
        int inetMatchResult = 0x00;
        for (final Player player : this.plugin.getServer().getOnlinePlayers()) {
            final InetAddress tempAddr = player.getAddress().getAddress();
            if (tempAddr.equals(inetAddress)) {
                ++inetMatchResult;
            }
        }

        final LightConfiguration lightConfiguration = this.plugin.getLightConfiguration();

        if (inetMatchResult < lightConfiguration.getPlayersSameIp()) {
            return;
        }

        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                LightLoginCommand.colorAndReplace(lightConfiguration.getMessagePlayersSameIp(), plugin));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

         plugin.getServer().getOnlinePlayers().stream()
        .filter(p -> !p.equals(player))
        .forEach(p -> p.hidePlayer(plugin, player));

        this.database.searchRowFromPK(player.getUniqueId().toString())
                .whenComplete((row, throwable) -> {

                    if (throwable != null) {
                        this.plugin.getLogger().warning(throwable.getLocalizedMessage());
                        return;
                    }

                    if (row == null) {
                        if (player.isOnline()) {
                            authManager.addUnregistered(player);
                            // LightLoginCommand.sendColorPrefixMessages(player, this.configuration.getRegisterMessage(), this.plugin);
                        }
                        return;
                    }

                    final long lastLogin = row.getLastLogin();
                    final long lastIpv4 = row.getLastIpv4();

                    final long currentIpv4 = NetworkUtilities.convertInetSocketAddressToLong(player.getAddress());
                    if (currentIpv4 != lastIpv4) {
                        authManager.unauthenticate(player);
                        authManager.addUnloginned(player);
                        if (player.isOnline()) {
                            // LightLoginCommand.sendColorPrefixMessages(player, this.configuration.getLoginMessages(), this.plugin);
                        }
                        return;
                    }

                    final long timeNow = System.currentTimeMillis();
                    if ((timeNow - lastLogin) / 1E3 <= this.configuration.getSessionExpire()) {
                        this.authManager.authenticate(player);
                        LightLoginCommand.sendColorPrefixMessages(player, this.configuration.getLoginAuto(), this.plugin);
                    } else {
                        authManager.addUnloginned(player);
                        // LightLoginCommand.sendColorPrefixMessages(player, this.configuration.getLoginMessages(), this.plugin);
                    }

                });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLeave(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        if (!authManager.isAuthenticated(player)) {
            return;
        }

        this.database.updateRow(player.getUniqueId().toString(), LightLoginColumn.LAST_LOGIN, System.currentTimeMillis());
        this.database.updateRow(player.getUniqueId().toString(), LightLoginColumn.LAST_IPV4, NetworkUtilities.convertInetSocketAddressToLong(player.getAddress()));

        authManager.unauthenticate(player);
    }
}
