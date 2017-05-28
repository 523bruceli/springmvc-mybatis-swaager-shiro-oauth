package com.hjzgg.common.util.common;

import java.util.Date;

public class LngAndLat {
	
	private String nurseId;
	
	private String nurseName;
	
	private double longitude;//经度

	private double latitude;//纬度
	
	private Date locationtime;//时间
	
	public String getNurseId() {
		return nurseId;
	}

	public void setNurseId(String nurseId) {
		this.nurseId = nurseId;
	}

	public String getNurseName() {
		return nurseName;
	}

	public void setNurseName(String nurseName) {
		this.nurseName = nurseName;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public Date getLocationtime() {
		return locationtime;
	}

	public void setLocationtime(Date locationtime) {
		this.locationtime = locationtime;
	}
	
	
	
	
}
