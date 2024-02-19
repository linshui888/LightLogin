package top.cmarco.lightlogin.command;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import top.cmarco.lightlogin.LightLoginPlugin;

import java.util.Locale;
import java.util.stream.Collectors;

public final class BaseCommand extends LightLoginCommand {
    public BaseCommand(@NotNull LightLoginPlugin plugin) {
        super(plugin, "lightlogin.admin.info", "lightlogin", true);
    }

    @Override
    protected void commandLogic(@NotNull CommandSender sender, @NotNull String[] args) {

        if (args.length == 0) {
            sendColorPrefixMessages(sender, configuration.getPluginInfo().stream().map(s ->
                    s.replaceAll("\\{VERSION}", plugin.getDescription().getVersion())).collect(Collectors.toList()), plugin);
            return;
        }

        if (args.length != 1) {
            sendColorPrefixMessages(sender, configuration.getIncorrectCommandUsage(), plugin);
            return;
        }

        if (args[0].toLowerCase(Locale.ROOT).equals("info")) {
            sendColorPrefixMessages(sender, configuration.getPluginInfo().stream().map(s ->
                    s.replaceAll("\\{VERSION}", plugin.getDescription().getVersion())).collect(Collectors.toList()), plugin);
        } else {
            sendColorPrefixMessages(sender, configuration.getIncorrectCommandUsage(), plugin);
        }

    }
}
