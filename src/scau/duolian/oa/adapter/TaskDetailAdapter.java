package scau.duolian.oa.adapter;

import java.util.List;

import scau.duolian.oa.R;
import scau.duolian.oa.base.BaseUi;
import scau.duolian.oa.model.Wddh;
import scau.duolian.oa.model.Wdrw;
import scau.duolian.oa.model.Wdxm;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TaskDetailAdapter extends BaseExpandableListAdapter {
	private BaseUi baseUi = null;
	private Wdxm wdxm = null;
	private Wdrw wdrw = null;
//	private List<Wddh> wddhs = null;
	private LayoutInflater inflater = null;
	private CommentAdapter adapter = null;
	public TaskDetailAdapter(BaseUi baseUi, Wdxm wdxm, Wdrw wdrw,
			List<Wddh> wddhs) {
		super();
		this.baseUi = baseUi;
		this.wdxm = wdxm;
		this.wdrw = wdrw;
		adapter = new CommentAdapter(this.baseUi, wddhs);
		inflater = LayoutInflater.from(baseUi);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(groupPosition == 0){//	项目详情
			convertView = inflater.inflate(R.layout.item_task_ds,null );
			TextView tv_pro_name = (TextView) convertView.findViewById(R.id.tv_pro_name);
			TextView tv_starttime = (TextView) convertView.findViewById(R.id.tv_starttime);
			TextView tv_finishtime = (TextView) convertView.findViewById(R.id.tv_finishtime);
			TextView tv_ac_starttime = (TextView) convertView.findViewById(R.id.tv_ac_starttime);
			TextView tv_ac_finishtime = (TextView) convertView.findViewById(R.id.tv_pro_name);
			TextView tv_duty = (TextView) convertView.findViewById(R.id.tv_duty);
			TextView tv_state = (TextView) convertView.findViewById(R.id.tv_state);
			TextView tv_ds = (TextView) convertView.findViewById(R.id.tv_ds);
			Gallery gallery_files = (Gallery) convertView.findViewById(R.id.gallery_files);
			TextView tv_sum = (TextView) convertView.findViewById(R.id.tv_sum);
			
			tv_pro_name.setText(wdxm.title);
			tv_starttime.setText(wdrw.jhksrq);
			tv_finishtime.setText(wdrw.jhjsrq);
			tv_ac_starttime.setText(wdrw.sjksrq);
			tv_ac_finishtime.setText(wdrw.sjjsrq);
			tv_duty.setText(wdrw.author);
			tv_state.setText(wdrw.status);
			tv_ds.setText(wdrw.xj);
			tv_sum.setText(wdrw.xj);
		}else{
			ListView lv = new ListView(baseUi);
			lv.setAdapter(adapter);
			convertView = lv;
		}
		return convertView;
	}
	
	public void notifyThis(){
		adapter.notifyDataSetChanged();
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
	
	private class ViewGroupHolder{
		TextView tv_lx;
		TextView tv_title;
		ImageView iv_fold;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View view = convertView;
		if( groupPosition == 0 ){
			ViewGroupHolder holder = null;
			if(view ==null){
				view = inflater.inflate(R.layout.item_pro_detail_title, null);
				holder = new ViewGroupHolder();
				
				holder.tv_lx = (TextView)view.findViewById(R.id.tv_lx);
				holder.tv_title = (TextView)view.findViewById(R.id.tv_title);
				holder.iv_fold = (ImageView)view.findViewById(R.id.iv_fold);
				view.setTag(holder);
			}else{
				holder = (ViewGroupHolder) view.getTag();
			}
			holder.tv_lx.setText("任务名称");
			holder.tv_title.setText(wdrw.title);
			if(!isExpanded){
				holder.iv_fold.setImageResource(R.drawable.unfold);
			}else{
				holder.iv_fold.setImageResource(R.drawable.fold);
			}
		}else{
			view = inflater.inflate(R.layout.item_task_div, null);
		}
		return view;
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
