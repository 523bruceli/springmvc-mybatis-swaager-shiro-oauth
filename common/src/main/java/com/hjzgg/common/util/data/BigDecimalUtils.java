package com.hjzgg.common.util.data;

import com.hjzgg.common.enums.Constants;
import com.hjzgg.common.enums.LoanRoundingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 有关BigDecimal的工具类
 */
public class BigDecimalUtils {

	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(BigDecimalUtils.class);

	/**
	 * 描述: 获取最小值
	 * 
	 * @param amt0
	 * @param amt1
	 * @return
	 */
	public static BigDecimal min(BigDecimal amt0, BigDecimal amt1) {
		if (isAmtGreat(amt0, amt1)) {
			return amt1;
		} // 小于等于
		return amt0;
	}

	/**
	 * 描述: 获取最大值
	 *
	 * @param amt0
	 * @param amt1
	 * @return
	 */
	public static BigDecimal max(BigDecimal amt0, BigDecimal amt1) {
		if (isAmtGreat(amt0, amt1)) {
			return amt0;
		}
		return amt1;
	}

	/**
	 * 描述: 字符串转化为BigDecimal类型
	 * 
	 * @param amt0
	 *            需要转换的数值
	 * @return
	 */
	public static BigDecimal strToBigDecimal(String amt0) {
		if (amt0 == null || "".equals(amt0.trim())) {
			return BigDecimal.ZERO;
		}
		amt0 = amt0.trim();
		BigDecimal amt = new BigDecimal(amt0);
		return getZeroBigDecimalIfNull(amt);
	}

	/**
	 * 描述: 数据直接修约——结果四舍五入，保留两位精度
	 * 
	 * @param amt
	 *            需修约的数字
	 * @return
	 */
	public static BigDecimal roundingResult(BigDecimal amt) {
		amt = roundingResult(amt, Constants.DEFAULT_AMT_DIGTIAL_COUNT, LoanRoundingMode.FOUR_REMOVE_FIVE_UP.getRoundingMode());
		return amt;
	}

	/**
	 * 描述: 数据直接修约——保留两位精度
	 * 
	 * @param amt
	 *            需修约的数字
	 * @param roundingMode
	 *            修约方法
	 * @return
	 */
	public static BigDecimal getBigDecimalWhenEvenUp(BigDecimal amt, RoundingMode roundingMode) {
		amt = roundingResult(amt, Constants.DEFAULT_AMT_DIGTIAL_COUNT, roundingMode);
		return amt;
	}

	/**
	 * 描述: 数据直接修约——roundingMode
	 * 
	 * @param amt
	 *            需修约的数字
	 * @param reservedDigCount
	 *            保留精度
	 * @param roundingMode
	 *            修约方式
	 * @return
	 */
	public static BigDecimal roundingResult(BigDecimal amt, int reservedDigCount, RoundingMode roundingMode) {
		amt = getZeroBigDecimalIfNull(amt);
		amt = amt.setScale(reservedDigCount, roundingMode);
		return amt;
	}

	/**
	 * 描述: 第一个数是比较第二个数。 如果左边>右边，则返回>0 ；如果相等，返回0；如果左边<右边，返回负数。
	 * 
	 * @param amt0
	 * @param amt1
	 * @return
	 */
	public static int amtCompare(BigDecimal amt0, BigDecimal amt1) {
		amt0 = getZeroBigDecimalIfNull(amt0);
		amt1 = getZeroBigDecimalIfNull(amt1);
		return amt0.compareTo(amt1);
	}

	/**
	 * 描述: 判断第一个数是否等于第二个数
	 * 
	 * @param amt0
	 *            第一个数
	 * @param amt1
	 *            第二个数
	 * @return
	 */
	public static boolean isAmtEqual(BigDecimal amt0, BigDecimal amt1) {
		if (amtCompare(amt0, amt1) == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 描述: 判断第一个数是否大于第二个数
	 * 
	 * @param amt0
	 *            第一个数
	 * @param amt1
	 *            第二个数
	 * @return
	 */
	public static boolean isAmtGreat(BigDecimal amt0, BigDecimal amt1) {
		if (amtCompare(amt0, amt1) > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 描述: 判断第一个数是否大于等于第二个数
	 * 
	 * @param amt0
	 *            第一个数
	 * @param amt1
	 *            第二个数
	 * @return
	 */
	public static boolean isAmtGreatAndEqual(BigDecimal amt0, BigDecimal amt1) {
		if (amtCompare(amt0, amt1) >= 0) {
			return true;
		}
		return false;
	}

	/**
	 * 描述: 判断金额是否大于0
	 * 
	 * @param amt0
	 *            需要判断的金额
	 * @return
	 */
	public static boolean isAmtGreatThanZero(BigDecimal amt0) {
		if (amtCompare(amt0, BigDecimal.ZERO) > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 描述: 判断金额是否大于等于0
	 * 
	 * @param amt0
	 *            需要判断的金额
	 * @return
	 */
	public static boolean isAmtGreatThanOrEqualZero(BigDecimal amt0) {
		if (amtCompare(amt0, BigDecimal.ZERO) >= 0) {
			return true;
		}
		return false;
	}

	/**
	 * 描述: 判断金额是否小于0
	 * 
	 * @param amt0
	 *            需要判断的金额
	 * @return
	 */
	public static boolean isAmtLessThanZero(BigDecimal amt0) {
		if (amtCompare(amt0, BigDecimal.ZERO) < 0) {
			return true;
		}
		return false;
	}

	/**
	 * 描述:判断金额是否小于等于0
	 * 
	 * @param amt0
	 *            需要判断的金额
	 * @return
	 */
	public static boolean isAmtLessThanOrEqualZero(BigDecimal amt0) {
		if (amtCompare(amt0, BigDecimal.ZERO) <= 0) {
			return true;
		}
		return false;
	}

	/**
	 * 描述: 判断金额是否等于0
	 * 
	 * @param amt0
	 *            需要判断的金额
	 * @return
	 */
	public static boolean isAmtEqualZero(BigDecimal amt0) {
		if (amtCompare(amt0, BigDecimal.ZERO) == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 描述: 格式化浮点数
	 * 
	 * @param digital
	 *            需要格式化的数字
	 * @param count
	 *            保留精度
	 * @return
	 */
	public static String formatDigital(double digital, int count) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < count; i++) {
			sb.append("0");
		}
		NumberFormat format = new DecimalFormat("#0." + sb.toString());
		return format.format(digital);
	}

	/**
	 * 描述: 如果input为空返回零
	 * 
	 * @param input
	 *            判断值
	 * @return
	 */
	public static BigDecimal getZeroBigDecimalIfNull(BigDecimal input) {
		return input == null ? BigDecimal.ZERO : input;
	}

	/**
	 * 
	 * 描述: 计算两个金额相加
	 * 
	 * @param amt0
	 *            第一个金额
	 * @param amt1
	 *            第二个金额
	 * @return
	 */
	public static BigDecimal amtAdd(BigDecimal amt0, BigDecimal amt1) {
		amt0 = getZeroBigDecimalIfNull(amt0);
		amt1 = getZeroBigDecimalIfNull(amt1);
		return roundingResult(amt0.add(amt1));
	}

	/**
	 * 
	 * 描述: 计算两个金额相减
	 * 
	 * @param amt0
	 *            第一个金额（被减数）
	 * @param amt1
	 *            第二个金额（减数）
	 * @return
	 */
	public static BigDecimal amtSubs(BigDecimal amt0, BigDecimal amt1) {
		amt0 = getZeroBigDecimalIfNull(amt0);
		amt1 = getZeroBigDecimalIfNull(amt1);
		return roundingResult(amt0.subtract(amt1));
	}

	/**
	 * 
	 * 描述: 计算两个金额相乘——结果四舍五入，保留2位精度
	 * 
	 * @param amt0
	 *            第一个金额
	 * @param amt1
	 *            第二个金额
	 * @return
	 */
	public static BigDecimal amtMult(BigDecimal amt0, BigDecimal amt1) {
		return amtMult(amt0, amt1, 2, RoundingMode.HALF_UP);
	}

	/**
	 * 
	 * 描述: 计算两个金额相乘
	 * 
	 * @param amt0
	 *            第一个金额
	 * @param amt1
	 *            第二个金额
	 * @param reservedDigCount
	 *            数据保留精度
	 * @param roundingMode
	 *            数据舍入方式
	 * @return
	 */
	public static BigDecimal amtMult(BigDecimal amt0, BigDecimal amt1, int reservedDigCount, RoundingMode roundingMode) {
		amt0 = getZeroBigDecimalIfNull(amt0);
		amt1 = getZeroBigDecimalIfNull(amt1);
		return roundingResult(amt0.multiply(amt1, MathContext.DECIMAL32), reservedDigCount, roundingMode);
	}

	/**
	 * 
	 * 描述: 计算两个金额相除——结果四舍五入，保留2位精度
	 * 
	 * @param amt0
	 *            第一个金额（被除数）
	 * @param amt1
	 *            第二个金额（除数）
	 * @return
	 */
	public static BigDecimal amtDiv(BigDecimal amt0, BigDecimal amt1) {
		return amtDiv(amt0, amt1, Constants.DEFAULT_AMT_DIGTIAL_COUNT, LoanRoundingMode.FOUR_REMOVE_FIVE_UP.getRoundingMode());
	}

	/**
	 * 
	 * 描述: 计算两个金额相除
	 * 
	 * @param amt0
	 *            第一个金额（被除数）
	 * @param amt1
	 *            第二个金额（除数）
	 * @param reservedDigCount
	 *            数据保留精度
	 * @param roundingMode
	 *            数据舍入方式
	 * @return
	 */
	public static BigDecimal amtDiv(BigDecimal amt0, BigDecimal amt1, int reservedDigCount, RoundingMode roundingMode) {
		amt0 = getZeroBigDecimalIfNull(amt0);
		amt1 = getZeroBigDecimalIfNull(amt1);
		if (isAmtEqualZero(amt1)) {
			return null;
		}
		return amt0.divide(amt1, reservedDigCount, roundingMode);
	}

}
