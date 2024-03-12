/*
 * LightLogin - Optimised and Safe SpigotMC Software for Authentication
 *     Copyright © 2024  CMarco
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
