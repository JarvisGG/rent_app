package com.hc.core.update;

import android.content.Context;
import android.view.WindowManager;

/**
 * @author 李君波
 *
 */
public class UtilWindow {
    
    /**
     * 工具类禁止实例化
     */
    private UtilWindow(){
        
    }
    
    /** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     * @param context
     * @param dpValue
     * @author 李君波
     * @since 2014年11月19日 V 1.0
     */
    public static int dip2px(Context context,float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context,float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 返回屏幕宽度
     * 
     * @Title: getScreenWidth
     * @param context
     * @return int
     * @author 景庆超
     * @since 2014-11-19 V 1.0
     */
    @SuppressWarnings("deprecation")
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    /**
     * 返回屏幕高度
     * 
     * @Title: getScreenHeight
     * @param context
     * @return int
     * @author 景庆超
     * @since 2014-11-19 V 1.0
     */
    @SuppressWarnings("deprecation")
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }
    
}
