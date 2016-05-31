package com.hc.xiaobairent.activity;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.view.chart.CategorySeries;
import com.ab.view.chart.ChartFactory;
import com.ab.view.chart.GraphicalView;
import com.ab.view.chart.PointStyle;
import com.ab.view.chart.XYMultipleSeriesDataset;
import com.ab.view.chart.XYMultipleSeriesRenderer;
import com.ab.view.chart.XYSeriesRenderer;
import com.google.gson.Gson;
import com.hc.core.utils.SharedpfTools;
import com.hc.core.utils.Sign;
import com.hc.core.utils.UrlConnector;
import com.hc.xiaobairent.R;
import com.hc.xiaobairent.model.MyIncomeModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @ClassName: MyProfitActivity
 * @Description: 我的佣金
 * @author frank.fun@qq.com
 * @date 2016年5月7日 上午11:15:51
 *
 */
public class MyIncomeActivity extends Activity implements OnClickListener {
	protected static final String TAG = "MyIncomeActivity";
	private ImageView back;
	private TextView title;
	private TextView total;
	private TextView month;
	private LinearLayout income_detail_ll;
	private SharedpfTools sp;
	private AbHttpUtil http;
	private Sign sign;
	private Context context = this;
	private LinearLayout chart_ll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_income_activity);
		back = (ImageView) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		chart_ll = (LinearLayout) findViewById(R.id.chart_ll);
		income_detail_ll = (LinearLayout) findViewById(R.id.income_detail_ll);
		total = (TextView) findViewById(R.id.total);
		month = (TextView) findViewById(R.id.month);
		back.setOnClickListener(this);
		income_detail_ll.setOnClickListener(this);
		title.setText("我的佣金");
		sp = SharedpfTools.getInstance(context);
		http = AbHttpUtil.getInstance(context);
		sign = new Sign(context);
		getData();
	}

	private void getData() {
		sign.init();
		String s = sp.getAccessToken() + UrlConnector.SIGN + sign.getSign();
		http.get(UrlConnector.INCOME_ALL + s, new AbStringHttpResponseListener() {

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
				MyIncomeModel myIncomeModel = new Gson().fromJson(content, MyIncomeModel.class);
				total.setText(myIncomeModel.getCount() + "");
				month.setText(myIncomeModel.getCount() + "");
				if (myIncomeModel.getEach_month() != null && myIncomeModel.getEach_month().length > 0) {
					loadLineGraph(myIncomeModel.getEach_month());
				}
			}

		});
	}

	private void loadLineGraph(float[] values) {
		// 说明文字
		// 数据
		// 每个数据点的简要 说明
		// 创建渲染器
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		// 创建SimpleSeriesRenderer单一渲染器
		XYSeriesRenderer r = new XYSeriesRenderer();
		// 设置渲染器颜色
		r.setColor(Color.WHITE);
		r.setFillPoints(true);
		r.setPointStyle(PointStyle.CIRCLE);
		r.setLineWidth(2);
		r.setChartValuesTextSize(16);
		// 加入到集合中
		renderer.addSeriesRenderer(r);
		// 坐标轴标题文字大小
		renderer.setAxisTitleTextSize(16);
		// 图形标题文字大小
		renderer.setChartTitleTextSize(25);
		// 轴线上标签文字大小
		renderer.setLabelsTextSize(20);
		// 说明文字大小
		renderer.setLegendTextSize(20);
		// 图表标题
		renderer.setChartTitle("佣金");
		// X轴标题
		renderer.setXTitle("月份/月");
		// Y轴标题
		renderer.setYTitle("佣金/元");
		// X轴最小坐标点
		renderer.setXAxisMin(0);
		// X轴最大坐标点
		renderer.setXAxisMax(12);
		float max = values[0];
		float min = values[0];
		for (float f : values) {
			if (max < f) {
				max = f;
			}
			if (min > f) {
				min = f;
			}
		}
		if (min == 0 && min == max) {
			// Y轴最小坐标点
			renderer.setYAxisMin(-10);
			// Y轴最大坐标点
			renderer.setYAxisMax(+10);

		} else if (min == max) {
			// Y轴最小坐标点
			renderer.setYAxisMin(min - min / 10);
			// Y轴最大坐标点
			renderer.setYAxisMax(min + min / 10);
		} else {
			// Y轴最小坐标点
			renderer.setYAxisMin(min - (max - min) / 10);
			// Y轴最大坐标点
			renderer.setYAxisMax(max + (max - min) / 10);
		}
		// 坐标轴颜色
		renderer.setAxesColor(Color.WHITE);
		renderer.setXLabelsColor(Color.WHITE);
		renderer.setYLabelsColor(0, Color.WHITE);
		// 设置图表上标题与X轴与Y轴的说明文字颜色
		renderer.setLabelsColor(Color.WHITE);
		// 设置字体加粗
		renderer.setTextTypeface("sans_serif", Typeface.BOLD);
		// 设置在图表上是否显示值标签
		renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
		// 显示屏幕可见取区的XY分割数
		renderer.setXLabels(7);
		renderer.setYLabels(10);
		// X刻度标签相对X轴位置
		renderer.setXLabelsAlign(Align.RIGHT);
		// Y刻度标签相对Y轴位置
		renderer.setYLabelsAlign(Align.LEFT);
		renderer.setPanEnabled(false, true);
		renderer.setZoomEnabled(true);
		renderer.setZoomRate(1.1f);
		renderer.setBarSpacing(0f);
		// 标尺开启
		renderer.setScaleLineEnabled(true);
		// 设置标尺提示框高
		renderer.setScaleRectHeight(10);
		// 设置标尺提示框宽
		renderer.setScaleRectWidth(150);
		// 设置标尺提示框背景色
		renderer.setScaleRectColor(Color.argb(150, 52, 182, 232));
		renderer.setScaleLineColor(Color.argb(175, 150, 150, 150));
		renderer.setScaleCircleRadius(6);
		// 第一行文字的大小
		renderer.setExplainTextSize1(20);
		// 第二行文字的大小
		renderer.setExplainTextSize2(20);

		// 显示表格线
		renderer.setShowGrid(true);
		// 如果值是0是否要显示
		renderer.setDisplayValue0(true);
		// 创建渲染器数据填充器
		XYMultipleSeriesDataset mXYMultipleSeriesDataset = new XYMultipleSeriesDataset();
		CategorySeries series = new CategorySeries("");
		int seriesLength = values.length;
		for (int k = 0; k < seriesLength; k++) {
			// 设置每个点的颜色
			series.add(values[k], Color.WHITE, k + 1 + "月份");
		}
		mXYMultipleSeriesDataset.addSeries(series.toXYSeries());
		// 背景
		renderer.setApplyBackgroundColor(true);
		renderer.setBackgroundColor(Color.rgb(247, 96, 111));
		renderer.setMarginsColor(Color.rgb(247, 96, 111));
		// 线图
		final GraphicalView chart = ChartFactory.getLineChartView(this, mXYMultipleSeriesDataset, renderer);
		chart_ll.addView(chart);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			onBackPressed();
			break;
		case R.id.income_detail_ll:
			startActivity(new Intent(this, IncomeDetailActivity.class));
			break;
		default:
			break;
		}
	}

}
