package com.hc.xiaobairent.model;

import java.util.List;

public class MyCollectionModel {
	private List<MyCollectionItemModel> items;
	private MetaModel _meta;
	public List<MyCollectionItemModel> getItems() {
		return items;
	}
	public void setItems(List<MyCollectionItemModel> items) {
		this.items = items;
	}
	public MetaModel get_meta() {
		return _meta;
	}
	public void set_meta(MetaModel _meta) {
		this._meta = _meta;
	}
	
}
