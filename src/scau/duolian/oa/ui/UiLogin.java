package scau.duolian.oa.ui;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.http.AjaxParams;
import scau.duolian.oa.R;
import scau.duolian.oa.R.id;
import scau.duolian.oa.base.BaseApp;
import scau.duolian.oa.base.BaseMessage;
import scau.duolian.oa.base.BaseUi;
import scau.duolian.oa.base.C;
import scau.duolian.oa.base.MyCallBack;
import scau.duolian.oa.model.User;
import scau.duolian.oa.util.JsonUtil;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class UiLogin extends BaseUi {
	private EditText edt_account;
	private EditText edt_password;
	private CheckBox cb_remember_me;
	
	private SharedPreferences preferences = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_login);

		edt_account = (EditText) findViewById(R.id.edt_account);
		edt_password = (EditText) findViewById(R.id.edt_password);
		cb_remember_me = (CheckBox) findViewById(id.cb_remember_me);
		preferences = getPreferences(0);
		
		if(preferences.contains(C.config.uid)){
			String s_user = preferences.getString(C.config.user, "1");
			JSONObject jsonObject;
			try {
				jsonObject = new  JSONObject(s_user);
				((BaseApp) getApplication()).owner = (User) JsonUtil.json2model("User", jsonObject);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			forward(UiMessageCenter.class);
		}
	}

	/**
	 * 检查
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	private boolean check(String username, String password) {
		// TODO Auto-generated method stub
		return true;
	}

	//btn
	public void login(View view) {
		final String username = edt_account.getText().toString();
		final String password = edt_password.getText().toString();
		String mac = super.getMac();
		String dlyid = preferences.getString(C.config.dlyid, "1");

		if (check(username, password)) {
			AjaxParams params = new AjaxParams();
			params.put("a", "loginweb");
			params.put(C.params.dlyid, dlyid);
			params.put(C.params.username, username);
			params.put(C.params.password, password);
			params.put(C.params.mac, mac);
			get(C.api.login, params, new MyCallBack(this, true) {
				public void onFailure(Throwable t, String strMsg) {

					super.onFailure(t, strMsg);
					toast("为什么失败？");
				}

				@Override
				public void onResult(BaseMessage message) {
					// TODO Auto-generated method stub
					super.onResult(message);
					Log.i("result", message.getResult());
					if (message.isSuccess()) {
						String result = message.getResult();
						User user = JsonUtil.json2user(result);
						user.username = username;
						user.password = password;
						if(cb_remember_me.isChecked()){
							Editor editor = preferences.edit();
							editor.putString(C.config.user, result);
							editor.commit();
						}
						((BaseApp) getApplication()).owner = user;
						startService(new Intent("scau.duolian.oa.service.INIT"));
						forward(UiMessageCenter.class);
					} else
						toast("登陆失败,请再试~");
				}
			});
		}
	}

	public void doReg(View view) {
		// TODO Auto-generated method stub
		overlay(UiReg.class);
	}

}
