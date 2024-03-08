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
