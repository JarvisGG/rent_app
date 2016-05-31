package com.hc.core.utils;

import java.util.Calendar;

import android.widget.TextView;

public class GetTime {

	public GetTime(TextView weeks_tv, TextView date_tv, TextView week_tv, TextView zone_tv) {
		Calendar c = Calendar.getInstance();
		weeks_tv.setText("第" + c.get(Calendar.WEEK_OF_YEAR) + "周");
		date_tv.setText(c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH));
		String week = "";
		switch (c.get(Calendar.DAY_OF_WEEK)) {
		case 1:
			week = "星期天";
			break;
		case 2:
			week = "星期一";
			break;
		case 3:
			week = "星期二";
			break;
		case 4:
			week = "星期三";
			break;
		case 5:
			week = "星期四";
			break;
		case 6:
			week = "星期五";
			break;
		case 7:
			week = "星期六";
			break;
		default:
			break;
		}
		week_tv.setText(week);
		String am_pm = "";
		switch (c.get(Calendar.AM_PM)) {
		case 0:
			am_pm = "AM";
			break;
		case 1:
			am_pm = "PM";
			break;

		default:
			break;
		}
		zone_tv.setText(am_pm);
	}

}
