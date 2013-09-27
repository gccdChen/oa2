package scau.duolian.oa.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.UUID;

import net.tsz.afinal.http.AjaxParams;
import scau.duolian.oa.R;
import scau.duolian.oa.base.BaseMessage;
import scau.duolian.oa.base.BaseUiAuth;
import scau.duolian.oa.base.C;
import scau.duolian.oa.base.MyCallBack;
import scau.duolian.oa.model.User;
import scau.duolian.oa.util.PatternUtil;
import scau.duolian.oa.util.SDUtil;
import scau.duolian.oa.util.StringUtil;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class UiPersonSetting extends BaseUiAuth{
	private EditText edt_name = null;
	private EditText edt_newpwd = null;
	private EditText edt_configpwd = null;
	private EditText edt_phone = null;
	private EditText edt_qq = null;
	private ImageView iv_face = null;
	
	private String selectPath = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_personsetting);
		
		initCon();
		initData();
		iv_face.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showSelectPicDialog();
			}
		});
	}
	
	private void initData() {
		// TODO Auto-generated method stub
		User user = getUser();
		edt_name.setText(user.username);
		edt_phone.setText(user.phone);
		edt_phone.setText(user.qq);
	}

	private void initCon() {
		// TODO Auto-generated method stub
		iv_face = (ImageView) findViewById(R.id.iv_face);
		edt_newpwd = (EditText)findViewById(R.id.edt_newpwd);
		edt_configpwd = (EditText)findViewById(R.id.edt_configpwd);
		edt_name = (EditText)findViewById(R.id.edt_name);
		edt_qq = (EditText)findViewById(R.id.edt_qq);
		edt_phone = (EditText)findViewById(R.id.edt_phone);
	}

	private void handleFromAblum(Intent data) {
		ContentResolver resolver = getContentResolver();
		Uri originalUri = data.getData(); // 获得图片的uri
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(originalUri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		selectPath = cursor.getString(column_index);
	}

	private void handlePicFromCamera(Intent data) {
		Bundle bundle = data.getExtras();
		Bitmap bitmap = (Bitmap) bundle.get("data");//
		String path = saveTempImgFile(bitmap);
	}
	
	private String saveTempImgFile(Bitmap bitmap) {
		String fileName = UUID.randomUUID().toString() + ".png";
		fileName = SDUtil.saveImage(bitmap, fileName, C.dir.temp);
		return fileName;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case cameraRequestCode:
			handlePicFromCamera(data);
			break;
		default:
			handleFromAblum(data);
			break;
		}
	}
	
	//btn
	public void doSure(View view) {
		// TODO Auto-generated method stub
		AjaxParams params = new AjaxParams();
		
		params.put("a", "edit");
		params.put(C.params.dlyid, getUser().dlyid);
		params.put(C.params.uid, getUser().uid);
		params.put(C.params.mac, getMac());
		
		String tilte = edt_name.getText().toString();
		String mm = edt_newpwd.getText().toString();
		String mm2 = edt_configpwd.getText().toString();
		String qq = edt_qq.getText().toString();
		String phone = edt_phone.getText().toString();
		if(PatternUtil.checkTitle(tilte))
			params.put(C.params.title, tilte);
		if(PatternUtil.checkMm(mm,mm2)){
			params.put(C.params.mm, mm);
			params.put(C.params.mm2, getMac());
		}
		if(!StringUtil.isBlank(selectPath))
			try {
				params.put(C.params.file, new File(selectPath));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(PatternUtil.checkQq(qq))
			params.put(C.params.qq, qq);
		if(PatternUtil.checkPhone(phone))
			params.put(C.params.phone, phone);
		post(C.api.upadatePersonInfo, params, new MyCallBack(this,true){
			@Override
			public void onResult(BaseMessage message) {
				// TODO Auto-generated method stub
				super.onResult(message);
				toast(message.getMessage());
				if(message.isSuccess())
					finish();
			}
		});
	}

	public void doBack(View view) {
		// TODO Auto-generated method stub
		finish();
	}
}
