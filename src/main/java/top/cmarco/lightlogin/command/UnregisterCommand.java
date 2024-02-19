package top.cmarco.lightlogin.command;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import top.cmarco.lightlogin.LightLoginPlugin;
import top.cmarco.lightlogin.api.UnregisterEvent;
import top.cmarco.lightlogin.data.AuthenticationManager;
import top.cmarco.lightlogin.database.PluginDatabase;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public final class UnregisterCommand extends LightLoginCommand {

    public UnregisterCommand(@NotNull LightLoginPlugin plugin) {
        super(plugin, "lightlogin.admin.unregister", "unregister", true);
    }

    @Override
    protected void commandLogic(@NotNull CommandSender sender, @NotNull String[] args) {

        if (args.length != 1) {
            sendColorPrefixMessages(sender, super.configuration.getUnregisterIncorrectUsage(), super.plugin);
            return;
        }

        UUID playerUUID = null;
        if (!super.plugin.getServer().getOnlineMode()) {
            playerUUID = UUID.nameUUIDFromBytes(("OfflinePlayer:" + args[0]).getBytes(StandardCharsets.UTF_8));
        } else {
            OfflinePlayer offlinePlayer = super.plugin.getServer().getOfflinePlayer(args[0]);
            if (offlinePlayer.hasPlayedBefore()) {
                playerUUID = offlinePlayer.getUniqueId();
            }
        }

        if (playerUUID == null) {
            sendColorPrefixMessages(sender, super.configuration.getUnregisterNotFound(), super.plugin);
            return;
        }

        AuthenticationManager authManager = super.plugin.getAuthenticationManager();
        PluginDatabase database = super.plugin.getDatabase();
        authManager.unauthenticate(playerUUID);

        UnregisterEvent unregisterEvent = new UnregisterEvent(playerUUID);
        super.plugin.getServer().getPluginManager().callEvent(unregisterEvent);

        final UUID finalPlayerUUID = playerUUID;

        database.deleteRow(playerUUID.toString())
                .whenComplete((bool, throwable) -> {

                    if (throwable != null) {
                        super.plugin.getLogger().warning(throwable.getLocalizedMessage());
                        return;
                    }

                    if (bool) {

                        if (!(sender instanceof Player) || ((Player) sender).isOnline()) {
                            sendColorPrefixMessages(sender, super.configuration.getUnregisteredSuccess(), plugin);
                            authManager.addUnregistered(finalPlayerUUID);
                        }

                    } else {

                        if (!(sender instanceof Player) || ((Player) sender).isOnline()) {
                            sendColorPrefixMessages(sender, super.configuration.getUnregisterNotFound(), plugin);
                        }

                    }

                });

    }
}
