package lootcrate.gui.frames.menu.option;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import lootcrate.LootCrate;
import lootcrate.gui.events.custom.GUIItemClickEvent;
import lootcrate.gui.frames.menu.CrateItemFrame;
import lootcrate.gui.frames.menu.CrateKeyFrame;
import lootcrate.gui.frames.menu.CrateLocationFrame;
import lootcrate.gui.frames.types.BasicFrame;
import lootcrate.gui.frames.types.Frame;
import lootcrate.gui.items.GUIItem;
import lootcrate.objects.Crate;
import net.md_5.bungee.api.ChatColor;

public class CrateOptionMainMenuFrame extends BasicFrame implements Listener
{

    private LootCrate plugin;
    private Crate crate;

    public CrateOptionMainMenuFrame(LootCrate plugin, Player p, Crate crate)
    {
	super(plugin, p, crate.getName());

	this.plugin = plugin;
	this.crate = crate;

	registerFrame();
	generateFrame();
	registerItems();
    }

    @Override
    public void generateFrame()
    {
	fillBackground(Material.WHITE_STAINED_GLASS_PANE);
	fillOptions();
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
	    this.setItem(i, new GUIItem(i, m));
	}
    }

    public void fillOptions()
    {
	this.setItem(10, new GUIItem(10, Material.NAME_TAG, ChatColor.RED + "Name",
		ChatColor.GRAY + "Change the crate name.", "", ChatColor.DARK_RED + "Coming soon..."));
	this.setItem(13, new GUIItem(13, Material.STICK, ChatColor.RED + "Knockback Level",
		ChatColor.GRAY + "Change how far the crate knocks you back."));
	this.setItem(16, new GUIItem(16, Material.JUKEBOX, ChatColor.RED + "Open Sound",
		ChatColor.GRAY + "Change the sound that plays when a crate is opened.", "", ChatColor.DARK_RED + "Coming soon..."));
	this.setItem(30, new GUIItem(30, Material.BLAZE_POWDER, ChatColor.RED + "Animation Style",
		ChatColor.GRAY + "Change the open animation.", "", ChatColor.DARK_RED + "Coming soon..."));
	this.setItem(32, new GUIItem(32, Material.COMMAND_BLOCK, ChatColor.RED + "Open Message",
		ChatColor.GRAY + "Change the message player recieves upon crate opening.", "", ChatColor.DARK_RED + "Coming soon..."));
    }

    // events

    @EventHandler
    public void onGUIItemClick(GUIItemClickEvent e)
    {
	if (!e.sameFrame(this))
	    return;

	Player p = e.getPlayer();
	ItemStack item = e.getItem().getItemStack();

	Frame frameToOpen = null;

	switch (item.getType())
	{
	case STICK:
	    frameToOpen = new CrateOptionKnockBackFrame(plugin, p, crate);
	    break;
	default:
	    return;
	}

	this.close();
	frameToOpen.open();
    }

}