package scau.duolian.oa.base;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import scau.duolian.oa.R;
import scau.duolian.oa.util.AppUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

public class BaseUi extends Activity {
	private List<Activity> activities = new ArrayList<Activity>();
	public static FinalHttp fhttp;

	protected BaseApp app;
	protected BaseHandler handler;
	protected boolean showLoadBar = false;
	protected boolean showDebugMsg = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activities.add(this);
		debugMemory("onCreate");
		// async task handler
		this.handler = new BaseHandler(this);
		// init application
		this.app = (BaseApp) this.getApplicationContext();
		if (fhttp == null)
			fhttp = new FinalHttp();
	}

	protected boolean showingMenu = false;
	protected View menu = null;

	public void showMenu(View view) {
		// TODO Auto-generated method stub
		if (showingMenu)
			menu.setVisibility(View.GONE);
		else
			menu.setVisibility(View.VISIBLE);
		showingMenu = !showingMenu;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		activities.remove(this);
	}

	protected void exit() {
		for (Activity activity : activities) {
			if (activity != null)
				activity.finish();
		}
		System.exit(0);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// debug memory
		debugMemory("onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		// debug memory
		debugMemory("onPause");
		if (menu != null) {
			showingMenu = false;
			menu.setVisibility(View.GONE);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		// debug memory
		debugMemory("onStart");
	}

	@Override
	public void onStop() {
		super.onStop();
		// debug memory
		debugMemory("onStop");
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////
	// util method

	public void toast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	public void overlay(Class<?> classObj) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		intent.setClass(this, classObj);
		startActivity(intent);
	}

	public void overlay(Class<?> classObj, Bundle params) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		intent.setClass(this, classObj);
		intent.putExtras(params);
		startActivity(intent);
	}

	public void overlayForResult(Class<?> classObj, int requestCode) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		intent.setClass(this, classObj);
		startActivityForResult(intent, requestCode);
	}

	public void overlayForResult(Class<?> classObj, int requestCode,
			Bundle params) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		intent.setClass(this, classObj);
		intent.putExtras(params);
		startActivityForResult(intent, requestCode);
	}

	public void forward(Class<?> classObj) {
		Intent intent = new Intent();
		intent.setClass(this, classObj);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
		this.finish();
	}

	public void forward(Class<?> classObj, Bundle params) {
		Intent intent = new Intent();
		intent.setClass(this, classObj);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtras(params);
		this.startActivity(intent);
		this.finish();
	}

	// // logic

	protected boolean isLogin() {
		return ((BaseApp) getApplication()).isLogin();
	}

	protected String getMac() {
//		SharedPreferences preferences = getPreferences(0);
//		String mac = null;
//		if(!preferences.contains(C.config.mac)){
//			mac = DeviceHelper.getMac(this);
//			preferences.edit().putString(C.config.mac, mac).commit();
//		}else
//			mac = preferences.getString(C.config.mac, "");
//		return mac;
		return "00:16:6D:C0:1D:5D";// test
	}

	public Context getContext() {
		return this;
	}

	public BaseHandler getHandler() {
		return this.handler;
	}

	public void setHandler(BaseHandler handler) {
		this.handler = handler;
	}

	public LayoutInflater getLayout() {
		return (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public View getLayout(int layoutId) {
		return getLayout().inflate(layoutId, null);
	}

	public View getLayout(int layoutId, int itemId) {
		return getLayout(layoutId).findViewById(itemId);
	}

	// ///network
	public void get(String url, AjaxCallBack callBack) {
		AjaxParams params = new AjaxParams();
		params.put("c", "sync");
		fhttp.get(url, params, callBack);
	}

	public void get(String url, AjaxParams params, AjaxCallBack callBack) {
		url = getUrl(url);
		params.put("c", "sync");
		Log.i("params", params.toString());
		fhttp.get(url, params, callBack);
	}

	public void post(String url, AjaxParams params, AjaxCallBack callBack) {
		url = getUrl(url);
		Log.i("params", params.toString());
		params.put("c", "sync");
		fhttp.post(url, params, callBack);
	}

	public void post(String url, AjaxParams params, String contentType, AjaxCallBack callBack) {
		url = getUrl(url);
		Log.i("params", params.toString());
		params.put("c", "sync");
		fhttp.post(url, null, params, contentType, callBack);
	}

	private String getUrl(String url) {
		// TODO Auto-generated method stub
		String u = null;
		if (url.startsWith("http:"))
			u = url;
		else
			u = C.api.BASE + url;
		Log.i("url", u);
		return u;
	}

	private ProgressDialog progressDialog = null;

	public void showLoadBar() {
		if (progressDialog == null)
			progressDialog = ProgressDialog.show(this, "请稍等...", "获取数据中...",
					true);
		progressDialog.show();
		showLoadBar = true;
	}

	public void hideLoadBar() {
		if (showLoadBar) {
			if (progressDialog != null)
				progressDialog.dismiss();
			showLoadBar = false;
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////
	// logic method

	public void doFinish() {
		this.finish();
		System.exit(0);
	}

	public void sendMessage(int what) {
		Message m = new Message();
		m.what = what;
		handler.sendMessage(m);
	}

	public void sendMessage(int what, String data) {
		Bundle b = new Bundle();
		b.putString("data", data);
		Message m = new Message();
		m.what = what;
		m.setData(b);
		handler.sendMessage(m);
	}

	public void showPic(Bitmap bitmap) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		Bundle bundle = new Bundle();
		bundle.putParcelable("bitmap", bitmap);
		intent.putExtra("bundle", bundle);
		startActivity(intent);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////
	// debug method

	public void debugMemory(String tag) {
		if (this.showDebugMsg) {
			Log.w(this.getClass().getSimpleName(),
					tag + ":" + AppUtil.getUsedMemory());
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////
	// common classes

	protected final static int cameraRequestCode = 1;
	protected final static int ablumRequestCode = 2;

	/**
	 * 打开相册
	 * 
	 * @param activity
	 */
	protected static void showPicAblum(Activity activity) {
		Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
		getAlbum.setType("image/*");
		activity.startActivityForResult(getAlbum, ablumRequestCode);
	}

	/**
	 * 打开照相机
	 * 
	 * @param activity
	 */
	protected static void showCamera(Activity activity) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		activity.startActivityForResult(intent, cameraRequestCode);
	}

	/**
	 * 选择图片打开对话框
	 */
	protected void showSelectPicDialog() {
		new AlertDialog.Builder(this)
				.setTitle("请选择")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(new String[] { "从相册", "从照相机" }, 0,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									showPicAblum(BaseUi.this);
									break;
								default:
									showCamera(BaseUi.this);
									break;
								}
								dialog.dismiss();
							}
						}).show();
	}

	public static final int SELECT_FILE_CODE = 500;

	public Intent openFileChooser() {
		Intent i = new Intent(Intent.ACTION_GET_CONTENT);
		i.addCategory(Intent.CATEGORY_OPENABLE);
		i.setType("*/*");

		Intent chooser = createChooserIntent(createCameraIntent(), createCamcorderIntent(), createSoundRecorderIntent());
		chooser.putExtra(Intent.EXTRA_INTENT, i);
		return chooser;
	}

	private Intent createChooserIntent(Intent... intents) {
		Intent chooser = new Intent(Intent.ACTION_CHOOSER);
		chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);
		chooser.putExtra(Intent.EXTRA_TITLE, getString(R.string.choose_upload));
		return chooser;
	}

	private String mCameraFilePath;

	private Intent createCameraIntent() {
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File externalDataDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		File cameraDataDir = new File(externalDataDir.getAbsolutePath()
				+ File.separator + "browser-photos");
		cameraDataDir.mkdirs();
		mCameraFilePath = cameraDataDir.getAbsolutePath()
				+ File.separator + System.currentTimeMillis() + ".jpg";
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(mCameraFilePath)));
		return cameraIntent;
	}

	private Intent createCamcorderIntent() {
		return new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
	}

	private Intent createSoundRecorderIntent() {
		return new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
	}

}
