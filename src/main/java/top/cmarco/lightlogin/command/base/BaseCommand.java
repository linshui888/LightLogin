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

package top.cmarco.lightlogin.command.base;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import top.cmarco.lightlogin.LightLoginPlugin;
import top.cmarco.lightlogin.command.LightLoginCommand;

import java.util.Locale;
import java.util.stream.Collectors;

public final class BaseCommand extends LightLoginCommand {
    public BaseCommand(@NotNull LightLoginPlugin plugin) {
        super(plugin, "lightlogin.admin.info", "lightlogin", true);
    }

    public static void sendCommandHelps(@NotNull final CommandSender sender) {
        final String s1 = colorMessage("&6&l★&r&eLightLogin&6&l★&r&f Help Page:");
        final String s2 = colorMessage("&7Set your account's email.");
        final String s3 = colorMessage("  ● &7/&eemail &7<&6email&7>");
        final String s4 = colorMessage("&7Request to change your current password.");
        final String s5 = colorMessage("  ● &7/&echangepassword &7<&6oldPassword&7> &7<&6newPassword&7>");
        final String s6 = colorMessage("&7Reset your password via email.");
        final String s7 = colorMessage("  ● &7/&eresetpassword");
        final String s8 = colorMessage("&7Unauthenticate an online player.");
        final String s9 = colorMessage("  ● &7/&eunlogin &7<&6username&7>");
        final String s10 = colorMessage("&7Unregister a player");
        final String s11 = colorMessage("  ● &7/&eunregister &7<&6username&7>");
        final String s12 = colorMessage("&7Authenticate into the server.");
        final String s13 = colorMessage("  ● &7/&elogin &7<&6password&7>");
        final String s14 = colorMessage("&7Register into this server.");
        final String s15 = colorMessage("  ● &7/&eregister &7<&6password&7> &7<&6passwordConfirm&7>");

        final String[] englishMessages = {s1,s2,s3,s4,s5,s6,s7,s8,s9,s10,s11,s12,s13,s14,s15};
        sender.sendMessage(englishMessages);
    }

    @Override
    protected void commandLogic(@NotNull final CommandSender sender, @NotNull final String[] args) {

        if (args.length == 0) {
            sendCommandHelps(sender);
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
