package com.hc.xiaobairent.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.ui.BindView;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.hc.core.base.BaseActivity;
import com.hc.core.utils.SharedpfTools;
import com.hc.core.utils.Sign;
import com.hc.core.utils.UrlConnector;
import com.hc.xiaobairent.R;
import com.hc.xiaobairent.adapter.MyMessageAdapter;
import com.hc.xiaobairent.dialog.ListViewDialog.Builder;
import com.hc.xiaobairent.model.MessageItemModel;
import com.hc.xiaobairent.model.MessageModel;

public class ZfMyMessageActivity extends BaseActivity implements OnRefreshListener2<ScrollView>, OnItemClickListener, OnItemLongClickListener{
	// 初始化标题
	@BindView(id = R.id.menu_back, click = true)
	private ImageView menuBack;
	@BindView(id = R.id.menu_title)
	private TextView menuTitle;	
	// 列表刷新
	@BindView(id = R.id.scrollview_more)
	private PullToRefreshScrollView scrollViewMore;
	@BindView(id = R.id.deputelookroom_list)
	private ListView myMessageList;	
	// 消息列表
	private MyMessageAdapter myMessageAdapter;	
	private List<MessageItemModel> mList; 
	// 页码
	private SharedpfTools sharedpfTools = SharedpfTools.getInstance(ZfMyMessageActivity.this);
	private int page = 0;
	private KJHttp http;
	private AbHttpUtil httpUtil;
	private Gson gson;
	private Sign sign;
	private MessageModel messageModel;
	private List<MessageItemModel> list;
	
	// 请求方式: 1为刷新，2为加载更多
	
	private static final int REFRESH = 1;
	private static final int MORE = 2;
	private static final int DELECT = 3;
	
	private int itemPosition;
	
	
	@Override
	public void setRootView() {
		setAbContentView(R.layout.zf_activity_mymessage);
	}
	
	@Override
	public void initData() {
		super.initData();
		mList = new ArrayList<MessageItemModel>();
		scrollViewMore.setMode(Mode.BOTH);
		scrollViewMore.setOnRefreshListener(this);
		myMessageAdapter = new MyMessageAdapter(ZfMyMessageActivity.this, mList);
		myMessageList.setAdapter(myMessageAdapter);
		myMessageList.setOnItemClickListener(this);
		myMessageList.setOnItemLongClickListener(this);
		initTab();
		applyData(REFRESH);	
		
	}
	
	private void initTab() {
		menuTitle.setText("消息中心");
	}
	
	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.menu_back:
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;

		default:
			break;
		}
	}
	// 点击查看详情
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		itemPosition = mList.get(position).getId();
		Intent intent = new Intent();
		intent.putExtra("id", mList.get(position).getId());
		intent.setClass(ZfMyMessageActivity.this, ZfMyMessageDetailActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
		
	}
	// 长按删除
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			final int position, long id) {
		itemPosition = mList.get(position).getId();;
//		Builder builder = new Builder(ZfMyMessageActivity.this);
//		builder.setCancelable(true);
//		builder.setTitle("删除这条消息？");
//		builder.setPositiveButton("确定", new OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				mList.remove(position);
//				myMessageAdapter.notifyDataSetChanged();
//				applyData(DELECT);
//			}
//		});
//		
//		builder.setNegativeButton("取消", new OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				
//			}
//		});
//		
//		AlertDialog alertDialog = builder.create();
//		alertDialog.show();
		
		Builder builder = new Builder(this);
		
		builder.setTitle("删除这条消息？");
		
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mList.remove(position);
				myMessageAdapter.notifyDataSetChanged();
				applyData(DELECT);	
				dialog.dismiss();
			}
		});
		
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();			
			}
		});
		
		builder.create().show();
		return true;
		
	}

	private void applyData(final int command) {
		// 初始化参数 
		sign = new Sign(ZfMyMessageActivity.this);
		sign.init();
		http = new KJHttp();
		httpUtil = AbHttpUtil.getInstance(getApplicationContext());
		gson = new Gson();
		String url = null;
		switch (command) {
		case MORE:
			url = UrlConnector.MYMESSAGE+sharedpfTools.getAccessToken()+UrlConnector.SIGN+sign.getSign()+UrlConnector.NEW_STATUS+"1"+UrlConnector.NEW_PAGE+page;
			requestData(url);
			break;
		case REFRESH:
			mList.clear();
			page = 1;
			url = UrlConnector.MYMESSAGE+sharedpfTools.getAccessToken()+UrlConnector.SIGN+sign.getSign()+UrlConnector.NEW_STATUS+"1"+UrlConnector.NEW_PAGE+page;
			requestData(url);
			break;
			
		case DELECT:
			url = UrlConnector.MYMESSAGE_DELECT+itemPosition+"?access-token="+sharedpfTools.getAccessToken()+UrlConnector.SIGN+sign.getSign();
			handleData(url);
			break;
		
		}		
		
	}
	
	private void requestData(String url) {
		http.get(url, null, false, new HttpCallBack() {
			@Override
			public void onFailure(int errorNo, String strMsg) {
				super.onFailure(errorNo, strMsg);
				Toast.makeText(ZfMyMessageActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				messageModel = gson.fromJson(t, MessageModel.class);
				list = messageModel.getItems();		
				int pageCount = messageModel.get_meta().getPageCount();
				int currentPage = messageModel.get_meta().getCurrentPage();
				
				if(list != null && list.size() > 0 && page <= pageCount) {
					for(MessageItemModel item : list){
						mList.add(item);
					}
					myMessageAdapter.notifyDataSetChanged();	
					page += 1;
				} else {
					Toast.makeText(getApplicationContext(), "没有更多数据",
							Toast.LENGTH_SHORT).show();
					
				}
				
				
				if (scrollViewMore.isRefreshing()) {
					scrollViewMore.onRefreshComplete();
				}
			}
		});
	}
	
	private void handleData(String url) {
		AbRequestParams params = new AbRequestParams();
		params.put("_method", "delete");
		httpUtil.post(url, params, new AbStringHttpResponseListener() {
			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFailure(int statusCode, String content, Throwable error) {
				Toast.makeText(ZfMyMessageActivity.this, "请求失败", Toast.LENGTH_SHORT).show();				
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				Log.e("delete", content);	
				try {
					JSONObject jsonObject = new JSONObject(content);
					int state = jsonObject.getInt("state");
					Toast.makeText(getApplicationContext(), 
							jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
		});
		
		
//		HttpParams params = new HttpParams();
//		params.put("_method", "delete");
//		//Log.e("deleteParams", params.getJsonParams());
//		Log.e("deleteParamsurl", params.getUrlParams().toString());
//		http.jsonPost(url, params, false, new HttpCallBack() {
//			
//			@Override
//			public void onFailure(int errorNo, String strMsg) {
//				super.onFailure(errorNo, strMsg);
//				Toast.makeText(ZfMyMessageActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
//			}
//			
//			@Override
//			public void onSuccess(String t) {
//				super.onSuccess(t);
//				Log.e("delete", t);
//			}
//		
//		});
	}
	
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		applyData(MORE);
		
	}
	
	
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		applyData(REFRESH);
	}

	

}
