package com.hc.xiaobairent.activity;

import org.kymjs.kjframe.ui.BindView;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hc.core.base.BaseActivity;
import com.hc.core.utils.RentConstants;
import com.hc.core.utils.SharedpfTools;
import com.hc.xiaobairent.R;

public class ZfRegister01Activity extends BaseActivity{

	// 标题栏
	@BindView(id = R.id.menu_title)
	private TextView menuTitle;
	@BindView(id = R.id.menu_back, click = true)
	private ImageView menuBack;
	
	// 身份选择
	@BindView(id = R.id.identity_landlord, click = true)
	private LinearLayout landlord;
	@BindView(id = R.id.identity_agent, click = true)
	private LinearLayout agent;
	@BindView(id = R.id.identity_tenant, click = true)
	private LinearLayout tenant;
	
	private SharedpfTools sharedpfTools;
	
	@Override
	public void setRootView() {
		setAbContentView(R.layout.zf_activity_register_step01);
	}
	
	@Override
	public void initData() {
		super.initData();
		sharedpfTools = SharedpfTools.getInstance(getApplicationContext());
		initTab();
	}
	
	private void initTab() {
		menuTitle.setText("注册身份");
	}
	
	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.menu_back:
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;
		
		case R.id.identity_landlord:
			//sharedpfTools.setUserType(RentConstants.OWNER);
			intent.putExtra("userType", RentConstants.OWNER);
			intent.setClass(getApplicationContext(), ZfRegister02Activity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			break;
		
		case R.id.identity_agent:
			//sharedpfTools.setUserType(RentConstants.BROKER);
			intent.putExtra("userType", RentConstants.BROKER);
			intent.setClass(getApplicationContext(), ZfRegister02Activity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			break;
		
		case R.id.identity_tenant:
			//sharedpfTools.setUserType(RentConstants.USER);
			intent.putExtra("userType", RentConstants.USER);
			intent.setClass(getApplicationContext(), ZfRegister02Activity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			break;
		default:
			break;
		}
	}
	

}
