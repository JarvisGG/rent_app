package com.hc.xiaobairent.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import com.hc.xiaobairent.adapter.MyRentalAdapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

public class ImageLoader {
	private ImageView mImageView;
	private String mUrl;
	// 创建 LruCache
	private LruCache<String, Bitmap> mCache;
	private ListView mListView;
	private Set<LoadAspyncTask> mTask;
	
	public ImageLoader(ListView listView) {
		mListView = listView;
		mTask = new HashSet<>();
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		 int cacheSize = maxMemory / 4;
	        mCache = new LruCache<String, Bitmap>(cacheSize) {
	            @Override
	            protected int sizeOf(String key, Bitmap value) {
	               // 在每次存入缓存的时候调用
	                return value.getByteCount();
	            }
	        };
	}
	
	// 添加到cache 
	public void addBitmapToCache(String url, Bitmap bitmap) {
		if(getBitmapFromCache(url) == null) {
			mCache.put(url, bitmap);
		}
	}
	
	// 从cache获取数据
	public Bitmap getBitmapFromCache(String url) {
		return mCache.get(url);
	}
	
	// 通过url加载图片
	public Bitmap getBitmapFromURL(String urlString) {
		Bitmap bitmap;
		InputStream is = null;
		URL url;
		try {
			url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			is = new BufferedInputStream(connection.getInputStream());
			bitmap = BitmapFactory.decodeStream(is);
			connection.disconnect();
			return bitmap;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
		}
		
		return null;
	}
	
	public void showImageByAsyncTask(ImageView imageView, String url) {
		Bitmap bitmap = getBitmapFromCache(url);
		
		if(bitmap != null) {
			imageView.setImageBitmap(bitmap);
		}
	}
	
	public void cancelAllTasks() {
		if(mTask != null) {
            for (LoadAspyncTask task : mTask) {
                task.cancel(false);
            }
        }
	}
	
	public void loadImages(int start, int end) {
		if(end > MyRentalAdapter.URLS.length ) {
			end = MyRentalAdapter.URLS.length;
		}
        for(int i = start; i < end; i++) {
            String url = MyRentalAdapter.URLS[i];
            // 从缓存中取出相应的图片
            Bitmap bitmap = getBitmapFromCache(url);
            // 如果缓存中没有，那么必须下载
            if(bitmap == null) {
            	LoadAspyncTask task = new LoadAspyncTask(url);
                task.execute(url);
                mTask.add(task);
            } else {
                ImageView imageView = (ImageView) mListView.findViewWithTag(url);
                imageView.setImageBitmap(bitmap);

            }
        }
    }
	
	private class LoadAspyncTask extends AsyncTask<String, Void, Bitmap> {

		private String mUrl;
		
		public LoadAspyncTask(String url) {
			mUrl = url;
		}
		
		@Override
		protected Bitmap doInBackground(String... params) {
			 // 从网络获取图片
            Bitmap bitmap = getBitmapFromURL(params[0]);
            String url = params[0];
            if (bitmap != null) {
                // 将不在缓存的图片加入缓存
                addBitmapToCache(url, bitmap);
            }
            return bitmap;
		}
		
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			super.onPostExecute(bitmap);
			ImageView imageView = (ImageView) mListView.findViewWithTag(mUrl);
            if (imageView != null && bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
            mTask.remove(this);
		}
		
	}
}
