package lootcrate.events.listeners;

import lootcrate.LootCrate;
import lootcrate.enums.ChatState;
import lootcrate.enums.CrateOptionType;
import lootcrate.gui.frames.creation.items.CrateItemCreationCommandsFrame;
import lootcrate.gui.frames.menu.CrateFrame;
import lootcrate.objects.Crate;
import lootcrate.objects.CrateOption;
import lootcrate.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

    private final LootCrate plugin;

    public PlayerChatListener(LootCrate plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        if (!plugin.getChatManager().hasState(p))
            return;

        ChatState state = plugin.getChatManager().getState(p);
        Crate crate = state.getCrate();
        Crate finalCrate;

        switch (state) {
            case CHANGE_CRATE_NAME:
                crate.setName(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
                sendConfirmation(p, ChatColor.GOLD + "Crate name has been set to " + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', e.getMessage()));
                break;
            case CHANGE_CRATE_MESSAGE:
                crate.setOption(new CrateOption(CrateOptionType.OPEN_MESSAGE,
                        ChatColor.translateAlternateColorCodes('&', e.getMessage())));
                sendConfirmation(p, ChatColor.GOLD + "Crate open message has been set to " + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', e.getMessage()));
                break;
            case CHANGE_CRATE_SOUND:
                Sound sound = Sound.valueOf(e.getMessage());
                if (sound == null)
                    return;
                crate.setOption(new CrateOption(CrateOptionType.OPEN_SOUND, sound.toString()));
                p.playSound(p.getLocation(), sound, 1, 1);
                sendConfirmation(p, ChatColor.GOLD + "Crate open sound has been set to " + ChatColor.YELLOW + sound.name());
                break;
            case CREATE_CRATE_NAME:
                crate = new Crate(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
                plugin.getCrateManager().addDefaultOptions(crate);
                plugin.getCacheManager().update(crate);
                finalCrate = crate;
                Bukkit.getScheduler().runTask(plugin, () -> {
                    plugin.getInvManager().openFrame(p, new CrateFrame(plugin, e.getPlayer(), finalCrate));
                });
                break;
            case ADD_ITEM_COMMAND:
                state.getCrateItem().getCommands().add(e.getMessage());
                crate.replaceItem(state.getCrateItem());
                plugin.getCacheManager().update(crate);
                finalCrate = crate;
                Bukkit.getScheduler().runTask(plugin, () -> {
                    plugin.getInvManager().openFrame(p, new CrateItemCreationCommandsFrame(plugin, e.getPlayer(), finalCrate, state.getCrateItem()));
                });
                break;
            case KNOCKBACK:
                double amt;
                try {
                    amt = Double.parseDouble(e.getMessage());
                } catch (NumberFormatException exception) {
                    sendConfirmation(p, ChatColor.RED + "Cannot set the cooldown to " + ChatColor.DARK_RED + e.getMessage());
                    break;
                }
                if(amt > 20d) {
                    sendConfirmation(p, ChatColor.RED + "Cannot set the cooldown that high due to possible lag issues. Max is 20.0");
                }
                crate.setOption(new CrateOption(CrateOptionType.KNOCK_BACK, amt));
                sendConfirmation(p, ChatColor.GOLD + "Crate knockback has been set to " + ChatColor.YELLOW + amt);
                break;
            case COOLDOWN:
                int amount;
                try {
                    amount = Integer.parseInt(e.getMessage());
                } catch (NumberFormatException exception) {
                    sendConfirmation(p, ChatColor.RED + "Cannot set the cooldown to " + ChatColor.DARK_RED + e.getMessage());
                    break;
                }
                crate.setOption(new CrateOption(CrateOptionType.COOLDOWN, amount));
                sendConfirmation(p, ChatColor.GOLD + "Crate cooldown has been set to " + ChatColor.YELLOW + amount);
                break;
            default:
                return;
        }

        e.setCancelled(true);
        plugin.getCacheManager().update(crate);
        plugin.getChatManager().removePlayer(p);

    }

    private void sendConfirmation(Player p, String message) {
        p.sendMessage(message);
    }

}
