package scau.duolian.oa.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.http.AjaxParams;
import scau.duolian.oa.R;
import scau.duolian.oa.adapter.SelHbAdapter;
import scau.duolian.oa.base.BaseMessage;
import scau.duolian.oa.base.BaseUiAuth;
import scau.duolian.oa.base.C;
import scau.duolian.oa.base.MyCallBack;
import scau.duolian.oa.model.Wdhb;
import scau.duolian.oa.model.Wdrw;
import scau.duolian.oa.model.Wdrwlx;
import scau.duolian.oa.model.Wdxm;
import scau.duolian.oa.util.DateUtil;
import scau.duolian.oa.util.StringUtil;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * 添加/编辑任务
 * 接收参数 id 如果没有则是添加,有则是编辑
 * 	xmid 这个必须有
 */
public class UiTaskEdit extends BaseUiAuth {
	private EditText title, bz, zrr, jhksrq, jhjsrq;
	private EditText et_xj;
	private Gallery gal_file;
	private Spinner rwlb, status;
	private Uri result;
	private String id = null, xmid = null;
	private String selFilePath = null;

	private FinalDb db = null;
	
	private Wdxm wdxm = null;
	private Wdrw wdrw = null;
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_taskedit);
		db = FinalDb.create(this);
		init();
	}

	private void init() {
		rwlb = (Spinner) findViewById(R.id.spi_rwlb);
		status = (Spinner) findViewById(R.id.spi_status);
		bz = (EditText) findViewById(R.id.edt_bz);
		zrr = (EditText) findViewById(R.id.edt_zrr);
		jhksrq = (EditText) findViewById(R.id.edt_jhksrq);
		jhjsrq = (EditText) findViewById(R.id.edt_jhjsrq);
		et_xj = (EditText) findViewById(R.id.et_xj);
		gal_file = (Gallery) findViewById(R.id.gal_file);
		List<Wdrwlx> wdrwlxs = db.findAll(Wdrwlx.class,"id");
		rwlb.setAdapter(new ArrayAdapter<Wdrwlx>(this, android.R.layout.simple_list_item_1, wdrwlxs));
		status.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, STATUS));
		
		Intent intent = getIntent();
		xmid = intent.getStringExtra("xmid");
		
		if(!intent.hasExtra("id")){
			return ;
		}
		
		id = intent.getStringExtra("id");
		wdrw = db.findById(id, Wdrw.class);
		wdxm = db.findById(wdrw.xmid,Wdxm.class);
		
		((TextView)findViewById(R.id.tv_title)).setText(wdrw.title);
		((TextView)findViewById(R.id.tv_xmid)).setText(wdxm.title);
		
		jhksrq.setText(DateUtil.longStrToStr( wdrw.jhksrq));
		jhjsrq.setText(DateUtil.longStrToStr( wdrw.jhjsrq));
		bz.setText(wdrw.xj);
		et_xj.setText(wdrw.xj);
		
		Wdhb wdhb = db.findById(wdrw.author, Wdhb.class);
		zrr.setText(wdhb.name);
		String statu = wdrw.status;
		status.setSelection(Integer.parseInt(wdrw.status));
		for(int i =0 ;i<wdrwlxs.size();i++){
			if(wdrw.rwlx.equals(wdrwlxs.get(i).id)){
				rwlb.setSelection(i);
				break;
			}
		}
		
	}

	private static final String[] STATUS = {
		"未开始","10%","20%","30%","40%","50%","60%","70%","80%","90%","已完成"
	};

	public void doConfirm(View v) {
		AjaxParams params = new AjaxParams();
		if(!StringUtil.isBlank(id))
			params.put("id", id);
		params.put("a", "send");
		params.put("dlyid", getUser().dlyid);
		params.put("uid", getUser().uid);
		params.put("mac", getMac());
		params.put("xmid", xmid);
		params.put("rwlb", rwlb.getSelectedItem().toString());
		params.put("title", title.toString().trim());
		params.put("bz", bz.toString().trim());
		params.put("zrr", zrr.toString().trim());
		params.put("jhksrq", jhksrq.toString().trim());
		params.put("jhjsrq", jhjsrq.toString().trim());
		params.put("status", status.getSelectedItem().toString());
		try {
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
		}
		get(C.api.addTask, params, new MyCallBack(this) {
			public void onResult(BaseMessage message) {
				if (message.isSuccess()) {
					toast("成功");
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
		openFileChooser();
	}


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Activity.RESULT_OK){
			switch (requestCode) {
			case SELECT_FILE_CODE:
				result = data == null  ? null : data.getData();
				break;
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	//btn
	public void doForbidden(View view){
		AjaxParams params = new AjaxParams();
		params.put("a", "zz");
		params.put(C.params.dlyid,getUser().dlyid);
		params.put(C.params.uid,getUser().uid);
		params.put(C.params.id,wdrw.id);
		params.put(C.params.mac,getMac());
		post(C.api.forbiddenTask, params, new MyCallBack(this, true){
			@Override
			public void onResult(BaseMessage message) {
				// TODO Auto-generated method stub
				super.onResult(message);
				toast(message.getMessage());
			}
		});
	}
	
	public void selFile(View view){
		startActivityForResult(openFileChooser(), SELECT_FILE_CODE);
	}
	public void showSelDate(View view){
		Date date = new Date();
		DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
				// TODO Auto-generated method stub
				jhksrq.setText(""+year+"-"+monthOfYear+"-"+dayOfMonth);
			}
		}, date.getYear(), date.getMonth(), date.getDate());
	}
	public void showSelDateB(View view){
		Date date = new Date();
		DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
				// TODO Auto-generated method stub
				jhjsrq.setText(""+year+"-"+monthOfYear+"-"+dayOfMonth);
			}
		}, date.getYear(), date.getMonth(), date.getDate());
	}
	
	Dialog dialog = null;
	public void showSelHb(View view){
		dialog = new Dialog(this);
		ListView listView = new ListView(this);
		List<Wdhb> wdhbs = db.findAll(Wdhb.class);
		SelHbAdapter adapter = new SelHbAdapter(this, wdhbs, zrr,dialog);
		listView.setAdapter(adapter);
		dialog.setContentView(listView);
		dialog.show();
		
	}
}
