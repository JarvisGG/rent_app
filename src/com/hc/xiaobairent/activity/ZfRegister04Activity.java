package com.hc.xiaobairent.activity;

import org.kymjs.kjframe.ui.BindView;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hc.core.base.BaseActivity;
import com.hc.xiaobairent.R;

public class ZfRegister04Activity extends BaseActivity{

	// 标题栏
	@BindView(id = R.id.menu_title)
	private TextView menuTitle;
	@BindView(id = R.id.menu_back, click = true)
	private ImageView menuBack;
	@BindView(id = R.id.next_btu, click = true)
	private Button submitBtn;
	@Override
	public void setRootView() {
		setAbContentView(R.layout.zf_activity_register_step04);
	}
	
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();
	}
	
	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.menu_back:
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;

		case R.id.next_btu:
			startActivity(new Intent(ZfRegister04Activity.this, ZfLoginActivity.class));
			overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_in);
			break;
		default:
			break;
		}
	}

}
