package cn.wekyjay.www.wkkit.command;

import cn.wekyjay.www.wkkit.WkKit;
import cn.wekyjay.www.wkkit.config.ConfigManager;
import cn.wekyjay.www.wkkit.config.LangConfigLoader;
import cn.wekyjay.www.wkkit.hook.SweetMailHooker;
import cn.wekyjay.www.wkkit.kit.Kit;
import cn.wekyjay.www.wkkit.kit.KitGroupManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class KitGroup {
	public void onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length >= 3 && args[1].equalsIgnoreCase("create")) {
			String groupname = args[2];
			if(!KitGroupManager.contains(groupname)) {
				new KitGroupManager(args[2]);
				sender.sendMessage(LangConfigLoader.getStringWithPrefix("KIT_GROUP_CREATE_SUCCESS", ChatColor.GREEN));
				return;
			}else {
				sender.sendMessage(LangConfigLoader.getStringWithPrefix("KIT_GROUP_EXISTS", ChatColor.YELLOW));
				return;
			}

		}
		// 删除礼包组
		if(args.length >= 3 && args[1].equalsIgnoreCase("delete")) {
			String groupname = args[2];
			// 判断是否存在该礼包组
			if(!KitGroupManager.contains(groupname)) {
				sender.sendMessage(LangConfigLoader.getStringWithPrefix("KIT_GROUP_NONEXIST", ChatColor.RED));
				return;
			}
			File file = new File(WkKit.kitFile.getAbsoluteFile(),groupname + ".yml");
			if(args.length >= 4 && args[3].equalsIgnoreCase("true")) {
				List<String> kits = KitGroupManager.getGroupKits(groupname);
				if(kits != null) {
					for(String kitname : kits) {
						ConfigManager.getKitconfig().set(kitname, null);
						OfflinePlayer[] s = Bukkit.getOfflinePlayers();
						for(OfflinePlayer offp : s) {
							String pname = offp.getName();
							if(WkKit.getPlayerData().contain_Kit(pname,kitname)) {
								WkKit.getPlayerData().delKitToFile(pname, kitname);
							}
							// 邮件系统已由 SweetMail 接管
						}
						//保存配置
						try {
							ConfigManager.getKitconfig().save(file.getName());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

			}else {
				// 如果是default组必须是true
				if(groupname.equalsIgnoreCase("Default")) {
					sender.sendMessage(LangConfigLoader.getStringWithPrefix("KIT_GROUP_CANTDEFAULT", ChatColor.RED));
					return;
				}
				List<String> kits = KitGroupManager.getGroupKits(groupname);
				File defile = new File(WkKit.kitFile.getAbsolutePath(),"Default.yml");
				if(!defile.exists())new KitGroupManager("Default");
				for(String kitname : kits) {
					KitGroupManager.toGroup(Kit.getKit(kitname), "Default");
				}
			}
			file.delete();
			ConfigManager.reloadKit();
			ConfigManager.reloadMenu();
			sender.sendMessage(LangConfigLoader.getStringWithPrefix("KIT_GROUP_DELETE_SUCCESS", ChatColor.GREEN));
		}
		
		// 移动组
		if(args.length >= 4 && args[1].equalsIgnoreCase("move")) {
			KitGroupManager.toGroup(Kit.getKit(args[2]), args[3]);
			return;
		}
		
		if(args.length >= 2 && args[1].equalsIgnoreCase("list") && sender.isOp()) {
			List<String> s = KitGroupManager.getGroups();
			StringBuilder allkit = new StringBuilder();
			int num = 0;
			for(String a : s) {
				allkit.append(a + " ");
				num++;
			}
			sender.sendMessage(ChatColor.GREEN + "Groups(" + num + ")：" + allkit.toString());
		}
		// 发送一个礼包组给指定目标
		if(args.length >= 4 && args[1].equalsIgnoreCase("send") && sender.isOp()) {
			String target = args[3];
			String groupname = args[2];
			int kitnum = 1;
			if(args.length == 5) {
				kitnum = Integer.parseInt(args[4]);
			}
			List<Kit> kits = new ArrayList<>();
			for(String kitname : KitGroupManager.getGroup(groupname).getKeys(false)) {
				if(Kit.getKit(kitname)!= null) {
					kits.add(Kit.getKit(kitname));
				}
			}
			
			// 根据目标发放礼包
			//发放实体礼包给：@all
			if(target.equalsIgnoreCase("@All")) {
				if(SweetMailHooker.getInstance() == null || !SweetMailHooker.isAvailable()) {
					sender.sendMessage("§c邮件系统未启用，无法发送礼包组！请联系管理员安装 SweetMail 插件。");
					return;
				}
				OfflinePlayer[] playerlist = Bukkit.getOfflinePlayers();
				for(OfflinePlayer player : playerlist) {
					if(player.getPlayer() != null) {
						for(Kit kit : kits) {
							for(int i = 0; i < kitnum; i++) {
								SweetMailHooker.getInstance().sendKitMail(
									player.getPlayer(),
									kit,
									LangConfigLoader.getString("KIT_MAIL_TITLE"),
									LangConfigLoader.getString("KIT_SEND_PICKUP")
								);
							}
						}
					}
				}
				sender.sendMessage(LangConfigLoader.getStringWithPrefix("KIT_SEND_ALL", ChatColor.GREEN));
				return;
			}
			//发放礼包给：@online
			if(target.equalsIgnoreCase("@Online")) {
				if(SweetMailHooker.getInstance() == null || !SweetMailHooker.isAvailable()) {
					sender.sendMessage("§c邮件系统未启用，无法发送礼包组！请联系管理员安装 SweetMail 插件。");
					return;
				}
				OfflinePlayer[] playerlist = Bukkit.getOfflinePlayers();
				for(OfflinePlayer player : playerlist) {
					if(player.isOnline() && player.getPlayer() != null) {
						for(Kit kit : kits) {
							for(int i = 0; i < kitnum; i++) {
								SweetMailHooker.getInstance().sendKitMail(
									player.getPlayer(),
									kit,
									LangConfigLoader.getString("KIT_MAIL_TITLE"),
									LangConfigLoader.getString("KIT_SEND_PICKUP")
								);
							}
						}
					}
				}
				sender.sendMessage(LangConfigLoader.getStringWithPrefix("KIT_SEND_ONLINE", ChatColor.GREEN));
				return;
			}
			
			// 发放礼包给自己
			if(target.equalsIgnoreCase("@Me") && sender instanceof Player) {
				if(SweetMailHooker.getInstance() != null && SweetMailHooker.isAvailable()) {
					for(Kit kit : kits) {
						for(int i = 0; i < kitnum; i++) {
							SweetMailHooker.getInstance().sendKitMail(
								(Player)sender,
								kit,
								LangConfigLoader.getString("KIT_MAIL_TITLE"),
								LangConfigLoader.getString("KIT_SEND_PICKUP")
							);
						}
					}
					sender.sendMessage(LangConfigLoader.getStringWithPrefix("KIT_SEND_PLAYER", ChatColor.GREEN));
				} else {
					sender.sendMessage("§c邮件系统未启用，无法发送礼包组！请联系管理员安装 SweetMail 插件。");
				}
				return;
			}
			
			//发放礼包给：player
			if(!target.equalsIgnoreCase("@All") && !target.equalsIgnoreCase("@Online")) {
				String pname = target;
				Player targetPlayer = Bukkit.getPlayer(pname);
				if(SweetMailHooker.getInstance() == null || !SweetMailHooker.isAvailable()) {
					sender.sendMessage("§c邮件系统未启用，无法发送礼包组！请联系管理员安装 SweetMail 插件。");
					return;
				}
				if(targetPlayer != null) {
					for(Kit kit : kits) {
						for(int i = 0; i < kitnum; i++) {
							SweetMailHooker.getInstance().sendKitMail(
								targetPlayer,
								kit,
								LangConfigLoader.getString("KIT_MAIL_TITLE"),
								LangConfigLoader.getString("KIT_SEND_PICKUP")
							);
						}
					}
					sender.sendMessage(LangConfigLoader.getStringWithPrefix("KIT_SEND_PLAYER", ChatColor.GREEN));
				} else {
					sender.sendMessage(LangConfigLoader.getStringWithPrefix("NO_PLAYER", ChatColor.RED));
				}
			}
		}
		
	}
}
