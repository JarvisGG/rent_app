package com.hc.xiaobairent;

import java.util.ArrayList;
import java.util.List;

import com.hc.core.utils.RentConstants;
import com.hc.core.utils.SharedpfTools;
import com.hc.xiaobairent.fragment.IndexFragment;
import com.hc.xiaobairent.fragment.MineFragment;
import com.hc.xiaobairent.fragment.SearchFragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

/**
 * 
 * @ClassName: MainActivity
 * @Description: 主Activity
 * @author frank.fun@qq.com
 * @date 2016年5月12日 上午9:54:05
 *
 */
public class MainActivity extends FragmentActivity implements OnClickListener {
	private Fragment indexFragment;
	private Fragment searchFragment;
	private Fragment mineFragment;
	private List<Fragment> fragments = new ArrayList<Fragment>();
	private FragmentPagerAdapter adapter;
	private ViewPager view_pager;
	private LinearLayout home_ll;
	private ImageView home_iv;
	private TextView home_tv;
	private LinearLayout search_ll;
	private ImageView search_iv;
	private TextView search_tv;
	private LinearLayout mine_ll;
	private ImageView mine_iv;
	private TextView mine_tv;
	private int userType = 0;
	private Context context = this;
	private SharedpfTools sp;

	private int currentItem;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.main_activity);
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		initView();
		initData();
	}

	private void initView() {
		view_pager = (ViewPager) findViewById(R.id.view_pager);
		home_ll = (LinearLayout) findViewById(R.id.home_ll);
		home_iv = (ImageView) findViewById(R.id.home_iv);
		home_tv = (TextView) findViewById(R.id.home_tv);
		search_ll = (LinearLayout) findViewById(R.id.search_ll);
		search_iv = (ImageView) findViewById(R.id.search_iv);
		search_tv = (TextView) findViewById(R.id.search_tv);
		mine_ll = (LinearLayout) findViewById(R.id.mine_ll);
		mine_iv = (ImageView) findViewById(R.id.mine_iv);
		mine_tv = (TextView) findViewById(R.id.mine_tv);
		home_ll.setOnClickListener(this);
		search_ll.setOnClickListener(this);
		mine_ll.setOnClickListener(this);
	}

	private void initData() {
		sp = SharedpfTools.getInstance(context);
		userType = sp.getUserType();
		indexFragment = new IndexFragment();
		fragments.add(indexFragment);
		if (userType != RentConstants.OWNER) {
			searchFragment = new SearchFragment();
			fragments.add(searchFragment);
		} else {
			search_ll.setVisibility(View.GONE);
		}
		mineFragment = new MineFragment();
		fragments.add(mineFragment);
		adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return fragments.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return fragments.get(arg0);
			}
		};
		view_pager.setAdapter(adapter);
		view_pager.setOffscreenPageLimit(3);
		view_pager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				currentItem = view_pager.getCurrentItem();
				setTab(currentItem);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	private void setTab(int currentItem) {
		switch (currentItem) {
		case 0:
			home_iv.setImageResource(R.drawable.home_focus);
			home_tv.setTextColor(getResources().getColor(R.color.theme_color));
			if (userType != RentConstants.OWNER) {
				search_iv.setImageResource(R.drawable.search);
				search_tv.setTextColor(getResources().getColor(R.color.txt_gray));
				mine_iv.setImageResource(R.drawable.mine);
				mine_tv.setTextColor(getResources().getColor(R.color.txt_gray));
			} else {
				mine_iv.setImageResource(R.drawable.mine);
				mine_tv.setTextColor(getResources().getColor(R.color.txt_gray));
			}
			break;
		case 1:
			home_iv.setImageResource(R.drawable.home);
			home_tv.setTextColor(getResources().getColor(R.color.txt_gray));
			if (userType != RentConstants.OWNER) {
				search_iv.setImageResource(R.drawable.search_focus);
				search_tv.setTextColor(getResources().getColor(R.color.theme_color));
				mine_iv.setImageResource(R.drawable.mine);
				mine_tv.setTextColor(getResources().getColor(R.color.txt_gray));
			} else {
				mine_iv.setImageResource(R.drawable.mine_focus);
				mine_tv.setTextColor(getResources().getColor(R.color.theme_color));
			}
			break;
		case 2:
			home_iv.setImageResource(R.drawable.home);
			home_tv.setTextColor(getResources().getColor(R.color.txt_gray));
			search_iv.setImageResource(R.drawable.search);
			search_tv.setTextColor(getResources().getColor(R.color.txt_gray));
			mine_iv.setImageResource(R.drawable.mine_focus);
			mine_tv.setTextColor(getResources().getColor(R.color.theme_color));
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_ll:
			view_pager.setCurrentItem(0);
			break;
		case R.id.search_ll:
			view_pager.setCurrentItem(1);
			break;
		case R.id.mine_ll:
			if (userType != RentConstants.OWNER) {
				view_pager.setCurrentItem(2);
			} else {
				view_pager.setCurrentItem(2);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 双击返回函数
	 */
	private static Boolean isExit = false;

	@Override
	public void onBackPressed() {
		if (!isExit) {
			isExit = true;
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					isExit = false;
				}
			}, 2000);
		} else {
//			sp.clear();
//			sp.setUserType(RentConstants.USER);
			super.onBackPressed();
		}
	}

	// 通过权限渲染fragment
	@Override
	protected void onResume() {
		super.onResume();
		userType = sp.getUserType();
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		if (userType == RentConstants.OWNER) {
			if (searchFragment != null) {
				ft.hide(searchFragment);
			}
			ft.commit();
		}
		setTab(currentItem);

	}
}
