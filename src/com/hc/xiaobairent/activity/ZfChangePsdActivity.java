package com.hc.xiaobairent.activity;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.ui.BindView;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.hc.core.base.BaseActivity;
import com.hc.core.utils.SharedpfTools;
import com.hc.core.utils.Sign;
import com.hc.core.utils.UrlConnector;
import com.hc.xiaobairent.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class ZfChangePsdActivity extends BaseActivity{
	
	// 初始化标题
	@BindView(id = R.id.menu_back, click = true)
	private ImageView menuBack;
	@BindView(id = R.id.menu_title)
	private TextView menuTitle;
	@BindView(id = R.id.menu_title_right, click = true)
	private TextView menuControl;
	@BindView(id = R.id.user_oldpsd)
	private EditText userOldPsd;
	@BindView(id = R.id.user_newpsd)
	private EditText userNewPsd;
	@BindView(id = R.id.user_repsd)
	private EditText userRePsd;
	@BindView(id = R.id.next_btu, click = true)
	private Button submitBtn;
	
	// 网络参数
	private SharedpfTools sharedpfTools = SharedpfTools.getInstance(ZfChangePsdActivity.this);
	private HttpUtils httpUtils;
	private RequestParams requestParams;
	private Sign sign;
	private AbHttpUtil http;

	private String oldPsd = null;
	private String newPsd = null;
	private String rePsd = null;
	
	@Override
	public void setRootView() {
		setAbContentView(R.layout.zf_activity_changepsd);
	}
	
	@Override
	public void initData() {
		super.initData();
		userOldPsd.addTextChangedListener(watcher);
		userNewPsd.addTextChangedListener(watcher);
		userRePsd.addTextChangedListener(watcher);
		sign = new Sign(ZfChangePsdActivity.this);
		sign.init();
		http = AbHttpUtil.getInstance(ZfChangePsdActivity.this);
		httpUtils = new HttpUtils();
		
//		
//		userOldPsd.setText("123456");
//		userNewPsd.setText("888888");
//		userRePsd.setText("888888");
		initTab();
	}
	
	private void initTab() {
		menuTitle.setText("修改密码");
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.menu_back:
			View view = getCurrentFocus();
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			if(imm != null) {
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			}
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;
		
		case R.id.next_btu:
			
			AbRequestParams params = new AbRequestParams();
			oldPsd = userOldPsd.getText().toString().trim();
			newPsd = userNewPsd.getText().toString().trim();
			rePsd = userRePsd.getText().toString().trim();
			if(oldPsd.length() != 0) {
				params.put("ypassword", oldPsd);
				sign.addParam("ypassword", oldPsd);
			}
			if(newPsd.length() != 0) {
				params.put("password", newPsd);
				sign.addParam("password", newPsd);
			}
			if(rePsd.length() != 0) {
				params.put("password_", rePsd);
				sign.addParam("password_", rePsd);
			}
			String url = UrlConnector.CHANGE_PAS+sharedpfTools.getAccessToken()+UrlConnector.SIGN + sign.getSign();	
			http.post(url, params, new AbStringHttpResponseListener() {
				
				@Override
				public void onStart() {
					
				}
				
				@Override
				public void onFinish() {
					
				}
				
				@Override
				public void onFailure(int statusCode, String content, Throwable error) {
					Toast.makeText(ZfChangePsdActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
					
				}
				
				@Override
				public void onSuccess(int statusCode, String content) {
					try {
						JSONObject jsonObject = new JSONObject(content);
						int state = jsonObject.getInt("state");
						if(state == 1) {
							Toast.makeText(ZfChangePsdActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
							startActivity(new Intent(ZfChangePsdActivity.this, ZfLoginActivity.class));
							overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
						} else {
							Toast.makeText(ZfChangePsdActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
			
			break;
		
		default:
			break;
		}
	}
	
	private TextWatcher watcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			oldPsd = userNewPsd.getText().toString().trim();
			newPsd = userNewPsd.getText().toString().trim();
			rePsd = userRePsd.getText().toString().trim();
			if(oldPsd.length()!= 0 && newPsd.length() != 0 && rePsd.length() != 0) {
				changeBtnStatus(1);
			} else {
				changeBtnStatus(2);
			}			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			oldPsd = userNewPsd.getText().toString().trim();
			newPsd = userNewPsd.getText().toString().trim();
			rePsd = userRePsd.getText().toString().trim();
			if(oldPsd.length()!= 0 && newPsd.length() != 0 && rePsd.length() != 0) {
				changeBtnStatus(1);
			} else {
				changeBtnStatus(2);
			}
			
		}
	};
	
	private void changeBtnStatus(int command) {
		switch (command) {
		case 1:
			submitBtn.setBackground(getResources().getDrawable(R.drawable.btn_hl));
			submitBtn.setClickable(true);
			break;
		case 2:
			submitBtn.setBackground(getResources().getDrawable(R.drawable.btn_d));
			submitBtn.setClickable(false);
			break;
		default:
			break;
		}
	}

}
