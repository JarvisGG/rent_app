package com.hc.xiaobairent.model;

import java.util.List;

public class RepaymentModel {
	private List<RepayItemModel> items; // [
	private RepayLinkModel _links; // 
	private MetaModel _meta; //
	
	public List<RepayItemModel> getItems() {
		return items;
	}
	public void setItems(List<RepayItemModel> items) {
		this.items = items;
	}
	public RepayLinkModel get_links() {
		return _links;
	}
	public void set_links(RepayLinkModel _links) {
		this._links = _links;
	}
	public MetaModel get_meta() {
		return _meta;
	}
	public void set_meta(MetaModel _meta) {
		this._meta = _meta;
	}
}
