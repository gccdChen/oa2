package scau.duolian.oa.ui;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.http.AjaxParams;
import scau.duolian.oa.R;
import scau.duolian.oa.base.BaseMessage;
import scau.duolian.oa.base.BaseUiAuth;
import scau.duolian.oa.base.C;
import scau.duolian.oa.base.MyCallBack;
import scau.duolian.oa.model.Wddh;
import scau.duolian.oa.model.Wdlcb;
import scau.duolian.oa.model.Wdxm;
import scau.duolian.oa.util.DateUtil;
import scau.duolian.oa.util.StringUtil;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

		db.create(this);
		
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


	public void doConfirm(View v) {
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
}
