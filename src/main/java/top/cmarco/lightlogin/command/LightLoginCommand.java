package top.cmarco.lightlogin.command;

import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.cmarco.lightlogin.LightLoginPlugin;
import top.cmarco.lightlogin.configuration.LightConfiguration;

import java.util.List;


public abstract class LightLoginCommand implements CommandExecutor {

    protected final LightLoginPlugin plugin;
    protected final LightConfiguration configuration;
    protected final String basePermission;
    protected final String commandName;

    protected LightLoginCommand(@NotNull LightLoginPlugin plugin,
                                @Nullable String basePermission,
                                @NotNull String commandName) {
        this.plugin = plugin;
        this.basePermission = basePermission;
        this.commandName = commandName;
        this.configuration = plugin.getLightConfiguration();
    }

    public static void sendColorMessage(@NotNull final Player player, @NotNull String string) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', string));
    }
    public static void sendColorPrefixMessages(@NotNull final Player player, @NotNull List<String> strings, @NotNull LightLoginPlugin plugin) {
        strings.forEach(str -> sendColorMessage(player,
                str
                        .replaceAll("\\{PREFIX}", plugin.getLightConfiguration().getMessagePrefix())
                        .replaceAll("\\{PLAYER}", plugin.getLightConfiguration().getMessagePrefix())));
    }

    protected abstract void commandLogic(@NotNull final Player player, @NotNull final String[] args);

    public final void register() {
        this.plugin.getServer().getPluginCommand(this.commandName).setExecutor(this);
    }

    @Override
    public final boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof final Player player)) {
            sender.sendMessage(LightLoginPlugin.PREFIX + ChatColor.RED + "This command can only be executed as a player!");
            return false;
        }

        if (this.basePermission != null && !player.hasPermission(this.basePermission)) {
            sender.sendMessage(LightLoginPlugin.PREFIX + ChatColor.RED + "You are missing permission &l" + this.basePermission);
            return false;
        }

        this.commandLogic(player, args);

        return false;
    }
}
