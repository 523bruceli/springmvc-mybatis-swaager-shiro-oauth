package com.hjzgg.common.util.secure;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * SHA-256安全签名/验签
 */
public class SHAUtil {
	private static final String SHA = "SHA";

	/**
	 * SHA加密
	 *
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptSHA(byte[] data) throws Exception {
		MessageDigest sha = MessageDigest.getInstance(SHA);
		sha.update(data);
		return sha.digest();
	}

	/**
	 * 对数据进行SHA256签名
	 * 
	 * @param data
	 * @return
	 */
	public static String digestBySHA256(String data, String charSet) {
		try {
			return DigestUtils.sha256Hex(data.getBytes(charSet));
		} catch (UnsupportedEncodingException e) {

			throw new RuntimeException("SHA256Util exception: data=" + data);
		}

	}

	/**
	 * SHA256验签方法
	 * 
	 * @param text
	 *            明文
	 * @param key
	 *            密钥
	 * @param sha256
	 *            密文
	 * @return true/false
	 * @throws Exception
	 */
	public static boolean verify(String text, String key, String sha256, String charSet) throws Exception {
		String sha256Text = digestBySHA256(text + key, charSet);

		if (sha256Text.equalsIgnoreCase(sha256)) {
			return true;
		} else {
			return false;
		}
	}
}
