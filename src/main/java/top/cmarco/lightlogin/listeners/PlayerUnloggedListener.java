package top.cmarco.lightlogin.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.*;
import org.jetbrains.annotations.NotNull;
import top.cmarco.lightlogin.LightLoginPlugin;
import top.cmarco.lightlogin.data.AuthenticationManager;

import java.util.List;
import java.util.function.Supplier;

@RequiredArgsConstructor
public final class PlayerUnloggedListener implements Listener {

    private final LightLoginPlugin plugin;

    private <K extends Cancellable> void cancelIfPlayerUnauthenticated(@NotNull Player player, @NotNull K cancellable) {
        final AuthenticationManager authManager = plugin.getAuthenticationManager();

        if (!authManager.isAuthenticated(player)) {
            cancellable.setCancelled(true);
        }
    }

    private <K extends Event & Cancellable> void cancelIfUnauthenticated(@NotNull K cancellableEntityEvent,
                                                                         @NotNull Supplier<Entity> playerSupplier) {
        final Entity entity = playerSupplier.get();
        if (entity instanceof final Player player) {
            this.cancelIfPlayerUnauthenticated(player, cancellableEntityEvent);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        final String message = event.getMessage();
        final List<String> allowedCommands = this.plugin.getLightConfiguration().getAllowedCommands();
        final boolean isAllowed = allowedCommands.stream().anyMatch(message::startsWith);
        if (!isAllowed) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVelocity(PlayerVelocityEvent event) {
        this.cancelIfPlayerUnauthenticated(event.getPlayer(), event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent event) {
        this.cancelIfPlayerUnauthenticated(event.getPlayer(), event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        this.cancelIfPlayerUnauthenticated(event.getPlayer(), event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {
        this.cancelIfPlayerUnauthenticated(event.getPlayer(), event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrop(PlayerDropItemEvent event) {
        this.cancelIfPlayerUnauthenticated(event.getPlayer(), event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onArrowPickup(PlayerPickupArrowEvent event) {
        this.cancelIfPlayerUnauthenticated(event.getPlayer(), event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemPickup(EntityPickupItemEvent event) {
        this.cancelIfUnauthenticated(event, event::getEntity);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        this.cancelIfUnauthenticated(event, event::getDamager);
        this.cancelIfUnauthenticated(event, event::getEntity);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBow(EntityShootBowEvent event) {
        this.cancelIfUnauthenticated(event, event::getEntity);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHealthGain(EntityRegainHealthEvent event) {
        this.cancelIfUnauthenticated(event, event::getEntity);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        this.cancelIfUnauthenticated(event, event::getPlayer);
    }

}
