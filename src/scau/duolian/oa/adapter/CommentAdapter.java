package scau.duolian.oa.adapter;

import java.util.List;

import scau.duolian.oa.R;
import scau.duolian.oa.base.BaseUi;
import scau.duolian.oa.model.Wddh;
import scau.duolian.oa.util.DateUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter{
	private BaseUi baseUi = null;
	private List<Wddh> wddhs = null;
	private LayoutInflater inflater = null;
	public CommentAdapter(BaseUi baseUi, List<Wddh> wddhs) {
		super();
		this.baseUi = baseUi;
		this.wddhs = wddhs;
		inflater = LayoutInflater.from(baseUi);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return wddhs.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return wddhs.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return Long.parseLong(wddhs.get(position).id);
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
			view = inflater.inflate(R.layout.item_comments, null);
			holder = new ViewHolder();
			holder.iv_face = (ImageView) view.findViewById(R.id.iv_face);
			holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			holder.tv_content = (TextView) view.findViewById(R.id.tv_content);
			holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
		}else{
			holder = (ViewHolder) view.getTag();
		}
		Wddh wddh = wddhs.get(position);
		holder.tv_name.setText(wddh.author);
		holder.tv_content.setText(wddh.bz);
		holder.tv_time.setText(DateUtil.longStrToStr(wddh.dt));
		return view;
	}
	
}
