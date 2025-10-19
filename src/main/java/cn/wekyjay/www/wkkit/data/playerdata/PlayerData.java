package cn.wekyjay.www.wkkit.data.playerdata;

import java.util.List;

public interface PlayerData {
	/**
	 * 设置或添加的玩家数据
	 * @param kitname
	 * @param playername
	 * @param data
	 * @param time
	 */
	void setKitToFile(String playername, String kitname, String data,int time);
	
	/**
	 * 删除玩家的指定礼包数据
	 * @param playername
	 * @param kitname
	 */
	void delKitToFile(String playername, String kitname);
	
	/**
	 * 设置玩家指定礼包的上次领取日期
	 * @param playername
	 * @param kitname
	 * @param value
	 */
	void setKitData(String playername, String kitname, String value);
	/**
	 * 设置玩家指定礼包的领取次数
	 * @param playername
	 * @param kitname
	 * @param value
	 */
	void setKitTime(String playername, String kitname, int value);
	/**
	 * 获得玩家数据内的所有礼包名
	 * @param playername
	 * @return
	 */
	List<String> getKits(String playername);
	
	/**
	 * 获取玩家礼包的日期数据
	 * @param kitname
	 * @return
	 */
	String getKitData(String playername, String kitname);
	
	/**
	 * 获取玩家礼包的领取次数数据
	 * @param kitname
	 * @return
	 */
	Integer getKitTime(String playername, String kitname);
	
	/**
	 * 判断玩家指定礼包数据是否存在
	 * @param playername
	 * @param kitname
	 * @return
	 */
	Boolean contain_Kit(String playername, String kitname);
	
	/**
	 * 判断玩家是否有数据存在
	 * @param playername
	 * @return
	 */
	Boolean contain_Kit(String playername);
}
