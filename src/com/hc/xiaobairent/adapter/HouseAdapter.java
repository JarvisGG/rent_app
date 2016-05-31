package com.hc.xiaobairent.adapter;

import java.util.List;

import com.ab.image.AbImageLoader;
import com.hc.xiaobairent.R;
import com.hc.xiaobairent.model.HouseModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HouseAdapter extends BaseAdapter {

	private Context mContext;
	private List<HouseModel> list;
	private AbImageLoader bitmap;

	public HouseAdapter(Context context, List<HouseModel> list) {
		this.mContext = context;
		this.list = list;
		bitmap = AbImageLoader.getInstance(context);
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
			view = LayoutInflater.from(mContext).inflate(R.layout.list_item_house_img_bg, parent, false);
			holder.all_rl = (RelativeLayout) view.findViewById(R.id.all_rl);
			holder.pic_iv = (ImageView) view.findViewById(R.id.pic_iv);
			holder.info_tv = (TextView) view.findViewById(R.id.info_tv);
			holder.title_tv = (TextView) view.findViewById(R.id.title_tv);
			holder.num_tv = (TextView) view.findViewById(R.id.num_tv);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		bitmap.display(holder.pic_iv, list.get(position).getImg());
		holder.info_tv.setText(list.get(position).getAddress() + "\t\t\t\t" + list.get(position).getPrice() + "元/㎡·天");
		holder.title_tv.setText(list.get(position).getPark_name());
		holder.num_tv.setText(list.get(position).getNum() + "套正在出租");
		return view;
	}

	class ViewHolder {
		private ImageView pic_iv;
		private TextView info_tv;
		private TextView title_tv;
		private TextView num_tv;
		private RelativeLayout all_rl;
	}

}
