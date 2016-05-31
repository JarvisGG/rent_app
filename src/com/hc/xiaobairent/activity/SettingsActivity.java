package com.hc.xiaobairent.activity;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.ui.BindView;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbStringHttpResponseListener;
import com.hc.core.base.BaseActivity;
import com.hc.core.utils.SharedpfTools;
import com.hc.core.utils.Sign;
import com.hc.core.utils.UrlConnector;
import com.hc.xiaobairent.R;
import com.hc.xiaobairent.dialog.UtilDialog;
import com.hc.xiaobairent.utils.ShareContent;
import com.umeng.socialize.media.UMImage;

public class SettingsActivity extends BaseActivity {
	
	// 初始化标题
	@BindView(id = R.id.menu_back, click = true)
	private ImageView menuBack;
	@BindView(id = R.id.menu_title)
	private TextView menuTitle;
	
	@BindView(id = R.id.setting_function, click = true)
	private LinearLayout settingFun;
	@BindView(id = R.id.setting_share, click = true)
	private LinearLayout settingShare;
	@BindView(id = R.id.setting_help, click = true)
	private LinearLayout settingHelp;
	@BindView(id = R.id.setting_contact, click = true)
	private LinearLayout settingContact;
	@BindView(id = R.id.setting_updata, click = true)
	private LinearLayout settingUpdata;
	@BindView(id = R.id.next_btu, click = true)
	private Button submitBtn;
	
	private SharedpfTools sp;
	
	// 分享
	private ShareContent shareContent = new ShareContent();
	private String shareUrl = "";
	private String title = "";
	private UMImage shareImage;
	
	// 获取联系人手机
	private String phone = "";
	private Sign sign;
	private AbHttpUtil httpUtil;
	
	@Override
	public void setRootView() {
		setAbContentView(R.layout.zf_activity_setting);
	}
	
	@Override
	public void initData() {
		super.initData();
		sp = SharedpfTools.getInstance(getApplicationContext());
		sign = new Sign(SettingsActivity.this);
		httpUtil = AbHttpUtil.getInstance(SettingsActivity.this);
		sign.init();
		
		initTab();
	}
	
	private void initTab() {
		menuTitle.setText("设置");
	}
	
	
	private void applyPhone() {
		String url = UrlConnector.CONTACT_US + sp.getAccessToken()+UrlConnector.SIGN + sign.getSign();
		httpUtil.post(url, null, new AbStringHttpResponseListener() {
			
			@Override
			public void onStart() {
				
			}
			
			@Override
			public void onFinish() {
				
			}
			
			@Override
			public void onFailure(int statusCode, String content, Throwable error) {
				Toast.makeText(SettingsActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				try {
					JSONObject jsonObject = new JSONObject(content);
					phone = jsonObject.getString("phone");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void showContact() {
		applyPhone();
		String message = "一键拨号电话：" + phone;
		UtilDialog.showConfirmDialog(SettingsActivity.this, "拨号", "拨号", message, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Uri dialUri = Uri.parse("tel:" + phone);
				Intent intent = new Intent(Intent.ACTION_CALL,dialUri);
				startActivity(intent);
			}
		}, null);
	}
	
	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.menu_back:
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;
			
		case R.id.setting_share:
			shareImage = new UMImage(SettingsActivity.this, UrlConnector.SHARE_ICON);
			shareContent.init(SettingsActivity.this, "分享给好友", title, shareImage, shareUrl);
			break;
			
		case R.id.setting_function:
			startActivity(new Intent(SettingsActivity.this, ZfAboutUsActivity.class));
			overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			break;
			
		case R.id.setting_help:
			startActivity(new Intent(SettingsActivity.this, ZfFeedBackActivity.class));
			overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			break;
			
		case R.id.setting_contact:
			showContact();
			break;
			
		case R.id.next_btu:
			sp.setLogStatus(false);
			sp.clear();
			finish();
			startActivity(new Intent(SettingsActivity.this, ZfLoginActivity.class));
			overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			break;

		default:
			break;
		}
	}

}
