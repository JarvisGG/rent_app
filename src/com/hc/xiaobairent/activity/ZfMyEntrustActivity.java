package com.hc.xiaobairent.activity;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.ui.BindView;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import com.hc.xiaobairent.adapter.MyEntrustAdapter;
import com.hc.xiaobairent.dialog.ListViewDialog.Builder;
import com.hc.xiaobairent.model.MyEntrustItemModel;
import com.hc.xiaobairent.model.MyEntrustModel;

public class ZfMyEntrustActivity extends BaseActivity implements OnRefreshListener2<ScrollView>, OnItemClickListener, OnItemLongClickListener {
	// 初始化标题
	@BindView(id = R.id.menu_back, click = true)
	private ImageView menuBack;
	@BindView(id = R.id.menu_title)
	private TextView menuTitle;
	
	// 列表刷新
	@BindView(id = R.id.scrollview_more)
	private PullToRefreshScrollView scrollViewMore;
	@BindView(id = R.id.deputelookroom_list)
	private ListView myEntrustList;
	
	// 消息列表
	private MyEntrustAdapter myEntrustAdapter;
	private List<MyEntrustItemModel> mList;
	private List<MyEntrustItemModel> list;
	
	private SharedpfTools sharedpfTools;
	private int page = 0;
	private KJHttp http;
	private AbHttpUtil httpUtil;
	private Gson gson;
	private Sign sign;
	private MyEntrustModel myEntrustModel;
	
	// 请求方式: 1为刷新，2为加载更多，3为删除条目
	private static final int REFRESH = 1;
	private static final int MORE = 2;
	private static final int DELECT = 3;
	
	private int itemPosition;
	
	@Override
	public void setRootView() {
		setAbContentView(R.layout.zf_activity_myentrust);
	}
	
	@Override
	public void initData() {
		super.initData();
		initTab();
		
		scrollViewMore.setMode(Mode.BOTH);
		scrollViewMore.setOnRefreshListener(this);
		mList = new ArrayList<MyEntrustItemModel>();	
		myEntrustAdapter = new MyEntrustAdapter(ZfMyEntrustActivity.this, mList);
		myEntrustList.setAdapter(myEntrustAdapter);
		myEntrustList.setOnItemClickListener(this);
		myEntrustList.setOnItemLongClickListener(this);
		applyData(REFRESH);
		
	}
	
	private void applyData(int command) {
		
		sign = new Sign(ZfMyEntrustActivity.this);
		sign.init();
		http = new KJHttp();
		httpUtil = AbHttpUtil.getInstance(ZfMyEntrustActivity.this);
		gson = new Gson();
		sharedpfTools = SharedpfTools.getInstance(ZfMyEntrustActivity.this);
		String url = null;
		switch (command) {
		case REFRESH:
			mList.clear();
			page = 0;
		
		case MORE:
			url = UrlConnector.MYENTRUST + sharedpfTools.getAccessToken() + UrlConnector.SIGN + sign.getSign();
			requestData(url);
			break;
		
		case DELECT:
			sign.addParam("id", itemPosition);
			url = UrlConnector.MYENTRUST_DELETE + sharedpfTools.getAccessToken() + UrlConnector.SIGN + sign.getSign();
			handleData(url);
			break;
		default:
			break;
		}
	}
	
	// 长按删除
	private void handleData(String url) {
		AbRequestParams params = new AbRequestParams();
		params.put("id", itemPosition);
		
		httpUtil.post(url, params, new AbStringHttpResponseListener() {
			
			@Override
			public void onStart() {
				
			}
			
			@Override
			public void onFinish() {
				
			}
			
			@Override
			public void onFailure(int statusCode, String content, Throwable error) {
				Toast.makeText(ZfMyEntrustActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				Log.e("delete", content);
				Toast.makeText(ZfMyEntrustActivity.this, 
						"删除成功", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	// 加载数据
	private void requestData(String url) {
		httpUtil.get(url, null, new AbStringHttpResponseListener() {
			
			@Override
			public void onStart() {
				
			}
			
			@Override
			public void onFinish() {
				
			}
			
			@Override
			public void onFailure(int statusCode, String content, Throwable error) {
				Toast.makeText(ZfMyEntrustActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				myEntrustModel = gson.fromJson(content, MyEntrustModel.class);
				list = myEntrustModel.getItems();
				int pageCount = myEntrustModel.get_meta().getPageCount();
				int currentPage = myEntrustModel.get_meta().getCurrentPage();
				
				if(list != null && list.size() > 0 && page <= pageCount) {
					for(MyEntrustItemModel item : list) {
						mList.add(item);
					}
					myEntrustAdapter.notifyDataSetChanged();
					page += 1;
				} else {
					Toast.makeText(getApplicationContext(), "没有更多数据",
							Toast.LENGTH_SHORT).show();
				}
				
				if(scrollViewMore.isRefreshing()) {
					scrollViewMore.onRefreshComplete();
				}
				
			}
		});
	}
	
	
	private void initTab() {
		menuTitle.setText("我的委托");
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
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent();
		itemPosition = mList.get(position).getId();
		intent.putExtra("deleteId", itemPosition);
		intent.setClass(ZfMyEntrustActivity.this, ZfEntrustDetailActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
		
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			final int position, long id) {
		itemPosition = mList.get(position).getId();
		Builder builder = new Builder(this);
		builder.setTitle("删除这条委托?");
		builder.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mList.remove(position);
				myEntrustAdapter.notifyDataSetChanged();
				applyData(DELECT);
				dialog.dismiss();
			}
		});
		
		builder.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		builder.create().show();
		return true;
	}
	
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		applyData(REFRESH);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		applyData(MORE);
	}
	
//	private class GetDataTask extends AsyncTask<Void, Void, String[]> {
//
//		@Override
//		protected String[] doInBackground(Void... params) {
//			// Simulates a background job.
//			try {
//				Thread.sleep(4000);
//			} catch (InterruptedException e) {
//			}
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(String[] result) {
//
//			scrollViewMore.onRefreshComplete();
//
//			super.onPostExecute(result);
//		}
//	}

	
}
