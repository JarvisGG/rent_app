package com.hc.xiaobairent.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hc.xiaobairent.R;
import com.hc.xiaobairent.model.RegionModel;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class RegionAdapter extends BaseAdapter{
	
	private List<RegionModel> mList;
	private Context mContext;
	
	public RegionAdapter(Context context, List<RegionModel> list) {
		this.mContext = context;
		this.mList = list;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.zf_item_area, null);
			ViewUtils.inject(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.areaName.setText(mList.get(position).getLocal_name());
		
		return convertView;
	}
	
	class ViewHolder {
		@ViewInject(R.id.area_name)
		private TextView areaName;
	}
	
}
