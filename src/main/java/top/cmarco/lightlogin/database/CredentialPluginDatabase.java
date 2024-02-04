/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package top.cmarco.lightlogin.database;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.cmarco.lightlogin.LightLoginPlugin;
import top.cmarco.lightlogin.configuration.LightConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Getter
public abstract class CredentialPluginDatabase extends AbstractPluginDatabase {

    /* ------------------------------ */

    protected final String username;
    protected final String password;
    protected final String address;
    protected final String databaseName;
    protected final int port;

    /* ------------------------------ */

    protected Connection connection = null;

    protected CredentialPluginDatabase(final @NotNull LightLoginPlugin plugin,
                                       final @NotNull DatabaseType type) {
        super(plugin, type);
        LightConfiguration conf = plugin.getLightConfiguration();
        this.username = conf.getUsername();
        this.password = conf.getPassword();
        this.address = conf.getAddress();
        this.databaseName = conf.getDatabaseName();
        this.port = conf.getPort();
    }


    @NotNull
    protected String createConnectionUrl() {
        return String.format("jdbc:%s://%s:%d/%s",
                this.databaseType.getName(),
                this.address,
                this.port,
                this.databaseName);
    }

    @Override
    public final @Nullable Connection getConnection() {
        return this.connection;
    }

    @Override
    public void connect() {

        if (this.connection != null) {
            return;
        }

        try {
            this.connection = DriverManager.getConnection(this.createConnectionUrl());
        } catch (SQLException exception) {
            super.plugin.getLogger().warning("WARNING! Could not connect to the database.");
            super.plugin.setDisabled(true);
            if (super.plugin.getLightConfiguration().isCrashShutdown()) {
                super.plugin.getLogger().warning("Shutting the server down!");
                super.plugin.getServer().shutdown();
            }
        }
    }
}
