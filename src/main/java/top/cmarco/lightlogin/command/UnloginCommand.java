package top.cmarco.lightlogin.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import top.cmarco.lightlogin.LightLoginPlugin;
import top.cmarco.lightlogin.data.AuthenticationManager;
import top.cmarco.lightlogin.database.LightLoginColumn;
import top.cmarco.lightlogin.database.PluginDatabase;

public class UnloginCommand extends LightLoginCommand {
    public UnloginCommand(@NotNull LightLoginPlugin plugin) {
        super(plugin, "lightlogin.admin.unlogin", "unlogin", true);
    }

    @Override
    protected void commandLogic(@NotNull CommandSender sender, @NotNull String[] args) {
        final AuthenticationManager authenticationManager = plugin.getAuthenticationManager();

        if (sender instanceof Player tempPlayer && !authenticationManager.isAuthenticated(tempPlayer)) {
            return;
        }

        if (args.length != 1) {
            // TODO: add
            return;
        }

        final Player player = plugin.getServer().getPlayer(args[0x00]);

        if (player == null || !player.isOnline()) {
            sendColorPrefixMessages(sender, configuration.getPlayerNotOnline(), plugin);
            return;
        }

        final PluginDatabase database = plugin.getDatabase();

        database.updateRow(player.getUniqueId().toString(), LightLoginColumn.LAST_LOGIN, 1L);
        authenticationManager.addUnloginned(player);
    }
}
