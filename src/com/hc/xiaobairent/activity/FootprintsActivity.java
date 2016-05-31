package com.hc.xiaobairent.activity;

import java.util.ArrayList;
import java.util.List;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.google.gson.Gson;
import com.hc.core.utils.RentConstants;
import com.hc.core.utils.SharedpfTools;
import com.hc.core.utils.Sign;
import com.hc.core.utils.UrlConnector;
import com.hc.xiaobairent.R;
import com.hc.xiaobairent.adapter.HouseAdapter;
import com.hc.xiaobairent.model.HouseListModel;
import com.hc.xiaobairent.model.HouseModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * @ClassName: FootprintsActivity
 * @Description: 我的足迹
 * @author frank.fun@qq.com
 * @date 2016年5月20日 上午11:23:17
 *
 */
public class FootprintsActivity extends Activity implements OnHeaderRefreshListener {
	protected static final String TAG = "FootprintsActivity";
	private AbPullToRefreshView mPullRefreshView;
	private ImageView back;
	private TextView title;
	private ListView listview;
	private List<HouseModel> list = new ArrayList<>();
	private HouseAdapter adapter;
	private SharedpfTools sp;
	private AbHttpUtil http;
	private Sign sign;
	private Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.footprints_activity);
		back = (ImageView) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		mPullRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		listview = (ListView) findViewById(R.id.listview);
		title.setText("我的足迹");
		mPullRefreshView.setOnHeaderRefreshListener(this);
		mPullRefreshView.setLoadMoreEnable(false);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		adapter = new HouseAdapter(context, list);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				startActivity(new Intent(context, HouseDetailActivity.class).putExtra(RentConstants.ID_PARAM,
						list.get(position).getId()));
			}
		});
		sp = SharedpfTools.getInstance(context);
		http = AbHttpUtil.getInstance(context);
		sign = new Sign(context);
		getData();
	}

	private void getData() {
		sign.init();
		http.get(UrlConnector.FOOT_PRINTS + sp.getAccessToken() + UrlConnector.SIGN + sign.getSign(),
				new AbStringHttpResponseListener() {

					@Override
					public void onStart() {

					}

					@Override
					public void onFinish() {

					}

					@Override
					public void onFailure(int statusCode, String content, Throwable error) {
						mPullRefreshView.onHeaderRefreshFinish();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						mPullRefreshView.onHeaderRefreshFinish();
						Log.v(TAG, content);
						HouseListModel footprintsModel = new Gson().fromJson(content, HouseListModel.class);
						list.addAll(footprintsModel.getItems());
						adapter.notifyDataSetChanged();
					}
				});
	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView view) {
		getData();
	}
}
