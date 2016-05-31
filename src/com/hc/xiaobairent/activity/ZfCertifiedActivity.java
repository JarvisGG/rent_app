package com.hc.xiaobairent.activity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.BindView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.google.gson.Gson;
import com.hc.core.base.BaseActivity;
import com.hc.core.utils.IconSelectDialog;
import com.hc.core.utils.RentConstants;
import com.hc.core.utils.SharedpfTools;
import com.hc.core.utils.Sign;
import com.hc.core.utils.UrlConnector;
import com.hc.xiaobairent.R;
import com.hc.xiaobairent.utils.Bimp;
import com.hc.xiaobairent.utils.Bimp1;
import com.hc.xiaobairent.utils.FileUtils;
import com.hc.xiaobairent.utils.ImageItem;
import com.hc.xiaobairent.utils.PublicWay;
import com.hc.xiaobairent.utils.Res;

public class ZfCertifiedActivity extends BaseActivity{
	// 标题栏
	@BindView(id = R.id.menu_title)
	private TextView menuTitle;
	@BindView(id = R.id.menu_back, click = true)
	private ImageView menuBack;
	
	@BindView(id = R.id.gv_gallery1)
	private GridView gvGallery1;
	@BindView(id = R.id.gv_gallery2)
	private GridView gvGallery2;
	@BindView(id = R.id.next_btu, click = true)
	private Button submitBtn;
	@BindView(id = R.id.property_pic)
	private LinearLayout properthDiv;
	@BindView(id = R.id.property_tv)
	private TextView tvProperty;
	
	private int listSize = 2;
	private List<ImageItem> list;
	public static Bitmap bimap;
	private GridAdapter adapter1;
	private GridAdapter1 adapter2;
	
	private AbHttpUtil httpUtil;
	private KJHttp kjHttp;
	private SharedpfTools sharedpfTools;
	private Sign sign;
	private Gson gson;
	private int userType;

	// 参数
	private static final int TAKE_PICTURE = 10;
	private static final int SELECT_PICTURE = 11;
	private static final int TAKE_SELECT_PICTURE = 12;

	@Override
	public void setRootView() {
		setAbContentView(R.layout.zf_activity_certified);
	}
	
	@Override
	public void initData() {
		super.initData();
		
		sharedpfTools = SharedpfTools.getInstance(getApplicationContext());
		sign = new Sign(getApplicationContext());
		sign.init();
		httpUtil = AbHttpUtil.getInstance(getApplicationContext());
		kjHttp = new KJHttp();

		Res.init(this);
		bimap = BitmapFactory.decodeResource(getResources(), R.drawable.add_photo);
		PublicWay.activityList.add(this);
		
		userType = sharedpfTools.getUserType();
		// 显示不同的提交图片类型
		
		switch (userType) {
		case RentConstants.OWNER:
			properthDiv.setVisibility(View.VISIBLE);
			tvProperty.setText("上传房产照片");
			break;
		
		case RentConstants.BROKER:
			properthDiv.setVisibility(View.VISIBLE);
			tvProperty.setText("上传营业执照照片");
			break;
			
		default:
			properthDiv.setVisibility(View.GONE);
			break;
		}
		
		gvGallery1.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter1 = new GridAdapter(this);
		gvGallery1.setAdapter(adapter1);
		gvGallery1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position == Bimp.tempSelectBitmap.size()) {
					showSelectIcon(1);
				} else {
					Intent intent = new Intent(ZfCertifiedActivity.this,
							GalleryActivity.class);
					intent.putExtra("position", "1");
					intent.putExtra("ID", position);
					overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
					startActivity(intent);
				}
			}
		});
		
		gvGallery2.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter2 = new GridAdapter1(this);
		gvGallery2.setAdapter(adapter2);
		gvGallery2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position == Bimp1.tempSelectBitmap.size()) {
					showSelectIcon(2);
				} else {
					Intent intent = new Intent(ZfCertifiedActivity.this,
							GalleryActivity1.class);
					intent.putExtra("position", "1");
					intent.putExtra("ID", position);
					overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
					startActivity(intent);
				}
			}
		});
		initTab();
	}
	
	// 选择头像
	private void showSelectIcon(final int command) {
		final IconSelectDialog dialog = new IconSelectDialog(ZfCertifiedActivity.this);
		dialog.show();
		
		Button take = (Button) dialog.findViewById(R.id.btn_photo_take);
		take.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 选择拍照
				Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(openCameraIntent, command);
				overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
				dialog.cancel();
			}
		});
		
		Button select = (Button) dialog.findViewById(R.id.btn_photo_select);
		select.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (command) {
				case 1:
					startActivity(new Intent(ZfCertifiedActivity.this, AlbumActivity.class));
					break;
					
				case 2:
					startActivity(new Intent(ZfCertifiedActivity.this, AlbumActivity1.class));
					break;
				default:
					break;
				}
				
				overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
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
		
	
	private void initTab(){
		menuTitle.setText("上传照片");
		switch (sharedpfTools.getUserType()) {
		case RentConstants.BROKER:
			tvProperty.setText("上传营业执照照片");
			break;

		case RentConstants.OWNER:
			tvProperty.setText("上传房产证照片");
			break;
			
		default:
			break;
		}
	}
	
	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.menu_back:
			finish();
			overridePendingTransition(R.anim.cu_push_left_in, R.anim.cu_push_right_out);
			break;
		
		case R.id.next_btu:
			applyData();
				//startActivity(new Intent(getApplicationContext(), ZfRegister03Activity.class));
				//overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
			

		default:
			break;
		}
	}
	
	// 提交照片
	private void applyData(){
		HttpParams params = new HttpParams();
		Log.e("length3", Bimp.getBimp().tempSelectBitmap.size()+"");
		for(int i = 0; i < Bimp.getBimp().tempSelectBitmap.size(); i++) {
			params.put("idcards[]", new File(Bimp.getBimp().tempSelectBitmap.get(i).getImagePath()));
		}
		switch (userType) {
		case RentConstants.OWNER:
			for(int i = 0; i < Bimp1.getBimp().tempSelectBitmap.size(); i++) {
				params.put("ownership[]", new File(Bimp1.getBimp().tempSelectBitmap.get(i).getImagePath()));
			}
			break;
		
		case RentConstants.BROKER:
			for(int i = 0; i < Bimp1.getBimp().tempSelectBitmap.size(); i++) {
				params.put("licence[]", new File(Bimp1.getBimp().tempSelectBitmap.get(i).getImagePath()));
			}
			break;
			
		default:
			break;
		}
		String url = UrlConnector.VERIFY + sharedpfTools.getAccessToken() + UrlConnector.SIGN + sign.getSign();
		Log.e("updata_pic_url", url);
		kjHttp.post(url, params, new HttpCallBack() {
			@Override
			public void onFailure(int errorNo, String strMsg) {
				super.onFailure(errorNo, strMsg);
				Toast.makeText(getApplication(), "请求失败",
						Toast.LENGTH_LONG).show();

			}
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				Log.e("updata_pic", t);
				try {
					JSONObject jsonObject = new JSONObject(t);
					if(jsonObject.getInt("state") == 1) {
						startActivity(new Intent(ZfCertifiedActivity.this, ZfLoginActivity.class));
						overridePendingTransition(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}
	
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading(1);
		}

		public int getCount() {
			if(Bimp.tempSelectBitmap.size() == 2){
				return 2;
			}
			return (Bimp.tempSelectBitmap.size() + 1);
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.zf_photo_select_item,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position ==Bimp.tempSelectBitmap.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.add_photo));
				if (position == 2) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
			}

			return convertView;
		}
		public class ViewHolder {
			public ImageView image;
		}
	}
	
	public class GridAdapter1 extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter1(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading(2);
		}

		public int getCount() {
			if(Bimp1.tempSelectBitmap.size() == 2){
				return 2;
			}
			return (Bimp1.tempSelectBitmap.size() + 1);
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.zf_photo_select_item,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position ==Bimp1.tempSelectBitmap.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.add_photo));
				if (position == 2) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp1.tempSelectBitmap.get(position).getBitmap());
			}

			return convertView;
		}
		public class ViewHolder {
			public ImageView image;
		}
	}
	
	public void loading(final int command) {
		switch (command) {
		case 1:
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.tempSelectBitmap.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							Bimp.max += 1;
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						}
					}
				}
			}).start();
			break;
			
		case 2:
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp1.max == Bimp1.tempSelectBitmap.size()) {
							Message message = new Message();
							message.what = 2;
							handler.sendMessage(message);
							break;
						} else {
							Bimp1.max += 1;
							Message message = new Message();
							message.what = 2;
							handler.sendMessage(message);
						}
					}
				}
			}).start();

		default:
			break;
		}

	}
	
	
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				adapter1.notifyDataSetChanged();
				break;
			
			case 2:
				adapter2.notifyDataSetChanged();
				break;
			}
			super.handleMessage(msg);
		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if (Bimp.tempSelectBitmap.size() < 2 && resultCode == RESULT_OK) {
				
				String fileName = String.valueOf(System.currentTimeMillis());
				Bitmap bm = (Bitmap) data.getExtras().get("data");
				// 写入缓存文件
				ByteArrayOutputStream baos = null;
				BufferedOutputStream bos = null;
				FileOutputStream fos = null;
				
				baos = new ByteArrayOutputStream();
				bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				byte[] photodata = baos.toByteArray();
				try {
					fos = new FileOutputStream(FileUtils.SDPATH + fileName + ".jpg");
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
				
				ImageItem takePhoto = new ImageItem();
				takePhoto.setBitmap(bm);
				takePhoto.setImagePath(FileUtils.SDPATH + fileName + ".jpg");
				Bimp.getBimp().tempSelectBitmap.add(takePhoto);
			}
			break;
		case 2:
			if (Bimp1.tempSelectBitmap.size() < 2 && resultCode == RESULT_OK) {
				
				String fileName = String.valueOf(System.currentTimeMillis());
				Bitmap bm = (Bitmap) data.getExtras().get("data");
				
				ByteArrayOutputStream baos = null;
				BufferedOutputStream bos = null;
				FileOutputStream fos = null;
				
				baos = new ByteArrayOutputStream();
				bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				byte[] photodata = baos.toByteArray();
				try {
					fos = new FileOutputStream(FileUtils.SDPATH + fileName + ".jpg");
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
				
				ImageItem takePhoto = new ImageItem();
				takePhoto.setBitmap(bm);
				takePhoto.setImagePath(FileUtils.SDPATH + fileName + ".jpg");
				Bimp1.getBimp().tempSelectBitmap.add(takePhoto);
			}
			break;
		}
	}
	

	protected void onRestart() {
		adapter1.update();
		adapter2.update();
		super.onRestart();
	}
}
