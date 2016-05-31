package com.hc.xiaobairent.utils;

import com.hc.core.utils.SharedpfTools;

import android.content.Context;
import android.text.TextUtils;

public class SearchHistoryUtil {
	private static SharedpfTools sp;
	private static SearchHistoryUtil instance;

	private SearchHistoryUtil(Context context) {
		sp = SharedpfTools.getInstance(context);
		if (TextUtils.isEmpty(sp.getSearchHistory())) {
			sp.setSearchHistory("历史搜索,清空搜索历史");
		}
	}

	public static SearchHistoryUtil getInstance(Context context) {
		instance = new SearchHistoryUtil(context);
		return instance;
	}

	public void add(String content) {
		StringBuffer sb = new StringBuffer(sp.getSearchHistory());
		sb.insert(5, content + ",");
		sp.setSearchHistory(sb.toString());
	}

	public String[] getHistory() {
		return sp.getSearchHistory().split(",");
	}

	public void clear() {
		sp.setSearchHistory("历史搜索,清空搜索历史");
	}
}
