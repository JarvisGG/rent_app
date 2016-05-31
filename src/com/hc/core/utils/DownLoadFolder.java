package com.hc.core.utils;

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

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.hc.xiaobairent.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DownLoadFolder {

	private AbRequestParams params;
	private AbHttpUtil httpUtil;
	private SharedpfTools sharedpfTools;
	private static final int TIME_OUT = 100 * 1000; // 超时时间
	private static final String savePath = "/sdcard/updatedemo/";
	private String apkPath = null;
	private String saveFileName = savePath;
	private int pro;
	private Dialog dialog;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;
	private boolean interceptFlag = false;
	private ProgressBar mProgress;
	private Context context;
	private TextView tv;

	public DownLoadFolder(Context context) {
		this.context = context;
		httpUtil = AbHttpUtil.getInstance(context);
		sharedpfTools = SharedpfTools.getInstance(context);

	}

	public void initFolder(String url) {
		params = new AbRequestParams();
		httpUtil.post(url + sharedpfTools.getAccessToken(), params, new AbStringHttpResponseListener() {

			@Override
			public void onStart() {
			}

			@Override
			public void onFinish() {
			}

			@Override
			public void onFailure(int arg0, String arg1, Throwable arg2) {
			}

			@Override
			public void onSuccess(int arg0, String arg1) {
				Log.e("task", arg1);
				try {
					JSONObject object = new JSONObject(arg1);
					apkPath = object.getString("path");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				// Items model = gson.fromJson(arg1, Items.class);
				// apkPath = model.getUpload().get(0).getPath();
				Log.e("boolean", apkPath + "," + (apkPath != null) + "," + (!apkPath.equals("")));
				if (apkPath != null && !apkPath.equals("")) {
					showUpdataDialog();
				}

			}
		});
	}

	protected void showUpdataDialog() {
		Builder builder = new Builder(context);
		builder.setTitle("文件下载");
		final LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.aim_progress_app_update, null);
		mProgress = (ProgressBar) v.findViewById(R.id.progress);
		tv = (TextView) v.findViewById(R.id.tv);
		builder.setView(v);
		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});
		dialog = builder.create();
		dialog.show();
		downLoad();
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(pro);
				tv.setText("已下载" + pro + "%");
				break;
			case DOWN_OVER:
				dialog.dismiss();
				break;
			default:
				break;
			}
		};
	};

	public void downLoad() {

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
					System.out.println("code-----" + code);
					InputStream is = conn.getInputStream();
					System.out.println("huoquliu----" + is);
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
							message.what = 2;
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
}
