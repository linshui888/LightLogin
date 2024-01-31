/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package top.cmarco.lightlogin.database;

import org.jetbrains.annotations.NotNull;
import top.cmarco.lightlogin.LightLoginPlugin;

public final class MySqlDatabase extends CredentialPluginDatabase {

    public MySqlDatabase(@NotNull LightLoginPlugin plugin) {
        super(plugin, DatabaseType.MYSQL);
    }

    /**
     * Creates the tables necessarily used by this software.
     * Do not rewrite the table if already exists.
     */
    @Override
    public void createTables() {

    }
}
