package scau.duolian.oa.ui;

import java.io.File;
import java.io.FileNotFoundException;

import net.tsz.afinal.http.AjaxParams;
import scau.duolian.oa.R;
import scau.duolian.oa.base.BaseMessage;
import scau.duolian.oa.base.BaseUiAuth;
import scau.duolian.oa.base.C;
import scau.duolian.oa.base.MyCallBack;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class UIProjectEdit extends BaseUiAuth {
	private EditText title, jhksrq, jhjsrq, bz, xj;
	private Spinner fzr, cy, gcy, xmlb, status;
	private Uri result;
	private String id;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		title = (EditText) findViewById(R.id.edt_title);
		jhksrq = (EditText) findViewById(R.id.edt_jhksrq);
		jhjsrq = (EditText) findViewById(R.id.edt_jhjsrq);
		bz = (EditText) findViewById(R.id.edt_bz);
		xj = (EditText) findViewById(R.id.edt_xj);
		fzr = (Spinner) findViewById(R.id.et_fzr);
		cy = (Spinner) findViewById(R.id.et_cy);
		gcy = (Spinner) findViewById(R.id.et_gcy);
		xmlb = (Spinner) findViewById(R.id.spi_xmlb);
		status = (Spinner) findViewById(R.id.spi_status);
	}

	public void doConfirm(View v) {
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		AjaxParams params = new AjaxParams();
		params.put("a", "send");
		params.put("dlyid", getUser().dlyid);
		params.put("uid", getUser().uid);
		params.put("id", id);
		params.put("mac", getMac());
		params.put("title", title.getText().toString().trim());
		params.put("bz", bz.getText().toString().trim());
		params.put("jhksrq", jhksrq.getText().toString().trim());
		params.put("jhjsrq", jhjsrq.getText().toString().trim());

		params.put("fzr", fzr.getSelectedItem().toString());
		params.put("cy", cy.getSelectedItem().toString());
		params.put("gcy", gcy.getSelectedItem().toString());
		params.put("xmlb", xmlb.getSelectedItem().toString());
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

}
