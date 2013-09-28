package scau.duolian.oa.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import scau.duolian.oa.R;
import scau.duolian.oa.adapter.CommentAdapter;
import scau.duolian.oa.base.BaseMessage;
import scau.duolian.oa.base.BaseUiAuth;
import scau.duolian.oa.base.C;
import scau.duolian.oa.base.MyCallBack;
import scau.duolian.oa.model.Wddh;
import scau.duolian.oa.model.Wdrc;
import scau.duolian.oa.util.DateUtil;
import scau.duolian.oa.util.JsonUtil;
/**
 *	伙伴的日程详情 
 * 参数
 * 日程 id
 * 伙伴 hbid
 */
public class UiCalenderDetail extends BaseUiAuth{
	private List<Wddh> wddhs = new ArrayList<Wddh>();
	private TextView tv_title = null;
	private TextView tv_create_time = null;
	private TextView tv_remind_time = null;
	private TextView tv_content = null;
	private TextView edt_msg = null;
	private ListView lv_comment = null;
	private FinalDb db = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_calender_detail);
		db = FinalDb.create(this);
		init();
		refresh();
	}

	String id = null;
	String hbid = null;
	private Wdrc wdrc = null;
	private CommentAdapter adapter = null;
	private void init() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		id = intent.getStringExtra("hbid");
		hbid = intent.getStringExtra("id");
		
		wdrc = db.findById(id, Wdrc.class);
		
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_content = (TextView) findViewById(R.id.tv_content);
		tv_remind_time = (TextView) findViewById(R.id.tv_remind_time);
		tv_create_time = (TextView) findViewById(R.id.tv_create_time);
		lv_comment = (ListView) findViewById(R.id.lv_comment);
		adapter = new CommentAdapter(this, wddhs);
		lv_comment.setAdapter(adapter);
		
		tv_title.setText(wdrc.title);
		tv_content.setText(wdrc.bz);
		tv_create_time.setText(DateUtil.longStrToStr(wdrc.xdsj));
		tv_remind_time.setText(DateUtil.longStrToStr(wdrc.txsj));
	}
	private void refresh() {
		// TODO Auto-generated method stub
		AjaxParams params= new AjaxParams();
		params.put("a", "getdetail");
		params.put(C.params.id,id);
		params.put("hbid",hbid);
		params.put(C.params.dlyid,getUser().dlyid);
		params.put(C.params.uid,getUser().uid);
		params.put(C.params.mac,getMac());
		get(C.api.calComment,params, new MyCallBack(this, true){
			@Override
			public void onResult(BaseMessage message) {
				// TODO Auto-generated method stub
				super.onResult(message);
				toast(message.getMessage());
				if(message.isSuccess()){
					String result = message.getResult();
					JSONArray modelJsonArray;
					try {
						modelJsonArray = new JSONArray(result);
						wddhs = JsonUtil.json2models("Wddh", modelJsonArray);
						update();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					
				}
			}
		});
	}
	public void update(){
		adapter.notifyDataSetChanged();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
	}
	
	//btn
	public void doSend(View view){
		String content = edt_msg.getText().toString();
		AjaxParams params = new AjaxParams();
		params.put("a", "getdetail");
		params.put(C.params.id,wdrc.id);
		params.put(C.params.dlyid,getUser().dlyid);
		params.put(C.params.uid,getUser().uid);
		params.put("bz",content);
		params.put(C.params.mac,getMac());
		post(C.api.sendCalComment, params, new MyCallBack(this,true){
			@Override
			public void onResult(BaseMessage message) {
				// TODO Auto-generated method stub
				super.onResult(message);
				toast(message.getMessage());
				if(message.isSuccess()){
					refresh();
				}
			}
		});
	}
	
	public void doSelectFile(){
		startActivityForResult(openFileChooser(), SELECT_FILE_CODE);
	}
}
