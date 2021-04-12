package kriks.zombieapocalypse;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustEvent;

public class ZombieSpawn implements Listener {

    ZombieApocalypse plugin;

    ZombieSpawn(ZombieApocalypse plugin){
        this.plugin = plugin;
    }

    @EventHandler
    void MobSpawned(CreatureSpawnEvent event){
        Entity mob = event.getEntity();

        if (mob instanceof Monster && event.getEntity().getType() != EntityType.ZOMBIE && plugin.properties.isOnlyZombieSpawn()){
            event.setCancelled(true);
            if (!plugin.properties.isLessZombieSpawn()){
                mob.getWorld().spawnEntity(mob.getLocation(), EntityType.ZOMBIE);
            }
        }
    }

    @EventHandler
    public void onEntityCombust(EntityCombustEvent event){
        if(event.getEntity() instanceof Zombie && !plugin.properties.isZombieBurn()){
            event.setCancelled(true);
        }
    }
}
