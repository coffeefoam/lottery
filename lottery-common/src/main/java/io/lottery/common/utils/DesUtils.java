package io.lottery.common.utils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;

public class DesUtils {

	/**
	 * DES加密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByKey(byte[] data, String key) throws Exception {
		// 可信任的随机数
		SecureRandom secureRandom = new SecureRandom();
		DESKeySpec keySpec = new DESKeySpec(key.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(keySpec);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, secureRandom);
		return cipher.doFinal(data);
	}

	public static byte[] decryptByKey(byte[] data, String key) throws Exception {
		// 可信任的随机数
		SecureRandom secureRandom = new SecureRandom();
		DESKeySpec keySpec = new DESKeySpec(key.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(keySpec);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.DECRYPT_MODE, secretKey, secureRandom);
		return cipher.doFinal(data);
	}

	public static void main(String[] args) throws Exception {
		String sourceStr = "月光宝盒";
		String key = "1234567890";
		System.out.println("待加密：" + sourceStr);
		byte[] encryptByte = DesUtils.encryptByKey(sourceStr.getBytes(), key);
		String encryptStr = Base64.encodeBase64String(encryptByte);
		System.out.println("加密后：" + encryptStr);
		byte[] decryptByte = DesUtils.decryptByKey(Base64.decodeBase64(encryptStr), key);
		String decryptStr = new String(decryptByte);
		System.out.println("解密后：" + decryptStr);
	}

}
