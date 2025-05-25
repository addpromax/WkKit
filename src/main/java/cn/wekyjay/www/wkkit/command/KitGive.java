package cn.wekyjay.www.wkkit.command;

import cn.wekyjay.www.wkkit.WkKit;
import cn.wekyjay.www.wkkit.api.PlayersReceiveKitEvent;
import cn.wekyjay.www.wkkit.api.ReceiveType;
import cn.wekyjay.www.wkkit.config.LangConfigLoader;
import cn.wekyjay.www.wkkit.kit.Kit;
import cn.wekyjay.www.wkkit.kit.KitGetter;
import cn.wekyjay.www.wkkit.tool.WKTool;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Arrays;
import java.util.Objects;

public class KitGive {
	static WkKit wk = WkKit.getWkKit();// 调用主类实例		

	public Boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length < 3) {
			sender.sendMessage(LangConfigLoader.getStringWithPrefix("Commands.give", ChatColor.GREEN));
			return true;
		}
		Player p = null;
		String kitName = args[1];
		Kit kit = Kit.getKit(kitName);
		// 判断礼包是否存在
		if(kit == null) {
			sender.sendMessage(LangConfigLoader.getStringWithPrefix("KIT_CREATE_NONEXIST", ChatColor.RED));
			return true;
		}
		if(args[2].equalsIgnoreCase("@Me")) {
			p = (Player)sender;
		} else {
			// 寻找玩家
			String player_name = args[2];
			OfflinePlayer result_player = Arrays.stream(Bukkit.getOfflinePlayers())
					.filter(offlinePlayer -> Objects.equals(offlinePlayer.getName(), player_name))
					.findFirst().orElse(null);
			// 判断玩家是否存在
			if(result_player == null) {
				sender.sendMessage(LangConfigLoader.getStringWithPrefix("NO_PLAYER", ChatColor.RED));
				return true;
			}
			// 玩家不在线则发送至邮箱
			if (!result_player.isOnline()){
				sendToMail(player_name,kit);
				// 提示发送成功
				sender.sendMessage(LangConfigLoader.getStringWithPrefix("KIT_SEND_ALL", ChatColor.GREEN));
				return true;
			}else {
				p = result_player.getPlayer();
			}

		}

		this.ExcutionMode(sender,p, kit, args.length>=4?args[3]:"1");
		return true;
		
	}
	public void ExcutionMode(CommandSender sender,Player player, Kit kit, String mode) {
		PlayerInventory pinv = player.getInventory();//使用封装类的getplayer方法获取玩家背包
		ItemStack[] getItemList = kit.getItemStacks();//获取Kits.Item的list集合


		switch(mode) {
			case "2":
				if(!WKTool.hasSpace(player, kit)) {//判断是否有足够的背包空间
					sender.sendMessage(LangConfigLoader.getStringWithPrefix("KIT_GIVE_FAILED",ChatColor.RED));
					return;
				}
				if(PlayersReceiveKitEvent.callEvent(player, kit, ReceiveType.GIVE).isCancelled()) return;// 回调事件
				WKTool.addItem(player,getItemList);
				// 执行指令
				if(kit.getCommands() != null) new KitGetter().runCommands(kit, player);
				break;
			case "3":
				if(!WKTool.hasSpace(player, 1)) {//判断是否有足够的背包空间
					sendToMail(player.getName(),kit);
					sender.sendMessage(LangConfigLoader.getStringWithPrefix("KIT_GIVE_SUCCESS",ChatColor.GREEN));
					return;
				}
				if(PlayersReceiveKitEvent.callEvent(player, kit, ReceiveType.GIVE).isCancelled()) return;// 回调事件
				pinv.addItem(kit.getKitItem());
				break;
			case "4":
				if(!WKTool.hasSpace(player, kit)) {//判断是否有足够的背包空间
					sendToMail(player.getName(),kit);
					sender.sendMessage(LangConfigLoader.getStringWithPrefix("KIT_GIVE_SUCCESS",ChatColor.GREEN));
					return;
				}
				if(PlayersReceiveKitEvent.callEvent(player, kit, ReceiveType.GIVE).isCancelled()) return;// 回调事件
				WKTool.addItem(player,getItemList);
				// 执行指令
				if(kit.getMythicMobs() != null) new KitGetter().runMythicMobs(kit,player);
				break;
			default:
				if(!WKTool.hasSpace(player, kit)) {//判断是否有足够的背包空间
					sendToMail(player.getName(),kit);
					sender.sendMessage(LangConfigLoader.getStringWithPrefix("KIT_GIVE_SUCCESS",ChatColor.GREEN));
					return;
				}
				if(PlayersReceiveKitEvent.callEvent(player, kit, ReceiveType.GIVE).isCancelled()) return;// 回调事件

				/*
				 *  1.3.0 取消物品堆叠 解决堆叠过多产生bug
				 */
				WKTool.addItem(player,getItemList);
		}
	    // 发送消息提示
		sender.sendMessage(LangConfigLoader.getStringWithPrefix("KIT_GIVE_SUCCESS",ChatColor.GREEN));//输出发送成功
	}

	/**
	 * 	发送礼包至邮箱
	 * @param player_name
	 * @param kit
	 */
	private void sendToMail(String player_name,Kit kit){
		if(WkKit.getPlayerData().contain_Mail(player_name,kit.getKitname())) {
			int num = WkKit.getPlayerData().getMailKitNum(player_name, kit.getKitname());
			WkKit.getPlayerData().setMailNum(player_name, kit.getKitname(), num + 1);
		}else {
			WkKit.getPlayerData().setMailNum(player_name, kit.getKitname(), 1);
		}
	}
}
