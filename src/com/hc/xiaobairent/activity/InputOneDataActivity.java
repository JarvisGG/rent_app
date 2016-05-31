package com.hc.xiaobairent.activity;

import com.hc.core.utils.RentConstants;
import com.hc.xiaobairent.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @ClassName: InputOneDataActivity
 * @Description: 输入单条数据
 * @author frank.fun@qq.com
 * @date 2016年5月20日 上午11:23:43
 *
 */
public class InputOneDataActivity extends Activity {
	protected static final String TAG = "InputOneDataActivity";
	private ImageView back;
	private TextView title;
	private EditText content_et;
	private TextView confirm_tv;
	private String data = "";
	private Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.input_one_data_activity);
		back = (ImageView) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		content_et = (EditText) findViewById(R.id.content_et);
		confirm_tv = (TextView) findViewById(R.id.confirm_tv);
		data = getIntent().getStringExtra(RentConstants.CONTENT_PARAM);
		title.setText(data);
		switch (data) {
		case "姓名":
			content_et.setHint("请输入您的真实姓名");
			break;
		case "身份证号码":
			content_et.setHint("请输入您的身份证号码");
			content_et.setKeyListener(DigitsKeyListener.getInstance("0123456789Xx"));
			break;
		default:
			break;
		}
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		confirm_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				if (data.equals("身份证号码")) {
					if (content_et.getText().toString().trim().length() != 18) {
						Toast.makeText(context, "身份证号码长度不对", Toast.LENGTH_SHORT).show();
					} else {
						intent.putExtra(RentConstants.CONTENT_PARAM, content_et.getText().toString().trim());
						setResult(RESULT_OK, intent);
						finish();
					}
				} else {
					intent.putExtra(RentConstants.CONTENT_PARAM, content_et.getText().toString().trim());
					setResult(RESULT_OK, intent);
					finish();
				}
			}
		});
		if (TextUtils.isEmpty(content_et.getText().toString().trim())) {
			confirm_tv.setEnabled(false);
		}
		content_et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(s)) {
					confirm_tv.setEnabled(false);
				} else {
					confirm_tv.setEnabled(true);
				}
			}
		});
	}

}
