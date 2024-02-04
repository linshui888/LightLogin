package top.cmarco.lightlogin.database;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum LightLoginColumn {

    UUID("uuid", String.class),
    PASSWORD("password", String.class),
    SALT("salt", String.class),
    EMAIL("email", String.class),
    LAST_LOGIN("last_login", Long.class),
    LAST_IPV4("last_ipv4", Long.class);

    private final String name;
    private final Class<?> columnType;
}
