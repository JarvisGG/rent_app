package com.hc.core.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * 
 * @ClassName: DownloadUtil
 * @Description: 根据链接下载文件到Download文件夹
 * @author frank.fun@qq.com
 * @date 2016年4月14日 下午1:08:16
 *
 */
public class DownloadUtil {
	private static DownloadUtil downloadUtil;
	private static ProgressBar pBar;
	private static Context mContext;
	private static String newFilename;
	private static Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			int prog = msg.arg1;
			if (prog != 100) {
				pBar.setProgress(msg.arg1);
			} else {
				pBar.setVisibility(View.GONE);
				Toast.makeText(mContext, "文件" + newFilename + "下载完毕", Toast.LENGTH_SHORT).show();
			}
		};
	};

	public DownloadUtil(Context context) {
		mContext = context;
	}

	public static DownloadUtil getInstance(Context context) {
		downloadUtil = new DownloadUtil(context);
		return downloadUtil;
	}

	/**
	 * 
	 * @Title: download
	 * @Description: 开始下载
	 * @param fileUrl
	 * @param progressBar
	 */
	public void download(String fileUrl, ProgressBar progressBar) {
		pBar = progressBar;
		download(fileUrl);
	}

	/**
	 * 
	 * @Title: download
	 * @Description: 开始下载
	 * @param fileUrl
	 * @param progressBar
	 */
	public void download(final String fileUrl) {
		new Thread(new Runnable() {
			public void run() {
				// 要下载的文件路径
				String dirName = Environment.getExternalStorageDirectory().getPath() + "/Download/";
				File df = new File(dirName);
				/** 如果目录不存在，创建新目录 */
				if (!df.exists()) {
					df.mkdir();
				}
				/** 准备拼接新的文件名（保存在存储卡后的文件名） */
				newFilename = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
				String newFilePath = dirName + newFilename;
				File file = new File(newFilePath);
				/** 如果目标文件已经存在，则删除。产生覆盖旧文件的效果 */
				if (file.exists()) {
					file.delete();
				}
				try {
					/** 构造URL */
					URL url = new URL(fileUrl);
					/** 打开连接 */
					URLConnection con = url.openConnection();
					/** 获得文件的长度 */
					int contentLength = con.getContentLength();
					Log.v("download", "文件大小 :" + contentLength);
					/** 输入流 */
					InputStream is = con.getInputStream();
					/** 1K的数据缓冲 */
					byte[] bs = new byte[1024];
					/** 读取到的数据长度 */
					int len;
					/** 输出的文件流 */
					OutputStream os = new FileOutputStream(newFilePath);
					Log.v("download", "开始读取");
					int count = 0;
					int percentage = 0;
					while ((len = is.read(bs)) > 0) {
						Log.v("download", "开始读取" + len);
						os.write(bs, 0, len);
						if (pBar != null) {
							count += len;
							percentage = count * 100 / contentLength;
							Message message = new Message();
							message.arg1 = percentage;
							Log.v("percentage", percentage + "%");
							handler.sendMessage(message);
						}
					}
					Log.v("download", "完毕，关闭所有链接");
					os.close();
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

}
