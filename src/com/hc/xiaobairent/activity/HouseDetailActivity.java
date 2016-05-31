package com.hc.xiaobairent.activity;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJBitmap;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.google.gson.Gson;
import com.hc.core.utils.RentConstants;
import com.hc.core.utils.SharedpfTools;
import com.hc.core.utils.Sign;
import com.hc.core.utils.SlidingPlayView;
import com.hc.core.utils.UrlConnector;
import com.hc.xiaobairent.R;
import com.hc.xiaobairent.model.HouseDetailModel;
import com.hc.xiaobairent.model.HouseModel;
import com.hc.xiaobairent.model.UrlModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @ClassName: HouseDetailActivity
 * @Description: 园区详情
 * @author frank.fun@qq.com
 * @date 2016年5月18日 上午10:15:52
 *
 */
public class HouseDetailActivity extends Activity implements OnClickListener {
	protected static final String TAG = "HouseDetailActivity";
	private ImageView back;
	private TextView title;
	private RelativeLayout sliding_play_view;
	private SlidingPlayView slidingPlayView;
	private TextView park_name_tv;
	private TextView address_tv;
	private TextView num_tv;
	private TextView price_tv;
	private LinearLayout address_ll;
	private TextView address_detail_tv;
	private TextView region_tv;
	private TextView area_tv;
	private TextView house_type_tv;
	private TextView company_tv;
	private LinearLayout house_ll;
	private TextView describe_tv;
	private TextView see_more_describe_tv;
	private SharedpfTools sp;
	private AbHttpUtil http;
	private AbRequestParams params;
	private Sign sign;
	private Context context = this;
	private String park_id = "0";
	private HouseDetailModel houseDetail;
	private KJBitmap bitmap;
	private LayoutInflater inflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.house_detail_activity);
		back = (ImageView) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		sliding_play_view = (RelativeLayout) findViewById(R.id.sliding_play_view);
		park_name_tv = (TextView) findViewById(R.id.park_name_tv);
		address_tv = (TextView) findViewById(R.id.address_tv);
		num_tv = (TextView) findViewById(R.id.num_tv);
		price_tv = (TextView) findViewById(R.id.price_tv);
		address_ll = (LinearLayout) findViewById(R.id.address_ll);
		address_detail_tv = (TextView) findViewById(R.id.address_detail_tv);
		region_tv = (TextView) findViewById(R.id.region_tv);
		area_tv = (TextView) findViewById(R.id.area_tv);
		house_type_tv = (TextView) findViewById(R.id.house_type_tv);
		company_tv = (TextView) findViewById(R.id.company_tv);
		house_ll = (LinearLayout) findViewById(R.id.house_ll);
		describe_tv = (TextView) findViewById(R.id.describe_tv);
		see_more_describe_tv = (TextView) findViewById(R.id.see_more_describe_tv);
		title.setText("星光中心大厦");
		back.setOnClickListener(this);
		see_more_describe_tv.setOnClickListener(this);
		sp = SharedpfTools.getInstance(context);
		http = AbHttpUtil.getInstance(context);
		inflater = LayoutInflater.from(context);
		bitmap = new KJBitmap();
		slidingPlayView = new SlidingPlayView(sliding_play_view, context);
		sign = new Sign(context);
		park_id = getIntent().getStringExtra(RentConstants.ID_PARAM);
		Log.v(TAG, park_id);
		addFootprints();
		getData();
	}

	private void getData() {
		if (sp.getLogStatus()) {
			sign.init();
			sign.addParam("id", park_id);
			http.get(UrlConnector.HOUSE_DETAIL + park_id + "?access-token=" + sp.getAccessToken() + UrlConnector.SIGN
					+ sign.getSign(), new AbStringHttpResponseListener() {

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
						public void onSuccess(int statusCode, String t) {
							Log.v(TAG, t);
							houseDetail = new Gson().fromJson(t, HouseDetailModel.class);
							loadData();
						}
					});
		} else {
			params = new AbRequestParams();
			params.put("id", park_id);
			http.post(UrlConnector.HOUSE_DETAIL_UNLOGIN, params, new AbStringHttpResponseListener() {

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
				public void onSuccess(int statusCode, String t) {
					Log.v(TAG, t);
					houseDetail = new Gson().fromJson(t, HouseDetailModel.class);
					loadData();
				}
			});
		}
	}

	private void loadData() {
		slidingPlayView.removeAllViews();
		for (UrlModel img : houseDetail.getImg()) {
			ImageView imageView = new ImageView(context);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setMaxWidth(LayoutParams.MATCH_PARENT);
			imageView.setMaxHeight(LayoutParams.MATCH_PARENT);
			bitmap.display(imageView, img.getUrl());
			slidingPlayView.addView(imageView);
		}
		slidingPlayView.useNavigator(R.drawable.dot_focused, R.drawable.dot_unfocus);
		slidingPlayView.startPlay();

		park_name_tv.setText(houseDetail.getPark_name());
		address_tv.setText(houseDetail.getAddress());
		num_tv.setText(houseDetail.getNum() + "套房源在租");
		price_tv.setText(houseDetail.getPrice() + "元/㎡·天");
		address_detail_tv.setText(houseDetail.getAddress());
		area_tv.setText(houseDetail.getArea() + "㎡");
		house_type_tv.setText(houseDetail.getHouse_type());
		company_tv.setText(houseDetail.getCompany());
		describe_tv.setText(houseDetail.getDescribe());
		Log.v(TAG, describe_tv.getLineCount() + "");
		if (houseDetail.getHouse() != null && houseDetail.getHouse().size() > 0) {
			region_tv.setText("查看全部" + houseDetail.getHouse().size() + "套房型");
			region_tv.setOnClickListener(this);
			addOneHouse(houseDetail.getHouse().get(0));
		} else {
			region_tv.setVisibility(View.GONE);
		}

	}

	private void addCollect(String position, final int command, final ImageView ivPic) {
		int id = Integer.parseInt(position);
		params = new AbRequestParams();
		sign.init();
		sign.addParam("house_id", id);
		params.put("house_id", id);
		String url = UrlConnector.MYCOLLECTION_ADD + sp.getAccessToken() + UrlConnector.SIGN + sign.getSign();
		http.post(url, params, new AbStringHttpResponseListener() {

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
				try {
					JSONObject jsonObject = new JSONObject(content);
					int status = jsonObject.getInt("status");
					if (status == 1) {
						if (command == 1) {
							Toast.makeText(HouseDetailActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
							ivPic.setImageResource(R.drawable.icon_6);
						} else {
							Toast.makeText(HouseDetailActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
							ivPic.setImageResource(R.drawable.icon_collect_hl);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});
	}

	private void addOneHouse(final HouseModel house) {
		View v = inflater.inflate(R.layout.list_item_house_img_left, address_ll, false);
		final ViewHolder holder = new ViewHolder();
		holder.pic = (ImageView) v.findViewById(R.id.pic);
		holder.title = (TextView) v.findViewById(R.id.title);
		holder.subtitle = (TextView) v.findViewById(R.id.subtitle);
		holder.summary = (TextView) v.findViewById(R.id.summary);
		holder.commission = (TextView) v.findViewById(R.id.commission);
		holder.collect_iv = (ImageView) v.findViewById(R.id.collect_iv);
		holder.rent_tv = (TextView) v.findViewById(R.id.rent_tv);
		if (sp.getUserType() == RentConstants.BROKER) {
			holder.subtitle_two = (TextView) v.findViewById(R.id.subtitle_two);
			holder.subtitle_two.setVisibility(View.VISIBLE);
			holder.subtitle_two.setText("佣金:" + house.getCharge());

		}
		bitmap.display(holder.pic, house.getImg());
		holder.title.setText(house.getPrice() + "元/月");
		holder.subtitle.setText(house.getArea() + "㎡\t\t\t\t" + house.getDay_price() + "元/㎡·天");
		holder.summary.setText(house.getHouse_type());
		holder.commission.setText(house.getCharge());
		if (house.getIs_fenqi() == HouseModel.YES) {
			holder.rent_tv.setText("小白分期租");
		} else {
			holder.rent_tv.setText("我要租");
		}
		/**
		 * @author xiaofei
		 * @function 收藏成功
		 * @date 2016年5月19日 上午15:43:52
		 */
		holder.collect_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (house.getIs_fav() == 1) {
					house.setIs_fav(0);
					addCollect(house.getId(), 1, holder.collect_iv);
				} else {
					house.setIs_fav(1);
					addCollect(house.getId(), 0, holder.collect_iv);
				}
			}
		});

		holder.rent_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, RentActivity.class);
				intent.putExtra(RentConstants.ID_PARAM, house.getId());
				intent.putExtra(RentConstants.CONTENT_PARAM, Float.valueOf(house.getPrice()));
				intent.putExtra(RentConstants.TYPE_PARAM, house.getIs_fenqi());
				startActivity(intent);
			}
		});
		house_ll.addView(v);
	}

	private void addFootprints() {
		sign.init();
		params = new AbRequestParams();
		params.put("park_id", park_id);
		http.post(UrlConnector.ADD_FOOT_PRINTS + sp.getAccessToken() + UrlConnector.SIGN + sign.getSign(), params,
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
					public void onSuccess(int statusCode, String t) {
						Log.v(TAG, t);
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.region_tv:
			if (region_tv.getText().toString().equals("收起")) {
				region_tv.setText("查看全部" + houseDetail.getHouse().size() + "套房型");
				region_tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
				if (houseDetail.getHouse() != null && houseDetail.getHouse().size() > 0) {
					house_ll.removeAllViewsInLayout();
					addOneHouse(houseDetail.getHouse().get(0));
				}
			} else {
				region_tv.setText("收起");
				region_tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_arrow, 0);
				if (houseDetail.getHouse() != null && houseDetail.getHouse().size() > 0) {
					house_ll.removeAllViewsInLayout();
					for (HouseModel house : houseDetail.getHouse()) {
						addOneHouse(house);
					}
				}
			}
			break;
		case R.id.see_more_describe_tv:
			if (describe_tv.getMaxLines() == 3) {
				describe_tv.setMaxLines(1000);
				see_more_describe_tv.setText("收起");
				see_more_describe_tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_arrow, 0);
			} else {
				describe_tv.setMaxLines(3);
				see_more_describe_tv.setText("点击查看更多");
				see_more_describe_tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		slidingPlayView.stopPlay();
	}

	public class ViewHolder {
		ImageView pic;
		TextView title;
		TextView subtitle;
		TextView subtitle_two;
		TextView commission;
		TextView summary;
		ImageView collect_iv;
		TextView rent_tv;
	}
}
