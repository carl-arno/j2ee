package pers.hawk.room.z.security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.Base64Utils;

public class BaseAES {

	/**
	 * UTF-8编码解密
	 * 
	 * @param data
	 * @return
	 */
	public static String decrypt(byte[] data) {
		try {

			Cipher cipher = Cipher.getInstance(AESConstant.getAesstring());

			SecretKeySpec secretKeySpec = new SecretKeySpec(AESConstant.getKEY(), AESConstant.getAes());

			IvParameterSpec ivParameterSpec = new IvParameterSpec(AESConstant.getVI());

			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

			return new String(cipher.doFinal(data), "UTF-8");

		} catch (Exception ex) {
			// logger.error(ex.getLocalizedMessage());
			throw new RuntimeException(ex);
		}
	}

	/**
	 * AES直接解密
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] decryptByte(byte[] data) {
		try {

			Cipher cipher = Cipher.getInstance(AESConstant.getAesstring());

			SecretKeySpec secretKeySpec = new SecretKeySpec(AESConstant.getKEY(), AESConstant.getAes());

			IvParameterSpec ivParameterSpec = new IvParameterSpec(AESConstant.getVI());

			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

			return cipher.doFinal(data);

		} catch (Exception ex) {
			// logger.error(ex.getLocalizedMessage());
			throw new RuntimeException(ex);
		}
	}

	/**
	 * AES直接加密
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] encrypt(byte[] data) {
		try {

			Cipher cipher = Cipher.getInstance(AESConstant.getAesstring());

			SecretKeySpec secretKeySpec = new SecretKeySpec(AESConstant.getKEY(), AESConstant.getAes());

			IvParameterSpec ivParameterSpec = new IvParameterSpec(AESConstant.getVI());

			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

			data = cipher.doFinal(data);

		} catch (Exception ex) {
			// logger.error(ex.getLocalizedMessage());
			throw new RuntimeException(ex);
		}
		return data;
	}

	/**
	 * AES base64加密
	 * 
	 * @param data
	 * @param key
	 * @param iv
	 * @param md5
	 * @return
	 */
	public static String encrypt64(String data) {
		try {

			Cipher cipher = Cipher.getInstance(AESConstant.getAesstring());

			SecretKeySpec secretKeySpec = new SecretKeySpec(AESConstant.getKEY(), AESConstant.getAes());

			IvParameterSpec ivParameterSpec = new IvParameterSpec(AESConstant.getVI());

			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

			data = Base64.encodeBase64String(cipher.doFinal(data.getBytes("UTF-8")));

		} catch (Exception ex) {
			// logger.error(ex.getLocalizedMessage());
			throw new RuntimeException(ex);
		}
		return data;
	}

	/**
	 * AES 64解密
	 * 
	 * @param data
	 * @param md5
	 * @return
	 */
	public static String decrypt64(byte[] data) {
		try {

			Cipher cipher = Cipher.getInstance(AESConstant.getAesstring());

			SecretKeySpec secretKeySpec = new SecretKeySpec(AESConstant.getKEY(), AESConstant.getAes());

			IvParameterSpec ivParameterSpec = new IvParameterSpec(AESConstant.getVI());

			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

			return new String(cipher.doFinal(Base64Utils.decodeFromString(new String(data))));

		} catch (Exception ex) {
			// logger.error(ex.getLocalizedMessage());
			throw new RuntimeException(ex);
		}
	}

	/**
	 * @param data
	 * @param md5
	 * @return
	 */
	public static byte[] decryptByte64(byte[] data) {
		try {

			Cipher cipher = Cipher.getInstance(AESConstant.getAesstring());

			SecretKeySpec secretKeySpec = new SecretKeySpec(AESConstant.getKEY(), AESConstant.getAes());

			IvParameterSpec ivParameterSpec = new IvParameterSpec(AESConstant.getVI());

			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

			return cipher.doFinal(data);

		} catch (Exception ex) {
			// logger.error(ex.getLocalizedMessage());
			throw new RuntimeException(ex);
		}
	}
}
