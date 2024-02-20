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
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import top.cmarco.lightlogin.command.*;
import top.cmarco.lightlogin.configuration.ConfigurationFiles;
import top.cmarco.lightlogin.configuration.LightConfiguration;
import top.cmarco.lightlogin.data.AuthenticationManager;
import top.cmarco.lightlogin.data.BasicAuthenticationManager;
import top.cmarco.lightlogin.database.DatabaseType;
import top.cmarco.lightlogin.database.MySqlDatabase;
import top.cmarco.lightlogin.database.PluginDatabase;
import top.cmarco.lightlogin.database.SQLiteDatabase;
import top.cmarco.lightlogin.listeners.AuthenticationListener;
import top.cmarco.lightlogin.listeners.LoginAuthenticatorListener;
import top.cmarco.lightlogin.listeners.PlayerUnloggedListener;
import top.cmarco.lightlogin.log.SafetyFilter;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Objects;

public final class LightLoginPlugin extends JavaPlugin {

    private AbstractFilter safetyFilter = null;
    private PlayerUnloggedListener playerUnloggedListener = null;
    private LoginAuthenticatorListener loginAuthenticatorListener = null;
    private AuthenticationListener authenticationListener = null;
    private LightConfiguration lightConfiguration = null;
    private PluginDatabase database = null;
    private AuthenticationManager authenticationManager = null;
    private BaseCommand baseCommand = null;
    private LoginCommand loginCommand = null;
    private UnloginCommand unloginCommand = null;
    private RegisterCommand registerCommand = null;
    private UnregisterCommand unregisterCommand = null;
    private ChangePasswordCommand changePasswordCommand = null;
    private final EnumMap<ConfigurationFiles, FileConfiguration> languagesConfigMap = new EnumMap<>(ConfigurationFiles.class);
    private boolean disabled = false;

    private BukkitLibraryManager bukkitLibraryManager = null;
    private Library hikariCP = null, bcpkix = null, bcutils = null, bcprov = null;

    private void loadLibraries() {
        if (this.hikariCP != null || this.bcpkix != null || this.bcutils != null || this.bcprov != null || this.bukkitLibraryManager != null) {
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

        this.bukkitLibraryManager.loadLibraries(this.bcpkix, this.bcprov, this.bcutils, this.hikariCP);
    }

    private YamlConfiguration createCustomConfig(@NotNull ConfigurationFiles configurationFile) {
        YamlConfiguration customConfig = null;
        File customConfigFile = new File(super.getDataFolder(), configurationFile.getFilename());
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource(configurationFile.getFilename(), false);
        }

        customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
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
        Logger rootLogger = (Logger) LogManager.getRootLogger();
        this.safetyFilter = new SafetyFilter();
        this.safetyFilter.start();
        rootLogger.addFilter(this.safetyFilter);
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

        this.authenticationManager = new BasicAuthenticationManager(this);
        this.authenticationManager.startLoginNotifyTask();
        this.authenticationManager.startRegisterNotifyTask();
    }

    /**
     * Method responsible for registering all of this software listeners.
     */
    private void registerAllListeners() {
        this.playerUnloggedListener = new PlayerUnloggedListener(this);
        this.loginAuthenticatorListener = new LoginAuthenticatorListener(this);
        this.authenticationListener = new AuthenticationListener(this);

        getServer().getPluginManager().registerEvents(this.loginAuthenticatorListener, this);
        getServer().getPluginManager().registerEvents(this.playerUnloggedListener, this);
        getServer().getPluginManager().registerEvents(this.authenticationListener, this);
    }

    private void setupConfig() {
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

        getLogger().info("Loaded config with language " + language);

        lightConfiguration = new LightConfiguration(chosenLanguage, this);
        lightConfiguration.loadConfig();
    }

    private void setupDatabase() {
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
            default: {this.database = null; break;}
        }

        assert this.database != null;

        this.database.loadDriverClass();
        this.database.connect();
        this.database.createTables();
    }

    /**
     * Plugin startup logic.
     * Called on STARTUP phase.
     */
    @Override
    public void onEnable() {
        this.loadLibraries();
        this.setupConfig();
        this.setupDatabase();
        this.setupAuthenticationManager();
        this.registerAllListeners();
        this.setupCommands();
        this.setupChatFilter();
    }

    /**
     * Plugin disable logic.
     */
    @Override
    public void onDisable() {
        this.database.close();
    }

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
}