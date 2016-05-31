package com.hc.xiaobairent.activity;

import java.io.File;
import java.util.List;

import org.kymjs.kjframe.ui.BindView;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.image.AbImageLoader;
//import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hc.core.base.BaseActivity;
import com.hc.core.utils.RentConstants;
import com.hc.core.utils.SharedpfTools;
import com.hc.core.utils.Sign;
import com.hc.core.utils.UrlConnector;
import com.hc.xiaobairent.R;
import com.hc.xiaobairent.model.ImgPathModel;
import com.hc.xiaobairent.model.MyRoomDetailModel;

public class ZfMyRoomDetailActivity extends BaseActivity {

	// 初始化标题
	@BindView(id = R.id.menu_back, click = true)
	private ImageView menuBack;
	@BindView(id = R.id.menu_title)
	private TextView menuTitle;

	@BindView(id = R.id.building_name_et)
	private EditText etRoomName;
	@BindView(id = R.id.building_area_et)
	private EditText etRoomArea;
	@BindView(id = R.id.house_type_tv, click = true)
	private TextView tvRoomType;
	@BindView(id = R.id.region_tv, click = true)
	private TextView tvRoomRegion;
	@BindView(id = R.id.address_et)
	private EditText etRoomAddress;
	@BindView(id = R.id.circle_et)
	private EditText etRoomCricle;
	@BindView(id = R.id.rent_et)
	private EditText etRoomRent;
	@BindView(id = R.id.commit_tv, click = true)
	private Button submitBtn;

	private SharedpfTools sharedpfTools;
	private AbHttpUtil httpUtil;
	private MyRoomDetailModel myRoomDetailModel;
	private Gson gson;
	private Sign sign;

	private int detailId;
	private int province_id;
	private int city_id;
	private int district_id;
	private int type_id;
	private int park_id;
	private ImageView[] imgViews = new ImageView[5];
	private File[] picFile = new File[5];
	private int[] imgViewsSource = { R.id.pic1_iv, R.id.pic2_iv, R.id.pic3_iv, R.id.pic4_iv, R.id.pic5_iv };

	private static final int CHOOSE_CITY = 0;
	private static final int CHOOSE_PHOTO = 1;
	private static final int CHOOSE_TYPE = 2;

	private int picPosition = 0;
	private List<ImgPathModel> img_url;
	private String latitude;
	private String longitude;
	private AbImageLoader bitmap;

	// private String wordUrl;
	//
	@Override
	public void setRootView() {
		setAbContentView(R.layout.zf_activity_myroom_detail);
	}

	@Override
	public void initData() {
		super.initData();
		Bundle bundle = getIntent().getExtras();
		detailId = bundle.getInt("deleteId");
		sharedpfTools = SharedpfTools.getInstance(ZfMyRoomDetailActivity.this);
		httpUtil = AbHttpUtil.getInstance(ZfMyRoomDetailActivity.this);
		sign = new Sign(ZfMyRoomDetailActivity.this);
		bitmap = AbImageLoader.getInstance(ZfMyRoomDetailActivity.this);
		sign.init();
		gson = new Gson();
		for (int i = 0; i < 5; i++) {
			imgViews[i] = (ImageView) findViewById(imgViewsSource[i]);
			imgViews[i].setOnClickListener(this);
		}

		initTab();
		applyData();
	}

	private void applyData() {
		AbRequestParams params = new AbRequestParams();
		params.put("id", detailId);
		sign.addParam("id", detailId);
		String url = UrlConnector.MYROOMDETAIL + sharedpfTools.getAccessToken() + UrlConnector.SIGN + sign.getSign();
		httpUtil.post(url, params, new AbStringHttpResponseListener() {

			@Override
			public void onStart() {

			}

			@Override
			public void onFinish() {

			}

			@Override
			public void onFailure(int statusCode, String content, Throwable error) {
				Toast.makeText(ZfMyRoomDetailActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				Log.e("detail", content);
				myRoomDetailModel = gson.fromJson(content, MyRoomDetailModel.class);
				province_id = Integer.parseInt(myRoomDetailModel.getProvince_id());
				city_id = Integer.parseInt(myRoomDetailModel.getCity_id());
				district_id = Integer.parseInt(myRoomDetailModel.getDistrict_id());
				type_id = Integer.parseInt(myRoomDetailModel.getHouse_type_id());
				park_id = Integer.parseInt(myRoomDetailModel.getPark_id());
				etRoomName.setText(myRoomDetailModel.getHouse_name());
				etRoomArea.setText(myRoomDetailModel.getArea());
				tvRoomType.setText(myRoomDetailModel.getHouse_type());
				tvRoomRegion.setText(myRoomDetailModel.getProvince_name() + "-" + myRoomDetailModel.getCity_name() + "-"
						+ myRoomDetailModel.getDistrict_name());
				etRoomAddress.setText(myRoomDetailModel.getAddress());
				etRoomCricle.setText(myRoomDetailModel.getPark_name());
				etRoomRent.setText(myRoomDetailModel.getPrice());
				longitude = myRoomDetailModel.getLongitude();
				latitude = myRoomDetailModel.getLatitude();
				img_url = myRoomDetailModel.getUpdate_img();
				picPosition = img_url.size();
				if (img_url.size() > 0) {
					int i;
					for (i = 0; i < img_url.size(); i++) {
						bitmap.display(imgViews[i], img_url.get(i).getPath());
						// Glide.with(ZfMyRoomDetailActivity.this)
						// .load(img_url.get(i).getPath())
						// .into(imgViews[i]);
						imgViews[i].setVisibility(View.VISIBLE);
					}
					if (i < 5) {
						imgViews[i].setImageResource(R.drawable.add_photo);
						imgViews[i].setVisibility(View.VISIBLE);
						while (i + 1 < img_url.size()) {
							imgViews[i].setVisibility(View.GONE);
						}
					}
				}
			}
		});
	}

	private void submitData() {
		if (TextUtils.isEmpty(etRoomName.getText().toString().trim())) {
			Toast.makeText(ZfMyRoomDetailActivity.this, "请输入园区名", Toast.LENGTH_SHORT).show();
			return;
		}
		AbRequestParams params = new AbRequestParams();
		params.put("house_name", etRoomName.getText().toString().trim());
		params.put("area", etRoomArea.getText().toString().trim());
		params.put("house_type", type_id);
		params.put("province_id", province_id);
		params.put("city_id", city_id);
		params.put("district_id", district_id);
		params.put("address", etRoomAddress.getText().toString().trim());
		params.put("park_id", park_id);
		params.put("price", etRoomRent.getText().toString().trim());
		params.put("charge", etRoomRent.getText().toString().trim());
		params.put("longitude", longitude);
		params.put("latitude", latitude);
		params.put("_method", "put");
		for (int i = 0; i < picFile.length; i++) {
			if (picFile[i] != null) {
				params.put(i + "", picFile[i]);
			}
		}
		sign.addParam("house_name", etRoomName.getText().toString().trim());
		sign.addParam("area", etRoomArea.getText().toString().trim());
		sign.addParam("house_type", type_id + "");
		sign.addParam("province_id", province_id);
		sign.addParam("city_id", city_id);
		sign.addParam("district_id", district_id);
		sign.addParam("address", etRoomAddress.getText().toString().trim());
		sign.addParam("park_id", park_id);
		sign.addParam("price", etRoomRent.getText().toString().trim());
		sign.addParam("charge", etRoomRent.getText().toString().trim());
		sign.addParam("longitude", longitude);
		sign.addParam("latitude", latitude);

		String url = UrlConnector.UPDATE_HOUSE + detailId + "?access-token=" + sharedpfTools.getAccessToken()
				+ UrlConnector.SIGN + sign.getSign();
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
				Log.v("ZFMYROOMDETAIL", content);
				Toast.makeText(ZfMyRoomDetailActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();
				ZfMyRoomDetailActivity.this.finish();
			}
		});
	}

	private void initTab() {
		menuTitle.setText("房源详情");
	}

	@Override
	public void widgetClick(View v) {
		Intent intent;
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.menu_back:
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;

		case R.id.house_type_tv:
		case R.id.house_type_iv:
			intent = new Intent(ZfMyRoomDetailActivity.this, ChooseTypeActivity.class);
			startActivityForResult(intent, CHOOSE_TYPE);
			break;

		case R.id.region_tv:
		case R.id.region_iv:
			intent = new Intent(ZfMyRoomDetailActivity.this, ChooseCityActivity.class);
			startActivityForResult(intent, CHOOSE_CITY);
			break;

		case R.id.pic1_iv:
			picPosition = 0;
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			startActivityForResult(intent, CHOOSE_PHOTO);
			break;
		case R.id.pic2_iv:
			picPosition = 1;
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			startActivityForResult(intent, CHOOSE_PHOTO);
			break;
		case R.id.pic3_iv:
			picPosition = 2;
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			startActivityForResult(intent, CHOOSE_PHOTO);
			break;
		case R.id.pic4_iv:
			picPosition = 3;
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			startActivityForResult(intent, CHOOSE_PHOTO);
			break;
		case R.id.pic5_iv:
			picPosition = 4;
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			startActivityForResult(intent, CHOOSE_PHOTO);
			break;

		case R.id.commit_tv:
			submitData();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case CHOOSE_PHOTO:
				if (Build.VERSION.SDK_INT >= 19) {
					handleImageOnKitKat(data);
				} else {
					handleImageBeforeKitKat(data);
				}
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
				tvRoomRegion.setText(sb.toString());
				district_id = data.getIntExtra("regionId", 0);
				city_id = data.getIntExtra("cityId", 0);
				province_id = data.getIntExtra("provienceId", 0);
				break;

			case CHOOSE_TYPE:
				tvRoomType.setText(data.getStringExtra(RentConstants.CONTENT_PARAM));
				type_id = Integer.parseInt(data.getStringExtra(RentConstants.ID_PARAM));
				Log.v("myroomdetail",
						data.getStringExtra(RentConstants.CONTENT_PARAM) + data.getStringExtra(RentConstants.ID_PARAM));

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
			imgViews[picPosition].setImageBitmap(bitmap);
			picFile[picPosition] = new File(imagePath);
			if (picPosition < 4) {
				if (!imgViews[picPosition + 1].isShown()) {
					imgViews[picPosition + 1].setVisibility(View.VISIBLE);
				}
			}
		}
	}
}
