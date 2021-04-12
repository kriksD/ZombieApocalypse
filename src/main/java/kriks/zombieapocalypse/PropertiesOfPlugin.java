package kriks.zombieapocalypse;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.*;

public class PropertiesOfPlugin {

    ZombieApocalypse plugin;
    private boolean OnlyZombieSpawn;
    private boolean ZombieCanBreakBlocks;
    private boolean LessZombieSpawn;
    private boolean ZombieBurn;

    private final List<String> blocksNameToBreak = new ArrayList<>();
    private final List<Integer> blocksChanceToBreak = new ArrayList<>();
    private List<String> workInWorlds;

    PropertiesOfPlugin(ZombieApocalypse plugin){
        this.plugin = plugin;
        getConfigData();
    }

    void getConfigData(){
        createDefaultConfigWhenNonExist();
        FileConfiguration config = plugin.getConfig();
        OnlyZombieSpawn = config.getBoolean("OnlyZombieSpawn");
        ZombieCanBreakBlocks = config.getBoolean("ZombieCanBreakBlocks");
        LessZombieSpawn = config.getBoolean("LessZombieSpawn");
        ZombieBurn = config.getBoolean("ZombieBurn");
        boolean skipAndSetTenForAll = config.getBoolean("SkipAndSetTenForAll");

        workInWorlds = config.getStringList("WorkInWorlds");

        if (skipAndSetTenForAll){
            setDefaultBlockToBreakData();
        } else {
            setBlockToBreakDataFromConfig();
        }
    }

    private void setDefaultBlockToBreakData(){
        for (Material materialBlock : Material.values()){
            if (materialBlock.isBlock()){
                blocksNameToBreak.add(materialBlock.name());

                if (materialBlock.name().equals("AIR") || materialBlock.name().equals("BEDROCK") ||
                        materialBlock.name().equals("LAVA") || materialBlock.name().equals("WATER")){
                    blocksChanceToBreak.add(0);
                } else {
                    blocksChanceToBreak.add(10);
                }
            }
        }
    }

    private void setBlockToBreakDataFromConfig(){
        for (String blockToBreak : plugin.getConfig().getStringList("ChanceToBreakBlock")){
            String[] blockToBreakDivided = blockToBreak.split(" ");
            blocksNameToBreak.add(blockToBreakDivided[0]);
            blocksChanceToBreak.add(Integer.parseInt(blockToBreakDivided[1]));
        }
    }

    private void createDefaultConfigWhenNonExist(){
        File configFile = new File(plugin.getDataFolder() + File.separator + "config.yml");
        if (!configFile.exists()){
            plugin.getConfig().options().copyDefaults(true);
            plugin.saveDefaultConfig();
            plugin.saveConfig();
        }
    }

    public boolean isOnlyZombieSpawn() {
        return OnlyZombieSpawn;
    }

    public boolean isZombieCanBreakBlocks() {
        return ZombieCanBreakBlocks;
    }

    public boolean isLessZombieSpawn() {
        return LessZombieSpawn;
    }

    public boolean isZombieBurn() {
        return ZombieBurn;
    }

    public String getFromBlocksNameToBreakByIndex(int index){
        return blocksNameToBreak.get(index);
    }

    public ArrayList<String> getBlocksNameToBreak(){
        return new ArrayList<>(blocksNameToBreak);
    }

    public int getFromBlocksChanceToBreakByIndex(int index){
        return blocksChanceToBreak.get(index);
    }

    public ArrayList<Integer> getBlocksChanceToBreak(){
        return new ArrayList<>(blocksChanceToBreak);
    }

    public ArrayList<String> getWorkInWorlds(){
        return new ArrayList<>(workInWorlds);
    }
}
