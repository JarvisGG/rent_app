package com.hc.xiaobairent.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hc.xiaobairent.R;
import com.hc.xiaobairent.model.MyEntrustItemModel;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MyEntrustAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<MyEntrustItemModel> mList;

	public MyEntrustAdapter(Context context, List<MyEntrustItemModel> list) {
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.zf_item_deputelookroom, null);
			ViewUtils.inject(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.titleText.setText(mList.get(position).getProvince_name()+"-"+
				mList.get(position).getCity_name()+"-"+mList.get(position).getDistrict_name());
		viewHolder.contentText.setText(mList.get(position).getRemark());
		viewHolder.timeText.setText(mList.get(position).getCreated_time());
		
		return convertView;
	}
	
	class ViewHolder {
		@ViewInject(R.id.tv_title)
		TextView titleText;
		@ViewInject(R.id.tv_content)
		TextView contentText;
		@ViewInject(R.id.tv_time)
		TextView timeText;
	}
	

}
