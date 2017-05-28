package com.hjzgg.common.util.common;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

/**
 * 汉语拼音处理类
 */
public class PinYinUtil {
	private PinYinUtil() {
	}

	/**
	 * 将传入字符串转为汉语拼音
	 * 
	 * @param str
	 * @return
	 */
	public static String getHanyuPinyin(String str) {
		if (null == str || str.length() == 0) {
			return null;
		}

		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);

		StringBuilder result = new StringBuilder();
		for (int i = 0; i < str.length(); ++i) {
			String pinyin = getPinyinByChar(str.charAt(i), format);
			result.append(pinyin);
		}

		return result.toString();
	}

	/**
	 * 将传入字符串转为汉语拼音首字母
	 * 
	 * @param str
	 * @return
	 */
	public static String getShouZiMu(String str) {
		if (null == str || str.length() == 0) {
			return null;
		}

		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);

		String pinyin;
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < str.length(); ++i) {
			pinyin = getPinyinByChar(str.charAt(i), format);
			if (null != pinyin)
				result.append(pinyin.charAt(0));
		}

		return result.toString();
	}

	/**
	 * 单个字符处理
	 * 
	 * @param ch
	 * @param format
	 * @return
	 */
	private static String getPinyinByChar(char ch, HanyuPinyinOutputFormat format) {
		try {
			String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(ch, format);
			// 有多个读音返回第一个，不能转换的返回原有字符的大写
			if (null == pinyins) {
				return String.valueOf(ch).toUpperCase();
			} else {
				return pinyins[0];
			}
		} catch (Exception e) {
			return String.valueOf(ch).toUpperCase();
		}
	}
}
