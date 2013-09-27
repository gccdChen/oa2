package scau.duolian.oa.adapter;

import java.util.List;

import net.tsz.afinal.FinalDb;
import scau.duolian.oa.R;
import scau.duolian.oa.base.BaseUi;
import scau.duolian.oa.base.C;
import scau.duolian.oa.model.Wddh;
import scau.duolian.oa.model.Wdhb;
import scau.duolian.oa.model.Wdlcb;
import scau.duolian.oa.model.Wdrw;
import scau.duolian.oa.model.Wdxm;
import scau.duolian.oa.ui.UiAddMilestone;
import scau.duolian.oa.ui.UiAddTask;
import scau.duolian.oa.util.DateUtil;
import scau.duolian.oa.util.StringUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ProDetailAdapter extends BaseExpandableListAdapter{

	private Wdxm project = null;
	private BaseUi activity = null;
 	private LayoutInflater inflater = null;
 	private List<Wddh> wddhs = null;
	private CommentAdapter adapter = null;
	private FinalDb db = null;
	
	private List<Wdlcb> wdlcbs = null;
	private List<Wdrw> wdrws = null;
	
	public ProDetailAdapter( BaseUi activity,Wdxm project,FinalDb db,List<Wddh> wddhs) {
		super();
		this.project = project;
		this.activity = activity;
		this.db = db;
		this.wddhs = wddhs;
		wdlcbs = db.findAllByWhere(Wdlcb.class, " xmid = '"+project.id+"'");
		wdrws = db.findAllByWhere(Wdrw.class, " xmid = '"+project.id+"'");
		inflater = LayoutInflater.from(activity);
		
		adapter = new CommentAdapter(activity, this.wddhs);
	}
	
	public void notifiThis(){
		adapter.notifyDataSetChanged();
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		switch (groupPosition) {
			case 1:
				return wdrws.size();
			case 2:
				return wdlcbs.size();
//			case 3:
//				return 1;
			default:
				return 1;
		}
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ImageView iv_fold = null;
		switch (groupPosition) {
			case 0:
				convertView = inflater.inflate(R.layout.item_pro_detail_title, null);
				iv_fold = (ImageView) convertView.findViewById(R.id.iv_fold);
				TextView tv_title = (TextView)convertView.findViewById(R.id.tv_title);
				tv_title.setText(project.title);
				if(isExpanded){
					iv_fold.setImageResource(R.drawable.fold);
				}else{
					iv_fold.setImageResource(R.drawable.unfold);
				}
				break;
			case 1:
				convertView = inflater.inflate(R.layout.item_pro_detail_task, null);
				iv_fold = (ImageView) convertView.findViewById(R.id.iv_fold);
				if(isExpanded){
					iv_fold.setImageResource(R.drawable.fold);
				}else{
					iv_fold.setImageResource(R.drawable.unfold);
				}
				convertView.findViewById(R.id.iv_add).setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						activity.overlay(UiAddTask.class);
					}
				});
				break;
			case 2:
				convertView = inflater.inflate(R.layout.item_pro_detail_milestone, null);
				iv_fold = (ImageView) convertView.findViewById(R.id.iv_fold);
				if(isExpanded){
					iv_fold.setImageResource(R.drawable.fold);
				}else{
					iv_fold.setImageResource(R.drawable.unfold);
				}
				convertView.findViewById(R.id.iv_add).setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						activity.overlay(UiAddMilestone.class);
					}
				});
				break;
			case 3:
				convertView = inflater.inflate(R.layout.item_task_div, null);
				TextView textView = (TextView) convertView.findViewById(R.id.title);
				textView.setText("【项目讨论】");
				break;
			default:
				break;
		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		switch (groupPosition) {
			case 0:
				convertView = inflater.inflate(R.layout.item_pro_ds, null);
				TextView tv_starttime = (TextView) convertView.findViewById(R.id.tv_starttime);
				TextView tv_finishtime = (TextView) convertView.findViewById(R.id.tv_finishtime);
				TextView tv_duty = (TextView) convertView.findViewById(R.id.tv_duty);
				TextView tv_teamer = (TextView) convertView.findViewById(R.id.tv_teamer);
				TextView tv_observer = (TextView) convertView.findViewById(R.id.tv_observer);
				TextView tv_state = (TextView) convertView.findViewById(R.id.tv_state);
				TextView tv_ds = (TextView) convertView.findViewById(R.id.tv_ds);
				TextView tv_sum = (TextView) convertView.findViewById(R.id.tv_sum);
				
				
				tv_starttime.setText(DateUtil.longStrToStr(project.ksrq));
				tv_finishtime.setText(DateUtil.longStrToStr(project.jsrq));
				if(!StringUtil.isBlank(project.author)){
					Wdhb author = db.findById(project.author,Wdhb.class);
					tv_duty.setText(author.name);
				}
				if(!StringUtil.isBlank(project.member)){
					String m = project.member.replaceAll(",", "','");
					List<Wdhb> members = db.findAllByWhere(Wdhb.class, " in ('"+m+"')");
					StringBuffer tx_mem = new StringBuffer();
					for(int i=0;i<members.size();i++){
						tx_mem.append(members.get(i)+",");
					}
					tv_teamer.setText( tx_mem.subSequence(0,tx_mem.length()-1));
				}
				if(!StringUtil.isBlank(project.visitor)){
					String m = project.visitor.replaceAll(",", "','");
					List<Wdhb> members = db.findAllByWhere(Wdhb.class, " in ('"+m+"')");
					StringBuffer tx_mem = new StringBuffer();
					for(int i=0;i<members.size();i++){
						tx_mem.append(members.get(i)+",");
					}
					tv_observer.setText( tx_mem.subSequence(0,tx_mem.length()-1));
				}
				tv_state.setText(C.array.proStatus[Integer.parseInt(project.status)]);
				tv_ds.setText(project.bz);
				tv_sum.setText(project.xj);
				break;
			case 1:
				convertView = inflater.inflate(R.layout.item_task, null);
				TextView tv_task_title = (TextView) convertView.findViewById(R.id.tv_task_title);
				TextView tv_task_state = (TextView) convertView.findViewById(R.id.tv_task_state);
				Wdrw wdrw = wdrws.get(childPosition);
				tv_task_title.setText(wdrw.title);
				tv_task_state.setText("["+C.array.proStatus[Integer.parseInt(wdrw.status)]+"]");
				break;
			case 2:
				convertView = inflater.inflate(R.layout.item_task, null);
				TextView tv_mile_title = (TextView) convertView.findViewById(R.id.tv_task_title);
				TextView tv_mile_state = (TextView) convertView.findViewById(R.id.tv_task_state);
				Wdlcb wdlcb = wdlcbs.get(childPosition);
				tv_mile_title.setText(wdlcb.title);
				tv_mile_state.setText("["+C.array.proStatus[Integer.parseInt(wdlcb.status)]+"]");
				break;
			case 3:
				ListView lv_comment = new ListView(activity);
				lv_comment.setAdapter(adapter);
				convertView = lv_comment;
				break;
		default:
			break;
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

}
