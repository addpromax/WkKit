package cn.wekyjay.www.wkkit.kitcode;

import cn.wekyjay.www.wkkit.WkKit;
import cn.wekyjay.www.wkkit.config.LangConfigLoader;
import cn.wekyjay.www.wkkit.tool.MessageManager;
import cn.wekyjay.www.wkkit.tool.WKTool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CodeManager {
	private final static String Base32Alphabet = "ABCDEFGHIJKMNPQRSTUVWXYZ23456789";
	private static String password;

	//从byte转为字符表索引所需要的位数
	final static int convertByteCount = 5;

	public static String getPassword() {
		return password;
	}

	public static void checkPassWord() {
		String value = WkKit.getWkKit().getConfig().getString("KitCode.Key");
		String pattern = "^(?=.*[a-zA-Z])(?=.*\\d).+$";
		if(!(value != null && WKTool.ismatche(value, pattern) && value.length() > 6 && value.length() < 18)) {
			char charr[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
			StringBuilder sb = new StringBuilder();
			Random r = new Random();
			for (int x = 0; x < 12; ++x) {
				sb.append(charr[r.nextInt(charr.length)]);
			}
			password = sb.toString();
			WkKit.getWkKit().getConfig().set("KitCode.Key", sb.toString());
			WkKit.getWkKit().saveConfig();
			MessageManager.sendMessageWithPrefix(LangConfigLoader.getString("AUTO_GENERATE_KEY"));
		}else {
			password = value;
		}
	}

	/**
	 * 生成兑换码
	 * 这里每一次生成兑换码的最大数量为int的最大值即2147483647
	 * @return
	 */
	public static List<String> create(byte groupid,int codecount,int codelength,String password) {
		List<String> cdklist = new ArrayList<>();

		//8位的数据总长度
		int fullcodelength = codelength * convertByteCount / 8;
		//随机码对时间和id同时做异或处理
		//类型1，id4，随机码n,校验码1
		int randcount = fullcodelength - 6;//随机码有多少个

		//如果随机码小于0 不生成
		if(randcount <= 0 ) {
			return null;
		}
		for(int i = 0 ; i < codecount ; i ++) {
			//这里使用i作为code的id
			//生成n位随机码
			byte[] randbytes = new byte[randcount];
			for(int j = 0 ; j  < randcount ; j ++) {
				randbytes[j] = (byte)(Math.random() * Byte.MAX_VALUE);
			}

			//存储所有数据
			ByteHelper byteHapper = ByteHelper.CreateBytes(fullcodelength);
			byteHapper.AppendNumber(groupid).AppendNumber(i).AppendBytes(randbytes);

			//计算校验码 这里使用所有数据相加的总和与byte.max 取余
			byte verify = (byte) (byteHapper.GetSum() % Byte.MAX_VALUE);
			byteHapper.AppendNumber(verify);

			//使用随机码与时间和ID进行异或
			for(int j = 0 ; j < 5 ; j ++) {
				byteHapper.bytes[j] = (byte) (byteHapper.bytes[j] ^ (byteHapper.bytes[5 + j % randcount]));
			}

			//使用密码与所有数据进行异或来加密数据
			byte[] passwordbytes = password.getBytes();
			for(int j = 0 ; j < byteHapper.bytes.length ; j++){
				byteHapper.bytes[j] = (byte) (byteHapper.bytes[j] ^ passwordbytes[j % passwordbytes.length]);
			}

			//这里存储最终的数据
			byte[] bytes = new byte[codelength];

			//按6位一组复制给最终数组
			for(int j = 0 ; j < byteHapper.bytes.length ; j ++) {
				for(int k = 0 ; k < 8 ; k ++) {
					int sourceindex = j*8+k;
					int targetindex_x = sourceindex / convertByteCount;
					int targetindex_y = sourceindex % convertByteCount;
					byte placeval = (byte)Math.pow(2, k);
					byte val = (byte)((byteHapper.bytes[j] & placeval) == placeval ? 1:0);
					//复制每一个bit
					bytes[targetindex_x] = (byte)(bytes[targetindex_x] | (val << targetindex_y));
				}
			}

			StringBuilder result = new StringBuilder();
			//编辑最终数组生成字符串
			for(int j = 0 ; j < bytes.length ; j ++) {
				result.append(Base32Alphabet.charAt(bytes[j]));
				if(j == 3 || j == 7) result.append('-');
			}
			// 如果存在就重新生成
			if(WkKit.CDKConfig.contains(result.toString()))i--;
			else  cdklist.add(result.toString());
		}
		return cdklist;
	}

	/**
	 * 验证兑换码
	 * @param code
	 */
	public static Boolean VerifyCode(String code){
		if(code.length() != 14 || !code.contains("-")) return false;
		// 去掉分节
		StringBuilder result = new StringBuilder();
		for(String str : code.split("-")) {
			result.append(str);
		}
		code = result.toString();

		byte[] bytes = new byte[code.length()];

		//首先遍历字符串从字符表中获取相应的二进制数据
		for(int i=0;i<code.length();i++){
			byte index = (byte) Base32Alphabet.indexOf(code.charAt(i));
			bytes[i] = index;
		}

		//还原数组
		int fullcodelength = code.length() * convertByteCount / 8;
		int randcount = fullcodelength - 6;//随机码有多少个

		byte[] fullbytes = new byte[fullcodelength];
		for(int j = 0 ; j < fullbytes.length ; j ++) {
			for(int k = 0 ; k < 8 ; k ++) {
				int sourceindex = j*8+k;
				int targetindex_x = sourceindex / convertByteCount;
				int targetindex_y = sourceindex % convertByteCount;

				byte placeval = (byte)Math.pow(2, targetindex_y);
				byte val = (byte)((bytes[targetindex_x] & placeval) == placeval ? 1:0);

				fullbytes[j] = (byte) (fullbytes[j] | (val << k));
			}
		}

		//解密，使用密码与所有数据进行异或来加密数据
		byte[] passwordbytes = password.getBytes();
		for(int j = 0 ; j < fullbytes.length ; j++){
			fullbytes[j] = (byte) (fullbytes[j] ^ passwordbytes[j % passwordbytes.length]);
		}

		//使用随机码与时间和ID进行异或
		for(int j = 0 ; j < 5 ; j ++) {
			fullbytes[j] = (byte) (fullbytes[j] ^ (fullbytes[5 + j % randcount]));
		}

		//获取校验码 计算除校验码位以外所有位的总和
		int sum = 0;
		for(int i = 0 ;i < fullbytes.length - 1; i ++){
			sum += fullbytes[i];
		}
		byte verify = (byte) (sum % Byte.MAX_VALUE);

		//校验
		if(verify == fullbytes[fullbytes.length - 1]){
			return true;
		}else {
			return false;
		}
	}
}
