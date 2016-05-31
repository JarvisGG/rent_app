package com.hc.xiaobairent.activity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.BindView;

import android.R.integer;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.google.gson.Gson;
import com.hc.core.base.BaseActivity;
import com.hc.core.utils.IconSelectDialog;
import com.hc.core.utils.SharedpfTools;
import com.hc.core.utils.Sign;
import com.hc.core.utils.UrlConnector;
import com.hc.core.view.CircularImage;
import com.hc.xiaobairent.R;
import com.hc.xiaobairent.constants.ZfContants;
import com.hc.xiaobairent.dialog.ListViewDialog;
import com.hc.xiaobairent.dialog.SexSelectDialog;
import com.hc.xiaobairent.model.PersonInformationModel;

public class ZfPersonInfoActivity extends BaseActivity {

	private File sdcardTempFile = new File(ZfContants.FILEPATH,
			"tmp_pic_" + SystemClock.currentThreadTimeMillis() + ".jpg");
	private File tempFile = new File(ZfContants.FILEPATH, System.currentTimeMillis() + ".jpg");
	// 参数
	private static final int TAKE_PICTURE = 10;
	private static final int SELECT_PICTURE = 11;
	private static final int TAKE_SELECT_PICTURE = 12;

	// 初始化标题
	@BindView(id = R.id.menu_back, click = true)
	private ImageView menuBack;
	@BindView(id = R.id.menu_title)
	private TextView menuTitle;

	// 个人信息item
	@BindView(id = R.id.ll_head_img_personalinfo, click = true)
	private LinearLayout head_img_personlinfoLayout;
	@BindView(id = R.id.head_img_personalinfo)
	private CircularImage head_CircularImage;
	@BindView(id = R.id.user_name_ll_personalinfo, click = true)
	private LinearLayout user_name_ll_personalinfo;
	@BindView(id = R.id.user_name_txt_personalinfo)
	private TextView user_name_txt_personalinfo;
	@BindView(id = R.id.sex_ll_personalinfo, click = true)
	private LinearLayout sex_text_personlinfoLayout;
	@BindView(id = R.id.sex_txt_personalinfo)
	private TextView sex_txt_prosonalinfo;
	@BindView(id = R.id.phone_ll_personalinfo, click = true)
	private LinearLayout phone_ll_personalinfo;
	@BindView(id = R.id.phone_txt_personalinfo)
	private TextView phone_txt_personalinfo;
	@BindView(id = R.id.weixin_ll_personalinfo, click = true)
	private LinearLayout weixin_ll_personalinfo;
	@BindView(id = R.id.weixin_txt_personalinfo)
	private TextView weixin_txt_personalinfo;
	@BindView(id = R.id.qq_ll_personalinfo, click = true)
	private LinearLayout qq_ll_personalinfo;
	@BindView(id = R.id.qq_txt_personalinfo)
	private TextView qq_txt_personalinfo;
	@BindView(id = R.id.next_btu, click = true)
	private Button submitBtn;

	private SharedpfTools sharedpfTools;
	private Sign sign;
	private AbHttpUtil httpUtil;
	private Gson gson;
	private KJBitmap bitmap;

	private int sexType;

	@Override
	public void setRootView() {
		setAbContentView(R.layout.zf_activity_personinfo);
	}

	@Override
	public void initData() {
		super.initData();

		sharedpfTools = SharedpfTools.getInstance(this);
		sign = new Sign(ZfPersonInfoActivity.this);
		sign.init();
		gson = new Gson();
		httpUtil = AbHttpUtil.getInstance(this);
		bitmap = new KJBitmap();

		applyData();
		initTab();

		// 初始化文件目录
		File dirPathFile = new File(ZfContants.FILEPATH);
		if (!dirPathFile.isDirectory()) {
			dirPathFile.mkdirs();
		}
		if (!sdcardTempFile.exists()) {
			try {
				sdcardTempFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 请求数据
	private void applyData() {
		String url = UrlConnector.PERSONALINFO + sharedpfTools.getAccessToken() + UrlConnector.SIGN + sign.getSign();
		httpUtil.post(url, null, new AbStringHttpResponseListener() {

			@Override
			public void onStart() {

			}

			@Override
			public void onFinish() {

			}

			@Override
			public void onFailure(int statusCode, String content, Throwable error) {
				Toast.makeText(ZfPersonInfoActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				PersonInformationModel personInformationModel = gson.fromJson(content, PersonInformationModel.class);
				bitmap.display(head_CircularImage, personInformationModel.getAvatar());
				user_name_txt_personalinfo.setText(personInformationModel.getT_nickname());
				
				if (!TextUtils.isEmpty(personInformationModel.getGender())) {
					sexType = Integer.parseInt(personInformationModel.getGender());
					if (sexType == 0) {
						sex_txt_prosonalinfo.setText("男");
					} else {
						sex_txt_prosonalinfo.setText("女");
					}
				} else {
					sex_txt_prosonalinfo.setText("请选择");
				}

				phone_txt_personalinfo.setText(personInformationModel.getT_mobile());
				weixin_txt_personalinfo.setText(personInformationModel.getWeixin());
				qq_txt_personalinfo.setText(personInformationModel.getQq());

			}
		});
	}

	// 上传数据
	private void sendData() {
		sign.clear();
		sign.init();
		AbRequestParams params = new AbRequestParams();
		if (!TextUtils.isEmpty(user_name_txt_personalinfo.getText().toString().trim())) {
			sign.addParam("t_nickname", user_name_txt_personalinfo.getText().toString().trim());
			params.put("t_nickname", user_name_txt_personalinfo.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(phone_txt_personalinfo.getText().toString().trim())) {
			sign.addParam("t_mobile", phone_txt_personalinfo.getText().toString().trim());
			params.put("t_mobile", phone_txt_personalinfo.getText().toString().trim());
		}

		if (!TextUtils.isEmpty(qq_txt_personalinfo.getText().toString().trim())) {
			sign.addParam("qq", qq_txt_personalinfo.getText().toString().trim());
			params.put("qq", qq_txt_personalinfo.getText().toString().trim());
		}
		if (!TextUtils.isEmpty(weixin_txt_personalinfo.getText().toString().trim())) {
			sign.addParam("weixin", weixin_txt_personalinfo.getText().toString().trim());
			params.put("weixin", weixin_txt_personalinfo.getText().toString().trim());
		}

		sign.addParam("gender", sexType);
		params.put("gender", sexType);

		String signCodeString = sign.getSign();
		String url = UrlConnector.PERSONALINFO_EDIT + sharedpfTools.getAccessToken() + UrlConnector.SIGN
				+ signCodeString;
		httpUtil.post(url, params, new AbStringHttpResponseListener() {

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
				Log.e("updatePersonInfo", content);
				Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
				// try {
				// JSONObject object = new JSONObject(content);
				// Toast.makeText(getApplicationContext(),
				// object.getString("msg"), Toast.LENGTH_SHORT).show();
				// } catch (JSONException e) {
				// e.printStackTrace();
				// }

			}
		});

	}

	private void sendPhoto() {
		if (sdcardTempFile.exists()) {
			AbRequestParams params = new AbRequestParams();
			params.put("0", sdcardTempFile);
			String signCodeString = sign.getSign();
			String url = UrlConnector.PERSONALAVATOR_EDIT + sharedpfTools.getAccessToken() + UrlConnector.SIGN
					+ signCodeString;
			httpUtil.post(url, params, new AbStringHttpResponseListener() {

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
					Log.e("updatePersonInfo", content);
					try {
						JSONObject object = new JSONObject(content);
						// Toast.makeText(getApplicationContext(),
						// object.getString("msg"), Toast.LENGTH_SHORT).show();
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			});
		}
	}

	private void initTab() {
		menuTitle.setText("个人信息");
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.menu_back:
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;
		case R.id.ll_head_img_personalinfo:
			showSelectIcon();
			break;
		case R.id.sex_ll_personalinfo:
			showSelectSex();
			break;
		case R.id.user_name_ll_personalinfo:
			startActivityForResult(new Intent(ZfPersonInfoActivity.this, ZfChangeInfoActivity.class)
					.putExtra("from", ZfContants.EDIT_NAME)
					.putExtra("content", user_name_txt_personalinfo.getText().toString()), ZfContants.EDIT_NAME);
			overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			break;
		case R.id.phone_ll_personalinfo:
			if (!TextUtils.isEmpty(phone_txt_personalinfo.getText().toString().trim())) {
				startActivityForResult(new Intent(ZfPersonInfoActivity.this, ZfChangeInfoActivity.class)
						.putExtra("from", ZfContants.EDIT_MOBILE)
						.putExtra("content", phone_txt_personalinfo.getText().toString()), ZfContants.EDIT_MOBILE);
				overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			}
			break;
		case R.id.weixin_ll_personalinfo:
				startActivityForResult(new Intent(ZfPersonInfoActivity.this, ZfChangeInfoActivity.class)
						.putExtra("from", ZfContants.EDIT_WEIXIN)
						.putExtra("content", weixin_txt_personalinfo.getText().toString()), ZfContants.EDIT_WEIXIN);
				overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			break;
		case R.id.qq_ll_personalinfo:
				startActivityForResult(new Intent(ZfPersonInfoActivity.this, ZfChangeInfoActivity.class)
						.putExtra("from", ZfContants.EDIT_QQ)
						.putExtra("content", qq_txt_personalinfo.getText().toString()), ZfContants.EDIT_QQ);
				overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			break;
		case R.id.next_btu:
			sendData();
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;
		default:
			break;
		}
	}

	private void showSelectSex() {
		final SexSelectDialog dialog = new SexSelectDialog(ZfPersonInfoActivity.this);
		dialog.show();
		TextView textView = (TextView) dialog.findViewById(R.id.title);
		textView.setText("请选择性别");
		Button selectMaleButton = (Button) dialog.findViewById(R.id.male);
		selectMaleButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sex_txt_prosonalinfo.setText("男");
				sexType = 0;
				dialog.cancel();
			}
		});

		Button selectFemaleButton = (Button) dialog.findViewById(R.id.female);
		selectFemaleButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sex_txt_prosonalinfo.setText("女");
				sexType = 1;
				dialog.cancel();
			}
		});
	}

	// 选择头像
	private void showSelectIcon() {
		final IconSelectDialog dialog = new IconSelectDialog(ZfPersonInfoActivity.this);
		dialog.show();

		Button take = (Button) dialog.findViewById(R.id.btn_photo_take);
		take.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 选择拍照
				Intent cameraintentIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// 制定相机存储位置
				cameraintentIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
				startActivityForResult(cameraintentIntent, TAKE_PICTURE);
				dialog.cancel();
			}
		});

		Button select = (Button) dialog.findViewById(R.id.btn_photo_select);
		select.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startPhotoZoom(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "android.intent.action.PICK",
						SELECT_PICTURE);
				dialog.cancel();
			}
		});

		Button cancel = (Button) dialog.findViewById(R.id.btn_cancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			switch (requestCode) {
			case SELECT_PICTURE:
				Bitmap bmp = BitmapFactory.decodeFile(sdcardTempFile.getAbsolutePath());
				head_CircularImage.setImageBitmap(bmp);
				sendPhoto();
				break;

			case TAKE_PICTURE:
				startPhotoZoom(Uri.fromFile(tempFile), "com.android.camera.action.CROP", TAKE_SELECT_PICTURE);
				sendPhoto();
				break;

			case TAKE_SELECT_PICTURE:
				if (data != null) {
					sentPicToNext(data);
				}
				sendPhoto();
				break;

			case ZfContants.EDIT_NAME:
				user_name_txt_personalinfo.setText(data.getStringExtra("info"));
				break;

			case ZfContants.EDIT_MOBILE:
				phone_txt_personalinfo.setText(data.getStringExtra("info"));
				break;

			case ZfContants.EDIT_WEIXIN:
				weixin_txt_personalinfo.setText(data.getStringExtra("info"));
				break;

			case ZfContants.EDIT_QQ:
				qq_txt_personalinfo.setText(data.getStringExtra("info"));
				break;

			default:
				break;
			}
			break;

		default:
			break;
		}
	}

	// 裁剪正方形图片
	private void startPhotoZoom(Uri uri, String method, int type) {
		Intent intent = new Intent(method);
		intent.setDataAndType(uri, "image/*");

		intent.putExtra("crop", "true");
		// 裁剪框比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 裁剪输出尺寸
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);

		switch (type) {
		case SELECT_PICTURE:
			intent.putExtra("output", Uri.fromFile(sdcardTempFile));
			break;

		case TAKE_SELECT_PICTURE:
			intent.putExtra("return-data", true);
			intent.putExtra("noFaceDetection", true);
		default:
			break;
		}
		startActivityForResult(intent, type);
	}

	// 将进行剪裁后的图片传递到下一个界面上
	private void sentPicToNext(Intent picData) {
		Bundle bundle = picData.getExtras();
		if (bundle != null) {
			Bitmap photo = bundle.getParcelable("data");
			if (photo != null) {
				head_CircularImage.setImageBitmap(photo);
			}
			// 写入缓存文件
			ByteArrayOutputStream baos = null;
			BufferedOutputStream bos = null;
			FileOutputStream fos = null;

			baos = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] photodata = baos.toByteArray();
			try {
				fos = new FileOutputStream(sdcardTempFile);
				bos = new BufferedOutputStream(fos);
				bos.write(photodata);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (baos != null) {
					try {
						baos.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (bos != null) {
					try {
						bos.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}

		}
	}

//	 @Override
//	 protected void onResume() {
//	 super.onResume();
//	 applyData();
//	 }

}
