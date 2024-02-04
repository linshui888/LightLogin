/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package top.cmarco.lightlogin.configuration;

import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.cmarco.lightlogin.LightLoginPlugin;

import java.util.List;

/**
 * This class represents the configuration for the LightLoginPlugin, providing convenient methods
 * to access various configuration parameters related to the database.
 */
@RequiredArgsConstructor
public final class LightConfiguration {

    // The LightLoginPlugin instance associated with this configuration.
    private final LightLoginPlugin plugin;

    // The FileConfiguration instance used to store and retrieve configuration settings.
    private FileConfiguration configuration = null;

    /**
     * Loads the default configuration for the associated plugin and retrieves the configuration instance.
     */
    public void loadConfig() {
        this.plugin.saveDefaultConfig();
        configuration = this.plugin.getConfig();
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

    public int getLoginDelay() {
        return this.configuration.getInt("login.command-delay");
    }

    public int getMaxFailedAttempts() {
        return this.configuration.getInt("login.max-failed-attempts");
    }

    public List<String> getBruteforcePunishment() {
        return this.configuration.getStringList("login.bruteforce-punishment");
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

    public int getSafePasswordMaxLength() {
        return this.configuration.getInt("safe-passwords.force-safe.max-length", 32);
    }

    public List<String> getCommandTooFast() {
        return this.configuration.getStringList("messages.command-too-fast");
    }


}
