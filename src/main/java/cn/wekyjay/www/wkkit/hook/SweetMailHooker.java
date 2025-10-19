package cn.wekyjay.www.wkkit.hook;

import cn.wekyjay.www.wkkit.kit.Kit;
import org.bukkit.entity.Player;
import top.mrxiaom.sweetmail.IMail;
import top.mrxiaom.sweetmail.attachments.AttachmentItem;

/**
 * SweetMail 挂钩类
 * 用于集成 SweetMail 插件的邮件系统
 */
public class SweetMailHooker {
    
    private static SweetMailHooker instance;
    
    /**
     * 构造函数，初始化 SweetMail 集成
     */
    public SweetMailHooker() {
        instance = this;
    }
    
    /**
     * 获取 SweetMailHooker 实例
     */
    public static SweetMailHooker getInstance() {
        return instance;
    }
    
    /**
     * 向玩家发送包含礼包的邮件
     * @param player 接收邮件的玩家
     * @param kit 要发送的礼包
     * @param title 邮件标题
     * @param content 邮件内容
     */
    public void sendKitMail(Player player, Kit kit, String title, String content) {
        try {
            IMail.MailDraft draft = IMail.api().createSystemMail("WkKit")
                    .setReceiverFromPlayer(player)
                    .setIcon("CHEST")
                    .setTitle(title)
                    .addContent(content);
            
            // 添加礼包物品作为附件
            if (kit.getItemStacks() != null && kit.getItemStacks().length > 0) {
                for (org.bukkit.inventory.ItemStack item : kit.getItemStacks()) {
                    if (item != null && item.getType() != org.bukkit.Material.AIR) {
                        draft.addAttachments(AttachmentItem.build(item));
                    }
                }
            }
            
            // 发送邮件
            IMail.Status status = draft.send();
            
            if (!status.ok()) {
                // 邮件发送失败的处理
                player.sendMessage("§c邮件发送失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 向玩家发送纯文本邮件
     * @param player 接收邮件的玩家
     * @param title 邮件标题
     * @param content 邮件内容
     */
    public void sendTextMail(Player player, String title, String content) {
        try {
            IMail.Status status = IMail.api().createSystemMail("WkKit")
                    .setReceiverFromPlayer(player)
                    .setIcon("PAPER")
                    .setTitle(title)
                    .addContent(content)
                    .send();
            
            if (!status.ok()) {
                player.sendMessage("§c邮件发送失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 检查 SweetMail 是否可用
     */
    public static boolean isAvailable() {
        try {
            IMail.api();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

