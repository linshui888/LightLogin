package top.cmarco.lightlogin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import top.cmarco.lightlogin.LightLoginPlugin;
import top.cmarco.lightlogin.api.PlayerAuthenticateEvent;

public class AuthenticationListener implements Listener {

    private final LightLoginPlugin plugin;


    public AuthenticationListener(@NotNull final LightLoginPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public final void onAuth(PlayerAuthenticateEvent event) {

         plugin.getServer().getOnlinePlayers().stream()
                .filter(p -> !p.equals(event.getPlayer()))
                .forEach(p -> p.showPlayer(plugin, event.getPlayer()));

    }
}
