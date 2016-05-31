package com.hc.xiaobairent.activity;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.ui.BindView;

import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbStringHttpResponseListener;
import com.hc.core.base.BaseActivity;
import com.hc.core.utils.SharedpfTools;
import com.hc.core.utils.Sign;
import com.hc.core.utils.UrlConnector;
import com.hc.xiaobairent.R;

public class ZfAboutUsActivity extends BaseActivity{

	// 初始化标题
	@BindView(id = R.id.menu_back, click = true)
	private ImageView menuBack;
	@BindView(id = R.id.menu_title)
	private TextView menuTitle;
	@BindView(id = R.id.menu_title_right, click = true)
	private TextView menuRight;
	
	@BindView(id = R.id.tv_about_us)
	private TextView tvAboutus;
	
	private SharedpfTools sharedpfTools;
	private AbHttpUtil http;
	private Sign sign;
	
	@Override
	public void setRootView() {
		setAbContentView(R.layout.zf_activity_aboutus);
	}
	
	@Override
	public void initData() {
		super.initData();
		http = AbHttpUtil.getInstance(ZfAboutUsActivity.this);
		sharedpfTools = SharedpfTools.getInstance(ZfAboutUsActivity.this);
		sign = new Sign(ZfAboutUsActivity.this);
		sign.init();
		applyData();
	}
	
	private void applyData() {
		String url = UrlConnector.ABOUT_US + sharedpfTools.getAccessToken() + UrlConnector.SIGN + sign.getSign();
		http.get(url, null, new AbStringHttpResponseListener() {
			
			@Override
			public void onStart() {
				
			}
			
			@Override
			public void onFinish() {
				
			}
			
			@Override
			public void onFailure(int statusCode, String content, Throwable error) {
				Toast.makeText(ZfAboutUsActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				try {
					JSONObject jsonObject = new JSONObject(content);
					menuTitle.setText(jsonObject.getString("title"));
					tvAboutus.setText(Html.fromHtml(jsonObject.getString("summary")));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.menu_back:
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;

		default:
			break;
		}
	}

}
