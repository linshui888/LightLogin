package top.cmarco.lightlogin.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;
import top.cmarco.lightlogin.LightLoginPlugin;
import top.cmarco.lightlogin.api.PlayerAuthenticateEvent;
import top.cmarco.lightlogin.api.PlayerRegisterEvent;
import top.cmarco.lightlogin.api.PlayerUnauthenticateEvent;
import top.cmarco.lightlogin.api.UnregisterEvent;
import top.cmarco.lightlogin.data.VoidLoginManager;
import top.cmarco.lightlogin.log.AuthLogs;

public class AuthenticationListener implements Listener {

    private final LightLoginPlugin plugin;
    private final VoidLoginManager voidLoginManager;

    public AuthenticationListener(@NotNull final LightLoginPlugin plugin) {
        this.plugin = plugin;
        this.voidLoginManager = plugin.getVoidLoginManager();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public final void onAuth(PlayerAuthenticateEvent event) {
        Player player = event.getPlayer();

        plugin.getStartupLoginsManager().addPlayer(player);

        plugin.getServer().getOnlinePlayers().stream()
                .filter(p -> !p.equals(player))
                .forEach(p -> p.showPlayer(plugin, player));

        AuthLogs authLogs = plugin.getAuthLogs();
        authLogs.add("Player " + player.getName() + " has been authenticated through " + event.getAuthenticationCause().getFormalName());

        if (voidLoginManager == null) {
            return;
        }
        voidLoginManager.sendToLastLocation(player);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public final void onUnauth(PlayerUnauthenticateEvent event) {
        Player player = event.getPlayer();
        AuthLogs authLogs = plugin.getAuthLogs();
        authLogs.add("Player " + player.getName() + " has been unauthenticated through " + event.getAuthenticationCause().getFormalName());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public final void onRegister(PlayerRegisterEvent event) {
        Player player = event.getPlayer();
        AuthLogs authLogs = plugin.getAuthLogs();
        authLogs.add("Player " + player.getName() + " has successfully registered.");
    }

    @EventHandler(priority = EventPriority.HIGH)
    public final void onUnregister(UnregisterEvent event) {
        AuthLogs authLogs = plugin.getAuthLogs();
        authLogs.add("Player " + event.getName() + " has been unregistered.");

        if (voidLoginManager == null) {
            return;
        }
        this.voidLoginManager.clearLastLocation(event.getUuid());
    }
}
