package com.hc.xiaobairent.activity;

import java.util.ArrayList;
import java.util.List;

import com.hc.core.utils.RentConstants;
import com.hc.xiaobairent.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @ClassName: ChoosePeriodActivity
 * @Description: 选择区间
 * @author frank.fun@qq.com
 * @date 2016年5月20日 上午11:22:50
 *
 */
public class ChooseDepositPeriodActivity extends Activity implements OnClickListener {
	private ImageView back;
	private TextView title;
	private Context context = this;
	private List<String> months = new ArrayList<>();
	private ArrayAdapter<String> adapter;
	private ListView listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_period_activity);
		back = (ImageView) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		listview = (ListView) findViewById(R.id.listview);
		title.setText("请选择押金月数");
		months.add("6个月");
		months.add("12个月");
		adapter = new ArrayAdapter<>(context, R.layout.list_item_textview, months);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent();
				switch (position) {
				case 0:
					intent.putExtra(RentConstants.CONTENT_PARAM, 6);
					break;
				case 1:
					intent.putExtra(RentConstants.CONTENT_PARAM, 12);
					break;
				}
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		default:
			break;
		}
	}

}
