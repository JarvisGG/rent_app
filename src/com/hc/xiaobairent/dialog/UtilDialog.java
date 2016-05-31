package com.hc.xiaobairent.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.hc.xiaobairent.R;


public class UtilDialog {
	
	public static AlertDialog showConfirmDialog(Context context, String okText, String title, String message,
			final DialogInterface.OnClickListener okClicked, final DialogInterface.OnClickListener cancelClicked) {
		
		final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		View vDialog = LayoutInflater.from(context).inflate(R.layout.aim_dialog_confirm_pop_up, null);
		alertDialog.setView(vDialog, 0, 0, 0, 0);
		TextView tvTitle = (TextView) vDialog.findViewById(R.id.tv_dialog_title);
		if (title == null || title.equals("")) {
			tvTitle.setText("确认");
		} else {
			tvTitle.setText(title);
		}

		TextView tvMessage = (TextView) vDialog.findViewById(R.id.tv_dialog_message);
		if (message != null) {
			tvMessage.setText(message);
		}

		Button okBtn = (Button) vDialog.findViewById(R.id.btn_dialog_confirm_submit);
		okBtn.setText(okText);
		okBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (okClicked == null) {

				} else {
					okClicked.onClick(alertDialog, DialogInterface.BUTTON_POSITIVE);
				}
			}
		});
		vDialog.findViewById(R.id.btn_dialog_confirm_cancel).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (cancelClicked == null) {
					alertDialog.dismiss();
				} else {
					cancelClicked.onClick(alertDialog, DialogInterface.BUTTON_NEGATIVE);
				}
			}
		});

		alertDialog.setCancelable(true);
		// Showing Alert Message
		alertDialog.show();
		alertDialog.getWindow().setLayout(android.view.WindowManager.LayoutParams.FILL_PARENT,
				android.view.WindowManager.LayoutParams.WRAP_CONTENT);
		return alertDialog;
	}
}
