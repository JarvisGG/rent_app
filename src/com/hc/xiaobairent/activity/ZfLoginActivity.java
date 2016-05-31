package com.hc.xiaobairent.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.ui.BindView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.hc.core.base.BaseActivity;
import com.hc.core.utils.SharedpfTools;
import com.hc.core.utils.Sign;
import com.hc.core.utils.SignUtils;
import com.hc.core.utils.UrlConnector;
import com.hc.xiaobairent.MainActivity;
import com.hc.xiaobairent.R;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ZfLoginActivity extends BaseActivity {

	protected static final String TAG = "ZfLoginActivity";
	// 初始化标题
	@BindView(id = R.id.menu_back, click = true)
	private ImageView menuBack;
	@BindView(id = R.id.menu_title)
	private TextView menuTitle;
	@BindView(id = R.id.menu_title_right, click = true)
	private TextView menuRight;

	/** 登录框 */
	@BindView(id = R.id.edt_username)
	private EditText edt_username;
	@BindView(id = R.id.edt_pass)
	private EditText edt_pass;
	@BindView(id = R.id.login_btu, click = true)
	private Button login_btu;
	@BindView(id = R.id.tv_find_pass, click = true)
	private TextView tv_find_pass;
	@BindView(id = R.id.weixin_login_ll, click = true)
	private LinearLayout weixin_login_ll;
	@BindView(id = R.id.qq_login_ll, click = true)
	private LinearLayout qq_login_ll;
	private SharedpfTools sharedpfTools = SharedpfTools.getInstance(this);
	private AbRequestParams params;
	private AbHttpUtil http;
	private String username = null;
	private String password = null;
	private UMShareAPI mShareAPI;
	private SHARE_MEDIA platform;
	private String access_token = "";
	private Context context = this;
	// 0-正常登录       1-三方登录
	private int LoginMethod = 0;

	@Override
	public void setRootView() {
		setAbContentView(R.layout.zf_activity_login);
	}

	@Override
	public void initData() {
		super.initData();
		http = AbHttpUtil.getInstance(ZfLoginActivity.this);
		http.setTimeout(10000);
		edt_username.addTextChangedListener(watcher);
		edt_pass.addTextChangedListener(watcher);
//		edt_username.setText("15054257701");
//		edt_pass.setText("123456");
		mShareAPI = UMShareAPI.get(this);
		initTab();
	}

	private void initTab() {
		menuTitle.setText("登录");
		menuRight.setText("注册");
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.menu_back:
			hideInput();
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;
		case R.id.login_btu:
			login();
			break;
		case R.id.menu_title_right:
			startActivity(new Intent(ZfLoginActivity.this, ZfRegister01Activity.class));
			overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			break;
		case R.id.tv_find_pass:
			startActivity(new Intent(ZfLoginActivity.this, ZfForgetPsdStep01.class));
			overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			break;
		case R.id.weixin_login_ll:
			platform = SHARE_MEDIA.WEIXIN;
			mShareAPI.doOauthVerify(this, platform, loginListener);
			break;
		case R.id.qq_login_ll:
			platform = SHARE_MEDIA.QQ;
			mShareAPI.doOauthVerify(this, platform, loginListener);
			break;
		default:
			break;
		}
	}

	private UMAuthListener loginListener = new UMAuthListener() {
		@Override
		public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
			Toast.makeText(ZfLoginActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
			access_token = data.get("access_token");
			mShareAPI.isInstall(ZfLoginActivity.this, ZfLoginActivity.this.platform);
			mShareAPI.getPlatformInfo(ZfLoginActivity.this, ZfLoginActivity.this.platform, getDataListener);
		}

		@Override
		public void onError(SHARE_MEDIA platform, int action, Throwable t) {
			Toast.makeText(getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel(SHARE_MEDIA platform, int action) {
			Toast.makeText(getApplicationContext(), "取消授权", Toast.LENGTH_SHORT).show();
		}
	};
	private UMAuthListener getDataListener = new UMAuthListener() {
		@Override
		public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
			Toast.makeText(getApplicationContext(), "授权成功", Toast.LENGTH_SHORT).show();
			// Log.e(TAG, "Authorize succeed");
			// if (data != null) {
			// Log.e(TAG, data.toString());
			// for (String i : data.keySet()) {
			// Log.e(TAG, i + ">>" + data.get(i));
			// }
			// } else {
			// Log.e(TAG, "data is null");
			// }
			if (ZfLoginActivity.this.platform == SHARE_MEDIA.WEIXIN) {
				weixinLogin(data.get("nickname"), access_token, data.get("openid"), data.get("headimgurl"));
			} else {
				qqLogin(data.get("screen_name"), access_token, data.get("openid"), data.get("profile_image_url"));
			}
		}

		@Override
		public void onError(SHARE_MEDIA platform, int action, Throwable t) {
			Toast.makeText(getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel(SHARE_MEDIA platform, int action) {
			Toast.makeText(getApplicationContext(), "取消授权", Toast.LENGTH_SHORT).show();
		}
	};

	private void qqLogin(String t_nickname, String access_token, String openid, String t_photo) {
		params = new AbRequestParams();
		params.put("t_nickname", t_nickname);
		params.put("access_token", access_token);
		params.put("openid", openid);
		params.put("t_photo", t_photo);
		http.post(UrlConnector.QQ_LOGIN, params, new AbStringHttpResponseListener() {

			@Override
			public void onStart() {
			}

			@Override
			public void onFinish() {
			}

			@Override
			public void onFailure(int statusCode, String content, Throwable error) {
				Toast.makeText(ZfLoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				Log.v("登陆返回结果", content);
				try {
					JSONObject object = new JSONObject(content);
					sharedpfTools.setAccessToken(object.getString("access_token"));
					sharedpfTools.setAppsercert(object.getString("appsercert"));
					// sharedpfTools.setUserType(object.getInt("rent_user_type"));
					LoginMethod = 1;
					applyUserType();
					Log.e("access_token", object.getString("access_token"));
					Log.e("appsercert", object.getString("appsercert"));
					Toast.makeText(ZfLoginActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});

	}

	private void weixinLogin(String t_nickname, String access_token, String openid, String t_photo) {
		params = new AbRequestParams();
		params.put("t_nickname", t_nickname);
		params.put("access_token", access_token);
		params.put("openid", openid);
		params.put("t_photo", t_photo);
		http.post(UrlConnector.XEIXIN_LOGIN, params, new AbStringHttpResponseListener() {

			@Override
			public void onStart() {
			}

			@Override
			public void onFinish() {
			}

			@Override
			public void onFailure(int statusCode, String content, Throwable error) {
				Toast.makeText(ZfLoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				Log.v("登陆返回结果", content);
				try {
					JSONObject object = new JSONObject(content);
					sharedpfTools.setAccessToken(object.getString("access_token"));
					sharedpfTools.setAppsercert(object.getString("appsercert"));
					// sharedpfTools.setUserType(object.getInt("rent_user_type"));
					LoginMethod = 1;
					applyUserType();
					Log.e("access_token", object.getString("access_token"));
					Log.e("appsercert", object.getString("appsercert"));
					Toast.makeText(ZfLoginActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});

	}

	private void login() {
		String source;
		username = edt_username.getText().toString().trim();
		password = edt_pass.getText().toString().trim();
		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
			username = "18724796251";
			password = "888888";
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mobile", username);
		map.put("pwd", password);
		JSONObject jsonObject = new JSONObject(map);
		source = jsonObject.toString();
		params = new AbRequestParams();
		params.put("data", SignUtils.sign(source, SignUtils.PRIVATE_KEY));
		http.post(UrlConnector.LOGIN_URL, params, new AbStringHttpResponseListener() {

			@Override
			public void onStart() {
			}

			@Override
			public void onFinish() {
			}

			@Override
			public void onFailure(int statusCode, String content, Throwable error) {
				Toast.makeText(ZfLoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				Log.v("登陆返回结果", content);
				try {
					JSONObject object = new JSONObject(content);
					Toast.makeText(ZfLoginActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
					sharedpfTools.setAccessToken(object.getString("access_token"));
					sharedpfTools.setAppsercert(object.getString("appsercert"));
					LoginMethod = 0;
					applyUserType();
					Log.e("access_token", object.getString("access_token"));
					Log.e("appsercert", object.getString("appsercert"));
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});

	}

	private void applyUserType() {
		Sign sign = new Sign(context);
		sign.init();
		String url = UrlConnector.GETUSERTYPE + sharedpfTools.getAccessToken() + UrlConnector.SIGN + sign.getSign();
		http.get(url, new AbStringHttpResponseListener() {

			@Override
			public void onStart() {

			}

			@Override
			public void onFinish() {

			}

			@Override
			public void onFailure(int statusCode, String content, Throwable error) {

			}

			@Override
			public void onSuccess(int statusCode, String content) {
				Log.v("获取用户类型", content);
				JSONObject object;
				try {
					object = new JSONObject(content);
					sharedpfTools.setUserType(object.getInt("type"));
					sharedpfTools.setLogStatus(true);
					// 隐藏软键盘
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
							getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					Intent intent = new Intent();
					//intent.putExtra("LoginMethod", LoginMethod);
					sharedpfTools.setLoginMethod(LoginMethod+"");
					intent.setClass(ZfLoginActivity.this, MainActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
					finish();
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});
	}

	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			username = edt_username.getText().toString().trim();
			password = edt_pass.getText().toString().trim();
			if (username != null && password != null) {
				login_btu.setBackground(getResources().getDrawable(R.drawable.btn_hl));
				login_btu.setClickable(true);
			} else {
				login_btu.setBackground(getResources().getDrawable(R.drawable.btn_d));
				login_btu.setClickable(false);
			}

		}

		@Override
		public void afterTextChanged(Editable s) {

			username = edt_username.getText().toString().trim();
			password = edt_pass.getText().toString().trim();
			if (username != null && password != null) {
				login_btu.setBackground(getResources().getDrawable(R.drawable.btn_hl));
				login_btu.setClickable(true);
			} else {
				login_btu.setBackground(getResources().getDrawable(R.drawable.btn_d));
				login_btu.setClickable(false);
			}
		}
	};

	// 隐藏软键盘
	private void hideInput() {
		View view = getCurrentFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mShareAPI.onActivityResult(requestCode, resultCode, data);
	}
}
