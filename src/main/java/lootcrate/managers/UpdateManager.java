package lootcrate.managers;

import lootcrate.LootCrate;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class UpdateManager implements Manager {
    private final OptionManager optionManager;
    private final LootCrate plugin;
    private int project = 0;
    private URL checkURL;
    private String newVersion = "";

    /**
     * Constructor for CrateManager
     *
     * @param plugin An instance of the plugin
     */
    public UpdateManager(LootCrate plugin) {
        this.plugin = plugin;
        this.optionManager = plugin.getOptionManager();
        this.newVersion = plugin.getDescription().getVersion();
        this.project = 87046;
        try {
            this.checkURL = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + getProjectID());
        } catch (MalformedURLException e) {
        }
    }

    public int getProjectID() {
        return project;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public String getResourceURL() {
        return "https://www.spigotmc.org/resources/" + project;
    }

    public String getNewVersion() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                URLConnection con;
                try {
                    con = checkURL.openConnection();
                    newVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        if (newVersion != null)
            return newVersion;
        return "None";
    }

    public boolean checkForUpdates() {
        URLConnection con = null;
        try {
            con = checkURL.openConnection();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            this.newVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Integer.valueOf(plugin.getDescription().getVersion().replace(".", "")) < Integer
                .valueOf(newVersion.replace(".", ""));
        // return !plugin.getDescription().getVersion().equals(newVersion);
    }

    public void sendNotificationCommandSender(CommandSender p) {
        p.sendMessage(ChatColor.YELLOW + "Running " + plugin.getName() + " v" + plugin.getDescription().getVersion());
        if (checkForUpdates()) {
            p.sendMessage(ChatColor.RED + "New update found! (v" + getNewVersion() + ").");
            p.sendMessage(ChatColor.RED + "Download @ " + getResourceURL());
        }
    }

    public void sendNotificationPlayer(Player p) {
        p.sendMessage(ChatColor.YELLOW + "Running " + plugin.getName() + " v" + plugin.getDescription().getVersion());
        if (checkForUpdates()) {
            p.sendMessage(ChatColor.RED + "New update found! (v" + getNewVersion() + ").");
            p.sendMessage(ChatColor.RED + "Download @ " + getResourceURL());
        }
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }
}
