package plugin.treasurehunt.command;

import java.util.SplittableRandom;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import plugin.treasurehunt.Main;


/**
 * 宝探しゲームの開始コマンド
 * 結果はプレイヤー名、点数、日時などで表示されます
 */

public class TreasureHuntCommand extends BaseCommand implements CommandExecutor, Listener{
  private Main main;

  public TreasureHuntCommand(Main main){
    this.main = main;
  }

  @Override
  public boolean onExecutePlayerCommand(Player player, Command command, String label,
      String[] args) {
    Location chestLocation = getChestSpawnLocation(player);
    chestLocation.getBlock().setType(Material.CHEST);
    return false;
  }

  @Override
  public boolean onExecuteNPCCommand(CommandSender sender, Command command, String label,
      String[] args) {
    return false;
  }

  /**
   * チェストの出現場所を取得します。出現エリアはX軸とZ軸は自分の位置からプラス、ランダムで-10〜9の値が設定されます。 Y軸はプレイヤーと同じ位置になります。
   *
   * @param player コマンドを実行したプレイヤー
   * @return チェストの出現場所
   */
  private Location getChestSpawnLocation(Player player) {
    Location playerLocation = player.getLocation();
    int randomX = new SplittableRandom().nextInt(20) - 10;
    int randomZ = new SplittableRandom().nextInt(20) - 10;

    double x = playerLocation.getX() + randomX;
    double y = playerLocation.getY();
    double z = playerLocation.getZ() + randomZ;

    return new Location(player.getWorld(), x, y, z);
  }
}

