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

package top.cmarco.lightlogin.command.utils;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public final class PlayerUtils {

    private PlayerUtils() {
        throw new RuntimeException();
    }

    @Nullable
    public static UUID getUuid(@NotNull final Plugin plugin, @NotNull final String username) {
        UUID playerUUID = null;
        if (!plugin.getServer().getOnlineMode()) {
            playerUUID = UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(StandardCharsets.UTF_8));
        } else {
            OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(username);
            if (offlinePlayer.hasPlayedBefore()) {
                playerUUID = offlinePlayer.getUniqueId();
            }
        }

        return playerUUID;
    }
}
