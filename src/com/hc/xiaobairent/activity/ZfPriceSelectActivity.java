package com.hc.xiaobairent.activity;
import java.lang.reflect.Type;
import java.util.List;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.BindView;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hc.core.base.BaseActivity;
import com.hc.core.utils.SharedpfTools;
import com.hc.core.utils.Sign;
import com.hc.core.utils.UrlConnector;
import com.hc.xiaobairent.R;
import com.hc.xiaobairent.adapter.HousePriceAdapter;
import com.hc.xiaobairent.model.HousePriceItemModel;
import com.hc.xiaobairent.model.HousePriceModel;
import com.hc.xiaobairent.model.HouseSizeItemModel;

public class ZfPriceSelectActivity extends BaseActivity implements OnItemClickListener{
	
	// 初始化标题
	@BindView(id = R.id.menu_back, click = true)
	private ImageView menuBack;
	@BindView(id = R.id.menu_title)
	private TextView menuTitle;
	
	@BindView(id = R.id.price_list)
	private ListView priceList;
	
	private HousePriceAdapter housePriceAdapter;
	private List<HousePriceItemModel> mList;
	
	private SharedpfTools sharedpfTools = SharedpfTools.getInstance(ZfPriceSelectActivity.this);
	private KJHttp http;
	private Gson gson;
	private Sign sign;
	private HousePriceModel housePriceModel;
	
	@Override
	public void setRootView() {
		setAbContentView(R.layout.zf_activity_price_select);
	}
	
	@Override
	public void initData() {
		super.initData();
		priceList.setOnItemClickListener(this);
		applyData();
	}
	
	private void applyData() {
		sign = new Sign(ZfPriceSelectActivity.this);
		sign.init();
		http = new KJHttp();
		gson = new Gson();
		String url = UrlConnector.PRICELIST+sharedpfTools.getAccessToken()+UrlConnector.SIGN+sign.getSign();
		
		HttpParams params = new HttpParams();
		
		http.jsonPost(url, params, new HttpCallBack() {
			
			@Override
			public void onFailure(int errorNo, String strMsg) {
				super.onFailure(errorNo, strMsg);
				Toast.makeText(getApplication(), "请求失败", Toast.LENGTH_SHORT).show();;
			}
			
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				Log.e("onSuccessPrice", t);
				Type listType = new TypeToken<List<HousePriceItemModel>>(){}.getType();
				mList = gson.fromJson(t, listType);
				//mList = housePriceModel.getPrice();
				housePriceAdapter = new HousePriceAdapter(ZfPriceSelectActivity.this, mList);
				priceList.setAdapter(housePriceAdapter);
				
			}
		
		});
	}
	
	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.menu_back:
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent();
		intent.putExtra("price", mList.get(position).getName());
		intent.putExtra("price_id", mList.get(position).getId());
		setResult(RESULT_OK, intent);
		onBackPressed();
		overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
	}

}
