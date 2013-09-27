package scau.duolian.oa.ui;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.http.AjaxParams;
import scau.duolian.oa.R;
import scau.duolian.oa.base.BaseMessage;
import scau.duolian.oa.base.BaseUiAuth;
import scau.duolian.oa.base.C;
import scau.duolian.oa.base.MyCallBack;
import scau.duolian.oa.model.Wdhb;
import scau.duolian.oa.util.DeviceHelper;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class UiPartnerDetail extends BaseUiAuth{
	FinalDb db = null ;
	FinalBitmap fb = null; 
	private ImageView iv_face = null;
	private TextView tv_name = null;
	private TextView tv_org = null;
	private TextView tv_department = null;
	private TextView tv_job = null;
	private TextView tv_email = null;
	private TextView tv_phone = null;
	private Wdhb wdhb = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_parnerdetail);
		db = FinalDb.create(this);
		fb = FinalBitmap.create(this);
		Intent intent = getIntent();
		String hbid = intent.getStringExtra(C.params.hbid); 
		wdhb = db.findById(hbid, Wdhb.class);
		initCon();
		initData();
	}
	
	private void initCon() {
		// TODO Auto-generated method stub
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_org = (TextView) findViewById(R.id.tv_org);
		tv_department = (TextView) findViewById(R.id.tv_department);
		tv_job = (TextView) findViewById(R.id.tv_job);
		tv_email = (TextView) findViewById(R.id.tv_email);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		iv_face = (ImageView) findViewById(R.id.iv_face);
	}

	private void initData(){
		
		tv_name.setText(wdhb.name);
		tv_org.setText(wdhb.dept);
		tv_department.setText(wdhb.dept);
		tv_job.setText(wdhb.job);
		tv_email.setText(wdhb.email);
		tv_phone.setText(wdhb.mobile);
		fb.display(iv_face, wdhb.photo);
	}
	
	public void dail(View view){
		DeviceHelper.dial(this, wdhb.mobile);
	}
	public void sendEmail(View view){
		DeviceHelper.sendEmail(this, wdhb.email);
	}
	public void sendMsg(View view){
		overlay(UiParnerCom.class);
	}
	public void edit(View view){
		overlay(UiPersonSetting.class);
	}
	public void forbidden(View view){
		AjaxParams params = new AjaxParams();
		params.put("a", "jyhb");
		params.put(C.params.dlyid, getUser().dlyid);
		params.put(C.params.uid, getUser().uid);
		params.put(C.params.hbid, wdhb.id);
		params.put(C.params.mac, getMac());
		
		post(C.api.forbiddenParner, params, new MyCallBack(this, true){
			@Override
			public void onResult(BaseMessage message) {
				// TODO Auto-generated method stub
				super.onResult(message);
				toast(message.getMessage());
			}
			@Override
			public void onFailure(Throwable t, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, strMsg);
				toast(strMsg);
			}
		});
	}
	
	public void showCalender(View view){
		Bundle params = new Bundle();
		params.putString("id", wdhb.id);
		overlay(UiPartnerCalender.class, params);
	}
}
