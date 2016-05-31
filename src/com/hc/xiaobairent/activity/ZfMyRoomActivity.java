package com.hc.xiaobairent.activity;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.ui.BindView;

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
import com.hc.xiaobairent.adapter.MyRoomAdapter;
import com.hc.xiaobairent.model.MyRoomItemModel;
import com.hc.xiaobairent.model.MyRoomModel;

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

public class ZfMyRoomActivity extends BaseActivity
		implements OnRefreshListener2<ScrollView>, OnItemClickListener, OnItemLongClickListener {
	// 初始化标题
	@BindView(id = R.id.menu_back, click = true)
	private ImageView menuBack;
	@BindView(id = R.id.menu_title)
	private TextView menuTitle;

	// 列表刷新
	@BindView(id = R.id.scrollview_more)
	private PullToRefreshScrollView scrollView;
	@BindView(id = R.id.myroom_list)
	private ListView myRoomList;

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

	@Override
	public void setRootView() {
		setAbContentView(R.layout.zf_activity_myroom);
	}

	@Override
	public void initData() {
		super.initData();
		// 上拉刷新，下拉加载更多
		scrollView.setMode(Mode.BOTH);
		scrollView.setOnRefreshListener(this);

		mList = new ArrayList<MyRoomItemModel>();
		myRoomAdapter = new MyRoomAdapter(ZfMyRoomActivity.this, mList, myRoomList, 1);
		myRoomList.setAdapter(myRoomAdapter);
		myRoomList.setOnItemClickListener(this);
		myRoomList.setOnItemLongClickListener(this);

		initTab();
		applyData(REFRESH);
	}

	private void applyData(int command) {
		// 初始化参数化
		sharedpfTools = SharedpfTools.getInstance(ZfMyRoomActivity.this);
		httpUtil = AbHttpUtil.getInstance(ZfMyRoomActivity.this);
		sign = new Sign(ZfMyRoomActivity.this);
		sign.init();
		gson = new Gson();
		String url = null;
		switch (command) {
		case REFRESH:
			mList.clear();
			page = 0;
		case MORE:
			url = UrlConnector.MYROOMLIST + sharedpfTools.getAccessToken() + UrlConnector.SIGN + sign.getSign();
			requestData(url);
			break;

		default:
			break;
		}
	}

	private void requestData(String url) {
		Log.e("url", url);
		httpUtil.get(url, null, new AbStringHttpResponseListener() {

			@Override
			public void onStart() {

			}

			@Override
			public void onFinish() {

			}

			@Override
			public void onFailure(int statusCode, String content, Throwable error) {
				Toast.makeText(ZfMyRoomActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
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

				if (scrollView.isRefreshing()) {
					scrollView.onRefreshComplete();
				}
			}
		});
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		itemPosition = mList.get(position).getId();
		Intent intent = new Intent();
		intent.putExtra("deleteId", itemPosition);
		intent.setClass(ZfMyRoomActivity.this, ZfMyRoomDetailActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
	}

	private void initTab() {
		menuTitle.setText("我的房源");
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

}
