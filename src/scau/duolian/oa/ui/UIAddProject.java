package scau.duolian.oa.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.http.AjaxParams;
import scau.duolian.oa.R;
import scau.duolian.oa.base.BaseMessage;
import scau.duolian.oa.base.BaseUiAuth;
import scau.duolian.oa.base.C;
import scau.duolian.oa.base.MyCallBack;
import scau.duolian.oa.model.Wdhb;
import scau.duolian.oa.model.Wdxm;
import scau.duolian.oa.util.DateUtil;
import scau.duolian.oa.util.StringUtil;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * 新建/编辑 项目 参数 id 项目id 若没有则是新建
 */
public class UIAddProject extends BaseUiAuth {
	private TextView edt_title;
	private EditText jhksrq, jhjsrq, bz;
	private EditText edt_xj;
	private EditText fzr, cy, gcy;
	private Uri result;
	private FinalDb db = null;
	private String selfzrid, selcyids, selgcyids;
	private Spinner spi_xmlb;
	private Spinner spi_status;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_addproject);
		db = FinalDb.create(this);
		init();
	}

	private String id = null;
	private List<Wdhb> wdhbs = null;

	private void init() {
		edt_title = (EditText) findViewById(R.id.edt_title);
		jhksrq = (EditText) findViewById(R.id.edt_jhksrq);
		jhjsrq = (EditText) findViewById(R.id.edt_jhjsrq);
		bz = (EditText) findViewById(R.id.edt_bz);
		edt_xj = (EditText) findViewById(R.id.edt_xj);

		fzr = (EditText) findViewById(R.id.et_fzr);
		cy = (EditText) findViewById(R.id.et_cy);
		gcy = (EditText) findViewById(R.id.et_gcy);

		spi_xmlb = (Spinner) findViewById(R.id.spi_xmlb);
		spi_status = (Spinner) findViewById(R.id.spi_status);

		spi_xmlb.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, C.array.proType));
		spi_status.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, C.array.proStatus));

		wdhbs = db.findAll(Wdhb.class);
		// 编辑
		Intent intent = getIntent();
		if (!intent.hasExtra("id"))
			return;
		
		((TextView) findViewById(R.id.tv_title)).setText("编辑项目");
		
		id = intent.getStringExtra("id");
		Wdxm wdxm = db.findById(id, Wdxm.class);
		edt_title.setText(wdxm.title);
		bz.setText(wdxm.bz);
		jhksrq.setText(DateUtil.longStrToStr(wdxm.ksrq));
		jhjsrq.setText(DateUtil.longStrToStr(wdxm.jsrq));
		edt_xj.setText(DateUtil.longStrToStr(wdxm.xj));

		if (!StringUtil.isBlank(wdxm.author)) {
			Wdhb author = db.findById(wdxm.author, Wdhb.class);
			fzr.setText(author.name);
			selfzrid = wdxm.author;
		}
		if (!StringUtil.isBlank(wdxm.member)) {
			String m = wdxm.member.replaceAll(",", "','");
			List<Wdhb> members = db.findAllByWhere(Wdhb.class, " in ('" + m + "')");
			StringBuffer tx_mem = new StringBuffer();
			for (int i = 0; i < members.size(); i++) {
				tx_mem.append(members.get(i) + ",");
			}
			cy.setText(tx_mem.subSequence(0, tx_mem.length() - 1));
			selcyids = wdxm.member;
		}
		if (!StringUtil.isBlank(wdxm.visitor)) {
			String m = wdxm.visitor.replaceAll(",", "','");
			List<Wdhb> members = db.findAllByWhere(Wdhb.class, " in ('" + m + "')");
			StringBuffer tx_mem = new StringBuffer();
			for (int i = 0; i < members.size(); i++) {
				tx_mem.append(members.get(i) + ",");
			}
			gcy.setText(tx_mem.subSequence(0, tx_mem.length() - 1));
			selgcyids = wdxm.visitor;
		}

	}

	public void doConfirm(View v) {
		AjaxParams params = new AjaxParams();
		params.put("a", "send");
		if (!StringUtil.isBlank(id))
			params.put(C.params.id, id);
		params.put("dlyid", getUser().dlyid);
		params.put("uid", getUser().uid);
		params.put("mac", getMac());
		params.put("title", edt_title.getText().toString().trim());
		params.put("bz", bz.getText().toString().trim());
		params.put("jhksrq", "" + DateUtil.strToTime(jhksrq.getText().toString()).getTime());
		params.put("jhjsrq", "" + DateUtil.strToTime(jhjsrq.getText().toString()).getTime());

		params.put("fzr", fzr.getText().toString());
		params.put("cy", selcyids);
		params.put("gcy", selgcyids);

		params.put("xmlb", "" + spi_xmlb.getSelectedItemPosition());
		params.put("status", "" + spi_status.getSelectedItemPosition());

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
		get(C.api.addProject, params, new MyCallBack(this) {
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
		if (requestCode == SELECT_FILE_CODE)
			result = data == null || resultCode != RESULT_OK ? null : data.getData();
		super.onActivityResult(requestCode, resultCode, data);
	}

	// btn
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

	Dialog dialog = null;

	public void selPartner(View view) {
		if (dialog == null) {
			dialog = new Dialog(this,R.style.scau_dialog);
			View v = LayoutInflater.from(this).inflate(R.layout.dialog_sel_hb, null);
			dialog.setContentView(v);

			ViewGroup layout = (ViewGroup) v.findViewById(R.id.ll_1);

			TextView tv_name = null;
			for(int i =0 ;i<wdhbs.size();i++){
				final Wdhb wdhb = wdhbs.get(i);
				tv_name = new TextView(this);
				tv_name.setText(wdhb.name);
				tv_name.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						fzr.setText(wdhb.name);
						selfzrid = wdhb.id;
						if(dialog != null)
							dialog.dismiss();
					}
				});
				layout.addView(tv_name);
			}
		}
		dialog.show();
	}

	Dialog dialog2 = null;
	boolean selcys = false;

	Set<Wdhb> sels_cys = new HashSet<Wdhb>();
	Set<Wdhb> sels_gcys = new HashSet<Wdhb>();
	public void selPartners(View view) {
		if (dialog2 == null) {
			dialog2 = new Dialog(this,R.style.scau_dialog);
			View v = LayoutInflater.from(this).inflate(R.layout.dialog_sel_hb, null);
			dialog2.setContentView(v);
			ViewGroup layout = (ViewGroup) v.findViewById(R.id.ll_1);

			if (view.getId() == R.id.btn_sel_members) 
				selcys = true;
			else if(view.getId() == R.id.btn_sel_gcys)
				selcys = false;
			for(int i =0 ;i<wdhbs.size();i++){
				final Wdhb wdhb = wdhbs.get(i);
				TextView tv_name = new TextView(this);
				tv_name.setText(wdhb.name);
				tv_name.setOnClickListener(new MyOnClickListenter(tv_name, wdhb, selcys));
				if(selcys && sels_cys.contains(wdhb)){
					tv_name.setBackgroundColor(0xFF0000FF);
				}else if(!selcys && sels_gcys.contains(wdhb)){
					tv_name.setBackgroundColor(0xFF0000FF);
				}
				layout.addView(tv_name);
			}

			Button btn_sure = new Button(this);
			btn_sure.setText("确认");
			btn_sure.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					StringBuffer selnames = new StringBuffer();
					if (selcys){
						for(Wdhb wdhb : sels_cys){
							selcyids += wdhb.id+",";
							selnames.append( wdhb.name+",");
						}
					}else{
						for(Wdhb wdhb : sels_gcys){
							selgcyids += wdhb.id+",";
							selnames.append( wdhb.name+",");
						}
					}
					if(selnames.length()>2){
						if (!selcys)
							gcy.setText(selnames.substring(0, selnames.length()-1));
						else
							cy.setText(selnames.substring(0, selnames.length()-1));
					}else{
						if (!selcys)
							gcy.setText("");
						else
							cy.setText("");
					}
					if (dialog2 != null){
						dialog2.dismiss();
						dialog2 = null;
					}
				}
			});
			layout.addView(btn_sure);
		}
		dialog2.show();
	}
	
	class MyOnClickListenter implements View.OnClickListener{
		private TextView view = null;
		private Wdhb wdhb = null;
		private boolean selcys = true;

		public MyOnClickListenter(TextView view, Wdhb wdhb, boolean selcys) {
			super();
			this.view = view;
			this.wdhb = wdhb;
			this.selcys = selcys;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Set<Wdhb> sels = selcys?sels_cys:sels_gcys;
			if(sels.contains(wdhb)){
				view.setBackgroundColor(0xFFFFFFFF);
				sels.remove(wdhb);
			}else{
				view.setBackgroundColor(0xFF0000FF);
				sels.add(wdhb);
			}
		}
		
	}

	public void doForbidden(View view) {
		AjaxParams params = new AjaxParams();
		params.put("a", "zz");
		params.put(C.params.id, id);
		params.put("dlyid", getUser().dlyid);
		params.put("uid", getUser().uid);
		params.put("mac", getMac());
		post(C.api.forbiddenPro, params, new MyCallBack(this, true) {
			@Override
			public void onResult(BaseMessage message) {
				// TODO Auto-generated method stub
				super.onResult(message);
				toast(message.getMessage());
				if (message.isSuccess())
					finish();
			}
		});
	}
}
