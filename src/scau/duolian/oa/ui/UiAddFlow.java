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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

/**
 * 添加流程
 */
public class UiAddFlow extends BaseUiAuth {
	private EditText title, bz;
	private Spinner lclx, deal;
	private RadioGroup lcdj;
	private RadioButton rb;
	private Uri result;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_addflow);
		init();
	}

	private void init() {
		title = (EditText) findViewById(R.id.edt_title);
		bz = (EditText) findViewById(R.id.edt_bz);
		lclx = (Spinner) findViewById(R.id.spi_rwlb);
		deal = (Spinner) findViewById(R.id.spi_deal);
	}

	public void doConfirm(View v) {
		int p = lcdj.getCheckedRadioButtonId();
		rb = (RadioButton) findViewById(p);
		String lcdj2 = "" + lcdj.indexOfChild(rb);
		AjaxParams params = new AjaxParams();
		params.put("a", "send");
		params.put("dlyid", getUser().dlyid);
		params.put("uid", getUser().uid);
		params.put("mac", getMac());
		params.put("title", title.getText().toString().trim());
		params.put("bz", bz.getText().toString().trim());
		params.put("deal", deal.getSelectedItem().toString());
		params.put("lcdj", lcdj2);
		params.put("lclx", lclx.getSelectedItem().toString());
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

	private final static int FILECHOOSER_RESULTCODE = 9;

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == FILECHOOSER_RESULTCODE)
			result = data == null || resultCode != RESULT_OK ? null : data.getData();
		super.onActivityResult(requestCode, resultCode, data);
	}

}
