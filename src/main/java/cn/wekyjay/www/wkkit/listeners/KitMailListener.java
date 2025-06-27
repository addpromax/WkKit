package cn.wekyjay.www.wkkit.listeners;


import cn.wekyjay.www.wkkit.WkKit;
import cn.wekyjay.www.wkkit.api.PlayersReceiveKitEvent;
import cn.wekyjay.www.wkkit.api.ReceiveType;
import cn.wekyjay.www.wkkit.command.KitMail;
import cn.wekyjay.www.wkkit.config.LangConfigLoader;
import cn.wekyjay.www.wkkit.invholder.MailHolder;
import cn.wekyjay.www.wkkit.kit.Kit;
import cn.wekyjay.www.wkkit.tool.ItemEditer;
import cn.wekyjay.www.wkkit.tool.WKTool;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static org.bukkit.event.inventory.InventoryAction.NOTHING;
import static org.bukkit.event.inventory.InventoryAction.UNKNOWN;

public class KitMailListener implements Listener {
    static WkKit wk = WkKit.getWkKit();// 调用主类实例

    Map<String, Integer> m = new HashMap<>();
    String guiName;


    /*玩家打开礼包邮箱事件*/
    @EventHandler
    public void onInventory(InventoryOpenEvent e) {
        String pagetitle = WKTool.replacePlaceholder("page", 1 + "", LangConfigLoader.getString("GUI_PAGETITLE"));
        if (WKTool.getEventInvTitle(e).equals(LangConfigLoader.getString("KITMAIL_TITLE")) || WKTool.getEventInvTitle(e).equals(LangConfigLoader.getString("KITMAIL_TITLE") + " - " + pagetitle)) {
            guiName = LangConfigLoader.getString("KITMAIL_TITLE");
            m.put(e.getPlayer().getName(), 1);
        }
    }

    /*玩家礼包邮箱领取礼包事件*/
    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventory(InventoryClickEvent e) {
        if (e.getInventory().getHolder() != null && e.getInventory().getHolder() instanceof MailHolder && e.getWhoClicked() == WKTool.getEventInvPlayer(e)) {
            e.setCancelled(true);
            if (e.getAction().equals(NOTHING) || e.getAction().equals(UNKNOWN)) {
                return;
            }
            String name = e.getWhoClicked().getName();
            Player p = (Player) e.getWhoClicked();
            Bukkit.getLogger().info("测试");
            // 判断是否能领取
            if (e.getRawSlot() >= 8 & e.getRawSlot() <= 44 && ItemEditer.hasWkKitTag(e.getCurrentItem()) && WKTool.hasSpace(p, 1)) {
                p.getInventory().addItem(e.getCurrentItem());
                String kitName = ItemEditer.getWkKitTagValue(e.getCurrentItem());
                PlayersReceiveKitEvent event = new PlayersReceiveKitEvent(p, Kit.getKit(kitName), ReceiveType.MAIL);
                Bukkit.getPluginManager().callEvent(event);
                Bukkit.getLogger().info("测试1");
                if (event.isCancelled()) return;
                WkKit.getPlayerData().delMailToFile(name, kitName);
                p.closeInventory();
                (new KitMail()).openKitMail(p, 1);
                Bukkit.getLogger().info("测试2");
                return;
            }
            List<String> set = WkKit.getPlayerData().getMailKits(name);
            Bukkit.getLogger().info("测试3");
            // 如果大于一页则需要手动领取
            if (e.getInventory().firstEmpty() == -1 && e.getRawSlot() < 45 && e.getRawSlot() > 8 ){
                p.sendMessage(LangConfigLoader.getStringWithPrefix("KIT_GET_FAILED", ChatColor.YELLOW));
                return;
            }
            if (set != null && !set.isEmpty()) {
                if (e.getRawSlot() == 52) {
                    if (!WKTool.hasSpace(p, (int) Arrays.stream(e.getInventory().getContents()).filter(Objects::nonNull).count() - 18)) {
                        p.sendMessage(LangConfigLoader.getStringWithPrefix("KIT_GET_FAILED", ChatColor.YELLOW));
                        return;
                    }
                    for (ItemStack is : e.getInventory().getContents()) {
                        if (is != null && ItemEditer.hasWkKitTag(is)) {
                            String kitName = ItemEditer.getWkKitTagValue(is);
                            // 回调事件
                            PlayersReceiveKitEvent event = new PlayersReceiveKitEvent(p, Kit.getKit(kitName), ReceiveType.MAIL);
                            Bukkit.getPluginManager().callEvent(event);
                            // 如果没有取消
                            if (!event.isCancelled()) {
                                p.getInventory().addItem(is);
                                WkKit.getPlayerData().delMailToFile(name, kitName);
                            }
                        }
                    }
                    p.closeInventory();
                    (new KitMail()).openKitMail(p, 1);
                    return;
                }
                /**
                 * 翻页逻辑
                 */
                if (e.getRawSlot() == 50 || e.getRawSlot() == 48) {
                    int page = 1;
                    if (e.getCurrentItem().getItemMeta().getDisplayName().equals(LangConfigLoader.getString("NEXT_PAGE"))) {
                        page = ((MailHolder) e.getInventory().getHolder()).getPage() + 1;
                    } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(LangConfigLoader.getString("PREVIOUS_PAGE"))) {
                        page = ((MailHolder) e.getInventory().getHolder()).getPage() - 1;
                    } else {
                        return;
                    }

                    new KitMail().openKitMail((Player) e.getWhoClicked(), page);
                }
            }
        }
    }
}