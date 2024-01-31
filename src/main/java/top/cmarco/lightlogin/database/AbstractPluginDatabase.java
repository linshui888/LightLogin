/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package top.cmarco.lightlogin.database;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import top.cmarco.lightlogin.LightLoginPlugin;

/**
 * An abstract implementation for PluginDatabase.
 */
@Getter
public abstract class AbstractPluginDatabase implements PluginDatabase {

    protected final LightLoginPlugin plugin;
    protected final DatabaseType databaseType;

    protected AbstractPluginDatabase(final @NotNull LightLoginPlugin plugin,
                                     final @NotNull DatabaseType databaseType) {
        this.plugin = plugin;
        this.databaseType = databaseType;
    }

    @Override
    public void loadDriverClass() {
        try {
            final Class<?> driverClass = Class.forName(this.databaseType.getClassName());
        } catch (ClassNotFoundException exception) {
            this.plugin.setDisabled(true);
            this.plugin.getLogger().warning("WARNING! Could not load database driver class \"" + this.databaseType.getClassName() + "\"");
            if (this.plugin.getLightConfiguration().isCrashShutdown()) {
                this.plugin.getLogger().warning("Shutting down the server. . .");
                this.plugin.getServer().shutdown();
            }
        }
    }
}
