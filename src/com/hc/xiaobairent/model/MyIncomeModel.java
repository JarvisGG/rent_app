package com.hc.xiaobairent.model;

public class MyIncomeModel {
	// {"each_month":[0,0,0,0,0,0,0,0,0,0,0,0],"count":0,"month_count":0}

	private float[] each_month;// 775,
	private String count;// 775,
	private String month_count;// 55

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getMonth_count() {
		return month_count;
	}

	public void setMonth_count(String month_count) {
		this.month_count = month_count;
	}

	public float[] getEach_month() {
		return each_month;
	}

	public void setEach_month(float[] each_month) {
		this.each_month = each_month;
	}
}
