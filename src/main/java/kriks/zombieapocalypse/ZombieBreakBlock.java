package kriks.zombieapocalypse;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ZombieBreakBlock implements Runnable{

    ZombieApocalypse plugin;

    ZombieBreakBlock(ZombieApocalypse plugin){
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (!plugin.properties.isZombieCanBreakBlocks())
            return;

        List<Entity> zombies = getAllZombiesFromWorlds();

        for (Entity zombie : zombies){
            if (zombie.getType() == EntityType.ZOMBIE && isPlayerNearbyByEntity(zombie)){
                Block frontBlock = getBlockFacing(zombie);
                Block upFrontBlock = getUpBlockFacing(zombie);
                int frontBlockIndex = getIndexBlock(frontBlock);
                int upFrontBlockIndex = getIndexBlock(upFrontBlock);

                if(isChanceWorked(upFrontBlockIndex, zombie)){
                    breakBlockByZombie(upFrontBlock);
                } else if (isChanceWorked(frontBlockIndex, zombie)){
                    breakBlockByZombie(frontBlock);
                }
            }
        }
    }

    private List<Entity> getAllZombiesFromWorlds(){
        List<Entity> zombies = new ArrayList<>();
        for (String worldName : plugin.properties.getWorkInWorlds()){
            zombies.addAll(Objects.requireNonNull(Objects.requireNonNull(Bukkit.getWorld(worldName)).getEntities()));
        }
        return zombies;
    }

    private boolean isPlayerNearbyByEntity(Entity entity){
        for (Entity e : entity.getNearbyEntities(8, 8, 8)){
            if (e.getType() == EntityType.PLAYER){
                return true;
            }
        }
        return false;
    }

    private  Block getBlockFacing(Entity entity){
        return entity.getLocation().getBlock().getRelative(entity.getFacing(), 1);
    }

    private  Block getUpBlockFacing(Entity entity){
        Block FBlock = entity.getLocation().getBlock().getRelative(entity.getFacing(), 1);
        return entity.getWorld().getBlockAt(FBlock.getX(), FBlock.getY() + 1, FBlock.getZ());
    }

    private int getIndexBlock(Block block){
        return plugin.properties.getBlocksNameToBreak().indexOf(block.getType().toString());
    }

    private boolean isChanceWorked(int index, Entity forEntity){
        if (plugin.properties.getFromBlocksChanceToBreakByIndex(index) == 0){
            return false;
        }
        return new Random().nextInt(forEntity.getEntityId()) % plugin.properties.getFromBlocksChanceToBreakByIndex(index) != 0;
    }

    private void breakBlockByZombie(Block brokenBlock){
        brokenBlock.setType(Material.AIR);
        brokenBlock.getWorld().playSound(brokenBlock.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 0.5F, 1.0F);
    }
}