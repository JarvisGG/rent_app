package com.hc.core.upload;

import org.kymjs.kjframe.ui.BindView;

import com.hc.core.base.BaseActivity;
import com.hc.xiaobairent.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class upload extends BaseActivity {
	@BindView(id = R.id.wv_content)
	private WebView wvContent;
	/** 顶部标题 */
	@BindView(id = R.id.tv_topbar)
	private TextView tvTopbar;
	@BindView(id = R.id.iv_back, click = true)
	private ImageView ivBack;
	private ValueCallback<Uri> mUploadMessage;

	@Override
	public void setRootView() {
		setContentView(R.layout.hc_activity_upload);
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void initData() {
		super.initData();
		wvContent.getSettings().setJavaScriptEnabled(true);
		wvContent.loadUrl("http://121.199.41.31:8099/article/ceshi_upload2");
		wvContent.setWebChromeClient(new WebChromeClient() {

			public void openFileChooser(ValueCallback<Uri> uploadMsg,
					String acceptType, String capture) {
				if (mUploadMessage != null) {
					mUploadMessage.onReceiveValue(null);
				}
				mUploadMessage = uploadMsg;
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/*");
				startActivityForResult(
						Intent.createChooser(intent, "完成操作需要使用"), 10);

			}
		});
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		Uri uri = arg2.getData();
		mUploadMessage.onReceiveValue(uri);
		mUploadMessage = null;
	}

	@Override
	public void initWidget() {
		super.initWidget();
		tvTopbar.setText("上传");
		tvTopbar.setVisibility(View.VISIBLE);
		ivBack.setVisibility(View.VISIBLE);
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.iv_back:
			onBackPressed();
			break;
		}
	}
}
