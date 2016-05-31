package com.hc.core.update;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.hc.xiaobairent.R;
import com.lidroid.xutils.http.RequestParams;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 应用更新工具类
 * 
 * @ClassName: UtilUpdateApp
 * @Description:
 * @author 景庆超
 * @date 2014-11-19 下午1:41:41
 * 
 */
public class UtilUpdateApp implements HttpCallback {
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;
	@SuppressLint("SdCardPath")
	private static final String savePath = "/sdcard/Download/";
	private static final String saveFileName = savePath + "FeierLife.apk";
	private static final int TIME_OUT = 100 * 1000; // 超时时间

	private Context context;
	private ProgressBar mProgress;
	private TextView tv;
	private Dialog updatedialog;
	private int status;
	private String apkPath = null;
	private String updateUrlStr;
	private int pro;
	public static final int NOALERT = 0;
	public static final int ALERTDIALOG = 1;
	public static final int TOAST = 2;
	/** 是否显示提示框 */
	private int alertStyle = 0;
	private boolean interceptFlag = false;

	public UtilUpdateApp(Context context, String updateUrlStr) {
		this.context = context;
		this.updateUrlStr = updateUrlStr;
	}

	public UtilUpdateApp(Context context, String updateUrlStr, int alertStyle) {
		this.context = context;
		this.updateUrlStr = updateUrlStr;
		this.alertStyle = alertStyle;
	}

	/**
	 * 向外部提供的方法
	 */
	public void checkedUpdate() {
		UtilHttp.sendPost(context, updateUrlStr, this);

	}

	/**
	 * 版本升级对话框
	 * 
	 * @Title: showUpdateDialog
	 * @param apkPath2
	 *            void
	 * @author
	 * @since 2014-11-19 V 1.0
	 */
	private void showUpdateDialog() {

		UtilDailog.showConfirmDialog(context, "确定", "版本升级", "系统检测到新版本，您需要更新吗?", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				showDownloadDialog();
				dialog.dismiss();
			}

		}, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}

		});
	}

	@Override
	public RequestParams onParams() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("version", UtilPhone.getAppVersionName(context));
		Log.v("viersion", UtilPhone.getAppVersionName(context));
		return params;
	}

	@Override
	public void onSuccess(String result, int flag) {
		JSONObject object;
		try {
			object = new JSONObject(result);
			status = object.optInt("status");
			apkPath = object.optString("url");
			Log.v("downloadpath", apkPath);
			if (status == 1) {
				showUpdateDialog();
			} else {
				switch (alertStyle) {
				case ALERTDIALOG:
					UtilDailog.showTipMessage(context, "当前已是最新版本");
					break;
				case TOAST:
					Toast.makeText(context, "当前已是最新版本", Toast.LENGTH_SHORT).show();
					break;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Log.v("downloadpath", "获取下载地址失败");
		}
	}

	/**
	 * 软件更新对话框
	 * 
	 * @Title: showDownloadDialog void
	 * @author
	 * @since 2014-11-19 V 1.0
	 */
	private void showDownloadDialog() {
		updatedialog = new Dialog(context);
		View v = LayoutInflater.from(context).inflate(R.layout.aim_progress_app_update, null);
		updatedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		updatedialog.setContentView(v);
		TextView title = (TextView) v.findViewById(R.id.title);
		mProgress = (ProgressBar) v.findViewById(R.id.progress);
		title.setText("软件版本更新");
		tv = (TextView) v.findViewById(R.id.tv);
		tv.setText("取消");
		tv.setOnClickListener(new DialogOnClickListener());
		updatedialog.show();
		downloadAPK();
	}

	public class DialogOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			updatedialog.dismiss();
			interceptFlag = true;
		}

	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(pro);
				tv.setText("取消（已下载" + pro + "%）");
				break;
			case DOWN_OVER:
				updatedialog.dismiss();
				// String path = (String) msg.obj;
				installAPK(new File(saveFileName));
				break;
			}
		};
	};

	/**
	 * 下载apk
	 * 
	 * @Title: downloadAPK void
	 * @author
	 * @since 2014-11-19 V 1.0
	 */
	private void downloadAPK() {
		new Thread() {
			public void run() {
				try {
					URL url = new URL(apkPath);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setReadTimeout(TIME_OUT);
					conn.setConnectTimeout(TIME_OUT);
					// conn.setDoInput(true); // 允许输入流
					// conn.setDoOutput(true); // 允许输出流
					// conn.setUseCaches(false); // 不允许使用缓存
					conn.setRequestMethod("GET"); // 请求方式
					conn.connect();
					int length = conn.getContentLength();
					int code = conn.getResponseCode();
					InputStream is = conn.getInputStream();
					File file = new File(savePath);
					if (!file.exists()) {
						file.mkdir();
					}
					String apkFile = saveFileName;
					File ApkFile = new File(apkFile);
					FileOutputStream fos = new FileOutputStream(ApkFile);
					int count = 0;
					byte buf[] = new byte[1024];
					do {
						int numread = is.read(buf);
						count += numread;
						pro = (int) (((float) count / length) * 100);
						System.out.println("progress更新进度" + pro);
						System.out.println("numread" + numread);
						// 更新进度
						mHandler.sendEmptyMessage(DOWN_UPDATE);
						if (numread < 0) {
							// 下载完成通知安装
							Message message = Message.obtain(mHandler);
							message.what = DOWN_OVER;
							message.obj = apkFile;
							message.sendToTarget();
							break;
						}
						fos.write(buf, 0, numread);
					} while (!interceptFlag);// 点击取消就停止下载.
					fos.close();
					is.close();

				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

	/**
	 * 安装apk
	 * 
	 * @Title: installAPK
	 * @param file
	 *            void
	 * @author
	 * @since 2014-11-19 V 1.0
	 */

	// mSavePath 是指下载保存文件的路径
	private void installAPK(File file) {
		try {
			Runtime.getRuntime().exec("chmod 701 " + saveFileName);
			Runtime.getRuntime().exec("chmod 777 " + file.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (!file.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent intentInstall = new Intent(Intent.ACTION_VIEW);
		intentInstall.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		context.startActivity(intentInstall);
		android.os.Process.killProcess(android.os.Process.myPid());
	}

}
