package com.hc.core.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class SlidingPlayView {
	private RelativeLayout container;
	private ViewPager viewPager;
	private LinearLayout navLayout;
	private Context context;
	private boolean useNavigator = false;
	private int focused;
	private int unfocused;
	private PagerAdapter adapter;
	private List<View> views = new ArrayList<View>();

	public SlidingPlayView(RelativeLayout container, Context context) {
		this.container = container;
		this.context = context;
		viewPager = new ViewPager(context);
		viewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		adapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				container.removeView(views.get(position));

			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				container.addView(views.get(position));
				return views.get(position);
			}
		};
		viewPager.setAdapter(adapter);
		viewPager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				if (useNavigator) {
					resetNavigator(arg0);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		this.container.addView(viewPager);
	}

	public void addView(View view) {
		views.add(view);
		adapter.notifyDataSetChanged();
	}

	public void useNavigator(int focused, int unfocused) {
		this.focused = focused;
		this.unfocused = unfocused;
		useNavigator = true;
		navLayout = new LinearLayout(context);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 40);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		navLayout.setLayoutParams(params);
		navLayout.setOrientation(LinearLayout.HORIZONTAL);
		navLayout.setGravity(Gravity.RIGHT);
		for (int i = 0; i < views.size(); i++) {
			ImageView imageView = new ImageView(context);
			imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			imageView.setPadding(10, 10, 10, 10);
			imageView.setScaleType(ScaleType.CENTER_INSIDE);
			imageView.setImageDrawable(context.getResources().getDrawable(unfocused));
			navLayout.addView(imageView);
		}
		container.addView(navLayout);
		resetNavigator(0);
	}

	private void resetNavigator(int arg0) {
		for (int i = 0; i < views.size(); i++) {
			if (i == arg0) {
				((ImageView) navLayout.getChildAt(i)).setImageDrawable(context.getResources().getDrawable(focused));
			} else {
				((ImageView) navLayout.getChildAt(i)).setImageDrawable(context.getResources().getDrawable(unfocused));
			}
		}
	}

	/** 用于轮播的线程. */
	private Runnable runnable;
	private boolean play = false;

	/**
	 * 描述：自动轮播.
	 */
	public void startPlay() {
		runnable = new Runnable() {
			public void run() {
				if (viewPager != null) {
					handler.sendEmptyMessage(0);
				}
			}
		};
		if (handler != null) {
			play = true;
			handler.postDelayed(runnable, 5000);
		}
	}

	/**
	 * 描述：自动轮播.
	 */
	public void stopPlay() {
		if (handler != null) {
			play = false;
			container.removeView(navLayout);
			handler.removeCallbacks(runnable);
		}
	}

	/** 用与轮换的 handler. */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				int count = views.size() - 1;
				int i = viewPager.getCurrentItem();
				if (i != count) {
					i++;
					viewPager.setCurrentItem(i, true);
				} else {
					i = 0;
					viewPager.setCurrentItem(i, false);
				}
				if (play) {
					handler.postDelayed(runnable, 5000);
				}
			}
		}

	};

	public void removeAllViews() {
		views.clear();
		adapter.notifyDataSetChanged();
	}
}
