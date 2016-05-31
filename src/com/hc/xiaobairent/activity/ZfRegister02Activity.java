package com.hc.xiaobairent.activity;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.ui.BindView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.hc.core.base.BaseActivity;
import com.hc.core.utils.RentConstants;
import com.hc.core.utils.SharedpfTools;
import com.hc.core.utils.UrlConnector;
import com.hc.xiaobairent.R;
import com.hc.xiaobairent.utils.InputVerify;

public class ZfRegister02Activity extends BaseActivity{
	
	// 标题栏
	@BindView(id = R.id.menu_title)
	private TextView menuTitle;
	@BindView(id = R.id.menu_back, click = true)
	private ImageView menuBack;
	
	// 输入框
	@BindView(id = R.id.edt_userphone)
	private EditText edtUserphone;
	@BindView(id = R.id.get_verifycode, click = true)
	private Button getVercode;
	@BindView(id = R.id.edt_vercode)
	private EditText edtVercode;
	@BindView(id = R.id.next_btu, click = true)
	private Button submitBtn;
	
	private String userPhone;
	private String vercode;
	/** 用户类型 */
	private SharedpfTools sharedpfTools;
	private int userType;
	
	/** 倒计时 */
	private int countNum = 60;
	
	// 网络参数
	private AbHttpUtil httpUtil;
	private AbRequestParams params;
	

	@Override
	public void setRootView() {
		setAbContentView(R.layout.zf_activity_register_step02);
	}
	
	@Override
	public void initData() {
		super.initData();
		edtUserphone.addTextChangedListener(textWatcher);
		edtVercode.addTextChangedListener(textWatcher);
		httpUtil = AbHttpUtil.getInstance(getApplicationContext());
		sharedpfTools = SharedpfTools.getInstance(getApplicationContext());
		
		
		Bundle bundle = getIntent().getExtras();
		userType = bundle.getInt("userType");
//		userType = sharedpfTools.getUserType();
		
		initTab();
	}
	
	private void initTab() {
		menuTitle.setText("注册");
	}
	
	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.menu_back:
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;
			
		case R.id.get_verifycode:
			if(countNum == 60) {
				userPhone = edtUserphone.getText().toString().trim();
				if(!TextUtils.isEmpty(userPhone) && InputVerify.phoneVerify(userPhone)) {
					getCodeSend();	
				} else {
					Toast.makeText(getApplicationContext(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
				}
			}
			break;
			
		case R.id.next_btu:
			applyData();
			break;

		default:
			break;
		}
	}
	
	private void applyData() {
		userPhone = edtUserphone.getText().toString().trim();
		vercode = edtVercode.getText().toString().trim();
		String url = UrlConnector.PHONE_VER;
		params = new AbRequestParams();
		params.put("type", 1);
		params.put("mobile", userPhone);
		params.put("code", vercode);
		
		httpUtil.post(url, params,new AbStringHttpResponseListener() {
			
			@Override
			public void onStart() {
				
			}
			
			@Override
			public void onFinish() {
				
			}
			
			@Override
			public void onFailure(int statusCode, String content, Throwable error) {
				Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT).show();						
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				try {
					JSONObject jsonObject = new JSONObject(content);
					int state = jsonObject.getInt("state");
					if(state == 1) {
						switch (userType) {
						case RentConstants.BROKER:
						case RentConstants.OWNER:
							Intent intent = new Intent();
							intent.putExtra("mobile", userPhone);
							intent.putExtra("userType", userType);
							intent.setClass(getApplicationContext(), ZfRegister03Activity.class);
							startActivity(intent);
							overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
							break;
							
						case RentConstants.USER:
							Intent intent1 = new Intent();
							intent1.putExtra("mobile", userPhone);
							intent1.putExtra("userType", userType);
							intent1.setClass(getApplicationContext(), ZfRegister03Activity.class);
							startActivity(intent1);
							overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
							break;

						default:
							break;
						}
					} else {
						Toast.makeText(getApplicationContext(), "验证码错误", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
						
				
			}
		});
	}
	
	private void getCodeSend() {
		// 等待
		new Thread(new CountSecond()).start();
		// 请求后台发送验证码
		String url = UrlConnector.PHONE_VERCODE;
		params = new AbRequestParams();
		params.put("type", 1);
		params.put("mobile", userPhone);
		
		httpUtil.post(url, params, new AbStringHttpResponseListener() {
			
			@Override
			public void onStart() {
				
			}
			
			@Override
			public void onFinish() {
				
			}
			
			@Override
			public void onFailure(int statusCode, String content, Throwable error) {
				Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();				
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				try {
					JSONObject jsonObject = new JSONObject(content);
					int state = jsonObject.getInt("state");
					if(state == 1) {
						Toast.makeText(getApplicationContext(), "验证码发送成功", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}			
			}
		});
	}
	
	TextWatcher textWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			userPhone = edtUserphone.getText().toString().trim();
			vercode = edtVercode.getText().toString().trim();
			if(userPhone.length() != 0 && vercode.length() != 0) {
				submitBtn.setBackground(getResources().getDrawable(R.drawable.btn_hl));
				submitBtn.setClickable(true);
			} else {
				submitBtn.setBackground(getResources().getDrawable(R.drawable.btn_d));
				submitBtn.setClickable(false);
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			userPhone = edtUserphone.getText().toString().trim();
			vercode = edtVercode.getText().toString().trim();
			if(userPhone.length() != 0 && vercode.length() != 0) {
				submitBtn.setBackground(getResources().getDrawable(R.drawable.btn_hl));
				submitBtn.setClickable(true);
			} else {
				submitBtn.setBackground(getResources().getDrawable(R.drawable.btn_d));
				submitBtn.setClickable(false);
			}
		}
	};

	class CountSecond implements Runnable {

		@Override
		public void run() {
			while(countNum > 0) {
				handler.sendEmptyMessage(1);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private Handler handler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 1) {
				countNum--;
				if(countNum > 0) {
					getVercode.setClickable(false);
					getVercode.setText("重新获取 "+countNum+"s");
				} else {
					getVercode.setClickable(true);
					getVercode.setText("获取验证码");
					getVercode.setBackgroundResource(R.drawable.button_verifycode);
					getVercode.setTextColor(Color.parseColor("#737373"));
				}
			}
		};
	};
}
