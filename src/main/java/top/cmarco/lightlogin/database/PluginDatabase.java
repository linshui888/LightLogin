/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package top.cmarco.lightlogin.database;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.cmarco.lightlogin.data.LightLoginDbRow;

import java.sql.Connection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a specific implementation of database used by this
 * plugin to store data.
 * Only one implementation may be used at once by this software,
 * however many different ones may be coded so that the user can
 * choose his preferred one.
 *
 * Currently implemented: SQLite, MySQL, PostgresSQL
 */
public interface PluginDatabase {

    /**
     * Load the necessary driver classes used by the database.
     * When not necessary, leave implementation empty.
     */
    default void loadDriverClass() {}

    /**
     * Perform a connection to the database server.
     */
    void connect();

    /**
     * Creates the tables necessarily used by this software.
     * Do not rewrite the table if already exists.
     */
    void createTables();

    /**
     * Get the connection to the database.
     * @return Connection to the database, null if you could not achieve connection.
     */
    @Nullable Connection getConnection();

    CompletableFuture<LightLoginDbRow> searchRowFromPK(@NotNull String uuid);

    CompletableFuture<LightLoginDbRow> addRow(@NotNull LightLoginDbRow row);

    CompletableFuture<Void> updateRow(@NotNull String uuid, @NotNull LightLoginColumn column, @NotNull Object columnValue);
}
