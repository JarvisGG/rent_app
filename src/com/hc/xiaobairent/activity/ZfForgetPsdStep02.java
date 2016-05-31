package com.hc.xiaobairent.activity;

import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.ui.BindView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.hc.core.base.BaseActivity;
import com.hc.core.utils.CheckPassword;
import com.hc.core.utils.RentConstants;
import com.hc.core.utils.SharedpfTools;
import com.hc.core.utils.Sign;
import com.hc.core.utils.SignUtils;
import com.hc.core.utils.UrlConnector;
import com.hc.xiaobairent.R;
import com.hc.xiaobairent.dialog.SuccessDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;

public class ZfForgetPsdStep02 extends BaseActivity{
	/** 顶部标题 */
	@BindView(id = R.id.menu_title)
	private TextView tvTopbar;
	@BindView(id = R.id.menu_back, click = true)
	private ImageView ivBack;
	@BindView(id = R.id.menu_title_right)
	private TextView ivMineRigth;
	
	/** 注册菜单 */
	@BindView(id = R.id.edt_psd)
	private EditText edt_psd;
	@BindView(id = R.id.edt_repsd)
	private EditText edt_repsd;
	@BindView(id = R.id.next_btu, click=true)
	private Button register_btu;
	
	private Sign sign;
	private AbHttpUtil httpUtil;
	
	private String vercode = "";
	private String psd = "";
	private String repsd = "";
	private String mobile = "";
	
	@Override
	public void setRootView() {
		setAbContentView(R.layout.zf_activity_changepsd_step02);
	}
	
	@Override
	public void initData() {
		super.initData();
		initTab();
		Bundle bundle = getIntent().getExtras();
		mobile = bundle.getString("mobile");
		vercode = bundle.getString("vercode");
		psd = edt_psd.getText().toString().trim();
		repsd = edt_repsd.getText().toString().trim();
		httpUtil = AbHttpUtil.getInstance(ZfForgetPsdStep02.this);
	}
	
	private void initTab() {
		ivMineRigth.setVisibility(View.VISIBLE);
		ivBack.setVisibility(View.VISIBLE);
		tvTopbar.setVisibility(View.VISIBLE);
		tvTopbar.setText("修改密码");
		ivMineRigth.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.menu_back:
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;
		
		case R.id.next_btu:
			
				applyData();
//				SuccessDialog registerSuccessDialog = new SuccessDialog(ZfForgetPsdStep02.this);
//				registerSuccessDialog.show();
			
		default:
			break;
		}
	}
	
	private void applyData() {
		String source;
		psd = edt_psd.getText().toString().trim();
		repsd = edt_repsd.getText().toString().trim();
		if(!psd.equals(repsd)) {
			Toast.makeText(ZfForgetPsdStep02.this, "两次输入的不一致", Toast.LENGTH_SHORT).show();
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("phone", mobile);
			map.put("code", vercode);
			map.put("password", psd);
			map.put("password_", repsd);
			
			JSONObject jsonObject = new JSONObject(map);
			source = jsonObject.toString();
			
			AbRequestParams params = new AbRequestParams();
			//params.put("data", SignUtils.sign(source, SignUtils.PRIVATE_KEY));
			params.put("phone", mobile);
			params.put("code", vercode);
			params.put("password", psd);
			params.put("password_", repsd);
			
			String url = UrlConnector.FROGETPSD;
			httpUtil.post(url,params, new AbStringHttpResponseListener() {
				
				@Override
				public void onStart() {
					
				}
				
				@Override
				public void onFinish() {
					
				}
				
				@Override
				public void onFailure(int statusCode, String content, Throwable error) {
					Toast.makeText(ZfForgetPsdStep02.this, "修改失败", Toast.LENGTH_SHORT).show();
				}
				
				@Override
				public void onSuccess(int statusCode, String content) {
					Log.v("修改返回结果", content);
					Intent intent = new Intent();
					try {
						JSONObject object = new JSONObject(content);
						int state = object.getInt("state");
						if(state == 1) {
							intent.setClass(ZfForgetPsdStep02.this, ZfLoginActivity.class);
							startActivity(intent);
						} else {
							Toast.makeText(ZfForgetPsdStep02.this, object.getString("message"), Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		
		if(ev.getAction() == MotionEvent.ACTION_DOWN) {
			View view = getCurrentFocus();
			if(isShouldHideInput(view, ev)) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if(imm != null) {
					imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
				}
			}
			return super.dispatchTouchEvent(ev);
		}
		 // 必不可少，否则所有的组件都不会有TouchEvent了  
		if (getWindow().superDispatchTouchEvent(ev)) {  
	        return true;  
	    }  
	    return onTouchEvent(ev); 
	}
	
	public boolean isShouldHideInput(View v, MotionEvent event) {
		if(v != null && (v instanceof EditText)) {
			int[] leftTop = {0, 0};
			v.getLocationInWindow(leftTop);
			int left = leftTop[0];  
	        int top = leftTop[1];  
	        int bottom = top + v.getHeight();  
	        int right = left + v.getWidth(); 
	        if(event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
	        	return false;
	        } else {
	        	return true;
	        }
		}
		return false;  
	}

}
