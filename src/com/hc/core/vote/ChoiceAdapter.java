package com.hc.core.vote;

import java.util.List;

import org.kymjs.kjframe.ui.AnnotateUtil;
import org.kymjs.kjframe.ui.BindView;

import com.hc.xiaobairent.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChoiceAdapter extends BaseAdapter {
	private List<VoteOptionModel> list;
	private LayoutInflater inflater;

	public ChoiceAdapter(Context context, List<VoteOptionModel> list) {
		this.list = list;
		inflater = LayoutInflater.from(context);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.hc_choice_list_item,
					parent, false);
			AnnotateUtil.initBindView(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (list.get(position).isSelected()) {//选中
			holder.image.setImageResource(R.drawable.selected_vote);
		} else {
			holder.image.setImageResource(R.drawable.unselected_vote);
		}
		holder.text.setText(list.get(position).getTitle());//标题
		return convertView;
	}

	private class ViewHolder {
		@BindView(id = R.id.image)
		ImageView image;
		@BindView(id = R.id.text)
		TextView text;
	}
}
