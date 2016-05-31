package com.hc.xiaobairent.fragment;

import org.ksoap2.SoapEnvelope;
import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.ui.AnnotateUtil;
import org.kymjs.kjframe.ui.BindView;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbStringHttpResponseListener;
import com.google.gson.Gson;
import com.hc.core.utils.RentConstants;
import com.hc.core.utils.SharedpfTools;
import com.hc.core.utils.Sign;
import com.hc.core.utils.UrlConnector;
import com.hc.core.view.CircularImage;
import com.hc.xiaobairent.R;
import com.hc.xiaobairent.activity.CertifyActivity;
import com.hc.xiaobairent.activity.FootprintsActivity;
import com.hc.xiaobairent.activity.MyIncomeActivity;
import com.hc.xiaobairent.activity.PostHouseActivity;
import com.hc.xiaobairent.activity.SettingsActivity;
import com.hc.xiaobairent.activity.TenantInformationActivity;
import com.hc.xiaobairent.activity.ZfChangePsdActivity;
import com.hc.xiaobairent.activity.ZfLoginActivity;
import com.hc.xiaobairent.activity.ZfMyAgentActivity;
import com.hc.xiaobairent.activity.ZfMyCollectActivity;
import com.hc.xiaobairent.activity.ZfMyEntrustActivity;
import com.hc.xiaobairent.activity.ZfMyMessageActivity;
import com.hc.xiaobairent.activity.ZfMyNumActivity;
import com.hc.xiaobairent.activity.ZfMyRentalActivity;
import com.hc.xiaobairent.activity.ZfMyRepayActivity;
import com.hc.xiaobairent.activity.ZfMyRoomActivity;
import com.hc.xiaobairent.activity.ZfPersonInfoActivity;
import com.hc.xiaobairent.model.PersonSmallInfoModel;

/**
 * 
 * @author xiaofei
 *
 */
public class MineFragment extends Fragment implements OnClickListener {

	@BindView(id = R.id.iv_center_head, click = true)
	private CircularImage centerHeadImage;
	@BindView(id = R.id.btn_sign, click = true)
	private Button btnSign;
	@BindView(id = R.id.ll_mcenter_info, click = true)
	private LinearLayout mcenterInfo;
	@BindView(id = R.id.ll_mcenter_certified, click = true)
	private LinearLayout mcenterCertified;
	@BindView(id = R.id.tv_mcenter_certified_status)
	private TextView mcenterCertifyStatus;
	@BindView(id = R.id.ll_mcenter_entrust, click = true)
	private LinearLayout mcenterEntrust;
	@BindView(id = R.id.ll_mcenter_rental, click = true)
	private LinearLayout mcenterRental;
	@BindView(id = R.id.ll_mcenter_repay, click = true)
	private LinearLayout mcenterRepay;
	@BindView(id = R.id.ll_mcenter_collect, click = true)
	private LinearLayout mcenterCollect;
	@BindView(id = R.id.ll_mcenter_footprints, click = true)
	private LinearLayout ll_mcenter_footprints;
	@BindView(id = R.id.ll_mcenter_message, click = true)
	private LinearLayout mcenterMessage;
	@BindView(id = R.id.ll_mcenter_changepsd, click = true)
	private LinearLayout mcenterChangePsd;
	@BindView(id = R.id.ll_mcenter_setting, click = true)
	private ImageView mcenterSetting;
	@BindView(id = R.id.ll_mcenter_agent, click = true)
	private LinearLayout mcenterAgent;

	@BindView(id = R.id.ll_mcenter_num, click = true)
	private LinearLayout mcenterNum;
	@BindView(id = R.id.ll_mcenter_commission, click = true)
	private LinearLayout mcenterCommission;
	@BindView(id = R.id.ll_mcenter_tenant, click = true)
	private LinearLayout mcenterTenant;
	@BindView(id = R.id.tv_sign_username)
	private TextView signUsername;

	@BindView(id = R.id.ll_mcenter_room, click = true)
	private LinearLayout mcenterRoom;
	@BindView(id = R.id.ll_mcenter_myroom, click = true)
	private LinearLayout mcenterMyroom;

//	@BindView(id = R.id.ll_mcenter_certify, click = true)
//	private LinearLayout mcenterCertify;
	@BindView(id = R.id.ll_mcenter_room, click = true)
	private LinearLayout ll_mcenter_room;

	private SharedpfTools sp;
	private AbHttpUtil httpUtil;
	private Sign sign;
	private Gson gson;
	private KJBitmap bitmap;
	private int LoginMethod = 0;
	private int verify = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.zf_fragment_mine, container, false);
		AnnotateUtil.initBindView(this, view);
		sp = SharedpfTools.getInstance(getActivity());
		if (sp.getLogStatus() == false) {
			showItem(RentConstants.USER);
			centerHeadImage.setImageResource(R.drawable.portrait_2);
			signUsername.setVisibility(View.GONE);
			btnSign.setVisibility(View.VISIBLE);
		} else {
			showItem(sp.getUserType());
			applyData();
		}
		return view;
	}

	// 请求数据
	public void applyData() {
		sign = new Sign(getActivity());
		sign.init();
		gson = new Gson();
		bitmap = new KJBitmap();
		httpUtil = AbHttpUtil.getInstance(getActivity());
		if (sp.getLogStatus()) {
			String url = UrlConnector.CERTIFY_TEST + sp.getAccessToken() + UrlConnector.SIGN + sign.getSign();

			httpUtil.post(url, null, new AbStringHttpResponseListener() {

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
					Log.e("return", content);
//					PersonInformationModel personInformationModel = gson.fromJson(content,
//							PersonInformationModel.class);
//					bitmap.display(centerHeadImage, personInformationModel.getAvatar());
//					signUsername.setVisibility(View.VISIBLE);
//					signUsername.setText(personInformationModel.getT_nickname());
//					btnSign.setVisibility(View.GONE);
					PersonSmallInfoModel personinfo = gson.fromJson(content, PersonSmallInfoModel.class);
					bitmap.display(centerHeadImage, personinfo.getAvatar());
					signUsername.setVisibility(View.VISIBLE);
					signUsername.setText(personinfo.getT_nickname());
					btnSign.setVisibility(View.GONE);
					verify = personinfo.getVerify();
					if(sp.getUserType() == RentConstants.USER) {
						if(verify == 0) {
							mcenterCertifyStatus.setText("未认证");
						} else {
							mcenterCertifyStatus.setText("已认证");
						}
					}
				}
			});
		} else {
			centerHeadImage.setImageResource(R.drawable.portrait_2);
			signUsername.setVisibility(View.GONE);
			btnSign.setVisibility(View.VISIBLE);
		}
	}

	// 根据权限渲染界面
	private void showItem(int type) {
		switch (type) {

		case RentConstants.USER:
			mcenterCertified.setVisibility(View.VISIBLE);
			mcenterEntrust.setVisibility(View.VISIBLE);
			mcenterRental.setVisibility(View.VISIBLE);
			mcenterRepay.setVisibility(View.VISIBLE);
			mcenterCollect.setVisibility(View.VISIBLE);
			ll_mcenter_footprints.setVisibility(View.VISIBLE);
			mcenterMessage.setVisibility(View.VISIBLE);
			mcenterAgent.setVisibility(View.VISIBLE);

			mcenterNum.setVisibility(View.GONE);
			mcenterCommission.setVisibility(View.GONE);
			mcenterTenant.setVisibility(View.GONE);

			mcenterRoom.setVisibility(View.GONE);
			mcenterMyroom.setVisibility(View.GONE);
			break;

		case RentConstants.OWNER:
			mcenterCertified.setVisibility(View.GONE);
			mcenterEntrust.setVisibility(View.GONE);
			mcenterRental.setVisibility(View.GONE);
			mcenterRepay.setVisibility(View.GONE);
			mcenterCollect.setVisibility(View.GONE);
			ll_mcenter_footprints.setVisibility(View.GONE);
			mcenterMessage.setVisibility(View.GONE);
			mcenterAgent.setVisibility(View.GONE);

			mcenterNum.setVisibility(View.GONE);
			mcenterCommission.setVisibility(View.GONE);
			mcenterTenant.setVisibility(View.GONE);

			mcenterRoom.setVisibility(View.VISIBLE);
			mcenterMyroom.setVisibility(View.VISIBLE);
			break;

		case RentConstants.BROKER:

			mcenterCertified.setVisibility(View.GONE);
			mcenterEntrust.setVisibility(View.GONE);
			mcenterRental.setVisibility(View.GONE);
			mcenterRepay.setVisibility(View.GONE);
			mcenterCollect.setVisibility(View.GONE);
			ll_mcenter_footprints.setVisibility(View.GONE);
			mcenterMessage.setVisibility(View.VISIBLE);
			mcenterAgent.setVisibility(View.GONE);

			mcenterNum.setVisibility(View.VISIBLE);
			mcenterCommission.setVisibility(View.VISIBLE);
			mcenterTenant.setVisibility(View.VISIBLE);

			mcenterRoom.setVisibility(View.GONE);
			mcenterMyroom.setVisibility(View.GONE);
			break;

		default:
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		
		if (sp.getLogStatus() == false) {
			showItem(RentConstants.USER);
			centerHeadImage.setImageResource(R.drawable.portrait_2);
			signUsername.setVisibility(View.GONE);
			btnSign.setVisibility(View.VISIBLE);
			// startActivity(new Intent(getActivity(), ZfLoginActivity.class));
			// getActivity().overridePendingTransition(R.anim.cu_push_right_in,
			// R.anim.cu_push_left_out);
		} else {
			showItem(sp.getUserType());
			applyData();
			LoginMethod = Integer.parseInt(sp.getLoginMethod());
			// 1-三方登录   2-正常登陆
			if (LoginMethod == 1) {
				mcenterChangePsd.setVisibility(View.GONE);
			} else {
				mcenterChangePsd.setVisibility(View.VISIBLE);
			}
//			Bundle bundle = getActivity().getIntent().getExtras();
//			if(bundle.containsKey("LoginMethod")) {
//				LoginMethod = bundle.getInt("LoginMethod");
//				// 1-三方登录   2-正常登陆
//				if (LoginMethod == 1) {
//					mcenterChangePsd.setVisibility(View.GONE);
//				} else {
//					mcenterChangePsd.setVisibility(View.VISIBLE);
//				}
//			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_mcenter_collect:
			if (sp.getLogStatus()) {
				startActivity(new Intent(getActivity(), ZfMyCollectActivity.class));
				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			} else {
				startActivity(new Intent(getActivity(), ZfLoginActivity.class));
				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			}
			break;
		case R.id.ll_mcenter_setting:
			if (sp.getLogStatus()) {
				startActivity(new Intent(getActivity(), SettingsActivity.class));
				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			} else {
				startActivity(new Intent(getActivity(), ZfLoginActivity.class));
				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			}
			break;

		case R.id.ll_mcenter_info:
			if (sp.getLogStatus()) {
				startActivity(new Intent(getActivity(), ZfPersonInfoActivity.class));
				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			} else {
				startActivity(new Intent(getActivity(), ZfLoginActivity.class));
				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			}

			break;
		case R.id.ll_mcenter_certified:
			if (sp.getLogStatus()) {
				startActivity(new Intent(getActivity(), CertifyActivity.class));
				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			} else {
				startActivity(new Intent(getActivity(), ZfLoginActivity.class));
				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			}

			break;
		case R.id.ll_mcenter_footprints:
			if (sp.getLogStatus()) {
				startActivity(new Intent(getActivity(), FootprintsActivity.class));
				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			} else {
				startActivity(new Intent(getActivity(), ZfLoginActivity.class));
				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			}

			break;

		case R.id.ll_mcenter_message:
			if (sp.getLogStatus()) {
				startActivity(new Intent(getActivity(), ZfMyMessageActivity.class));
				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			} else {
				startActivity(new Intent(getActivity(), ZfLoginActivity.class));
				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			}

			break;

		case R.id.ll_mcenter_changepsd:
			if (sp.getLogStatus()) {
				startActivity(new Intent(getActivity(), ZfChangePsdActivity.class));
				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			} else {
				startActivity(new Intent(getActivity(), ZfLoginActivity.class));
				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			}

			break;

		case R.id.ll_mcenter_entrust:
			if (sp.getLogStatus()) {
				startActivity(new Intent(getActivity(), ZfMyEntrustActivity.class));
				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			} else {
				startActivity(new Intent(getActivity(), ZfLoginActivity.class));
				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			}

			break;

		case R.id.ll_mcenter_rental:
			if (sp.getLogStatus()) {
				startActivity(new Intent(getActivity(), ZfMyRentalActivity.class));
				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			} else {
				startActivity(new Intent(getActivity(), ZfLoginActivity.class));
				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			}

			break;

		case R.id.btn_sign:
			startActivity(new Intent(getActivity(), ZfLoginActivity.class));
			getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			break;

		case R.id.ll_mcenter_repay:
			if (sp.getLogStatus()) {
				startActivity(new Intent(getActivity(), ZfMyRepayActivity.class));
				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			} else {
				startActivity(new Intent(getActivity(), ZfLoginActivity.class));
				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			}

			break;

		case R.id.ll_mcenter_num:
			if (sp.getLogStatus()) {
				startActivity(new Intent(getActivity(), ZfMyNumActivity.class));
				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			} else {
				startActivity(new Intent(getActivity(), ZfLoginActivity.class));
				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			}

			break;
		case R.id.ll_mcenter_commission:
			if (sp.getLogStatus()) {
				startActivity(new Intent(getActivity(), MyIncomeActivity.class));
				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			} else {
				startActivity(new Intent(getActivity(), ZfLoginActivity.class));
				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			}

			break;
		case R.id.ll_mcenter_tenant:
			if (sp.getLogStatus()) {
				startActivity(new Intent(getActivity(), TenantInformationActivity.class));
				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			} else {
				startActivity(new Intent(getActivity(), ZfLoginActivity.class));
				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			}

			break;

		case R.id.ll_mcenter_agent:
			if (sp.getLogStatus()) {
				startActivity(new Intent(getActivity(), ZfMyAgentActivity.class));
				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			} else {
				startActivity(new Intent(getActivity(), ZfLoginActivity.class));
				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			}
			break;

		case R.id.ll_mcenter_myroom:
			if (sp.getLogStatus()) {
				startActivity(new Intent(getActivity(), ZfMyRoomActivity.class));
				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			} else {
				startActivity(new Intent(getActivity(), ZfLoginActivity.class));
				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			}
			break;

//		case R.id.ll_mcenter_certify:
//			if (sp.getLogStatus()) {
//				startActivity(new Intent(getActivity(), ZfCertifiedActivity.class));
//				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
//			} else {
//				startActivity(new Intent(getActivity(), ZfLoginActivity.class));
//				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
//			}
//			break;
		case R.id.ll_mcenter_room:
			if (sp.getLogStatus()) {
				startActivity(new Intent(getActivity(), PostHouseActivity.class));
				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			} else {
				startActivity(new Intent(getActivity(), ZfLoginActivity.class));
				getActivity().overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			}
			break;
		default:
			break;
		}
	}

}
