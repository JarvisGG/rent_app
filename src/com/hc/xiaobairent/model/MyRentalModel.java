package com.hc.xiaobairent.model;

import java.util.List;

public class MyRentalModel {

	private List<MyRentalItemModel> items;
	private MetaModel _meta;
	
	public MetaModel get_meta() {
		return _meta;
	}

	public void set_meta(MetaModel _meta) {
		this._meta = _meta;
	}

	public List<MyRentalItemModel> getItems() {
		return items;
	}

	public void setItems(List<MyRentalItemModel> items) {
		this.items = items;
	}
}
