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
 * 编辑 里程碑
 * 参数 id 里程碑 id
 *  xmid 项目id 必须要有
 */
public class UiEditMilestone extends BaseUiAuth {
	private TextView title,pro_title, rq, tx, bz,xj;

	private FinalDb db = null;
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_editmilestone);

		db.create(this);
		
		init();
	}
	private String id = null;
	private String xmid = null;
	private void init() {
		Intent intent = getIntent();
		xmid = intent.getStringExtra("xmid");
		Wdxm wdxm = db.findById(xmid, Wdxm.class);
		
		title = (TextView) findViewById(R.id.tv_title);
		pro_title = (TextView) findViewById(R.id.tv_pro_title);
		rq = (TextView) findViewById(R.id.edt_time_setting);
		tx = (TextView) findViewById(R.id.edt_remind_time);
		bz = (TextView) findViewById(R.id.edt_bz);
		xj = (TextView) findViewById(R.id.edt_xj);
		
		pro_title.setText(wdxm.title);
		rq.setText(DateUtil.getDateStr());
		tx.setText(DateUtil.getDateStr());
		
		if(!intent.hasExtra("id"))
			return ;
		
		id = intent.getStringExtra("id");
		Wdlcb wdlcb = db.findById(id, Wdlcb.class);
		rq.setText(DateUtil.longStrToStr(wdlcb.dt));
		title.setText(wdlcb.title);
		bz.setText(wdlcb.xj);
		xj.setText(wdlcb.xj);
	}


	public void doConfirm(View v) {
		String str_xj = xj.getText().toString();
		AjaxParams params = new AjaxParams();
		if(!StringUtil.isBlank(id))
			params.put(C.params.id, id);
		params.put("a", "send");
		params.put("dlyid", getUser().dlyid);
		params.put("uid", getUser().uid);
		params.put("mac", getMac());
		params.put("xmid", xmid);
		params.put("xj", str_xj);
		params.put("title", title.toString().trim());
		params.put("rq",""+ DateUtil.strToTime(rq.toString()));
		params.put("tx", ""+ DateUtil.strToTime(tx.toString()));
		post(C.api.editMilestone, params, new MyCallBack(this) {
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
	
	public void doForbidden(View view){
		AjaxParams params = new AjaxParams();
		params.put("a", "zz");
		params.put(C.params.id, id);
		params.put("dlyid", getUser().dlyid);
		params.put("uid", getUser().uid);
		params.put("mac", getMac());
		post(C.api.forbiddenLcb, params, new MyCallBack(this,true){
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
}
