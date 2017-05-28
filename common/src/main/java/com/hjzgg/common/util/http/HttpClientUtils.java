package com.hjzgg.common.util.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Http API 工具类
 *
 */
public class HttpClientUtils {

	private static Logger LOG = LoggerFactory.getLogger(HttpClientUtils.class);

	private static final String APPLICATION_JSON = "application/json";

	private static final String CONTENT_TYPE_TEXT_JSON = "text/json";

	public static String cUrl(String url){
		if(url==null)return null;
		BufferedReader rd=null;
		StringBuffer receive=new StringBuffer();
		HttpURLConnection conn=null;
		try {
			
			conn=(HttpURLConnection)new URL(url).openConnection(); 
			if(conn instanceof HttpsURLConnection){
				((HttpsURLConnection) conn).setSSLSocketFactory(MyX509TrustManager.getSSLSocketFactory());
			}
			
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("GET");  
			conn.setUseCaches(false);
			rd=new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
			String line;
			while(((line=rd.readLine())!=null)){
				receive.append(line).append("\r\n");//\r\n 表示换行
			}
		} catch (Exception e) {
			LOG.error("访问"+url+"异常",e);
			return null;
		}finally{
			if(conn!=null){
				conn.disconnect();
			}
			
			if(rd!=null){
				try {
					rd.close();
				} catch (IOException e) {}
			}
			
		}
		return receive.toString();
	}
	public static String cUrlPost(String url,String data){
	    String requestUrl = url;  
        PrintWriter printWriter = null;  
        BufferedReader bufferedReader = null;  
        StringBuffer responseResult = new StringBuffer();  
        HttpURLConnection httpURLConnection = null;  
        try {  
            URL realUrl = new URL(requestUrl);  
            // 打开和URL之间的连接  
            httpURLConnection = (HttpURLConnection) realUrl.openConnection();  
			if(httpURLConnection instanceof HttpsURLConnection){
				((HttpsURLConnection) httpURLConnection).setSSLSocketFactory(MyX509TrustManager.getSSLSocketFactory());
			
			
            // 设置通用的请求属性  
            httpURLConnection.setRequestProperty("accept", "*/*");  
            httpURLConnection.setRequestProperty("connection", "Keep-Alive");  
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length()));  
            // 发送POST请求必须设置如下两行  
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true); 
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            // 获取URLConnection对象对应的输出流  
            printWriter = new PrintWriter(httpURLConnection.getOutputStream());  
            // 发送请求参数  
            printWriter.write(data);  
            // flush输出流的缓冲  
            printWriter.flush();  
            // 根据ResponseCode判断连接是否成功  
            int responseCode = httpURLConnection.getResponseCode();  
            if (responseCode != 200) {  
            	LOG.error(" Error===" + responseCode);  
            } else {  
            	LOG.info("Post Success!");  
            }  
            // 定义BufferedReader输入流来读取URL的ResponseData  
            bufferedReader = new BufferedReader(new InputStreamReader(  
                    httpURLConnection.getInputStream()));  
            String line;  
            while ((line = bufferedReader.readLine()) != null) {  
                responseResult.append(line);  
            }  
			}
        } catch (Exception e) {  
        	LOG.error("send post request error!" + e);  
        } finally {  
        	if(httpURLConnection!=null){
        		 httpURLConnection.disconnect();  
        	}
           
            try {  
                if (printWriter != null) {  
                    printWriter.close();  
                }  
                if (bufferedReader != null) {  
                    bufferedReader.close();  
                }  
            } catch (IOException ex) {  
                ex.printStackTrace();  
            }  
  
        }  
        LOG.info(responseResult.toString());
		return responseResult.toString(); 
	} 
	public static void main(String[] args) {
		String json=cUrl("http://yun.sankai.com/cloud-portal/reg/testHttps");
		System.out.println(json);
	}
	/**
	 * URL编码请求
	 * 
	 * @param url
	 * @param params
	 * @param timeout
	 * @return
	 */
//	@Deprecated
//	public static String reqPostFormDataURLEncoding(String url, String params, int timeout) {
//		HttpClient httpclient = new DefaultHttpClient();
//		try {
//			// 目标地址
//			HttpPost httppost = new HttpPost(url);
//
//			String encoderJson = URLEncoder.encode(params, HTTP.UTF_8);
//			httppost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
//			StringEntity reqEntity = new StringEntity(encoderJson);
//			reqEntity.setContentType(CONTENT_TYPE_TEXT_JSON);
//			reqEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
//
//			// 设置请求的数据
//			httppost.setEntity(reqEntity);
//			// 执行
//			HttpResponse response = httpclient.execute(httppost);
//
//			HttpEntity entity = response.getEntity();
//			if (entity != null) {
//				LOG.error("回应的数据长度是:{} ", entity.getContentLength());
//			}
//
//			String rs = EntityUtils.toString(response.getEntity());
//
//			if (entity != null) {
//				entity.getContent().close();
//			}
//
//			return rs;
//		} catch (UnsupportedEncodingException e) {
//			LOG.error("HttpClient回调业务方Encoding出现异常: {}", e.getMessage(), e);
//		} catch (ClientProtocolException e) {
//			LOG.error("HttpClient回调业务方ClientProtocol出现异常: {}", e.getMessage(), e);
//		} catch (ParseException e) {
//			LOG.error("HttpClient回调业务方Parse出现异常: {}", e.getMessage(), e);
//		} catch (IllegalStateException e) {
//			LOG.error("HttpClient回调业务方Status出现异常: {}", e.getMessage(), e);
//		} catch (IOException e) {
//			LOG.error("HttpClient回调业务方IO出现异常: {}", e.getMessage(), e);
//		} catch (Exception e) {
//			LOG.error("HttpClient回调业务方出现异常: {}", e.getMessage(), e);
//		}
//		return null;
//	}

//	@Deprecated
//	public static String reqPostFormData(String url, String params, int timeout) {
//		HttpClient httpclient = new DefaultHttpClient();
//		try {
//			// 目标地址
//			HttpPost httppost = new HttpPost(url);
//			HttpParams httpParams = httppost.getParams();
//			// unit of httpclient's timeout is milliseconds
//			httpParams.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout)
//					.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
//			httppost.setParams(httpParams);
//			// 构造最简单的字符串数据
//			StringEntity reqEntity = new StringEntity(params);
//			// 设置类型
//			// reqEntity.setContentType("application/x-www-form-urlencoded");
//			reqEntity.setContentType("application/json;charset=UTF-8");
//
//			// 设置请求的数据
//			httppost.setEntity(reqEntity);
//			// 执行
//			HttpResponse response = httpclient.execute(httppost);
//
//			HttpEntity entity = response.getEntity();
//			if (entity != null) {
//				LOG.error("回应的数据长度是:{} ", entity.getContentLength());
//			}
//
//			String rs = EntityUtils.toString(response.getEntity());
//
//			if (entity != null) {
//				entity.getContent().close();
//			}
//
//			return rs;
//		} catch (UnsupportedEncodingException e) {
//			LOG.error("HttpClient回调业务方Encoding出现异常: {}", e.getMessage(), e);
//		} catch (ClientProtocolException e) {
//			LOG.error("HttpClient回调业务方ClientProtocol出现异常: {}", e.getMessage(), e);
//		} catch (ParseException e) {
//			LOG.error("HttpClient回调业务方Parse出现异常: {}", e.getMessage(), e);
//		} catch (IllegalStateException e) {
//			LOG.error("HttpClient回调业务方Status出现异常: {}", e.getMessage(), e);
//		} catch (IOException e) {
//			LOG.error("HttpClient回调业务方IO出现异常: {}", e.getMessage(), e);
//		} catch (Exception e) {
//			LOG.error("HttpClient回调业务方出现异常: {}", e.getMessage(), e);
//		}
//		return null;
//	}

	/**
	 * http post json 方法
	 * 支持httpclient4.4.1
	 * @param sendurl  
	 * @param data  JSON格式数据
	 * @param timeout (单位：毫秒)
	 * @return
	 */
	public static String reqHttpPostJson(String sendurl, String data, int timeout) {
		
		
		
		RequestConfig defaultRequestConfig = RequestConfig.custom()
				  .setSocketTimeout(timeout)   
				  .setConnectTimeout(timeout)  //连接超时时间(单位：毫秒) 
				  .setConnectionRequestTimeout(timeout)
				  .build();
		
		
		CloseableHttpClient client = HttpClients.custom()
			    .setDefaultRequestConfig(defaultRequestConfig)
			    .build();
		HttpPost post = new HttpPost(sendurl);
		StringEntity myEntity = new StringEntity(data, ContentType.APPLICATION_JSON);// 构造请求数据
		post.setEntity(myEntity);		// 设置请求体
		String responseContent = null; 	// 响应内容
		CloseableHttpResponse response = null;
		try {
			response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				responseContent = EntityUtils.toString(entity, "UTF-8");
			}
		} catch (ClientProtocolException e) {
			LOG.error("HttpClient异常: {}", e.getMessage(), e);
		} catch (IOException e) {
			LOG.error("HttpClient异常: {}", e.getMessage(), e);
		} finally {
			try {
				if (response != null)
					response.close();

			} catch (IOException e) {
				LOG.error("HttpClient异常: {}", e.getMessage(), e);
			} finally {
				try {
					if (client != null)
						client.close();
				} catch (IOException e) {
					LOG.error("HttpClient异常: {}", e.getMessage(), e);
				}
			}
		}
		return responseContent;
	}

}
