package top.cmarco.lightlogin.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.cmarco.lightlogin.LightLoginPlugin;
import top.cmarco.lightlogin.database.LightLoginColumn;
import top.cmarco.lightlogin.database.PluginDatabase;
import top.cmarco.lightlogin.encrypt.Argon2Utilities;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ChangePasswordCommand extends LightLoginCommand {
    public ChangePasswordCommand(@NotNull LightLoginPlugin plugin) {
        super(plugin, null, "changepassword", false);
    }

    @Override
    protected void commandLogic(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return;
        }

        final Player player = (Player) sender;

        if (args.length != 0x03) {
            sendColorMessage(player, "&cWrong syntax for changepassword command!", plugin);
            return;
        }

        final String oldPassword = args[0];
        final String newPassword = args[1];
        final String newPasswordConfirm = args[2];

        if (!newPassword.equals(newPasswordConfirm)) {
            sendColorPrefixMessages(player, configuration.getRegisterUnequalPasswordsMessage(), plugin);
            return;
        }

        if (!RegisterCommand.isPasswordSafe(newPassword, super.configuration)) {
            RegisterCommand.unsafePasswordMsg(player, super.configuration, super.plugin);
            return;
        }

        final PluginDatabase database = super.plugin.getDatabase();

        database.searchRowFromPK(player.getUniqueId().toString())
                .whenComplete((row, t) -> {


                    if (t != null) {
                        plugin.getLogger().warning(t.getLocalizedMessage());
                        if (player.isOnline()) {
                            sendColorPrefixMessages(player, configuration.getRegisterError(), plugin);
                        }
                        return;
                    }


                    if (row == null) {
                        if (player.isOnline()) {
                            sendColorPrefixMessages(player, configuration.getChangepasswordUnregistered(), plugin);
                        }
                        return;
                    }


                    final Base64.Encoder base64Encoder = Base64.getEncoder();

                    final String previousSaltBase64 = row.getPasswordSalt();
                    final String previousPasswordHashBase64 = row.getPasswordHash();
                    final byte[] rowSalt = Base64.getDecoder().decode(previousSaltBase64);

                    final String oldPasswordHashed = Argon2Utilities.encryptArgon2(oldPassword, rowSalt);

                    final boolean matchedPassword = oldPasswordHashed.equals(previousPasswordHashBase64);

                    if (!matchedPassword) {
                        if (player.isOnline()) {
                            sendColorPrefixMessages(player, configuration.getChangepasswordWrongOldPassword(), plugin);
                        }
                        return;
                    }


                    final String newPasswordHashOldSalt = Argon2Utilities.encryptArgon2(newPassword, rowSalt);

                    database.updateRow(player.getUniqueId().toString(), LightLoginColumn.PASSWORD, newPasswordHashOldSalt)
                            .whenComplete((v, th) -> {

                                if (th != null) {
                                    plugin.getLogger().warning(t.getLocalizedMessage());
                                    if (player.isOnline()) {
                                        sendColorPrefixMessages(player, configuration.getRegisterError(), plugin);
                                    }
                                    return;
                                }


                                if (player.isOnline()) {
                                    sendColorPrefixMessages(player, configuration.getChangePasswordUpdated(), plugin);
                                }

                            });

                });
    }
}
