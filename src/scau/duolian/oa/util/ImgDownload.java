package scau.duolian.oa.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import scau.duolian.oa.base.C;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.util.Log;

public class ImgDownload extends Thread{
	private static String TAG = "ImgDownload";
	private List<String> urls;
	public static String dir = C.dir.temp;
	
	public ImgDownload(List<String> urls) {
		super();
		this.urls = urls;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		for(int i =0 ;i<urls.size();i++){
			loadImage(urls.get(i));
		}
	}
	public synchronized static String getNameFromUrl(String url){
		if(url != null)
			return url.substring(url.lastIndexOf('/'));
		return null;
	}
	public String loadImage(String url) {
		HttpClient client = AndroidHttpClient.newInstance("Android");
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
				return null;
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				try {
					inputStream = entity.getContent();
					String path  = dir+"/"+getNameFromUrl(url);
					FileOutputStream outputStream = new FileOutputStream(new File(path));
					write(inputStream, outputStream);
					return path;
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
			((AndroidHttpClient) client).close();
		}
		return null;
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
