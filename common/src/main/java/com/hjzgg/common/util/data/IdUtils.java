package com.hjzgg.common.util.data;

import com.hjzgg.common.util.secure.BaseUtil;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

/**
 * ID工具类
 */
public class IdUtils {

	private static SecureRandom secRandom = new SecureRandom();

	private static Random random = new Random();

	/**
	 * 生成36位的唯一ID, 中间有-分割.
	 */
	public static String uuid() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 生成32位的唯一ID, 中间无-分割.
	 */
	public static String uuid2() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 使用SecureRandom随机生成19位的Long.
	 */
	public static long randomLong() {
		return Math.abs(secRandom.nextLong());
	}

	/**
	 * 基于Base62编码的SecureRandom随机生成指定位数的bytes.
	 */
	public static String randomBase62(int length) {
		byte[] randomBytes = new byte[length];
		secRandom.nextBytes(randomBytes);
		return BaseUtil.encodeBase62(randomBytes);
	}

	/**
	 * 生成0~9随机数
	 * 
	 * @return
	 */
	public static int randomNum() {
		return (int) (Math.random() * 10);
	}

	/**
	 * 生成N位随机数
	 *
	 * @return
	 */
	public static int randomNum(int n) {
		return (int) (Math.random() * (0.9 * (int) Math.pow(10, n))) + (int) Math.pow(10, n - 1);
	}

	/**
	 * 产生0-9的随机数
	 * 
	 * @return 0-9
	 * 
	 */
	public static String createRandomNum() {
		return String.valueOf(random.nextInt(10));
	}

	/**
	 * 产生a-z中任意小写字母
	 * 
	 * @return a-z,任意字母
	 * 
	 */
	public static String createRandomLowerLetter() {
		// char类型a，转换为byte，数值为97
		int a = 97;
		// 97+26位随机数，小写字母byte范围
		int randomInt = a + random.nextInt(26);
		return String.valueOf((char) (byte) randomInt);
	}

	/**
	 * 产生A-Z中任意大写字母
	 * 
	 * @return A-Z中任意字母
	 * 
	 */
	public static String createRandomUpperLetter() {
		// char类型A，转换为byte，数值为65
		byte A = 65;
		// 65+26位随机数，小写字母byte范围
		int randomInt = A + random.nextInt(26);
		return String.valueOf((char) (byte) randomInt);
	}

	/**
	 * 产生一个指定位数的随机码(由数字或大小写字母构成)
	 * 
	 * @param randomCodeLen
	 *            指定位数
	 * @param hasLetter
	 *            是否包括字母,true包括,false不包括
	 * @return 随机码
	 * 
	 */
	public static String createRandomCode(int randomCodeLen, boolean hasLetter) {
		// 存放结果
		StringBuilder result = null;
		if (randomCodeLen >= 1) {
			result = new StringBuilder();
			if (hasLetter) {
				for (int i = 0; i < randomCodeLen; i++) {
					// 产生0-1的随机数,去过区分大小写改成3
					int randomInt = random.nextInt(3);
					switch (randomInt) {
						case 0:
							// 数字
							result.append(createRandomNum());
							break;
						case 1:
							// 大写字母
							result.append(createRandomUpperLetter());
							break;
						case 2:
							// 小写字母
							result.append(createRandomLowerLetter());
							break;
					}
				}
			} else if (!hasLetter) {
				for (int i = 0; i < randomCodeLen; i++) {
					result.append(createRandomNum());
				}
			}
			return result.toString();
		}
		return null;
	}

}
