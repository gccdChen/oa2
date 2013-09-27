package scau.duolian.oa.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;

import scau.duolian.oa.R;
import scau.duolian.oa.adapter.ParnerAdapter;
import scau.duolian.oa.base.BaseMessage;
import scau.duolian.oa.base.BaseUiAuth;
import scau.duolian.oa.base.C;
import scau.duolian.oa.base.MyCallBack;
import scau.duolian.oa.model.Wddh;
import scau.duolian.oa.util.JsonUtil;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

/**
 * 合作伙伴 界面
 * 
 * 参数
 * 	hbid -伙伴id hbid
 */
public class UiParnerCom extends BaseUiAuth {
	private ListView lv_parnermessage = null;
	private List<Wddh> wdhbdhs = new ArrayList<Wddh>();
	private Wddh wdhbdh;
	private EditText ed;
	private String hbid = null;
	private String msg = null;
	private ParnerAdapter adapter = null;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_parnercom);

		lv_parnermessage = (ListView) findViewById(R.id.lv_parnermessage);
		init();
		refresh();
	}

	private void init() {
		adapter = new ParnerAdapter(this, wdhbdhs);
		ed = (EditText) findViewById(R.id.edt_msg);
		msg = ed.getText().toString().trim();
	}

	private void refresh() {
		Intent intent = getIntent();
		hbid = intent.getStringExtra("hbid");
		AjaxParams params = new AjaxParams();
		params.put("a", "get");
		params.put(C.params.dlyid, getUser().dlyid);
		params.put(C.params.uid, getUser().uid);
		params.put("hbid", hbid);
		params.put(C.params.mac, getMac());
		get(C.api.parnerCom, params, new MyCallBack(this) {
			public void onResult(BaseMessage message) {

				super.onResult(message);
				String result = message.getResult();
				try {
					wdhbdhs = JsonUtil.json2models("wdhbdh", new JSONArray(result));
					adapter.notifyDataSetChanged();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	public void doSend(View view) {
		AjaxParams params = new AjaxParams();
		params.put("a", "send");
		params.put("dlyid", getUser().dlyid);
		params.put("uid", getUser().uid);
		params.put("m", "9");
		params.put("id", hbid);
		params.put("mac", getMac());
		params.put("bz", msg);
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
		get(C.api.addParnerCom, params, new MyCallBack(this) {
			public void onResult(BaseMessage message) {

				super.onResult(message);
				if (message.isSuccess()) {
					toast("成功上传");
				} else {
					toast(message.getMessage());
				}
			}
		});
	}

	private final static int FILECHOOSER_RESULTCODE = 9;
	private Uri result;

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == FILECHOOSER_RESULTCODE)
			result = data == null || resultCode != RESULT_OK ? null : data.getData();
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void doSelectFile() {
		openFileChooser();
	}
}
