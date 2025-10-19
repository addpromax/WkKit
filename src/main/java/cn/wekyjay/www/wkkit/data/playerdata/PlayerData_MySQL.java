package cn.wekyjay.www.wkkit.data.playerdata;

import cn.wekyjay.www.wkkit.WkKit;
import cn.wekyjay.www.wkkit.kit.Kit;
import cn.wekyjay.www.wkkit.mysql.playersqldata.PlayerSQLData;

import java.util.List;

public class PlayerData_MySQL implements PlayerData{

	@Override
	public void setKitToFile(String playername, String kitname, String data, int time) {
		new PlayerSQLData().insertData(kitname, playername, data, time);
	}

	@Override
	public List<String> getKits(String playername) {
		return new PlayerSQLData().findKitName(playername);
	}

	@Override
	public String getKitData(String playername, String kitname) {
		return new PlayerSQLData().findKitData(playername, kitname);
	}

	@Override
	public Integer getKitTime(String playername, String kitname) {
		return new PlayerSQLData().findKitTime(playername, kitname);
	}

	@Override
	public void delKitToFile(String playername, String kitname) {
		new PlayerSQLData().deleteData(kitname, playername);
	}

	@Override
	public void setKitData(String playername, String kitname, String value) {
		if(!contain_Kit(playername, kitname)) {//如果没有数据
			int time;
			if(Kit.getKit(kitname).getTimes() == null) time = -1;
			else time = Kit.getKit(kitname).getTimes();
			setKitToFile(playername, kitname, value, time);
		}else {
			new PlayerSQLData().update_Data_Data(playername, kitname, value);
		}
	}

	/**
	 * 排他锁的方式更新数据
	 * @param playername
	 * @param kitname
	 * @param value
	 */
	public void setKitDataOfLock(String playername, String kitname, String value){
		if(!contain_Kit(playername, kitname)) {//如果没有数据
			int time;
			if(Kit.getKit(kitname).getTimes() == null) time = -1;
			else time = Kit.getKit(kitname).getTimes();
			setKitToFile(playername, kitname, value, time);
		}else {
			new PlayerSQLData().update_Data_Data_Lock(playername, kitname, value);
		}
	}

	@Override
	public void setKitTime(String playername, String kitname, int value) {
		if(!contain_Kit(playername, kitname)) {//如果没有数据
			setKitToFile(playername, kitname, "2009-5-17-8-0-0", value);
		}else {
			new PlayerSQLData().update_Time_Data(playername, kitname, value);
		}

	}

	@Override
	public Boolean contain_Kit(String playername, String kitname) {
		List<String> list = WkKit.getPlayerData().getKits(playername);
		if(list != null && list.contains(kitname)) {return true;}
		return false;
	}

	@Override
	public Boolean contain_Kit(String playername) {
		return new PlayerSQLData().findPlayer(playername);
	}
}
