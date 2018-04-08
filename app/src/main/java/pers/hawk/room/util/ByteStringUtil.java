package pers.hawk.room.util;

import java.io.UnsupportedEncodingException;

/**
 * 字节,字符串工具
 */
public class ByteStringUtil {

	/**
	 * integer 转 网络字节序(大端模式)
	 */
	public static byte[] int2Bytes(int value, int len) {
		byte[] b = new byte[len];
		for (int i = 0; i < len; i++) {
			b[len - i - 1] = (byte) ((value >> 8 * i) & 0xff);
		}
		return b;
	}

	/**
	 * 网络字节序(大端模式) 转 integer 四个字节
	 */
	public static int byteArrayToInt(byte[] b, int offset) {
		int value = 0;
		for (int i = 0; i < 4; i++) {
			int shift = (4 - 1 - i) * 8;
			value += (b[i + offset] & 0x000000FF) << shift;
		}
		return value;
	}

	/**
	 * 字节的字符串形式
	 */
	public static String getByteString(byte[] bytes) {
		StringBuffer stringBuffer = new StringBuffer("\r\n");
		String string = "";
		for (int i = 0; i < bytes.length; i++) {
			string = String.valueOf(Integer.toHexString(bytes[i] & 0xFF)).toUpperCase();
			if (string.length() < 2) {
				stringBuffer.append("0");
			}
			stringBuffer.append(string);
			stringBuffer.append(" ");
		}
		return stringBuffer.toString();
	}

	/**
	 * byte 转 string
	 */
	public static String getBodyString(byte[] bytes) {
		String string = null;
		try {
			string = new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return string;
	}

}
