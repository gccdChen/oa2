package scau.duolian.oa.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.http.AjaxParams;
import scau.duolian.oa.R;
import scau.duolian.oa.base.BaseMessage;
import scau.duolian.oa.base.BaseUiAuth;
import scau.duolian.oa.base.C;
import scau.duolian.oa.base.MyCallBack;
import scau.duolian.oa.model.Wddh;
import scau.duolian.oa.model.Wdhb;
import scau.duolian.oa.model.Wdlcb;
import scau.duolian.oa.model.Wdxm;
import scau.duolian.oa.util.DateUtil;
import scau.duolian.oa.util.SDUtil;
import scau.duolian.oa.util.StringUtil;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 添加 里程碑
 *  xmid 项目id 
 */
public class UiAddMilestone extends BaseUiAuth {
	private TextView title, rq, tx, bz;

	private FinalDb db = null;
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_addmilestone);

		db = FinalDb.create(this);
		
		init();
	}
	private String id = null;
	private String xmid = null;
	private void init() {
		Intent intent = getIntent();
		xmid = intent.getStringExtra("xmid");
		
		title = (TextView) findViewById(R.id.tv_title);
		rq = (TextView) findViewById(R.id.edt_time_setting);
		tx = (TextView) findViewById(R.id.edt_remind_time);
		bz = (TextView) findViewById(R.id.edt_bz);
		
		rq.setText(DateUtil.getDateStr());
		tx.setText(DateUtil.getDateStr());
		
	}


	public void doSure(View v) {
		String str_xj = bz.getText().toString();
		AjaxParams params = new AjaxParams();
		params.put("a", "send");
		params.put("dlyid", getUser().dlyid);
		params.put("uid", getUser().uid);
		params.put("mac", getMac());
		params.put("title", title.toString().trim());
		params.put("xmid", xmid);
		params.put("xj", str_xj);
		params.put("rq",""+ DateUtil.strToTime(rq.toString()));
		params.put("tx", ""+ DateUtil.strToTime(tx.toString()));
		post(C.api.addMilestone, params, new MyCallBack(this) {
			public void onResult(BaseMessage message) {
				if (message.isSuccess()) {
					toast("成功上传");
				} else {
					toast(message.getMessage());
				}
				super.onResult(message);
			}
		});
		finish();
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
		
	}

	private void handlePicFromCamera(Intent data) {
		Bundle bundle = data.getExtras();
		Bitmap bitmap = (Bitmap) bundle.get("data");//拿到bitmap
		SDUtil.saveImage(bitmap, UUID.randomUUID()+".jog", C.dir.temp);//拿到path
	}

	Dialog dialog = null;

	public void showSelDate(View view) {
		Calendar calendar = Calendar.getInstance();
		DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				// TODO Auto-generated method stub
				rq.setText("" + year + "-" + monthOfYear + "-" + dayOfMonth);
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
				tx.setText("" + year + "-" + monthOfYear + "-" + dayOfMonth);
			}
		}, calendar.get(Calendar.YEAR) , calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		dialog.show();
	}
}
