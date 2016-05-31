package com.hc.xiaobairent.adapter;

import java.util.List;

import com.hc.xiaobairent.R;
import com.hc.xiaobairent.model.IdNameModel;
import com.hc.xiaobairent.utils.CityModel;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class IdNameAdapter extends BaseAdapter {
	private Context mContext;
	private List<IdNameModel> list;
	private int gravity;

	public IdNameAdapter(Context context, List<IdNameModel> list, int gravity) {
		this.mContext = context;
		this.list = list;
		this.gravity = gravity;
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
			holder.textview.setGravity(gravity);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.textview.setText(list.get(position).getName());
		return view;
	}

	class ViewHolder {
		private TextView textview;
	}

}
