package com.hc.xiaobairent.adapter;

import java.util.List;

import com.hc.xiaobairent.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HistoryAdapter extends BaseAdapter {

	private Context mContext;
	private List<String> list;

	public HistoryAdapter(Context context, List<String> list) {
		this.mContext = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder = null;
		if (position == list.size() - 1) {
			if (view == null) {
				holder = new ViewHolder();
				view = LayoutInflater.from(mContext).inflate(R.layout.list_item_stroke_button, parent, false);
				holder.textview = (TextView) view.findViewById(R.id.textview);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
		} else {
			if (view == null) {
				holder = new ViewHolder();
				view = LayoutInflater.from(mContext).inflate(R.layout.list_item_textview, parent, false);
				holder.textview = (TextView) view.findViewById(R.id.textview);
				if (position == 0) {
					holder.textview.setTextColor(mContext.getResources().getColor(R.color.grayfont));
					holder.textview.setBackgroundColor(mContext.getResources().getColor(R.color.white));
				}
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
		}
		holder.textview.setText(list.get(position));
		return view;
	}

	class ViewHolder {
		private TextView textview;
	}

}
