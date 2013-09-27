package scau.duolian.oa.adapter;

import java.util.List;

import scau.duolian.oa.R;
import scau.duolian.oa.base.BaseUi;
import scau.duolian.oa.model.CalenderNote;
import scau.duolian.oa.model.Wdrc;
import scau.duolian.oa.ui.UiCalenderDetail;
import scau.duolian.oa.util.DateUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class CalenderNoteAdapter extends BaseExpandableListAdapter{

	private BaseUi baseUi = null;
	private List<CalenderNote> calenderNotes = null;
	private LayoutInflater inflater = null;
	public CalenderNoteAdapter(BaseUi baseUi, List<CalenderNote> calenderNotes) {
		super();
		this.baseUi = baseUi;
		this.calenderNotes = calenderNotes;
		inflater = LayoutInflater.from(this.baseUi);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return calenderNotes.get(groupPosition).wdrcs.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return Long.parseLong(calenderNotes.get(groupPosition).wdrcs.get(childPosition).id);
	}

	private class ChildViewHolder{
		private TextView tv_title;
	}

	
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		ChildViewHolder holder = null;
		
		if(view == null){
			view = inflater.inflate(R.layout.item_calender, null);
			holder = new ChildViewHolder();
			holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
			view.setTag(holder);
		}else
			holder = (ChildViewHolder) view.getTag();
		final Wdrc rc = calenderNotes.get(groupPosition).wdrcs.get(childPosition);
		
		holder.tv_title.setText(DateUtil.strToHourStr(rc.xdsj)+rc.title);
		view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle params = new Bundle();
				params.putString("id", rc.id);
				baseUi.overlay(UiCalenderDetail.class, params);
			}
		});
		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

}
