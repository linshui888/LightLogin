package top.cmarco.lightlogin.configuration;

public enum ConfigurationFiles {

    ENGLISH("config_english.yml"),
    SPANISH("config_spanish.yml"),
    CHINESE("config_chinese.yml"),
    FRENCH("config_french.yml"),
    HEBREW("config_hebrew.yml"),
    ITALIAN("config_italian.yml"),
    RUSSIAN("config_russian.yml"),
    FILIPINO("config_filipino.yml")
    ;

    private final String filename;

    ConfigurationFiles(final String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
