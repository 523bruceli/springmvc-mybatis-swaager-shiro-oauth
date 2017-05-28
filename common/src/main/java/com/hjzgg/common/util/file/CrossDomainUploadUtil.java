package com.hjzgg.common.util.file;

import com.hjzgg.common.util.http.MyX509TrustManager;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 解决平台跨域上传文件问题
 * 
 * @author Administrator
 *
 */
public class CrossDomainUploadUtil {
	private static final Logger logger = LoggerFactory.getLogger(CrossDomainUploadUtil.class);

	private CrossDomainUploadUtil() {

	}

	/**
	 * 上传文件
	 * 
	 * @param url
	 * @param request
	 * @return
	 */
	public static JSONArray uploadFile(String url, HttpServletRequest request) {
		JSONArray json = new JSONArray();
		try {
			String contentType = request.getContentType();
			String boundary = contentType.substring(contentType.indexOf('=') + 1);
			// 获得文件来源，文件名和输入流。
			List<FileItem> items = getFileItems(request);
			Iterator<FileItem> its = items.iterator();
			Map<String, String> params = new HashMap<>();
			Map<String, FileItem> files = new HashMap<>();
			while (its.hasNext()) {
				FileItem item = its.next();
				if (item.isFormField()) {
					params.put(item.getFieldName(), item.getString());
				} else {
					byte[] bt = item.get();
					if (bt.length > 4 * 1024 * 1024) {
						JSONObject j = new JSONObject();
						j.put("rs", "-1");
						j.put("msg", "上传文件不能大于4M");
						json.add(j);
						return json;
					}
					files.put(item.getName(), item);
				}
			}
			String returnStr = post(url, boundary, params, files);
			JSONObject jo = JSONObject.fromObject(returnStr);
			json.add(jo);
		} catch (Exception e) {
			logger.error("uploadFile-error", e);
		}

		return json;
	}

	/**
	 * 获取上传参数
	 * 
	 * @param request
	 * @return
	 * @throws FileUploadException
	 */
	@SuppressWarnings("unchecked")
	public static List<FileItem> getFileItems(HttpServletRequest request) throws FileUploadException {
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8");
		return upload.parseRequest(request);
	}

	/**
	 * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
	 * 
	 * @param url
	 *            文件上传的
	 * @param boundary
	 * @param params
	 *            普通参数
	 * @param files
	 *            文件参数
	 * @return
	 * @throws IOException
	 */
	public static String post(String url, String boundary, Map<String, String> params, Map<String, FileItem> files) throws IOException {
		String returnStr = "";
		HttpURLConnection conn = null;
		DataOutputStream outStream = null;
		try {
			String prefix = "--";
			String linend = "\r\n";
			String multipartFromData = "multipart/form-data";
			String charset = "UTF-8";

			conn = getFileUploadConnection(multipartFromData, url, boundary);
			// 首先组拼文本类型的参数
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sb.append(prefix);
				sb.append(boundary);
				sb.append(linend);
				sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + linend);
				sb.append(linend);
				sb.append(entry.getValue());
				sb.append(linend);
			}

			outStream = new DataOutputStream(conn.getOutputStream());
			outStream.write(sb.toString().getBytes(charset));
			InputStream in = null;
			// 发送文件数据
			if (files != null) {
				for (Map.Entry<String, FileItem> file : files.entrySet()) {
					StringBuilder sb1 = new StringBuilder();
					sb1.append(prefix);
					sb1.append(boundary);
					sb1.append(linend);
					// name是post中传参的键 filename是文件的名称
					sb1.append("Content-Disposition: form-data; name=\"myFile\"; filename=\"" + file.getKey() + "\"" + linend);
					sb1.append("Content-Type:image/jpeg" + linend);
					sb1.append(linend);
					outStream.write(sb1.toString().getBytes(charset));

					InputStream is = file.getValue().getInputStream();
					byte[] buffer = new byte[10240];
					int len = 0;
					while ((len = is.read(buffer)) != -1) {
						outStream.write(buffer, 0, len);
					}

					is.close();
				}

				// 请求结束标志
				byte[] endData = (linend + prefix + boundary + prefix + linend).getBytes();
				outStream.write(endData);
				outStream.flush();
				// 得到响应码
				int res = conn.getResponseCode();
				if (res == 200) {
					in = conn.getInputStream();
					int ch;
					StringBuilder sb2 = new StringBuilder();
					while ((ch = in.read()) != -1) {
						sb2.append((char) ch);
					}

					String resultStr = new String(sb2.toString().getBytes("ISO-8859-1"), charset);
					logger.info("上传文件返回值-----》" + resultStr);
					returnStr = resultStr.substring(resultStr.indexOf("'") + 2, resultStr.lastIndexOf("'") - 1);
					logger.info("上传文件返回值截取json内容-----》" + returnStr);
				} else {
					returnStr = res + "";
				}

			}
		} catch (Exception e) {
			logger.error("post-error", e);
		} finally {
			if (null != outStream) {
				outStream.close();
			}
			if (null != conn) {
				conn.disconnect();
			}
		}

		return returnStr;
	}

	/**
	 * 获取数据连接
	 * 
	 * @param contentType
	 * @param urlstr
	 * @return
	 * @throws Exception
	 */
	public static HttpURLConnection getFileUploadConnection(String contentType, String urlstr) throws Exception {
		return getFileUploadConnection(contentType, urlstr, null);
	}

	/**
	 * 获取数据连接
	 * 
	 * @param contentType
	 * @param urlstr
	 * @param boundary
	 * @return
	 * @throws Exception
	 */
	public static HttpURLConnection getFileUploadConnection(String contentType, String urlstr, String boundary) throws Exception {
		URL uri = new URL(urlstr);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		if (conn instanceof HttpsURLConnection) {
			((HttpsURLConnection) conn).setSSLSocketFactory(MyX509TrustManager.getSSLSocketFactory());
		}
		conn.setReadTimeout(5 * 1000); // 缓存的最长时间
		conn.setDoInput(true);// 允许输入
		conn.setDoOutput(true);// 允许输出
		conn.setUseCaches(false); // 不允许使用缓存
		conn.setRequestMethod("POST");
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", "UTF-8");
		if (boundary != null && !"".equals(boundary)) {
			conn.setRequestProperty("Content-Type", contentType + ";boundary=" + boundary);
		} else {
			conn.setRequestProperty("Content-Type", contentType);
		}
		return conn;
	}

	/**
	 * 上传文件
	 * 
	 * @param actionUrl
	 * @param params
	 * @param files
	 * @return
	 * @throws Exception
	 */
	public static String postFile(String actionUrl, Map<String, String> params, Map<String, byte[]> files) throws Exception {
		StringBuilder sb2 = null;
		String boundary = java.util.UUID.randomUUID().toString();
		String prefix = "--";
		String linend = "\r\n";
		String multipartFromData = "multipart/form-data";
		String charset = "UTF-8";

		HttpURLConnection conn = getFileUploadConnection(multipartFromData, actionUrl, boundary);
		// 首先组拼文本类型的参数
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(prefix);
			sb.append(boundary);
			sb.append(linend);
			sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + linend);
			sb.append("Content-Type: text/plain; charset=" + charset + linend);
			sb.append("Content-Transfer-Encoding: 8bit" + linend);
			sb.append(linend);
			sb.append(entry.getValue());
			sb.append(linend);
		}

		DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
		outStream.write(sb.toString().getBytes());
		InputStream in;
		// 发送文件数据
		if (files != null) {
			for (Map.Entry<String, byte[]> file : files.entrySet()) {
				StringBuilder sb1 = new StringBuilder();
				sb1.append(prefix);
				sb1.append(boundary);
				sb1.append(linend);
				sb1.append("Content-Disposition: form-data; name=\"pic\"; filename=\"" + file.getKey() + "\"" + linend);
				sb1.append("Content-Type: application/octet-stream; charset=" + charset + linend);
				sb1.append(linend);
				outStream.write(sb1.toString().getBytes());

				outStream.write(file.getValue());

				outStream.write(linend.getBytes());
			}

			// 请求结束标志
			byte[] endData = (prefix + boundary + prefix + linend).getBytes();
			outStream.write(endData);
			outStream.flush();
			// 得到响应码
			int res = conn.getResponseCode();
			if (res == 200) {
				in = conn.getInputStream();
				int ch;
				sb2 = new StringBuilder();
				while ((ch = in.read()) != -1) {
					sb2.append((char) ch);
				}
			}
			outStream.close();
			conn.disconnect();
			// 解析服务器返回来的数据
			return null == sb2 ? "" : sb2.toString();
		} else {
			return "Update icon Fail";
		}
	}
}
