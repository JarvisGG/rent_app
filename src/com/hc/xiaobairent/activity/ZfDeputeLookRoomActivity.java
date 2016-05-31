package com.hc.xiaobairent.activity;
import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.ui.BindView;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
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
import com.hc.xiaobairent.adapter.DeputeLookRoomAdapter;
import com.hc.xiaobairent.bean.DeputeLookRoomBean;
import com.hc.xiaobairent.model.DeputeLookRoomModel;
import com.hc.xiaobairent.model.MyEntrustItemModel;

public class ZfDeputeLookRoomActivity extends BaseActivity implements OnRefreshListener2<ScrollView>, OnItemClickListener{
	// 初始化标题
	@BindView(id = R.id.menu_back, click = true)
	private ImageView menuBack;
	@BindView(id = R.id.menu_title)
	private TextView menuTitle;
	
	// 列表刷新
	@BindView(id = R.id.scrollview_more)
	private PullToRefreshScrollView scrollViewMore;
	@BindView(id = R.id.deputelookroom_list)
	private ListView deputeLookRoomList;
	
	// 看房列表
	private DeputeLookRoomAdapter deputeLookRoomAdapter;
	private List<MyEntrustItemModel> mList; 
	private List<MyEntrustItemModel> list;
		
	private SharedpfTools sharedpfTools;
	private AbHttpUtil httpUtil;
	private Gson gson;
	private Sign sign;
	private DeputeLookRoomModel deputeLookRoomModel;
	private int page = 0;
	
	private static final int REFRESH = 1;
	private static final int MORE = 2;
	private static final int DELETE = 3;
	
	
	@Override
	public void setRootView() {
		setAbContentView(R.layout.zf_activity_deputelookroom);
	}
	
	@Override
	public void initData() {
		super.initData();
		initTab();
		
		scrollViewMore.setMode(Mode.BOTH);
		scrollViewMore.setOnRefreshListener(this);
		mList = new ArrayList<MyEntrustItemModel>();	
		deputeLookRoomAdapter = new DeputeLookRoomAdapter(ZfDeputeLookRoomActivity.this, mList);
		deputeLookRoomList.setAdapter(deputeLookRoomAdapter);
		deputeLookRoomList.setOnItemClickListener(this);
		
		applyData(REFRESH);
	}
	
	private void applyData(int command) {
		sign = new Sign(ZfDeputeLookRoomActivity.this);
		sign.init();
		gson = new Gson();
		httpUtil = AbHttpUtil.getInstance(ZfDeputeLookRoomActivity.this);
		sharedpfTools = SharedpfTools.getInstance(ZfDeputeLookRoomActivity.this);
		String url = null;
		switch (command) {
		case REFRESH:
			mList.clear();
			page = 0;
		case MORE:
			url = UrlConnector.DEPUTELOOKROOM + sharedpfTools.getAccessToken() + UrlConnector.SIGN + sign.getSign();
			requestData(url);
			break;

		case DELETE:
			break;
			
		default:
			break;
		}
	}
	
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
				Toast.makeText(ZfDeputeLookRoomActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				deputeLookRoomModel = gson.fromJson(content, DeputeLookRoomModel.class);
				list = deputeLookRoomModel.getItems();
				int pageCount = deputeLookRoomModel.get_meta().getPageCount();
				int currentPage = deputeLookRoomModel.get_meta().getCurrentPage();
				
				if(list != null && pageCount > 0 && page < pageCount) {
					for (MyEntrustItemModel item : list) {
						mList.add(item);
					}
					deputeLookRoomAdapter.notifyDataSetChanged();
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
		menuTitle.setText("委托看房");
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
		intent.putExtra("itemData", mList.get(position));
		intent.setClass(ZfDeputeLookRoomActivity.this, ZfDeputeDetailActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
		
	}
	
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		
		
		applyData(REFRESH);
		
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

		applyData(MORE);
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {

			scrollViewMore.onRefreshComplete();

			super.onPostExecute(result);
		}
	}

	

}
