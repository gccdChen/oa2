package scau.duolian.oa.adapter;

import java.util.List;

import scau.duolian.oa.R;
import scau.duolian.oa.model.Wddh;
import scau.duolian.oa.model.Wdhb;
import scau.duolian.oa.util.DateUtil;
import scau.duolian.oa.util.ImageLoader;
import scau.duolian.oa.util.StringUtil;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ParnerAdapter extends BaseAdapter {
	private Activity activity = null;
	public List<Wddh> wdhbdhs = null;
	public String keyword = null;
	public boolean filter = false;
	public ImageLoader imageLoader = null;
	private LayoutInflater inflater = null;
	private Wdhb wdhb = null;

	public ParnerAdapter(Activity activity, Wdhb wdhb,List<Wddh> wdhbdhs) {
		super();
		this.activity = activity;
		this.wdhbdhs = wdhbdhs;
		this.wdhb = wdhb;
		this.inflater = LayoutInflater.from(activity);
		imageLoader = new ImageLoader();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return wdhbdhs.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return wdhbdhs.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	private class ViewHolder {
		ImageView iv_pic;
		ImageView iv_attch;
		TextView tv_name;
		TextView tv_content;
		TextView tv_time;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		ViewHolder holder = null;
		if (view == null) {
			view = inflater.inflate(R.layout.item_parnercom, null);
			holder = new ViewHolder();
			holder.iv_attch = (ImageView) view.findViewById(R.id.iv_attch);
			holder.iv_pic = (ImageView) view.findViewById(R.id.iv_pic);
			holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			holder.tv_content = (TextView) view.findViewById(R.id.tv_content);
			holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
			view.setTag(holder);
		} else
			holder = (ViewHolder) view.getTag();
		Wddh wdhbdh = wdhbdhs.get(position % wdhbdhs.size());

		imageLoader.loadImage(wdhb.photo, holder.iv_pic);
		
		if(StringUtil.isBlank(wdhbdh.file))
			holder.iv_attch.setVisibility(View.GONE);
		else
			imageLoader.loadImage(wdhbdh.file, holder.iv_attch);
		
		holder.tv_name.setText(wdhbdh.author);
		holder.tv_content.setText(wdhbdh.bz);
		holder.tv_time.setText(DateUtil.longStrToStr(wdhbdh.dt));

		return view;
	}

}
