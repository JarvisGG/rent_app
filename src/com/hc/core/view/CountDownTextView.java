package com.hc.core.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.hc.xiaobairent.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CountDownTextView extends LinearLayout implements Runnable {
	public final static int DAY_TIME = 86400000, HOUR_TIME = 3600000,
			MIN_TIME = 60000, SEC_TIME = 1000;
	private TextView day;
	private TextView hour;
	private TextView min;
	private TextView second;
	private TextView single;
	private LinearLayout multiple;
	private int[] t;

	private int d = 0;
	private int h = 0;
	private int m = 0;
	private int s = 0;
	private String da;
	private String ho;
	private String mi;
	private String se;
	private boolean run;

	public CountDownTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		InitView(context);
	}

	public CountDownTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		InitView(context);
	}

	public CountDownTextView(Context context) {
		super(context);
		InitView(context);
	}

	private void InitView(Context context) {
		View.inflate(context, R.layout.hc_time_down, this);
		single = (TextView) findViewById(R.id.single);
		multiple = (LinearLayout) findViewById(R.id.multiple);
		multiple.setVisibility(View.GONE);
		day = (TextView) this.findViewById(R.id.day);
		hour = (TextView) this.findViewById(R.id.hour);
		min = (TextView) this.findViewById(R.id.min);
		second = (TextView) this.findViewById(R.id.sec);
	}

	public void setTimes(int[] t) {
		this.t = t;
		d = t[0];
		h = t[1];
		m = t[2];
		s = t[3];
	}

	public void setTimesByEndTime(String string) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date endTime;
		long currentTime;
		long timeRemaining;
		currentTime = System.currentTimeMillis();
		try {
			endTime = sdf.parse(string);
			timeRemaining = endTime.getTime() - currentTime;
			d = (int) (timeRemaining / DAY_TIME);
			h = ((int) timeRemaining / HOUR_TIME) - d * 24;
			m = ((int) (timeRemaining / MIN_TIME)) - h * 60 - d * 24 * 60;
			s = ((int) (timeRemaining / SEC_TIME)) - m * 60 - h * 60 * 60 - d
					* 24 * 60 * 60;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean ComputeTime() {
		s--;
		if (d == 0 && h == 0 && m == 0 && s == 0) {
			return false;
		} else {
			if (s < 0) {
				m--;
				s = 59;
				if (m < 0) {
					h--;
					m = 59;
					if (h < 0) {
						d--;
						h = 23;
					}
				}
			}
		}
		return true;

	}

	@Override
	public void run() {
		run = true;
		if (ComputeTime()) {
			da = d + "";
			ho = h + "";
			mi = m + "";
			se = s + "";
			if (h < 10) {
				ho = "0" + h;
			}
			if (m < 10) {
				mi = "0" + m;
			}
			if (s < 10) {
				se = "0" + s;
			}
			single.setText("距离投票结束还有：" + da + "天" + ho + "小时" + mi + "分" + se
					+ "秒");
			postDelayed(this, 1000);
		} else {
			single.setText("投票已结束");
		}
	}

	public boolean isRun() {
		return run;
	}

}
