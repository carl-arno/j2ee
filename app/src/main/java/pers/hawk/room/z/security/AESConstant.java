package pers.hawk.room.z.security;

public class AESConstant {

	private static final String AESSTRING = "AES/CBC/PKCS5Padding";

//	private static final String AESSTRING = "AES/ECB/PKCS7Padding";
	
	private static final String AES = "AES";

	private static String KEYSTRING = "iBTpRJ001iTEM001";

	private static String VISTRING = "iBTpRJ001iTEM001";

	private static byte[] KEY = KEYSTRING.getBytes();

	private static byte[] VI = VISTRING.getBytes();

	private static boolean MD5 = false;

	
	
	
	public static String getKEYSTRING() {
		return KEYSTRING;
	}

	public static void setKEYSTRING(String kEYSTRING) {
		KEYSTRING = kEYSTRING;
	}

	public static String getVISTRING() {
		return VISTRING;
	}

	public static void setVISTRING(String vISTRING) {
		VISTRING = vISTRING;
	}

	public static byte[] getKEY() {
		return KEY;
	}

	public static void setKEY(byte[] kEY) {
		KEY = kEY;
	}

	public static byte[] getVI() {
		return VI;
	}

	public static void setVI(byte[] vI) {
		VI = vI;
	}

	public static boolean isMD5() {
		return MD5;
	}

	public static void setMD5(boolean mD5) {
		MD5 = mD5;
	}

	public static String getAesstring() {
		return AESSTRING;
	}

	public static String getAes() {
		return AES;
	}

}
