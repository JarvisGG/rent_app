package com.hc.xiaobairent.adapter;

import java.util.List;

import org.kymjs.kjframe.KJBitmap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hc.xiaobairent.R;
import com.hc.xiaobairent.bean.MyCollectBean;
import com.hc.xiaobairent.model.MyCollectionItemModel;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 
 * @author xiaofei
 *
 */
public class MyCollectAdapter extends BaseAdapter{
	
	private Context context;
	private List<MyCollectionItemModel> mList;
	
	public MyCollectAdapter(Context context, List<MyCollectionItemModel> list) {
		this.context = context;
		this.mList = list;
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public Object getItem(int position) {
		return mList.get(position);
		
	};
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.zf_item_mycollect, null);
			ViewUtils.inject(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.mTitle.setText(mList.get(position).getPrice());
		viewHolder.mContent.setText(mList.get(position).getArea());
		viewHolder.mAddress.setText(mList.get(position).getHouse_type());
		viewHolder.mtype.setText(mList.get(position).getPark_name());
		if(mList.get(position).getImg() != "") {
			KJBitmap bitmap = new KJBitmap();
			bitmap.display(viewHolder.mImageView, mList.get(position).getImg());
		}
		
		return convertView;
	}
	
	class ViewHolder {
		@ViewInject(R.id.tv_icon)
		private ImageView mImageView;
		@ViewInject(R.id.tv_title)
		private TextView mTitle;
		@ViewInject(R.id.tv_content)
		private TextView mContent;
		@ViewInject(R.id.tv_type)
		private TextView mtype;
		@ViewInject(R.id.tv_address)
		private TextView mAddress;
	}
}
