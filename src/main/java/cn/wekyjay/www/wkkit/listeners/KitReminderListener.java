package cn.wekyjay.www.wkkit.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * 礼包邮箱登录提醒领取
 * 已由 SweetMail 插件接管邮件系统
 * @author WekyJay
 *
 */
public class KitReminderListener implements Listener{
	@EventHandler
	public void whenPlayerOnline(PlayerJoinEvent e) {
		// 邮件提醒功能已由 SweetMail 插件接管
		// 请使用 SweetMail 的配置来管理邮件提醒功能
	}
}
