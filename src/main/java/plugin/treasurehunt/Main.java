package plugin.treasurehunt;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.treasurehunt.command.TreasureHuntCommand;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        TreasureHuntCommand treasureHuntCommand = new TreasureHuntCommand(this);
        Bukkit.getPluginManager().registerEvents(treasureHuntCommand, this);
        getCommand("treasureHuntCommand").setExecutor(treasureHuntCommand);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
