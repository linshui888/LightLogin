package top.cmarco.lightlogin.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import top.cmarco.lightlogin.LightLoginPlugin;
import top.cmarco.lightlogin.command.LightLoginCommand;
import top.cmarco.lightlogin.configuration.LightConfiguration;
import top.cmarco.lightlogin.data.AuthenticationManager;
import top.cmarco.lightlogin.database.LightLoginColumn;
import top.cmarco.lightlogin.database.PluginDatabase;
import top.cmarco.lightlogin.network.NetworkUtilities;

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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        this.database.searchRowFromPK(player.getUniqueId().toString())
                .whenComplete((row, throwable) -> {

                    if (throwable != null) {
                        this.plugin.getLogger().warning(throwable.getLocalizedMessage());
                        return;
                    }

                    if (row == null) {
                        if (player.isOnline()) {
                            LightLoginCommand.sendColorPrefixMessages(player, this.configuration.getRegisterMessage(), this.plugin);
                        }
                        return;
                    }

                    final long lastLogin = row.lastLogin();
                    final long lastIpv4 = row.last_ipv4();

                    final long currentIpv4 = NetworkUtilities.convertInetSocketAddressToLong(player.getAddress());
                    if (currentIpv4 != lastIpv4) {
                        authManager.unauthenticate(player);
                        if (player.isOnline()) {
                            LightLoginCommand.sendColorPrefixMessages(player, this.configuration.getLoginMessages(), this.plugin);
                        }
                        return;
                    }

                    final long timeNow = System.currentTimeMillis();
                    if ((timeNow - lastLogin) / 1E3 <= this.configuration.getSessionExpire()) {
                        this.authManager.authenticate(player);
                        LightLoginCommand.sendColorPrefixMessages(player, this.configuration.getLoginAuto(), this.plugin);
                    } else {
                        LightLoginCommand.sendColorPrefixMessages(player, this.configuration.getLoginMessages(), this.plugin);
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
        this.database.updateRow(player.getUniqueId().toString(), LightLoginColumn.LAST_IPV4, System.currentTimeMillis());

        authManager.unauthenticate(player);
    }
}
