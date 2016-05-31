package com.hc.xiaobairent.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.ui.BindView;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import com.hc.xiaobairent.adapter.MyCollectAdapter;
import com.hc.xiaobairent.dialog.ListViewDialog.Builder;
import com.hc.xiaobairent.model.MyCollectionItemModel;
import com.hc.xiaobairent.model.MyCollectionModel;

/**
 * @ClassName: ZfMyCollectActivity
 * @Description：我的收藏模块
 * @author xiaofei
 * @date 2016年5月23日 上午
 */
public class ZfMyCollectActivity extends BaseActivity implements OnRefreshListener2<ScrollView>, OnItemClickListener, OnItemLongClickListener{
	// 初始化标题
	@BindView(id = R.id.menu_back, click = true)
	private ImageView menuBack;
	@BindView(id = R.id.menu_title)
	private TextView menuTitle;
	
	// 列表刷新
	@BindView(id = R.id.scrollview_more)
	private PullToRefreshScrollView scrollViewMore;
	@BindView(id = R.id.mycollect_list)
	private ListView myCollectList;
	
	// 收藏列表
	private List<MyCollectionItemModel> mList;
	
	private MyCollectAdapter myCollectAdapter;
	
	private SharedpfTools sharedpfTools = SharedpfTools.getInstance(ZfMyCollectActivity.this);
	private int page = 0;
	private KJHttp http;
	private AbHttpUtil httpUtil;
	private Gson gson;
	private Sign sign;
	private List<MyCollectionItemModel> list;
	private int itemPosition;
	
	// 请求方式: 1为刷新，2为加载更多
	private static final int REFRESH = 1;
	private static final int MORE = 2;
	private static final int DELECT = 3;
	
	@Override
	public void setRootView() {
		setAbContentView(R.layout.zf_activity_mycollect);
	}
	
	@Override
	public void initData() {
		super.initData();
		initTab();
		// 上拉刷新，下拉加载更多
		scrollViewMore.setMode(Mode.BOTH);
		scrollViewMore.setOnRefreshListener(this);
	
		mList = new ArrayList<MyCollectionItemModel>();
		myCollectAdapter = new MyCollectAdapter(ZfMyCollectActivity.this, mList);
		myCollectList.setAdapter(myCollectAdapter);
		myCollectList.setOnItemClickListener(this);
		myCollectList.setOnItemLongClickListener(this);
		applyData(REFRESH);
	}
	
	private void initTab() {
		menuTitle.setText("我的收藏");
	}
	
	private void applyData(final int command) {
		// 初始化参数化
		sign = new Sign(ZfMyCollectActivity.this);
		sign.init();
		http = new KJHttp();
		httpUtil = AbHttpUtil.getInstance(getApplicationContext());
		gson = new Gson();
		sharedpfTools = SharedpfTools.getInstance(getApplicationContext());
		String url = null;
		switch (command) {
		case MORE:
			url = UrlConnector.MYCOLLECTION+sharedpfTools.getAccessToken()+UrlConnector.SIGN+sign.getSign();
			requestData(url);
			break;
		
		case REFRESH:
			mList.clear();
			page = 1;
			url = UrlConnector.MYCOLLECTION+sharedpfTools.getAccessToken()+UrlConnector.SIGN+sign.getSign();
			requestData(url);
			break;
			
		case DELECT:
			url = UrlConnector.MYCOLLECT_DELETE+itemPosition;
			handleData(url);
			break;

		default:
			break;
		}
		
	}
	
	private void handleData(String url) {
		AbRequestParams params = new AbRequestParams();
		params.put("_method", "delete");
		httpUtil.post(url, params, new AbStringHttpResponseListener() {
			
			@Override
			public void onStart() {
				
			}
			
			@Override
			public void onFinish() {
				
			}
			
			@Override
			public void onFailure(int statusCode, String content, Throwable error) {
				Toast.makeText(ZfMyCollectActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
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
	}
	
	private void requestData(String url) {
		http.get(url, null, false, new HttpCallBack() {
			@Override
			public void onFailure(int errorNo, String strMsg) {
				super.onFailure(errorNo, strMsg);
			}
			
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				MyCollectionModel myCollectionModel = gson.fromJson(t, MyCollectionModel.class);
				list = myCollectionModel.getItems();
				int pageCount = myCollectionModel.get_meta().getPageCount();
				int currentPage = myCollectionModel.get_meta().getCurrentPage();
				
				if(list != null && list.size() > 0 && page <= pageCount) {
					for(MyCollectionItemModel item : list) {
						mList.add(item);
					}
					myCollectAdapter.notifyDataSetChanged();
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
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			final int position, long id) {
		itemPosition = mList.get(position).getId();
		Builder builder = new Builder(this);
		builder.setTitle("删除这条收藏？");
		builder.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mList.remove(position);
				myCollectAdapter.notifyDataSetChanged();
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
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
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		applyData(MORE);
		
	}
	
	
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		applyData(REFRESH);
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
//			// Do some stuff here
//
//			// Call onRefreshComplete when the list has been refreshed.
//			scrollViewMore.onRefreshComplete();
//
//			super.onPostExecute(result);
//		}
//	}

}
