package scau.duolian.oa.adapter;

import java.util.List;

import net.tsz.afinal.FinalDb;
import scau.duolian.oa.R;
import scau.duolian.oa.base.C;
import scau.duolian.oa.model.Message;
import scau.duolian.oa.model.User;
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

public class MessageAdapter extends BaseAdapter{
	private Activity activity = null;
	public List<Message> messages = null;
	public String keyword = null;
	public boolean filter = false;
	private FinalDb db = null;
	private LayoutInflater inflater = null;
	private ImageLoader imageLoader = null;
	public MessageAdapter(Activity activity, List<Message> messages,FinalDb db) {
		super();
		this.activity = activity;
		this.messages = messages;
		this.inflater = LayoutInflater.from(activity);
		this.db = db;
		imageLoader = new ImageLoader();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return messages.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return messages.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	private class ViewHolder{
		ImageView iv_face;
		TextView tv_name;
		TextView tv_title;
		TextView tv_content;
		TextView tv_time;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		ViewHolder holder = null;
		if(view == null){
			view = inflater.inflate(R.layout.item_messages, null);
			holder = new ViewHolder();
			holder.iv_face = (ImageView) view.findViewById(R.id.iv_face);
			holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
			holder.tv_content = (TextView) view.findViewById(R.id.tv_content);
			holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
			view.setTag(holder);
		}else
			holder = (ViewHolder) view.getTag();
		final Message message = messages.get(position%messages.size());
		
		if(!StringUtil.isBlank(message.author)){
			Wdhb user = db.findById(message.author, Wdhb.class);
			if(user != null){
				if(!StringUtil.isBlank(user.name))
					holder.tv_name.setText(user.name);
				if(!StringUtil.isBlank(user.photo))
				imageLoader.loadImage(user.photo, holder.iv_face);
			}
		}
		holder.tv_title.setText(message.title);
		holder.tv_content.setText(message.subtitle);
		holder.tv_time.setText(DateUtil.longStrToStr(message.dt) );
		
		
		if(filter){
			if(!message.find(keyword))
				view.setVisibility(View.GONE);
		}
		
		view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (Integer.parseInt(message.mtype)) {
				case C.task.partner_discuss:
					break;

				default:
					break;
				}
			}
		});
		return view;
	}

}
