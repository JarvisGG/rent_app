package com.hc.xiaobairent.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.kymjs.kjframe.ui.BindView;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hc.core.base.BaseActivity;
import com.hc.core.update.MulParamsHttpCallback;
import com.hc.xiaobairent.R;
import com.hc.xiaobairent.adapter.HouseAdapter;
import com.hc.xiaobairent.adapter.RegionAdapter;
import com.hc.xiaobairent.adapter.SelectAdapter;
import com.hc.xiaobairent.model.HouseModel;
import com.hc.xiaobairent.utils.CityModel;
import com.hc.xiaobairent.utils.CityUtil;

public class ZfAreaSelectedActivity extends BaseActivity implements OnItemClickListener{
	
	// 初始化标题
	@BindView(id = R.id.menu_back, click = true)
	private ImageView menuBack;
	@BindView(id = R.id.menu_title)
	private TextView menuTitle;
	
	@BindView(id = R.id.area_list)
	private ListView areaList;
	
	private List<CityModel> provienceList;
	private List<CityModel> cityList = new ArrayList<>();
	private List<CityModel> regionList = new ArrayList<>();
	private List<HouseModel> list = new ArrayList<>();
	
	private int grade;

	private static final int GRADE_PROVENCE = 1;
	private static final int GRADE_CITY = 2;
	private static final int GRADE_AREA = 3;
	
	private String province, city, area, province_id, city_id, area_id;
	private RegionAdapter adapter;
	private HouseAdapter adapter1;
	
	private SelectAdapter pAdapter;
	private SelectAdapter cAdapter;
	private SelectAdapter rAdapter;

	@Override
	public void setRootView() {
		setContentView(R.layout.zf_activity_area_select);
	}
	
	@Override
	public void initData() {
		super.initData();
		
		provienceList = CityUtil.getList(ZfAreaSelectedActivity.this);
		addAll(provienceList);
		pAdapter = new SelectAdapter(ZfAreaSelectedActivity.this, provienceList);
		areaList.setAdapter(pAdapter);
		areaList.setOnItemClickListener(this);
		grade = GRADE_PROVENCE;
		menuTitle.setText("选择省份");
		
	}
	
	private void addAll(List<CityModel> list) {
		CityModel all = new CityModel();
		all.setId(0);
		all.setRegion_name("全部");
		all.setRegion_type((short) 1);
		list.add(0, all);
		
	}
	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		setProgressBarVisibility(false);
		if (grade == GRADE_PROVENCE) {
			menuTitle.setText("选择县市");
			grade = GRADE_CITY;
			province = provienceList.get(position).getRegion_name();
			province_id = provienceList.get(position).getId() + "";
			cityList.clear();
			if (provienceList.get(position).getSon() != null
					&& provienceList.get(position).getSon().size() > 0) {
				cityList.addAll(provienceList.get(position).getSon());
			}
			addAll(cityList);
			cAdapter = new SelectAdapter(ZfAreaSelectedActivity.this, cityList);
			areaList.setAdapter(cAdapter);

		} else if (grade == GRADE_CITY) {
			menuTitle.setText("选择乡镇");
			grade = GRADE_AREA;
			city_id = cityList.get(position).getId() + "";
			city = cityList.get(position).getRegion_name();
			regionList.clear();
			if (cityList.get(position).getSon() != null
					&& cityList.get(position).getSon().size() > 0) {
				regionList.addAll(cityList.get(position).getSon());
			}
			addAll(regionList);
			rAdapter = new SelectAdapter(ZfAreaSelectedActivity.this, regionList);
			areaList.setAdapter(rAdapter);
			
		} else if (grade == GRADE_AREA) {
			area = regionList.get(position).getRegion_name() + "";
			area_id = regionList.get(position).getId() + "";
			Intent intent = new Intent();
			intent.putExtra("province", province);
			intent.putExtra("province_id", province_id);
			intent.putExtra("city", city);
			intent.putExtra("city_id", city_id);
			intent.putExtra("area", area);
			intent.putExtra("area_id", area_id);
			setResult(RESULT_OK, intent);
			onBackPressed();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
		}
		
	}
	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.menu_back:
			if (grade == GRADE_PROVENCE) {
				onBackPressed();
			} else if (grade == GRADE_CITY) {
				menuTitle.setText("选择省份");
				areaList.setAdapter(pAdapter);
				grade = GRADE_PROVENCE;
			} else if (grade == GRADE_AREA) {
				menuTitle.setText("选择乡镇");
				areaList.setAdapter(cAdapter);
				grade = GRADE_CITY;
			}
			break;

		default:
			break;
		}
	}

}
