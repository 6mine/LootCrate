package lootcrate.managers;

import java.util.HashMap;

import org.bukkit.entity.Player;

import lootcrate.LootCrate;
import lootcrate.enums.ChatState;
import net.md_5.bungee.api.ChatColor;

public class ChatManager
{
    private LootCrate plugin;
    private HashMap<Player, ChatState> map;

    /**
     * Constructor of ChatManager
     * 
     * @param plugin
     *            Instance of plugin
     */
    public ChatManager(LootCrate plugin)
    {
	this.plugin = plugin;
	map = new HashMap<Player, ChatState>();
    }

    public void addPlayer(Player p, ChatState state)
    {
	map.put(p, state);
    }

    public void removePlayer(Player p)
    {
	if (map.containsKey(p))
	    map.remove(p);
    }

    public ChatState getState(Player p)
    {
	if (map.containsKey(p))
	    return map.get(p);
	return null;
    }

    public boolean hasState(Player p)
    {
	return map.containsKey(p);
    }

    public void sendNotification(Player p)
    {
	if (!hasState(p))
	    return;

	switch (getState(p))
	{
	case CHANGE_CRATE_NAME:
	    p.sendMessage(ChatColor.GOLD + "Enter the new name for your crate.");
	    break;
	case CHANGE_CRATE_MESSAGE:
	    p.sendMessage(ChatColor.GOLD + "Enter the new open message for your crate.");
	    break;
	case CHANGE_CRATE_SOUND:
	    p.sendMessage(ChatColor.GOLD + "Enter the new open sound for your crate. " + ChatColor.RED
		    + "Options: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Sound.html");
	    break;
	default:
	    break;

	}
    }

}