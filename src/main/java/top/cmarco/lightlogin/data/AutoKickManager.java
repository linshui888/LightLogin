package top.cmarco.lightlogin.data;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import top.cmarco.lightlogin.LightLoginPlugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public final class AutoKickManager {

    private final LightLoginPlugin plugin;
    private final HashMap<UUID, Long> joinedMap = new HashMap<>();
    private BukkitTask bukkitTask = null;

    public AutoKickManager(@NotNull final LightLoginPlugin plugin) {
        this.plugin = plugin;
    }

    public void startAutoKickTask() {
        if (bukkitTask != null) {
            stopAutoKickTask();
        }

        final AuthenticationManager authenticationManager = plugin.getAuthenticationManager();
        double ticks = plugin.getLightConfiguration().getKickAfterSeconds() * 1E3;
        this.bukkitTask = plugin.getServer().getScheduler().runTaskTimer(plugin, ()-> {


            final Collection<? extends Player> onlinePlayers = plugin.getServer().getOnlinePlayers();
            for (final Player onlinePlayer : onlinePlayers) {

                if (!joinedMap.containsKey(onlinePlayer.getUniqueId())) {
                    addEntered(onlinePlayer);
                    continue;
                }

                final Long lastEnter = joinedMap.get(onlinePlayer.getUniqueId());
                final boolean compareTo = (System.currentTimeMillis() - lastEnter) >= ticks;
                if (!authenticationManager.isAuthenticated(onlinePlayer) && compareTo) {
                    onlinePlayer.kickPlayer(plugin.getLightConfiguration().getLoginTookTooMuchTime());
                }

            }

        }, 0L, 20L);
    }

    public void stopAutoKickTask() {
        if (bukkitTask == null) {
            return;
        }

        bukkitTask.cancel();
        bukkitTask = null;
    }

    public void addEntered(final Player player) {
        this.joinedMap.put(player.getUniqueId(), System.currentTimeMillis());
    }
}
