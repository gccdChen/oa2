package scau.duolian.oa.ui;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;

import scau.duolian.oa.R;
import scau.duolian.oa.adapter.CalenderNoteAdapter;
import scau.duolian.oa.base.BaseMessage;
import scau.duolian.oa.base.BaseUiAuth;
import scau.duolian.oa.base.C;
import scau.duolian.oa.base.MyCallBack;
import scau.duolian.oa.model.CalenderNote;
import scau.duolian.oa.model.Wdrc;
import scau.duolian.oa.util.ChangeUtil;
import scau.duolian.oa.util.JsonUtil;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;
/**
 * 查看伙伴的日程
 * 参数
 * id 伙伴id
 *
 */
public class UiPartnerCalender extends BaseUiAuth{
	
	private CalenderNoteAdapter adapter = null;
	private ExpandableListView expandableListView = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_calendar);
		initCon();
	}
	
	private void initCon() {
		// TODO Auto-generated method stub
		expandableListView = (ExpandableListView) findViewById(R.id.content);
		
		Intent intent = getIntent();
		String id = intent.getStringExtra("id");
		AjaxParams params = new AjaxParams();
		params.put("a","get");
		params.put("hbid", id);
		params.put(C.params.dlyid,getUser().dlyid);
		params.put(C.params.uid,getUser().uid);
		params.put(C.params.mac,getMac());
		get(C.api.partnerCalender,params, new MyCallBack(this, true){
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
						wdrcs = JsonUtil.json2models("Wdrc", modelJsonArray);
						update();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		});
		adapter = new CalenderNoteAdapter(this, calenderNotes);
	}
	protected void update() {
		// TODO Auto-generated method stub
		if(wdrcs != null){
			calenderNotes = getCalenderNotes();
			adapter.notifyDataSetChanged();
		}
	}
	List<CalenderNote> calenderNotes = new ArrayList<CalenderNote>();
	List<Wdrc> wdrcs = null;

	private List<CalenderNote> getCalenderNotes() {
		// TODO Auto-generated method stub
		return ChangeUtil.Wdrcs2Cals(wdrcs);
	}
}
