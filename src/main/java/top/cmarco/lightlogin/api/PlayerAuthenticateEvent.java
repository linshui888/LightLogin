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

package top.cmarco.lightlogin.api;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public final class PlayerAuthenticateEvent extends PlayerEvent {

    private final AuthenticationCause authenticationCause;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public PlayerAuthenticateEvent(@NotNull Player who, @NotNull AuthenticationCause authenticationCause) {
        super(who);
        this.authenticationCause = authenticationCause;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @NotNull @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    @NotNull
    public AuthenticationCause getAuthenticationCause() {
        return authenticationCause;
    }
}
