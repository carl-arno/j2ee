package pers.hawk.room.util;

/**
 * 中文转换工具
 */
public class ChineseUtil {

	public static String toUnicode(String string ){
		StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            // 取出每一个字符
            char c = string.charAt(i);
      
            // 转换为unicode
            stringBuffer.append("\\u" + Integer.toHexString(c));
        }
      return stringBuffer.toString();
	}
	
}
