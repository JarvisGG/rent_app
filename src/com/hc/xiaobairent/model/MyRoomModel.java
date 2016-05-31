package com.hc.xiaobairent.model;

import java.util.List;

public class MyRoomModel {
	private List<MyRoomItemModel> items;
	private MetaModel _meta;
	public List<MyRoomItemModel> getItems() {
		return items;
	}
	public void setItems(List<MyRoomItemModel> items) {
		this.items = items;
	}
	public MetaModel get_meta() {
		return _meta;
	}
	public void set_meta(MetaModel _meta) {
		this._meta = _meta;
	}
	
}
