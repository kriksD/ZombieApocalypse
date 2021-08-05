package kriks.zombieapocalypse;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ZombieBreakBlock implements Runnable {

    ZombieApocalypse plugin;

    ZombieBreakBlock(ZombieApocalypse plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (!plugin.properties.isZombieCanBreakBlocks())
            return;

        for (Entity zombie : getAllZombiesFromWorlds()) {
            if (isPlayerNearbyByEntity(zombie)) {

                Block frontBlock = getBlockFacing(zombie);
                Block upFrontBlock = getUpBlockFacing(zombie);
                Block upUpFrontBlock = getUpUpBlockFacing(zombie);
                int frontBlockIndex = getIndexOfBlock(frontBlock);
                int upFrontBlockIndex = getIndexOfBlock(upFrontBlock);
                int upUpFrontBlockIndex = getIndexOfBlock(upUpFrontBlock);

                if (isChanceWorked(upUpFrontBlockIndex, zombie)) {
                    breakBlockByZombie(upUpFrontBlock);

                } else if (isChanceWorked(upFrontBlockIndex, zombie)) {
                    breakBlockByZombie(upFrontBlock);

                } else if (isChanceWorked(frontBlockIndex, zombie) && upFrontBlock.getType() != Material.AIR) {
                    breakBlockByZombie(frontBlock);
                }
            }
        }
    }

    private List<Zombie> getAllZombiesFromWorlds() {
        List<Zombie> zombies = new ArrayList<>();
        for (String worldName : plugin.properties.getWorkInWorlds()) {
            for (Entity entity : Objects.requireNonNull(Bukkit.getWorld(worldName)).getEntities()) {
                if (entity instanceof Zombie) {
                    zombies.add((Zombie) entity);
                }
            }
        }
        return zombies;
    }

    private boolean isPlayerNearbyByEntity(Entity entity) {
        for (Entity e : entity.getNearbyEntities(8, 8, 8)) {
            if (e.getType() == EntityType.PLAYER) {
                return true;
            }
        }
        return false;
    }

    private Block getBlockFacing(Entity entity) {
        return entity.getLocation().getBlock().getRelative(entity.getFacing(), 1);
    }

    private Block getUpBlockFacing(Entity entity) {
        Block FBlock = entity.getLocation().getBlock().getRelative(entity.getFacing(), 1);
        return entity.getWorld().getBlockAt(FBlock.getX(), FBlock.getY() + 1, FBlock.getZ());
    }

    private Block getUpUpBlockFacing(Entity entity) {
        Block FBlock = entity.getLocation().getBlock().getRelative(entity.getFacing(), 1);
        return entity.getWorld().getBlockAt(FBlock.getX(), FBlock.getY() + 2, FBlock.getZ());
    }

    private int getIndexOfBlock(Block block) {
        return plugin.properties.getBlocksNameToBreak().indexOf(block.getType().toString());
    }

    private boolean isChanceWorked(int index, Entity forEntity) {
        if (plugin.properties.getBlockChanceToBreakByIndex(index) == 0) {
            return false;
        }
        return new Random().nextInt(forEntity.getEntityId()) % plugin.properties.getBlockChanceToBreakByIndex(index) != 0;
    }

    private void breakBlockByZombie(Block brokenBlock) {
        brokenBlock.setType(Material.AIR);
        brokenBlock.getWorld().playSound(brokenBlock.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 0.5F, 1.0F);
    }
}