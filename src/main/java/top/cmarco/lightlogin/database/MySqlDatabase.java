/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package top.cmarco.lightlogin.database;

import org.jetbrains.annotations.NotNull;
import top.cmarco.lightlogin.LightLoginPlugin;
import top.cmarco.lightlogin.data.LightLoginDbRow;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CompletableFuture;

public final class MySqlDatabase extends HikariPluginDatabase {

    public MySqlDatabase(@NotNull LightLoginPlugin plugin) {
        super(plugin, DatabaseType.MYSQL);
    }

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS lightlogin (" +
            "uuid char(36) primary key not null," +
            "password text not null," +
            "salt text not null," +
            "email varchar(64)," +
            "last_login integer not null," +
            "last_ipv4 integer not null" +
            ");";

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

    private final static String SELECT_WHERE = "SELECT * FROM lightlogin WHERE uuid=?;";

    @Override
    public CompletableFuture<LightLoginDbRow> searchRowFromPK(@NotNull String uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try (final PreparedStatement statement = connection.prepareStatement(SELECT_WHERE)) {
                statement.setString(1, uuid);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    final String uuidUnused = resultSet.getString(1);
                    final String hash = resultSet.getString(2);
                    final String salt = resultSet.getString(3);
                    final String email = resultSet.getString(4);
                    final long lastLogin = resultSet.getLong(5);
                    final long lastIpv4 = resultSet.getLong(6);
                    return new LightLoginDbRow(uuid, hash, salt, email, lastLogin, lastIpv4);
                }
                resultSet.close();
            } catch (SQLException exception) {
                super.plugin.getLogger().warning("WARNING! Error database search for " + uuid);
                super.plugin.getLogger().warning(exception.getLocalizedMessage());
            }
            return null;
        });
    }

    private final static String INSERT_UPDATE = "INSERT INTO lightlogin(uuid, password, salt, email, last_login, last_ipv4) " +
            "VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE password=?, salt=?, email=?, last_login=?, last_ipv4=?;";

    @Override
    public CompletableFuture<LightLoginDbRow> addRow(@NotNull LightLoginDbRow row) {
        return CompletableFuture.supplyAsync(() -> {
            try (final PreparedStatement statement = connection.prepareStatement(INSERT_UPDATE)) {
                statement.setString(1, row.uuid());
                statement.setString(2, row.passwordHash());
                statement.setString(3, row.passwordSalt());
                statement.setString(4, row.email());
                statement.setLong(5, row.lastLogin());
                statement.setLong(6, row.last_ipv4());
                statement.setString(7, row.passwordHash());
                statement.setString(8, row.passwordSalt());
                statement.setString(9, row.email());
                statement.setLong(10, row.lastLogin());
                statement.setLong(11, row.last_ipv4());
                statement.execute();
                return row;
            } catch (SQLException exception) {
                super.plugin.getLogger().warning("WARNING! Error database search for " + row);
                super.plugin.getLogger().warning(exception.getLocalizedMessage());
            }
            return null;
        });
    }

    private static final String UPDATE_TABLE = "UPDATE lightlogin SET {COLUMN}=? WHERE uuid=?;";

    @Override
    public CompletableFuture<Void> updateRow(@NotNull String uuid, @NotNull LightLoginColumn column, @NotNull Object columnValue) {

        if (!column.getColumnType().isInstance(columnValue)) {
            super.plugin.getLogger().warning("WARNING! Passed illegal value type for " + column + "equal to " + columnValue);
            return CompletableFuture.completedFuture(null);
        }

        return CompletableFuture.supplyAsync(() -> {
            try (final PreparedStatement statement = connection.prepareStatement(UPDATE_TABLE.replace("{COLUMN}", column.getName()))) {
                switch (column) {
                    case LAST_IPV4, LAST_LOGIN -> statement.setLong(1, (Long) columnValue);
                    case EMAIL, PASSWORD, SALT -> statement.setString(1, (String) columnValue);
                    default -> {
                        super.plugin.getLogger().warning("WARNING! Illegal lightlogin column value passed.");
                        return null;
                    }
                }
                statement.setString(2, uuid);
                statement.executeUpdate();
            } catch (SQLException exception) {
                super.plugin.getLogger().warning("WARNING! Error database update for ");
                super.plugin.getLogger().warning("uuid=" + uuid + " column="+column + " value=" + columnValue);
                super.plugin.getLogger().warning(exception.getLocalizedMessage());
            }
            return null;
        });
    }
}
