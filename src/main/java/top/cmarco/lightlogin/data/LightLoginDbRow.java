package top.cmarco.lightlogin.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LightLoginDbRow {
    @NotNull
    private final String uuid;

    @NotNull
    private final String passwordHash;

    @NotNull
    private final String passwordSalt;

    @Nullable
    private final String email;

    private final long lastLogin;

    private final long last_ipv4;

    public LightLoginDbRow(@NotNull String uuid,
                           @NotNull String passwordHash,
                           @NotNull String passwordSalt,
                           @Nullable String email,
                           long lastLogin,
                           long last_ipv4) {
        this.uuid = uuid;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
        this.email = email;
        this.lastLogin = lastLogin;
        this.last_ipv4 = last_ipv4;
    }

    @NotNull
    public String getUuid() {
        return uuid;
    }

    @NotNull
    public String getPasswordHash() {
        return passwordHash;
    }

    @NotNull
    public String getPasswordSalt() {
        return passwordSalt;
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public long getLastIpv4() {
        return last_ipv4;
    }

    @Override
    public String toString() {
        return "{" + uuid + ", " + passwordHash + ", " + passwordSalt + ", " +
                (email != null ? email + ", " : "") + lastLogin + ", " + last_ipv4 + "}";
    }
}
