package top.cmarco.lightlogin.listeners;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import top.cmarco.lightlogin.LightLoginPlugin;

import java.util.HashSet;
import java.util.UUID;

public final class AuthorisedEventsManager {

    private final HashSet<UUID> authorisedPlayers = new HashSet<>(1);
    private final StandardEventBus eventBus;
    private final LightLoginPlugin plugin;

    public AuthorisedEventsManager(@NotNull LightLoginPlugin plugin) {
        this.eventBus = new StandardEventBus(plugin);
        this.plugin = plugin;
    }

    public void registerBlockEvent() {
        for (final BlockedEvents blockedEvent : BlockedEvents.values()) {

            this.eventBus.subscribe(blockedEvent.getCancellableEventClass(), (event -> {

                UUID playerUUID = event.getPlayer().getUniqueId();
                if (!this.authorisedPlayers.contains(playerUUID)) {
                    event.setCancelled(true);
                }

            }));

        }
    }
}
