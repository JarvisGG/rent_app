package com.hc.xiaobairent.utils;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class ShareContent {

	private Context context;
	private String title, content, sharepUrl;
	private UMImage picShare;
	private ShareAction shareAction;

	public void init(Context context, String title, String content, UMImage picShare, String sharepUrl) {
		this.context = context;
		this.title = title;
		this.content = content;
		this.picShare = picShare;
		this.sharepUrl = sharepUrl;

		configPlatforms();

	}

	// 配置参数
	private void configPlatforms() {
		// 添加设置appId, appSecret
		addQQZonePlatform();
		addWXPlatform();

		setDisplay();
	}

	// 设置分享面板
	private void setDisplay() {
		shareAction = new ShareAction((Activity) context);
		shareAction.setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener).open();
	}

	// 设置分享内容
	private void setContent() {
		shareAction.withTitle(title);
		shareAction.withText(content);
		shareAction.withMedia(picShare);
		shareAction.withTargetUrl(sharepUrl);
	}

	// 绑定回调
	private void setCallBack() {
		shareAction.setCallback(umShareListener);
	}

	// 打开分享面板
	private void openShareLayout() {
		shareAction.open();
	}

	private void addQQZonePlatform() {

		String appId = "100424468";
		String appSecret = "c7394704798a158208a74ab60104f0ba";
		PlatformConfig.setQQZone(appId, appSecret);

	}

	private void addWXPlatform() {
		String appId = "wx979529870885b03e";
		String appSecret = "e1d4fa9d9c4133d311d0c5d2ad67363d";
		PlatformConfig.setWeixin(appId, appSecret);
	}

	private UMShareListener umShareListener = new UMShareListener() {
		@Override
		public void onResult(SHARE_MEDIA platform) {
			Log.d("plat", "platform" + platform);
			Toast.makeText(context, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			Toast.makeText(context, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			Toast.makeText(context, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
		}
	};
}
