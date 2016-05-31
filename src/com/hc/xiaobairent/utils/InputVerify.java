package com.hc.xiaobairent.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputVerify {
	public static boolean phoneVerify(String phone) {
		Pattern pattern = null;
		Matcher matcher = null;
		pattern = Pattern.compile("^[1][3,4,5,6,7,8,9][0-9]{9}$");
		matcher = pattern.matcher(phone);
		return matcher.matches();
	}
}
