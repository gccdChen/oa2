package scau.duolian.oa.base;

import scau.duolian.oa.model.User;
import android.app.Application;

public class BaseApp extends Application {
	public User owner = null;
	public boolean isLogin(){
		return owner != null;
	}
}