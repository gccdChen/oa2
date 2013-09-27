package scau.duolian.oa.ui;

import java.util.List;

import net.tsz.afinal.FinalDb;

import android.os.Bundle;
import android.widget.ExpandableListView;
import scau.duolian.oa.R;
import scau.duolian.oa.adapter.CalenderNoteAdapter;
import scau.duolian.oa.base.BaseUiAuth;
import scau.duolian.oa.model.CalenderNote;
import scau.duolian.oa.model.Wdrc;
import scau.duolian.oa.util.ChangeUtil;

public class UiCalender extends BaseUiAuth{
	
	private CalenderNoteAdapter adapter = null;
	private ExpandableListView expandableListView = null;
	private FinalDb db = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_calendar);
		db = FinalDb.create(this);
		initCon();
		initdata();
	}
	
	private void initCon() {
		// TODO Auto-generated method stub
		expandableListView = (ExpandableListView) findViewById(R.id.content);
	}
	List<CalenderNote> calenderNotes = null;
	private void initdata() {
		// TODO Auto-generated method stub
		calenderNotes = getCalenderNotes();
		adapter = new CalenderNoteAdapter(this, calenderNotes);
		expandableListView.setAdapter(adapter);
	}

	private List<CalenderNote> getCalenderNotes() {
		// TODO Auto-generated method stub
		List<Wdrc> wdrcs = db.findAll(Wdrc.class, "xdsj");
		return ChangeUtil.Wdrcs2Cals(wdrcs);
	}
}
