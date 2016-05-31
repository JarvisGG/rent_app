package com.hc.xiaobairent.model;

import java.util.List;

public class IndexModel {
	private String num;// 1,
	private List<AdsModel> ad;// [
	private List<HouseModel> park1;// [
	private List<HouseModel> park2;// [

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public List<AdsModel> getAd() {
		return ad;
	}

	public void setAd(List<AdsModel> ad) {
		this.ad = ad;
	}

	public List<HouseModel> getPark1() {
		return park1;
	}

	public void setPark1(List<HouseModel> park1) {
		this.park1 = park1;
	}

	public List<HouseModel> getPark2() {
		return park2;
	}

	public void setPark2(List<HouseModel> park2) {
		this.park2 = park2;
	}
}
