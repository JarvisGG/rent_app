// package com.hc.core.utils;
//
// import com.ab.http.AbHttpUtil;
// import com.ab.http.AbStringHttpResponseListener;
// import com.ab.image.AbImageLoader;
// import com.google.gson.Gson;
// import com.hc.core.anji.model.UserInfoModel;
//
// import android.content.Context;
// import android.util.Log;
// import android.widget.ImageView;
// import android.widget.TextView;
//
// public class GetPersonalInformation {
// private Sign sign;
// private SharedpfTools sharedpfTools;
// private AbHttpUtil http;
// private AbImageLoader bitmap;
//
//
// public void getInstants(Context context) {
// sign = new Sign(context);
// sharedpfTools = SharedpfTools.getInstance(context);
// http = AbHttpUtil.getInstance(context);
// bitmap = AbImageLoader.getInstance(context);
// }
//
// public void loadUserInfo() {
// sign.init();
// http.get(UrlConnector.PERSONAL_INFORMATION + sharedpfTools.getAccessToken() +
// UrlConnector.TWO + sign.getSign(),
// new AbStringHttpResponseListener() {
//
// @Override
// public void onStart() {
// }
//
// @Override
// public void onFinish() {
// }
//
// @Override
// public void onFailure(int arg0, String arg1, Throwable arg2) {
// }
//
// @Override
// public void onSuccess(int arg0, String t) {
// UserInfoModel uInfo = new Gson().fromJson(t, UserInfoModel.class);
// email = uInfo.getT_email();
// mobile = uInfo.getT_mobile();
// bitmap.display(headPic, uInfo.getT_photo() + "");
// if (uInfo.getProfile() != null) {
// id_personal_formation.setText(uInfo.getProfile().getUser_sn() + "");
// position_personal_formation.setText(uInfo.getProfile().getPosition() + "");
// }
// realname_personal_formation.setText(uInfo.getT_realname() + "");
// mobile_personal_formation.setText(mobile + "");
// email_personal_formation.setText(email + "");
// department_personal_formation.setText(uInfo.getDepartment_ids() + "");
// Log.e("url", UrlConnector.PERSONAL_INFORMATION +
// sharedpfTools.getAccessToken()
// + UrlConnector.TWO + sign.getSign());
// Log.e("PI数据", t);
// }
// });
// }
// }
