package com.hc.xiaobairent.fragment;

import java.util.zip.Inflater;

import org.kymjs.kjframe.KJBitmap;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.google.gson.Gson;
import com.hc.core.utils.RentConstants;
import com.hc.core.utils.SharedpfTools;
import com.hc.core.utils.SlidingPlayView;
import com.hc.core.utils.UrlConnector;
import com.hc.xiaobairent.R;
import com.hc.xiaobairent.activity.HouseDetailActivity;
import com.hc.xiaobairent.activity.MoreActivity;
import com.hc.xiaobairent.activity.NewHouseActivity;
import com.hc.xiaobairent.activity.RentManagmentActivity;
import com.hc.xiaobairent.activity.ZfDeputeFindRoomActivity;
import com.hc.xiaobairent.activity.ZfDeputeLookRoomActivity;
import com.hc.xiaobairent.activity.ZfHouseListActivity;
import com.hc.xiaobairent.model.AdsModel;
import com.hc.xiaobairent.model.HouseModel;
import com.hc.xiaobairent.model.IndexModel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @ClassName: IndexFragment
 * @Description: 首页
 * @author frank.fun@qq.com
 * @date 2016年5月12日 上午9:32:48
 *
 */
public class IndexFragment extends Fragment implements OnClickListener, OnHeaderRefreshListener {
	protected static final String TAG = "IndexFragment";
	private AbPullToRefreshView mPullRefreshView;
	// private AbSlidingPlayView sliding_play_view;
	private RelativeLayout sliding_play_view;
	private SlidingPlayView slidingPlayView;
	private LinearLayout center_left_ll;
	private LinearLayout two_ll;
	private ImageView center_left_iv;
	private TextView center_left_tv;
	private ImageView center_right_iv;
	private TextView center_right_tv;
	private LinearLayout new_ll;
	private TextView num_tv;
	private TextView title_one_list_tv;
	private TextView more_one_tv;
	private ImageView more_one_iv;
	private LinearLayout one_content_ll;
	private TextView title_two_list_tv;
	private TextView more_two_tv;
	private ImageView more_two_iv;
	private LinearLayout two_content_ll;
	private int userType;
	private SharedpfTools sp;
	private AbHttpUtil http;
	private AbRequestParams params;
	private Context context;
	private IndexModel indexModel;
	private KJBitmap bitmap;
	private LayoutInflater inflater;
	
	private static final int RENT = 1;
	private static final int NRENT = 2;

	@Override
	public void onResume() {
		super.onResume();
		getData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.index_fragment, container, false);
		mPullRefreshView = (AbPullToRefreshView) view.findViewById(R.id.mPullRefreshView);
		// sliding_play_view = (AbSlidingPlayView)
		// view.findViewById(R.id.sliding_play_view);
		sliding_play_view = (RelativeLayout) view.findViewById(R.id.sliding_play_view);
		center_left_ll = (LinearLayout) view.findViewById(R.id.center_left_ll);
		two_ll = (LinearLayout) view.findViewById(R.id.two_ll);
		center_left_iv = (ImageView) view.findViewById(R.id.center_left_iv);
		center_left_tv = (TextView) view.findViewById(R.id.center_left_tv);
		center_right_iv = (ImageView) view.findViewById(R.id.center_right_iv);
		center_right_tv = (TextView) view.findViewById(R.id.center_right_tv);
		new_ll = (LinearLayout) view.findViewById(R.id.new_ll);
		num_tv = (TextView) view.findViewById(R.id.num_tv);
		title_one_list_tv = (TextView) view.findViewById(R.id.title_one_list_tv);
		more_one_tv = (TextView) view.findViewById(R.id.more_one_tv);
		more_one_iv = (ImageView) view.findViewById(R.id.more_one_iv);
		one_content_ll = (LinearLayout) view.findViewById(R.id.one_content_ll);
		title_two_list_tv = (TextView) view.findViewById(R.id.title_two_list_tv);
		more_two_tv = (TextView) view.findViewById(R.id.more_two_tv);
		more_two_iv = (ImageView) view.findViewById(R.id.more_two_iv);
		two_content_ll = (LinearLayout) view.findViewById(R.id.two_content_ll);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		context = getContext();
		sp = SharedpfTools.getInstance(context);
		http = AbHttpUtil.getInstance(context);
		inflater = LayoutInflater.from(context);
		bitmap = new KJBitmap();
		userType = sp.getUserType();
		slidingPlayView = new SlidingPlayView(sliding_play_view, context);
		switch (userType) {
		case RentConstants.USER:
			two_ll.setVisibility(View.GONE);
			break;
		case RentConstants.BROKER:
			two_ll.setVisibility(View.GONE);
			center_left_tv.setText("委托看房");
			center_right_iv.setImageResource(R.drawable.rent_management);
			center_right_tv.setText("出租管理");
			title_one_list_tv.setText("找房源");
			break;
		case RentConstants.OWNER:
			center_left_ll.setVisibility(View.GONE);
			title_one_list_tv.setText("已租房源");
			break;
		default:
			break;
		}
		mPullRefreshView.setOnHeaderRefreshListener(this);
		mPullRefreshView.setLoadMoreEnable(false);
		center_left_iv.setOnClickListener(this);
		center_left_tv.setOnClickListener(this);
		center_right_iv.setOnClickListener(this);
		center_right_tv.setOnClickListener(this);
		more_one_tv.setOnClickListener(this);
		more_one_iv.setOnClickListener(this);
		more_two_tv.setOnClickListener(this);
		more_two_iv.setOnClickListener(this);
	}

	private void getData() {
		slidingPlayView.stopPlay();
		params = new AbRequestParams();
		params.put("userGenre", userType);
		if (userType == RentConstants.OWNER) {
			params.put("access-token", sp.getAccessToken());
		}
		http.post(UrlConnector.INDEX, params, new AbStringHttpResponseListener() {

			@Override
			public void onStart() {
			}

			@Override
			public void onFinish() {
			}

			@Override
			public void onFailure(int statusCode, String content, Throwable error) {
				mPullRefreshView.onHeaderRefreshFinish();
			}

			@Override
			public void onSuccess(int statusCode, String t) {
				mPullRefreshView.onHeaderRefreshFinish();
				Log.v(TAG, t);
				indexModel = new Gson().fromJson(t, IndexModel.class);
				if (indexModel != null) {
					loadData();
				}
			}

		});
	}

	private void loadData() {
		/** 轮播图 */
		slidingPlayView.removeAllViews();
		for (AdsModel ad : indexModel.getAd()) {
			ImageView imageView = new ImageView(context);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setMaxWidth(ViewGroup.LayoutParams.MATCH_PARENT);
			imageView.setMaxHeight(ViewGroup.LayoutParams.MATCH_PARENT);
			bitmap.display(imageView, ad.getAd_pic());
			slidingPlayView.addView(imageView);
		}
		slidingPlayView.useNavigator(R.drawable.dot_focused, R.drawable.dot_unfocus);
		slidingPlayView.startPlay();
		// sliding_play_view.setNavPageResources(R.drawable.dot_focused,
		// R.drawable.dot_unfocus);
		// sliding_play_view.setNavHorizontalGravity(Gravity.RIGHT);
		// sliding_play_view.startPlay();
		/** 新上房源 */
		if (TextUtils.isEmpty(indexModel.getNum()) || indexModel.getNum().equals("0")) {
			new_ll.setVisibility(View.GONE);
		} else {
			new_ll.setVisibility(View.VISIBLE);
			num_tv.setText(indexModel.getNum());
		}
		/** 列表一 */
		one_content_ll.removeAllViews();
		if (indexModel.getPark1() != null) {
			int i = 0;
			for (final HouseModel house : indexModel.getPark1()) {
				if (i < 5) {
					View view = inflater.inflate(R.layout.list_item_house_img_bg, one_content_ll, false);
					houseHolder holder = new houseHolder();
					holder.pic_iv = (ImageView) view.findViewById(R.id.pic_iv);
					holder.info_tv = (TextView) view.findViewById(R.id.info_tv);
					holder.title_tv = (TextView) view.findViewById(R.id.title_tv);
					holder.num_tv = (TextView) view.findViewById(R.id.num_tv);
					bitmap.display(holder.pic_iv, house.getImg());
					holder.info_tv.setText(house.getAddress() + "\t\t\t\t" + house.getPrice() + "元/㎡·天");
					holder.title_tv.setText(house.getPark_name());
					holder.num_tv.setText(house.getNum() + "套正在出租");
					view.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if(sp.getUserType() == RentConstants.OWNER) {
								startActivity(new Intent(getActivity(), ZfHouseListActivity.class)
										.putExtra(RentConstants.ID_PARAM, Integer.parseInt(house.getId())).putExtra("status", NRENT));
							} else {
								startActivity(new Intent(context, HouseDetailActivity.class).putExtra(RentConstants.ID_PARAM,
										house.getId()));
							}
						}
					});
					one_content_ll.addView(view);
				}
				i++;
			}
		}
		/** 列表二 */
		two_content_ll.removeAllViews();
		if (two_ll.getVisibility() == View.VISIBLE && indexModel.getPark2() != null) {
			int i = 0;
			for (final HouseModel house : indexModel.getPark2()) {
				if (i < 5) {
					View view = inflater.inflate(R.layout.list_item_house_img_bg, two_content_ll, false);
					houseHolder holder = new houseHolder();
					holder.pic_iv = (ImageView) view.findViewById(R.id.pic_iv);
					holder.info_tv = (TextView) view.findViewById(R.id.info_tv);
					holder.title_tv = (TextView) view.findViewById(R.id.title_tv);
					holder.num_tv = (TextView) view.findViewById(R.id.num_tv);
					bitmap.display(holder.pic_iv, house.getImg());
					holder.info_tv.setText(house.getAddress() + "\t\t\t\t" + house.getPrice() + "元/㎡·天");
					holder.title_tv.setText(house.getPark_name());
					holder.num_tv.setText(house.getNum() + "套正在出租");
					view.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if(sp.getUserType() == RentConstants.OWNER) {
							startActivity(new Intent(getActivity(), ZfHouseListActivity.class)
									.putExtra(RentConstants.ID_PARAM, Integer.parseInt(house.getId())).putExtra("status", RENT));
							} else {
								startActivity(new Intent(context, HouseDetailActivity.class).putExtra(RentConstants.ID_PARAM,
										house.getId()));
							}
						}
					});
					two_content_ll.addView(view);
				}
				i++;
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.center_left_iv:
		case R.id.center_left_tv:
			if (userType == RentConstants.USER) {
				startActivity(new Intent(getActivity(), ZfDeputeFindRoomActivity.class));
			} else {
				startActivity(new Intent(getActivity(), ZfDeputeLookRoomActivity.class));
			}
			break;
		case R.id.center_right_iv:
		case R.id.center_right_tv:
			// FIXME 测试出租管理
			switch (center_right_tv.getText().toString().trim()) {
			case "出租管理":
				startActivity(new Intent(getActivity(), RentManagmentActivity.class));
				break;
			default:
				startActivity(new Intent(getActivity(), NewHouseActivity.class));
				break;
			}
			// if (userType == RentConstants.USER) {
			// startActivity(new Intent(getActivity(),
			// RentManagmentActivity.class));
			// } else {
			// startActivity(new Intent(getActivity(),
			// NewEstateActivity.class));
			// }
			break;
		case R.id.more_one_tv:
		case R.id.more_one_iv:
			startActivity(new Intent(getActivity(), MoreActivity.class).putExtra(RentConstants.TITLE_PARAM,
					title_one_list_tv.getText().toString().trim()));
			break;
		case R.id.more_two_tv:
		case R.id.more_two_iv:
			startActivity(new Intent(getActivity(), MoreActivity.class).putExtra(RentConstants.TITLE_PARAM,
					title_two_list_tv.getText().toString().trim()));
			break;
		default:
			break;
		}
	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView view) {
		getData();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		slidingPlayView.stopPlay();
	}

	private class houseHolder {
		public ImageView pic_iv;
		public TextView info_tv;
		public TextView title_tv;
		public TextView num_tv;
	}
}
