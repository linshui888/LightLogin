package top.cmarco.lightlogin.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import top.cmarco.lightlogin.LightLoginPlugin;

import java.util.function.Consumer;

/**
 * @author ImIllusion
 */
@RequiredArgsConstructor
public class StandardEventBus implements EventBus, Listener {

    private final LightLoginPlugin plugin;

    @Override
    public final <T extends PlayerEvent & Cancellable> void subscribe(@NotNull Class<T> eventClass, @NotNull Consumer<T> handler) {
        // Event Class, Listener, Event Priority, EventExecutor, Plugin, Ignore Cancelled
        Bukkit.getPluginManager().registerEvent(eventClass, this, EventPriority.HIGHEST, (listener, event) -> {
            if(eventClass.isInstance(event)) {
                handler.accept(eventClass.cast(event));
            }
        }, plugin, true);
    }

    @Override
    public final void dispose() {
        HandlerList.unregisterAll(this);
    }
}