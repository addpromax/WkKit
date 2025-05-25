package cn.wekyjay.www.wkkit.tool.items;

import cn.wekyjay.www.wkkit.tool.WKTool;
import org.bukkit.inventory.ItemStack;
import de.tr7zw.changeme.nbtapi.NBT;

public enum Barrier {
		DEFAULT();
		private ItemStack item;
		Barrier() {
			if(WKTool.getVersion() <= 7) {
				item = NBT.itemStackFromNBT(NBT.parseNBT("{id:30s,Count:1b,Damage:0s,}"));
			}else {
				item = new org.bukkit.inventory.ItemStack(org.bukkit.Material.BARRIER);
			}
		}
		public ItemStack getItemStack() {
			return item;
		}
}
