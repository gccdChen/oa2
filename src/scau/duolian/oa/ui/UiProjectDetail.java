package scau.duolian.oa.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;

import scau.duolian.oa.R;
import scau.duolian.oa.adapter.ProDetailAdapter;
import scau.duolian.oa.base.BaseMessage;
import scau.duolian.oa.base.BaseUiAuth;
import scau.duolian.oa.base.C;
import scau.duolian.oa.base.MyCallBack;
import scau.duolian.oa.model.Wddh;
import scau.duolian.oa.model.Wdrw;
import scau.duolian.oa.model.Wdxm;
import scau.duolian.oa.util.DateUtil;
import scau.duolian.oa.util.DeviceHelper;
import scau.duolian.oa.util.JsonUtil;
import scau.duolian.oa.util.SDUtil;
import scau.duolian.oa.util.StringUtil;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

/**
 * 获取指定项目详情
 * 
 * 需要参数 id ,任务 wdxm id
 */
public class UiProjectDetail extends BaseUiAuth {
	private TextView xmmc, rq, tx, xx, xj;
	private FinalDb db = null;
	private List<Wdrw> wdrws;
	private Wdxm wdxm;
	private String id;
	private ProDetailAdapter adapter = null;
	private ExpandableListView elv_content = null;
	private EditText edt_msg = null;
	private List<Wddh> wddhs = new ArrayList<Wddh>();

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_project_detail);
		db = FinalDb.create(this);
		init();
		refresh();
	}

	public void update() {
		adapter.notifiThis();
	}

	private void refresh() {
		// TODO Auto-generated method stub
		AjaxParams params = new AjaxParams();
		params.put("a", "getdetail");
		params.put(C.params.id, id);
		params.put(C.params.dlyid, getUser().dlyid);
		params.put(C.params.uid, getUser().uid);
		params.put(C.params.mac, getMac());
		get(C.api.rwComment, params, new MyCallBack(this, true) {
			@Override
			public void onResult(BaseMessage message) {
				// TODO Auto-generated method stub
				super.onResult(message);
				toast(message.getMessage());
				if (message.isSuccess()) {
					String result = message.getResult();
					JSONArray modelJsonArray;
					try {
						modelJsonArray = new JSONArray(result);
						wddhs = JsonUtil.json2models("Wddh", modelJsonArray);
						update();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	private void init() {
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		wdxm = db.findById(id, Wdxm.class);

		elv_content = (ExpandableListView) findViewById(R.id.el_pro_content);
		adapter = new ProDetailAdapter(this, wdxm, db, wddhs);
		elv_content.setAdapter(adapter);

		edt_msg = (EditText) findViewById(R.id.edt_msg);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case cameraRequestCode:
				handlePicFromCamera(data);
				break;
			case ablumRequestCode:
				handleFromAblum(data);
				break;
			default:
				break;
			}
		}
	}

	private void handleFromAblum(Intent data) {
		ContentResolver resolver = getContentResolver();
		Uri originalUri = data.getData(); // 获得图片的uri
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(originalUri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		selFilePath = cursor.getString(column_index);
		toast(selFilePath + "已选择.");
//			Bitmap bitmap = MediaStore.Images.Media.getBitmap(resolver,
//					originalUri);

		// saveTempImgFile(path);
	}

	private void handlePicFromCamera(Intent data) {
		Bundle bundle = data.getExtras();
		Bitmap bitmap = (Bitmap) bundle.get("data");//
		selFilePath = saveTempImgFile(bitmap);
		toast("照片已选择.");
	}

	private String saveTempImgFile(Bitmap bitmap) {
		String fileName = UUID.randomUUID().toString() + ".png";
		fileName = SDUtil.saveImage(bitmap, fileName, C.dir.temp);
		return fileName;
	}

	private String selFilePath = null;

	// btn
	public void doSend(View view) {
		String bz = edt_msg.getText().toString();
		AjaxParams params = new AjaxParams();
		params.put("a", "send");
		params.put("m", "8");
		params.put(C.params.dlyid, getUser().dlyid);
		params.put(C.params.uid, getUser().uid);
		params.put(C.params.id, id);
		params.put(C.params.mac, getMac());
		params.put(C.params.bz, bz);
		if (!StringUtil.isBlank(selFilePath))
			try {
				params.put(C.params.file, new File(selFilePath));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		post(C.api.sendRwComment, params, new MyCallBack(this, true) {
			@Override
			public void onResult(BaseMessage message) {
				// TODO Auto-generated method stub
				super.onResult(message);
				toast(message.getMessage());
				if (message.isSuccess()) {
					refresh();
				}
			}
		});
	}

	public void doSelectFile(View view) {
		showSelectPicDialog();
	}

	public void addToCalender(View view) {
		// TODO Auto-generated method stub
		String ds = wdxm.bz;
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateUtil.longStrToDate(wdxm.ksrq));
		ContentResolver cr = getContentResolver();
		DeviceHelper.addToCalendar("[多联小助手]"+wdxm.title,"[内容]"+ds, cal, cr);
		toast("已加入到日历");
	}
	
	public void toSetting(View view){
		Bundle params = new  Bundle();
		params.putString("id",wdxm.id);
		overlay(UIAddProject.class,params);
	}
}
