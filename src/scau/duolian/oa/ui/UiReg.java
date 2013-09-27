package scau.duolian.oa.ui;

import java.util.regex.Pattern;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.http.AjaxParams;
import scau.duolian.oa.R;
import scau.duolian.oa.base.BaseMessage;
import scau.duolian.oa.base.BaseUi;
import scau.duolian.oa.base.C;
import scau.duolian.oa.base.MyCallBack;
import scau.duolian.oa.util.PatternUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class UiReg extends BaseUi {
	private EditText edt_account;
	private EditText edt_password;
	private EditText edt_affirm_password;
	private EditText edt_verifi_code;
	private ImageView iv_verifi_code;
	private FinalBitmap fb = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_reg);
		
		fb = FinalBitmap.create(this);
		initCon();
		initData();
	}
	String verifi_code_url = "http://www.duolia.com/yun/validatecode.jsp?charNum=4&mac=";
	private void initData() {
		// TODO Auto-generated method stub
		fb.display(iv_verifi_code, verifi_code_url + getMac());
	}
	private void initCon() {
		// TODO Auto-generated method stub
		edt_account = (EditText) findViewById(R.id.edt_account);
		edt_password = (EditText) findViewById(R.id.edt_password);
		edt_affirm_password = (EditText) findViewById(R.id.edt_affirm_password);
		edt_verifi_code = (EditText) findViewById(R.id.edt_verifi_code);
		iv_verifi_code = (ImageView) findViewById(R.id.iv_verifi_code);
	}
	
	//btn 
	public void doReg(View view) {
		// TODO Auto-generated method stub
		String regName = edt_account.getText().toString();
		String password = edt_password.getText().toString();
		String password2 = edt_affirm_password.getText().toString();
		String validateCode = edt_verifi_code.getText().toString();
		AjaxParams params = new AjaxParams();
		params.put("a","reg");
		params.put("dlyid","1");
		params.put(C.params.regName,regName);
		params.put(C.params.password,password);
		params.put(C.params.password2,password2);
		params.put(C.params.validateCode,validateCode);
		post(C.api.reg, params, new MyCallBack(this,true){
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
	public void toLogin(View view) {
		// TODO Auto-generated method stub
		forward(UiLogin.class);
	}
}
