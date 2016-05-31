package com.hc.xiaobairent.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.bumptech.glide.Glide;
import com.hc.core.utils.SharedpfTools;
import com.hc.core.utils.Sign;
import com.hc.core.utils.UrlConnector;
import com.hc.xiaobairent.R;
import com.hc.xiaobairent.model.MyRoomItemModel;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MyRoomAdapter extends BaseAdapter implements OnScrollListener{
	
	private Context mContext;
	private List<MyRoomItemModel> mList;
	private ListView mListView;
	private AbHttpUtil httpUtil;
	private SharedpfTools sharedpfTools;
	private Sign sign;
	private int mFlag;
	
	private static final int UP = 1;
	private static final int DOWN = 0;

	public MyRoomAdapter(Context context, List<MyRoomItemModel> list, ListView listView, int flag) {
		this.mContext = context;
		this.mList = list;
		mListView = listView;
		mListView.setOnScrollListener(this);
		httpUtil = AbHttpUtil.getInstance(mContext);
		sharedpfTools = SharedpfTools.getInstance(mContext);
		sign = new Sign(mContext);
		mFlag = flag;
		
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
		if(convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.zf_item_myroom, null);
			ViewUtils.inject(viewHolder, convertView);
			convertView.setTag(R.id.my_rooms_item, viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag(R.id.my_rooms_item);
		}
		
		//KJBitmap bitmap = new KJBitmap();
		viewHolder.tvTitle.setText(mList.get(position).getHouse_name());
		viewHolder.tvContent.setText("租房日期: "+mList.get(position).getStart_time()
				+"至"+mList.get(position).getEnd_time());
		if(mList.get(position).getImg() != "") {
		//	bitmap.display(viewHolder.mImageView, mList.get(position).getImg(), 64, 64);
			Glide.with(mContext)
				.load(mList.get(position).getImg())
				.thumbnail(0.1f)
				.into(viewHolder.tvIcon);
		}
		if (mFlag == 1) {
			viewHolder.listControl.setVisibility(View.VISIBLE);
			if(Integer.parseInt(mList.get(position).getIs_sale()) == 1) {
				viewHolder.refundBtn.setBackgroundResource(R.drawable.rental_btn2);
				viewHolder.refundBtn.setText("下架");
				viewHolder.refundBtn.setTextColor(Color.parseColor("#eb4555"));
			} else {
				viewHolder.refundBtn.setBackgroundResource(R.drawable.rental_btn1);
				viewHolder.refundBtn.setText("上架");
				viewHolder.refundBtn.setTextColor(Color.parseColor("#1fa5e4"));
			}
			
			viewHolder.refundBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(Integer.parseInt(mList.get(position).getIs_sale()) == 1) {
						changeData(DOWN, mList.get(position).getId(), ((Button)v), position);
					} else {
						changeData(UP, mList.get(position).getId(), ((Button)v), position);
					}
					
				}
			});
			
		} else {
			viewHolder.listControl.setVisibility(View.GONE);
		}
		
		
		return convertView;
	}
	


	private void changeData(int command, int position, final Button view, final int curposition) {
		AbRequestParams params = new AbRequestParams();
		sign.init();
		params.put("id", position);
		params.put("is_sale", command+"");
		sign.addParam("id", position);
		sign.addParam("is_sale", command);
		String url = UrlConnector.MYROOM_UP_DOWN + sharedpfTools.getAccessToken() + UrlConnector.SIGN + sign.getSign();
		httpUtil.post(url, params, new AbStringHttpResponseListener() {
			
			@Override
			public void onStart() {
				
			}
			
			@Override
			public void onFinish() {
				
			}
			
			@Override
			public void onFailure(int statusCode, String content, Throwable error) {
				
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				Log.e("myroomcontrol", content);
				try {
					JSONObject object = new JSONObject(content);
					switch (Integer.parseInt(object.getString("is_sale"))) {
					case UP:
						Toast.makeText(mContext, "上架成功", Toast.LENGTH_SHORT).show();
						view.setBackgroundResource(R.drawable.rental_btn2);
						view.setText("下架");
						view.setTextColor(Color.parseColor("#eb4555"));
						mList.get(curposition).setIs_sale(1+"");
						break;
					case DOWN:
						Toast.makeText(mContext, "下架成功", Toast.LENGTH_SHORT).show();
						view.setBackgroundResource(R.drawable.rental_btn1);
						view.setText("上架");
						view.setTextColor(Color.parseColor("#1fa5e4"));
						mList.get(curposition).setIs_sale(0+"");
						break;
					default:
						break;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				};
			}
		});
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) {
		    // 加载可见项,开始所有Task
			Glide.with(mContext).resumeRequests();
		} else {
		    // 停止任务，暂停所有Task
			Glide.with(mContext).pauseRequests();;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	} 
	
	class ViewHolder {
		
		@ViewInject(R.id.refund_btn)
		Button refundBtn;
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
		@ViewInject(R.id.room_list_control)
		RelativeLayout listControl;
	}

}
