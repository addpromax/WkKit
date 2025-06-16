package cn.wekyjay.www.wkkit.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabCompleter implements TabExecutor{

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if(!sender.isOp()) {
			return Arrays.asList("mail","open","cdk","info","get");
		}
		List<String> list = Arrays.asList("create","cdk","delete","send","savecache","get","kits","give","info","open","mail","group","edit","transfer","update","reload");
		List<String> savelist = new ArrayList<String>();
		String firstcmd = args[0];
		
    	if(!(args[0] == null) && !list.contains(firstcmd)) {
        	int length = args[0].length();
        	String abc = args[0];
        	for(String s : list) {
        		if(s.regionMatches(true, 0, abc, 0, length)) savelist.add(s);
        	}
        	return savelist;
        }else if(args[0] == null) {
        	return list;
        }

		if(firstcmd.equalsIgnoreCase("create")) return TabCreate.returnList(args, args.length, sender);
		if(firstcmd.equalsIgnoreCase("cdk")) return TabCdk.returnList(args, args.length, sender);
		if(firstcmd.equalsIgnoreCase("open")) return TabOpen.returnList(args, args.length, sender);
		if(firstcmd.equalsIgnoreCase("delete")) return TabDelete.returnList(args, args.length, sender);
		if(firstcmd.equalsIgnoreCase("send")) return TabSend.returnList(args, args.length, sender);
		if(firstcmd.equalsIgnoreCase("give")) return TabGive.returnList(args, args.length, sender);
		if(firstcmd.equalsIgnoreCase("edit")) return TabEdit.returnList(args, args.length, sender);
		if(firstcmd.equalsIgnoreCase("info")) return TabInfo.returnList(args, args.length, sender);
		if(firstcmd.equalsIgnoreCase("group")) return TabGroup.returnList(args, args.length, sender);
		if(firstcmd.equalsIgnoreCase("transfer")) return TabTransfer.returnList(args, args.length, sender);
	
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return false;
	}

}
