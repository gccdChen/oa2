package scau.duolian.oa.ui;

import java.util.List;

import net.tsz.afinal.FinalDb;
import scau.duolian.oa.R;
import scau.duolian.oa.adapter.FlowAdapter;
import scau.duolian.oa.base.BaseUiAuth;
import scau.duolian.oa.model.Wdlc;
import scau.duolian.oa.model.Wdlclx;
import scau.duolian.oa.widget.MenuWidget;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class UiFlow extends BaseUiAuth{
	private List<Wdlc> wdlcs = null;
	private List<Wdlclx> wdlclxs = null;
	private FinalDb db = null;
	private ListView lv_flow = null;
	private Spinner spi_miletype = null;
	
	private MenuWidget menu = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_flow);
		db = FinalDb.create(this);
		init();
	}
	FlowAdapter adapter = null;
	private void init() {
		// TODO Auto-generated method stub
		wdlcs = db.findAll(Wdlc.class);
		wdlclxs = db.findAll(Wdlclx.class);
		
		lv_flow = (ListView) findViewById(R.id.lv_flow);
		spi_miletype = (Spinner) findViewById(R.id.spi_miletype);
		
		spi_miletype.setAdapter(new ArrayAdapter<Wdlclx>(this,android.R.layout.simple_spinner_item,wdlclxs));
		adapter = new FlowAdapter(this, wdlcs);
		lv_flow.setAdapter(adapter);
		menu = (MenuWidget) findViewById(R.id.menu);
	}
	
	public void refresh(){
		adapter.notifyDataSetChanged();
	}
	int sel = 0;
	//btn
	public void selAll(View view){
		if(sel != 0){
			wdlcs = db.findAll(Wdlc.class);
			refresh();
		}
		sel = 0;
	}
	public void selWait(View view){
		if(sel != 1){
			wdlcs = db.findAllByWhere(Wdlc.class, " status = '0'");
			refresh();
		}
		sel = 1;
	}
	public void selIng(View view){
		if(sel != 2){
			wdlcs = db.findAllByWhere(Wdlc.class, " status != '0' and status != '-2' and status != '-1'");
			refresh();
		}
		sel = 2;
	}
	public void selEnd(View view){
		if(sel != 3){
			wdlcs = db.findAllByWhere(Wdlc.class, " status = '-2' or status = '-1'");
			refresh();
		}
		sel = 3;
	}
	public void doAdd(View view){
		overlay(UiAddFlow.class);
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
