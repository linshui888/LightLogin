package top.cmarco.lightlogin.listeners;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * @author ImIllusion
 */
public interface EventBus {

    /**
     * Registers an event handler for the specified event class
     * @param eventClass The event class
     * @param handler The event handler
     * @param <T> The event type
     */
    <T extends PlayerEvent & Cancellable> void subscribe(@NotNull Class<T> eventClass, @NotNull Consumer<T> handler);

    /**
     * Unregisters all associated handlers
     */
    void dispose();
}