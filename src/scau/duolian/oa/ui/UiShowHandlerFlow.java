package scau.duolian.oa.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import scau.duolian.oa.R;
import scau.duolian.oa.adapter.CommentAdapter;
import scau.duolian.oa.adapter.ShowFlowAdapter;
import scau.duolian.oa.base.BaseMessage;
import scau.duolian.oa.base.BaseUiAuth;
import scau.duolian.oa.base.C;
import scau.duolian.oa.base.MyCallBack;
import scau.duolian.oa.model.Lzls;
import scau.duolian.oa.model.Wddh;
import scau.duolian.oa.model.Wdlc;
import scau.duolian.oa.util.JsonUtil;

/**
 *  查看/编辑 流程
 * 参数 id 流程id
 * 是否编辑 boolean
 */
public class UiShowHandlerFlow extends BaseUiAuth{
	private FinalDb db = null;
	private String id = null;
	
	private ExpandableListView elv_content = null;
	private ShowFlowAdapter adapter = null;
	private ListView lv_comments = null;
	private CommentAdapter adapter2 = null;
	
	private TextView edt_bz = null;
	private String selFilePath = null;
	private RadioButton ra_reject = null;
	private RadioButton ra_end = null;
	
	
	private List<Wddh> wddhs = new ArrayList<Wddh>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_flowdetail);
		db = FinalDb.create(this);
		init();
		refresh();
	}
	private void init() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		
		Wdlc wdlc = db.findById(id, Wdlc.class);
		
		edt_bz = (TextView) findViewById(R.id.edt_bz);
		
		elv_content = (ExpandableListView) findViewById(R.id.elv_content);
		adapter = new ShowFlowAdapter(this, wdlc, db, wddhs);
		elv_content.setAdapter(adapter);
		
		lv_comments = (ListView) findViewById(R.id.lv_comments);
		adapter2 = new CommentAdapter(this, wddhs);
		lv_comments.setAdapter(adapter2);
		
		ra_reject = (RadioButton) findViewById(R.id.ra_reject);
		ra_end = (RadioButton) findViewById(R.id.ra_end);
	}
	private void refresh() {
		// TODO Auto-generated method stub
		AjaxParams params = new AjaxParams();
		params.put("a", "getdetail");
		params.put(C.params.id,id);
		params.put(C.params.dlyid,getUser().dlyid);
		params.put(C.params.uid,getUser().uid);
		params.put(C.params.mac,getMac());
		get(C.api.flowDetail, params, new MyCallBack(this,true){
			@Override
			public void onResult(BaseMessage message) {
				// TODO Auto-generated method stub
				super.onResult(message);
				toast(message.getMessage());
				if(message.isSuccess()){
					String result = message.getResult();
					JSONArray array;
					try {
						array = new JSONArray(result);
						
						JSONArray modelJsonArray = new JSONArray(result);
						wddhs = JsonUtil.json2models("Wddh", modelJsonArray);
						
						JSONArray mo = message.orgJson.getJSONArray("lzls");
						adapter.adapter1.lzlss = JsonUtil.json2models("Lzls", mo);
						Log.i("adapter.adapter1.lzlss",""+ adapter.adapter1.lzlss.size());
						update();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		});
	}
	
	public void update() {
		// TODO Auto-generated method stub
		adapter.notifiThis();
		adapter2.notifyDataSetChanged();
	}
	
	//btn
	String deal = "";
	public void doSumbit(View view){
		String yj = edt_bz.getText().toString();
		AjaxParams params = new AjaxParams();
		params.put("a", "check");
		params.put(C.params.id,id);
		params.put(C.params.dlyid,getUser().dlyid);
		params.put(C.params.uid,getUser().uid);
		params.put(C.params.mac,getMac());
		String sfjs = "1";
		String jg = "1";
		
		if(!ra_end.isChecked())
			sfjs = "0";
		if(ra_reject.isChecked())
			jg = "0";
		params.put("jg",jg);
		params.put("sfjs",sfjs);
		params.put("deal",deal);
		params.put("yj",yj);
		post(C.api.handlerFlow, params, new MyCallBack(this,true){
			@Override
			public void onResult(BaseMessage message) {
				// TODO Auto-generated method stub
				super.onResult(message);
				toast(message.getMessage());
				if(message.isSuccess())
					refresh();
			}
		});
	}
	/**
	 * 提交讨论
	 * @param view
	 */
	public void doSure(View view){
		String msg = ((TextView)findViewById(R.id.edt_msg)).getText().toString();
		AjaxParams params = new AjaxParams();
		params.put("a", "check");
		params.put(C.params.id,id);
		params.put(C.params.dlyid,getUser().dlyid);
		params.put(C.params.uid,getUser().uid);
		params.put(C.params.mac,getMac());
		params.put(C.params.bz,msg);
		post(C.api.sendFlowComment, params, new MyCallBack(this, true){
			@Override
			public void onResult(BaseMessage message) {
				// TODO Auto-generated method stub
				super.onResult(message);
				toast(message.getMessage());
				if(message.isSuccess())
					refresh();
			}
		});
	}
}
