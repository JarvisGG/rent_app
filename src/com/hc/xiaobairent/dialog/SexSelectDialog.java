package com.hc.xiaobairent.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.hc.xiaobairent.R;

public class SexSelectDialog extends Dialog{
	
	public SexSelectDialog(Context context) {
		super(context, R.style.Theme_dialog);
		setContentView(R.layout.aim_layout_selecticon_sex);
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.BOTTOM;
		window.setAttributes(params);
	}

}
