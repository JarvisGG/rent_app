package com.hc.xiaobairent.utils;

import java.util.List;

public class CityModel {
	private int id;
	private String region_name;
	private short region_type;
	private List<CityModel> son;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRegion_name() {
		return region_name;
	}

	public void setRegion_name(String region_name) {
		this.region_name = region_name;
	}

	public short getRegion_type() {
		return region_type;
	}

	public void setRegion_type(short region_type) {
		this.region_type = region_type;
	}

	public List<CityModel> getSon() {
		return son;
	}

	public void setSon(List<CityModel> son) {
		this.son = son;
	}

}
