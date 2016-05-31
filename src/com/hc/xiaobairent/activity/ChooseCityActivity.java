package com.hc.xiaobairent.activity;

import java.util.ArrayList;
import java.util.List;

import com.hc.core.utils.RentConstants;
import com.hc.xiaobairent.R;
import com.hc.xiaobairent.adapter.SelectAdapter;
import com.hc.xiaobairent.utils.CityModel;
import com.hc.xiaobairent.utils.CityUtil;

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
 * @ClassName: ChooseCityActivity
 * @Description: 选择地址
 * @author frank.fun@qq.com
 * @date 2016年5月20日 下午5:26:50
 *
 */
public class ChooseCityActivity extends Activity {
	protected static final String TAG = "ChooseCityActivity";
	protected static final int CITY = 1;
	protected static final int REGION = 2;
	private ImageView back;
	private TextView title;
	private List<CityModel> provienceList = new ArrayList<>();
	private List<CityModel> cityList = new ArrayList<>();
	private List<CityModel> regionList = new ArrayList<>();
	private SelectAdapter adapter;
	private ListView listview;
	private Context context = this;
	private int provienceId = 0;
	private int cityId = 0;
	private String provienceName = "";
	private String cityName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.title_list_activity);
		back = (ImageView) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		listview = (ListView) findViewById(R.id.listview);
		provienceList = CityUtil.getList(context);
		initBack();
		initProvience();
	}

	private void initBack() {
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (title.getText().toString()) {
				case "请选择省份":
					onBackPressed();
					break;
				case "请选择城市":
					initProvience();
					break;
				case "请选择地区":
					initCity();
					break;
				default:
					break;
				}
			}
		});
	}

	/**
	 * 
	 * @Title: initProvience
	 * @Description: 选择省份
	 */
	private void initProvience() {
		title.setText("请选择省份");
		adapter = new SelectAdapter(context, provienceList);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				provienceId = provienceList.get(position).getId();
				provienceName = provienceList.get(position).getRegion_name();
				cityList = provienceList.get(position).getSon();
				if (cityList != null && cityList.size() > 0) {
					initCity();
				} else {
					Intent intent = new Intent();
					intent.putExtra("provienceId", provienceId);
					intent.putExtra("provienceName", provienceName);
					setResult(RESULT_OK, intent);
					finish();
				}
			}
		});
	}

	/**
	 * 
	 * @Title: initCity
	 * @Description: 选择城市
	 */
	private void initCity() {
		title.setText("请选择城市");
		adapter = new SelectAdapter(context, cityList);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				cityId = cityList.get(position).getId();
				cityName = cityList.get(position).getRegion_name();
				regionList = cityList.get(position).getSon();
				if (regionList != null && regionList.size() > 0) {
					initRegion();
				} else {
					Intent intent = new Intent();
					intent.putExtra("provienceId", provienceId);
					intent.putExtra("provienceName", provienceName);
					intent.putExtra("cityId", cityId);
					intent.putExtra("cityName", cityName);
					setResult(RESULT_OK, intent);
					finish();
				}
			}
		});
	}

	/**
	 * 
	 * @Title: initRegion
	 * @Description: 请选择地区
	 */
	private void initRegion() {
		title.setText("请选择地区");
		adapter = new SelectAdapter(context, regionList);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("provienceId", provienceId);
				intent.putExtra("provienceName", provienceName);
				intent.putExtra("cityId", cityId);
				intent.putExtra("cityName", cityName);
				intent.putExtra("regionId", regionList.get(position).getId());
				intent.putExtra("regionName", regionList.get(position).getRegion_name());
				Log.v(TAG,
						"regionId>" + regionList.get(position).getId() + ">>regionName"
								+ regionList.get(position).getRegion_name() + "cityId>" + cityId + ">>cityName"
								+ cityName + "provienceId>" + provienceId + ">>provienceName" + provienceName);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}
}
