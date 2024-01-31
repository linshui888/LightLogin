/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package top.cmarco.lightlogin.database;

import org.jetbrains.annotations.NotNull;
import top.cmarco.lightlogin.LightLoginPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

public final class SQLiteDatabase extends CredentialPluginDatabase {
    public SQLiteDatabase(final @NotNull LightLoginPlugin plugin) {
        super(plugin, DatabaseType.SQLITE);
    }

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS lightlogin (" +
            "uuid char(36) primary key not null," +
            "password char(32) not null," +
            "email varchar(64)," +
            "last_login integer" +
            ");";


    @Override
    protected @NotNull String createConnectionUrl() {
        File pluginDataFolder = super.plugin.getDataFolder();
        File dbFile = new File(pluginDataFolder, "lightlogin.sqlite");

        if (!dbFile.exists()) {
            try {
                dbFile.createNewFile();
            } catch (IOException exception) {
                super.plugin.getLogger().warning("WARNING! Error during sqlite file creation!");
                super.plugin.getLogger().warning(exception.getLocalizedMessage());
            }
        }

        return "jdbc:sqlite:" + dbFile;
    }

    /**
     * Creates the tables necessarily used by this software.
     * Do not rewrite the table if already exists.
     */
    @Override
    public void createTables() {
        try (final Statement statement = super.connection.createStatement()) {
            statement.execute(CREATE_TABLE);
        } catch (SQLException exception) {
            super.plugin.setDisabled(true);
            super.plugin.getLogger().warning("WARNING! Error during table creation!");
            super.plugin.getLogger().warning(exception.getLocalizedMessage());
            if (super.plugin.getLightConfiguration().isCrashShutdown()) {
                super.plugin.getLogger().warning("Shutting down server . . .");
                super.plugin.getServer().shutdown();
            }
        }
    }
}
