package com.hc.xiaobairent.model;

import java.util.List;

public class MyEntrustModel {
	private List<MyEntrustItemModel> items;
	private MetaModel _meta;
	public List<MyEntrustItemModel> getItems() {
		return items;
	}
	public void setItems(List<MyEntrustItemModel> items) {
		this.items = items;
	}
	public MetaModel get_meta() {
		return _meta;
	}
	public void set_meta(MetaModel _meta) {
		this._meta = _meta;
	}
	
}
