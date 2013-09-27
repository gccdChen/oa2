package scau.duolian.oa.adapter;

import java.util.List;

import scau.duolian.oa.R;
import scau.duolian.oa.base.BaseUi;
import scau.duolian.oa.base.C;
import scau.duolian.oa.model.Wddh;
import scau.duolian.oa.model.Wdlc;
import scau.duolian.oa.util.DateUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FlowAdapter extends BaseAdapter{
	private BaseUi baseUi = null;
	private List<Wdlc> wdlcs = null;
	private LayoutInflater inflater = null;
	public FlowAdapter(BaseUi baseUi, List<Wdlc> wdlcs) {
		super();
		this.baseUi = baseUi;
		this.wdlcs = wdlcs;
		inflater = LayoutInflater.from(baseUi);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return wdlcs.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return wdlcs.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return Long.parseLong(wdlcs.get(position).id);
	}

	private class ViewHolder{
		ImageView iv_face;
		TextView tv_name;
		TextView tv_content;
		TextView tv_time;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		ViewHolder holder = null;
		if(view == null){
			view = inflater.inflate(R.layout.item_flow, null);
			holder = new ViewHolder();
			holder.iv_face = (ImageView) view.findViewById(R.id.iv_face);
			holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			holder.tv_content = (TextView) view.findViewById(R.id.tv_content);
			holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
		}else{
			holder = (ViewHolder) view.getTag();
		}
		Wdlc wddh = wdlcs.get(position);
		holder.tv_name.setText(wddh.author);
		holder.tv_content.setText(wddh.title);
		String status  = null;
		if(wddh.status.equals("0"))
			status = "待办";
		else if(wddh.status.equals("-1"))
			status = "结束";
		else if(wddh.status.equals("-2"))
			status = "完成";
		else
			status = "第"+wddh.status+"步";
		holder.tv_time.setText(DateUtil.longStrToStr(wddh.startdt) + "  ["+status+"]");
		return view;
	}
	
}
