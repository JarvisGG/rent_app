package com.hc.xiaobairent.activity;

import java.util.ArrayList;
import java.util.List;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.google.gson.Gson;
import com.hc.core.utils.RentConstants;
import com.hc.core.utils.SharedpfTools;
import com.hc.core.utils.Sign;
import com.hc.core.utils.UrlConnector;
import com.hc.xiaobairent.R;
import com.hc.xiaobairent.adapter.ImageTitleSubtitleInfoAdapter;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @ClassName: RentManagmentActivity
 * @Description: 出租管理
 * @author frank.fun@qq.com
 * @date 2016年5月20日 上午10:43:02
 *
 */
public class RentManagmentActivity extends Activity {
	protected static final String TAG = "RentManagmentActivity";
	private ImageView back;
	private TextView title;
	private ListView listview;
	private List<HouseModel> list = new ArrayList<>();
	private ImageTitleSubtitleInfoAdapter adapter;
	private SharedpfTools sp;
	private AbHttpUtil http;
	private Sign sign;
	private Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rent_managment_activity);
		back = (ImageView) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		listview = (ListView) findViewById(R.id.listview);
		adapter = new ImageTitleSubtitleInfoAdapter(context, list);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				startActivity(new Intent(context, RentManagmentDetailActivity.class).putExtra(RentConstants.ID_PARAM,
						list.get(position).getHouse_id()));
			}
		});
		title.setText("出租管理");
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		sp = SharedpfTools.getInstance(context);
		http = AbHttpUtil.getInstance(context);
		sign = new Sign(context);
		sign.init();
		Log.v(TAG, UrlConnector.RENT_MANAGE_LIST + sp.getAccessToken() + UrlConnector.SIGN + sign.getSign());
		http.get(UrlConnector.RENT_MANAGE_LIST + sp.getAccessToken() + UrlConnector.SIGN + sign.getSign(),
				new AbStringHttpResponseListener() {

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
						Log.v(TAG, content);
						list.clear();
						HouseListModel houseListModel = new Gson().fromJson(content, HouseListModel.class);
						List<HouseModel> houses = houseListModel.getItems();
						if (houses != null && houses.size() > 0) {
							list.addAll(houses);
						}
						adapter.notifyDataSetChanged();
					}
				});
	}
}
