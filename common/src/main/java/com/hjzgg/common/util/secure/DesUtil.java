package com.hjzgg.common.util.secure;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.Key;

/**
 * des3 cbc/ecb 加解密
 */
public class DesUtil {

	private static final String DES = "DESede";
	private static final String TRANSFORMATION = "DESede/ECB/PKCS5Padding";// 或者
																			// DESede/ECB/NoPadding

	private static final byte[] IV = { 3, '#', 6, 'q', 0, '/', 'b', 'j' };

	/**
	 * 使用3Des进行加密，
	 *
	 * @param plainText
	 * @param charset
	 * @return
	 */
	public static byte[] des3Encrypt(String plainText, String key, String charset) {

		try {
			// 生成密钥
			SecretKey secretKey = new SecretKeySpec(key.getBytes(charset), DES);
			/*
			 * KeyGenerator kg = KeyGenerator.getInstance(DES); kg.init(new
			 * SecureRandom(key.getBytes(charset))); SecretKey secretKey =
			 * kg.generateKey();
			 */
			// 加密
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);

			return cipher.doFinal(plainText.getBytes(charset));

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 3DES 加密
	 *
	 * @param keybyte
	 *            加密密钥，长度为24字节
	 * @param src
	 *            被加密的数据缓冲区（源）
	 * @return
	 */
	public static byte[] encrypt(byte[] keybyte, byte[] src) {
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, DES);
			// 加密
			Cipher c1 = Cipher.getInstance(TRANSFORMATION);
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			return c1.doFinal(src);// 在单一方面的加密或解密
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	/**
	 * 使用3DES进行解密
	 *
	 * @param cipherText
	 * @param key
	 * @param charset
	 * @return
	 */
	public static String des3Decrypt(byte[] cipherText, String key, String charset) {
		try {
			SecretKey secretKey = new SecretKeySpec(key.getBytes(charset), DES);

			/*
			 * KeyGenerator kg = KeyGenerator.getInstance(DES); // kg.init(56);
			 * kg.init(new SecureRandom(key.getBytes(charset))); SecretKey
			 * secretKey = kg.generateKey();
			 */

			Cipher c1 = Cipher.getInstance(TRANSFORMATION);
			c1.init(Cipher.DECRYPT_MODE, secretKey); // 初始化为解密模式
			byte[] bytes = c1.doFinal(cipherText);
			return new String(bytes, charset);

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 3DES 解密
	 *
	 * @param keybyte
	 *            为加密密钥，长度为24字节
	 * @param src
	 *            为加密后的缓冲区
	 * @return
	 */
	private static byte[] decrypt(byte[] keybyte, byte[] src) {
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, DES);
			// 解密
			Cipher c1 = Cipher.getInstance(TRANSFORMATION);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	/**
	 * ECB加密,不要IV
	 *
	 * @param key
	 *            密钥
	 * @param data
	 *            明文
	 * @return Base64编码的密文
	 * @throws Exception
	 */
	public static byte[] des3EncodeECB(byte[] key, byte[] data) throws Exception {
		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(key);
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);

		Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");

		cipher.init(Cipher.ENCRYPT_MODE, deskey);

		byte[] bOut = cipher.doFinal(data);

		return bOut;
	}

	/**
	 * ECB解密,不要IV
	 *
	 * @param key
	 *            密钥
	 * @param data
	 *            Base64编码的密文
	 * @return 明文
	 * @throws Exception
	 */
	public static byte[] des3DecodeECB(byte[] key, byte[] data) throws Exception {

		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(key);
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);

		Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");

		cipher.init(Cipher.DECRYPT_MODE, deskey);

		byte[] bOut = cipher.doFinal(data);

		return bOut;
	}

	/**
	 * CBC加密
	 *
	 * @param key
	 *            密钥
	 * @param keyiv
	 *            IV
	 * @param data
	 *            明文
	 * @return Base64编码的密文
	 * @throws Exception
	 */
	public static byte[] des3EncodeCBC(byte[] key, byte[] keyiv, byte[] data) throws Exception {

		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(key);
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);

		Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(keyiv);

		cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);

		byte[] bOut = cipher.doFinal(data);

		return bOut;
	}

	/**
	 * CBC解密
	 *
	 * @param key
	 *            密钥
	 * @param keyiv
	 *            IV
	 * @param data
	 *            Base64编码的密文
	 * @return 明文
	 * @throws Exception
	 */
	public static byte[] des3DecodeCBC(byte[] key, byte[] keyiv, byte[] data) throws Exception {

		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(key);
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);

		Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(keyiv);

		cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

		byte[] bOut = cipher.doFinal(data);

		return bOut;
	}

	/**
	 * 将元数据进行补位后进行3DES加密
	 * <p/>
	 * 补位后 byte[] = 描述有效数据长度(int)的byte[]+原始数据byte[]+补位byte[]
	 *
	 * @param sourceData
	 *            元数据字符串
	 * @return 返回3DES加密后的16进制表示的字符串
	 */
	public static String encrypt2HexStr(byte[] keys, String sourceData) {
		byte[] source = new byte[0];
		try {
			// 元数据
			source = sourceData.getBytes("UTF-8");

			// 1.原数据byte长度
			int merchantData = source.length;
			// System.out.println("原数据据:" + sourceData);
			// System.out.println("原数据byte长度:" + merchantData);
			// System.out.println("原数据HEX表示:" + bytes2Hex(source));
			// 2.计算补位
			int x = (merchantData + 4) % 8;
			int y = (x == 0) ? 0 : (8 - x);
			// System.out.println("需要补位 :" + y);
			// 3.将有效数据长度byte[]添加到原始byte数组的头部
			byte[] sizeByte = DigitCovertUtil.intToByteArray(merchantData);
			byte[] resultByte = new byte[merchantData + 4 + y];

			for (int i = 0; i < 4; i++) {
				resultByte[i] = sizeByte[i];
			}
			// 4.填充补位数据
			for (int i = 0; i < merchantData; i++) {
				resultByte[4 + i] = source[i];
			}
			for (int i = 0; i < y; i++) {
				resultByte[merchantData + 4 + i] = 0x00;
			}
			// System.out.println("补位后的byte数组长度:" + resultByte.length);
			// System.out.println("补位后数据HEX表示:" + bytes2Hex(resultByte));
			// System.out.println("秘钥HEX表示:" +bytes2Hex(keys));
			// System.out.println("秘钥长度:" +keys.length);

			byte[] desdata = encrypt(keys, resultByte);

			// System.out.println("加密后的长度:" + desdata.length);

			return DigitCovertUtil.bytes2Hex(desdata);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 3DES 解密 进行了补位的16进制表示的字符串数据
	 *
	 * @return
	 */
	public static String decrypt4HexStr(byte[] keys, String data) {

		byte[] hexSourceData = new byte[0];
		try {
			hexSourceData = DigitCovertUtil.hex2byte(data.getBytes("UTF-8"));

			// 解密
			byte[] unDesResult = decrypt(keys, hexSourceData);
			byte[] dataSizeByte = new byte[4];
			for (int i = 0; i < 4; i++) {
				dataSizeByte[i] = unDesResult[i];
			}
			// 有效数据长度
			int dsb = DigitCovertUtil.byteArrayToInt(dataSizeByte, 0);

			byte[] tempData = new byte[dsb];
			for (int i = 0; i < dsb; i++) {
				tempData[i] = unDesResult[4 + i];
			}

			return DigitCovertUtil.hex2bin(DigitCovertUtil.toHexString(tempData));

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

}
