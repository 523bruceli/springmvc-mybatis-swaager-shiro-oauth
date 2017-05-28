package com.hjzgg.common.enums;

import java.math.RoundingMode;

public enum LoanRoundingMode {
	// 四舍五入
	FOUR_REMOVE_FIVE_UP("R", "四舍五入"),
	// 去掉尾部
	REMOVE_TAIL("T", "去掉尾部"),
	// 直接进位
	UP("C", "直接进位");

	private String code;

	private String desc;

	LoanRoundingMode(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public RoundingMode getRoundingMode() {
		switch (values()[ordinal()]) {
			case FOUR_REMOVE_FIVE_UP:
				return RoundingMode.HALF_UP;
			case REMOVE_TAIL:
				return RoundingMode.DOWN;
			case UP:
				return RoundingMode.UP;
		}
		return RoundingMode.HALF_UP;
	}
}
