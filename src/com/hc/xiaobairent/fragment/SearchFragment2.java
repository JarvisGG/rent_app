package com.hc.xiaobairent.fragment;

import java.util.ArrayList;
import java.util.List;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hc.core.utils.RentConstants;
import com.hc.core.utils.SharedpfTools;
import com.hc.core.utils.Sign;
import com.hc.core.utils.UrlConnector;
import com.hc.xiaobairent.R;
import com.hc.xiaobairent.activity.HouseDetailActivity;
import com.hc.xiaobairent.activity.SearchActivity;
import com.hc.xiaobairent.adapter.HouseAdapter;
import com.hc.xiaobairent.adapter.IdNameAdapter;
import com.hc.xiaobairent.adapter.SelectAdapter;
import com.hc.xiaobairent.model.HouseModel;
import com.hc.xiaobairent.model.IdNameModel;
import com.hc.xiaobairent.model.ParksModel;
import com.hc.xiaobairent.utils.CityModel;
import com.hc.xiaobairent.utils.CityUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @ClassName: SearchFragment
 * @Description: 房源列表页
 * @author frank.fun@qq.com
 * @date 2016年5月12日 上午9:32:05
 *
 */
public class SearchFragment2 extends Fragment implements OnClickListener, OnHeaderRefreshListener {
	protected static final String TAG = "SearchFragment";
	private static final int AREA = 0;
	private static final int DECRATION = 1;
	private static final int PRICE = 2;
	private AbPullToRefreshView mPullRefreshView;
	private TextView search_tv;
	private TextView region_tv;
	private TextView area_tv;
	private TextView decration_tv;
	private TextView price_tv;
	private ListView listview;
	private ListView first_lv;
	private ListView second_lv;
	private ListView third_lv;
	private ListView forth_lv;
	private HouseAdapter adapter;
	private List<HouseModel> list = new ArrayList<>();
	private LinearLayout select_ll;
	private AbHttpUtil http;
	private AbRequestParams params;
	private Sign sign;
	private SharedpfTools sp;
	private Context context;
	private List<CityModel> provienceList = new ArrayList<>();
	private List<CityModel> cityList = new ArrayList<>();
	private List<CityModel> regionList = new ArrayList<>();
	private List<IdNameModel> areaList = new ArrayList<>();
	private List<IdNameModel> decrationList = new ArrayList<>();
	private List<IdNameModel> priceList = new ArrayList<>();
	private IdNameAdapter areaAdapter;
	private IdNameAdapter decrationAdapter;
	private IdNameAdapter priceAdapter;
	private SelectAdapter pAdapter;
	private SelectAdapter cAdapter;
	private SelectAdapter rAdapter;

	private String park_name = "";// [string] 园区名称（绿景国际）
	private String province_id = "";// [int] 省id（4）
	private String city_id = "";// [int] 市id（53）
	private String district_id = "";// [int] 区id（518）
	private String area_shuttle = "";// [int] 面积区间id
	private String price_shuttle = "";// [int] 单价区间id
	private String redecorate = "";// [int] 装修分类id

	@Override
	public void onHeaderRefresh(AbPullToRefreshView view) {
		getData();
		getLists();
	}

	@Override
	public void onResume() {
		super.onResume();
		getData();
		getLists();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.search_fragment, container, false);
		mPullRefreshView = (AbPullToRefreshView) view.findViewById(R.id.mPullRefreshView);
		search_tv = (TextView) view.findViewById(R.id.search_tv);
		region_tv = (TextView) view.findViewById(R.id.region_tv);
		area_tv = (TextView) view.findViewById(R.id.area_tv);
		decration_tv = (TextView) view.findViewById(R.id.decration_tv);
		price_tv = (TextView) view.findViewById(R.id.price_tv);
		listview = (ListView) view.findViewById(R.id.listview);
		first_lv = (ListView) view.findViewById(R.id.first_lv);
		second_lv = (ListView) view.findViewById(R.id.second_lv);
		third_lv = (ListView) view.findViewById(R.id.third_lv);
		forth_lv = (ListView) view.findViewById(R.id.forth_lv);
		select_ll = (LinearLayout) view.findViewById(R.id.select_ll);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mPullRefreshView.setOnHeaderRefreshListener(this);
		mPullRefreshView.setLoadMoreEnable(false);
		search_tv.setOnClickListener(this);
		region_tv.setOnClickListener(this);
		area_tv.setOnClickListener(this);
		decration_tv.setOnClickListener(this);
		price_tv.setOnClickListener(this);
		context = getContext();
		http = AbHttpUtil.getInstance(context);
		provienceList = CityUtil.getList(context);
		sp = SharedpfTools.getInstance(context);
		sign = new Sign(context);
		adapter = new HouseAdapter(context, list);
		addAll(provienceList);
		pAdapter = new SelectAdapter(context, provienceList);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				startActivity(new Intent(getActivity(), HouseDetailActivity.class).putExtra(RentConstants.ID_PARAM,
						list.get(position).getId()));
			}
		});
	}

	private void addAll(List<CityModel> list) {
		CityModel all = new CityModel();
		all.setId(0);
		all.setRegion_name("全部");
		all.setRegion_type((short) 1);
		list.add(0, all);
	}

	private void getLists() {
		sign.init();
		params = new AbRequestParams();
		params.put("access-token", sp.getAccessToken());
		params.put("sign", sign.getSign());
		getAreaList(UrlConnector.AREA, params, AREA);
		getAreaList(UrlConnector.DECRATION, params, DECRATION);
		getAreaList(UrlConnector.PRICE, params, PRICE);
	}

	private void getAreaList(String url, AbRequestParams params, final int target) {
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
				List<IdNameModel> list = new Gson().fromJson(content, new TypeToken<List<IdNameModel>>() {
				}.getType());
				switch (target) {
				case AREA:
					areaList.clear();
					areaList.addAll(list);
					areaAdapter = new IdNameAdapter(context, areaList, Gravity.CENTER);
					break;
				case DECRATION:
					decrationList.clear();
					decrationList.addAll(list);
					decrationAdapter = new IdNameAdapter(context, decrationList, Gravity.CENTER);
					break;
				case PRICE:
					priceList.clear();
					priceList.addAll(list);
					priceAdapter = new IdNameAdapter(context, priceList, Gravity.CENTER);
					break;
				default:
					break;
				}
			}
		});
	}

	private void getData() {
		params = new AbRequestParams();
		params.put("park_name", park_name);
		params.put("province_id", province_id);
		params.put("city_id", city_id);
		params.put("district_id", district_id);
		params.put("area_shuttle", area_shuttle);
		params.put("price_shuttle", price_shuttle);
		params.put("redecorate", redecorate);
		http.get(UrlConnector.HOUSE_LIST, params, new AbStringHttpResponseListener() {

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
				list.clear();
				ParksModel parksModel = new Gson().fromJson(t, ParksModel.class);
				List<HouseModel> items = parksModel.getItems();
				if (items != null && items.size() > 0) {
					list.addAll(items);
				}
				adapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_tv:
			startActivity(new Intent(context, SearchActivity.class));
			break;
		case R.id.region_tv:
			if (select_ll.isShown() && first_lv.isShown()) {
				select_ll.setVisibility(View.GONE);
			} else {
				select_ll.setVisibility(View.VISIBLE);
				first_lv.setVisibility(View.VISIBLE);
				second_lv.setVisibility(View.INVISIBLE);
				third_lv.setVisibility(View.INVISIBLE);
				forth_lv.setVisibility(View.INVISIBLE);
				first_lv.setAdapter(pAdapter);
				first_lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						district_id = "";
						city_id = "";
						if (position == 0) {
							province_id = "";
							select_ll.setVisibility(View.GONE);
							getData();
						} else {
							province_id = provienceList.get(position).getId() + "";
							cityList.clear();
							if (provienceList.get(position).getSon() != null
									&& provienceList.get(position).getSon().size() > 0) {
								cityList.addAll(provienceList.get(position).getSon());
							}
							addAll(cityList);
							cAdapter = new SelectAdapter(context, cityList);
							second_lv.setVisibility(View.VISIBLE);
							second_lv.setAdapter(cAdapter);
							second_lv.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
									if (position == 0) {
										city_id = "";
										select_ll.setVisibility(View.GONE);
										getData();
									} else {
										city_id = cityList.get(position).getId() + "";
										regionList.clear();
										if (cityList.get(position).getSon() != null
												&& cityList.get(position).getSon().size() > 0) {
											regionList.addAll(cityList.get(position).getSon());
										}
										addAll(regionList);
										rAdapter = new SelectAdapter(context, regionList);
										third_lv.setVisibility(View.VISIBLE);
										third_lv.setAdapter(rAdapter);
										third_lv.setOnItemClickListener(new OnItemClickListener() {

											@Override
											public void onItemClick(AdapterView<?> parent, View view, int position,
													long id) {
												if (position == 0) {
													district_id = "";
												} else {
													district_id = regionList.get(position).getId() + "";
												}
												select_ll.setVisibility(View.GONE);
												getData();
											}
										});
									}
								}
							});
						}
					}
				});
			}
			break;
		case R.id.area_tv:
			if (select_ll.isShown()) {
				select_ll.setVisibility(View.GONE);
			} else {
				first_lv.setVisibility(View.GONE);
				second_lv.setVisibility(View.GONE);
				third_lv.setVisibility(View.GONE);
				select_ll.setVisibility(View.VISIBLE);
				forth_lv.setVisibility(View.VISIBLE);
				forth_lv.setAdapter(areaAdapter);
				forth_lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						if (position == 0) {
							area_shuttle = "";
						} else {
							area_shuttle = areaList.get(position).getId() + "";
						}
						area_tv.setText(areaList.get(position).getName());
						select_ll.setVisibility(View.GONE);
						getData();
					}
				});
			}
			break;
		case R.id.decration_tv:
			if (select_ll.isShown()) {
				select_ll.setVisibility(View.GONE);
			} else {
				first_lv.setVisibility(View.GONE);
				second_lv.setVisibility(View.GONE);
				third_lv.setVisibility(View.GONE);
				select_ll.setVisibility(View.VISIBLE);
				forth_lv.setVisibility(View.VISIBLE);
				forth_lv.setAdapter(decrationAdapter);
				forth_lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						if (position == 0) {
							redecorate = "";
						} else {
							redecorate = decrationList.get(position).getId() + "";
						}
						decration_tv.setText(decrationList.get(position).getName());
						select_ll.setVisibility(View.GONE);
						getData();
					}
				});
			}
			break;
		case R.id.price_tv:
			if (select_ll.isShown()) {
				select_ll.setVisibility(View.GONE);
			} else {
				first_lv.setVisibility(View.GONE);
				second_lv.setVisibility(View.GONE);
				third_lv.setVisibility(View.GONE);
				select_ll.setVisibility(View.VISIBLE);
				forth_lv.setVisibility(View.VISIBLE);
				forth_lv.setAdapter(priceAdapter);
				forth_lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						if (position == 0) {
							price_shuttle = "";
						} else {
							price_shuttle = priceList.get(position).getId() + "";
						}
						price_tv.setText(priceList.get(position).getName());
						select_ll.setVisibility(View.GONE);
						getData();
					}
				});
			}
			break;
		default:
			break;
		}
	}
}
