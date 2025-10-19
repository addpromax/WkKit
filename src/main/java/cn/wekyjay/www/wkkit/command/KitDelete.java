package cn.wekyjay.www.wkkit.command;

import cn.wekyjay.www.wkkit.WkKit;
import cn.wekyjay.www.wkkit.config.ConfigManager;
import cn.wekyjay.www.wkkit.config.LangConfigLoader;
import cn.wekyjay.www.wkkit.kit.Kit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class KitDelete {
	public void onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(args.length < 2) {
			sender.sendMessage(LangConfigLoader.getStringWithPrefix("Commands.delete", ChatColor.GREEN));
			return;
		}
		
		String kitname = args[1];
		String filename = ConfigManager.getKitconfig().getContainsFilename(kitname);
		
		if(Kit.getKit(kitname) != null) {//判断是否存在该礼包
			//删除礼包
			ConfigManager.getKitconfig().set(kitname, null);
			ConfigManager.getKitconfig().getKitsList().remove(Kit.getKit(kitname));
			//删除玩家配置中的礼包记录
			OfflinePlayer[] s = Bukkit.getOfflinePlayers();
			for(OfflinePlayer offp : s) {
				String pname = offp.getName();
				if(WkKit.getPlayerData().contain_Kit(pname,kitname)) {
					WkKit.getPlayerData().delKitToFile(pname, kitname);
				}
				// 邮件系统已由 SweetMail 接管，无需删除邮件数据
			}

			//保存配置
			try {
				ConfigManager.getKitconfig().save(filename);
				sender.sendMessage(LangConfigLoader.getStringWithPrefix("KIT_DELETE_SUCCESS", ChatColor.GREEN));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}else {//礼包不存在执行的代码
			sender.sendMessage(LangConfigLoader.getStringWithPrefix("KIT_DELETE_NONEXIST", ChatColor.RED));
			return;
		}	
		
	}
}
