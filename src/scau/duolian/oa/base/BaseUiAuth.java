package scau.duolian.oa.base;

import scau.duolian.oa.model.User;
import scau.duolian.oa.ui.UiLogin;
import scau.duolian.oa.util.JsonUtil;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class BaseUiAuth extends BaseUi {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (!isLogin()) {
			SharedPreferences preferences = getPreferences(0);
			if(preferences.contains(C.config.user)){
				String result = preferences.getString(C.config.user, "");
				setUser(JsonUtil.json2user(result));
			}else
				forward(UiLogin.class);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (menu != null) {
			showingMenu = false;
			menu.setVisibility(View.GONE);
		}
	}

	public boolean isAdmin(){
		return getUser().isIsadmin();
	}
	
	public User getUser() {
		// TODO Auto-generated method stub
		return ((BaseApp) getApplication()).owner;
	}
	
	private void setUser(User user){
		((BaseApp) getApplication()).owner = user;
	}

	public void doBack(View view) {
		// TODO Auto-generated method stub
		finish();
	}
}
