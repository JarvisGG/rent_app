package com.hc.xiaobairent.adapter;

import java.util.List;

import com.hc.xiaobairent.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class UploadImageAdapter extends BaseAdapter {

	private Context context;
	private List<String> list;

	public UploadImageAdapter(Context context, List<String> list) {
		this.context = context;
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
			view = LayoutInflater.from(context).inflate(R.layout.list_item_upload_image, parent, false);
			holder.imageview = (ImageView) view.findViewById(R.id.imageview);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		if (position == list.size() - 1) {
			holder.imageview.setImageResource(R.drawable.add_photo);
		} else {
			Bitmap bitmap = BitmapFactory.decodeFile(list.get(position));
			holder.imageview.setImageBitmap(bitmap);
		}
		return view;
	}

	class ViewHolder {
		private ImageView imageview;
	}

}
