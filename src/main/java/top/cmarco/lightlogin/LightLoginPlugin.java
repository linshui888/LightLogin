/*
* This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
* If a copy of the MPL was not distributed with this file,
* You can obtain one at https://mozilla.org/MPL/2.0/.
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
import top.cmarco.lightlogin.security.LightLoginSecurity;
import top.cmarco.lightlogin.world.generator.EmptyChunkGenerator;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Objects;

@SuppressWarnings("removal")
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
    private final EnumMap<ConfigurationFiles, FileConfiguration> languagesConfigMap = new EnumMap<>(ConfigurationFiles.class);
    private PlaintextPasswordManager plaintextPasswordManager = null;
    private boolean disabled = false;
    private BukkitLibraryManager bukkitLibraryManager = null;
    private Library hikariCP = null, bcpkix = null, bcutils = null, bcprov = null, postgreSQL = null;
    private World loginWorld;
    private AuthLogs authLogs = null;
    private VoidLoginManager voidLoginManager = null;
    private StartupLoginsManager startupLoginsManager = null;
    private LightLoginSecurity lightLoginSecurity = null;

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
        this.sendConsoleColoured(StartupLogo.LOGO);
        this.sendConsoleColoured("&e© CMarco 2024");
    }

    private void setupSecurity() {
        this.lightLoginSecurity = new LightLoginSecurity(this);
        this.sendConsoleColoured("&7[ &a&l. . .&r &7] &eSetting up JVM security factors");
        java.lang.System.setSecurityManager(this.lightLoginSecurity);
        this.sendConsoleColoured("&7[ &a&l✔&r &7] &aCorrectly set up JVM security factors!");
    }

    public void setVoidLoginManager() {
        if (!this.lightConfiguration.isVoidWorldEnabled()) {
            return;
        }

        this.sendConsoleColoured("&7[ &a&l. . .&r &7] &eSetting up void world feature");
        this.voidLoginManager = new VoidLoginManager(this.loginWorld);
        this.sendConsoleColoured("&7[ &a&l✔&r &7] &aCorrectly set up void world feature!");
    }

    private void setAuthLogs() {
        this.sendConsoleColoured("&7[ &a&l. . .&r &7] &eSetting up authentication logging");
        this.authLogs = new AuthLogs(this);
        this.sendConsoleColoured("&7[ &a&l✔&r &7] &aCorrectly set up void authentication logging!");
    }

    public void loadLoginWorld() {

        if (!this.lightConfiguration.isVoidWorldEnabled()) {
            return;
        }

        if (loginWorld != null) {
            return;
        }

        WorldCreator worldCreator = WorldCreator.name("login_world");
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
        if (this.hikariCP != null || this.bcpkix != null || this.bcutils != null || this.bcprov != null || this.bukkitLibraryManager != null || postgreSQL != null) {
            return;
        }

        this.bukkitLibraryManager = new BukkitLibraryManager(this);
        this.bukkitLibraryManager.addMavenCentral();
        this.hikariCP = Library.builder()
                .groupId("com{}zaxxer")
                .artifactId("HikariCP")
                .version("5.0.1")
                .relocate("com{}zaxxer{}hikari","top{}cmarco{}lightlogin{}libs{}com{}zaxxer{}hikari")
                .build();
        this.bcpkix = Library.builder()
                .groupId("org{}bouncycastle")
                .artifactId("bcpkix-jdk18on")
                .version("1.77")
                .relocate("org{}bouncycastle", "top{}cmarco{}lightlogin{}libs{}org{}bouncycastle")
                .build();
        this.bcutils = Library.builder()
                .groupId("org{}bouncycastle")
                .artifactId("bcutil-jdk18on")
                .version("1.77")
                .relocate("org{}bouncycastle", "top{}cmarco{}lightlogin{}libs{}org{}bouncycastle")
                .build();
        this.bcprov = Library.builder()
                .groupId("org{}bouncycastle")
                .artifactId("bcprov-jdk18on")
                .version("1.77")
                .relocate("org{}bouncycastle", "top{}cmarco{}lightlogin{}libs{}org{}bouncycastle")
                .build();
        this.postgreSQL = Library.builder()
                .groupId("org{}postgresql")
                .artifactId("postgresql")
                .version("42.7.2")
                .relocate("org{}postgresql", "top{}cmarco{}lightlogin{}libs{}org{}postgresql")
                .build();

        this.bukkitLibraryManager.loadLibraries(this.bcpkix, this.bcprov, this.bcutils, this.hikariCP, this.postgreSQL);
    }

    private YamlConfiguration createCustomConfig(@NotNull ConfigurationFiles configurationFile) {
        YamlConfiguration customConfig = null;
        File customConfigFile = new File(super.getDataFolder(), configurationFile.getFilename());

        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            super.saveResource(configurationFile.getFilename(), false);
        }

        customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
            customConfig.options().copyDefaults(true);
            return customConfig;
        } catch (IOException | InvalidConfigurationException exception) {
            getLogger().warning("WARNING! Error loading custom configuration " + configurationFile.getFilename());
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
        this.sendConsoleColoured("&7[ &a&l. . .&r &7] &eSetting up safe log filter");
        Logger rootLogger = (Logger) LogManager.getRootLogger();
        this.safetyFilter = new SafetyFilter();
        this.safetyFilter.start();
        rootLogger.addFilter(this.safetyFilter);
        this.sendConsoleColoured("&7[ &a&l✔&r &7] &aCorrectly set up safe log filter!");
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
        this.baseCommand.register();
        this.loginCommand.register();
        this.loginCommand.startClearTasks();
        this.registerCommand.register();
        this.unregisterCommand.register();
        this.unloginCommand.register();
        this.changePasswordCommand.register();
    }

    /**
     * Setup the authentication manager instance
     */
    private void setupAuthenticationManager() {
        if (this.authenticationManager != null) {
            return;
        }

        this.sendConsoleColoured("&7[ &a&l. . .&r &7] &eSetting up authentication manager.");
        this.authenticationManager = new BasicAuthenticationManager(this);
        this.authenticationManager.startLoginNotifyTask();
        this.authenticationManager.startRegisterNotifyTask();
        this.sendConsoleColoured("&7[ &a&l✔&r &7] &aCorrectly set up authentication manager!");
    }

    private void setupKickManager() {
        if (this.autoKickManager != null) {
            return;
        }

        this.sendConsoleColoured("&7[ &a&l. . .&r &7] &eSetting up kick manager.");
        this.autoKickManager = new AutoKickManager(this);
        this.autoKickManager.startAutoKickTask();
        this.sendConsoleColoured("&7[ &a&l✔&r &7] &aCorrectly set up kick manager!");
    }

    /**
     * Method responsible for registering all of this software listeners.
     */
    private void registerAllListeners() {
        this.sendConsoleColoured("&7[ &a&l. . .&r &7] &eSetting up custom listeners.");
        this.playerUnloggedListener = new PlayerUnloggedListener(this);
        this.loginAuthenticatorListener = new LoginAuthenticatorListener(this);
        this.authenticationListener = new AuthenticationListener(this);
        this.passwordObfuscationListener = new PasswordObfuscationListener(this);

        getServer().getPluginManager().registerEvents(this.loginAuthenticatorListener, this);
        getServer().getPluginManager().registerEvents(this.playerUnloggedListener, this);
        getServer().getPluginManager().registerEvents(this.authenticationListener, this);
        getServer().getPluginManager().registerEvents(this.passwordObfuscationListener, this);
        this.sendConsoleColoured("&7[ &a&l✔&r &7] &aCorrectly set up custom listeners!");
    }

    private void setupConfig() {
        this.sendConsoleColoured("&7[ &a&l. . .&r &7] &eSetting up configurations.");
        saveDefaultConfig();
        saveAllConfigs();

        final String language = getConfig().getString("language");
        ConfigurationFiles chosenLanguage = null;

        for (ConfigurationFiles value : ConfigurationFiles.values()) {
            if (value.name().equalsIgnoreCase(language)) {
                chosenLanguage = value;
                break;
            }
        }

        if (chosenLanguage == null) {
            getLogger().warning("WARNING! Invalid configuration language chosen as " + language);
            getLogger().warning("Will use config_english.yml until issue is solved!");
            chosenLanguage = ConfigurationFiles.ENGLISH;
        }

        this.sendConsoleColoured("&7[ &a&l✔&r &7] &aLoaded config with language" + language + "!");

        lightConfiguration = new LightConfiguration(chosenLanguage, this);
        lightConfiguration.loadConfig();
    }

    private void setupDatabase() {
        this.sendConsoleColoured("&7[ &a&l. . .&r &7] &eSetting up authentication Database.");
        final DatabaseType databaseType = DatabaseType.fromName(
                Objects.requireNonNull(this.lightConfiguration.getDatabaseType()));

        if (databaseType == null) {
            this.disabled = true;
            getLogger().warning("WARNING! Invalid database type from config file. Cannot load plugin.");
            if (lightConfiguration.isCrashShutdown()) {
                getLogger().warning("Shutting down server . . .");
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

        this.sendConsoleColoured("&7[ &a&l✔&r &7] &aCorrectly set up Authentication Database &7(&a" + databaseType.name() + "&7)&e!");
    }

    /* -------------------------------------------------------------------------- */

    public AbstractFilter getSafetyFilter() {
        return safetyFilter;
    }

    public PlayerUnloggedListener getPlayerUnloggedListener() {
        return playerUnloggedListener;
    }

    public LoginAuthenticatorListener getLoginAuthenticatorListener() {
        return loginAuthenticatorListener;
    }

    public LightConfiguration getLightConfiguration() {
        return lightConfiguration;
    }

    public PluginDatabase getDatabase() {
        return database;
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public BaseCommand getBaseCommand() {
        return baseCommand;
    }

    public LoginCommand getLoginCommand() {
        return loginCommand;
    }

    public RegisterCommand getRegisterCommand() {
        return registerCommand;
    }

    public UnregisterCommand getUnregisterCommand() {
        return unregisterCommand;
    }

    public EnumMap<ConfigurationFiles, FileConfiguration> getLanguagesConfigMap() {
        return languagesConfigMap;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public AuthenticationListener getAuthenticationListener() {
        return authenticationListener;
    }

    public ChangePasswordCommand getChangePasswordCommand() {
        return changePasswordCommand;
    }

    public BukkitLibraryManager getBukkitLibraryManager() {
        return bukkitLibraryManager;
    }

    public Library getHikariCP() {
        return hikariCP;
    }

    public Library getBcpkix() {
        return bcpkix;
    }

    public Library getBcutils() {
        return bcutils;
    }

    public Library getBcprov() {
        return bcprov;
    }

    public UnloginCommand getUnloginCommand() {
        return unloginCommand;
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


}