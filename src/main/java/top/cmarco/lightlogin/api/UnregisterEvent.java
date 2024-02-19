package top.cmarco.lightlogin.api;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class UnregisterEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final UUID uuid;

    public UnregisterEvent(@NotNull UUID uuid) {
        this.uuid = uuid;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @NotNull @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public UUID getUuid() {
        return uuid;
    }
}
