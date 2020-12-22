package lootcrate.commands.subs;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableMap;

import lootcrate.LootCrate;
import lootcrate.objects.Crate;
import lootcrate.objects.CrateItem;
import lootcrate.objects.CrateKey;
import lootcrate.other.Message;
import lootcrate.other.Permission;
import lootcrate.utils.CommandUtils;
import lootcrate.utils.interfaces.SubCommand;

public class SubCommandLootCrateCommand implements SubCommand
{
    private String[] args;
    private CommandSender sender;
    private LootCrate plugin;
    
    public SubCommandLootCrateCommand(LootCrate plugin, CommandSender sender, String[] args)
    {
	this.plugin = plugin;
	this.sender = sender;
	this.args = args;
	runSubCommand();
    }

    @Override
    public void runSubCommand()
    {
	Player p = (Player) sender;
	
	if (args.length <= 3)
	{
	    plugin.messageManager.sendMessage(sender, Message.LOOTCRATE_COMMAND_COMMAND_USAGE, null);
	    return;
	}
	
	if (!p.hasPermission(Permission.COMMAND_LOOTCRATE_COMMAND.getKey()))
	{
	    plugin.messageManager.sendMessage(sender, Message.NO_PERMISSION_COMMAND, null);
	    return;
	}

	if (CommandUtils.tryParse(args[1]) == null || CommandUtils.tryParse(args[2]) == null)
	{
	    plugin.messageManager.sendMessage(sender, Message.LOOTCRATE_COMMAND_COMMAND_USAGE, null);
	    return;
	}
	
	Crate crate = plugin.crateManager.getCrateById(CommandUtils.tryParse(args[1]));
	if (crate == null)
	{
	    plugin.messageManager.sendMessage(sender, Message.LOOTCRATE_NOT_FOUND,
		    ImmutableMap.of("id", "" + CommandUtils.tryParse(args[1])));
	    return;
	}
	
	CrateItem item = plugin.crateManager.getCrateItemById(crate, Integer.parseInt(args[2]));
	String command = CommandUtils.builder(args, 3);
	item.getCommands().add(command);
	crate.replaceItem(item);
	plugin.crateManager.save(crate);
	
	plugin.crateManager.save(crate);
	plugin.messageManager.sendMessage(sender, Message.LOOTCRATE_COMMAND_COMMAND_SUCCESS,
		ImmutableMap.of("id", "" + crate.getId(), "name", crate.getName(), "ItemId", "" + item.getId()));

	plugin.messageManager.crateNotification(crate, sender);
    }
    
    
}
