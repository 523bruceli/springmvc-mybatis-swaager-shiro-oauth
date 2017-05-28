package com.hjzgg.common.util.cookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * cookie工具类
 */
public class CookieUtil {

	private static final int COOKIE_MAX_AGE = 7 * 24 * 3600;// 7天(单位：秒)

	/**
	 * 删除Cookie
	 * 
	 * @param request
	 * @param response
	 * @param name
	 */
	public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		if (null == name) {
			return;
		}
		Cookie cookie = getCookie(request, name);
		if (null != cookie) {
			cookie.setPath("/");
			cookie.setValue("");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
	}

	/**
	 * 根据Cookie名称得到Cookie对象，不存在该对象则返回Null
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (null == cookies || null == name || name.length() == 0) {
			return null;
		}
		Cookie cookie = null;
		for (Cookie c : cookies) {
			if (name.equals(c.getName())) {
				cookie = c;
				break;
			}
		}
		return cookie;
	}

	/**
	 * 根据cookie名查询cookie值
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	public static String findCookieValByName(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (null == cookies || null == name || name.length() == 0) {
			return null;
		}
		String value = null;
		for (Cookie c : cookies) {
			if (name.equals(c.getName())) {
				value = c.getValue();
				break;
			}
		}
		return value;
	}

	/**
	 * 添加一条新的Cookie，默认7天过期时间(单位：秒)
	 * 
	 * @param response
	 * @param name
	 * @param value
	 */
	public static void setCookie(HttpServletResponse response, String name, String value) {
		setCookie(response, name, value, COOKIE_MAX_AGE);
	}

	/**
	 * 添加一条新的Cookie，可以指定过期时间(单位：秒)
	 * 
	 * @param response
	 * @param name
	 * @param value
	 * @param maxValue
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, int maxValue) {
		if (null == name) {
			return;
		}
		if (null == value) {
			value = "";
		}
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		if (maxValue != 0) {
			cookie.setMaxAge(maxValue);
		} else {
			cookie.setMaxAge(COOKIE_MAX_AGE);
		}
		response.addCookie(cookie);
	}
}