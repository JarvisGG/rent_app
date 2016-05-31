package com.hc.xiaobairent.bean;

import java.util.Date;

import android.R.integer;

public class MyRepayBean {
	private String name;
	private String housing;
	private int count;
	private int residue_count;
	private int money;
	private Date time;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHousing() {
		return housing;
	}
	public void setHousing(String housing) {
		this.housing = housing;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getResidue_count() {
		return residue_count;
	}
	public void setResidue_count(int residue_count) {
		this.residue_count = residue_count;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
}
