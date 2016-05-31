package com.hc.xiaobairent.activity;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.ui.BindView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.hc.xiaobairent.adapter.MyRentalAdapter;
import com.hc.xiaobairent.model.MyRentalItemModel;
import com.hc.xiaobairent.model.MyRentalModel;

public class ZfMyRentalActivity extends BaseActivity implements OnRefreshListener2<ScrollView>{
	// 初始化标题
	@BindView(id = R.id.menu_back, click = true)
	private ImageView menuBack;
	@BindView(id = R.id.menu_title)
	private TextView menuTitle;
	// 列表刷新
	@BindView(id = R.id.scrollview_more)
	private PullToRefreshScrollView scrollViewMore;
	@BindView(id = R.id.myrental_list)
	private ListView myRentalList;
	// 租房列表
	private List<MyRentalItemModel> mList;
	private List<MyRentalItemModel> list;
	private MyRentalAdapter myRentalAdapter;
	private MyRentalModel myRentalModel;
	// 接口参数
	private SharedpfTools sharedpfTools = SharedpfTools.getInstance(ZfMyRentalActivity.this);
	private int page = 1;
	private KJHttp kjhttp;
	private Gson gson;
	private Sign sign;
	
	// 请求方式：1为刷新，2为加载更多
	private static final int REFRESH = 1;
	private static final int MORE = 2;
	
	@Override
	public void setRootView() {
		setAbContentView(R.layout.zf_activity_myrental);
	}
	
	@Override
	public void initData() {
		super.initData();
		initTab();
		mList = new ArrayList<MyRentalItemModel>();
		scrollViewMore.setMode(Mode.BOTH);
		scrollViewMore.setOnRefreshListener(this);
		myRentalAdapter = new MyRentalAdapter(ZfMyRentalActivity.this, mList, myRentalList);
		myRentalList.setAdapter(myRentalAdapter);
		
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		applyData(REFRESH);
	}
	
	private void initTab() {
		menuTitle.setText("我的租房");
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
	
	private void applyData(int command) {
		switch (command) {
		case MORE:
			break;
		case REFRESH:
			mList.clear();
			page = 1;
		default:
			break;
		}
		sign = new Sign(ZfMyRentalActivity.this);
		sign.init();
		kjhttp = new KJHttp();
		gson = new Gson();
		String url = UrlConnector.MYRENTS_ITEMLIST+sharedpfTools.getAccessToken()+UrlConnector.SIGN+sign.getSign();
		// 不设置缓存
		kjhttp.get(url, null, false, new HttpCallBack() {
			@Override
			public void onFailure(int errorNo, String strMsg) {
				super.onFailure(errorNo, strMsg);
				Toast.makeText(ZfMyRentalActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				Log.e("rentalRoomt", t);
				myRentalModel = gson.fromJson(t, MyRentalModel.class);
				list = myRentalModel.getItems();

				int pageCount = myRentalModel.get_meta().getPageCount();
				int currentPage = myRentalModel.get_meta().getCurrentPage();
				if(list != null && list.size() > 0 && page <= pageCount) {
					for(MyRentalItemModel item : list) {
						mList.add(item);
					}					
					myRentalAdapter.notifyDataSetChanged();
					page += 1;

				} else {
					Toast.makeText(getApplicationContext(), "没有更多数据了", Toast.LENGTH_SHORT).show();
	
				}
				
				if(scrollViewMore.isRefreshing()) {
					scrollViewMore.onRefreshComplete();
				}
				
			}
		});
		
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		applyData(REFRESH);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		applyData(MORE);
	}
}
