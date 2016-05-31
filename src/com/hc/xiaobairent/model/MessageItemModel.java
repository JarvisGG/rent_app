package com.hc.xiaobairent.model;

import java.util.List;

public class MessageItemModel {
	private int id;
	private String title;
	private String content;
	private MessageUser user;
	private int relatedid;
	private String add_time;
	private String read;
	private int type;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public MessageUser getUser() {
		return user;
	}
	public void setUser(MessageUser user) {
		this.user = user;
	}
	public int getRelatedid() {
		return relatedid;
	}
	public void setRelatedid(int relatedid) {
		this.relatedid = relatedid;
	}
	public String getAdd_time() {
		return add_time;
	}
	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}
	public String getRead() {
		return read;
	}
	public void setRead(String read) {
		this.read = read;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

}
