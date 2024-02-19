package top.cmarco.lightlogin.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import top.cmarco.lightlogin.LightLoginPlugin;
import top.cmarco.lightlogin.api.PlayerAuthenticateEvent;
import top.cmarco.lightlogin.data.AuthenticationManager;
import top.cmarco.lightlogin.database.LightLoginColumn;
import top.cmarco.lightlogin.database.PluginDatabase;
import top.cmarco.lightlogin.encrypt.Argon2Utilities;

import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class LoginCommand extends LightLoginCommand {
    public LoginCommand(@NotNull LightLoginPlugin plugin) {
        super(plugin, null, "login", false);
    }

    private final HashMap<UUID, Long> lastLoginAttempt = new HashMap<>();
    private final ConcurrentHashMap<UUID, Integer> failedAttempts = new ConcurrentHashMap<>();

    private static final long HOUR_TICKS = 20L*60L*60L;

    public final void startClearTasks() {
        super.plugin.getServer().getScheduler().runTaskTimer(
                super.plugin,
                this.failedAttempts::clear,
                HOUR_TICKS*24,
                HOUR_TICKS*24);
    }

    @Override
    protected void commandLogic(@NotNull CommandSender sender, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            return;
        }

        final Player player = (Player) sender;

        if (args.length != 1) {
            sendColorPrefixMessages(player, super.configuration.getLoginIncorrectUsage(), super.plugin);
            return;
        }

        final UUID uuid = player.getUniqueId();
        final long timeNow = System.currentTimeMillis();
        if (!this.lastLoginAttempt.containsKey(uuid)) {
            this.lastLoginAttempt.put(uuid, timeNow);
            this.failedAttempts.put(uuid, 1);
        } else {
            final long lastAttempt = this.lastLoginAttempt.get(uuid);
            if ((timeNow - lastAttempt) / 1E3 <= super.configuration.getLoginDelay()) {
                sendColorPrefixMessages(player, super.configuration.getCommandTooFast(), super.plugin);
                return;
            }
            this.lastLoginAttempt.put(uuid, timeNow);
        }


        final AuthenticationManager authManager = super.plugin.getAuthenticationManager();

        if (authManager.isAuthenticated(player)) {
            sendColorPrefixMessages(player, super.configuration.getLoginAlreadyAuthenticated(), super.plugin);
            return;
        }

        final String password = args[0];

        final PluginDatabase database = super.plugin.getDatabase();
        database.searchRowFromPK(player.getUniqueId().toString())
                .whenComplete((row, throwable) -> {

                    if (throwable != null) {
                        if (player.isOnline()) {
                            sendColorPrefixMessages(player, super.configuration.getLoginError(), super.plugin);
                        }
                        super.plugin.getLogger().warning(throwable.getLocalizedMessage());
                        return;
                    }

                    if (row == null) {
                        if (player.isOnline()) {
                            sendColorPrefixMessages(player, super.configuration.getLoginUnregistered(), super.plugin);
                        }
                        return;
                    }

                    final String hashAttempt = Argon2Utilities.encryptArgon2(password, Base64.getDecoder().decode(row.getPasswordSalt()));
                    final String databaseHash = row.getPasswordHash();

                    if (databaseHash.equalsIgnoreCase(hashAttempt)) {
                        if (player.isOnline()) {
                            sendColorPrefixMessages(player, super.configuration.getLoginSuccess(), super.plugin);
                        }

                        authManager.authenticate(player);
                        database.updateRow(uuid.toString(), LightLoginColumn.LAST_LOGIN, System.currentTimeMillis());

                    } else if (player.isOnline()) {

                        sendColorPrefixMessages(player, super.configuration.getLoginWrongPassword(), super.plugin);

                        int currentAttempts = this.failedAttempts.get(uuid);

                        if (currentAttempts >= super.configuration.getMaxFailedAttempts() + 1) {
                            super.plugin.getServer().getScheduler().runTask(super.plugin, () -> {
                                super.configuration.getBruteforcePunishment().forEach(cmd -> {
                                    super.plugin.getServer().dispatchCommand(
                                            super.plugin.getServer().getConsoleSender(),
                                            cmd.replace("{PLAYER}", player.getName()));
                                });
                            });
                        } else {
                            this.failedAttempts.put(uuid, currentAttempts + 1);
                        }
                    }

                });

    }
}
