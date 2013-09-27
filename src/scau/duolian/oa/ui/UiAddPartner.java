package scau.duolian.oa.ui;

import java.util.List;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.http.AjaxParams;
import scau.duolian.oa.R;
import scau.duolian.oa.adapter.DepartmentAdapter;
import scau.duolian.oa.base.BaseMessage;
import scau.duolian.oa.base.BaseUiAuth;
import scau.duolian.oa.base.C;
import scau.duolian.oa.base.MyCallBack;
import scau.duolian.oa.model.Wdbm;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class UiAddPartner extends BaseUiAuth{
	private FinalDb db = null;
	private EditText edt_email = null;
	private Spinner sp_department = null;
	private EditText edt_content = null;
	private String sel_dept_id = "1";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_addparner);
		db = FinalDb.create(this);
		initCon();
		initdata();
	}
	List<Wdbm> wdbms = null;
	private void initdata() {
		// TODO Auto-generated method stub
		wdbms = db.findAll(Wdbm.class);
		sp_department.setAdapter(new DepartmentAdapter(this, wdbms));
	}

	private void initCon() {
		// TODO Auto-generated method stub
		sp_department = (Spinner) findViewById(R.id.spi_department);
		edt_email = (EditText) findViewById(R.id.tv_email);
		edt_content = (EditText) findViewById(R.id.tv_ds);
	}
	
	public void add(View view){
		String email = edt_email.getText().toString();
		String content = edt_content.getText().toString();
		AjaxParams params = new AjaxParams();
		params.put("a","yqhb");
		params.put(C.params.dlyid, getUser().dlyid);
		params.put(C.params.dlyid, getUser().dlyid);
		params.put(C.params.uid, getUser().uid);
		params.put(C.params.mac, getMac());
		params.put("email", email);
		params.put("deptid", sel_dept_id);
		params.put("bz", content);
		post(C.api.addPartner, params, new MyCallBack(this, showLoadBar){
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
