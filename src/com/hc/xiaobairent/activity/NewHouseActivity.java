package com.hc.xiaobairent.activity;

import java.util.ArrayList;
import java.util.List;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbStringHttpResponseListener;
import com.google.gson.Gson;
import com.hc.core.utils.RentConstants;
import com.hc.core.utils.UrlConnector;
import com.hc.xiaobairent.R;
import com.hc.xiaobairent.adapter.HouseAdapter;
import com.hc.xiaobairent.model.HouseModel;
import com.hc.xiaobairent.model.ParksModel;

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
 * @ClassName: SearchActivity
 * @Description: 搜索小区
 * @author frank.fun@qq.com
 * @date 2016年5月7日 上午11:15:51
 *
 */
public class NewHouseActivity extends Activity {
	protected static final String TAG = "NewHouseActivity";
	private ImageView back;
	private TextView title;
	private ListView listview;
	private AbHttpUtil http;
	private Context context = this;
	private HouseAdapter adapter;
	private List<HouseModel> list = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_activity);
		back = (ImageView) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		listview = (ListView) findViewById(R.id.listview);
		title.setText("新上房源");
		http = AbHttpUtil.getInstance(context);
		adapter = new HouseAdapter(context, list);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				startActivity(new Intent(context, HouseDetailActivity.class).putExtra(RentConstants.ID_PARAM,
						list.get(position).getId()));
			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		getData();
	}

	private void getData() {
		http.get(UrlConnector.NEW_HOUSE, new AbStringHttpResponseListener() {

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
			public void onSuccess(int statusCode, String t) {
				Log.v(TAG, t);
				list.clear();
				ParksModel parksModel = new Gson().fromJson(t, ParksModel.class);
				List<HouseModel> items = parksModel.getItems();
				if (items != null && items.size() > 0) {
					list.addAll(items);
				}
				adapter.notifyDataSetChanged();
			}
		});
	}

}
