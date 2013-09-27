package scau.duolian.oa.ui;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import scau.duolian.oa.R;
import scau.duolian.oa.adapter.MessageAdapter;
import scau.duolian.oa.base.BaseUiAuth;
import scau.duolian.oa.model.Message;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

public class UiMessageCenter extends BaseUiAuth{
	private static List<Message> messages = new ArrayList<Message>();
	private static String keyword = null;
	/**
	 * 搜索是否开启
	 */
	private static boolean filter = false;
	private ListView lv_messsages = null;
	private EditText edt_search = null;
	
	private static MessageAdapter adapter = null;
	
	public final static int REFRESH_CODE = 1; 
	public static Handler handler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
				case REFRESH_CODE:
					refresh();
					break;
				default:
					break;
			}
		}
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_message_center);
		initCon();
		initData();
	}
	
	static FinalDb db = null;
	
	private void initData() {
		db = FinalDb.create(this);
		refresh();
	}
	View menu = null;
	private void initCon() {
		// TODO Auto-generated method stub
		lv_messsages = (ListView) findViewById(R.id.lv_messsages);
		edt_search = (EditText) findViewById(R.id.edt_search);
		menu = findViewById(R.id.menu);
		adapter = new MessageAdapter(this, messages);
		lv_messsages.setAdapter(adapter);
	}

	private static void refresh(){
		messages = db.findAll(Message.class, "dt");
		adapter.messages = messages;
		adapter.keyword = keyword;
		adapter.filter = filter;
		adapter.notifyDataSetChanged();
	}
	
	///btn
	public void doSearch(View view) {
		// TODO Auto-generated method stub
		keyword = edt_search.getText().toString();
		filter = true;
		refresh();
	}
	private boolean showingMenu = false ;
	public void showMenu(View view) {
		// TODO Auto-generated method stub
		if(showingMenu)
			menu.setVisibility(View.GONE);
		else
			menu.setVisibility(View.VISIBLE);
		showingMenu = !showingMenu;
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(menu!=null){
			showingMenu = false;
			menu.setVisibility(View.GONE);
		}
	}
}
