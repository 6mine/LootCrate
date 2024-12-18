package lootcrate.gui.items;

import lootcrate.LootCrate;
import lootcrate.enums.CustomizationOption;
import lootcrate.enums.Message;
import lootcrate.managers.CustomizationManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class NavItems
{

    private LootCrate plugin;
    private CustomizationManager customizationManager;

    public NavItems(LootCrate plugin)
    {
        this.plugin = plugin;
        this.customizationManager = plugin.getCustomizationManager();
    }

    public ItemStack getNavNext()
    {
        return new GUIItem(0, customizationManager.parseMaterial(CustomizationOption.NAVIGATION_NEXT_MATERIAL), customizationManager.parseName(CustomizationOption.NAVIGATION_NEXT_NAME)).getItemStack();
    }
    public ItemStack getNavPrev()
    {
        return new GUIItem(0, customizationManager.parseMaterial(CustomizationOption.NAVIGATION_PREVIOUS_MATERIAL), customizationManager.parseName(CustomizationOption.NAVIGATION_PREVIOUS_NAME)).getItemStack();
    }
}
