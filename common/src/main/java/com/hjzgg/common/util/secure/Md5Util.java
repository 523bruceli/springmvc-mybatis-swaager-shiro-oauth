package com.hjzgg.common.util.secure;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * MD5加密
 */
public class Md5Util {
	private static final Logger logger = LoggerFactory.getLogger(Md5Util.class);

	private static final String MD5 = "MD5";
	private static final String CHARSET = "utf-8";

	/***
	 * MD5加密
	 * 
	 * @param data
	 *            明文
	 * @return
	 */
	public static String digestByMd5(String data) {
		try {
			return DigestUtils.md5Hex(data.getBytes(CHARSET));
		} catch (UnsupportedEncodingException e) {
			logger.error("Md5Util exception: data=" + data);

			throw new RuntimeException("Md5Util exception: data=" + data);
		}

	}

	/***
	 * MD5加密
	 * 
	 * @param data
	 *            明文
	 * @return
	 */
	public final static String MD5(String data) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = data.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance(MD5);
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * MD5加密
	 * 
	 * @param text
	 *            明文
	 * @return
	 * @throws Exception
	 */
	public static String md5(String text) throws Exception {
		byte[] bytes = (text).getBytes(CHARSET);

		MessageDigest messageDigest = MessageDigest.getInstance(MD5);
		messageDigest.update(bytes);
		bytes = messageDigest.digest();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			if ((bytes[i] & 0xff) < 0x10) {
				sb.append("0");
			}

			sb.append(Long.toString(bytes[i] & 0xff, 16));
		}

		return sb.toString().toLowerCase();
	}

	/**
	 * MD5加密
	 * 
	 * @param text
	 *            明文
	 * @param key
	 *            密钥
	 * @return 密文
	 * @throws Exception
	 */
	public static String md5(String text, String key) throws Exception {
		byte[] bytes = (text + key).getBytes(CHARSET);

		MessageDigest messageDigest = MessageDigest.getInstance(MD5);
		messageDigest.update(bytes);
		bytes = messageDigest.digest();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			if ((bytes[i] & 0xff) < 0x10) {
				sb.append("0");
			}

			sb.append(Long.toString(bytes[i] & 0xff, 16));
		}

		return sb.toString().toLowerCase();
	}

	/**
	 * MD5验证方法
	 * 
	 * @param text
	 *            明文
	 * @param key
	 *            密钥
	 * @param md5
	 *            密文
	 * @return true/false
	 * @throws Exception
	 */
	public static boolean verify(String text, String key, String md5) throws Exception {
		String md5Text = digestByMd5(text + key);
		if (md5Text.equalsIgnoreCase(md5)) {
			return true;
		} else {
			return false;
		}
	}
}
