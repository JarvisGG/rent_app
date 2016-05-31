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
import android.widget.TextView;

public class ImageTitleSubtitleInfoAdapter extends BaseAdapter {

	private Context mContext;
	private List<HouseModel> list;
	private AbImageLoader bitmap;

	public ImageTitleSubtitleInfoAdapter(Context context, List<HouseModel> list) {
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
			view = LayoutInflater.from(mContext).inflate(R.layout.list_item_img_title_subtitle_info, parent, false);
			holder.pic = (ImageView) view.findViewById(R.id.pic);
			holder.title = (TextView) view.findViewById(R.id.title);
			holder.subtitle = (TextView) view.findViewById(R.id.subtitle);
			holder.summary = (TextView) view.findViewById(R.id.summary);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		bitmap.display(holder.pic, list.get(position).getImg());
		holder.title.setText(list.get(position).getHouse_name());
		holder.subtitle.setText(list.get(position).getHouse_name());
		holder.summary
				.setText("租房日期:" + list.get(position).getStart_time() + "\t至\t" + list.get(position).getEnd_time());
		return view;
	}

	class ViewHolder {
		private ImageView pic;
		private TextView title;
		private TextView subtitle;
		private TextView summary;
	}

}
