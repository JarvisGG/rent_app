package com.hc.xiaobairent.activity;

import org.kymjs.kjframe.ui.BindView;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hc.core.base.BaseActivity;
import com.hc.xiaobairent.R;
import com.hc.xiaobairent.constants.ZfContants;
import com.hc.xiaobairent.utils.InputVerify;

public class ZfChangeInfoActivity extends BaseActivity{
	// 初始化标题
	@BindView(id = R.id.menu_back, click = true)
	private ImageView menuBack;
	@BindView(id = R.id.menu_title)
	private TextView menuTitle;
	@BindView(id = R.id.menu_title_right, click = true)
	private TextView menuControl;
	
	@BindView(id = R.id.edit_info)
	private EditText infoEdit;
	
	private int type;
	
	@Override
	public void setRootView() {
		setAbContentView(R.layout.zf_activity_person_detail);
	}

	@Override
	public void initData() {
		super.initData();
		type = getIntent().getIntExtra("from", 0);
		initTab();
	}
	
	private void initTab() {
		menuControl.setText("完成");
		String oldContentString = getIntent().getStringExtra("content");
		infoEdit.setText(oldContentString);
		switch (type) {
		case ZfContants.EDIT_NAME:
			menuTitle.setText("用户名");
			infoEdit.setHint("请输入您的用户名");
			break;
		case ZfContants.EDIT_MOBILE:
			menuTitle.setText("手机");
			infoEdit.setHint("请输入您的手机号码");
			infoEdit.setKeyListener(new DigitsKeyListener(false, true));
			break;
		case ZfContants.EDIT_WEIXIN:
			menuTitle.setText("微信号");
			infoEdit.setHint("请输入您的微信号");
			break;
		case ZfContants.EDIT_QQ:
			menuTitle.setText("QQ账号");
			infoEdit.setHint("请输入您的QQ账号");
			infoEdit.setKeyListener(new DigitsKeyListener(false, true));
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.menu_back:
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;
		case R.id.menu_title_right:
			String info = infoEdit.getText().toString();
			Intent intent = new Intent();
			if(type == ZfContants.EDIT_MOBILE) {
				if(!TextUtils.isEmpty(info)) {
					if(!InputVerify.phoneVerify(info)) {
						Toast.makeText(getApplicationContext(), "手机号格式不正确", Toast.LENGTH_SHORT).show();
					} else {
						intent.putExtra("info", info);
						setResult(Activity.RESULT_OK, intent);  
						finish();
						overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
						
					}
				} else {
					Toast.makeText(getApplicationContext(), "手机号不可以为空", Toast.LENGTH_SHORT).show();
				}
			} else {
				intent.putExtra("info", info);
				setResult(Activity.RESULT_OK, intent);  
				finish();
				overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			}
			break;
			
		}
	}

}

