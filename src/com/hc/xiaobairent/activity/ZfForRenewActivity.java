package com.hc.xiaobairent.activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.BindView;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.google.gson.JsonObject;
import com.hc.core.base.BaseActivity;
import com.hc.core.utils.SharedpfTools;
import com.hc.core.utils.Sign;
import com.hc.core.utils.UrlConnector;
import com.hc.xiaobairent.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class ZfForRenewActivity extends BaseActivity{

	// 初始化标题
	@BindView(id = R.id.menu_back, click = true)
	private ImageView menuBack;
	@BindView(id = R.id.menu_title)
	private TextView menuTitle;
	
	@BindView(id = R.id.renew_date_select, click = true)
	private LinearLayout dateSelect;
	@BindView(id = R.id.tv_renew_date)
	private TextView tvRenewDate;
	@BindView(id = R.id.tv_housing_resources)
	private TextView tvHouse;
	@BindView(id = R.id.tv_checkout_date)
	private TextView tvCheckoutDate;
	@BindView(id = R.id.next_btu, click = true)
	private Button submitBtn;
	
	private static final String EWNEW = 1+"";
	private static final String STOPRENT = 2+"";
	
	private String dateBegin;
	private String dateEnd;
	
	private int id;
	
	// 网络参数
	private SharedpfTools sharedpfTools = SharedpfTools.getInstance(ZfForRenewActivity.this);
	private HttpUtils httpUtils;
	private RequestParams requestParams;
	private KJHttp kjHttp;
	private Sign sign;
	private AbHttpUtil http;
	private AbRequestParams params;
	
	@Override
	public void setRootView() {
		setAbContentView(R.layout.zf_activity_renew_request);
		
	}
	
	@Override
	public void initData() {
		super.initData();
		Bundle data = getIntent().getExtras();
		id = data.getInt("id");
		tvHouse.setText(data.getString("house"));
		dateBegin = data.getString("house_begin");
		dateEnd = data.getString("house_end");
		String date = dateBegin + " 至 " + dateEnd;
		tvCheckoutDate.setText(date);
		
		initTab();
	}
	
	private void initTab() {
		menuTitle.setText("续租申请");
	}
	
	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.menu_back:
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;
			
		case R.id.renew_date_select:
			startActivityForResult(new Intent(ZfForRenewActivity.this, ZfForRenewDateActivity.class), 0);
			overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			break;
		
		case R.id.next_btu:
			applyData();
			break;

		default:
			break;
		}
		
	}
	
	private void applyData() {
		sign = new Sign(ZfForRenewActivity.this);
		sign.init();
		http = AbHttpUtil.getInstance(ZfForRenewActivity.this);
		String url = UrlConnector.MYRENTS_OPERATION+id+"?access-token="
				+sharedpfTools.getAccessToken()+UrlConnector.SIGN+sign.getSign();
		
		params = new AbRequestParams();
		params.put("type", EWNEW);
		params.put("renew", (String) tvRenewDate.getText());
		params.put("_method", "put");
		
		http.post(url, params,new AbStringHttpResponseListener() {
			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFailure(int statusCode, String content, Throwable error) {
				Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
				
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				try {
					JSONObject jsonObject = new JSONObject(content);
					int status = jsonObject.getInt("status");
					Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
	}
	
//	private void applyData1() {
//		sign = new Sign(ZfForRenewActivity.this);
//		sign.init();
//		String url = UrlConnector.MYRENTS_OPERATION+id+"?access-token="
//				+sharedpfTools.getAccessToken()+UrlConnector.SIGN+sign.getSign();
//		kjHttp = new KJHttp();
//		
//		HttpParams params = new HttpParams();
//		params.put("type", EWNEW);
//		params.put("renew", (String) tvRenewDate.getText());
//		params.put("_method", "put");
//		
//		kjHttp.jsonPost(url, params, new HttpCallBack() {
//			@Override
//			public void onFailure(int errorNo, String strMsg) {
//				super.onFailure(errorNo, strMsg);
//				Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
//			}
//			
//			@Override
//			public void onSuccess(String t) {
//				super.onSuccess(t);
//				Toast.makeText(getApplicationContext(), t, Toast.LENGTH_SHORT).show();
//			}
//		});
//	}
//	
//	private void applyData() {
//		httpUtils = new HttpUtils();
//		sign = new Sign(ZfForRenewActivity.this);
//		sign.init();
//		requestParams = new RequestParams();
//		String url = UrlConnector.MYRENTS_OPERATION+id+"?access-token="
//		+sharedpfTools.getAccessToken()+UrlConnector.SIGN+sign.getSign();
//		
//		requestParams.addQueryStringParameter("type", EWNEW);
//		requestParams.addQueryStringParameter("renew", (String) tvRenewDate.getText());
//	//	requestParams.addQueryStringParameter("_method", "put");
//		httpUtils.send(HttpMethod.PUT, url, new RequestCallBack<String>() {
//
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> arg0) {
//				Log.e("asdasdasdasd", arg0.result);
//				try {
//					JSONObject jsonObject = new JSONObject(arg0.result);
//					int status = jsonObject.getInt("status");
//					Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//			}
//			
//		});
//	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			int dateRenew = data.getExtras().getInt("date");
			DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
			Date date_end;
			try {
	
				date_end = fmt.parse(dateEnd);
				Calendar f1 = Calendar.getInstance();
				f1.setTime(date_end);
				f1.add(Calendar.MONTH, dateRenew);
				date_end = f1.getTime();
				String date = dateBegin + " 至 " + fmt.format(date_end);
				tvCheckoutDate.setText(date);
			
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			chengeDate(dateRenew);
			
		}
	}
	
	private void chengeDate(int dateRenew) {
		switch (dateRenew) {
		case 1:
			tvRenewDate.setText("1个月");
			break;
		case 2:
			tvRenewDate.setText("2个月");
			break;
		case 3:
			tvRenewDate.setText("3个月");
			break;
		case 4:
			tvRenewDate.setText("4个月");
			break;
		case 5:
			tvRenewDate.setText("5个月");
			break;
		case 6:
			tvRenewDate.setText("6个月");
			break;
		case 7:
			tvRenewDate.setText("7个月");
			break;
		case 8:
			tvRenewDate.setText("8个月");
			break;
		case 9:
			tvRenewDate.setText("9个月");
			break;
		case 10:
			tvRenewDate.setText("10个月");
			break;
		case 11:
			tvRenewDate.setText("11个月");
			break;
		case 12:
			tvRenewDate.setText("12个月");
			break;
			
		default:
			break;
		}
	}
}