package com.hc.xiaobairent.activity;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbStringHttpResponseListener;
import com.google.gson.Gson;
import com.hc.core.utils.RentConstants;
import com.hc.core.utils.SharedpfTools;
import com.hc.core.utils.Sign;
import com.hc.core.utils.UrlConnector;
import com.hc.xiaobairent.R;
import com.hc.xiaobairent.model.HouseModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @ClassName: RentManagmentDetailActivity
 * @Description: 出租管理详情
 * @author frank.fun@qq.com
 * @date 2016年5月20日 上午10:42:02
 *
 */
public class RentManagmentDetailActivity extends Activity {
	protected static final String TAG = "RentManagmentDetailActivity";
	private ImageView back;
	private TextView title;
	private TextView source_tv;
	private TextView date_tv;
	private TextView rent_tv;
	private TextView times_tv;
	private TextView times_current_tv;
	private TextView name_tv;
	private TextView phone_tv;
	private TextView contracts_tv;
	private AbHttpUtil http;
	private Context context = this;
	private HouseModel houseModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rent_managment_detail_activity);
		back = (ImageView) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		source_tv = (TextView) findViewById(R.id.source_tv);
		date_tv = (TextView) findViewById(R.id.date_tv);
		rent_tv = (TextView) findViewById(R.id.rent_tv);
		times_tv = (TextView) findViewById(R.id.times_tv);
		times_current_tv = (TextView) findViewById(R.id.times_current_tv);
		name_tv = (TextView) findViewById(R.id.name_tv);
		phone_tv = (TextView) findViewById(R.id.phone_tv);
		contracts_tv = (TextView) findViewById(R.id.contracts_tv);
		contracts_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				// FIXME contract待做
				bundle.putString("wordUrl", houseModel.getContract().toString());
				startActivity(new Intent(context, ZfMyRoomContract.class).putExtras(bundle));
			}
		});
		title.setText("出租详情");
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		http = AbHttpUtil.getInstance(context);
		http.get(UrlConnector.RENT_MANAGE_DETAIL + getIntent().getStringExtra(RentConstants.ID_PARAM),
				new AbStringHttpResponseListener() {

					@Override
					public void onStart() {
					}

					@Override
					public void onFinish() {
					}

					@Override
					public void onFailure(int statusCode, String content, Throwable error) {
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						Log.v(TAG, content);
						houseModel = new Gson().fromJson(content, HouseModel.class);
						source_tv.setText(houseModel.getPark_name() + "\t\t\t" + houseModel.getHouse_name());
						date_tv.setText(houseModel.getStart_time() + "\t至\t" + houseModel.getEnd_time());
						rent_tv.setText(houseModel.getPrice() + "元/月");
						times_tv.setText(houseModel.getStage_num() + "期");
						times_current_tv.setText("第" + houseModel.getCurrent_num() + "期");
						name_tv.setText(houseModel.getUsername());
						phone_tv.setText(houseModel.getMobile());
					}
				});
	}
}
