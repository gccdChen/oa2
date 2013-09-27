package scau.duolian.oa.ui;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import scau.duolian.oa.R;
import scau.duolian.oa.adapter.PartnerListAdapter;
import scau.duolian.oa.base.BaseUiAuth;
import scau.duolian.oa.model.Wdhb;

public class UiPartnerList extends BaseUiAuth{
	private List<Wdhb> wdhbs = new ArrayList<Wdhb>();
	private ListView lv_content = null;
	private FinalDb finalDb = null;
	private View btn_add = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_partner);
		finalDb = FinalDb.create(this);
		init();
//		refresh();
	}
	private void refresh() {
		// TODO Auto-generated method stub
		wdhbs = finalDb.findAll(Wdhb.class);
		Log.i("wdhbs refresh", ""+wdhbs.size());
		adapter.notifyDataSetChanged();
	}
	private PartnerListAdapter adapter = null;
	
	private void init(){
		wdhbs = finalDb.findAll(Wdhb.class);
		lv_content = (ListView) findViewById(R.id.lv_content);
		adapter = new PartnerListAdapter(this, wdhbs);
		lv_content.setAdapter(adapter);
		
		findViewById(R.id.btn_menu).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btn_add = findViewById(R.id.btn_add);
		if(isAdmin()){
			btn_add.setVisibility(View.VISIBLE);
		}else{
			btn_add.setVisibility(View.GONE);
		}
	}
	
	public void addPartner(View view){
		overlay(UiAddPartner.class);
	}
}
