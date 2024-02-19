package top.cmarco.lightlogin.database;

public enum LightLoginColumn {

    UUID("uuid", String.class),
    PASSWORD("password", String.class),
    SALT("salt", String.class),
    EMAIL("email", String.class),
    LAST_LOGIN("last_login", Long.class),
    LAST_IPV4("last_ipv4", Long.class);

    private final String name;
    private final Class<?> columnType;

    LightLoginColumn(String name, Class<?> columnType) {
        this.name = name;
        this.columnType = columnType;
    }

    public String getName() {
        return name;
    }

    public Class<?> getColumnType() {
        return columnType;
    }
}
