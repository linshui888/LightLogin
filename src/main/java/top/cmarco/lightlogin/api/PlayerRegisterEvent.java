package top.cmarco.lightlogin.api;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerRegisterEvent extends PlayerEvent {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public PlayerRegisterEvent(@NotNull Player who) {
        super(who);
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @NotNull @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
}
