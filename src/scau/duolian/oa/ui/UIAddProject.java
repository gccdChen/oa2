package scau.duolian.oa.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.http.AjaxParams;
import scau.duolian.oa.R;
import scau.duolian.oa.adapter.MutiSelHbAdapter;
import scau.duolian.oa.adapter.SelHbAdapter;
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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
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
		params.put("cy", cy.getText().toString());
		params.put("gcy", gcy.getText().toString());

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

	private final static int FILECHOOSER_RESULTCODE = 9;

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == FILECHOOSER_RESULTCODE)
			result = data == null || resultCode != RESULT_OK ? null : data.getData();
		super.onActivityResult(requestCode, resultCode, data);
	}

	// btn
	public void showSelDate(View view) {
		Date date = new Date();
		DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				// TODO Auto-generated method stub
				jhksrq.setText("" + year + "-" + monthOfYear + "-" + dayOfMonth);
			}
		}, date.getYear(), date.getMonth(), date.getDate());
	}

	public void showSelDateB(View view) {
		Date date = new Date();
		DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				// TODO Auto-generated method stub
				jhjsrq.setText("" + year + "-" + monthOfYear + "-" + dayOfMonth);
			}
		}, date.getYear(), date.getMonth(), date.getDate());
	}

	Dialog dialog = null;

	public void selPartner(View view) {
		if (dialog == null) {
			dialog = new Dialog(this);

			LinearLayout layout = new LinearLayout(this);
			layout.setOrientation(LinearLayout.VERTICAL);
			LayoutParams params = (LayoutParams) layout.getLayoutParams();
			params.height = 200;
			layout.setLayoutParams(params);

			ListView listView = new ListView(this);
			SelHbAdapter adapter = new SelHbAdapter(this, wdhbs, (TextView) view, dialog);
			adapter.selId = selfzrid;
			listView.setAdapter(adapter);
		}
		dialog.show();
	}

	Dialog dialog2 = null;
	MutiSelHbAdapter adapter2 = null;
	boolean selgcys = false;

	public void selPartners(View view) {
		if (dialog2 == null) {
			dialog2 = new Dialog(this);

			LinearLayout layout = new LinearLayout(this);
			layout.setOrientation(LinearLayout.VERTICAL);
			LayoutParams params = (LayoutParams) layout.getLayoutParams();
			params.height = 200;
			layout.setLayoutParams(params);

			ListView listView = new ListView(this);

			if (view.getId() == R.id.btn_sel_gcys) {
				adapter2 = new MutiSelHbAdapter(this, wdhbs);
				selgcys = true;
			} else {
				adapter2 = new MutiSelHbAdapter(this, wdhbs);
			}
			listView.setAdapter(adapter2);

			layout.addView(listView);

			Button btn_sure = new Button(this);
			btn_sure.setText("确认");
			btn_sure.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					StringBuffer selnames = new StringBuffer();
					List<Wdhb> list = adapter2.sels;
					if (selgcys)
						selgcyids = "";
					else
						selcyids = "";

					for (int i = 0; i < list.size(); i++) {
						if (i == list.size() - 1) {
							selnames.append(list.get(i).name);
							if (selgcys)
								selgcyids += list.get(i).id;
							else
								selcyids += list.get(i).id;
						} else {
							selnames.append(list.get(i).name + ",");
							if (selgcys)
								selgcyids += list.get(i).id + ",";
							else
								selcyids += list.get(i).id + ",";
						}
					}

					if (selgcys)
						gcy.setText(selnames);
					else
						cy.setText(selnames);

					if (dialog2 != null)
						dialog2.dismiss();
				}
			});
		}
		dialog.show();
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
