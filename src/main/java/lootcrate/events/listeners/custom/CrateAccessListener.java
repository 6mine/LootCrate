package lootcrate.events.listeners.custom;

import com.google.common.collect.ImmutableMap;
import lootcrate.LootCrate;
import lootcrate.enums.Message;
import lootcrate.enums.Placeholder;
import lootcrate.events.custom.CrateAccessEvent;
import lootcrate.events.custom.CrateOpenEvent;
import lootcrate.events.custom.CrateViewEvent;
import lootcrate.objects.Crate;
import lootcrate.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;

public class CrateAccessListener implements Listener {
    private final LootCrate plugin;

    public CrateAccessListener(LootCrate plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAccess(CrateAccessEvent e) {
        Player p = e.getPlayer();
        Crate crate = e.getCrate();
        Action action = e.getAction();

        // if player has permission to interact with the crate
        if (!CommandUtils.hasCratePermission(crate, p)) {
            plugin.getMessageManager().sendMessage(p, Message.NO_PERMISSION_LOOTCRATE_INTERACT,
                    ImmutableMap.of(Placeholder.CRATE_NAME, crate.getName()));
            return;
        }

        // if left click + shift, call open event
        if (action == Action.LEFT_CLICK_BLOCK && p.isSneaking()) {
            CrateViewEvent event = new CrateViewEvent(crate, p, e.getLocation());
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled())
                e.setCancelled(true);
        }

        // if it was left click, check if key
        if (action == Action.LEFT_CLICK_BLOCK) {
            CrateOpenEvent event = new CrateOpenEvent(crate, p, e.getLocation());
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled())
                e.setCancelled(true);
        }

    }

}
