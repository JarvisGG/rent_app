package com.hc.xiaobairent.activity;

import java.util.ArrayList;
import java.util.List;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbStringHttpResponseListener;
import com.google.gson.Gson;
import com.hc.core.utils.SharedpfTools;
import com.hc.core.utils.Sign;
import com.hc.core.utils.UrlConnector;
import com.hc.xiaobairent.R;
import com.hc.xiaobairent.adapter.IncomeAdapter;
import com.hc.xiaobairent.model.IncomeModel;
import com.hc.xiaobairent.model.IncomesModel;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @ClassName: IncomeDetailActivity
 * @Description: 收入明细
 * @author frank.fun@qq.com
 * @date 2016年5月7日 上午11:16:10
 *
 */
public class IncomeDetailActivity extends Activity implements OnClickListener {
	protected static final String TAG = "IncomeDetailActivity";
	private ImageView back;
	private TextView title;
	private ListView listview;
	private List<IncomeModel> list = new ArrayList<>();
	private IncomeAdapter adapter;
	private SharedpfTools sp;
	private AbHttpUtil http;
	private Sign sign;
	private Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.income_detail_activity);
		back = (ImageView) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		listview = (ListView) findViewById(R.id.listview);
		adapter = new IncomeAdapter(context, list);
		listview.setAdapter(adapter);
		back.setOnClickListener(this);
		title.setText("收入明细");
		sp = SharedpfTools.getInstance(context);
		http = AbHttpUtil.getInstance(context);
		sign = new Sign(context);
		getData();
	}

	private void getData() {
		sign.init();
		http.get(UrlConnector.INCOME_DETAIL + sp.getAccessToken() + UrlConnector.SIGN + sign.getSign(),
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
						IncomesModel incomesModel = new Gson().fromJson(content, IncomesModel.class);
						loadData(incomesModel.getItems());
					}

				});
	}

	private void loadData(List<IncomeModel> items) {
		list.clear();
		if (items != null && items.size() > 0) {
			list.addAll(items);
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			onBackPressed();
			break;
		default:
			break;
		}
	}

}
