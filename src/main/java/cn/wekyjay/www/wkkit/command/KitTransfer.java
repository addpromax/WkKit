package cn.wekyjay.www.wkkit.command;

import cn.wekyjay.www.wkkit.WkKit;
import cn.wekyjay.www.wkkit.config.LangConfigLoader;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Iterator;
import java.util.Set;

public class KitTransfer {
	public void onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length == 2 && WkKit.getWkKit().getConfig().getString("MySQL.Enable").equalsIgnoreCase("true")) {
			// YAML转MYSQL
			if(args[0].equalsIgnoreCase("transfer") && args[1].equalsIgnoreCase("mysql")) {
				Set<String> playerlist = WkKit.playerConfig.getKeys(false);
				Iterator<String> it = playerlist.iterator();// 遍历所有玩家
				while(it.hasNext()) {
					String playername = it.next().toString();
					ConfigurationSection cs = WkKit.playerConfig.getConfigurationSection(playername);
					for(String kitname : cs.getKeys(false)) {
						String data = cs.getString(kitname + ".data");
						int time = cs.getInt(playername + ".time");
						// 存在礼包就更新数据
						if(WkKit.getPlayerData().contain_Kit(playername, kitname)) {
							WkKit.getPlayerData().setKitTime(playername, kitname, time);
							WkKit.getPlayerData().setKitData(playername, kitname, data);
						}else {//不存在就添加数据
							WkKit.getPlayerData().setKitToFile(playername, kitname, data, time);
						}
					}
				}
				// 邮件数据迁移已废弃，邮件系统由 SweetMail 接管
				sender.sendMessage(LangConfigLoader.getStringWithPrefix("KIT_TRANSFER_SUCCESS", ChatColor.GREEN));
			}
		}else if(!WkKit.getWkKit().getConfig().getString("MySQL.Enable").equalsIgnoreCase("true")) {
			sender.sendMessage(LangConfigLoader.getStringWithPrefix("MYSQL_NOENABLE", ChatColor.RED));
		}
	}
}
