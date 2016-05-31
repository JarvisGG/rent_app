package com.hc.xiaobairent.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hc.xiaobairent.R;
import com.hc.xiaobairent.model.HouseSizeItemModel;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class HouseSizeAdapter extends BaseAdapter {

	private Context mContext;
	private List<HouseSizeItemModel> mList;
	
	public HouseSizeAdapter(Context context, List<HouseSizeItemModel> list) {
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
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.zf_item_size, null);
			ViewUtils.inject(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tvSize.setText(mList.get(position).getName());
		return convertView;
	}
	
	class ViewHolder {
		@ViewInject(R.id.size_name)
		private TextView tvSize;
	}
	
}
