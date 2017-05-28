package com.hjzgg.common.util.request;

import com.hjzgg.common.util.json.GsonUtils;
import nl.bitwalker.useragentutils.Browser;
import nl.bitwalker.useragentutils.OperatingSystem;
import nl.bitwalker.useragentutils.UserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(RequestUtils.class);

	// \b 是单词边界(连着的两个(字母字符 与 非字母字符) 之间的逻辑上的间隔), 字符串在编译时会被转码一次,所以是 "\\b"
	// \B 是单词内部逻辑间隔(连着的两个字母字符之间的逻辑上的间隔)
	private static String PHONEREG = "\\b(ip(hone|od)|android|opera m(ob|in)i|windows (phone|ce)|blackberry|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp"
			+ "|laystation portable)|nokia|fennec|htc[-_]|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
	private static String TABLEREG = "\\b(ipad|tablet|(Nexus 7)|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";

	// 移动设备正则匹配：手机端、平板
	private static Pattern PHONEPAT = Pattern.compile(PHONEREG, Pattern.CASE_INSENSITIVE);
	private static Pattern TABLEPAT = Pattern.compile(TABLEREG, Pattern.CASE_INSENSITIVE);

	public static String parseRequestParam(HttpServletRequest request) throws IOException {
		ServletInputStream sis = request.getInputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(sis));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = in.readLine()) != null) {
			sb.append(line);
		}
		sis.close();
		in.close();
		return sb.toString();
	}

	public static StringBuilder getScheme() {
		HttpServletRequest request = getHttpServletRequest();
		StringBuilder sb = new StringBuilder();
		if ("443".equals(request.getHeader("isHttp"))) {
			sb.append("https://");
		} else {
			sb.append("http://");
		}
		return sb;
	}

	public static HttpServletRequest getHttpServletRequest() {
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = servletRequestAttributes.getRequest();
		return request;
	}

	public static StringBuilder getSchemeAndServerName() {
		HttpServletRequest request = getHttpServletRequest();
		StringBuilder sb = new StringBuilder();
		if ("443".equals(request.getHeader("isHttp"))) {
			sb.append("https://");
		} else {
			sb.append("http://");
		}
		sb.append(request.getServerName());
		return sb;
	}

	public static StringBuilder getUrl() {
		HttpServletRequest request = getHttpServletRequest();
		StringBuilder sb = new StringBuilder();
		if ("443".equals(request.getHeader("isHttp"))) {
			sb.append("https://");
		} else {
			sb.append("http://");
		}
		sb.append(request.getServerName());
		sb.append(request.getRequestURI());
		return sb;
	}

	/**
	 * 获取客户端ip
	 */
	public static String getClientIp() {
		HttpServletRequest request = getHttpServletRequest();
		String ip = request.getHeader("X-Forwarded-For");
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip=" + ip);
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip=" + ip);
				}
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip=" + ip);
				}
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip=" + ip);
				}
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip=" + ip);
				}
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip=" + ip);
				}
			}
		} else if (ip.length() > 15) {
			String[] ips = ip.split(",");
			for (int index = 0; index < ips.length; index++) {
				String strIp = (String) ips[index];
				if (!("unknown".equalsIgnoreCase(strIp))) {
					ip = strIp;
					break;
				}
			}
		}
		return ip;
	}

	/**
	 * 根据IP获取地址
	 * 
	 * @param ip
	 *            请求的参数 格式为：name=xxx&pwd=xxx
	 * @param encodingString
	 *            服务器端请求编码。如GBK,UTF-8等
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static IpAnalizyResult getAddresses(String ip, String encodingString) throws UnsupportedEncodingException {
		// 这里调用pconline的接口
		String urlStr = "http://ip.taobao.com/service/getIpInfo.php";
		// 从http://whois.pconline.com.cn取得IP所在的省市区信息
		String returnStr = getResult(urlStr, ip, encodingString);
		if (returnStr != null) {
			// 处理返回的省市区信息
			System.out.println(returnStr);
			IpAnalizyResult ipAnalizyResult = GsonUtils.fromJson(returnStr, IpAnalizyResult.class);
			return ipAnalizyResult;
		}
		return null;
	}

	/**
	 * @param urlStr
	 *            请求的地址
	 * @param content
	 *            请求的参数 格式为：name=xxx&pwd=xxx
	 * @param encoding
	 *            服务器端请求编码。如GBK,UTF-8等
	 * @return
	 */
	private static String getResult(String urlStr, String content, String encoding) {
		URL url = null;
		HttpURLConnection connection = null;
		try {
			url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();// 新建连接实例
			connection.setConnectTimeout(2000);// 设置连接超时时间，单位毫秒
			connection.setReadTimeout(2000);// 设置读取数据超时时间，单位毫秒
			connection.setDoOutput(true);// 是否打开输出流 true|false
			connection.setDoInput(true);// 是否打开输入流true|false
			connection.setRequestMethod("POST");// 提交方法POST|GET
			connection.setUseCaches(false);// 是否缓存true|false
			connection.connect();// 打开连接端口
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());// 打开输出流往对端服务器写数据F
			out.writeBytes(content);// 写数据,也就是提交你的表单 name=xxx&pwd=xxx
			out.flush();// 刷新
			out.close();// 关闭输出流
			// 往对端写完数据对端服务器返回数据,以BufferedReader流来读取
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			reader.close();
			return buffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();// 关闭连接
			}
		}
		return null;
	}

	/**
	 * 获取客户端系统名称
	 */
	public static String getClientSystemName() {
		HttpServletRequest httpServletRequest = getHttpServletRequest();

		String agent = httpServletRequest.getHeader("User-Agent");
		UserAgent useragent = new UserAgent(agent);

		// 得到用户的操作系统名
		OperatingSystem operatingSystem = useragent.getOperatingSystem();
		return operatingSystem.getName();

	}

	/**
	 * 访问来自PC机的返回true，移动端的返回false
	 * 
	 * @return
	 */
	public static boolean isFromPC() {
		HttpServletRequest httpServletRequest = getHttpServletRequest();

		String agent = httpServletRequest.getHeader("User-Agent");
		return check(agent);
	}

	/**
	 * 检测是否是移动设备访问
	 * 
	 * @Title: check
	 * @Date : 2014-7-7 下午01:29:07
	 * @param userAgent
	 *            浏览器标识
	 * @return false:移动设备接入，true:pc端接入
	 */
	public static boolean check(String userAgent) {
		if (null == userAgent) {
			userAgent = "";
		}
		// 匹配
		Matcher matcherPhone = PHONEPAT.matcher(userAgent);
		Matcher matcherTable = TABLEPAT.matcher(userAgent);
		if (matcherPhone.find() || matcherTable.find()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 获取客户端浏览器名称
	 */
	public static String getClientBrowserName() {
		HttpServletRequest httpServletRequest = getHttpServletRequest();

		String agent = httpServletRequest.getHeader("User-Agent");
		UserAgent useragent = new UserAgent(agent);

		// 得到用户的浏览器名称
		Browser browser = useragent.getBrowser();
		return browser.getName();
	}

	/**
	 * unicode 转换成 中文
	 *
	 * @author fanhui 2007-3-15
	 * @param theString
	 * @return
	 */
	public static String decodeUnicode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
							case '0':
							case '1':
							case '2':
							case '3':
							case '4':
							case '5':
							case '6':
							case '7':
							case '8':
							case '9':
								value = (value << 4) + aChar - '0';
								break;
							case 'a':
							case 'b':
							case 'c':
							case 'd':
							case 'e':
							case 'f':
								value = (value << 4) + 10 + aChar - 'a';
								break;
							case 'A':
							case 'B':
							case 'C':
							case 'D':
							case 'E':
							case 'F':
								value = (value << 4) + 10 + aChar - 'A';
								break;
							default:
								throw new IllegalArgumentException("Malformed      encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't') {
						aChar = '\t';
					} else if (aChar == 'r') {
						aChar = '\r';
					} else if (aChar == 'n') {
						aChar = '\n';
					} else if (aChar == 'f') {
						aChar = '\f';
					}
					outBuffer.append(aChar);
				}
			} else {
				outBuffer.append(aChar);
			}
		}
		return outBuffer.toString();
	}

}
