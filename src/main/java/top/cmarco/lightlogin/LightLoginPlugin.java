/*
* This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
* If a copy of the MPL was not distributed with this file,
* You can obtain one at https://mozilla.org/MPL/2.0/.
*/
package top.cmarco.lightlogin;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;
import top.cmarco.lightlogin.configuration.LightConfiguration;
import top.cmarco.lightlogin.database.DatabaseType;
import top.cmarco.lightlogin.database.MySqlDatabase;
import top.cmarco.lightlogin.database.PluginDatabase;
import top.cmarco.lightlogin.database.SQLiteDatabase;
import top.cmarco.lightlogin.listeners.AuthorisedEventsManager;

import java.util.Objects;

@Getter
public final class LightLoginPlugin extends JavaPlugin {

    private AuthorisedEventsManager authorisedEventsManager = null;
    private LightConfiguration lightConfiguration = null;
    private PluginDatabase database = null;
    @Setter private boolean disabled = false;

    /**
     * Method responsible for registering all of this software listeners.
     */
    private void registerAllListeners() {
        this.authorisedEventsManager = new AuthorisedEventsManager(this);
        this.authorisedEventsManager.registerBlockEvent();
    }

    private void setupConfig() {
        lightConfiguration = new LightConfiguration(this);
        lightConfiguration.loadConfig();
    }

    private void setupDatabase() {
        final DatabaseType databaseType = DatabaseType.fromName(
                Objects.requireNonNull(this.lightConfiguration.getDatabaseType()));

        if (databaseType == null) {
            this.disabled = true;
            getLogger().warning("WARNING! Invalid database type from config file. Cannot load plugin.");
            if (lightConfiguration.isCrashShutdown()) {
                getLogger().warning("Shutting down server . . .");
                getServer().shutdown();
            }
            return;
        }

        switch (databaseType) {
            case SQLITE -> this.database = new SQLiteDatabase(this);
            case MYSQL -> this.database = new MySqlDatabase(this);
            default -> this.database = null;
        }
        assert this.database != null;

        this.database.loadDriverClass();
        this.database.connect();
        this.database.createTables();
    }

    /**
     * Plugin startup logic.
     * Called on STARTUP phase.
     */
    @Override
    public void onEnable() {
        this.setupConfig();
        this.setupDatabase();
        this.registerAllListeners();
    }

    /**
     * Plugin disable logic.
     */
    @Override
    public void onDisable() {
    }
}