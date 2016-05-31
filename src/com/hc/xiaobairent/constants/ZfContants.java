package com.hc.xiaobairent.constants;

import android.os.Environment;

public class ZfContants {
	/** 时间换算用数据 */
	public static final int DAY_TIME = 86400000, HOUR_TIME = 3600000, MIN_TIME = 60000, SEC_TIME = 1000;
	/** 图片保存路径 */
	public static String FILEPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "android" + "/"
			+ "data" + "/" + "com.aim.lexiangs.activity/cache/";
	/** 注册页 */
	public static final int PERSON_TO_REGISTER = 2;
	/** 修改个性签名 */
	public static final int EDIT_SIGNATURE = 3;
	/** 修改姓名 */
	public static final int EDIT_NAME = 4;
	/** 修改手机 */
	public static final int EDIT_MOBILE = 5;
	/** 修改微信*/
	public static final int EDIT_WEIXIN = 6;
	/** 修改QQ号*/
	public static final int EDIT_QQ = 7;
	/** 修改行业*/
	public static final int EDIT_INDUSTRY = 8;
	/** 修改邮箱*/
	public static final int EDIT_EMAIL = 9;
	/** 友盟 */
	public static final String DESCRIPTOR = "com.umeng.share";
}
