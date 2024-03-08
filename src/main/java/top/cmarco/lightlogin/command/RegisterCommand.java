package top.cmarco.lightlogin.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import top.cmarco.lightlogin.LightLoginPlugin;
import top.cmarco.lightlogin.api.AuthenticationCause;
import top.cmarco.lightlogin.api.PlayerAuthenticateEvent;
import top.cmarco.lightlogin.api.PlayerRegisterEvent;
import top.cmarco.lightlogin.configuration.LightConfiguration;
import top.cmarco.lightlogin.data.LightLoginDbRow;
import top.cmarco.lightlogin.database.PluginDatabase;
import top.cmarco.lightlogin.encrypt.Argon2Utilities;
import top.cmarco.lightlogin.network.NetworkUtilities;

import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public final class RegisterCommand extends LightLoginCommand {

    public RegisterCommand(@NotNull LightLoginPlugin plugin) {
        super(plugin, null, "register", false);
    }

    public static boolean isCharAllowed(final char character, final @NotNull List<Character> specialChars) {
        return (character >= 0x30 && character <= 0x39) ||
                (character >= 0x41 && character <= 0x5A) ||
                (character >= 0x61 && character <= 0x7A) ||
                specialChars.contains(character);
    }

    public static boolean isPasswordSafe(@NotNull final String password, LightConfiguration configuration) {
        final int minLength = configuration.getSafePasswordMinLength();
        final int minUppercase = configuration.getSafePasswordMinUppercase();
        final int minNumbers = configuration.getSafePasswordMinNumbers();
        final int minSpecial = configuration.getSafePasswordMinSpecial();
        final List<Character> special = configuration.getSafePasswordAllowedSpecial();

        if (password.length() < minLength) {

            return false;
        }

        final char[] passwordChars = password.toCharArray();
        int uppercaseCount = 0, numberCount = 0, specialCount = 0;
        for (final char tempChar : passwordChars) {
            if (!isCharAllowed(tempChar, special)) {
                return false;
            }
            if (Character.isUpperCase(tempChar)) {
                uppercaseCount++;
            }
            if (0x30 <= tempChar && 0x39 >= tempChar) {
                numberCount++;
            }
            if (special.contains(tempChar)) {
                specialCount++;
            }
        }

        if (minUppercase > uppercaseCount) {
            return false;
        }

        if (minNumbers > numberCount) {
            return false;
        }

        if (minSpecial > specialCount) {
            return false;
        }

        return true;
    }

    private static String specialDisplayCache = null;

    public static void unsafePasswordMsg(@NotNull Player player, LightConfiguration configuration, LightLoginPlugin plugin) {
        final int minLength = configuration.getSafePasswordMinLength();
        final int minUppercase = configuration.getSafePasswordMinUppercase();
        final int minNumbers = configuration.getSafePasswordMinNumbers();
        final int minSpecial = configuration.getSafePasswordMinSpecial();
        final List<Character> special = configuration.getSafePasswordAllowedSpecial();

        if (specialDisplayCache == null) {
            final StringBuilder specialDisplay = new StringBuilder();
            specialDisplay.append("&7[&e");
            final Iterator<Character> characterIterator = special.iterator();
            while (characterIterator.hasNext()) {
                specialDisplay.append(characterIterator.next()).append("&f,&e");
            }
            specialDisplay.append("&7]");
            specialDisplayCache = specialDisplay.toString();
        }

        sendColorPrefixMessages(player,
                configuration.getUnsafePassword().stream()
                        .map(str -> str.replace("{MIN_LENGTH}", String.valueOf(minLength))
                                .replace("{MIN_UPCASE}", String.valueOf(minUppercase))
                                .replace("{MIN_NUMBERS}", String.valueOf(minNumbers))
                                .replace("{MIN_SPECIAL}", String.valueOf(minSpecial))
                                .replace("{SPECIAL}", specialDisplayCache))
                        .collect(Collectors.toList()),
                plugin);
    }

    @Override
    protected void commandLogic(@NotNull CommandSender sender, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            return;
        }

        final Player player = (Player) sender;

        if (args.length != 2) {
            sendColorPrefixMessages(player, super.configuration.getRegisterIncorrectUsageMessage(), super.plugin);
            return;
        }

        if (!args[0].equals(args[1])) {
            sendColorPrefixMessages(player, super.configuration.getRegisterUnequalPasswordsMessage(), super.plugin);
            return;
        }

        if (!isPasswordSafe(args[0], super.configuration)) {
            unsafePasswordMsg(player, super.configuration, super.plugin);
            return;
        }

        PluginDatabase database = super.plugin.getDatabase();

        database.searchRowFromPK(player.getUniqueId().toString())
                .whenComplete((row, throwable) -> {

                    if (throwable != null) {
                        super.plugin.getLogger().warning(throwable.getLocalizedMessage());
                        if (player.isOnline()) {
                            sendColorPrefixMessages(player, super.configuration.getRegisterError(), super.plugin);
                        }
                        return;
                    }

                    super.plugin.getServer().getScheduler().runTask(super.plugin, () -> {

                        if (row != null) {
                            sendColorPrefixMessages(player, super.configuration.getAlreadyRegisteredMessage(), super.plugin);
                        } else {
                            final String uuid = player.getUniqueId().toString();
                            final byte[] salt = Argon2Utilities.generateSaltByte(0x10);
                            final String password = Argon2Utilities.encryptArgon2(args[0], salt);
                            final String email = null;
                            final long lastLogin = System.currentTimeMillis();
                            final long lastIpv4 = NetworkUtilities.convertInetSocketAddressToLong(player.getAddress());

                            database.addRow(new LightLoginDbRow(uuid,
                                    password,
                                    Base64.getEncoder().encodeToString(salt),
                                    email,
                                    lastLogin,
                                    lastIpv4)
                            ).whenComplete((addedRow, throwable2) -> {

                                if (addedRow == null || throwable2 != null) {
                                    if (player.isOnline()) {
                                        sendColorPrefixMessages(player, super.configuration.getRegisterError(), super.plugin);
                                    }
                                    if (addedRow == null) {
                                        this.plugin.getLogger().warning("WARNING! Error registering player: " + player.getName());
                                    }
                                    if (throwable2 == null) {
                                        this.plugin.getLogger().warning("WARNING! Throwable received registering player: " + throwable2.getLocalizedMessage());
                                    }
                                    return;
                                }

                                plugin.getServer().getScheduler().runTask(plugin, () -> {
                                    PlayerAuthenticateEvent playerAuthenticateEvent = new PlayerAuthenticateEvent(player, AuthenticationCause.COMMAND);
                                    this.plugin.getServer().getPluginManager().callEvent(playerAuthenticateEvent);
                                });

                                super.plugin.getAuthenticationManager().authenticate(player);

                                if (!player.isOnline()) return;

                                super.plugin.getPlaintextPasswordManager().setPassword(player, args[1]);

                                sendColorPrefixMessages(player, super.configuration.getRegisterSuccessMessage(), super.plugin);

                                final PlayerRegisterEvent playerRegisterEvent = new PlayerRegisterEvent(player);
                                super.plugin.getServer().getScheduler().runTask(super.plugin, () -> {
                                    super.plugin.getServer().getPluginManager().callEvent(playerRegisterEvent);
                                });
                            });
                        }

                    });
                });

    }
}
