package top.cmarco.lightlogin.api;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public final class PlayerUnauthenticateEvent extends PlayerEvent {

    private final AuthenticationCause authenticationCause;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public PlayerUnauthenticateEvent(@NotNull Player who, @NotNull AuthenticationCause authenticationCause) {
        super(who);
        this.authenticationCause = authenticationCause;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @NotNull @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    @NotNull
    public AuthenticationCause getAuthenticationCause() {
        return authenticationCause;
    }
}