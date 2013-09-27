package scau.duolian.oa.test;

import scau.duolian.oa.R;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.Toast;

public class UiTest2 extends Activity {
	TabHost tabHost;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test2);
        //获取TabHost对象
//        tabHost = getTabHost();
        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();
//        tabHost.setup();
		//新建一个newTabSpec,设置标签和图标(setIndicator),设置内容(setContent)
		tabHost.addTab(tabHost.newTabSpec("Test one").setIndicator("",getResources().getDrawable(android.R.drawable.ic_menu_call)).setContent(R.id.tabFirst));
		tabHost.addTab(tabHost.newTabSpec("Test two").setIndicator("",getResources().getDrawable(android.R.drawable.ic_menu_camera)).setContent(R.id.tabSecond));
		//设置TabHost的背景颜色
		tabHost.setBackgroundColor(Color.argb(150,22,70,150));
		//设置TabHost的背景图片资源
//		tabHost.setBackgroundResource(R.drawable.bg);
		//设置当前现实哪一个标签
		tabHost.setCurrentTab(0);   //0为标签ID
		//标签切换处理，用setOnTabChangedListener	
		tabHost.setOnTabChangedListener(new OnTabChangeListener(){
			public void onTabChanged(String tabId){
				Toast.makeText(UiTest2.this, "This is a Test!", Toast.LENGTH_LONG).show();
			}
		});
    }
}