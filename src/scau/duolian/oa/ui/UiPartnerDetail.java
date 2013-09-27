package scau.duolian.oa.ui;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.http.AjaxParams;
import scau.duolian.oa.R;
import scau.duolian.oa.base.BaseMessage;
import scau.duolian.oa.base.BaseUiAuth;
import scau.duolian.oa.base.C;
import scau.duolian.oa.base.MyCallBack;
import scau.duolian.oa.model.Wdbm;
import scau.duolian.oa.model.Wdhb;
import scau.duolian.oa.util.DeviceHelper;
import scau.duolian.oa.util.ImageLoader;
import scau.duolian.oa.util.StringUtil;
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
	private ImageLoader imageLoader = null;
	private Wdbm wdbm;
	private Wdbm org = null;
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
		wdbm = db.findById(wdhb.dept,Wdbm.class);
		// 获取顶层组织
		
		if(wdbm != null && wdbm.parentid != null){
			Wdbm temp = wdbm;
			do{
				temp = db.findById(wdbm.parentid, Wdbm.class);
				org = temp;
			}while(org!=null && org.parentid != null);
		}
		
		imageLoader = new ImageLoader();
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
		if(wdbm != null){
			if(org == null)
				org = wdbm;
			tv_org.setText(org.title);
			tv_department.setText(wdbm.title);
		}
		tv_job.setText(wdhb.job);
		tv_email.setText(wdhb.email);
		tv_phone.setText(wdhb.mobile);
		fb.display(iv_face, wdhb.photo);
		
		if(!StringUtil.isBlank(wdhb.photo))
			imageLoader.loadImage(wdhb.photo, iv_face);
	}
	
	public void dail(View view){
		DeviceHelper.dial(this, wdhb.mobile);
	}
	public void sendEmail(View view){
		DeviceHelper.sendEmail(this, wdhb.email);
	}
	public void sendMsg(View view){
		Bundle bundle = new Bundle();
		bundle.putString("hbid", wdhb.id);
		overlay(UiParnerCom.class,bundle);
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
