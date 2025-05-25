package cn.wekyjay.www.wkkit.tool.items;

import cn.wekyjay.www.wkkit.tool.WKTool;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public enum PlayerHead {
	DEFAULT(),
	PRESENT_RED("dca29a3a-76d3-4979-88a2-2da034b99212","[I;-593323462,1993558393,-2002637408,884576786]","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNlZjlhYTE0ZTg4NDc3M2VhYzEzNGE0ZWU4OTcyMDYzZjQ2NmRlNjc4MzYzY2Y3YjFhMjFhODViNyJ9fX0="),
	MENU_BOOK("412a2b41-327c-4f2a-a555-9a12b8c4174a","[I;1093282625,847007530,-1521116654,-1195108534]","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjE0Y2Q1NGY3MmMxMTAxMzRjNjljZWU1Nzc1ZDE1ODM4NmE2ODc2MDk1ZjM3OWRkNDI3ZmVkNzNmYjNiNjc0MCJ9fX0="),
	STEV_PLUSHIE("6a4729be-2ecd-4ce9-8581-725a03b76b5f","[I;1783048638,785206505,-2055114150,62352223]","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGE1NWQwZmYwNDA0MDJlNmE5MjQ2ZDA5YmE5MGUyZTE1YzY4YTE5ZjdkOWE3ZGMwNWVjNGE1NzE3MDc4NGNjZSJ9fX0=");

	private ItemStack head;

	PlayerHead() {
		this(null, null, null);
	}

	PlayerHead(String id, String newid, String textures) {
		String fullVersion = WKTool.getFullVersion();
		if (WKTool.compareVersion(fullVersion, "1.13") < 0) {
			// 1.12及以下
			head = createLegacyHead(id, textures);
		} else if (WKTool.compareVersion(fullVersion, "1.20.5") >= 0) {
			// 1.20.5及以上
			head = createModernHead(newid, textures);
		} else {
			// 1.13-1.20.4
			head = createMidHead(id, textures);
		}
	}

	private ItemStack createLegacyHead(String id, String textures) {
		// 1.12及以下
		ItemStack item = new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short) 3);
		NBT.modify(item, nbt -> {
			ReadWriteNBT skullOwner = nbt.getOrCreateCompound("SkullOwner");
			skullOwner.setString("Id", id != null ? id : UUID.randomUUID().toString());
			skullOwner.getOrCreateCompound("Properties")
					.getCompoundList("textures")
					.addCompound()
					.setString("Value", textures != null ? textures : "");
		});
		return item;
	}

	private ItemStack createMidHead(String id, String textures) {
		// 1.13-1.20.4
		ItemStack item = new ItemStack(Material.PLAYER_HEAD);
		NBT.modify(item, nbt -> {
			ReadWriteNBT skullOwner = nbt.getOrCreateCompound("SkullOwner");
			skullOwner.setString("Id", id != null ? id : UUID.randomUUID().toString());
			skullOwner.getOrCreateCompound("Properties")
					.getCompoundList("textures")
					.addCompound()
					.setString("Value", textures != null ? textures : "");
		});
		return item;
	}

	private ItemStack createModernHead(String newid, String textures) {
		// 1.20.5及以上
		ItemStack item = new ItemStack(Material.PLAYER_HEAD);
		UUID uuid;
		if (newid != null) {
			if (newid.startsWith("[I;")) {
				uuid = parseMojangIntArray(newid);
			} else {
				uuid = UUID.fromString(newid);
			}
		} else {
			uuid = UUID.randomUUID();
		}
		NBT.modifyComponents(item, nbt -> {
			ReadWriteNBT profileNbt = nbt.getOrCreateCompound("minecraft:profile");
			profileNbt.setUUID("id", uuid);
			ReadWriteNBT propertiesNbt = profileNbt.getCompoundList("properties").addCompound();
			propertiesNbt.setString("name", "textures");
			propertiesNbt.setString("value", textures != null ? textures : "");
		});
		return item;
	}

	private static UUID parseMojangIntArray(String intArray) {
		// 例: [I;-593323462,1993558393,-2002637408,884576786]
		intArray = intArray.replace("[I;", "").replace("]", "");
		String[] parts = intArray.split(",");
		if (parts.length != 4) throw new IllegalArgumentException("Invalid Mojang int array for UUID");
		long most = (Long.parseLong(parts[0].trim()) << 32) | (Long.parseLong(parts[1].trim()) & 0xFFFFFFFFL);
		long least = (Long.parseLong(parts[2].trim()) << 32) | (Long.parseLong(parts[3].trim()) & 0xFFFFFFFFL);
		return new UUID(most, least);
	}

	public ItemStack getItemStack() {
		return head;
	}

	public static ItemStack getPlayerHead(Player p) {
		ItemStack item = DEFAULT.getItemStack().clone();
		NBT.modify(item, nbt -> {
			ReadWriteNBT skullOwner = nbt.getOrCreateCompound("SkullOwner");
			skullOwner.setString("Name", p.getName());
			skullOwner.setString("Id", p.getUniqueId().toString());
		});
		return item;
	}
}
