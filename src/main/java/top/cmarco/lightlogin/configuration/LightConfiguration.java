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

/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package top.cmarco.lightlogin.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.cmarco.lightlogin.LightLoginPlugin;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class represents the configuration for the LightLoginPlugin, providing convenient methods
 * to access various configuration parameters related to the database.
 */
public final class LightConfiguration {

    private final ConfigurationFiles chosenLanguage;

    // The LightLoginPlugin instance associated with this configuration.
    private final LightLoginPlugin plugin;

    // The FileConfiguration instance used to store and retrieve configuration settings.
    private FileConfiguration configuration = null;

    public LightConfiguration(ConfigurationFiles chosenLanguage, LightLoginPlugin plugin) {
        this.chosenLanguage = chosenLanguage;
        this.plugin = plugin;
    }

    /**
     * Loads the default configuration for the associated plugin and retrieves the configuration instance.
     */
    public void loadConfig() {
        configuration = this.plugin.getLanguagesConfigMap().get(chosenLanguage);
    }

    public List<String> getPluginInfo() {
        return this.configuration.getStringList("plugin.info");
    }

    public List<String> getIncorrectCommandUsage() {
        return this.configuration.getStringList("messages.incorrect-command-usage");
    }

    /**
     * Retrieves the database type from the configuration.
     *
     * @return The database type, or null if not found.
     */
    @Nullable
    public String getDatabaseType() {
        return this.configuration.getString("database.type");
    }

    /**
     * Retrieves the database username from the configuration.
     *
     * @return The database username, or null if not found.
     */
    @Nullable
    public String getUsername() {
        return this.configuration.getString("database.username");
    }

    /**
     * Retrieves the database password from the configuration.
     *
     * @return The database password, or null if not found.
     */
    @Nullable
    public String getPassword() {
        return this.configuration.getString("database.password");
    }

    /**
     * Retrieves the database address from the configuration.
     *
     * @return The database address, or null if not found.
     */
    @Nullable
    public String getAddress() {
        return this.configuration.getString("database.address");
    }

    /**
     * Retrieves the database port from the configuration.
     *
     * @return The database port.
     */
    public int getPort() {
        return this.configuration.getInt("database.port", 3306);
    }

    /**
     * Retrieves the database name from the configuration.
     *
     * @return The database name, or null if not found.
     */
    @Nullable
    public String getDatabaseName() {
        return this.configuration.getString("database.db-name");
    }

    /**
     * Retrieves whether the server should turn off after a plugin or database crash.
     *
     * @return Shutdown feature.
     */
    public boolean isCrashShutdown() {
        return this.configuration.getBoolean("crash-shutdown", false);
    }

    /**
     * Retrieves all the commands allowed by un-authenticated users.
     *
     * @return A list of commands: can be empty.
     */
    @NotNull
    public List<String> getAllowedCommands() {
        return this.configuration.getStringList("login.allowed-commands");
    }

    public int getKickAfterSeconds() {
        return this.configuration.getInt("login.kick-after-seconds", 120);
    }

    public int getLoginDelay() {
        return this.configuration.getInt("login.command-delay");
    }

    public int getMaxFailedAttempts() {
        return this.configuration.getInt("login.max-failed-attempts");
    }

    public List<String> getBruteforcePunishment() {
        return this.configuration.getStringList("login.bruteforce-punishment");
    }

    public String getLoginTookTooMuchTime() {
        return String.join("\n", this.configuration.getStringList("messages.login-took-too-much-time"));
    }

    public boolean isAutoLogin() {
        return this.configuration.getBoolean("login.auto-login");
    }

    public int getSessionExpire() {
        return this.configuration.getInt("login.session-expire");
    }

    public String getMessagePrefix() {
        return this.configuration.getString("messages.prefix");
    }

    public List<String> getPlayerOnlyCommandMessage() {
        return this.configuration.getStringList("messages.player-only-command");
    }

    public List<String> getMissingPermissionMessage() {
        return this.configuration.getStringList("messages.missing-permission");
    }

    public List<String> getUnregisterIncorrectUsage() {
        return this.configuration.getStringList("messages.unregister-incorrect-usage");
    }

    public List<String> getRegisterMessage() {
        return this.configuration.getStringList("messages.register-message");
    }

    public List<String> getRegisterIncorrectUsageMessage() {
        return this.configuration.getStringList("messages.register-incorrect-usage");
    }

    public List<String> getRegisterUnequalPasswordsMessage() {
        return this.configuration.getStringList("messages.register-unequal-passwords");
    }

    public List<String> getRegisterSuccessMessage() {
        return this.configuration.getStringList("messages.register-success");
    }

    public List<String> getAlreadyRegisteredMessage() {
        return this.configuration.getStringList("messages.already-registered");
    }

    public List<String> getUnregisteredMessage() {
        return this.configuration.getStringList("messages.unregistered");
    }

    public List<String> getUnregisteredSuccess() {
        return this.configuration.getStringList("messages.unregistered-success");
    }


    public List<String> getUnsafePassword() {
        return this.configuration.getStringList("messages.unsafe-password");
    }

    public List<String> getRegisterError() {
        return this.configuration.getStringList("messages.register-error");
    }

    public List<String> getLoginIncorrectUsage() {
        return this.configuration.getStringList("messages.login-incorrect-usage");
    }

    public List<String> getUnregisterNotFound() {
        return this.configuration.getStringList("messages.unregister-not-found");
    }

    public List<String> getLoginAlreadyAuthenticated() {
        return this.configuration.getStringList("messages.login-already-authenticated");
    }

    public List<String> getLoginError() {
        return this.configuration.getStringList("messages.login-error");
    }

    public List<String> getLoginUnregistered() {
        return this.configuration.getStringList("messages.login-unregistered");
    }

    public List<String> getLoginWrongPassword() {
        return this.configuration.getStringList("messages.login-wrong-password");
    }

    public List<String> getLoginSuccess() {
        return this.configuration.getStringList("messages.login-success");
    }

    public List<String> getLoginAuto() {
        return this.configuration.getStringList("messages.login-auto");
    }

    public List<String> getLoginMessages() {
        return this.configuration.getStringList("messages.login-messages");
    }

    public boolean isSafePasswordForceEnabled() {
        return this.configuration.getBoolean("safe-passwords.force-safe.enabled", true);
    }

    public int getSafePasswordMinLength() {
        return this.configuration.getInt("safe-passwords.force-safe.min-length", 8);
    }

    public int getSafePasswordMinUppercase() {
        return this.configuration.getInt("safe-passwords.force-safe.min-uppercase", 1);
    }

    public int getSafePasswordMinNumbers() {
        return this.configuration.getInt("safe-passwords.force-safe.min-numbers", 2);
    }

    public int getSafePasswordMinSpecial() {
        return this.configuration.getInt("safe-passwords.force-safe.min-special", 1);
    }

    public List<Character> getSafePasswordAllowedSpecial() {
        return this.configuration.getCharacterList("safe-passwords.force-safe.allowed-special");
    }

    public String getMessagePlayersSameIp() {
        return this.configuration.getString("messages.players-same-ip");
    }

    public int getSafePasswordMaxLength() {
        return this.configuration.getInt("safe-passwords.force-safe.max-length", 32);
    }

    public List<String> getCommandTooFast() {
        return this.configuration.getStringList("messages.command-too-fast");
    }

    public List<String> getChangepasswordUnregistered() {
        return this.configuration.getStringList("messages.changepassword-unregistered");
    }

    public List<String> getChangepasswordWrongOldPassword() {
        return this.configuration.getStringList("messages.changepassword-wrong-oldpassword");
    }

    public List<String> getChangePasswordUpdated() {
        return this.configuration.getStringList("messages.changepassword-changed");
    }

    public List<String> getPlayerNotOnline() {
        return this.configuration.getStringList("messages.player-not-online");
    }

    public int getPlayersSameIp() {
        return this.configuration.getInt("safety.players-same-ip", 2);
    }

    public boolean isVoidWorldEnabled() {
        return this.configuration.getBoolean("void-world.enabled", true);
    }

    public String getVoidWorldMode() {
        return this.configuration.getString("void-world.mode", "NORMAL");
    }

    public List<String> getPasswordInChat() {
        return this.configuration.getStringList("messages.password-in-chat");
    }

    public List<String> getEmailUpdateHelp() {
        return this.configuration.getStringList("messages.email-update-help");
    }

    public List<String> getEmailInvalidFormat() {
        return this.configuration.getStringList("messages.email-invalid-format");
    }

    public List<String> getEmailUpdated() {
        return this.configuration.getStringList("messages.email-updated");
    }

    public List<String> getEmailSomethingWrong() {
        return this.configuration.getStringList("messages.email-something-wrong");
    }

    public String getEmailSMTP() {
        return this.configuration.getString("email.email-smtp");
    }

    public int getEmailPort() {
        return this.configuration.getInt("email.email-port");
    }

    public boolean getEmailUseTLS() {
        return this.configuration.getBoolean("email.use-tls", true);
    }

    public String getEmailAccount() {
        return this.configuration.getString("email.email-account");
    }

    public String getEmailPassword() {
        return this.configuration.getString("email.email-password");
    }

    public String getEmailSenderName() {
        return this.configuration.getString("email.email-sender-name");
    }

    public int getRecoveryPasswordMinLength() {
        return this.configuration.getInt("email.recovery-password-min-length");
    }

    public String getEmailSubject() {
        return this.configuration.getString("email.email-subject");
    }

    public List<String> getEmailTextContent() {
        return this.configuration.getStringList("email.email-text-content");
    }

    public List<String> getPasswordResetHelp() {
        return this.configuration.getStringList("messages.password-reset-help");
    }

    public List<String> getPasswordResetHelpAdmins() {
        return this.configuration.getStringList("messages.password-reset-help-admins");
    }

    public List<String> getPasswordResetMissingEmail() {
        return this.configuration.getStringList("messages.password-reset-missing-email");
    }

    public List<String> getPasswordResetNotEnabled() {
        return this.configuration.getStringList("messages.password-reset-not-enabled");
    }

    public List<String> getPasswordResetFailed() {
        return this.configuration.getStringList("messages.password-reset-failed");
    }

    public List<String> getPasswordResetSuccess() {
        return this.configuration.getStringList("messages.password-reset-success");
    }

    public List<String> getPasswordResetTooEarly() {
        return this.configuration.getStringList("messages.password-reset-too-early");
    }

    public int getRecoveryMinDelay() {
        return this.configuration.getInt("email.recovery-min-delay", 90);
    }

    public boolean isEmailEnabled() {
        return this.configuration.getBoolean("email.enabled", false);
    }

    public boolean isLoginBlindness() {
        return this.configuration.getBoolean("login-blindness.enabled", false);
    }
}
