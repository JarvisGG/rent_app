package com.hc.xiaobairent.model;

public class PersonSmallInfoModel {
	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	private String t_nickname;
	private String avatar;
	private int verify;
	public String getT_nickname() {
		return t_nickname;
	}
	public void setT_nickname(String t_nickname) {
		this.t_nickname = t_nickname;
	}

	public int getVerify() {
		return verify;
	}
	public void setVerify(int verify) {
		this.verify = verify;
	}
	
}
