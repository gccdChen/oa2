package scau.duolian.oa.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.http.AjaxParams;
import scau.duolian.oa.R;
import scau.duolian.oa.base.BaseMessage;
import scau.duolian.oa.base.BaseUiAuth;
import scau.duolian.oa.base.C;
import scau.duolian.oa.base.MyCallBack;
import scau.duolian.oa.model.Wdhb;
import scau.duolian.oa.model.Wdrwlx;
import scau.duolian.oa.util.DateUtil;
import scau.duolian.oa.util.SDUtil;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * 添加任务
 */
public class UiAddTask extends BaseUiAuth {
	private TextView title, bz, jhksrq, jhjsrq;
	private TextView zrr;
	private Spinner rwlb;
	private Uri result;
	private FinalDb db;

	private List<Wdhb> wdhbs = null;
	private List<Wdrwlx> wdrwlxs = null;
	private String selzrrid = null;
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_addtask);
		db = FinalDb.create(this);
		init();
		refresh();
	}

	private void init() {
		title = (TextView) findViewById(R.id.tv_title);
		bz = (TextView) findViewById(R.id.edt_bz);
		zrr = (TextView) findViewById(R.id.tv_zrr);
		jhksrq = (TextView) findViewById(R.id.edt_jhksrq);
		jhjsrq = (TextView) findViewById(R.id.edt_jhjsrq);
		rwlb = (Spinner) findViewById(R.id.spi_rwlb);

		wdrwlxs = db.findAll(Wdrwlx.class);
		wdhbs = db.findAll(Wdhb.class);
		
		rwlb.setAdapter(new ArrayAdapter<Wdrwlx>(this, android.R.layout.simple_spinner_item, wdrwlxs));

	}

	private void refresh() {

	}

	public void doSure(View v) {
		String str_title = title.getText().toString();
		String str_bz = bz.getText().toString();
		AjaxParams params = new AjaxParams();
		params.put("a", "send");
		params.put("dlyid", getUser().dlyid);
		params.put("uid", getUser().uid);
		params.put("mac", getMac());
		params.put("rwlb", rwlb.getSelectedItem().toString());
		params.put("title", str_title.toString().trim());
		params.put("bz", str_bz.toString().trim());
		if(selzrrid != null)
			params.put("zrr", selzrrid);
		params.put("jhksrq", ""+DateUtil.dateStrToLong(jhksrq.getText().toString()));
		params.put("jhjsrq", ""+DateUtil.dateStrToLong(jhksrq.getText().toString()));
	/*	try {
			if (result != null) {
				File file = new File(result.getPath());
				if (file.exists()) {
					params.put("file", file);// 是否正确
					Log.i("doSend", result.getPath() + "    " + result.toString());
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		post(C.api.addTask, params, new MyCallBack(this) {
			public void onResult(BaseMessage message) {
				toast(message.getMessage());
				if (message.isSuccess()) {
					finish();
				}
				super.onResult(message);
			}
		});
	}

	public void doCancel(View v) {
		finish();
	}

	public void doSelectFile(View v) {
		showSelectPicDialog();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK){
			switch (requestCode) {
			case ablumRequestCode:
				break;
			case cameraRequestCode:
				break;
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	interface MyCallback{
		public String path = null;
		public Bitmap bitmap = null;
		public void handler(String path,Bitmap bitmap);
	}
	
	private void handleFromAblum(Intent data) {
		ContentResolver resolver = getContentResolver();
		Uri originalUri = data.getData(); // 获得图片的uri
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(originalUri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String p = cursor.getString(column_index);//拿到path
		try {
			Bitmap bitmap = MediaStore.Images.Media.getBitmap(resolver,originalUri);//拿到bitmap
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//handle 方法.
	}

	private void handlePicFromCamera(Intent data) {
		Bundle bundle = data.getExtras();
		Bitmap bitmap = (Bitmap) bundle.get("data");//拿到bitmap
		SDUtil.saveImage(bitmap, UUID.randomUUID()+".jog", C.dir.temp);//拿到path
		//handle 方法.
	}

	Dialog dialog = null;

	public void selPartner(View view) {
		if (dialog == null) {
			dialog = new Dialog(this, R.style.scau_dialog);
			View v = LayoutInflater.from(this).inflate(R.layout.dialog_sel_hb, null);
			dialog.setContentView(v);

			ViewGroup layout = (ViewGroup) v.findViewById(R.id.ll_1);

			TextView tv_name = null;
			for (int i = 0; i < wdhbs.size(); i++) {
				final Wdhb wdhb = wdhbs.get(i);
				tv_name = new TextView(this);
				tv_name.setText(wdhb.name);
				tv_name.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						zrr.setText(wdhb.name);
						selzrrid = wdhb.id;
						if (dialog != null)
							dialog.dismiss();
					}
				});
				layout.addView(tv_name);
			}
		}
		dialog.show();
	}
	public void showSelDate(View view) {
		Calendar calendar = Calendar.getInstance();
		DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				// TODO Auto-generated method stub
				jhksrq.setText("" + year + "-" + monthOfYear + "-" + dayOfMonth);
			}
		}, calendar.get(Calendar.YEAR) , calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		dialog.show();
	}

	public void showSelDateB(View view) {
		Calendar calendar = Calendar.getInstance();
		DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				// TODO Auto-generated method stub
				jhjsrq.setText("" + year + "-" + monthOfYear + "-" + dayOfMonth);
			}
		}, calendar.get(Calendar.YEAR) , calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		dialog.show();
	}

}
