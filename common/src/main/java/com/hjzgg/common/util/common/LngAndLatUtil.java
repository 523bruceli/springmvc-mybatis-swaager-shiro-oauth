package com.hjzgg.common.util.common;

import net.sf.json.JSONObject;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 根据地址获得经纬度
 * 
 * @author Administrator
 *
 */
public class LngAndLatUtil {

	/**
	 * 根据地址获得经纬度
	 * 
	 * @param address
	 * @return
	 */
	public static LngAndLat getLngAndLat(String address) {
		LngAndLat latAndLng = new LngAndLat();
		String url = "https://api.map.baidu.com/geocoder/v2/?address=" + address + "&output=json&ak=2i6NaIyF6fP4NOFdLs4I9Fk9";
		String json = loadJSON(url);
		if (StringUtils.isEmpty(json)) {
			return latAndLng;
		}
		int len = json.length();
		// 如果不是合法的json格式
		if (json.indexOf("{") != 0 || json.lastIndexOf("}") != len - 1) {
			return latAndLng;
		}
		JSONObject obj = JSONObject.fromObject(json);
		if (obj.get("status").toString().equals("0")) {
			double lng = obj.getJSONObject("result").getJSONObject("location").getDouble("lng");
			double lat = obj.getJSONObject("result").getJSONObject("location").getDouble("lat");
			latAndLng.setLatitude(lat);
			latAndLng.setLongitude(lng);
		}
		return latAndLng;
	}

	public static String loadJSON(String url) {
		StringBuilder json = new StringBuilder();
		try {
			URL oracle = new URL(url);
			URLConnection yc = oracle.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				json.append(inputLine);
			}
			in.close();
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return json.toString();
	}

	public static void main(String[] args) {
		LngAndLat latAndLng = LngAndLatUtil.getLngAndLat("北京");
		System.out.println("经度：" + latAndLng.getLongitude() + "---纬度：" + latAndLng.getLatitude());
	}
}
