package com.hc.xiaobairent.activity;

import java.io.File;
import java.util.Calendar;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.hc.core.utils.RentConstants;
import com.hc.core.utils.SharedpfTools;
import com.hc.core.utils.Sign;
import com.hc.core.utils.UrlConnector;
import com.hc.xiaobairent.R;

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
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @ClassName: PostHouseActivity
 * @Description: 发布房屋信息
 * @author frank.fun@qq.com
 * @date 2016年5月20日 上午11:24:18
 *
 */
public class PostHouseActivity extends Activity implements OnClickListener {
	protected static final String TAG = "PostHouseActivity";
	private static final int CHOOSE_CITY = 0;
	private static final int TAKE_PHOTO = 11;
	private static final int CROP_PHOTO = 12;
	private static final int CHOOSE_PHOTO = 13;
	private static final int CHOOSE_TYPE = 2;
	private static final int CHOOSE_CIRCLE = 3;
	private static final int PIC1 = 21;
	private static final int PIC2 = 22;
	private static final int PIC3 = 23;
	private static final int PIC4 = 24;
	private static final int PIC5 = 25;
	private PopupWindow popupWindow = null;
	private ScrollView scrollview;
	private ImageView back;
	private TextView title;
	private EditText building_name_et;
	private EditText building_area_et;
	private TextView house_type_tv;
	private ImageView house_type_iv;
	private TextView region_tv;
	private ImageView region_iv;
	private EditText address_et;
	private TextView circle_tv;
	private ImageView circle_iv;
	private EditText rent_et;
	private EditText commission_et;
	private ImageView pic1_iv;
	private ImageView pic2_iv;
	private ImageView pic3_iv;
	private ImageView pic4_iv;
	private ImageView pic5_iv;
	private TextView commit_tv;
	private Context context = this;
	private AbHttpUtil http;
	private AbRequestParams params;
	private SharedpfTools sp;
	private Sign sign;
	private String house_type = "";
	private String circle = "";
	private int provienceId = 0;
	private int cityId = 0;
	private int regionId = 0;
	private int picPosition = 0;
	private File pic1File;
	private File pic2File;
	private File pic3File;
	private File pic4File;
	private File pic5File;
	private Uri imageUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_house_activity);
		back = (ImageView) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		scrollview = (ScrollView) findViewById(R.id.scrollview);
		building_name_et = (EditText) findViewById(R.id.building_name_et);
		building_area_et = (EditText) findViewById(R.id.building_area_et);
		house_type_tv = (TextView) findViewById(R.id.house_type_tv);
		house_type_iv = (ImageView) findViewById(R.id.house_type_iv);
		region_tv = (TextView) findViewById(R.id.region_tv);
		region_iv = (ImageView) findViewById(R.id.region_iv);
		address_et = (EditText) findViewById(R.id.address_et);
		circle_tv = (TextView) findViewById(R.id.circle_tv);
		circle_iv = (ImageView) findViewById(R.id.circle_iv);
		rent_et = (EditText) findViewById(R.id.rent_et);
		commission_et = (EditText) findViewById(R.id.commission_et);
		pic1_iv = (ImageView) findViewById(R.id.pic1_iv);
		pic2_iv = (ImageView) findViewById(R.id.pic2_iv);
		pic3_iv = (ImageView) findViewById(R.id.pic3_iv);
		pic4_iv = (ImageView) findViewById(R.id.pic4_iv);
		pic5_iv = (ImageView) findViewById(R.id.pic5_iv);
		commit_tv = (TextView) findViewById(R.id.commit_tv);
		title.setText("发布房源");
		back.setOnClickListener(this);
		pic1_iv.setOnClickListener(this);
		pic2_iv.setOnClickListener(this);
		pic3_iv.setOnClickListener(this);
		pic4_iv.setOnClickListener(this);
		pic5_iv.setOnClickListener(this);
		house_type_tv.setOnClickListener(this);
		house_type_iv.setOnClickListener(this);
		region_tv.setOnClickListener(this);
		region_iv.setOnClickListener(this);
		circle_tv.setOnClickListener(this);
		circle_iv.setOnClickListener(this);
		commit_tv.setOnClickListener(this);
		http = AbHttpUtil.getInstance(context);
		sp = SharedpfTools.getInstance(context);
		sign = new Sign(context);
	}

	protected void hideSoftKeyboard() {
		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	@Override
	public void onClick(View v) {
		hideSoftKeyboard();
		Intent intent;
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.house_type_tv:
		case R.id.house_type_iv:
			intent = new Intent(context, ChooseTypeActivity.class);
			startActivityForResult(intent, CHOOSE_TYPE);
			break;
		case R.id.region_tv:
		case R.id.region_iv:
			intent = new Intent(context, ChooseCityActivity.class);
			startActivityForResult(intent, CHOOSE_CITY);
			break;
		case R.id.circle_tv:
		case R.id.circle_iv:
			intent = new Intent(context, ChooseCircleActivity.class);
			startActivityForResult(intent, CHOOSE_CIRCLE);
			break;
		case R.id.pic1_iv:
			picPosition = PIC1;
			handlePopupWindow();
			break;
		case R.id.pic2_iv:
			picPosition = PIC2;
			handlePopupWindow();
			break;
		case R.id.pic3_iv:
			picPosition = PIC3;
			handlePopupWindow();
			break;
		case R.id.pic4_iv:
			picPosition = PIC4;
			handlePopupWindow();
			break;
		case R.id.pic5_iv:
			picPosition = PIC5;
			handlePopupWindow();
			break;
		case R.id.commit_tv:
			commitData();
			break;
		default:
			break;
		}
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

	private void commitData() {
		/*
		 * house_name [string] 大楼名称 area [float] 建筑面积 house_type [string] 户型(精装)
		 * province_id [int] 省id（4） city_id [int] 市id（53） district_id
		 * [int]区id（518） address [string] 具体地址 park_id [int] 所在商圈（传园区id 2） price
		 * [decimal] 租金 charge [decimal] 佣金 longitude [float] 经度（120.38）
		 * latitude [float] 纬度（36.08） 上传文件数组 [array]
		 * Y上传附件数组（array(0=>附件1,1=>附件2)）
		 */
		if (TextUtils.isEmpty(building_name_et.getText().toString().trim())) {
			Toast.makeText(context, "请输入园区名", Toast.LENGTH_SHORT).show();
			return;
		}
		sign.init();
		params = new AbRequestParams();
		params.put("house_name", building_name_et.getText().toString().trim());
		params.put("area", building_area_et.getText().toString().trim());
		params.put("house_type", house_type);
		params.put("province_id", provienceId);
		params.put("city_id", cityId);
		params.put("district_id", regionId);
		params.put("address", address_et.getText().toString().trim());
		params.put("park_id", circle);
		params.put("price", rent_et.getText().toString().trim());
		params.put("charge", rent_et.getText().toString().trim());
		params.put("longitude", "120.38");
		params.put("latitude", "36.08");
		sign.addParam("house_name", building_name_et.getText().toString().trim());
		sign.addParam("area", building_area_et.getText().toString().trim());
		sign.addParam("house_type", house_type);
		sign.addParam("province_id", provienceId);
		sign.addParam("city_id", cityId);
		sign.addParam("district_id", regionId);
		sign.addParam("address", address_et.getText().toString().trim());
		sign.addParam("park_id", circle);
		sign.addParam("price", rent_et.getText().toString().trim());
		sign.addParam("charge", commission_et.getText().toString().trim());
		sign.addParam("longitude", "120.38");
		sign.addParam("latitude", "36.08");
		if (pic1File != null) {
			params.put("0", pic1File);
			if (pic2File != null) {
				params.put("1", pic2File);
				if (pic3File != null) {
					params.put("2", pic3File);
					if (pic4File != null) {
						params.put("3", pic4File);
						if (pic5File != null) {
							params.put("4", pic5File);
						}
					}
				}
			}
		}
		http.post(UrlConnector.POST_HOUSE + sp.getAccessToken() + UrlConnector.SIGN + sign.getSign(), params,
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
						Toast.makeText(context, "发布成功！", Toast.LENGTH_SHORT).show();
						PostHouseActivity.this.finish();
					}
				});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
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
			case CHOOSE_TYPE:
				house_type_tv.setText(data.getStringExtra(RentConstants.CONTENT_PARAM));
				house_type = data.getStringExtra(RentConstants.ID_PARAM);
				Log.v(TAG,
						data.getStringExtra(RentConstants.CONTENT_PARAM) + data.getStringExtra(RentConstants.ID_PARAM));
				break;
			case CHOOSE_CIRCLE:
				circle_tv.setText(data.getStringExtra(RentConstants.CONTENT_PARAM));
				circle = data.getStringExtra(RentConstants.ID_PARAM);
				Log.v(TAG,
						data.getStringExtra(RentConstants.CONTENT_PARAM) + data.getStringExtra(RentConstants.ID_PARAM));
				break;
			case CHOOSE_CITY:
				StringBuffer sb = new StringBuffer("");
				if (!TextUtils.isEmpty(data.getStringExtra("provienceName"))) {
					sb.append(data.getStringExtra("provienceName"));
					if (!TextUtils.isEmpty(data.getStringExtra("cityName"))) {
						sb.append("-");
						sb.append(data.getStringExtra("cityName"));
						if (!TextUtils.isEmpty(data.getStringExtra("regionName"))) {
							sb.append("-");
							sb.append(data.getStringExtra("regionName"));
						}
					}
				}
				region_tv.setText(sb.toString());
				regionId = data.getIntExtra("regionId", 0);
				cityId = data.getIntExtra("cityId", 0);
				provienceId = data.getIntExtra("provienceId", 0);
				break;
			default:
				break;
			}
		}
	}

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

	private void displayImage(String imagePath) {
		if (imagePath != null) {
			Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
			switch (picPosition) {
			case PIC1:
				showImage(pic1_iv, bitmap);
				// pic1_iv.setImageBitmap(bitmap);
				pic1File = new File(imagePath);
				if (!pic2_iv.isShown()) {
					pic2_iv.setVisibility(View.VISIBLE);
				}
				break;
			case PIC2:
				showImage(pic2_iv, bitmap);
				// pic2_iv.setImageBitmap(bitmap);
				pic2File = new File(imagePath);
				if (!pic3_iv.isShown()) {
					pic3_iv.setVisibility(View.VISIBLE);
				}
				break;
			case PIC3:
				showImage(pic3_iv, bitmap);
				// pic3_iv.setImageBitmap(bitmap);
				pic3File = new File(imagePath);
				if (!pic4_iv.isShown()) {
					pic4_iv.setVisibility(View.VISIBLE);
				}
				break;
			case PIC4:
				showImage(pic4_iv, bitmap);
				// pic4_iv.setImageBitmap(bitmap);
				pic4File = new File(imagePath);
				if (!pic5_iv.isShown()) {
					pic5_iv.setVisibility(View.VISIBLE);
				}
				break;
			case PIC5:
				showImage(pic5_iv, bitmap);
				// pic5_iv.setImageBitmap(bitmap);
				pic5File = new File(imagePath);
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
