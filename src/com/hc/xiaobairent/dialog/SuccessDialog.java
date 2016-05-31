package com.hc.xiaobairent.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.hc.xiaobairent.R;

public class SuccessDialog extends Dialog{
	
	public SuccessDialog(Context context) {
		super(context, R.style.Theme_dialog);
		setContentView(R.layout.zf_success_dialog);
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.CENTER;
		window.setAttributes(params);		
	}

}
