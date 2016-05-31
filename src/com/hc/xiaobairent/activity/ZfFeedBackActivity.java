package com.hc.xiaobairent.activity;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.ui.BindView;

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
import com.hc.core.utils.SharedpfTools;
import com.hc.core.utils.Sign;
import com.hc.core.utils.UrlConnector;
import com.hc.xiaobairent.R;

public class ZfFeedBackActivity extends BaseActivity {

	// 初始化标题
	@BindView(id = R.id.menu_back, click = true)
	private ImageView menuBack;
	@BindView(id = R.id.menu_title)
	private TextView menuTitle;
	@BindView(id = R.id.menu_title_right, click = true)
	private TextView menuRight;

	@BindView(id = R.id.opinion_content)
	private EditText tvOptinion;
	@BindView(id = R.id.next_btu, click = true)
	private Button submitBtn;

	private AbHttpUtil http;
	private SharedpfTools sharedpfTools;
	private Sign sign;
	private AbRequestParams params;

	@Override
	public void setRootView() {
		setAbContentView(R.layout.zf_activity_freedback);
	}

	@Override
	public void initData() {
		super.initData();
		initTab();
		http = AbHttpUtil.getInstance(ZfFeedBackActivity.this);
		sign = new Sign(ZfFeedBackActivity.this);
		sign.init();
		sharedpfTools = SharedpfTools.getInstance(ZfFeedBackActivity.this);
		params = new AbRequestParams();

	}

	private void applyData() {
		params.put("feedback", tvOptinion.getText().toString().trim());
		String url = UrlConnector.FEEDBACK + sharedpfTools.getAccessToken();
		http.post(url, params, new AbStringHttpResponseListener() {

			@Override
			public void onStart() {

			}

			@Override
			public void onFinish() {

			}

			@Override
			public void onFailure(int statusCode, String content, Throwable error) {
				Toast.makeText(ZfFeedBackActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				try {
					JSONObject jsonObject = new JSONObject(content);
					Toast.makeText(ZfFeedBackActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
					if (jsonObject.getInt("status") == 1) {
						ZfFeedBackActivity.this.finish();
					}
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

		case R.id.next_btu:
			applyData();
			break;

		default:
			break;
		}
	}

	private void initTab() {
		menuTitle.setText("帮助与反馈");
	}

}
