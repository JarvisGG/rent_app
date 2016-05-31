package com.hc.xiaobairent.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.ui.BindView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.google.gson.Gson;
import com.hc.core.base.BaseActivity;
import com.hc.core.utils.RentConstants;
import com.hc.core.utils.SharedpfTools;
import com.hc.core.utils.Sign;
import com.hc.core.utils.SignUtils;
import com.hc.core.utils.UrlConnector;
import com.hc.xiaobairent.MainActivity;
import com.hc.xiaobairent.R;

public class ZfRegister03Activity extends BaseActivity {
	// 标题栏
	@BindView(id = R.id.menu_title)
	private TextView menuTitle;
	@BindView(id = R.id.menu_back, click = true)
	private ImageView menuBack;

	@BindView(id = R.id.next_btu, click = true)
	private Button submitBtn;
	@BindView(id = R.id.edt_userpsd)
	private EditText userPsd;
	@BindView(id = R.id.edt_userrepsd)
	private EditText userRepsd;
	@BindView(id = R.id.agree_isum, click = true)
	private CheckBox mAgree;

	private String psd = "";
	private String repsd = "";
	private String mobile = "";
	private int userType;
	private boolean check = false;

	private SharedpfTools sharedpfTools;
	private AbHttpUtil httpUtil;
	private Sign sign;
	private Gson gson;

	@Override
	public void setRootView() {
		setAbContentView(R.layout.zf_activity_register_step03);
	}

	@Override
	public void initData() {
		super.initData();
		Bundle bundle = getIntent().getExtras();
		mobile = bundle.getString("mobile");
		userType = bundle.getInt("userType");
		userPsd.addTextChangedListener(textWatcher);
		userRepsd.addTextChangedListener(textWatcher);
		sharedpfTools = SharedpfTools.getInstance(ZfRegister03Activity.this);
		httpUtil = AbHttpUtil.getInstance(ZfRegister03Activity.this);
		initTab();
	}

	private void initTab() {
		menuTitle.setText("设置密码");
	}

	@Override
	public void widgetClick(View v) {
		switch (v.getId()) {
		case R.id.menu_back:
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;

		case R.id.agree_isum:
			if (mAgree.isChecked()) {
				check = true;
			} else {
				check = false;
			}
			break;

		case R.id.next_btu:
			if (check) {
				applyData();
			} else {
				Toast.makeText(ZfRegister03Activity.this, "请同意条款", Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}
	}

	// 提交密码
	private void applyData() {
		String source;
		psd = userPsd.getText().toString().trim();
		repsd = userRepsd.getText().toString().trim();
		if (!psd.equals(repsd)) {
			Toast.makeText(ZfRegister03Activity.this, "两次输入的不一致", Toast.LENGTH_SHORT).show();
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("mobile", mobile);
			map.put("pwd", psd);
			map.put("realname", mobile);

			JSONObject jsonObject = new JSONObject(map);
			source = jsonObject.toString();

			AbRequestParams params = new AbRequestParams();
			params.put("data", SignUtils.sign(source, SignUtils.PRIVATE_KEY));

			String url = UrlConnector.REGISTER_USER;
			httpUtil.post(url, params, new AbStringHttpResponseListener() {

				@Override
				public void onStart() {

				}

				@Override
				public void onFinish() {

				}

				@Override
				public void onFailure(int statusCode, String content, Throwable error) {
					Toast.makeText(ZfRegister03Activity.this, "注册失败", Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onSuccess(int statusCode, String content) {
					Log.v("注册返回结果", content);
					Intent intent = new Intent();
					try {
						JSONObject jsonObject = new JSONObject(content);
						int state = jsonObject.getInt("state");
						if (state == 1) {
							sharedpfTools.setAccessToken(jsonObject.getString("access_token"));
							sharedpfTools.setAppsercert(jsonObject.getString("appsercert"));
							Log.e("access_token", jsonObject.getString("access_token"));
							// 设置注册用户类型
							applyChangeUserType();
							switch (userType) {
							case RentConstants.BROKER:
							case RentConstants.OWNER:
								intent.setClass(ZfRegister03Activity.this, ZfCertifiedActivity.class);
								break;
							case RentConstants.USER:
								intent.setClass(ZfRegister03Activity.this, ZfLoginActivity.class);
							default:
								break;
							}
							startActivity(intent);
							overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
							Toast.makeText(ZfRegister03Activity.this, "注册成功", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(ZfRegister03Activity.this, "注册失败", Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	private void applyChangeUserType() {
		AbRequestParams params = new AbRequestParams();
		sign = new Sign(ZfRegister03Activity.this);
		sign.init();
		sign.addParam("rent_user_type", userType);
		params.put("rent_user_type", userType);
		String url = UrlConnector.SETGETUSERTYPE + sharedpfTools.getAccessToken() + UrlConnector.SIGN + sign.getSign();
		httpUtil.post(url, params, new AbStringHttpResponseListener() {

			@Override
			public void onStart() {

			}

			@Override
			public void onFinish() {

			}

			@Override
			public void onFailure(int statusCode, String content, Throwable error) {
				Toast.makeText(ZfRegister03Activity.this, "获取错误", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				Log.e("setUserType", content);
				try {
					JSONObject jsonObject = new JSONObject(content);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});
	}

	TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			psd = userPsd.getText().toString().trim();
			repsd = userRepsd.getText().toString().trim();
			if (psd.length() != 0 && repsd.length() != 0) {
				submitBtn.setBackground(getResources().getDrawable(R.drawable.btn_hl));
				submitBtn.setClickable(true);
			} else {
				submitBtn.setBackground(getResources().getDrawable(R.drawable.btn_d));
				submitBtn.setClickable(false);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			psd = userPsd.getText().toString().trim();
			repsd = userRepsd.getText().toString().trim();
			if (psd.length() != 0 && repsd.length() != 0) {
				submitBtn.setBackground(getResources().getDrawable(R.drawable.btn_hl));
				submitBtn.setClickable(true);
			} else {
				submitBtn.setBackground(getResources().getDrawable(R.drawable.btn_d));
				submitBtn.setClickable(false);
			}
		}
	};
}
