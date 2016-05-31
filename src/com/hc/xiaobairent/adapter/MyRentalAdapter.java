package com.hc.xiaobairent.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ab.image.AbImageLoader;
import com.bumptech.glide.Glide;
import com.hc.xiaobairent.R;
import com.hc.xiaobairent.activity.ZfForRenewActivity;
import com.hc.xiaobairent.activity.ZfForRentActivity;
import com.hc.xiaobairent.model.MyRentalItemModel;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MyRentalAdapter extends BaseAdapter implements OnScrollListener {

	private Context mContext;
	private List<MyRentalItemModel> mList;

	public static String[] URLS;
	private boolean mFirstIn;
	private int mStart, mEnd;
	private ListView mListView;
	private AbImageLoader bitmap;

	public MyRentalAdapter(Context context, List<MyRentalItemModel> list, ListView listView) {
		this.mContext = context;
		this.mList = list;
		this.mListView = listView;
		bitmap = AbImageLoader.getInstance(context);
		URLS = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			URLS[i] = list.get(i).getImg();
		}
		mFirstIn = true;
		mListView.setOnScrollListener(this);
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.zf_item_myrental, null);
			ViewUtils.inject(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.tvTitle.setText(mList.get(position).getHouse_name());
		String content = "租房日期：" + mList.get(position).getStart_time() + " 至 " + mList.get(position).getEnd_time();
		viewHolder.tvContent.setText(content);
		viewHolder.tvPrice.setText(mList.get(position).getPrice() + "");
		viewHolder.tvType.setText(mList.get(position).getMode());
		if (mList.get(position).getImg() != "") {
			//bitmap.display(viewHolder.tvIcon, mList.get(position).getImg());
			Glide.with(mContext)
			.load(mList.get(position).getImg())
			.thumbnail(0.1f)
			.into(viewHolder.tvIcon);
		}

		viewHolder.refundBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				refundMethod(mList.get(position).getId(), mList.get(position).getHouse_name(),
						mList.get(position).getStart_time(), mList.get(position).getEnd_time());

			}
		});

		viewHolder.renewBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				renewMethod(mList.get(position).getId(), mList.get(position).getHouse_name(),
						mList.get(position).getStart_time(), mList.get(position).getEnd_time());
			}
		});
		return convertView;
	}

	private void refundMethod(int id, String house, String house_begin, String house_end) {
		Intent intent = new Intent();
		intent.putExtra("id", id);
		intent.putExtra("house", house);
		intent.putExtra("house_begin", house_begin);
		intent.putExtra("house_end", house_end);
		intent.setClass(mContext, ZfForRentActivity.class);
		mContext.startActivity(intent);
		((Activity) mContext).overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
	}

	private void renewMethod(int id, String house, String house_begin, String house_end) {
		Intent intent = new Intent();
		intent.putExtra("id", id);
		intent.putExtra("house", house);
		intent.putExtra("house_begin", house_begin);
		intent.putExtra("house_end", house_end);
		intent.setClass(mContext, ZfForRenewActivity.class);
		mContext.startActivity(intent);
		((Activity) mContext).overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
	}

	class ViewHolder {

		@ViewInject(R.id.refund_btn)
		Button refundBtn;
		@ViewInject(R.id.renew_btn)
		Button renewBtn;
		@ViewInject(R.id.tv_icon)
		ImageView tvIcon;
		@ViewInject(R.id.tv_title)
		TextView tvTitle;
		@ViewInject(R.id.tv_content)
		TextView tvContent;
		@ViewInject(R.id.tv_price)
		TextView tvPrice;
		@ViewInject(R.id.tv_type)
		TextView tvType;

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		 if (scrollState == SCROLL_STATE_IDLE) {
		 // 停止滑动，恢复加载
		 Glide.with(mContext).resumeRequests();
		 } else {
		 // 滑动过程中停止加载
		 Glide.with(mContext).pauseRequests();
		 }
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

		// mStart = firstVisibleItem;
		// mEnd = firstVisibleItem + visibleItemCount;
		// if (mFirstIn = true && visibleItemCount > 0) {
		// mImageLoader.loadImages(mStart, mEnd);
		// mFirstIn = false;
		// }
	}

}
