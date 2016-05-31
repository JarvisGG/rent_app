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
import com.hc.xiaobairent.adapter.HistoryAdapter;
import com.hc.xiaobairent.adapter.HouseAdapter;
import com.hc.xiaobairent.model.HouseModel;
import com.hc.xiaobairent.model.ParksModel;
import com.hc.xiaobairent.utils.SearchHistoryUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
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
public class SearchActivity extends Activity implements OnClickListener {
	protected static final String TAG = "SearchActivity";
	private TextView confirm_tv;
	private EditText search_et;
	private ListView listview;
	private SharedpfTools sp;
	private SearchHistoryUtil shUtil;
	private AbHttpUtil http;
	private Sign sign;
	private AbRequestParams params;
	private Context context = this;
	private HouseAdapter adapter;
	private List<HouseModel> list = new ArrayList<>();
	private HistoryAdapter shAdapter;
	private List<String> shList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity);
		confirm_tv = (TextView) findViewById(R.id.confirm_tv);
		search_et = (EditText) findViewById(R.id.search_et);
		listview = (ListView) findViewById(R.id.listview);
		confirm_tv.setOnClickListener(this);
		search_et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				Log.v(TAG, s + ">>>" + s.toString());
				if (TextUtils.isEmpty(s)) {
					confirm_tv.setText("取消");
				} else {
					confirm_tv.setText("搜索");
				}
				for (String i : shUtil.getHistory()) {
					Log.v(TAG, i);
				}
			}
		});
		sp = SharedpfTools.getInstance(context);
		http = AbHttpUtil.getInstance(context);
		shUtil = SearchHistoryUtil.getInstance(context);
		sign = new Sign(context);
		adapter = new HouseAdapter(context, list);
		shAdapter = new HistoryAdapter(context, shList);
		getHistory();
	}

	private void getHistory() {
		shList.clear();
		for (String i : shUtil.getHistory()) {
			shList.add(i);
		}
		shAdapter = new HistoryAdapter(context, shList);
		listview.setAdapter(shAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				hideSoftKeyboard();
				if (position == 0) {
				} else if (position == shUtil.getHistory().length - 1) {
					shUtil.clear();
					shList.clear();
					shAdapter.notifyDataSetChanged();
				} else {
					search_et.setText(shUtil.getHistory()[position]);
					getData(shUtil.getHistory()[position]);
				}
			}
		});
	}

	private void getData(String park_name) {
		params = new AbRequestParams();
		params.put("park_name", park_name);
		http.get(UrlConnector.HOUSE_LIST, params, new AbStringHttpResponseListener() {

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
				listview.setAdapter(adapter);
				listview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						hideSoftKeyboard();
						startActivity(new Intent(context, HouseDetailActivity.class).putExtra(RentConstants.ID_PARAM,
								list.get(position).getId()));
					}
				});
			}
		});
	}

	protected void hideSoftKeyboard() {
		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	@Override
	public void onClick(View v) {
		hideSoftKeyboard();
		switch (v.getId()) {
		case R.id.confirm_tv:
			if (confirm_tv.getText().toString().equals("取消")) {
				onBackPressed();
			} else {
				String s = search_et.getText().toString();
				getData(s);
				for (String i : shUtil.getHistory()) {
					if (s.equals(i)) {
						return;
					}
				}
				shUtil.add(search_et.getText().toString());
			}
			break;
		default:
			break;
		}
	}
}
