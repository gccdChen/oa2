package scau.duolian.oa.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import net.tsz.afinal.FinalDb;
import scau.duolian.oa.R;
import scau.duolian.oa.base.BaseUi;
import scau.duolian.oa.base.C;
import scau.duolian.oa.model.Lzls;
import scau.duolian.oa.model.Wddh;
import scau.duolian.oa.model.Wdhb;
import scau.duolian.oa.model.Wdlc;
import scau.duolian.oa.model.Wdlclx;
import scau.duolian.oa.model.Wdxm;
import scau.duolian.oa.util.DateUtil;
import scau.duolian.oa.util.StringUtil;

/**
 * 查看流程 
 *
 */
public class ShowFlowAdapter extends BaseExpandableListAdapter{
	private BaseUi baseUi = null;
	private Wdlc wdlc = null;
	private FinalDb db = null;
	private LayoutInflater inflater = null;
	private List<Wddh> wddhs = null;
	private Wdhb wdhb = null;
	private Wdlclx wdlclx = null;
	private List<Lzls> lzlss = null;
	
	private LzlsAdapter adapter1 = null;
	
	public ShowFlowAdapter(BaseUi baseUi, Wdlc wdlc, FinalDb db,List<Wddh> wddhs,List<Lzls> lzlss) {
		super();
		this.baseUi = baseUi;
		this.wdlc = wdlc;
		this.wddhs = wddhs;
		this.db = db;
		this.lzlss = lzlss;
		inflater.from(baseUi);
		wdlclx = db.findById(wdlc.type, Wdlclx.class);
		wdhb = db.findById(wdlc.author, Wdhb.class);
	}
	
	public void notifiThis(){
		adapter1.notifyDataSetChanged();
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
		if(groupPosition == 0){
			convertView = inflater.inflate(R.layout.item_flow_ds, null);
			TextView tv_ds = (TextView) convertView.findViewById(R.id.tv_ds);
			TextView tv_type = (TextView) convertView.findViewById(R.id.tv_type);
			TextView tv_level = (TextView) convertView.findViewById(R.id.tv_level);
			TextView tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			tv_ds.setText(wdlc.title);//这个详细 好像没有 性质也没有
			tv_type.setText(wdlclx.title);
			tv_level.setText(C.array.flowLevel[Integer.parseInt(wdlc.level)]);
			tv_time.setText(DateUtil.longStrToStr(wdlc.startdt));
			if(StringUtil.isBlank(wdlc.attach)){
				convertView.findViewById(R.id.ll_attach).setVisibility(View.GONE);
			}
			
		}else if(groupPosition == 1){
			LinearLayout view = new LinearLayout(baseUi);
			ListView listView = new ListView(baseUi);
			adapter1 = new LzlsAdapter(baseUi, lzlss);
			listView.setAdapter(adapter1);
			view.addView(listView);
			convertView = view;
		}
		return convertView;
	}
	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return 1;
	}
	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return 2;
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
		if(groupPosition == 0){
			convertView = inflater.inflate(R.layout.item_flow_detail_title1, null);
			TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			tv_title.setText(wdhb.name+"\n"+wdlc.title);
			ImageView iv_fold = (ImageView) convertView.findViewById(R.id.iv_fold);
			
			if(!isExpanded){
				iv_fold.setImageResource(R.drawable.unfold);
			}else{
				iv_fold.setImageResource(R.drawable.fold);
			}
		}else if(groupPosition == 1){
			convertView = inflater.inflate(R.layout.item_flow_detail_title1, null);
			TextView tv_lx = (TextView) convertView.findViewById(R.id.tv_lx);
			TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			tv_lx.setText("流程状态");
			tv_lx.setText("步骤"+wdlc.status);
			ImageView iv_fold = (ImageView) convertView.findViewById(R.id.iv_fold);
			
			if(!isExpanded){
				iv_fold.setImageResource(R.drawable.unfold);
			}else{
				iv_fold.setImageResource(R.drawable.fold);
			}
		}
		return convertView;
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
