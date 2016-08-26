package com.iworker.bigdata.common;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.lang.StringUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.alibaba.fastjson.JSON;

/**
 * 封装开发中常用的方法
 * @author 张震文
 * 2016年4月20日-上午10:59:59
 */
public class IworkerUtils {
	
	/** md5加密类 */
	private static MD5keyBean md5 = new MD5keyBean();
	
	/**
	 * 永中文档转换对应错误码
	 */
	public static Map<Integer, String> convertCodeMap = new HashMap<Integer, String>();
	static {
		convertCodeMap.put(0, "转换成功");
		convertCodeMap.put(1, "传入的文件，找不到");
		convertCodeMap.put(2, "传入的文件，打开失败");
		convertCodeMap.put(3, "转换过程异常失败");
		convertCodeMap.put(4, "传入的文件有密码");
		convertCodeMap.put(5, "targetFileName的后缀名错误");
	}
	
	/** 验证数组是否为空 */
	public static boolean isNotEmpty(Object[] objects) {
		if (objects != null && objects.length > 0) {
			return true;
		} else {
			return false;
		}
	}

	/** 验证list是否为空 */
	public static boolean isNotEmpty(List list) {
		if (list != null && list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public static String getMD5(String str) {
		return md5.getkeyBeanofStr(str);
	}
	
	/**
	 * des加密
	 * @param datasource 待加密字符串
	 * @param password 加密私钥，长度不能够小于8位
	 * @return 加密后的字节数组
	 */
	public static byte[] desCrypto(byte[] datasource, String password) {              
        try{  
        SecureRandom random = new SecureRandom();  
        DESKeySpec desKey = new DESKeySpec(password.getBytes());  
        //创建一个密匙工厂，然后用它把DESKeySpec转换成  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
        SecretKey securekey = keyFactory.generateSecret(desKey);  
        //Cipher对象实际完成加密操作  
        Cipher cipher = Cipher.getInstance("DES");  
        //用密匙初始化Cipher对象  
        cipher.init(Cipher.ENCRYPT_MODE, securekey, random);  
        //现在，获取数据并加密  
        //正式执行加密操作  
        return cipher.doFinal(datasource);  
        }catch(Throwable e){  
           e.printStackTrace();  
           return null;  
        }  
	}  
	
	/**
	 * 解密
	 * @param datasource 待解密字节数组
	 * @param password 解密私钥，长度不能够小于8位
	 * @return 解密后字节数组
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] datasource, String password) throws Exception {  
        // DES算法要求有一个可信任的随机数源  
        SecureRandom random = new SecureRandom();  
        // 创建一个DESKeySpec对象  
        DESKeySpec desKey = new DESKeySpec(password.getBytes());  
        // 创建一个密匙工厂  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
        // 将DESKeySpec对象转换成SecretKey对象  
        SecretKey securekey = keyFactory.generateSecret(desKey);  
        // Cipher对象实际完成解密操作  
        Cipher cipher = Cipher.getInstance("DES");  
        // 用密匙初始化Cipher对象  
        cipher.init(Cipher.DECRYPT_MODE, securekey, random);  
        // 真正开始解密操作  
        return cipher.doFinal(datasource);  
	}  
	
	/**
	 *  base64加密
	 * @param encodeStr 需要加密的字节数组
	 * @return
	 */
	public static String base64Encode(byte[] encodeStr) {
		return new BASE64Encoder().encode(encodeStr);
	}
	
	/**
	 * base64解密
	 * @param decodeStr 需要解密的字符串
	 * @return
	 * @throws Exception
	 */
	public static byte[] base64Decode(String decodeStr) throws Exception {
		return new BASE64Decoder().decodeBuffer(decodeStr);
	}
	
	/**
	 * 防止在map.get(key)为空时 导致的空指针异常
	 * @param map
	 * @param key
	 * @return
	 */
	public static String get(Map map, String key) {
		return map.get(key) == null ? "" : map.get(key).toString();
	}
	
	/**
	 * 根据自定义格式将日期转换成字符串
	 * @param pattern
	 * @param date
	 * @return
	 */
	public static String formatDate(String pattern, Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	
	/**
	 * json到对象的转换
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> T parseJson2Obj(String json, Class<T> clazz) {
		return (T) JSON.parseObject(json, clazz);
	}
	
	/**
	 * 对象转json
	 * @param obj
	 * @return
	 */
	public static String parseObj2Json(Object obj) {
		return com.alibaba.fastjson.JSONObject.toJSON(obj).toString();
	}
	
	/**
	 * 模拟php的time()函数，
	 * @return
	 */
	public static String get10TimeStamp() {
		return String.valueOf(new Date().getTime()/1000);
	}

	/**
	 * 截取字符串并且按要求加....
	 * @param content
	 * @param length 截取长度
	 * @param addDot 是否加...
	 * @return
	 */
	public static String cutstr(String content, int length, boolean addDot) {
		if(content.length() > length) {
			content = content.substring(0, length);
			if(addDot) {
				content += "...";
			}
		}
		return content;
	}

	/**
	 * 将list用指定分隔符切分成字符串
	 * @param list
	 * @param separator
	 * @return
	 */
	public static String list2Str(List<String> list, String separator) {
		
		if(!isNotEmpty(list)) {
			return "";
		}
 		StringBuffer sb = new StringBuffer();
		for(int i=0; i<list.size(); i++) {
			if (i == list.size() - 1) {  
			    sb.append(list.get(i));  
			} else {
				sb.append(list.get(i)).append(separator);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 将list用指定分隔符切分成字符串
	 * @param list
	 * @param separator
	 * flag 是否加上'str'
	 * @return
	 */
	public static String list2Str(List<String> list, String separator, boolean flag) {
		if(!isNotEmpty(list)) {
			return "";
		}
		String s = "";
		if(flag) {
			s = "'";
		}
 		StringBuffer sb = new StringBuffer();
		for(int i=0; i<list.size(); i++) {
			if (i == list.size() - 1) {  
			    sb.append(s + list.get(i) + s);  
			} else {
				sb.append(s + list.get(i) + s).append(separator);
			}
		}
		return sb.toString();
	}
	
	
	
	public static Calendar getFirstDayOfMonthCalendar() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天  
	    calendar.set(Calendar.HOUR_OF_DAY, 0);  
	    calendar.set(Calendar.MINUTE, 0);  
	    calendar.set(Calendar.SECOND, 0); 
	    return calendar;
	}
	
	

	/**
	 * 将list转成string泛型
	 * @param to_ids
	 * @return
	 */
	public static List<String> toStringList(List list) {
		List<String> returnList = new ArrayList<String>();
		if(!isNotEmpty(list)) {
			return returnList;
		}
		
		for(int i=0; i<list.size(); i++) {
			returnList.add(list.get(i).toString());
		}
		return returnList;
	}

	/**
	 * list去重
	 * @param userList
	 * @return
	 */
	public static List uniqueList(List userList) {
		return new ArrayList(new HashSet(userList));
	}
	
	/**
	 * 是否是手机号，简单验证
	 * @param mobile
	 * @return
	 */
	public static boolean isMobile(String mobile) {
		if(StringUtils.isBlank(mobile)) {
			return false;
		}
		String[] mobiles = new String[] {"134", "135", "136", "137", "138", "139", "150", "151", "152", "157", "158", "159", "187", "188", "147", "181", "182", "183", "184",
				"130", "131", "132", "155", "156", "185", "186", "145", "176",
				"133", "153", "180", "189", "177", "170", "178"
		};
		
		boolean flag = false;
		for(String mobileStart : mobiles) {
			if(mobile.startsWith(mobileStart)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	/**
	 * 模式php array函数
	 * @return
	 */
	public static List<String> addList(String ...strs) {
		List<String> list = new ArrayList<String>();
		int length = strs.length;
		if(length == 0) {
			return list;
		}
		
		for(String str : strs) {
			list.add(str);
		}
		return list;
	}

	public static Map<String, String> toStringMap(Map template) {
		Map<String, String> map = new HashMap<String, String>();
		Set<Entry> entrySet = template.entrySet();
		Iterator<Entry> it = entrySet.iterator();
		while (it.hasNext()) {
			Entry next = it.next();
			Object key = next.getKey();
			Object value = next.getValue();
			if(value == null) {
				value = "";
			}
			map.put(key.toString(), value.toString());
		}
		return map;
	}

	
	/**
	 * 获取7379缓存redis加密后的key
	 * @param key
	 * @return
	 */
	public static String getCachKey(String key) {
		return "json:" + getMD5(key).toLowerCase();
	}
}