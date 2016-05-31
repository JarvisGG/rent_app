package com.hc.xiaobairent.adapter;

import java.util.List;

import com.hc.xiaobairent.R;
import com.hc.xiaobairent.model.IncomeModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class IncomeAdapter extends BaseAdapter {

	private Context mContext;
	private List<IncomeModel> list;

	public IncomeAdapter(Context context, List<IncomeModel> list) {
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
			view = LayoutInflater.from(mContext).inflate(R.layout.list_item_income_detail, parent, false);
			holder.park_name_tv = (TextView) view.findViewById(R.id.park_name_tv);
			holder.time_tv = (TextView) view.findViewById(R.id.time_tv);
			holder.income_tv = (TextView) view.findViewById(R.id.income_tv);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.park_name_tv.setText(list.get(position).getHouse_name() + "");
		holder.time_tv.setText(list.get(position).getTime() + "");
		holder.income_tv.setText(list.get(position).getCharge() + "");
		return view;
	}

	class ViewHolder {
		private TextView park_name_tv;
		private TextView time_tv;
		private TextView income_tv;
	}

}
