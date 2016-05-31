package com.hc.xiaobairent.activity;

import org.kymjs.kjframe.ui.BindView;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hc.core.base.BaseActivity;
import com.hc.xiaobairent.R;

public class ZfForRenewDateActivity extends BaseActivity{

	// 初始化标题
	@BindView(id = R.id.menu_back, click = true)
	private ImageView menuBack;
	@BindView(id = R.id.menu_title)
	private TextView menuTitle;
	
	@BindView(id = R.id.renew_date1, click = true)
	private LinearLayout date1;
	@BindView(id = R.id.renew_date2, click = true)
	private LinearLayout date2;
	@BindView(id = R.id.renew_date3, click = true)
	private LinearLayout date3;
	@BindView(id = R.id.renew_date4, click = true)
	private LinearLayout date4;
	@BindView(id = R.id.renew_date5, click = true)
	private LinearLayout date5;
	@BindView(id = R.id.renew_date6, click = true)
	private LinearLayout date6;
	@BindView(id = R.id.renew_date7, click = true)
	private LinearLayout date7;
	@BindView(id = R.id.renew_date8, click = true)
	private LinearLayout date8;
	@BindView(id = R.id.renew_date9, click = true)
	private LinearLayout date9;
	@BindView(id = R.id.renew_date10, click = true)
	private LinearLayout date10;
	@BindView(id = R.id.renew_date11, click = true)
	private LinearLayout date11;
	@BindView(id = R.id.renew_date12, click = true)
	private LinearLayout date12;
	
	@Override
	public void setRootView() {
		setAbContentView(R.layout.zf_activity_renew_date);
		
	}
	
	@Override
	public void initData() {
		super.initData();
	}
	
	
	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		Intent intent;
		switch (v.getId()) {
		case R.id.menu_back:
			intent = new Intent();
			setResult(RESULT_CANCELED, intent);
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;
		
		case R.id.renew_date1:
			intent = new Intent();
			intent.putExtra("date", 1);
			setResult(RESULT_OK, intent);
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;
		case R.id.renew_date2:
			intent = new Intent();
			intent.putExtra("date", 2);
			setResult(RESULT_OK, intent);
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;
		case R.id.renew_date3:
			intent = new Intent();
			intent.putExtra("date", 3);
			setResult(RESULT_OK, intent);
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;
		case R.id.renew_date4:
			intent = new Intent();
			intent.putExtra("date", 4);
			setResult(RESULT_OK, intent);
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;
		case R.id.renew_date5:
			intent = new Intent();
			intent.putExtra("date", 5);
			setResult(RESULT_OK, intent);
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;
		case R.id.renew_date6:
			intent = new Intent();
			intent.putExtra("date", 6);
			setResult(RESULT_OK, intent);
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;
		case R.id.renew_date7:
			intent = new Intent();
			intent.putExtra("date", 7);
			setResult(RESULT_OK, intent);
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;
		case R.id.renew_date8:
			intent = new Intent();
			intent.putExtra("date", 8);
			setResult(RESULT_OK, intent);
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;
		case R.id.renew_date9:
			intent = new Intent();
			intent.putExtra("date", 9);
			setResult(RESULT_OK, intent);
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;
		case R.id.renew_date10:
			intent = new Intent();
			intent.putExtra("date", 10);
			setResult(RESULT_OK, intent);
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;
		case R.id.renew_date11:
			intent = new Intent();
			intent.putExtra("date", 11);
			setResult(RESULT_OK, intent);
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;
		case R.id.renew_date12:
			intent = new Intent();
			intent.putExtra("date", 12);
			setResult(RESULT_OK, intent);
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;

		default:
			break;
		}
	}
}