package scau.duolian.oa.adapter;

import java.util.List;

import scau.duolian.oa.R;
import scau.duolian.oa.base.BaseUi;
import scau.duolian.oa.model.Lzls;
import scau.duolian.oa.model.Wddh;
import scau.duolian.oa.util.DateUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LzlsAdapter extends BaseAdapter{
	private BaseUi baseUi = null;
	public List<Lzls> lzlss = null;
	private LayoutInflater inflater = null;
	public LzlsAdapter(BaseUi baseUi) {
		super();
		this.baseUi = baseUi;
		inflater = LayoutInflater.from(baseUi);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(lzlss==null)
			return 0;
		return lzlss.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		if(lzlss==null)
			return 0;
		return lzlss.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		if(lzlss==null)
			return 0;
		return Long.parseLong(lzlss.get(position).id);
	}

	private class ViewHolder{
		ImageView iv_face;
		TextView tv_name;
		TextView tv_step;
		TextView tv_content;
		TextView tv_time;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		ViewHolder holder = null;
		if(view == null){
			view = inflater.inflate(R.layout.item_flow_step, null);
			holder = new ViewHolder();
			holder.iv_face = (ImageView) view.findViewById(R.id.iv_face);
			holder.tv_step = (TextView) view.findViewById(R.id.tv_step);
			holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			holder.tv_content = (TextView) view.findViewById(R.id.tv_content);
			holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
			view.setTag(holder);
		}else{
			holder = (ViewHolder) view.getTag();
		}
		Lzls lzls = lzlss.get(position);
		if(lzls == null)
			return null;
		else{
			holder.tv_name.setText(""+lzls.author);
			holder.tv_step.setText("["+position+1+"]");
			holder.tv_content.setText(""+lzls.bz);
			holder.tv_time.setText(DateUtil.longStrToStr(lzls.dt));
		}
		return view;
	}
	
}
