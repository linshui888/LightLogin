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

import org.jetbrains.annotations.NotNull;

public enum AuthenticationCause {

    COMMAND("Command"),
    AUTOMATIC("Automatic"),
    ADMINS("Admins");

    private final String formalName;

    AuthenticationCause(String formalName) {
        this.formalName = formalName;
    }

    @NotNull
    public String getFormalName() {
        return formalName;
    }
}
