package scau.duolian.oa.base;

import scau.duolian.oa.model.User;
import scau.duolian.oa.ui.UiLogin;
import android.os.Bundle;
import android.view.View;

public class BaseUiAuth extends BaseUi {
	View menu = null;
	boolean showingMenu = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (!isLogin()) {
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

	public User getUser() {
		// TODO Auto-generated method stub
		return ((BaseApp) getApplication()).owner;
	}

	public void doBack(View view) {
		// TODO Auto-generated method stub
		finish();
	}

	public void showMenu(View view) {
		// TODO Auto-generated method stub
		if (showingMenu)
			menu.setVisibility(View.GONE);
		else
			menu.setVisibility(View.VISIBLE);
		showingMenu = !showingMenu;
	}
}
