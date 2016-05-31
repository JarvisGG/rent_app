package com.hc.core.vote;

import java.util.List;

public class VoteListModel {
	private String page_num;// 1,
	private String page_size;// 20,
	private String count;// 2,
	private List<VoteListItemModel> list;// [

	public String getPage_num() {
		return page_num;
	}

	public void setPage_num(String page_num) {
		this.page_num = page_num;
	}

	public String getPage_size() {
		return page_size;
	}

	public void setPage_size(String page_size) {
		this.page_size = page_size;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public List<VoteListItemModel> getList() {
		return list;
	}

	public void setList(List<VoteListItemModel> list) {
		this.list = list;
	}

	class VoteListItemModel {
		private String vote_id;// "15",
		private String title;// "你的兴趣和工作有多大关系",
		private String sort_order;// "2",
		private String add_time;// "2015-10-23 14:13:50",
		private String start_time;// "2015-10-23 00:00:00",
		private String end_time;// "2015-10-31 00:00:00"

		public String getVote_id() {
			return vote_id;
		}

		public void setVote_id(String vote_id) {
			this.vote_id = vote_id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getSort_order() {
			return sort_order;
		}

		public void setSort_order(String sort_order) {
			this.sort_order = sort_order;
		}

		public String getAdd_time() {
			return add_time;
		}

		public void setAdd_time(String add_time) {
			this.add_time = add_time;
		}

		public String getStart_time() {
			return start_time;
		}

		public void setStart_time(String start_time) {
			this.start_time = start_time;
		}

		public String getEnd_time() {
			return end_time;
		}

		public void setEnd_time(String end_time) {
			this.end_time = end_time;
		}
	}

}
