package com.hc.core.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * @function 检测密码是否合法
 * @condition1 不小于6位，不多于12位
 * @condition2 两次输入一致
 * @condition3 字母、数字、标点中两种或三种
 * @author Fun
 *
 */
public class CheckPassword {
	/**
	 * 
	 * @param context
	 * @param password
	 * @param confirmpassword
	 * @return
	 */
	public boolean checkPassword(Context context, String password, String confirmpassword) {
		if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmpassword)) {
			Toast.makeText(context, "请输入密码", Toast.LENGTH_SHORT);
			return false;
		} else if (!(password).equals(confirmpassword)) {
			Toast.makeText(context, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
			return false;
		} else if (password.length() < 6) {
			Toast.makeText(context, "请输入大于等于6位的密码", Toast.LENGTH_SHORT).show();
			return false;
		} else if (password.length() > 12) {
			Toast.makeText(context, "请输入小于等于12位的密码", Toast.LENGTH_SHORT).show();
			return false;
			// } else if (!checkPass(password)) {
			// Toast.makeText(context, "密码必须是字母、数字、标点中两种或三种",
			// Toast.LENGTH_SHORT).show();
			// return false;
		}
		return true;
	}

	/**
	 * @function 检查密码是否由字母、数字、标点中两种或三种构成，
	 * @param password
	 * @return true 是 ; false 否
	 */
	private boolean checkPass(String password) {
		String LETTER = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
		String DIGITS = "1234567890";
		String PONTUATION = "!@#$%^&*()_+{}:<>?|-=[];,./";
		boolean LET = false;
		boolean DIG = false;
		boolean PON = false;
		char[] chars = password.toCharArray();
		for (char c : chars) {
			for (char d : DIGITS.toCharArray()) {
				if (c == d) {
					DIG = true;
				}
			}
			for (char p : PONTUATION.toCharArray()) {
				if (c == p) {
					PON = true;
				}
			}
			for (char l : LETTER.toCharArray()) {
				if (c == l) {
					LET = true;
				}
			}
			if ((DIG && PON) || (DIG && LET) || (LET && PON)) {
				return true;
			}
		}
		return false;
	}
}

