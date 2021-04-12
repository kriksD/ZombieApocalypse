package kriks.zombieapocalypse;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ZombieApocalypse extends JavaPlugin {

    PropertiesOfPlugin properties = new PropertiesOfPlugin(this);

    @Override
    public void onEnable() {
        getLogger().info(getConfig().getString("message.OnEnable"));
        Bukkit.getPluginManager().registerEvents(new ZombieSpawn(this), this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new ZombieBreakBlock(this), 0, 100);
    }

    @Override
    public void onDisable() {
        getLogger().info(getConfig().getString("message.OnDisable"));
    }
}
