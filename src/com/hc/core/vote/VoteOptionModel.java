package com.hc.core.vote;

public class VoteOptionModel {

	private boolean selected = false;
	private String option_id;
	private String title;

	public String getOption_id() {
		return option_id;
	}

	public void setOption_id(String option_id) {
		this.option_id = option_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isSelected() {
		// TODO Auto-generated method stub
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
