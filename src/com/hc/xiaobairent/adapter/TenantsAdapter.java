package com.hc.xiaobairent.adapter;

import java.util.List;

import com.ab.image.AbImageLoader;
import com.hc.xiaobairent.R;
import com.hc.xiaobairent.model.HouseModel;
import com.hc.xiaobairent.model.TenantModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TenantsAdapter extends BaseAdapter {

	private Context mContext;
	private List<TenantModel> list;

	public TenantsAdapter(Context context, List<TenantModel> list) {
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
			view = LayoutInflater.from(mContext).inflate(R.layout.list_item_tenant, parent, false);
			holder.name_tv = (TextView) view.findViewById(R.id.name_tv);
			holder.phone_tv = (TextView) view.findViewById(R.id.phone_tv);
			holder.house_name_tv = (TextView) view.findViewById(R.id.house_name_tv);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.name_tv.setText(list.get(position).getUser_name());
		holder.phone_tv.setText(list.get(position).getTel());
		holder.house_name_tv.setText(list.get(position).getHouse_name());
		return view;
	}

	class ViewHolder {
		private TextView name_tv;
		private TextView phone_tv;
		private TextView house_name_tv;
	}

}
