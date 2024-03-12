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
package top.cmarco.lightlogin;

import com.alessiodp.libby.BukkitLibraryManager;
import com.alessiodp.libby.Library;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import top.cmarco.lightlogin.command.*;
import top.cmarco.lightlogin.configuration.ConfigurationFiles;
import top.cmarco.lightlogin.configuration.LightConfiguration;
import top.cmarco.lightlogin.data.*;
import top.cmarco.lightlogin.database.*;
import top.cmarco.lightlogin.listeners.AuthenticationListener;
import top.cmarco.lightlogin.listeners.LoginAuthenticatorListener;
import top.cmarco.lightlogin.listeners.PasswordObfuscationListener;
import top.cmarco.lightlogin.listeners.PlayerUnloggedListener;
import top.cmarco.lightlogin.log.AuthLogs;
import top.cmarco.lightlogin.log.SafetyFilter;
import top.cmarco.lightlogin.log.StartupLogo;
import top.cmarco.lightlogin.mail.MailManager;
import top.cmarco.lightlogin.security.LightLoginSecurity;
import top.cmarco.lightlogin.world.generator.EmptyChunkGenerator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class LightLoginPlugin extends JavaPlugin {

    private AbstractFilter safetyFilter = null;
    private PlayerUnloggedListener playerUnloggedListener = null;
    private LoginAuthenticatorListener loginAuthenticatorListener = null;
    private AuthenticationListener authenticationListener = null;
    private PasswordObfuscationListener passwordObfuscationListener = null;
    private LightConfiguration lightConfiguration = null;
    private PluginDatabase database = null;
    private AuthenticationManager authenticationManager = null;
    private AutoKickManager autoKickManager = null;
    private BaseCommand baseCommand = null;
    private LoginCommand loginCommand = null;
    private UnloginCommand unloginCommand = null;
    private RegisterCommand registerCommand = null;
    private UnregisterCommand unregisterCommand = null;
    private ChangePasswordCommand changePasswordCommand = null;
    private EmailCommand emailCommand = null;
    private ResetPasswordCommand resetPasswordCommand = null;
    private final EnumMap<ConfigurationFiles, FileConfiguration> languagesConfigMap = new EnumMap<>(ConfigurationFiles.class);
    private PlaintextPasswordManager plaintextPasswordManager = null;
    private boolean disabled = false;
    private BukkitLibraryManager bukkitLibraryManager = null;
    private Library hikariCP = null, bcpkix = null, bcutils = null, bcprov = null, postgreSQL = null, jakartaMail = null, jakartaApi = null, angus = null;
    private World loginWorld;
    private AuthLogs authLogs = null;
    private VoidLoginManager voidLoginManager = null;
    private StartupLoginsManager startupLoginsManager = null;
    private LightLoginSecurity lightLoginSecurity = null;
    private MailManager mailManager = null;

    /* --------------------------------------------------------- */

    /**
     * Plugin startup logic.
     * Called on STARTUP phase.
     */
    @Override
    public void onEnable() {
        this.printLogoStartup();
        this.setupChatFilter();
        this.setupSecurity();
        this.loadLibraries();
        this.setupConfig();
        this.setupDatabase();
        this.loadLoginWorld(); // 1
        this.setVoidLoginManager(); // 2
        this.setupAuthenticationManager();
        this.setupPasswordManager();
        this.setupStartupLoginsManager();
        this.registerAllListeners();
        this.setupMailManager();
        this.setupCommands();
        this.setupKickManager();
        this.setAuthLogs();
    }

    /**
     * Plugin disable logic.
     */
    @Override
    public void onDisable() {
        this.database.close();
        this.autoKickManager.stopAutoKickTask();
        this.authLogs.saveLogs();
    }

    /* --------------------------------------------------------- */

    private void setupMailManager() {
        this.mailManager = new MailManager(this);

        if (!lightConfiguration.isEmailEnabled()) {
            return;
        }

        this.mailManager.startSession();
    }

    private void setupStartupLoginsManager() {
        this.startupLoginsManager = new StartupLoginsManager();
    }

    public void sendConsoleColoured(String text) {
        super.getServer().getConsoleSender().sendMessage(LightLoginCommand.colorMessage(text));
    }

    private void setupPasswordManager() {
        this.plaintextPasswordManager = new PlaintextPasswordManager();
    }

    private void printLogoStartup() {
        this.sendConsoleColoured(StartupLogo.LOGO_ARRAY[0]);
        this.sendConsoleColoured(StartupLogo.getSTRINGS(0));
    }

    private void setupSecurity() {
        this.lightLoginSecurity = new LightLoginSecurity(this);
        this.sendConsoleColoured(StartupLogo.getSTRINGS(1));
        this.sendConsoleColoured(StartupLogo.getSTRINGS(2));
    }

    public void setVoidLoginManager() {
        if (!this.lightConfiguration.isVoidWorldEnabled()) {
            return;
        }

        this.sendConsoleColoured(StartupLogo.getSTRINGS(3));
        this.voidLoginManager = new VoidLoginManager(this.loginWorld);
        this.sendConsoleColoured(StartupLogo.getSTRINGS(4));
    }

    private void setAuthLogs() {
        this.sendConsoleColoured(StartupLogo.getSTRINGS(5));
        this.authLogs = new AuthLogs(this);
        this.sendConsoleColoured(StartupLogo.getSTRINGS(6));
    }

    public void loadLoginWorld() {

        if (!this.lightConfiguration.isVoidWorldEnabled()) {
            return;
        }

        if (loginWorld != null) {
            return;
        }

        WorldCreator worldCreator = WorldCreator.name(StartupLogo.getSTRINGS(7));
        worldCreator.generator(new EmptyChunkGenerator());

        World.Environment environment = null;
        String configEnv = lightConfiguration.getVoidWorldMode();
        for (World.Environment env : World.Environment.values()) {
            if (env.name().equalsIgnoreCase(configEnv)) {
                environment = env;
                break;
            }
        }

        worldCreator.environment(environment == null ? World.Environment.NORMAL : environment);
        World loginWorld = worldCreator.createWorld();
        assert loginWorld != null;
        this.loginWorld = loginWorld;
    }


    private void loadLibraries() {
        if (this.hikariCP != null || this.bcpkix != null || this.bcutils != null
                || this.bcprov != null || this.bukkitLibraryManager != null || postgreSQL != null
        || angus != null) {
            return;
        }

        this.bukkitLibraryManager = new BukkitLibraryManager(this);
        this.bukkitLibraryManager.addMavenCentral();
        this.hikariCP = Library.builder()
                .groupId(StartupLogo.getMY_STRINGS(0))
                .artifactId(StartupLogo.getMY_STRINGS(1))
                .version(StartupLogo.getMY_STRINGS(2))
                .relocate(StartupLogo.getMY_STRINGS(3), StartupLogo.getMY_STRINGS(4))
                .build();
        this.bcpkix = Library.builder()
                .groupId(StartupLogo.getMY_STRINGS(5))
                .artifactId(StartupLogo.getMY_STRINGS(6))
                .version(StartupLogo.getMY_STRINGS(7))
                .relocate(StartupLogo.getMY_STRINGS(8), StartupLogo.getMY_STRINGS(9))
                .build();
        this.bcutils = Library.builder()
                .groupId(StartupLogo.getMY_STRINGS(10))
                .artifactId(StartupLogo.getMY_STRINGS(11))
                .version(StartupLogo.getMY_STRINGS(12))
                .relocate(StartupLogo.getMY_STRINGS(13), StartupLogo.getMY_STRINGS(14))
                .build();
        this.bcprov = Library.builder()
                .groupId(StartupLogo.getMY_STRINGS(15))
                .artifactId(StartupLogo.getMY_STRINGS(16))
                .version(StartupLogo.getMY_STRINGS(17))
                .relocate(StartupLogo.getMY_STRINGS(18), StartupLogo.getMY_STRINGS(19))
                .build();
        this.postgreSQL = Library.builder()
                .groupId(StartupLogo.getMY_STRINGS(20))
                .artifactId(StartupLogo.getMY_STRINGS(21))
                .version(StartupLogo.getMY_STRINGS(22))
                .relocate(StartupLogo.getMY_STRINGS(23), StartupLogo.getMY_STRINGS(24))
                .build();

        this.angus = Library.builder()
                .groupId("org{}eclipse{}angus")
                .artifactId("angus-mail")
                .version("2.0.3")
                .relocate("org{}eclipse{}angus", "top{}cmarco{}lightlogin{}libs{}org{}eclipse{}angus")
                .build();

        this.jakartaApi = Library.builder()
                .groupId("jakarta.activation")
                .artifactId("jakarta.activation-api")
                .version("2.1.3")
                .relocate("jakarta{}activation", "top{}cmarco{}lightlogin{}libs{}jakarta{}activation")
                .build();

        this.jakartaMail = Library.builder()
                .groupId("jakarta.mail")
                .artifactId("jakarta.mail-api")
                .version("2.1.3")
                .relocate("jakarta{}mail","top{}cmarco{}lightlogin{}libs{}jakarta{}mail")
                .build();

        this.bukkitLibraryManager.loadLibraries(this.bcpkix, this.bcprov, this.bcutils, this.hikariCP, this.postgreSQL, this.jakartaApi, this.jakartaMail, this.angus);
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    private YamlConfiguration createCustomConfig(@NotNull ConfigurationFiles configurationFile) {
        File customConfigFile = new File(super.getDataFolder(), configurationFile.getFilename());

        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            super.saveResource(configurationFile.getFilename(), false);
        }

        final YamlConfiguration customConfig = new YamlConfiguration();

        try {

            YamlConfiguration original = new YamlConfiguration();
            InputStream stream = super.getResource(configurationFile.getFilename());
            assert stream != null;
            InputStreamReader streamReader = new InputStreamReader(stream);
            original.load(streamReader);
            customConfig.load(customConfigFile);

            Map<String, Object> currentValues = customConfig.getValues(true);
            Map<String, Object> originalValues = original.getValues(true);

            originalValues.forEach((a,b) -> {
                if (!currentValues.containsKey(a)) {
                    customConfig.set(a, b);
                }
            });

            customConfig.save(customConfigFile);

            streamReader.close();
            stream.close();

            return customConfig;
        } catch (IOException | InvalidConfigurationException exception) {
            getLogger().warning(StartupLogo.getSTRINGS(8) + configurationFile.getFilename());
            getLogger().warning(exception.getLocalizedMessage());
        }
        return null;
    }

    private void saveAllConfigs() {
        for (final ConfigurationFiles file : ConfigurationFiles.values()) {
            final FileConfiguration fileConfiguration = createCustomConfig(file);
            this.languagesConfigMap.put(file, fileConfiguration);
        }
    }

    /**
     * Setup chat filter to prevent password leaks
     */
    private void setupChatFilter() {
        this.sendConsoleColoured(StartupLogo.getSTRINGS(9));
        Logger rootLogger = (Logger) LogManager.getRootLogger();
        this.safetyFilter = new SafetyFilter();
        this.safetyFilter.start();
        rootLogger.addFilter(this.safetyFilter);
        this.sendConsoleColoured(StartupLogo.getSTRINGS(10));
    }

    /**
     * Setup all the commands registered by this software,
     * registering them into the server instance.
     *
     */
    private void setupCommands() {
        this.baseCommand = new BaseCommand(this);
        this.loginCommand = new LoginCommand(this);
        this.unloginCommand = new UnloginCommand(this);
        this.registerCommand = new RegisterCommand(this);
        this.unregisterCommand = new UnregisterCommand(this);
        this.changePasswordCommand = new ChangePasswordCommand(this);
        this.emailCommand = new EmailCommand(this);
        this.resetPasswordCommand = new ResetPasswordCommand(this);
        this.baseCommand.register();
        this.loginCommand.register();
        this.loginCommand.startClearTasks();
        this.registerCommand.register();
        this.unregisterCommand.register();
        this.unloginCommand.register();
        this.changePasswordCommand.register();
        this.emailCommand.register();
        this.resetPasswordCommand.register();
    }

    /**
     * Setup the authentication manager instance
     */
    private void setupAuthenticationManager() {
        if (this.authenticationManager != null) {
            return;
        }

        this.sendConsoleColoured(StartupLogo.getSTRINGS(11));
        this.authenticationManager = new BasicAuthenticationManager(this);
        this.authenticationManager.startLoginNotifyTask();
        this.authenticationManager.startRegisterNotifyTask();
        this.sendConsoleColoured(StartupLogo.getSTRINGS(12));
    }

    private void setupKickManager() {
        if (this.autoKickManager != null) {
            return;
        }

        this.sendConsoleColoured(StartupLogo.getSTRINGS(13));
        this.autoKickManager = new AutoKickManager(this);
        this.autoKickManager.startAutoKickTask();
        this.sendConsoleColoured(StartupLogo.getSTRINGS(14));
    }

    /**
     * Method responsible for registering all of this software listeners.
     */
    private void registerAllListeners() {
        this.sendConsoleColoured(StartupLogo.getSTRINGS(15));
        this.playerUnloggedListener = new PlayerUnloggedListener(this);
        this.loginAuthenticatorListener = new LoginAuthenticatorListener(this);
        this.authenticationListener = new AuthenticationListener(this);
        this.passwordObfuscationListener = new PasswordObfuscationListener(this);

        getServer().getPluginManager().registerEvents(this.loginAuthenticatorListener, this);
        getServer().getPluginManager().registerEvents(this.playerUnloggedListener, this);
        getServer().getPluginManager().registerEvents(this.authenticationListener, this);
        getServer().getPluginManager().registerEvents(this.passwordObfuscationListener, this);
        this.sendConsoleColoured(StartupLogo.getSTRINGS(16));
    }

    private void setupConfig() {
        this.sendConsoleColoured(StartupLogo.getSTRINGS(17));
        saveDefaultConfig();
        saveAllConfigs();

        final String language = getConfig().getString(StartupLogo.getSTRINGS(18));
        ConfigurationFiles chosenLanguage = null;

        for (ConfigurationFiles value : ConfigurationFiles.values()) {
            if (value.name().equalsIgnoreCase(language)) {
                chosenLanguage = value;
                break;
            }
        }

        if (chosenLanguage == null) {
            getLogger().warning(StartupLogo.getSTRINGS(19)+ language);
            getLogger().warning(StartupLogo.getSTRINGS(20));
            chosenLanguage = ConfigurationFiles.ENGLISH;
        }

        this.sendConsoleColoured(StartupLogo.getSTRINGS(21) + language + "!");

        lightConfiguration = new LightConfiguration(chosenLanguage, this);
        lightConfiguration.loadConfig();
    }

    private void setupDatabase() {
        this.sendConsoleColoured(StartupLogo.getSTRINGS(22));
        final DatabaseType databaseType = DatabaseType.fromName(
                Objects.requireNonNull(this.lightConfiguration.getDatabaseType()));

        if (databaseType == null) {
            this.disabled = true;
            getLogger().warning(StartupLogo.getSTRINGS(23));
            if (lightConfiguration.isCrashShutdown()) {
                getLogger().warning(StartupLogo.getSTRINGS(24));
                getServer().shutdown();
            }
            return;
        }

        switch (databaseType) {
            case SQLITE: {this.database = new SQLiteDatabase(this); break;}
            case MYSQL: {this.database = new MySqlDatabase(this); break;}
            case POSTGRESQL: {this.database = new PostgreSQLDatabase(this); break;}
            default: {this.database = null; break;}
        }

        assert this.database != null;

        this.database.loadDriverClass();
        this.database.connect();
        this.database.createTables();

        this.sendConsoleColoured(StartupLogo.getSTRINGS(25) + databaseType.name() + StartupLogo.getSTRINGS(26));
    }

    /* -------------------------------------------------------------------------- */

    public LightConfiguration getLightConfiguration() {
        return lightConfiguration;
    }

    public PluginDatabase getDatabase() {
        return database;
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public EnumMap<ConfigurationFiles, FileConfiguration> getLanguagesConfigMap() {
        return languagesConfigMap;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public AutoKickManager getAutoKickManager() {
        return autoKickManager;
    }

    public World getLoginWorld() {
        return loginWorld;
    }

    public PlaintextPasswordManager getPlaintextPasswordManager() {
        return plaintextPasswordManager;
    }

    public AuthLogs getAuthLogs() {
        return authLogs;
    }

    public VoidLoginManager getVoidLoginManager() {
        return voidLoginManager;
    }

    public StartupLoginsManager getStartupLoginsManager() {
        return startupLoginsManager;
    }

    public MailManager getMailManager() {
        return mailManager;
    }
}