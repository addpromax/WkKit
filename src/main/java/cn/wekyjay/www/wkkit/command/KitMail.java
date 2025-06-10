package cn.wekyjay.www.wkkit.command;

import cn.wekyjay.www.wkkit.WkKit;
import cn.wekyjay.www.wkkit.config.LangConfigLoader;
import cn.wekyjay.www.wkkit.invholder.MailHolder;
import cn.wekyjay.www.wkkit.kit.Kit;
import cn.wekyjay.www.wkkit.tool.WKTool;
import cn.wekyjay.www.wkkit.tool.items.GlassPane;
import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;



public class KitMail{

	static WkKit wk = WkKit.getWkKit();// 调用主类实例	

	public void onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player p = (Player) sender;
		openKitMail(p,1);
	}
	
	
	
	/**
	 * 为玩家打开礼包邮箱
	 * @param p
	 */
	public void openKitMail(Player p, int page) {
		String guiname = LangConfigLoader.getString("KITMAIL_TITLE");
		List<ItemStack> litem = new ArrayList<>();// kit list
		List<Inventory> linv = new ArrayList<>(); // inv list
		String pname = p.getName();
		List<String> kits = WkKit.getPlayerData().getMailKits(pname);
		// 如果没有就初始化值
		if(kits == null || kits.size() == 0) {
			kits = new ArrayList<>();
		}
		

		
	//添加物品到litem
		//添加礼包
		for(String kitname : kits) {
			Kit kit = Kit.getKit(kitname);
			if(kit == null) continue;
			ItemStack is = kit.getKitItem();
			// 添加名称
			NBT.modify(is, nbt -> {
				nbt.setString("wkkit", kitname);
			});
			int kitNum = WkKit.getPlayerData().getMailKitNum(pname, kitname);
			int maxsize = is.getMaxStackSize();
			if(kitNum >= 1 && kitNum <= maxsize) {
				is.setAmount(WkKit.getPlayerData().getMailKitNum(pname, kitname));
				litem.add(is);
			}else if(kitNum > maxsize){
				/**
				 * 20230630 修复邮箱超过数量不追加BUG
				 */
				int count = kitNum / maxsize;
				is.setAmount(maxsize);

				// 遍历添加
				for (int j = 0;j < count;j++){
					litem.add(is.clone());
				}
				// 取模不为0
				if (kitNum % maxsize != 0){
					is.setAmount(kitNum % maxsize);
					litem.add(is.clone());
				}
			}

		}

		//有效礼包个数
		int i = litem.size();


		//判断可创建的gui个数
		int guinum = 0;
		if(i % 36 == 0 && !(i == 0)) {
			guinum = i / 36;
		}else {
			guinum = (i / 36) + 1;
		}
			
			//创建gui到linv
			for(int i1 = 1; i1 <= guinum; i1++) {
				Inventory inv;
				if(guinum == 1) {//如果只有一页就不加页数
					inv = Bukkit.createInventory(new MailHolder(i1), 6*9, guiname);
				}else {
					String pagetitle = WKTool.replacePlaceholder("page", i1+"", LangConfigLoader.getString("GUI_PAGETITLE"));
					inv = Bukkit.createInventory(new MailHolder(i1), 6*9, guiname + " - " + pagetitle);
				}

				
				//添加物品：菜单栏
				ItemStack item_mn;
				if(wk.getConfig().getString("GUI.MenuMaterial").equalsIgnoreCase("Default")){
					item_mn = GlassPane.DEFAULT.getItemStack();
				}else {
					item_mn = new ItemStack(Material.getMaterial(wk.getConfig().getString("GUI.MenuMaterial")));
				}

				ItemMeta im = item_mn.getItemMeta();
				if (wk.getConfig().contains("GUI.Menu-CustomModelId") && WKTool.getVersion() >= 14) im.setCustomModelData(wk.getConfig().getInt("GUI.Menu-CustomModelId"));
				im.setDisplayName(LangConfigLoader.getString("DO_NOT_TOUCH"));
				item_mn.setItemMeta(im);
				//添加功能性物品：上一页
				ItemStack item_pre = new ItemStack(Material.getMaterial(wk.getConfig().getString("GUI.TurnPageMaterial")));
				ItemMeta ip = item_pre.getItemMeta();
				if (wk.getConfig().contains("GUI.TurnPrePage-CustomModelId") && WKTool.getVersion() >= 14) ip.setCustomModelData(wk.getConfig().getInt("GUI.TurnPrePage-CustomModelId"));
				ip.setDisplayName(LangConfigLoader.getString("PREVIOUS_PAGE"));
				item_pre.setItemMeta(ip);
				//添加功能性物品：下一页
				ItemStack item_next = new ItemStack(Material.getMaterial(wk.getConfig().getString("GUI.TurnPageMaterial")));
				ItemMeta in = item_next.getItemMeta();
				if (wk.getConfig().contains("GUI.TurnNextPage-CustomModelId") && WKTool.getVersion() >= 14) in.setCustomModelData(wk.getConfig().getInt("GUI.TurnNextPage-CustomModelId"));
				in.setDisplayName(LangConfigLoader.getString("NEXT_PAGE"));
				item_next.setItemMeta(in);
				
				for(int i0 = 0; i0 < 9; i0++) {//最上一排
					inv.setItem(i0, item_mn);
				}
				for(int i11 = 54 - 9; i11 < 54; i11++) {//最下一排
					inv.setItem(i11, item_mn);
				}
				
				
				if(kits.size() >= 1) {
					ItemStack item_getall;
					if(wk.getConfig().getString("GUI.GetAllMaterial").equalsIgnoreCase("Default")) {
						item_getall = GlassPane.GREEN.getItemStack();
					}else {
						item_getall = new ItemStack(Material.getMaterial(wk.getConfig().getString("GUI.GetAllMaterial")));
					}

					ItemMeta img = item_next.getItemMeta();
					if (wk.getConfig().contains("GUI.GetAll-CustomModelId") && WKTool.getVersion() >= 14) img.setCustomModelData(wk.getConfig().getInt("GUI.GetAll-CustomModelId"));
					img.setDisplayName(LangConfigLoader.getString("KITMAIL_GETALL"));
					item_getall.setItemMeta(img);
					inv.setItem(52, item_getall);
				}
				
				//判断当前页数添加功能按钮
				if(i1 > 1 && i1 < guinum) {
					inv.setItem(48, item_pre);
					inv.setItem(50, item_next);
				}
				
				if(i1 == guinum && i1 != 1) {
					inv.setItem(48, item_pre);
				}
				
				if(guinum > 1 && i1 == 1) {
					inv.setItem(50, item_next);
				}

				linv.add(inv);
			}
			

			//添加物品到指定的inv
			int itemnum = 0;
			for(int invnum = 0; invnum < guinum; invnum++) {
				for(int i2 = 9; i2 < 45; i2++) {
					if(itemnum >= litem.size() || litem.size() == 0 ) {
						break;
					}
					linv.get(invnum).setItem(i2, litem.get(itemnum));
					itemnum += 1;
				}
			}
			

			
			p.openInventory(linv.get(page -1));
		}
}
