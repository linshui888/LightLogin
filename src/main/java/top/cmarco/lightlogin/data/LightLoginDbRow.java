package top.cmarco.lightlogin.data;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record LightLoginDbRow(@NonNull String uuid,
                              @NotNull String passwordHash,
                              @NotNull String passwordSalt,
                              @Nullable String email,
                              long lastLogin,
                              long last_ipv4) {

    @Override
    public String toString() {
        return '{' + uuid + ", " + passwordHash + ", " + passwordSalt + ", " + email + ", " + lastLogin + ", " + last_ipv4 + '}';
    }
}
