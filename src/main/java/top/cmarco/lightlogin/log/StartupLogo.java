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

package top.cmarco.lightlogin.log;

public class StartupLogo {


    public static final String LOGO =
            "&6░▒▓█▓▒░      ░▒▓█▓▒░░▒▓██████▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓████████▓▒░▒▓█▓▒░      ░▒▓██████▓▒░ ░▒▓██████▓▒░░▒▓█▓▒░▒▓███████▓▒░  \n" +
            "&6░▒▓█▓▒░      ░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░  ░▒▓█▓▒░   ░▒▓█▓▒░     ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░ \n" +
            "&6░▒▓█▓▒░      ░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░  ░▒▓█▓▒░   ░▒▓█▓▒░     ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░ \n" +
            "&6░▒▓█▓▒░      ░▒▓█▓▒░▒▓█▓▒▒▓███▓▒░▒▓████████▓▒░  ░▒▓█▓▒░   ░▒▓█▓▒░     ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒▒▓███▓▒░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░ \n" +
            "&6░▒▓█▓▒░      ░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░  ░▒▓█▓▒░   ░▒▓█▓▒░     ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░ \n" +
            "&6░▒▓█▓▒░      ░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░  ░▒▓█▓▒░   ░▒▓█▓▒░     ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░ \n" +
            "&6░▒▓████████▓▒░▒▓█▓▒░░▒▓██████▓▒░░▒▓█▓▒░░▒▓█▓▒░  ░▒▓█▓▒░   ░▒▓████████▓▒░▒▓██████▓▒░ ░▒▓██████▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░";

    public static final String[] LOGO_ARRAY = {LOGO};

    private static final String[] STRINGS = {
            "&e© CMarco 2024",
            "&7[ &a&l. . .&r &7] &eSetting up JVM security factors",
            "&7[ &a&l✔&r &7] &aCorrectly set up JVM security factors!",
            "&7[ &a&l. . .&r &7] &eSetting up void world feature",
            "&7[ &a&l✔&r &7] &aCorrectly set up void world feature!",
            "&7[ &a&l. . .&r &7] &eSetting up authentication logging",
            "&7[ &a&l✔&r &7] &aCorrectly set up void authentication logging!",
            "login_world",
            "WARNING! Error loading custom configuration ",
            "&7[ &a&l. . .&r &7] &eSetting up safe log filter",
            "&7[ &a&l✔&r &7] &aCorrectly set up safe log filter!",
            "&7[ &a&l. . .&r &7] &eSetting up authentication manager.",
            "&7[ &a&l✔&r &7] &aCorrectly set up authentication manager!",
            "&7[ &a&l. . .&r &7] &eSetting up kick manager.",
            "&7[ &a&l✔&r &7] &aCorrectly set up kick manager!",
            "&7[ &a&l. . .&r &7] &eSetting up custom listeners.",
            "&7[ &a&l✔&r &7] &aCorrectly set up custom listeners!",
            "&7[ &a&l. . .&r &7] &eSetting up configurations.",
            "language",
            "WARNING! Invalid configuration language chosen as ",
            "Will use config_english.yml until issue is solved!",
            "&7[ &a&l✔&r &7] &aLoaded config with language",
            "&7[ &a&l. . .&r &7] &eSetting up authentication Database.",
            "WARNING! Invalid database type from config file. Cannot load plugin.",
            "Shutting down server . . .",
            "&7[ &a&l✔&r &7] &aCorrectly set up Authentication Database &7(&a",
            "&7)&e!"
    };
    private static final String[] MY_STRINGS = {
            "com{}zaxxer", "HikariCP", "5.0.1", "com{}zaxxer{}hikari", "top{}cmarco{}lightlogin{}libs{}com{}zaxxer{}hikari",
            "org{}bouncycastle", "bcpkix-jdk18on", "1.77", "org{}bouncycastle", "top{}cmarco{}lightlogin{}libs{}org{}bouncycastle",
            "org{}bouncycastle", "bcutil-jdk18on", "1.77", "org{}bouncycastle", "top{}cmarco{}lightlogin{}libs{}org{}bouncycastle",
            "org{}bouncycastle", "bcprov-jdk18on", "1.77", "org{}bouncycastle", "top{}cmarco{}lightlogin{}libs{}org{}bouncycastle",
            "org{}postgresql", "postgresql", "42.7.2", "org{}postgresql", "top{}cmarco{}lightlogin{}libs{}org{}postgresql"
    };

    public static String getMY_STRINGS(int number) {
        return MY_STRINGS[number];
    }

    public static String getSTRINGS(int number) {
        return STRINGS[number];
    }

}
