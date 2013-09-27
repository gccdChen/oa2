package scau.duolian.oa.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;

import scau.duolian.oa.R;
import scau.duolian.oa.adapter.ParnerAdapter;
import scau.duolian.oa.base.BaseMessage;
import scau.duolian.oa.base.BaseUiAuth;
import scau.duolian.oa.base.C;
import scau.duolian.oa.base.MyCallBack;
import scau.duolian.oa.model.Wddh;
import scau.duolian.oa.model.Wdhb;
import scau.duolian.oa.util.JsonUtil;
import scau.duolian.oa.util.SDUtil;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

/**
 * 对话交流 界面
 * 
 * 参数
 * 	hbid -伙伴id hbid
 */
public class UiParnerCom extends BaseUiAuth {
	private ListView lv_parnermessage = null;
	private List<Wddh> wdhbdhs = new ArrayList<Wddh>();
	private Wdhb wdhb = null;
	private EditText ed;
	private String hbid = null;
	private String msg = null;
	private ParnerAdapter adapter = null;
	private FinalDb db = null;
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_parnercom);
		
		db = FinalDb.create(this);
		Intent intent = getIntent();
		hbid = intent.getStringExtra("hbid");
		wdhb = db.findById(hbid,Wdhb.class);
		
		lv_parnermessage = (ListView) findViewById(R.id.lv_parnermessage);
		init();
		refresh();
	}

	private void init() {
		adapter = new ParnerAdapter(this,wdhb, wdhbdhs);
		lv_parnermessage.setAdapter(adapter);
		ed = (EditText) findViewById(R.id.edt_msg);
	}

	private void refresh() {
		AjaxParams params = new AjaxParams();
		params.put("a", "gettl");
		params.put(C.params.dlyid, getUser().dlyid);
		params.put(C.params.uid, getUser().uid);
		params.put("hbid", hbid);
		params.put(C.params.mac, getMac());
		get(C.api.parnerCom, params, new MyCallBack(this) {
			public void onResult(BaseMessage message) {

				super.onResult(message);
				String result = message.getResult();
				try {
					adapter.wdhbdhs = JsonUtil.json2models("Wddh", new JSONArray(result));
					adapter.notifyDataSetChanged();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	public void doSend(View view) {
		msg = ed.getText().toString().trim();
		AjaxParams params = new AjaxParams();
		params.put("a", "send");
		params.put("dlyid", getUser().dlyid);
		params.put("uid", getUser().uid);
		params.put("m", "9");
		params.put("id", hbid);
		params.put("mac", getMac());
		params.put("bz", msg);
		try {
//			if (result != null) {
//				File file = new File(result.getPath());
//				if (file.exists()) {
//					params.put("file", file);// 是否正确
//					Log.i("doSend", result.getPath() + "    " + result.toString());
//				}
//			}
			if (selFilePath != null) {
				File file = new File(selFilePath);
				if (file.exists()) {
					params.put("file", file);// 是否正确
//					Log.i("doSend", result.getPath() + "    " + result.toString());
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		get(C.api.addParnerCom, params, new MyCallBack(this) {
			public void onResult(BaseMessage message) {

				super.onResult(message);
				if (message.isSuccess()) {
					toast("成功上传");
				} else {
					toast(message.getMessage());
				}
			}
		});
	}

	private Uri result;

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if( resultCode == RESULT_OK)
			if (requestCode == ablumRequestCode){
//					result = data == null ? null : data.getData();
				handleFromAblum(data);
			}else if(requestCode == cameraRequestCode){
				handlePicFromCamera(data);
			}
		super.onActivityResult(requestCode, resultCode, data);
	}
	private String selFilePath = null;
	private void handleFromAblum(Intent data) {
		ContentResolver resolver = getContentResolver();
		Uri originalUri = data.getData(); // 获得图片的uri
		String[] proj = { MediaStore.Images.Media.DATA };
		// 好像是android多媒体数据库的封装接口，具体的看Android文档
		Cursor cursor = managedQuery(originalUri, proj, null, null, null);
		// 按我个人理解 这个是获得用户选择的图片的索引值
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		// 将光标移至开头 ，这个很重要，不小心很容易引起越界
		cursor.moveToFirst();
		// 最后根据索引值获取图片路径
		selFilePath = cursor.getString(column_index);
	}

	private void handlePicFromCamera(Intent data) {
		Bundle bundle = data.getExtras();
		Bitmap bitmap = (Bitmap) bundle.get("data");//
		selFilePath = SDUtil.saveImage(bitmap, UUID.randomUUID()+".jog", C.dir.temp);
	}

	public void doSelectFile(View view) {
//		startActivityForResult(openFileChooser(), SELECT_FILE_CODE);
		showSelectPicDialog();
	}
}
