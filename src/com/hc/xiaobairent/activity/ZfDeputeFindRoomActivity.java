package com.hc.xiaobairent.activity;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.BindView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.hc.core.base.BaseActivity;
import com.hc.core.utils.SharedpfTools;
import com.hc.core.utils.Sign;
import com.hc.core.utils.UrlConnector;
import com.hc.xiaobairent.R;
import com.hc.xiaobairent.utils.InputVerify;

public class ZfDeputeFindRoomActivity extends BaseActivity {

	// 初始化标题
	@BindView(id = R.id.menu_back, click = true)
	private ImageView menuBack;
	@BindView(id = R.id.menu_title)
	private TextView menuTitle;

	@BindView(id = R.id.username)
	private EditText tvUsername;
	@BindView(id = R.id.userphone)
	private EditText tvUserphone;
	@BindView(id = R.id.user_address, click = true)
	private LinearLayout userAddress;
	@BindView(id = R.id.tv_address)
	private TextView tvAddress;
	@BindView(id = R.id.user_area, click = true)
	private LinearLayout userArea;
	@BindView(id = R.id.tv_area)
	private TextView tvArea;
	@BindView(id = R.id.user_rent, click = true)
	private LinearLayout userRent;
	@BindView(id = R.id.tv_rent)
	private TextView tvRent;
	@BindView(id = R.id.tv_remark)
	private TextView tvRemark;
	@BindView(id = R.id.sub_btn, click = true)
	private Button subBtn;
	// 回掉参数
	private static final int SELECT_AREA = 101;
	private static final int SELECT_SIZE = 102;
	private static final int SELECT_RENT = 103;

	// form 表单
	private String name = null;
	private String mobile = null;
	private int province_id = 0;
	private int city_id = 0;
	private int district_id = 0;
	private String address = null;
	private String rental = "";
	private String remark = "";
	private int size_id;
	private int price_id;

	// 网络参数
	private AbHttpUtil httpUtil;
	private Sign sign;
	private SharedpfTools sharedpfTools = SharedpfTools.getInstance(ZfDeputeFindRoomActivity.this);
	private String mUrl;

	@Override
	public void setRootView() {
		setAbContentView(R.layout.zf_activity_deputefindroom);
	}

	@Override
	public void initData() {
		super.initData();
		initTab();
		tvUsername.addTextChangedListener(watcher);
		tvUserphone.addTextChangedListener(watcher);
		tvAddress.addTextChangedListener(watcher);
		subBtn.setClickable(false);
	}

	private void initTab() {
		menuTitle.setText("委托找房");
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		Intent intent = null;
		switch (v.getId()) {
		case R.id.menu_back:
			hideInput();
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;

		case R.id.user_address:
			intent = new Intent(this, ZfAreaSelectedActivity.class);
			startActivityForResult(intent, SELECT_AREA);
			overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			break;

		case R.id.user_area:
			intent = new Intent(this, ZfSizeSelectActivity.class);
			startActivityForResult(intent, SELECT_SIZE);
			overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			break;

		case R.id.user_rent:
			intent = new Intent(this, ZfPriceSelectActivity.class);
			startActivityForResult(intent, SELECT_RENT);
			overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			break;

		case R.id.sub_btn:
			name = tvUsername.getText().toString().trim();
			mobile = tvUserphone.getText().toString().trim();
			remark = tvRemark.getText().toString().trim();
			if (InputVerify.phoneVerify(mobile)) {
				dataSubmit();
			} else {
				Toast.makeText(ZfDeputeFindRoomActivity.this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
			}

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			switch (requestCode) {
			case SELECT_AREA:
				Bundle addressData = data.getExtras();
				String province = addressData.getString("province");
				String city = addressData.getString("city");
				String area = addressData.getString("area");
				province_id = Integer.parseInt(addressData.getString("province_id"));
				city_id = Integer.parseInt(addressData.getString("city_id"));
				district_id = Integer.parseInt(addressData.getString("area_id"));
				address = province + "-" + city + "-" + area;
				tvAddress.setText(address);
				break;

			case SELECT_SIZE:
				Bundle sizeData = data.getExtras();
				String size = sizeData.getString("size");
				size_id = Integer.parseInt(sizeData.getString("size_id"));
				tvArea.setText(size);
				area = size;
				break;
			case SELECT_RENT:
				Bundle priceData = data.getExtras();
				String price = priceData.getString("price");
				price_id = Integer.parseInt(priceData.getString("price_id"));
				tvRent.setText(price);
				rental = price;
				break;
			default:
				break;
			}
			break;
		}
	}

	private void dataSubmit() {
		sign = new Sign(ZfDeputeFindRoomActivity.this);
		sign.init();

		httpUtil = AbHttpUtil.getInstance(ZfDeputeFindRoomActivity.this);
		AbRequestParams params = new AbRequestParams();
		params.put("name", name);
		params.put("mobile", mobile);
		params.put("province_id", province_id);
		params.put("city_id", city_id);
		params.put("district_id", district_id);
		params.put("area_shuttle", size_id);
		params.put("rental_shuttle", price_id);
		//params.put("remark", remark);
		sign.addParam("name", name);
		sign.addParam("mobile", mobile);
		sign.addParam("province_id", province_id);
		sign.addParam("city_id", city_id);
		sign.addParam("district_id", district_id);
		sign.addParam("area_shuttle", size_id);
		sign.addParam("rental_shuttle", price_id);
		//sign.addParam("remark", remark);
		if(!TextUtils.isEmpty(remark)) {
			params.put("remark", remark);
			sign.addParam("remark", remark);
		}
		Log.e("param", params.getUrlParams().toString());
		mUrl = UrlConnector.ROOMSUBMIT + sharedpfTools.getAccessToken() + UrlConnector.SIGN + sign.getSign();
		Log.e("onSubmit", mUrl);

		httpUtil.post(mUrl, params, new AbStringHttpResponseListener() {

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
				Log.e("onSuccessSubmit", content);
				try {
					JSONObject jsonObject = new JSONObject(content);
					int status = jsonObject.getInt("status");
					if (status == 1) {
						Toast.makeText(ZfDeputeFindRoomActivity.this, "委托成功", Toast.LENGTH_SHORT).show();
						finish();
					} else {
						Toast.makeText(ZfDeputeFindRoomActivity.this, "委托失败", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

	// 监测 EditText 内容，判断button是否可点击
	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			name = tvUsername.getText().toString().trim();
			mobile = tvUserphone.getText().toString().trim();
			address = tvAddress.getText().toString().trim();
			if (name.length() != 0 && mobile.length() != 0 && address.length() != 0) {
				subBtn.setBackground(getResources().getDrawable(R.drawable.btn_hl));
				subBtn.setClickable(true);

			} else {
				subBtn.setBackground(getResources().getDrawable(R.drawable.btn_d));
				subBtn.setClickable(false);
			}

		}

		@Override
		public void afterTextChanged(Editable s) {
			name = tvUsername.getText().toString().trim();
			mobile = tvUserphone.getText().toString().trim();
			address = tvAddress.getText().toString().trim();
			if (name.length() != 0 && mobile.length() != 0 && address.length() != 0) {
				subBtn.setBackground(getResources().getDrawable(R.drawable.btn_hl));
				subBtn.setClickable(true);
			} else {
				subBtn.setBackground(getResources().getDrawable(R.drawable.btn_d));
				subBtn.setClickable(false);
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

}
