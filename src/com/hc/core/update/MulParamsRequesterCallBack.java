package com.hc.core.update;

import com.hc.xiaobairent.R;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * @ClassName RequesterCallBack
 * @Description Http回调函数.
 * @author 李君波
 * @date 2014年11月20日 下午3:12:35
 * @version 1.0
 */
public class MulParamsRequesterCallBack extends RequestCallBack<String> {

	private Dialog mProgressDialog;

	private int flag = 0;

	private MulParamsHttpCallback callBack;

	public MulParamsRequesterCallBack(MulParamsHttpCallback callBack) {
		super();
		this.callBack = callBack;
	}

	public MulParamsRequesterCallBack(MulParamsHttpCallback callBack, int flag) {
		super();
		this.flag = flag;
		this.callBack = callBack;
	}

	@Override
	public void onStart() {
		super.onStart();

		if (callBack instanceof Activity) {
			showLoadingDialog((Context) callBack);
		} else if (callBack instanceof Fragment) {
			showLoadingDialog(((Context) ((Fragment) callBack).getActivity()));
		} else {
		}
	}

	@Override
	public void onLoading(long total, long current, boolean isUploading) {
		super.onLoading(total, current, isUploading);
	}

	@Override
	public void onSuccess(ResponseInfo<String> responseInfo) {
		dismissLoadingDialog();
		Log.d("result:", responseInfo.result);
		callBack.onSuccess(responseInfo.result.toString(), flag);
	}

	@Override
	public void onFailure(HttpException error, String msg) {

		Log.e("error:", msg);

		dismissLoadingDialog();

		if (callBack instanceof Activity) {
			Toast.makeText((Context) callBack, error.toString(), Toast.LENGTH_SHORT).show();
		} else if (callBack instanceof Fragment) {
			Toast.makeText(((Fragment) callBack).getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	/*
	 * @Override public void onStopped() { super.onStopped(); }
	 */

	/**
	 * @Title: 打开加载数据的ProgressBar
	 * @param context
	 * @author 李君波
	 * @since 2014年11月21日 V 1.0
	 */
	private void showLoadingDialog(Context context) {
		mProgressDialog = new Dialog(context, R.style.CustomProgressDialog);
		ProgressBar progressBar = new ProgressBar(context);
		progressBar.setIndeterminate(true);
		progressBar.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.aim_custom_progress_draw));

		mProgressDialog.setContentView(progressBar);

		LayoutParams params = progressBar.getLayoutParams();
		params.width = params.height = UtilWindow.dip2px(context, 35);
		progressBar.setLayoutParams(params);

		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
	}

	/**
	 * @Title: 关闭加载数据的ProgressBar
	 * @author 李君波
	 * @since 2014年11月21日 V 1.0
	 */
	private void dismissLoadingDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing())
			mProgressDialog.dismiss();
	}

}
