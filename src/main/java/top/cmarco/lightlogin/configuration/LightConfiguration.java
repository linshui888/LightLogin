/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package top.cmarco.lightlogin.configuration;

import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;
import top.cmarco.lightlogin.LightLoginPlugin;

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

}
