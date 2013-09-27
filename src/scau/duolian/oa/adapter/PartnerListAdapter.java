package scau.duolian.oa.adapter;

import java.util.List;

import scau.duolian.oa.R;
import scau.duolian.oa.base.C;
import scau.duolian.oa.model.Message;
import scau.duolian.oa.model.Wdhb;
import scau.duolian.oa.ui.UiPartnerDetail;
import scau.duolian.oa.ui.UiPartnerList;
import scau.duolian.oa.util.DateUtil;
import scau.duolian.oa.util.ImageLoader;
import scau.duolian.oa.util.StringUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PartnerListAdapter extends BaseAdapter{
	private Activity activity = null;
	public List<Wdhb> wdhbs = null;
	public String keyword = null;
	public boolean filter = false;
	private ImageLoader imageLoader = null;
	private LayoutInflater inflater = null;
	public PartnerListAdapter(Activity activity, List<Wdhb> wdhbs) {
		super();
		this.activity = activity;
		this.wdhbs = wdhbs;
		this.inflater = LayoutInflater.from(activity);
		imageLoader = new ImageLoader();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return wdhbs.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return wdhbs.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	private class ViewHolder{
		ImageView iv_face;
		TextView tv_title;
		Button btn_go;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		ViewHolder holder = null;
		if(view == null){
			view = inflater.inflate(R.layout.item_partner, null);
			holder = new ViewHolder();
			holder.iv_face = (ImageView) view.findViewById(R.id.iv_face);
			holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
			holder.btn_go = (Button) view.findViewById(R.id.btn_go);
			view.setTag(holder);
		}else
			holder = (ViewHolder) view.getTag();
		final Wdhb wdhb = wdhbs.get(position%wdhbs.size());
		
		if(!StringUtil.isBlank(wdhb.photo))
			imageLoader.loadImage(wdhb.photo, holder.iv_face);
		holder.tv_title.setText(wdhb.name);
		OnClickListener clickListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_VIEW);
				Bundle params = new Bundle();
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				intent.setClass(activity, UiPartnerDetail.class);
				params.putString(C.params.hbid, ""+wdhb.id);
				intent.putExtras(params);
				activity.startActivity(intent);
			}
		};
		view.setOnClickListener(clickListener);
		holder.btn_go.setOnClickListener(clickListener);
		
		return view;
	}

}
