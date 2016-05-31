package com.hc.xiaobairent.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hc.xiaobairent.R;
import com.hc.xiaobairent.model.RepayItemModel;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MyRepayAdapter extends BaseAdapter{
	
	private Context mContext;
	private List<RepayItemModel> mList;

	public MyRepayAdapter(Context context, List<RepayItemModel> list) {
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.zf_item_myrepay, null);
			ViewUtils.inject(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.tvTitle.setText(mList.get(position).getName());
		
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
//		String tvTime = sdf.format(mList.get(position).getTime());
		viewHolder.tvTime.setText(mList.get(position).getTime());
		
		String count = mList.get(position).getCount();
		String residue_count = mList.get(position).getResidue_count();
		String tvCount = "第<font color='#eb4555'>"+count+"</font>次房租（剩余<font color='#eb4555'>"+residue_count+"</font>次）";
		viewHolder.tvCount.setText(Html.fromHtml(tvCount));
		
		String price = mList.get(position).getMoney();
		String tvPriceString = "<font color='#eb4555'>"+price + "</font>";
		viewHolder.tvPrice.setText(Html.fromHtml(tvPriceString));
		
		return convertView;
	}
	
	class ViewHolder {
		
		@ViewInject(R.id.tv_title)
		TextView tvTitle;
		@ViewInject(R.id.tv_time)
		TextView tvTime;
		@ViewInject(R.id.tv_count)
		TextView tvCount;
		@ViewInject(R.id.tv_price)
		TextView tvPrice;
	}

}
