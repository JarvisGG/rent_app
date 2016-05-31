package com.hc.core.vote;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.ui.BindView;

import com.hc.core.base.BaseActivity;
import com.hc.core.view.CountDownTextView;
import com.hc.core.view.NoScrollListView;
import com.hc.xiaobairent.R;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class VoteDetails extends BaseActivity {
	@BindView(id = R.id.noscrolllistview)
	private NoScrollListView noscrolllistview;
	@BindView(id = R.id.title)
	private TextView title;
	@BindView(id = R.id.submit, click = true)
	private Button submit;
	@BindView(id = R.id.time)
	private CountDownTextView time;

	/** 顶部标题 */
	@BindView(id = R.id.tv_topbar)
	private TextView tvTopbar;
	@BindView(id = R.id.iv_back, click = true)
	private ImageView ivBack;
	private List<VoteOptionModel> list;

	@Override
	public void setRootView() {
		setContentView(R.layout.hc_activity_vote_details);
	}

	@Override
	public void initData() {
		super.initData();
		list = new ArrayList<VoteOptionModel>();
		VoteOptionModel model = new VoteOptionModel();
		model.setTitle("1111");
		list.add(model);
		VoteOptionModel model1 = new VoteOptionModel();
		model1.setTitle("2222");
		list.add(model1);
		final ChoiceAdapter adapter = new ChoiceAdapter(this, list);
		noscrolllistview.setAdapter(adapter);
		noscrolllistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				for (int i = 0; i < list.size(); i++) {
					list.get(i).setSelected(false);
				}
				list.get(position).setSelected(true);
				adapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.iv_back:
			onBackPressed();
			break;
		}
	}

	@Override
	public void initWidget() {
		super.initWidget();
		tvTopbar.setText("投票");
		ivBack.setVisibility(View.VISIBLE);
		tvTopbar.setVisibility(View.VISIBLE);
	}
}
