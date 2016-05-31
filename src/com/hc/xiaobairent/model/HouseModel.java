package com.hc.xiaobairent.model;

import java.util.List;

public class HouseModel {
	/** 不支持分期 */
	public static final int NO = 0;
	/** 支持分期 */
	public static final int YES = 1;
	private String address;// "洒洒的",
	private String area;// "1234.00",
	private String charge;// "1234.00",
	private String city_id;// "53",
	private String company;// "自带物业",
	private List<ImgPathModel> contract;// ""
	private String created_time;// "1461561016",
	private String current_num;// 3,
	private String day_price;// "6.39",
	private String describe;// "阿斯达三毒",
	private String district_id;// "518",
	private String end_time;// "",
	private String house_id;// "鼎都商务楼2",
	private String house_name;// "鼎都商务楼2",
	private String house_type;// "写字楼",
	private String id;// "2",
	private String img;// "http://handone.oss-cn-shanghai.aliyuncs.com/6eadf06df548da71e75e5a862348977e/e758b3db72af7b8748bab259d9fc27ce/14617329789536.jpg",
	private String is_recommend;// "1",
	private String latitude;// "178.234343",
	private String longitude;// "21.234343",
	private String mobile;// "",
	private String mode;// "http://handone.oss-cn-shanghai.aliyuncs.com/6eadf06df548da71e75e5a862348977e/e758b3db72af7b8748bab259d9fc27ce/14617329789536.jpg",
	private String num;// 1
	private String park_name;// "绿景国际",
	private String price;// "9.12",
	private String province_id;// "4",
	private String redecorate;// "2",
	private String stage_num;// 12,
	private String start_time;// "",
	private String username;// "",
	private int is_fenqi;// [int] 是否分期（0不分期 1分期）
	private int is_fav;// int 0,1 是否收藏

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

	public String getCharge() {
		return charge;
	}

	public void setCharge(String charge) {
		this.charge = charge;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public List<ImgPathModel> getContract() {
		return contract;
	}

	public void setContract(List<ImgPathModel> contract) {
		this.contract = contract;
	}

	public String getCreated_time() {
		return created_time;
	}

	public void setCreated_time(String created_time) {
		this.created_time = created_time;
	}

	public String getCurrent_num() {
		return current_num;
	}

	public void setCurrent_num(String current_num) {
		this.current_num = current_num;
	}

	public String getDay_price() {
		return day_price;
	}

	public void setDay_price(String day_price) {
		this.day_price = day_price;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getDistrict_id() {
		return district_id;
	}

	public void setDistrict_id(String district_id) {
		this.district_id = district_id;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getHouse_id() {
		return house_id;
	}

	public void setHouse_id(String house_id) {
		this.house_id = house_id;
	}

	public String getHouse_name() {
		return house_name;
	}

	public void setHouse_name(String house_name) {
		this.house_name = house_name;
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

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getIs_recommend() {
		return is_recommend;
	}

	public void setIs_recommend(String is_recommend) {
		this.is_recommend = is_recommend;
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
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

	public String getProvince_id() {
		return province_id;
	}

	public void setProvince_id(String province_id) {
		this.province_id = province_id;
	}

	public String getRedecorate() {
		return redecorate;
	}

	public void setRedecorate(String redecorate) {
		this.redecorate = redecorate;
	}

	public String getStage_num() {
		return stage_num;
	}

	public void setStage_num(String stage_num) {
		this.stage_num = stage_num;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getIs_fenqi() {
		return is_fenqi;
	}

	public void setIs_fenqi(int is_fenqi) {
		this.is_fenqi = is_fenqi;
	}

	public int getIs_fav() {
		return is_fav;
	}

	public void setIs_fav(int is_fav) {
		this.is_fav = is_fav;
	}

}
