package com.hc.xiaobairent.activity;

import java.util.ArrayList;
import java.util.List;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbStringHttpResponseListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hc.core.utils.RentConstants;
import com.hc.core.utils.UrlConnector;
import com.hc.xiaobairent.R;
import com.hc.xiaobairent.adapter.IdNameAdapter;
import com.hc.xiaobairent.adapter.SelectTypeAdapter;
import com.hc.xiaobairent.model.IdNameModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @ClassName: ChooseCityActivity
 * @Description: 选择地址
 * @author frank.fun@qq.com
 * @date 2016年5月20日 下午5:26:50
 *
 */
public class ChooseCircleActivity extends Activity {
	protected static final String TAG = "ChooseCircleActivity";
	private ImageView back;
	private TextView title;
	private List<IdNameModel> list = new ArrayList<>();
	private IdNameAdapter adapter;
	private ListView listview;
	private Context context = this;
	private AbHttpUtil http;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.title_list_activity);
		back = (ImageView) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		listview = (ListView) findViewById(R.id.listview);
		adapter = new IdNameAdapter(context, list, Gravity.LEFT);
		listview.setAdapter(adapter);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		title.setText("选择所在商圈");
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent();
				intent.putExtra(RentConstants.ID_PARAM, list.get(position).getId());
				intent.putExtra(RentConstants.CONTENT_PARAM, list.get(position).getName());
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		http = AbHttpUtil.getInstance(context);
		http.post(UrlConnector.CHOOSE_CIRCLE, new AbStringHttpResponseListener() {

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
				List<IdNameModel> types = new Gson().fromJson(content, new TypeToken<List<IdNameModel>>() {
				}.getType());
				list.clear();
				if (types != null && types.size() > 0) {
					list.addAll(types);
				}
				adapter.notifyDataSetChanged();
			}
		});
	}
}
