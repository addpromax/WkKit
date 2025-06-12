package cn.wekyjay.www.wkkit.tool;

import cn.wekyjay.www.wkkit.WkKit;
import cn.wekyjay.www.wkkit.config.LangConfigLoader;
import cn.wekyjay.www.wkkit.listeners.ChackPluginListener;
import com.alibaba.druid.support.json.JSONParser;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;


public class ChackPluginVersion{

	private static final WkKit wkkit = WkKit.getWkKit();

	private static Map<String, Object> resourceInfo = null;


	public static Map<String, Object> getResourceInfo() {
		return resourceInfo;
	}

	/**
	 * Modify of 1.2.5
	 * 1.2.5之后修改为Spigot Resource API
	 */
	public ChackPluginVersion(){
		MessageManager.sendMessageWithPrefix(LangConfigLoader.getString("PLUGIN_CHACKUPDATE_IN"));

		ChackPluginVersion.resourceInfo = this.getResourceInfo("versions/latest");

		if (ChackPluginVersion.resourceInfo != null) {
			runCheck();
		}

	}


	public Map<String,Object> getResourceInfo(String path){
		HttpURLConnection con = null;
		BufferedReader buffer = null;
		InputStream inputStream = null;


		//得到连接对象
		try {
			URL url = new URL("https://api.spiget.org/v2/resources/98415/" + path);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			//设置连接超时时间
			con.setConnectTimeout(10000);
			//设置读取超时时间
			con.setReadTimeout(10000);
			//添加请求头
			con.setRequestProperty("Connection","keep-alive");
			//获取服务器返回的输入流
			inputStream = con.getInputStream();

			//得到响应码
			int responseCode = con.getResponseCode();
			// 如果响应码成功了则存储响应数据
			if (responseCode == HttpURLConnection.HTTP_OK){
				//读取输入流s
				buffer = new BufferedReader(new InputStreamReader(inputStream));
				StringBuilder respose = new StringBuilder();
				String line;
				while((line = buffer.readLine())!=null){
					respose.append(line);
				}
				// 响应成功则注册玩家登陆检查更新事件
				Bukkit.getPluginManager().registerEvents(new ChackPluginListener(),wkkit);
				return new JSONParser(respose.toString()).parseMap();
			}


		} catch (IOException e) {
			MessageManager.sendMessageWithPrefix(LangConfigLoader.getString("PLUGIN_CHACKUPDATE_FAILED"));
			return null;
		}finally {
			if (inputStream != null){
				try {
					inputStream.close();
				} catch (IOException ignored) {}
			}
			if (buffer != null){
				try {
					buffer.close();
				} catch (IOException ignored) {}
			}
		}
		MessageManager.sendMessageWithPrefix(LangConfigLoader.getString("PLUGIN_CHACKUPDATE_FAILED"));
		return null;
	}

	public void runCheck() {
        if(resourceInfo == null) {return;}
		String lver = resourceInfo.get("name").toString();
        if(!WkKit.getWkKit().getDescription().getVersion().equals(lver)) { //判断版本是否与最新版本不同
			// 提示下载链接
			MessageManager.sendMessageWithPrefix(LangConfigLoader.getString("PLUGIN_CHACKUPDATE_LINK") + "https://www.spigotmc.org/resources/%E2%9C%A8wkkit%E2%9C%A8-will-be-your-forever-gift-plugin-1-7-10-support-1-20-4%E2%9C%85.98415/");
			// 提示最新版本
        	MessageManager.sendMessageWithPrefix(LangConfigLoader.getString("PLUGIN_CHACKUPDATE_LATESTVERSION") + lver + " "
					+ LangConfigLoader.getString("PLUGIN_CHACKUPDATE_CURRENTVERSION") + WkKit.getWkKit().getDescription().getVersion()
			);
        }else {
        	MessageManager.sendMessageWithPrefix(LangConfigLoader.getString("PLUGIN_CHACKUPDATE_ED"));
        }
	}



}
