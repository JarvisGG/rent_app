package com.hc.core.vote;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.ui.BindView;

import com.hc.core.base.BaseActivity;
import com.hc.core.vote.VoteListModel.VoteListItemModel;
import com.hc.xiaobairent.R;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class VoteList extends BaseActivity {
	@BindView(id = R.id.listview)
	private ListView listview;
	/** 顶部标题 */
	@BindView(id = R.id.tv_topbar)
	private TextView tvTopbar;
	@BindView(id = R.id.iv_back, click = true)
	private ImageView ivBack;

	@Override
	public void setRootView() {
		setContentView(R.layout.hc_activity_vote_list);
	}

	@Override
	public void initData() {
		super.initData();
		List<VoteListItemModel> list = new ArrayList<VoteListModel.VoteListItemModel>();
		VoteListItemModel model = new VoteListModel().new VoteListItemModel();
		model.setTitle("山东高速帝国时代根深蒂固");
		model.setEnd_time("4234464645772342");
		list.add(model);
		VoteListAdapter adapter = new VoteListAdapter(this, list);// 投票列表adapter
		listview.setAdapter(adapter);
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
