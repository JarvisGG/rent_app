package com.hc.xiaobairent.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.ui.BindView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ScrollingView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.hc.core.base.BaseActivity;
import com.hc.core.utils.RentConstants;
import com.hc.core.utils.SharedpfTools;
import com.hc.core.utils.Sign;
import com.hc.core.utils.UrlConnector;
import com.hc.xiaobairent.R;
import com.hc.xiaobairent.adapter.MyRoomAdapter;
import com.hc.xiaobairent.model.MyRoomItemModel;
import com.hc.xiaobairent.model.MyRoomModel;

/**
 * @ClassName: ZfHouseListActivity
 * @Description：房源列表
 * @author xiaofei
 * @date 2016年5月27日 下午
 */
public class ZfHouseListActivity extends BaseActivity 
				implements OnRefreshListener2<ScrollView>, OnItemClickListener, OnItemLongClickListener {

	// 初始化标题
	@BindView(id = R.id.menu_back, click = true)
	private ImageView menuBack;
	@BindView(id = R.id.menu_title)
	private TextView menuTitle;
	
	// 列表刷新
	@BindView(id = R.id.scrollview_more)
	private PullToRefreshScrollView scrollView;
	@BindView(id = R.id.house_list)
	private ListView houseList;
	
	// 房源列表
	private List<MyRoomItemModel> mList;
	private List<MyRoomItemModel> list;
	private MyRoomModel myRoomModel;
	private MyRoomAdapter myRoomAdapter;
	
	private SharedpfTools sharedpfTools;
	private AbHttpUtil httpUtil;
	private Gson gson;
	private Sign sign;
	private int itemPosition;
	private int page = 0;
	
	// 请求方式
	private static final int REFRESH = 1;
	private static final int MORE = 2;
	private static final int DELECT = 3;
	
	private int parkId;
	private int status;
	@Override
	public void setRootView() {
		setContentView(R.layout.zf_activity_houses);
	}
	
	@Override
	public void initData() {
		super.initData();
		Bundle bundle = getIntent().getExtras();
		parkId = bundle.getInt("id");
		status = bundle.getInt("status");
		
		scrollView.setMode(Mode.BOTH);
		scrollView.setOnRefreshListener(this);
		
		mList = new ArrayList<MyRoomItemModel>();
		myRoomAdapter = new MyRoomAdapter(ZfHouseListActivity.this, mList, houseList, 0);
		houseList.setAdapter(myRoomAdapter);
		houseList.setOnItemClickListener(this);
		houseList.setOnItemLongClickListener(this);
		
		initTab();
		applyData(REFRESH);
	}
	
	private void applyData(int command) {
		sharedpfTools = SharedpfTools.getInstance(ZfHouseListActivity.this);
		httpUtil = AbHttpUtil.getInstance(ZfHouseListActivity.this);
		sign = new Sign(ZfHouseListActivity.this);
		sign.init();
		gson = new Gson();
		String url = null;
		switch (command) {
		case REFRESH:
			mList.clear();
			page = 0;
		case MORE:
			url = UrlConnector.MYROOMLIST + sharedpfTools.getAccessToken() + UrlConnector.SIGN;
			requestData(url);
			break;

		default:
			break;
		}
	}
	
	private void requestData(String url) {
		AbRequestParams params = new AbRequestParams();
		params.put("park_id", parkId);
		params.put("status", status);
		sign.addParam("park_id", parkId);
		sign.addParam("status", status);
		httpUtil.post(url + sign.getSign(), params, new AbStringHttpResponseListener() {
			
			@Override
			public void onStart() {
				
			}
			
			@Override
			public void onFinish() {
				
			}
			
			@Override
			public void onFailure(int statusCode, String content, Throwable error) {
				Toast.makeText(ZfHouseListActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				try {
					JSONObject object = new JSONObject(content);
					myRoomModel = gson.fromJson(content, MyRoomModel.class);
					list = myRoomModel.getItems();
					int pageCount = myRoomModel.get_meta().getPageCount();
					int currentPage = myRoomModel.get_meta().getCurrentPage();

					if (list != null && list.size() > 0 && page < pageCount) {
						mList.addAll(list);
						myRoomAdapter.notifyDataSetChanged();
						page += 1;
					} else {
						Toast.makeText(getApplicationContext(), "没有更多数据", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				

				if (scrollView.isRefreshing()) {
					scrollView.onRefreshComplete();
				}
			}
		});
	}
	
	private void initTab() {
		switch (status) {
		case 1:
			menuTitle.setText("未租房源");
			break;
		case 2:
			menuTitle.setText("已租房源");
		default:
			break;
		}
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
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		applyData(REFRESH);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		applyData(MORE);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		startActivity(new Intent(ZfHouseListActivity.this, ZfMyRoomDetailActivity.class)
			.putExtra("deleteId", mList.get(position).getId()+""));
		overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
	}



}
