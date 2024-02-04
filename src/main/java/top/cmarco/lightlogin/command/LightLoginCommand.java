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
    protected final boolean allowConsole;

    protected LightLoginCommand(@NotNull LightLoginPlugin plugin,
                                @Nullable String basePermission,
                                @NotNull String commandName,
                                boolean allowConsole) {
        this.plugin = plugin;
        this.basePermission = basePermission;
        this.commandName = commandName;
        this.allowConsole = allowConsole;
        this.configuration = plugin.getLightConfiguration();
    }

    public static void sendColorMessage(@NotNull final CommandSender sender, @NotNull String string) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', string));
    }
    public static void sendColorPrefixMessages(@NotNull final CommandSender sender, @NotNull List<String> strings, @NotNull LightLoginPlugin plugin) {
        strings.forEach(str -> {
            str = str.replaceAll("\\{PREFIX}", plugin.getLightConfiguration().getMessagePrefix());

            if (sender instanceof Player player) {
                str = str.replaceAll("\\{PLAYER}", player.getName());
            }

            sendColorMessage(sender, str);
        });
    }

    protected abstract void commandLogic(@NotNull final CommandSender sender, @NotNull final String[] args);

    public final void register() {
        this.plugin.getServer().getPluginCommand(this.commandName).setExecutor(this);
    }

    @Override
    public final boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!allowConsole && !(sender instanceof Player)) {
            sender.sendMessage(LightLoginPlugin.PREFIX + ChatColor.RED + "This command can only be executed as a player!");
            return false;
        }

        if (this.basePermission != null && !sender.hasPermission(this.basePermission)) {
            sender.sendMessage(LightLoginPlugin.PREFIX + ChatColor.RED + "You are missing permission &l" + this.basePermission);
            return false;
        }

        this.commandLogic(sender, args);

        return false;
    }
}
