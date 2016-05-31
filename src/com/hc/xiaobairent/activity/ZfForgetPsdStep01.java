package com.hc.xiaobairent.activity;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.ui.BindView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.hc.xiaobairent.activity.ZfRegister02Activity.CountSecond;
import com.hc.xiaobairent.utils.InputVerify;

public class ZfForgetPsdStep01 extends BaseActivity{
	/** 顶部标题 */
	@BindView(id = R.id.menu_title)
	private TextView tvTopbar;
	@BindView(id = R.id.menu_back, click = true)
	private ImageView ivBack;
	@BindView(id = R.id.menu_title_right)
	private TextView ivMineRigth;
	
	/** 手机框 */
	@BindView(id = R.id.edt_userphone)
	private EditText edtUserphone;
	@BindView(id = R.id.edt_vercode)
	private EditText edtVercode;
	@BindView(id = R.id.next_btu, click = true)
	private Button submitBtn;
	@BindView(id = R.id.get_verifycode, click = true)
	private Button getVercode;
	
	private String userPhone;
	private String vercode;
	
	private SharedpfTools sharedpfTools;
	
	/** 倒计时 */
	private int countNum = 60;
	
	private AbHttpUtil httpUtil;
	private AbRequestParams params;
	
	@Override
	public void setRootView() {
		setAbContentView(R.layout.zf_activity_changepsd_step01);
		
	}
	
	@Override
	public void initData() {
		super.initData();
		edtUserphone.addTextChangedListener(textWatcher);
		edtVercode.addTextChangedListener(textWatcher);
		httpUtil = AbHttpUtil.getInstance(getApplicationContext());
		sharedpfTools = SharedpfTools.getInstance(getApplicationContext());
		initTab();
	}
	
	private void initTab() {

		ivMineRigth.setVisibility(View.VISIBLE);
		ivBack.setVisibility(View.VISIBLE);
		tvTopbar.setVisibility(View.VISIBLE);
		tvTopbar.setText("忘记密码");
		ivMineRigth.setVisibility(View.VISIBLE);
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
						Intent intent = new Intent();
						intent.putExtra("mobile", userPhone);
						intent.putExtra("vercode", vercode);
						intent.setClass(getApplicationContext(), ZfForgetPsdStep02.class);
						startActivity(intent);
						overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
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
		params.put("type", 2);
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
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		
		if(ev.getAction() == MotionEvent.ACTION_DOWN) {
			View view = getCurrentFocus();
			if(isShouldHideInput(view, ev)) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if(imm != null) {
					imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
				}
			}
			return super.dispatchTouchEvent(ev);
		}
		 // 必不可少，否则所有的组件都不会有TouchEvent了  
		if (getWindow().superDispatchTouchEvent(ev)) {  
	        return true;  
	    }  
	    return onTouchEvent(ev); 
	}
	
	public boolean isShouldHideInput(View v, MotionEvent event) {
		if(v != null && (v instanceof EditText)) {
			int[] leftTop = {0, 0};
			v.getLocationInWindow(leftTop);
			int left = leftTop[0];  
	        int top = leftTop[1];  
	        int bottom = top + v.getHeight();  
	        int right = left + v.getWidth(); 
	        if(event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
	        	return false;
	        } else {
	        	return true;
	        }
		}
		return false;  
	}
}
