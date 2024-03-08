package top.cmarco.lightlogin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;
import top.cmarco.lightlogin.LightLoginPlugin;
import top.cmarco.lightlogin.command.LightLoginCommand;
import top.cmarco.lightlogin.data.PlaintextPasswordManager;

import java.util.stream.Collectors;

public final class PasswordObfuscationListener implements Listener {

    private final LightLoginPlugin plugin;

    public PasswordObfuscationListener(@NotNull final LightLoginPlugin plugin) {
        this.plugin = plugin;
    }

    private void sendWarning(@NotNull final Player player) {
        LightLoginCommand.sendColorPrefixMessages(player,
                plugin.getLightConfiguration().getPasswordInChat(), this.plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        String message = event.getMessage();

        final PlaintextPasswordManager plaintextManager = this.plugin.getPlaintextPasswordManager();
        final String plaintextPassword = plaintextManager.getPassword(player);

        if (plaintextPassword == null) {
            return;
        }

        final String[] splitMessage = message.split("\\s+");
        for (final String arg : splitMessage) {
            if (plaintextPassword.equals(arg)) {
                event.setCancelled(true);
                message = message.replace(arg, "*".repeat(arg.length()));
                sendWarning(player);
                player.sendMessage(message);
                break;
            }
        }

    }
}
