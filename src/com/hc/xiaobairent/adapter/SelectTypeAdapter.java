package com.hc.xiaobairent.adapter;

import java.util.List;

import com.hc.xiaobairent.R;
import com.hc.xiaobairent.utils.CityModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SelectTypeAdapter extends BaseAdapter {

	private Context mContext;
	private List<CityModel> list;

	public SelectTypeAdapter(Context context, List<CityModel> list) {
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
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.list_item_select, parent, false);
			holder.textview = (TextView) view.findViewById(R.id.textview);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.textview.setText(list.get(position).getRegion_name());
		return view;
	}

	class ViewHolder {
		private TextView textview;
	}

}
