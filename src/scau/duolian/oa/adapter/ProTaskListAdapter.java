package scau.duolian.oa.adapter;

import java.util.List;

import scau.duolian.oa.R;
import scau.duolian.oa.base.BaseUi;
import scau.duolian.oa.model.Wdrw;
import scau.duolian.oa.model.Wdxm;
import scau.duolian.oa.ui.UiProjectDetail;
import scau.duolian.oa.ui.UiTaskDetail;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProTaskListAdapter extends BaseExpandableListAdapter{

	private BaseUi activity;
	private List<Wdxm> projects;
	private LayoutInflater inflater;
	public ProTaskListAdapter(BaseUi activity,List<Wdxm> projects) {
		super();
		this.activity = activity;
		this.projects = projects; 
		inflater = LayoutInflater.from(activity);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return projects.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		if(projects.get(groupPosition).wdrws == null)
			return 0;
		return projects.get(groupPosition).wdrws.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return projects.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return projects.get(groupPosition).wdrws.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	private class ViewGroupHolder{
		TextView tv_title;
		ImageView iv_fold;
		ImageView iv_holder;
	}
	
	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		ViewGroupHolder holder = null;
		if(view ==null){
			view = inflater.inflate(R.layout.item_project, null);
			holder = new ViewGroupHolder();
			holder.tv_title = (TextView)view.findViewById(R.id.tv_title);
			holder.iv_fold = (ImageView)view.findViewById(R.id.iv_fold);
			holder.iv_holder = (ImageView)view.findViewById(R.id.iv_holder);
			view.setTag(holder);
		}else{
			holder = (ViewGroupHolder) view.getTag();
		}
		
		holder.tv_title.setText(projects.get(groupPosition).title);
		if(!isExpanded){
			holder.iv_fold.setImageResource(R.drawable.unfold);
		}else{
			holder.iv_fold.setImageResource(R.drawable.fold);
		}
		
		holder.iv_holder.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle params = new Bundle();
				params.putString("id", projects.get(groupPosition).id);
				activity.overlay(UiProjectDetail.class, params);
			}
		});
		return view;
	}

	private class ViewChildHolder{
		TextView tv_title;
		TextView tv_state;
	}
	
	@Override
	public View getChildView(int groupPosition, int childPosition,boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		ViewChildHolder holder = null;
		if(view == null){
			view = inflater.inflate(R.layout.item_task, null);
			holder = new ViewChildHolder();
			view.setTag(holder);
			holder.tv_title = (TextView) view.findViewById(R.id.tv_task_title);
			holder.tv_state = (TextView) view.findViewById(R.id.tv_task_state);
		}else{
			holder = (ViewChildHolder) view.getTag();
		}
		
		if(projects.get(groupPosition).wdrws == null){
			view.setVisibility(View.GONE);
			return view;
		}
		
		final Wdrw wdrw = projects.get(groupPosition).wdrws.get(childPosition);
		
		if(wdrw!=null){
			holder.tv_title.setText(wdrw.title);
			holder.tv_state.setText(wdrw.status);
		}
		
		view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle params = new Bundle();
				params.putString("id", wdrw.id);
				activity.overlay(UiTaskDetail.class, params);
			}
		});
		return view;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

}
