package com.hc.xiaobairent.model;

import java.util.List;

public class MessageModel {
	private List<MessageItemModel> items;
	private MetaModel _meta;

	public List<MessageItemModel> getItems() {
		return items;
	}

	public void setItems(List<MessageItemModel> items) {
		this.items = items;
	}

	public MetaModel get_meta() {
		return _meta;
	}

	public void set_meta(MetaModel _meta) {
		this._meta = _meta;
	}
	
	
}
