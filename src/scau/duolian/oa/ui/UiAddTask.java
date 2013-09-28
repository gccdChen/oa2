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

/**
 * 添加任务
 */
public class UiAddTask extends BaseUiAuth {
	private EditText title, bz, zrr, jhksrq, jhjsrq;
	private Spinner rwlb;
	private Uri result;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_addtask);
		init();
		refresh();
	}

	private void init() {
		title = (EditText) findViewById(R.id.edt_title);
		bz = (EditText) findViewById(R.id.edt_bz);
		zrr = (EditText) findViewById(R.id.edt_zrr);
		jhksrq = (EditText) findViewById(R.id.edt_jhksrq);
		jhjsrq = (EditText) findViewById(R.id.edt_jhjsrq);
		rwlb = (Spinner) findViewById(R.id.spi_rwlb);
	}

	private void refresh() {

	}

	public void doConfirm(View v) {
		AjaxParams params = new AjaxParams();
		params.put("a", "send");
		params.put("dlyid", getUser().dlyid);
		params.put("uid", getUser().uid);
		params.put("mac", getMac());
		params.put("rwlb", rwlb.getSelectedItem().toString());
		params.put("title", title.toString().trim());
		params.put("bz", bz.toString().trim());
		params.put("zrr", zrr.toString().trim());
		params.put("jhksrq", jhksrq.toString().trim());
		params.put("jhjsrq", jhjsrq.toString().trim());
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
		startActivityForResult(openFileChooser(), SELECT_FILE_CODE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SELECT_FILE_CODE)
			result = data == null || resultCode != RESULT_OK ? null : data.getData();
		super.onActivityResult(requestCode, resultCode, data);
	}

}
