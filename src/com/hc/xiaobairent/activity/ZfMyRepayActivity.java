package com.hc.xiaobairent.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
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
import com.hc.xiaobairent.adapter.MyRepayAdapter;
import com.hc.xiaobairent.model.RepayItemModel;
import com.hc.xiaobairent.model.RepaymentModel;

public class ZfMyRepayActivity extends BaseActivity implements OnRefreshListener2<ScrollView> {

	// 初始化标题
	@BindView(id = R.id.menu_back, click = true)
	private ImageView menuBack;
	@BindView(id = R.id.menu_title)
	private TextView menuTitle;

	// 列表刷新
	@BindView(id = R.id.scrollview_more)
	private PullToRefreshScrollView scrollViewMore;
	@BindView(id = R.id.myrepay_list)
	private ListView myRepayList;

	private List<RepayItemModel> mList;
	private MyRepayAdapter myRepayAdapter;

	private KJHttp kjHttp;
	private SharedpfTools sharedpfTools = SharedpfTools.getInstance(ZfMyRepayActivity.this);
	private Sign sign;
	private int page = 0;
	private Gson gson;
	private RepaymentModel repaymentModel;
	private List<RepayItemModel> mRepayItemModels;

	// 请求方式: 1为刷新，2为加载更多

	private static final int REFRESH = 1;
	private static final int MORE = 2;

	@Override
	public void setRootView() {
		setAbContentView(R.layout.zf_activity_myrepay);
	}

	@Override
	public void initData() {
		super.initData();
		initTab();
		scrollViewMore.setMode(Mode.BOTH);
		scrollViewMore.setOnRefreshListener(this);
		mList = new ArrayList<RepayItemModel>();
		myRepayAdapter = new MyRepayAdapter(ZfMyRepayActivity.this, mList);
		myRepayList.setAdapter(myRepayAdapter);
		applyData(REFRESH);
	}

	private void initTab() {
		menuTitle.setText("付款记录");
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

	private void applyData(final int command) {

		switch (command) {
		case MORE:
			break;
		case REFRESH:
			mList.clear();
			page = 1;
			break;
		}

		kjHttp = new KJHttp();
		sign = new Sign(ZfMyRepayActivity.this);
		sign.init();
		gson = new Gson();

		String url = UrlConnector.MYPAYMENT + sharedpfTools.getAccessToken() + UrlConnector.SIGN + sign.getSign();
		Log.e("RepayUrl", url);
		kjHttp.get(url, new HttpCallBack() {
			@Override
			public void onFailure(int errorNo, String strMsg) {
				super.onFailure(errorNo, strMsg);
				Toast.makeText(ZfMyRepayActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				 try {
				 JSONObject object = new JSONObject(t);
				 if(object.getString("error").length() != 0) {
				 Toast.makeText(getApplicationContext(), "无还款记录",
				 Toast.LENGTH_SHORT).show();
				 } else {
				repaymentModel = gson.fromJson(t, RepaymentModel.class);
				mRepayItemModels = repaymentModel.getItems();
				int pageCount = repaymentModel.get_meta().getPageCount();
				if (mRepayItemModels != null && mRepayItemModels.size() > 0 && page <= pageCount) {
					for (RepayItemModel item : mRepayItemModels) {
						mList.add(item);
					}

					myRepayAdapter.notifyDataSetChanged();
					page += 1;
				} else {
					Toast.makeText(getApplicationContext(), "没有更多数据", Toast.LENGTH_SHORT).show();

				}
				 }
				 } catch (JSONException e) {
				 e.printStackTrace();
				 }
				if (scrollViewMore.isRefreshing()) {
					scrollViewMore.onRefreshComplete();
				}

			}
		});

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
