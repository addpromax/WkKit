package cn.wekyjay.www.wkkit.tool;

import cn.wekyjay.www.wkkit.WkKit;
import cn.wekyjay.www.wkkit.kit.Kit;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Calendar;

public class KitRefresh {
		
	/**
	 * 将时间值转为毫秒
	 * @param value
	 * @return
	 */
	public long changeTomilli(String value,Calendar cal) {
		String[] part = value.split("(?<=\\d)(?=\\D)");
		int num = Integer.parseInt(part[0]);
		switch(part[1]) {
			case "d": cal.add(Calendar.DATE, num);break;
			case "w": cal.add(Calendar.WEEK_OF_MONTH, num);break;
			case "m": cal.add(Calendar.MONTH, num);break;
			case "y": cal.add(Calendar.YEAR, num);break;
		}
		return num;
	}
	
	/**
	 * 立刻刷新一个礼包
	 * @param kit 礼包
	 */
	public static void refreshNow(Kit kit) {
		String kitname = kit.getKitname();
		OfflinePlayer[] playerlist = Bukkit.getOfflinePlayers();
		for(OfflinePlayer player : playerlist) {
			String playername = player.getName();
			// 有礼包数据的就刷新领取状态
			if(WkKit.getPlayerData().contain_Kit(playername, kitname)) {
				WkKit.getPlayerData().setKitData(playername, kitname, "true");
			}
		}
	}
}
