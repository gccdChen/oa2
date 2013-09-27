package scau.duolian.oa.ui;

import java.util.Date;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.http.AjaxParams;
import scau.duolian.oa.R;
import scau.duolian.oa.base.BaseMessage;
import scau.duolian.oa.base.BaseUi;
import scau.duolian.oa.base.BaseUiAuth;
import scau.duolian.oa.base.C;
import scau.duolian.oa.base.MyCallBack;
import scau.duolian.oa.model.Wdrc;
import scau.duolian.oa.util.DateUtil;
import scau.duolian.oa.util.StringUtil;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
/**
 * 新增/编辑日程
 * 参数
 * id 日程id,若没有则是新建
 */
public class UiAddCalender extends BaseUiAuth{
	private TextView tv_title = null;
	private TextView edt_begintime = null;
	private TextView edt_remind_time = null;
	private TextView edt_bz = null;
	private FinalDb db = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_addcalender);
		db.create(this);
		init();
	}
	String id = null;
	private void init() {
		// TODO Auto-generated method stub
		tv_title = (TextView) findViewById(R.id.tv_title);
		edt_begintime = (TextView) findViewById(R.id.edt_jhksrq);
		edt_remind_time = (TextView) findViewById(R.id.edt_jhjsrq);
		edt_bz = (TextView) findViewById(R.id.edt_bz);
		
		Intent intent = getIntent();
		if(!intent.hasExtra("id"))
			return ;
		id = intent.getStringExtra("id");
		Wdrc wdrc = db.findById(id, Wdrc.class);
		
		tv_title.setText(wdrc.title);
		edt_begintime.setText(DateUtil.longStrToStr(wdrc.xdsj));
		edt_remind_time.setText(DateUtil.longStrToStr(wdrc.txsj));
		edt_bz.setText(wdrc.bz);
	}

	public void showSelDate(View view){
		Date date = new Date();
		DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
				// TODO Auto-generated method stub
				edt_begintime.setText(""+year+"-"+monthOfYear+"-"+dayOfMonth);
			}
		}, date.getYear(), date.getMonth(), date.getDate());
	}
	public void showSelDateB(View view){
		Date date = new Date();
		DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
				// TODO Auto-generated method stub
				edt_remind_time.setText(""+year+"-"+monthOfYear+"-"+dayOfMonth);
			}
		}, date.getYear(), date.getMonth(), date.getDate());
	}
	
	//btn
	public void doSelectFile(View view){
		
	}
	public void doSure(View view){
		String title = tv_title.getText().toString();
		String bz = edt_bz.getText().toString();
		String rq =""+ DateUtil.strToDate(edt_begintime.getText().toString()).getTime();
		String tx =""+ DateUtil.strToDate( edt_remind_time.getText().toString()).getTime();
		AjaxParams params = new AjaxParams();
		params.put("a", "get");
		params.put(C.params.dlyid, getUser().dlyid);
		params.put(C.params.uid, getUser().uid);
		if(!StringUtil.isBlank(id))
			params.put(C.params.id, id);
		params.put(C.params.mac, getMac());
		params.put(C.params.title, title);
		params.put(C.params.bz, bz);
		params.put("rq", rq);
		params.put("tx", tx);
		post(C.api.addCalender, params, new MyCallBack(this,true){
			@Override
			public void onResult(BaseMessage message) {
				// TODO Auto-generated method stub
				super.onResult(message);
				toast(message.getMessage());
				if(message.isSuccess()){
					finish();
				}
			}
		});
	}
}
