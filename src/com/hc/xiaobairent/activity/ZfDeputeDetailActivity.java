package com.hc.xiaobairent.activity;

import org.kymjs.kjframe.ui.BindView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hc.core.base.BaseActivity;
import com.hc.xiaobairent.R;
import com.hc.xiaobairent.model.MyEntrustItemModel;

public class ZfDeputeDetailActivity extends BaseActivity{
	// 初始化标题
	@BindView(id = R.id.menu_back, click = true)
	private ImageView menuBack;
	@BindView(id = R.id.menu_title)
	private TextView menuTitle;
	
	@BindView(id = R.id.username)
	private TextView tvUsername;
	@BindView(id = R.id.userphone)
	private TextView tvMobile;
	@BindView(id = R.id.tv_address)
	private TextView tvAddress;
	@BindView(id = R.id.tv_area)
	private TextView tvArea;
	@BindView(id = R.id.tv_rent)
	private TextView tvRent;
	@BindView(id = R.id.tv_remark)
	private TextView tvRenmark;
	
	@Override
	public void setRootView() {
		setAbContentView(R.layout.zf_activity_mydepute);
	};
	
	@Override
	public void initData() {
		super.initData();
		Bundle bundle = getIntent().getExtras();
		MyEntrustItemModel itemData = (MyEntrustItemModel) bundle.get("itemData");
		tvUsername.setText(itemData.getName());
		tvMobile.setText(itemData.getMobile());
		tvAddress.setText(itemData.getProvince_name()+"-"+itemData.getCity_name()+"-"+itemData.getDistrict_name());
		tvArea.setText(itemData.getArea());
		tvRent.setText(itemData.getRental());
		tvRenmark.setText(itemData.getRemark());
		initTab();
	}
	
	private void initTab() {
		menuTitle.setText("委托详情");
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
	
}
