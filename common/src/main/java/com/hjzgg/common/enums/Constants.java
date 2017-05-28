package com.hjzgg.common.enums;

import java.util.Locale;

final public class Constants {

    private Constants() {}
    
    /**
     * 记录最近更新用户
     */
	public static final String DEFAULT_USERID = "JD";
	
    public static final int MIN_DATE = 19000101;

    public static final int MAX_DATE = 99991231;

    public static final String HIGH_VALUE = "~~~~";

    public static final String YES = "Y";

    public static final String NO = "N";

    public static final String DEBIT = "D";

    public static final String CREDIT = "C";

    public static final String DEFAULT_CURRENCY = "CNY";

    //pls refer to IdUtils.getBaseId();
    //public static final long BASE_ID = 1000000000000000L;

    public static final long MAX_AMOUNT = 999999999999999L;
    /**
     * 默认时区
     */
	public static final Locale DEFAUTL_LOCALE = Locale.ENGLISH;
	/**
	 * 利率保留位数
	 */
	public static int DEFAULT_INT_RATE_DIGTIAL_COUNT = 12;
	/**
	 * 金额保留位数
	 */
	public static int DEFAULT_AMT_DIGTIAL_COUNT = 2;
	/**
	 * 金额中间计算保留位数
	 */
	public static int DEFAULT_AMT_COMPARE_DIGTIAL_COUNT = 11;
	/**
	 * 利率比较值
	 */
	public static double DEFAULT_RATE_COMPARE_VALUE = 1E-9;
	/**
	 * 浮点数比较值
	 */
	public static double DEFAULT_AMOUNT_ZERO_VALUE = 1E-4;
	/**
	 * 0的小整数
	 */
	public final static Short ZERO_SHORT = (short) 0;
	/**
	 * 0的浮点数
	 */
	public final static Double ZERO_DOUBLE = Double.valueOf(0);
    /**
     * the whole credit check rule
     */
    public final static String WHOLE_CREDIT_CHECK_RULE = "CC01";
    
    /**
     * 接口缺省版本号
     */
    public final static String DEFAULT_VERSION = "2.0";

    /**
     * 登陆用户的权限缓存key前缀
     */
    public final static String LOGIN_USER_AUTH_KEY_PREFIX = "loginUser_auth_";
    
    public final static String LOGIN_USER_DOMAIN_KEY_PREFIX = "loginUser_domain_prefix";

    public final static Integer AUTH_INVALID_SECOND = 1800;
}
