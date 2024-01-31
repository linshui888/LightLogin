package top.cmarco.lightlogin.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.*;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

@RequiredArgsConstructor
public enum BlockedEvents {

    PLAYER_ASYNC_CHAT(AsyncPlayerChatEvent.class),
    PLAYER_ADVANCEMENT_DONE(PlayerAdvancementDoneEvent.class),
    PLAYER_ANIMATION(PlayerAnimationEvent.class),
    PLAYER_BED_ENTER(PlayerBedEnterEvent.class),
    PLAYER_BED_LEAVE(PlayerBedLeaveEvent.class),
    PLAYER_BUCKET_ENTITY(PlayerBucketEntityEvent.class),
    // PLAYER_BUCKET(PlayerBucketEvent.class),
    PLAYER_CHANGED_MAIN_HAND(PlayerChangedMainHandEvent.class),
    PLAYER_CHANGED_WORLD(PlayerChangedWorldEvent.class),
    PLAYER_CHANNEL(PlayerChannelEvent.class),
    //PLAYER_CHAT(PlayerChatEvent.class),
    //PLAYER_CHAT_TAB_COMPLETE(PlayerChatTabCompleteEvent.class),
    //PLAYER_COMMAND_PREPROCESS(PlayerCommandPreprocessEvent.class), // TODO: Command handling
    //PLAYER_COMMAND_SEND(PlayerCommandSendEvent.class),
    PLAYER_DROP_ITEM(PlayerDropItemEvent.class),
    PLAYER_EDIT_BOOK(PlayerEditBookEvent.class),
    PLAYER_EGG_THROW(PlayerEggThrowEvent.class),
    PLAYER_EXP_CHANGE(PlayerExpChangeEvent.class),
    // PLAYER_EXP_COOLDOWN_CHANGE(PlayerExpCooldownChangeEvent.class),
    PLAYER_FISH(PlayerFishEvent.class),
    PLAYER_GAME_MODE_CHANGE(PlayerGameModeChangeEvent.class),
    PLAYER_HARVEST_BLOCK(PlayerHarvestBlockEvent.class),
    // PLAYER_HIDE_ENTITY(PlayerHideEntityEvent.class),
    PLAYER_INTERACT_ENTITY(PlayerInteractEntityEvent.class),
    PLAYER_INTERACT(PlayerInteractEvent.class),
    PLAYER_ITEM_BREAK(PlayerItemBreakEvent.class),
    PLAYER_ITEM_CONSUME(PlayerItemConsumeEvent.class),
    PLAYER_ITEM_DAMAGE(PlayerItemDamageEvent.class),
    PLAYER_ITEM_HELD(PlayerItemHeldEvent.class),
    PLAYER_ITEM_MEND(PlayerItemMendEvent.class),
    PLAYER_JOIN(PlayerJoinEvent.class),
    PLAYER_KICK(PlayerKickEvent.class),
    PLAYER_LEVEL_CHANGE(PlayerLevelChangeEvent.class),
    PLAYER_LOCALE_CHANGE(PlayerLocaleChangeEvent.class),
    PLAYER_LOGIN(PlayerLoginEvent.class),
    PLAYER_MOVE(PlayerMoveEvent.class),
    //PLAYER_PICKUP_ITEM(PlayerPickupItemEvent.class),
    PLAYER_QUIT(PlayerQuitEvent.class),
    PLAYER_RECIPE_BOOK_CLICK(PlayerRecipeBookClickEvent.class),
    PLAYER_RECIPE_BOOK_SETTINGS_CHANGE(PlayerRecipeBookSettingsChangeEvent.class),
    PLAYER_RECIPE_DISCOVER(PlayerRecipeDiscoverEvent.class),
    //PLAYER_RESOURCE_PACK_STATUS(PlayerResourcePackStatusEvent.class),
    PLAYER_RESPAWN(PlayerRespawnEvent.class),
    PLAYER_RIPTIDE(PlayerRiptideEvent.class),
    PLAYER_SHEAR_ENTITY(PlayerShearEntityEvent.class),
    //PLAYER_SHOW_ENTITY(PlayerShowEntityEvent.class),
    //PLAYER_SIGN_OPEN(PlayerSignOpenEvent.class),
    //PLAYER_SPAWN_CHANGE(PlayerSpawnChangeEvent.class),
    PLAYER_SPAWN_LOCATION(PlayerSpawnLocationEvent.class),
    PLAYER_STATISTIC_INCREMENT(PlayerStatisticIncrementEvent.class),
    PLAYER_SWAP_HAND_ITEMS(PlayerSwapHandItemsEvent.class),
    PLAYER_TAKE_LECTERN_BOOK(PlayerTakeLecternBookEvent.class),
    PLAYER_TOGGLE_FLIGHT(PlayerToggleFlightEvent.class),
    PLAYER_TOGGLE_SNEAK(PlayerToggleSneakEvent.class),
    PLAYER_TOGGLE_SPRINT(PlayerToggleSprintEvent.class),
    PLAYER_VELOCITY(PlayerVelocityEvent.class)

    // Add more events as needed

    ;

    private final Class<? extends PlayerEvent> cancellableEventClass;
    public <T extends PlayerEvent & Cancellable> Class<T> getCancellableEventClass() {
        return (Class<T>) this.cancellableEventClass;
    }
}
