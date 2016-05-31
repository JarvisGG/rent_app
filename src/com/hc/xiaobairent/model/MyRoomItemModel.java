package com.hc.xiaobairent.model;


public class MyRoomItemModel {
	private int id;
	private String img;
	private String house_name;
	private String start_time;
	private String end_time;
	private String is_sale;
	
	public String getIs_sale() {
		return is_sale;
	}
	public void setIs_sale(String is_sale) {
		this.is_sale = is_sale;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getHouse_name() {
		return house_name;
	}
	public void setHouse_name(String house_name) {
		this.house_name = house_name;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
}
