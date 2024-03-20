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

    /**
     * Public constructor for the configuration of this plugin.
     *
     * @param chosenLanguage The chosen language at runtime.
     * @param plugin The plugin instance.
     */
    public LightConfiguration(@NotNull ConfigurationFiles chosenLanguage, @NotNull LightLoginPlugin plugin) {
        this.chosenLanguage = chosenLanguage;
        this.plugin = plugin;
    }

    /**
     * Loads the default configuration for the associated plugin and retrieves the configuration instance.
     */
    public void loadConfig() {
        configuration = this.plugin.getLanguagesConfigMap().get(chosenLanguage);
    }

    /**
     * Retrieves basic information about the author of this plugin,
     * its version and its creation date.
     *
     * @return Information about this plugin.
     */
    public List<String> getPluginInfo() {
        return this.configuration.getStringList("plugin.info");
    }

    /**
     * Gets a generic error message for incorrect command usage.
     *
     * @return The incorrect command usage error message.
     */
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

    /**
     * Retrieves a concatenated string of messages indicating that the login process took too much time.
     *
     * @return A single string with all messages joined by a newline character.
     */
    public String getLoginTookTooMuchTime() {
        return String.join("\n", this.configuration.getStringList("messages.login-took-too-much-time"));
    }

    /**
     * Determines if auto-login is enabled for players.
     *
     * @return True if auto-login is enabled, false otherwise.
     */
    public boolean isAutoLogin() {
        return this.configuration.getBoolean("login.auto-login");
    }

    /**
     * Gets the session expiration time in seconds.
     *
     * @return The session expiration time as an integer.
     */
    public int getSessionExpire() {
        return this.configuration.getInt("login.session-expire");
    }

    /**
     * Fetches the prefix for login-related messages.
     *
     * @return The message prefix as a string.
     */
    public String getMessagePrefix() {
        return this.configuration.getString("messages.prefix");
    }

    /**
     * Retrieves a list of messages to be displayed when a command is executed by a non-player entity.
     *
     * @return A list of strings containing the player-only command messages.
     */
    public List<String> getPlayerOnlyCommandMessage() {
        return this.configuration.getStringList("messages.player-only-command");
    }

    /**
     * Retrieves a list of messages indicating that the player lacks the required permission for an action.
     *
     * @return A list of strings containing the missing permission messages.
     */
    public List<String> getMissingPermissionMessage() {
        return this.configuration.getStringList("messages.missing-permission");
    }

    /**
     * Retrieves a list of messages for incorrect usage of the unregister command.
     *
     * @return A list of strings containing the incorrect usage messages for unregister command.
     */
    public List<String> getUnregisterIncorrectUsage() {
        return this.configuration.getStringList("messages.unregister-incorrect-usage");
    }

    /**
     * Retrieves a list of messages related to the registration process.
     *
     * @return A list of strings containing the register messages.
     */
    public List<String> getRegisterMessage() {
        return this.configuration.getStringList("messages.register-message");
    }

    /**
     * Retrieves a list of messages for incorrect usage of the register command.
     *
     * @return A list of strings containing the incorrect usage messages for register command.
     */
    public List<String> getRegisterIncorrectUsageMessage() {
        return this.configuration.getStringList("messages.register-incorrect-usage");
    }

    /**
     * Retrieves a list of messages for when the passwords entered during registration do not match.
     *
     * @return A list of strings containing messages for unequal passwords during registration.
     */
    public List<String> getRegisterUnequalPasswordsMessage() {
        return this.configuration.getStringList("messages.register-unequal-passwords");
    }

    /**
     * Retrieves a list of messages indicating successful registration.
     *
     * @return A list of strings containing the success messages for registration.
     */
    public List<String> getRegisterSuccessMessage() {
        return this.configuration.getStringList("messages.register-success");
    }

    /**
     * Retrieves a list of messages indicating that the player is already registered.
     *
     * @return A list of strings containing the messages for already registered players.
     */
    public List<String> getAlreadyRegisteredMessage() {
        return this.configuration.getStringList("messages.already-registered");
    }

    /**
     * Retrieves a list of messages indicating that the player is not registered.
     *
     * @return A list of strings containing the messages for unregistered players.
     */
    public List<String> getUnregisteredMessage() {
        return this.configuration.getStringList("messages.unregistered");
    }

    /**
     * Retrieves a list of messages indicating successful unregistration.
     *
     * @return A list of strings containing the success messages for unregistration.
     */
    public List<String> getUnregisteredSuccess() {
        return this.configuration.getStringList("messages.unregistered-success");
    }

    /**
     * Retrieves a list of messages warning about unsafe passwords.
     *
     * @return A list of strings containing messages about unsafe passwords.
     */
    public List<String> getUnsafePassword() {
        return this.configuration.getStringList("messages.unsafe-password");
    }

    /**
     * Retrieves a list of messages indicating an error during the registration process.
     *
     * @return A list of strings containing error messages related to registration.
     */
    public List<String> getRegisterError() {
        return this.configuration.getStringList("messages.register-error");
    }

    /**
     * Retrieves a list of messages for incorrect usage of the login command.
     *
     * @return A list of strings containing the incorrect usage messages for login command.
     */
    public List<String> getLoginIncorrectUsage() {
        return this.configuration.getStringList("messages.login-incorrect-usage");
    }

    /**
     * Retrieves a list of messages indicating that an attempt was made to unregister a non-existent account.
     *
     * @return A list of strings containing messages for attempts to unregister non-existent accounts.
     */
    public List<String> getUnregisterNotFound() {
        return this.configuration.getStringList("messages.unregister-not-found");
    }

    /**
     * Retrieves login already authenticated messages.
     *
     * @return List of login already authenticated messages.
     */
    public List<String> getLoginAlreadyAuthenticated() {
        return this.configuration.getStringList("messages.login-already-authenticated");
    }

    /**
     * Retrieves login error messages.
     *
     * @return List of login error messages.
     */
    public List<String> getLoginError() {
        return this.configuration.getStringList("messages.login-error");
    }

    /**
     * Retrieves login unregistered messages.
     *
     * @return List of login unregistered messages.
     */
    public List<String> getLoginUnregistered() {
        return this.configuration.getStringList("messages.login-unregistered");
    }

    /**
     * Retrieves login wrong password messages.
     *
     * @return List of login wrong password messages.
     */
    public List<String> getLoginWrongPassword() {
        return this.configuration.getStringList("messages.login-wrong-password");
    }

    /**
     * Retrieves login success messages.
     *
     * @return List of login success messages.
     */
    public List<String> getLoginSuccess() {
        return this.configuration.getStringList("messages.login-success");
    }

    /**
     * Retrieves login auto messages.
     *
     * @return List of login auto messages.
     */
    public List<String> getLoginAuto() {
        return this.configuration.getStringList("messages.login-auto");
    }

    /**
     * Retrieves login messages.
     *
     * @return List of login messages.
     */
    public List<String> getLoginMessages() {
        return this.configuration.getStringList("messages.login-messages");
    }

    /**
     * Checks if safe password force is enabled.
     *
     * @return True if safe password force is enabled, false otherwise.
     */
    public boolean isSafePasswordForceEnabled() {
        return this.configuration.getBoolean("safe-passwords.force-safe.enabled", true);
    }

    /**
     * Retrieves safe password minimum length.
     *
     * @return Safe password minimum length.
     */
    public int getSafePasswordMinLength() {
        return this.configuration.getInt("safe-passwords.force-safe.min-length", 8);
    }

    /**
     * Retrieves safe password minimum uppercase characters.
     *
     * @return Safe password minimum uppercase characters.
     */
    public int getSafePasswordMinUppercase() {
        return this.configuration.getInt("safe-passwords.force-safe.min-uppercase", 1);
    }

    /**
     * Retrieves safe password minimum numbers.
     *
     * @return Safe password minimum numbers.
     */
    public int getSafePasswordMinNumbers() {
        return this.configuration.getInt("safe-passwords.force-safe.min-numbers", 2);
    }

    /**
     * Retrieves safe password minimum special characters.
     *
     * @return Safe password minimum special characters.
     */
    public int getSafePasswordMinSpecial() {
        return this.configuration.getInt("safe-passwords.force-safe.min-special", 1);
    }

    /**
     * Retrieves safe password allowed special characters.
     *
     * @return List of safe password allowed special characters.
     */
    public List<Character> getSafePasswordAllowedSpecial() {
        return this.configuration.getCharacterList("safe-passwords.force-safe.allowed-special");
    }

    /**
     * Retrieves message for players with the same IP.
     *
     * @return Message for players with the same IP.
     */
    public String getMessagePlayersSameIp() {
        return this.configuration.getString("messages.players-same-ip");
    }

    /**
     * Retrieves safe password maximum length.
     *
     * @return Safe password maximum length.
     */
    public int getSafePasswordMaxLength() {
        return this.configuration.getInt("safe-passwords.force-safe.max-length", 32);
    }

    /**
     * Retrieves command too fast messages.
     *
     * @return List of command too fast messages.
     */
    public List<String> getCommandTooFast() {
        return this.configuration.getStringList("messages.command-too-fast");
    }

    /**
     * Retrieves changepassword unregistered messages.
     *
     * @return List of changepassword unregistered messages.
     */
    public List<String> getChangepasswordUnregistered() {
        return this.configuration.getStringList("messages.changepassword-unregistered");
    }

    /**
     * Retrieves changepassword wrong old password messages.
     *
     * @return List of changepassword wrong old password messages.
     */
    public List<String> getChangepasswordWrongOldPassword() {
        return this.configuration.getStringList("messages.changepassword-wrong-oldpassword");
    }

    /**
     * Retrieves changepassword updated messages.
     *
     * @return List of changepassword updated messages.
     */
    public List<String> getChangePasswordUpdated() {
        return this.configuration.getStringList("messages.changepassword-changed");
    }

    /**
     * Retrieves player not online messages.
     *
     * @return List of player not online messages.
     */
    public List<String> getPlayerNotOnline() {
        return this.configuration.getStringList("messages.player-not-online");
    }

    /**
     * Retrieves the number of players with the same IP allowed.
     *
     * @return The number of players with the same IP allowed.
     */
    public int getPlayersSameIp() {
        return this.configuration.getInt("safety.players-same-ip", 2);
    }

    /**
     * Checks if void world is enabled.
     *
     * @return True if void world is enabled, false otherwise.
     */
    public boolean isVoidWorldEnabled() {
        return this.configuration.getBoolean("void-world.enabled", true);
    }

    /**
     * Retrieves void world mode.
     *
     * @return Void world mode.
     */
    public String getVoidWorldMode() {
        return this.configuration.getString("void-world.mode", "NORMAL");
    }

    /**
     * Retrieves password in chat messages.
     *
     * @return List of password in chat messages.
     */
    public List<String> getPasswordInChat() {
        return this.configuration.getStringList("messages.password-in-chat");
    }

    /**
     * Retrieves email update help messages.
     *
     * @return List of email update help messages.
     */
    public List<String> getEmailUpdateHelp() {
        return this.configuration.getStringList("messages.email-update-help");
    }

    /**
     * Retrieves email invalid format messages.
     *
     * @return List of email invalid format messages.
     */
    public List<String> getEmailInvalidFormat() {
        return this.configuration.getStringList("messages.email-invalid-format");
    }

    /**
     * Retrieves email updated messages.
     *
     * @return List of email updated messages.
     */
    public List<String> getEmailUpdated() {
        return this.configuration.getStringList("messages.email-updated");
    }

    /**
     * Retrieves email something wrong messages.
     *
     * @return List of email something wrong messages.
     */
    public List<String> getEmailSomethingWrong() {
        return this.configuration.getStringList("messages.email-something-wrong");
    }

    /**
     * Retrieves email SMTP server.
     *
     * @return Email SMTP server.
     */
    public String getEmailSMTP() {
        return this.configuration.getString("email.email-smtp");
    }

    /**
     * Retrieves email port.
     *
     * @return Email port.
     */
    public int getEmailPort() {
        return this.configuration.getInt("email.email-port");
    }

    /**
     * Checks if TLS is enabled for email.
     *
     * @return True if TLS is enabled, false otherwise.
     */
    public boolean getEmailUseTLS() {
        return this.configuration.getBoolean("email.use-tls", true);
    }

    /**
     * Retrieves email account.
     *
     * @return Email account.
     */
    public String getEmailAccount() {
        return this.configuration.getString("email.email-account");
    }

    /**
     * Retrieves email password.
     *
     * @return Email password.
     */
    public String getEmailPassword() {
        return this.configuration.getString("email.email-password");
    }

    /**
     * Retrieves email sender name.
     *
     * @return Email sender name.
     */
    public String getEmailSenderName() {
        return this.configuration.getString("email.email-sender-name");
    }

    /**
     * Retrieves recovery password minimum length.
     *
     * @return Recovery password minimum length.
     */
    public int getRecoveryPasswordMinLength() {
        return this.configuration.getInt("email.recovery-password-min-length");
    }

    /**
     * Retrieves email subject.
     *
     * @return Email subject.
     */
    public String getEmailSubject() {
        return this.configuration.getString("email.email-subject");
    }

    /**
     * Retrieves email text content.
     *
     * @return List of email text content.
     */
    public List<String> getEmailTextContent() {
        return this.configuration.getStringList("email.email-text-content");
    }

    /**
     * Retrieves password reset help messages.
     *
     * @return List of password reset help messages.
     */
    public List<String> getPasswordResetHelp() {
        return this.configuration.getStringList("messages.password-reset-help");
    }

    /**
     * Retrieves password reset help for admins.
     *
     * @return List of password reset help for admins.
     */
    public List<String> getPasswordResetHelpAdmins() {
        return this.configuration.getStringList("messages.password-reset-help-admins");
    }

    /**
     * Retrieves password reset missing email messages.
     *
     * @return List of password reset missing email messages.
     */
    public List<String> getPasswordResetMissingEmail() {
        return this.configuration.getStringList("messages.password-reset-missing-email");
    }

    /**
     * Retrieves password reset not enabled messages.
     *
     * @return List of password reset not enabled messages.
     */
    public List<String> getPasswordResetNotEnabled() {
        return this.configuration.getStringList("messages.password-reset-not-enabled");
    }

    /**
     * Retrieves password reset failed messages.
     *
     * @return List of password reset failed messages.
     */
    public List<String> getPasswordResetFailed() {
        return this.configuration.getStringList("messages.password-reset-failed");
    }

    /**
     * Retrieves password reset success messages.
     *
     * @return List of password reset success messages.
     */
    public List<String> getPasswordResetSuccess() {
        return this.configuration.getStringList("messages.password-reset-success");
    }

    /**
     * Retrieves password reset too early messages.
     *
     * @return List of password reset too early messages.
     */
    public List<String> getPasswordResetTooEarly() {
        return this.configuration.getStringList("messages.password-reset-too-early");
    }

    /**
     * Retrieves recovery minimum delay.
     *
     * @return Recovery minimum delay.
     */
    public int getRecoveryMinDelay() {
        return this.configuration.getInt("email.recovery-min-delay", 90);
    }

    /**
     * Checks if email is enabled.
     *
     * @return True if email is enabled, false otherwise.
     */
    public boolean isEmailEnabled() {
        return this.configuration.getBoolean("email.enabled", false);
    }

    /**
     * Checks if login blindness is enabled.
     *
     * @return True if login blindness is enabled, false otherwise.
     */
    public boolean isLoginBlindness() {
        return this.configuration.getBoolean("login-blindness.enabled", false);
    }

}
