package com.hc.core.vote;

import java.util.List;

import org.kymjs.kjframe.ui.AnnotateUtil;
import org.kymjs.kjframe.ui.BindView;

import com.hc.core.view.CountDownTextView;
import com.hc.core.vote.VoteListModel.VoteListItemModel;
import com.hc.xiaobairent.R;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class EmpolyeeAdapter extends BaseAdapter {

	private Context context;
	private List<VoteListItemModel> list;

	public EmpolyeeAdapter(Context context, List<VoteListItemModel> list) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.hc_item_lv_vote, null);
			holder = new ViewHolder();
			AnnotateUtil.initBindView(holder, convertView);//绑定view
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title.setText(list.get(position).getTitle());
		holder.timedowntextview.setTimesByEndTime(list.get(position)
				.getEnd_time());
		if (!holder.timedowntextview.isRun()) {
			holder.timedowntextview.run();
		}
		holder.jion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				context.startActivity(new Intent(context, VoteDetails.class));
			}
		});
		return convertView;
	}

	private class ViewHolder {
		@BindView(id = R.id.title)
		TextView title;
		@BindView(id = R.id.timedowntextview)
		CountDownTextView timedowntextview;
		@BindView(id = R.id.jion)
		Button jion;
	}

}
