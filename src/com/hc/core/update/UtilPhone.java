package com.hc.core.update;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;

/**
 * 手机工具类.
 *  
 * @author William Lee
 * @version 1.0 2014-11-17
 */
public class UtilPhone {

    /**
     * 获得本机唯一码
     * 
     * @Title: getDeviceId
     * @param context
     * @return deviceid String
     * @author 景庆超
     * @since 2014-5-10 V 1.0
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();

    }

    /**
     * 运营商
     * 
     * @Title: getProvidersName
     * @param tm
     * @return String
     * @author
     * @since 2014-11-19 V 1.0
     */
    public static String getProvidersName(TelephonyManager tm) {
        String ProvidersName = null;
        // 手机卡的唯一编号[用户ID]
        String IMSI = tm.getSubscriberId();
        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
            ProvidersName = "中国移动";
        } else if (IMSI.startsWith("46001")) {
            ProvidersName = "中国联通";
        } else if (IMSI.startsWith("46003")) {
            ProvidersName = "中国电信";
        }
        return ProvidersName;
    }

    /**
     * 验证邮箱格式
     * */
    public static boolean isEmail(String email) {
        String str = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();

    }

    /**
     * 验证手机号码格式 . 移动：134、135、136、137、138、139、150、151、152、157(TD)、158、159、187、188
     * 联通：130、131、132、155、156、185、186 电信：133、153、180、189、（1349卫通）
     * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
     * */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^1(3[0-9]|4[57]|5[0-35-9]|7[0-9]|8[0-9])\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();

    }

    /**
     * 电话号码验证【验证带区号】
     * 
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isPhone(String str) {
        if (str.length() > 9) {
            Pattern p1 = null;
            Matcher m = null;
            p1 = Pattern.compile("^0(10|2[0-5789]|\\d{3})\\d{7,8}$");
            m = p1.matcher(str);
            return m.matches();
        } else {
            return false;
        }
    }

    /**
     * 检测Sdcard是否存在
     * 
     * @Title: isExitsSdcard
     * @return boolean
     * @author 景庆超
     * @since 2014-11-19 V 1.0
     */
    public static boolean isExitsSdcard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    /**
     * 获取版本号.
     * 
     * @param context
     * @return int
     */
    public static int getAppVersionCode(Context context) {
        int version = 1;
        PackageInfo info;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = info.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 获取版本名称.
     * 
     * @param context
     * @return String
     */
    public static String getAppVersionName(Context context) {
        String version = "1.0";
        PackageInfo info;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }
}
