package plugin.treasurehunt.command;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import plugin.treasurehunt.Main;


/**
 * 宝探しゲームの開始コマンド
 * 結果はプレイヤー名、点数、日時などで表示されます
 */

public class TreasureHuntCommand extends BaseCommand implements CommandExecutor, Listener {
  private Main main;
  private int gameTime;
  private Location originalLocation;
  private List<Location> chestLocations;

  public TreasureHuntCommand(Main main) {
    this.main = main;
  }

  @Override
  public boolean onExecutePlayerCommand(Player player, Command command, String label,
      String[] args) {
    originalLocation = player.getLocation();
    playerTeleport(player);
    chestLocations = getChestSpawnLocation(player);
    startGame(player);
    return false;
  }

  @Override
  public boolean onExecuteNPCCommand(CommandSender sender, Command command, String label,
      String[] args) {
    return false;
  }

  /**
   *
   * @param player
   */
  private void playerTeleport(Player player) {
    World world = player.getWorld();
    player.teleport(new Location(player.getWorld(), -372, 51, -3770));

    world.setStorm(false);
    world.setThundering(false);
    world.setTime(1000);
  }


  /**
   * チェストの出現場所を取得します。出現エリアはX軸とZ軸は自分の位置からプラス、ランダムで-10〜9の値が設定されます。 Y軸はプレイヤーと同じ位置になります。
   *
   * @param player コマンドを実行したプレイヤー
   * @return チェストの出現場所をリストで返却
   */
  private List<Location> getChestSpawnLocation(Player player) {
    World world = player.getWorld();
    Location playerLocation = player.getLocation();
    SplittableRandom random = new SplittableRandom();
    List<Location> chestLocations = new ArrayList<>();

    for (int i = 0; i < 5; i++) {

      int randomX = random.nextInt(20) - 10;
      int randomZ = random.nextInt(20) - 10;
      double x = playerLocation.getX() + randomX;
      double y = playerLocation.getY();
      double z = playerLocation.getZ() + randomZ;

      Location chestLocation = new Location(player.getWorld(), x, y, z);
      chestLocation.getBlock().setType(Material.CHEST);

      // チェストにアイテムを追加
      Block block = chestLocation.getBlock();
      if (block.getState() instanceof Chest) {
        Chest chest = (Chest) block.getState();
        Inventory inventory = chest.getInventory();

        if (i == 0) {
          ItemStack diamond = new ItemStack(Material.DIAMOND);
          inventory.addItem(diamond);
        } else {
          ItemStack banner = new ItemStack(Material.STICK);
          inventory.addItem(banner);
        }
      } chestLocations.add(chestLocation);
    } return chestLocations;
  }

  private void startGame(Player player) {
    BossBar bossBar = Bukkit.createBossBar("残り時間", BarColor.RED, BarStyle.SOLID);
    bossBar.addPlayer(player);
    bossBar.setProgress(1.0);
    gameTime = 10;

    Bukkit.getScheduler().runTaskTimer(main, new Runnable() {
      @Override
      public void run() {
        if (gameTime <= 0) {
          this.cancel();

          player.sendTitle("ゲームが終了しました",
              "元の場所にテレポートします",
              0, 60, 0);

          player.teleport(originalLocation);
          removeChests();
          bossBar.removeAll();

          return;
        }

        gameTime -= 1; // ゲーム時間を5秒減少させる
        bossBar.setProgress(gameTime / 10.0);
      }

      private void cancel() {
        Bukkit.getScheduler().cancelTasks(main);
      }
    }, 0, 20);
  }

  private void removeChests() {
    for (Location location : chestLocations) {
      Block block = location.getBlock();
      if (block.getType() == Material.CHEST) {
        block.setType(Material.AIR);
      }
    }
    chestLocations.clear();
  }

}