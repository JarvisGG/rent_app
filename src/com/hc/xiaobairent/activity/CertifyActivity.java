package com.hc.xiaobairent.activity;

import java.io.File;
import java.net.URI;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.image.AbImageLoader;
import com.google.gson.Gson;
import com.hc.core.utils.DownloadUtil;
import com.hc.core.utils.RentConstants;
import com.hc.core.utils.Sign;
import com.hc.core.utils.UrlConnector;
import com.hc.xiaobairent.R;
import com.hc.xiaobairent.model.CertifyInfomationModel;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @ClassName: CertifyActivity
 * @Description: 实名认证
 * @author frank.fun@qq.com
 * @date 2016年5月20日 上午11:22:30
 *
 */
public class CertifyActivity extends Activity implements OnClickListener {
	protected static final String TAG = "CertifyActivity";

	private static final int TAKE_PHOTO = 1;
	private static final int CROP_PHOTO = 2;
	private static final int CHOOSE_PHOTO = 3;
	private static final int NAME = 6;
	private static final int ID = 7;
	private static final int FRONT = 4;
	private static final int BACK = 5;
	private PopupWindow popupWindow = null;
	private ScrollView scrollview;
	private ImageView back;
	private TextView title;
	private TextView name_tv;
	private TextView id_tv;
	private ImageView idcard_pic_front_iv;
	private ImageView idcard_pic_back_iv;
	private TextView commit_tv;
	private Uri imageUri;
	private Context context = this;
	private int pic = FRONT;
	private File frontFile;
	private File backFile;
	private AbHttpUtil http;
	private AbImageLoader imageLoader;
	private Sign sign;
	private boolean certified = false;
	private String frontPicPath = "";
	private String backPicPath = "";
	private DownloadUtil downloadUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.certify_activity);
		back = (ImageView) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		scrollview = (ScrollView) findViewById(R.id.scrollview);
		name_tv = (TextView) findViewById(R.id.name_tv);
		id_tv = (TextView) findViewById(R.id.id_tv);
		idcard_pic_front_iv = (ImageView) findViewById(R.id.idcard_pic_front_iv);
		idcard_pic_back_iv = (ImageView) findViewById(R.id.idcard_pic_back_iv);
		commit_tv = (TextView) findViewById(R.id.commit_tv);
		title.setText("实名认证");
		http = AbHttpUtil.getInstance(context);
		sign = Sign.getInstance(context);
		imageLoader = AbImageLoader.getInstance(context);
		downloadUtil = DownloadUtil.getInstance(context);
		back.setOnClickListener(this);
		getData();
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.name_tv:
			intent = new Intent(context, InputOneDataActivity.class);
			intent.putExtra(RentConstants.CONTENT_PARAM, "姓名");
			startActivityForResult(intent, NAME);
			break;
		case R.id.id_tv:
			intent = new Intent(context, InputOneDataActivity.class);
			intent.putExtra(RentConstants.CONTENT_PARAM, "身份证号码");
			startActivityForResult(intent, ID);
			break;
		case R.id.idcard_pic_front_iv:
			if (certified) {
				intent = new Intent();
				intent.setAction(android.content.Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath()
						+ "/Download/" + frontPicPath.substring(frontPicPath.lastIndexOf("/") + 1))), "image/*");
				startActivity(intent);
			} else {
				pic = FRONT;
				handlePopupWindow();
			}
			break;
		case R.id.idcard_pic_back_iv:
			if (certified) {
				intent = new Intent();
				intent.setAction(android.content.Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath()
						+ "/Download/" + backPicPath.substring(backPicPath.lastIndexOf("/") + 1))), "image/*");
				startActivity(intent);
			} else {
				pic = BACK;
				handlePopupWindow();
			}
			break;
		case R.id.commit_tv:
			sendData();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case NAME:
				name_tv.setText(data.getStringExtra(RentConstants.CONTENT_PARAM));
				break;
			case ID:
				id_tv.setText(data.getStringExtra(RentConstants.CONTENT_PARAM));
				break;
			case TAKE_PHOTO:
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(imageUri, "image/*");
				intent.putExtra("scale", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, CROP_PHOTO);
				break;
			case CROP_PHOTO:
				displayImage(imageUri.getPath());
				Log.v(TAG, "imgpath:" + imageUri.getPath());
				break;
			case CHOOSE_PHOTO:
				if (Build.VERSION.SDK_INT >= 19) {
					handleImageOnKitKat(data);
				} else {
					handleImageBeforeKitKat(data);
				}
				break;
			default:
				break;
			}
		}
	}

	private void getData() {
		sign.init();
		http.post(UrlConnector.CERTIFICATION_GET + sign.getAccessToken() + UrlConnector.SIGN + sign.getSign(),
				new AbStringHttpResponseListener() {

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
						try {
							JSONObject object = new JSONObject(content);
							object.get("error");
							handleDataUncertified();
						} catch (JSONException e1) {
							handleDataCertified(new Gson().fromJson(content, CertifyInfomationModel.class));

						}
					}

				});
	}

	private void handleDataUncertified() {
		certified = false;
		commit_tv.setVisibility(View.VISIBLE);
		name_tv.setOnClickListener(this);
		id_tv.setOnClickListener(this);
		idcard_pic_front_iv.setOnClickListener(this);
		idcard_pic_back_iv.setOnClickListener(this);
		commit_tv.setOnClickListener(this);
	}

	private void handleDataCertified(CertifyInfomationModel certifyInfomation) {
		certified = true;
		frontPicPath = certifyInfomation.getIdcard_front();
		backPicPath = certifyInfomation.getIdcard_back();
		imageLoader.display(idcard_pic_front_iv, frontPicPath);
		imageLoader.display(idcard_pic_back_iv, backPicPath);
		downloadUtil.download(frontPicPath);
		downloadUtil.download(backPicPath);
		name_tv.setText(certifyInfomation.getReal_name());
		id_tv.setText(certifyInfomation.getIdcard());
		idcard_pic_front_iv.setOnClickListener(this);
		idcard_pic_back_iv.setOnClickListener(this);
	}

	/**
	 * 
	 * @Title: sendData
	 * @Description: 提交数据 void 返回类型
	 */
	private void sendData() {
		/*
		 * real_name [string] Y 实名 idcard [string] Y 身份证号码 idcard_front 文件 Y
		 * 身份证正面照片 idcard_back 文件 Y 身份证背面照片
		 */
		sign.init();
		sign.addParam("real_name", name_tv.getText().toString().trim());
		sign.addParam("idcard", id_tv.getText().toString().trim());
		sign.addParam("idcard_front", frontFile);
		sign.addParam("idcard_back", backFile);
		http.post(UrlConnector.CERTIFICATION + sign.getAccessToken() + UrlConnector.SIGN + sign.getSign(),
				sign.getParams(), new AbStringHttpResponseListener() {

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
						try {
							Toast.makeText(context, new JSONObject(content).getString("msg"), Toast.LENGTH_SHORT)
									.show();
						} catch (JSONException e) {
							e.printStackTrace();
						}
						finish();
					}
				});
	}

	/**
	 * 
	 * @Title: handlePopupWindow
	 * @Description: 选取照片弹窗
	 */
	private void handlePopupWindow() {
		if (popupWindow == null) {
			showPopupWindow();
		} else {
			dismissPopupWindow();
		}
	}

	/**
	 * 
	 * @Title: showPopupWindow
	 * @Description: 展示弹窗
	 */
	private void showPopupWindow() {
		View view = View.inflate(context, R.layout.select_pic_method_puw, null);
		initPopupWindowView(view);
		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popupWindow.showAtLocation(scrollview.getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
		translateAnimation.setDuration(500);
		view.startAnimation(translateAnimation);
	}

	private void dismissPopupWindow() {
		TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);
		translateAnimation.setDuration(500);
		translateAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				popupWindow.dismiss();
				popupWindow = null;
			}
		});
		popupWindow.getContentView().startAnimation(translateAnimation);
	}

	private void initPopupWindowView(View view) {
		Button take = (Button) view.findViewById(R.id.take);
		Button choose = (Button) view.findViewById(R.id.choose);
		Button cancel = (Button) view.findViewById(R.id.cancel);
		take.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismissPopupWindow();
				File outputImage = new File(Environment.getExternalStorageDirectory(),
						Calendar.getInstance().getTimeInMillis() + ".jpg");
				try {
					if (outputImage.exists()) {
						outputImage.delete();
					}
					outputImage.createNewFile();
				} catch (Exception e) {
				}
				imageUri = Uri.fromFile(outputImage);
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, TAKE_PHOTO);
			}
		});
		choose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismissPopupWindow();
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				startActivityForResult(intent, CHOOSE_PHOTO);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismissPopupWindow();
			}
		});
	}

	/**
	 * 
	 * @Title: handleImageOnKitKat
	 * @Description: 处理选择图片信息
	 * @param data
	 *            设定文件 void 返回类型
	 */
	@TargetApi(19)
	private void handleImageOnKitKat(Intent data) {
		String imagePath = null;
		Uri uri = data.getData();
		if (DocumentsContract.isDocumentUri(this, uri)) {
			String docId = DocumentsContract.getDocumentId(uri);
			if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
				String id = docId.split(":")[1];
				String selection = MediaStore.Images.Media._ID + "=" + id;
				imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
			} else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
				Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(docId));
				imagePath = getImagePath(contentUri, null);
			}
		} else if ("content".equalsIgnoreCase(uri.getScheme())) {
			imagePath = getImagePath(uri, null);
		}
		displayImage(imagePath);
	}

	private void handleImageBeforeKitKat(Intent data) {
		Uri uri = data.getData();
		String imagePath = getImagePath(uri, null);
		displayImage(imagePath);
	}

	/**
	 * 
	 * @Title: getImagePath
	 * @Description: 获取图片路径
	 * @param uri
	 * @param selection
	 * @return 设定文件 String 返回类型
	 */
	private String getImagePath(Uri uri, String selection) {
		String path = null;
		Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				path = cursor.getString(cursor.getColumnIndex(Media.DATA));
			}
		}
		return path;
	}

	/**
	 * 
	 * @Title: displayImage
	 * @Description: 显示图片
	 * @param imagePath
	 */
	private void displayImage(String imagePath) {
		if (imagePath != null) {
			Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
			switch (pic) {
			case FRONT:
				showImage(idcard_pic_front_iv, bitmap);
				// idcard_pic_front_iv.setImageBitmap(bitmap);
				frontFile = new File(imagePath);
				break;
			case BACK:
				showImage(idcard_pic_back_iv, bitmap);
				// idcard_pic_back_iv.setImageBitmap(bitmap);
				backFile = new File(imagePath);
				break;
			default:
				break;
			}
		} else {
			Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
		}
	}

	private void showImage(ImageView imageView, Bitmap bitmap) {
		Bitmap sb = Bitmap.createScaledBitmap(bitmap, imageView.getWidth(), imageView.getWidth(), false);
		imageView.setImageBitmap(sb);
		if (sb != bitmap) {
			bitmap.recycle();
		}
	}
}
