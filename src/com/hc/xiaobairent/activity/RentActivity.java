package com.hc.xiaobairent.activity;

import java.io.File;
import java.util.Calendar;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.google.gson.Gson;
import com.hc.core.utils.RentConstants;
import com.hc.core.utils.SharedpfTools;
import com.hc.core.utils.Sign;
import com.hc.core.utils.UrlConnector;
import com.hc.xiaobairent.R;
import com.hc.xiaobairent.model.HouseModel;
import com.hc.xiaobairent.model.ReckonModel;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @ClassName: RentActivity
 * @Description: 我要租
 * @author frank.fun@qq.com
 * @date 2016年5月20日 上午10:43:18
 *
 */
public class RentActivity extends Activity implements OnClickListener {
	protected static final String TAG = "RentActivity";
	private static final int CHOOSE_PERIOD = 0;
	private static final int TAKE_PHOTO = 1;
	private static final int CROP_PHOTO = 2;
	private static final int CHOOSE_PHOTO = 3;
	private static final int FRONT = 101;
	private static final int BACK = 102;
	private static final int CHOOSE_MONTH = 6;
	private static final int CHOOSE_INSTALLMENT = 7;
	private static final int DEPOSIT_MONTH = 8;
	private static final int PIC1 = 21;
	private static final int PIC2 = 22;
	private static final int PIC3 = 23;
	private static final int PIC4 = 24;
	private static final int PIC5 = 25;
	private PopupWindow popupWindow = null;
	private int whichPic = 0;
	private ScrollView scrollview;
	private ImageView back;
	private TextView title;
	private EditText name_et;
	private EditText phone_et;
	private TextView period_tv;
	private ImageView period_iv;
	private EditText name_broker_et;
	private EditText broker_no_et;
	private ImageView idcard_pic_front_iv;
	private ImageView idcard_pic_back_iv;
	private TextView deposit_tv;
	private ImageView deposit_iv;
	private TextView paid_tv;
	private ImageView paid_iv;
	private LinearLayout staging_ll;
	private TextView installment_tv;
	private ImageView installment_iv;
	private TextView down_payment_tv;
	private TextView period_xiaobairent_tv;
	private ImageView pic1_iv;
	private ImageView pic2_iv;
	private ImageView pic3_iv;
	private ImageView pic4_iv;
	private ImageView pic5_iv;
	private TextView commit_tv;
	private File pic1File;
	private File pic2File;
	private File pic3File;
	private File pic4File;
	private File pic5File;
	private Uri imageUri;
	private Context context = this;
	private int pic = FRONT;
	private File frontFile;
	private File backFile;
	private AbHttpUtil http;
	private AbRequestParams params;
	private SharedpfTools sp;
	private Sign sign;
	private float rental = 0f;
	private int ya = 0;
	private int fu = 0;
	private int deposit = 0;
	private int period = 0;
	private int installment = 0;
	private int type = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rent_activity);
		back = (ImageView) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		scrollview = (ScrollView) findViewById(R.id.scrollview);
		name_et = (EditText) findViewById(R.id.name_et);
		phone_et = (EditText) findViewById(R.id.phone_et);
		period_tv = (TextView) findViewById(R.id.period_tv);
		period_iv = (ImageView) findViewById(R.id.period_iv);
		name_broker_et = (EditText) findViewById(R.id.name_broker_et);
		broker_no_et = (EditText) findViewById(R.id.broker_no_et);
		idcard_pic_front_iv = (ImageView) findViewById(R.id.idcard_pic_front_iv);
		idcard_pic_back_iv = (ImageView) findViewById(R.id.idcard_pic_back_iv);
		deposit_tv = (TextView) findViewById(R.id.deposit_tv);
		deposit_iv = (ImageView) findViewById(R.id.deposit_iv);
		paid_tv = (TextView) findViewById(R.id.paid_tv);
		paid_iv = (ImageView) findViewById(R.id.paid_iv);
		staging_ll = (LinearLayout) findViewById(R.id.staging_ll);
		installment_tv = (TextView) findViewById(R.id.installment_tv);
		installment_iv = (ImageView) findViewById(R.id.installment_iv);
		down_payment_tv = (TextView) findViewById(R.id.down_payment_tv);
		period_xiaobairent_tv = (TextView) findViewById(R.id.period_xiaobairent_tv);
		pic1_iv = (ImageView) findViewById(R.id.pic1_iv);
		pic2_iv = (ImageView) findViewById(R.id.pic2_iv);
		pic3_iv = (ImageView) findViewById(R.id.pic3_iv);
		pic4_iv = (ImageView) findViewById(R.id.pic4_iv);
		pic5_iv = (ImageView) findViewById(R.id.pic5_iv);
		commit_tv = (TextView) findViewById(R.id.commit_tv);
		title.setText("我要租");
		back.setOnClickListener(this);
		period_tv.setOnClickListener(this);
		period_iv.setOnClickListener(this);
		idcard_pic_front_iv.setOnClickListener(this);
		idcard_pic_back_iv.setOnClickListener(this);
		deposit_tv.setOnClickListener(this);
		deposit_iv.setOnClickListener(this);
		paid_tv.setOnClickListener(this);
		paid_iv.setOnClickListener(this);
		installment_tv.setOnClickListener(this);
		installment_iv.setOnClickListener(this);
		pic1_iv.setOnClickListener(this);
		pic2_iv.setOnClickListener(this);
		pic3_iv.setOnClickListener(this);
		pic4_iv.setOnClickListener(this);
		pic5_iv.setOnClickListener(this);
		commit_tv.setOnClickListener(this);
		http = AbHttpUtil.getInstance(context);
		sp = SharedpfTools.getInstance(context);
		sign = new Sign(context);
		rental = getIntent().getFloatExtra(RentConstants.CONTENT_PARAM, 0f);
		type = getIntent().getIntExtra(RentConstants.TYPE_PARAM, 0);
		if (type == HouseModel.NO) {
			staging_ll.setVisibility(View.GONE);
		} else {
			staging_ll.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.period_tv:
		case R.id.period_iv:
			startActivityForResult(new Intent(context, ChoosePeriodActivity.class), CHOOSE_PERIOD);
			break;
		case R.id.idcard_pic_front_iv:
			pic = FRONT;
			handlePopupWindow();
			break;
		case R.id.idcard_pic_back_iv:
			pic = BACK;
			handlePopupWindow();
			break;
		case R.id.deposit_tv:
		case R.id.deposit_iv:
			startActivityForResult(new Intent(context, ChooseDepositPeriodActivity.class), DEPOSIT_MONTH);
			break;
		case R.id.paid_tv:
		case R.id.paid_iv:
			startActivityForResult(new Intent(context, ChoosePeriodActivity.class), CHOOSE_MONTH);
			break;
		case R.id.installment_tv:
		case R.id.installment_iv:
			startActivityForResult(new Intent(context, ChoosePeriodActivity.class), CHOOSE_INSTALLMENT);
			break;
		case R.id.pic1_iv:
			pic = PIC1;
			handlePopupWindow();
			break;
		case R.id.pic2_iv:
			pic = PIC2;
			handlePopupWindow();
			break;
		case R.id.pic3_iv:
			pic = PIC3;
			handlePopupWindow();
			break;
		case R.id.pic4_iv:
			pic = PIC4;
			handlePopupWindow();
			break;
		case R.id.pic5_iv:
			pic = PIC5;
			handlePopupWindow();
			break;
		case R.id.commit_tv:
			commitData();
			break;
		default:
			break;
		}
	}

	private void commitData() {
		/*
		 * user_name [string] 姓名 house_id [int] 房源id tel [string] 手机号 month
		 * [int] 租房周期 agent [string] 经纪人名字 agent_num [int] 经纪人编号 shoufu
		 * [decimal] 首付 fenqi [int] 分期数 price [decimal] 每期还款数 ya [int] 押几（传数字）
		 * fu [int] 付几（传数字：半年6 一年12）
		 */
		if (TextUtils.isEmpty(name_et.getText().toString().trim())) {
			Toast.makeText(context, "请输入您的姓名", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(phone_et.getText().toString().trim())) {
			Toast.makeText(context, "请输入您的手机号", Toast.LENGTH_SHORT).show();
			return;
		}
		if (period == 0) {
			Toast.makeText(context, "请选择租房周期", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(name_broker_et.getText().toString().trim())) {
			Toast.makeText(context, "请输入经纪人姓名", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(broker_no_et.getText().toString().trim())) {
			Toast.makeText(context, "请输入经纪人编号", Toast.LENGTH_SHORT).show();
			return;
		}
		if (frontFile == null) {
			Toast.makeText(context, "请选择身份证正面照片", Toast.LENGTH_SHORT).show();
			return;
		}
		if (backFile == null) {
			Toast.makeText(context, "请选择身份证反面照片", Toast.LENGTH_SHORT).show();
			return;
		}
		if (ya == 0) {
			Toast.makeText(context, "请输入押几", Toast.LENGTH_SHORT).show();
			return;
		}
		if (fu == 0) {
			Toast.makeText(context, "请选择付几", Toast.LENGTH_SHORT).show();
			return;
		}
		if (type == HouseModel.YES && installment == 0) {
			Toast.makeText(context, "请选择小白租分期数", Toast.LENGTH_SHORT).show();
			return;
		}
		sign.init();
		params = new AbRequestParams();
		params.put("user_name", name_et.getText().toString().trim());
		params.put("house_id", getIntent().getStringExtra(RentConstants.ID_PARAM));
		params.put("tel", phone_et.getText().toString().trim());
		params.put("month", period);
		params.put("agent", name_broker_et.getText().toString().trim());
		params.put("agent_num", broker_no_et.getText().toString().trim());
		params.put("zheng", frontFile);
		params.put("fan", backFile);
		params.put("price", rental + "");
		params.put("ya", ya);
		params.put("fu", fu);
		sign.addParam("user_name", name_et.getText().toString().trim());
		sign.addParam("house_id", getIntent().getStringExtra(RentConstants.ID_PARAM));
		sign.addParam("tel", phone_et.getText().toString().trim());
		sign.addParam("month", period);
		sign.addParam("agent", name_broker_et.getText().toString().trim());
		sign.addParam("agent_num", broker_no_et.getText().toString().trim());
		sign.addParam("price", rental + "");
		sign.addParam("ya", ya);
		sign.addParam("fu", fu);
		if (type == HouseModel.YES) {
			params.put("shoufu", down_payment_tv.getText().toString().trim());
			params.put("fenqi", installment);
			sign.addParam("shoufu", down_payment_tv.getText().toString().trim());
			sign.addParam("fenqi", installment);
		}
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
		http.post(UrlConnector.RENT + sp.getAccessToken() + UrlConnector.SIGN + sign.getSign(), params,
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
						Toast.makeText(context, "提交申请成功！", Toast.LENGTH_SHORT).show();
						RentActivity.this.finish();
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case CHOOSE_PERIOD:
				period = data.getIntExtra(RentConstants.CONTENT_PARAM, 0);
				period_tv.setText(period + "个月");
				break;
			case DEPOSIT_MONTH:
				ya = data.getIntExtra(RentConstants.CONTENT_PARAM, 0);
				deposit_tv.setText(ya + "个月");
				if (type == HouseModel.YES) {
					getReckon();
				}
				break;
			case CHOOSE_MONTH:
				fu = data.getIntExtra(RentConstants.CONTENT_PARAM, 0);
				paid_tv.setText(fu + "个月");
				if (type == HouseModel.YES) {
					getReckon();
				}
				break;
			case CHOOSE_INSTALLMENT:
				installment = data.getIntExtra(RentConstants.CONTENT_PARAM, 0);
				installment_tv.setText(period + "期");
				if (type == HouseModel.YES) {
					getReckon();
				}
				break;
			case TAKE_PHOTO:
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(imageUri, "image/*");
				intent.putExtra("scale", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, CROP_PHOTO);
				break;
			case CROP_PHOTO:
				// try {
				displayImage(imageUri.getPath());
				Log.v(TAG, "imgpath:" + imageUri.getPath());
				// Bitmap bitmap =
				// BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
				// idcard_pic_back_iv.setImageBitmap(bitmap);
				// } catch (Exception e) {
				// }
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

	private void getReckon() {
		/*
		 * rental [float] 租金（万元） ya [int] 押几 fu [int] 付几（6（半年）或12（一年）） period
		 * [int] 分期数
		 */
		Log.v(TAG, "get reckon" + "rental" + rental + "ya" + ya + "fu" + fu + "period" + period);
		if (ya != 0) {
			if (fu == 0) {
				Toast.makeText(context, "请选择付房租月数", Toast.LENGTH_SHORT).show();
				return;
			}
			if (period == 0) {
				Toast.makeText(context, "请选择付房租期数", Toast.LENGTH_SHORT).show();
				return;
			}
		} else {
			Toast.makeText(context, "请选择押金月数", Toast.LENGTH_SHORT).show();
			return;
		}
		sign.init();
		params = new AbRequestParams();
		params.put("rental", rental + "");
		params.put("ya", ya);
		params.put("fu", fu);
		params.put("period", period);
		http.post(UrlConnector.RECKON + sp.getAccessToken() + UrlConnector.SIGN + sign.getSign(),
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
						ReckonModel reckon = new Gson().fromJson(content, ReckonModel.class);
						down_payment_tv.setText(reckon.getDownpayment());
						period_xiaobairent_tv.setText(reckon.getInstalment());
					}
				});
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
			switch (pic) {
			case FRONT:
				showImage(idcard_pic_front_iv,bitmap);
//				idcard_pic_front_iv.setImageBitmap(bitmap);
				frontFile = new File(imagePath);
				break;
			case BACK:
				showImage(idcard_pic_back_iv,bitmap);
//				idcard_pic_back_iv.setImageBitmap(bitmap);
				backFile = new File(imagePath);
				break;
			case PIC1:
				showImage(pic1_iv,bitmap);
//				pic1_iv.setImageBitmap(bitmap);
				pic1File = new File(imagePath);
				if (!pic2_iv.isShown()) {
					pic2_iv.setVisibility(View.VISIBLE);
				}
				break;
			case PIC2:
				showImage(pic2_iv,bitmap);
//				pic2_iv.setImageBitmap(bitmap);
				pic2File = new File(imagePath);
				if (!pic3_iv.isShown()) {
					pic3_iv.setVisibility(View.VISIBLE);
				}
				break;
			case PIC3:
				showImage(pic3_iv,bitmap);
//				pic3_iv.setImageBitmap(bitmap);
				pic3File = new File(imagePath);
				if (!pic4_iv.isShown()) {
					pic4_iv.setVisibility(View.VISIBLE);
				}
				break;
			case PIC4:
				showImage(pic4_iv,bitmap);
//				pic4_iv.setImageBitmap(bitmap);
				pic4File = new File(imagePath);
				if (!pic5_iv.isShown()) {
					pic5_iv.setVisibility(View.VISIBLE);
				}
				break;
			case PIC5:
				showImage(pic5_iv,bitmap);
//				pic5_iv.setImageBitmap(bitmap);
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
		Bitmap sb=Bitmap.createScaledBitmap(bitmap, imageView.getWidth(),  imageView.getWidth(), false);
		imageView.setImageBitmap(sb);
		if (sb!=bitmap) {
			bitmap.recycle();
		}
	}
}
