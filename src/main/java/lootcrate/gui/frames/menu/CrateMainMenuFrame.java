package lootcrate.gui.frames.menu;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import lootcrate.LootCrate;
import lootcrate.gui.events.custom.GUIItemClickEvent;
import lootcrate.gui.frames.types.ExtendedFrame;
import lootcrate.gui.frames.types.Frame;
import lootcrate.gui.items.GUIItem;
import lootcrate.objects.Crate;
import net.md_5.bungee.api.ChatColor;

public class CrateMainMenuFrame extends ExtendedFrame implements Listener
{

    private LootCrate plugin;
    private List<Crate> crates;

    public CrateMainMenuFrame(LootCrate plugin, Player p)
    {
	super(plugin, p, ChatColor.GOLD + "Main Menu");

	this.plugin = plugin;
	crates = plugin.cacheManager.getCache();

	generateFrame();
	registerItems();
	registerFrame();
    }

    @Override
    public void generateFrame()
    {
	fillBackground(Material.WHITE_STAINED_GLASS_PANE);
	fillCrates();
    }
    
    @Override
    public void unregisterFrame()
    {
	GUIItemClickEvent.getHandlerList().unregister(this);
    }

    // methods

    public void fillBackground(Material m)
    {
	for (int i = 0; i < getInventory().getSize(); i++)
	{
	    this.setItem(i, new GUIItem(i,m));
	}
    }

    public void fillCrates()
    {
	for (int i = 0; i < crates.size(); i++)
	{
	    this.setItem(i,
		    new GUIItem(i,Material.CHEST, crates.get(i).getName(), ChatColor.GRAY + "" + crates.get(i).getId()));
	}
    }

    // events


    @EventHandler
    public void onGUIItemClick(GUIItemClickEvent e)
    {
	if(!e.sameFrame(this)) return;
	
	Player p = e.getPlayer();
	ItemStack item = e.getItem().getItemStack();

	if (item.getType() != Material.CHEST)
	    return;

	String idFromLore = ChatColor.stripColor(item.getItemMeta().getLore().get(0));
	Crate crate = plugin.cacheManager.getCrateById(Integer.parseInt(idFromLore));

	CrateFrame crateFrame = new CrateFrame(plugin, p, crate);
	this.close();
	crateFrame.open();
    }

}