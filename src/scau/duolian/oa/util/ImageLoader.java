package scau.duolian.oa.util;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import scau.duolian.oa.R;
import scau.duolian.oa.base.BaseUi;
import scau.duolian.oa.base.C;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageLoader {
	private static final String TAG = "ImageLoader";
	private static final int MAX_CAPACITY = 10;// 一级缓存的最大空间
	private static final long DELAY_BEFORE_PURGE = 10 * 1000;// 定时清理缓存
	private static final String TEMP_DIR = C.dir.temp;
	// 0.75是加载因子为经验值，true则表示按照最近访问量的高低排序，false则表示按照插入顺序排序
	private HashMap<String, Bitmap> mFirstLevelCache = new LinkedHashMap<String, Bitmap>(
			MAX_CAPACITY / 2, 0.75f, true) {
		private static final long serialVersionUID = 1L;

		protected boolean removeEldestEntry(Entry<String, Bitmap> eldest) {
			if (size() > MAX_CAPACITY) {// 当超过一级缓存阈值的时候，将老的值从一级缓存搬到二级缓存
				mSecondLevelCache.put(eldest.getKey(),
						new SoftReference<Bitmap>(eldest.getValue()));
				return true;
			}
			return false;
		};
	};
	// 二级缓存，采用的是软应用，只有在内存吃紧的时候软应用才会被回收，有效的避免了oom
	private ConcurrentHashMap<String, SoftReference<Bitmap>> mSecondLevelCache = 
			new ConcurrentHashMap<String, SoftReference<Bitmap>>(MAX_CAPACITY / 2);

	// 定时清理缓存
	private Runnable mClearCache = new Runnable() {
		@Override
		public void run() {
			clear();
		}
	};
	private Handler mPurgeHandler = new Handler();

	// 重置缓存清理的timer
	private void resetPurgeTimer() {
		mPurgeHandler.removeCallbacks(mClearCache);
		mPurgeHandler.postDelayed(mClearCache, DELAY_BEFORE_PURGE);
	}

	/**
	 * 清理缓存
	 */
	private void clear() {
		mFirstLevelCache.clear();
		mSecondLevelCache.clear();
	}

	/**
	 * 返回缓存，如果没有则返回null
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap getBitmapFromCache(String url) {
		Bitmap bitmap = null;
		bitmap = getFromFirstLevelCache(url);// 从一级缓存中拿
		if (bitmap != null) {
			return bitmap;
		}
		bitmap = getFromSecondLevelCache(url);// 从二级缓存中拿
		
		bitmap = getFromSdCache(url);
		return bitmap;
	}

	/**
	 * 从二级缓存中拿
	 * 
	 * @param url
	 * @return
	 */
	private Bitmap getFromSecondLevelCache(String url) {
		Bitmap bitmap = null;
		SoftReference<Bitmap> softReference = mSecondLevelCache.get(url);
		if (softReference != null) {
			bitmap = softReference.get();
			if (bitmap == null) {// 由于内存吃紧，软引用已经被gc回收了
				mSecondLevelCache.remove(url);
			}
		}
		return bitmap;
	}

	/**
	 * 从一级缓存中拿
	 * 
	 * @param url
	 * @return
	 */
	private Bitmap getFromFirstLevelCache(String url) {
		Bitmap bitmap = null;
		synchronized (mFirstLevelCache) {
			bitmap = mFirstLevelCache.get(url);
			if (bitmap != null) {// 将最近访问的元素放到链的头部，提高下一次访问该元素的检索速度（LRU算法）
				mFirstLevelCache.remove(url);
				mFirstLevelCache.put(url, bitmap);
			}
		}
		return bitmap;
	}
	/**
	 * 从sd卡缓存中获取
	 * @param url
	 */
	private Bitmap getFromSdCache(String url) {
		// TODO Auto-generated method stub
		return getImage(TEMP_DIR, getNameFromUrl(url));
	}

	/**
	 * 加载图片，如果缓存中有就直接从缓存中拿，缓存中没有就下载
	 * @param url
	 * @param adapter
	 * @param holder
	 */
	public void loadImage(String url, BaseAdapter adapter, ImageView iv) {
		resetPurgeTimer();
		Bitmap bitmap = getBitmapFromCache(url);// 从缓存中读取
		if (bitmap == null) {
			iv.setImageResource(R.drawable.ic_launcher);//缓存没有设为默认图片
			ImageLoadTask imageLoadTask = new ImageLoadTask();
			imageLoadTask.execute(url, adapter);
		} else {
			iv.setImageBitmap(bitmap);//设为缓存图片
		}
	}
	/**
	 * 加载图片，如果缓存中有就直接从缓存中拿，缓存中没有就下载
	 * @param url
	 * @param holder
	 */
	public void loadImage(String url, ImageView iv) {
		resetPurgeTimer();
		Bitmap bitmap = getBitmapFromCache(url);// 从缓存中读取
		if (bitmap == null) {
			iv.setImageResource(R.drawable.ic_launcher);//缓存没有设为默认图片
			ImageLoadTask2 imageLoadTask = new ImageLoadTask2();
			imageLoadTask.execute(url, iv);
		} else {
			iv.setImageBitmap(bitmap);//设为缓存图片
		}
	}

	/**
	 * 放入缓存
	 * 
	 * @param url
	 * @param value
	 */
	public void addImage2Cache(String url, Bitmap value) {
		if (value == null || url == null) {
			return;
		}
		synchronized (mFirstLevelCache) {
			mFirstLevelCache.put(url, value);
		}
	}
	/**
	 * 保存到sd作缓存
	 * @param inputStream
	 * @param url
	 */
	public void saveSdCache(InputStream inputStream,String url) {
		// TODO Auto-generated method stub
		String name = getNameFromUrl(url);
		
	}

	class ImageLoadTask extends AsyncTask<Object, Void, Bitmap> {
		String url;
		BaseAdapter adapter;

		@Override
		protected Bitmap doInBackground(Object... params) {
			url = (String) params[0];
			adapter = (BaseAdapter) params[1];
			Bitmap drawable = loadImageFromInternet(url);// 获取网络图片
			return drawable;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if (result == null) {
				return ;
			}
			addImage2Cache(url, result);// 放入缓存
			adapter.notifyDataSetChanged();// 触发getView方法执行，这个时候getView实际上会拿到刚刚缓存好的图片
		}
	}
	class ImageLoadTask2 extends AsyncTask<Object, Void, Bitmap> {
		String url;
		ImageView view;
		@Override
		protected Bitmap doInBackground(Object... params) {
			url = (String) params[0];
			view = (ImageView) params[1];
			Bitmap drawable = loadImageFromInternet(url);// 获取网络图片
			return drawable;
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			if (result == null || view == null) {
				return ;
			}
			view.setImageBitmap(result);
		}
	}

	public Bitmap loadImageFromInternet(String url) {
		Bitmap bitmap = null;
		DefaultHttpClient client = (DefaultHttpClient) BaseUi.fhttp.getHttpClient();
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 3000);
		HttpConnectionParams.setSocketBufferSize(params, 3000);
		HttpResponse response = null;
		InputStream inputStream = null;
		HttpGet httpGet = null;
		try {
Log.i("loadImageFromInternet", "url:"+url);
			httpGet = new HttpGet(url);
			response = client.execute(httpGet);
			int stateCode = response.getStatusLine().getStatusCode();
			if (stateCode != HttpStatus.SC_OK) {
				Log.d(TAG, "func [loadImage] stateCode=" + stateCode);
				return bitmap;
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				try {
					inputStream = entity.getContent();
					bitmap = BitmapFactory.decodeStream(inputStream);
					saveSdCache(inputStream,url);
					return bitmap;
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		}catch(IllegalArgumentException e){
			e.printStackTrace();
		}catch(ConnectTimeoutException e){
			e.printStackTrace();
		}catch(SocketTimeoutException e){
			e.printStackTrace();
		}catch (ClientProtocolException e) {
			httpGet.abort();
			e.printStackTrace();
		} catch (IOException e) {
			httpGet.abort();
			e.printStackTrace();
		} finally {
//			client.close();
		}
		return bitmap;
	}

	public static String getNameFromUrl(String url){
		if(url != null)
			return url.substring(url.lastIndexOf('/'));
		return null;
	}
	final static Pattern p = Pattern.compile("\"(http://[^\"]+.(gif|jpg|jpeg|bmp|bmp|png))\"");
	///////////////************************* 		字符 	*******************************///////////////
	/**
	 * 
	 * @param src 原字符串
	 * @return
	 */
	public static List<String> getUrls(String src){
		List<String> picUrls = new ArrayList<String>();
		Matcher m = p.matcher(src.replaceAll("\\\\/", "/"));
		while(m.find()){
			picUrls.add(new String(m.group(1)));
		}
		return picUrls;	
	}
	/**
	 * 
	 * @param src 原字符串
	 * @param reg 正则表达
	 * @return
	 */
	public static List<String> getUrls(String src,String reg){
		List<String> picUrls = new ArrayList<String>();
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(src);
		while(m.find()){
			picUrls.add(m.group());
		}
		return picUrls;	
	}
	
	
	///////////////************************* 		 sd卡 	*******************************///////////////
	/**
	 * 从sd卡获取图片
	 * @param dir 目录
	 * @param fileName 文件
	 * @return
	 */
	public static Bitmap getImage(String dir,String fileName) {
		// check image file exists
		String realFileName = dir + "/" + fileName;
		File file = new File(realFileName);
		if (!file.exists()) {
			return null;
		}
		// get original image
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(realFileName, options);
	}
	private static double FREE_SD_SPACE_NEEDED_TO_CACHE = 10;
	/**
	 * 保存图片在sd卡上
	 * @param fileName
	 * @param sdir
	 * @return
	 */
	public static String saveImage(InputStream bitmapInput, String fileName, String sdir) {

		if (bitmapInput == null) {
			Log.w(TAG, " trying to save null bitmap");
			return null;
		}
		// 判断sdcard上的空间
		if (FREE_SD_SPACE_NEEDED_TO_CACHE > getFreeSpace()) {
			Log.w(TAG, "Low free space onsd, do not cache");
			return null;
		}
		// 不存在则创建目录
		File dir = new File(sdir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// 保存图片
		try {
			String realFileName = sdir + "/" + fileName;
			File file = new File(realFileName);
			file.createNewFile();
			OutputStream outStream = new FileOutputStream(file);
			write(bitmapInput, outStream);
//			bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			outStream.flush();
			outStream.close();
			Log.i(TAG, "Image saved tosd");
			return realFileName;
		} catch (FileNotFoundException e) {
			Log.w(TAG, "FileNotFoundException");
		} catch (IOException e) {
			Log.w(TAG, "IOException");
		}
		return null;
	}
	private static double MB = 1024;
	/**
	 * 计算sdcard上的剩余空间
	 * 
	 * @return
	 */
	public static int getFreeSpace() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
				.getBlockSize()) / MB;
		return (int) sdFreeMB;
	}
	
	private final static int BUFFER_SIZE = 1024 * 64;
	private static void write(InputStream inputStream, OutputStream outputStream) {
		BufferedInputStream bis = new BufferedInputStream(inputStream);
		BufferedOutputStream bos = new BufferedOutputStream(outputStream);
		byte[] buffer = new byte[BUFFER_SIZE];
		try {
			int len = 0;
			while ((len = bis.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
			bos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
