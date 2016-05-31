package com.hc.xiaobairent.model;

import java.util.List;

public class HouseDetailModel {
	private String address;// "洒洒的",
	private String area;// "1234.00",
	private String company;// "自带物业",
	private String describe;// "阿斯达三毒",
	private List<HouseModel> house;// [
	private String house_type;// "写字楼",
	private String id;// 2,
	private List<UrlModel> img;// [
	private String latitude;// 178.234343,
	private String longitude;// 21.234343,
	private String num;// 7,
	private String park_name;// "绿景国际",
	private String price;// "9.12",

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public List<HouseModel> getHouse() {
		return house;
	}

	public void setHouse(List<HouseModel> house) {
		this.house = house;
	}

	public String getHouse_type() {
		return house_type;
	}

	public void setHouse_type(String house_type) {
		this.house_type = house_type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<UrlModel> getImg() {
		return img;
	}

	public void setImg(List<UrlModel> img) {
		this.img = img;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getPark_name() {
		return park_name;
	}

	public void setPark_name(String park_name) {
		this.park_name = park_name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}
