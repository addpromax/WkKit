package cn.wekyjay.www.wkkit.edit.prompt;

import cn.handyplus.lib.adapter.PlayerSchedulerUtil;
import cn.wekyjay.www.wkkit.WkKit;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;

public class KitDeletePrompt {
	public static void newConversation(Player player, String kitname) {
		Conversation conversation = new ConversationFactory(WkKit.getWkKit()) // 构建一个会话
	            .withFirstPrompt(new KitDeletePrompt_1()) // 设置在所有生成的对话中使用的第一个提示。
	            .withTimeout(60)
	            .buildConversation(player);
		conversation.getContext().setSessionData("kitname", kitname);
		conversation.begin();
	}
}
class KitDeletePrompt_1 extends ValidatingPrompt{

	@Override
	public String getPromptText(ConversationContext context) {
		return "你是否要删除§e" + context.getSessionData("kitname") + "§f?(Y/N)";
	}

	@Override
	protected boolean isInputValid(ConversationContext context, String input) {
		if(input.equalsIgnoreCase("Y") || input.equalsIgnoreCase("N")) return true; 
		return false; 
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input) {
		if(input.equalsIgnoreCase("Y")) {

			PlayerSchedulerUtil.performCommand((Player)context.getForWhom(), "wk delete " + context.getSessionData("kitname"));

			context.getForWhom().sendRawMessage("§a已成功删除礼包 - " + context.getSessionData("kitname"));
		}else {
			context.getForWhom().sendRawMessage("§c你取消了礼包删除");
		}
		return Prompt.END_OF_CONVERSATION;
	}
	
}