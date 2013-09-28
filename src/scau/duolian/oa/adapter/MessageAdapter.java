package scau.duolian.oa.adapter;

import java.util.List;

import net.tsz.afinal.FinalDb;
import scau.duolian.oa.R;
import scau.duolian.oa.base.BaseUi;
import scau.duolian.oa.base.C;
import scau.duolian.oa.model.Message;
import scau.duolian.oa.model.User;
import scau.duolian.oa.model.Wdhb;
import scau.duolian.oa.ui.UiCalenderDetail;
import scau.duolian.oa.ui.UiFlow;
import scau.duolian.oa.ui.UiNoticeDetail;
import scau.duolian.oa.ui.UiParnerCom;
import scau.duolian.oa.ui.UiPersonSetting;
import scau.duolian.oa.ui.UiProjectDetail;
import scau.duolian.oa.ui.UiShowHandlerFlow;
import scau.duolian.oa.ui.UiTaskDetail;
import scau.duolian.oa.util.DateUtil;
import scau.duolian.oa.util.ImageLoader;
import scau.duolian.oa.util.StringUtil;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter {
	private BaseUi activity = null;
	public List<Message> messages = null;
	private FinalDb db = null;
	private LayoutInflater inflater = null;
	private ImageLoader imageLoader = null;

	public MessageAdapter(BaseUi activity, List<Message> messages, FinalDb db) {
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

	private class ViewHolder {
		ImageView iv_face;
		TextView tv_name;
		TextView tv_content;
		TextView tv_time;
	}
	Message message = null;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		ViewHolder holder = null;
		if (view == null) {
			view = inflater.inflate(R.layout.item_messages, null);
			holder = new ViewHolder();
			holder.iv_face = (ImageView) view.findViewById(R.id.iv_face);
			holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			holder.tv_content = (TextView) view.findViewById(R.id.tv_content);
			holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
			view.setTag(holder);
		} else
			holder = (ViewHolder) view.getTag();
		message = messages.get(position % messages.size());

		if (!StringUtil.isBlank(message.author)) {
			Wdhb user = db.findById(message.author, Wdhb.class);
			if (user != null) {
				if (!StringUtil.isBlank(user.name))
					holder.tv_name.setText("【" + user.name + "】");
				if (!StringUtil.isBlank(user.photo))
					imageLoader.loadImage(user.photo, holder.iv_face);
			}
		}
		String s = StringUtil.isBlank(message.title) ? "" : "[" + message.title + "]";
		if (!StringUtil.isBlank(message.subtitle))
			holder.tv_content.setText(s + message.subtitle);
		if (!StringUtil.isBlank(message.dt))
			holder.tv_time.setText(DateUtil.longStrToStr(message.dt));

		if(message.isreaded){
			holder.tv_name.setTextColor(0xa5a5a5);
			holder.tv_time.setTextColor(0xa5a5a5);
			holder.tv_content.setTextColor(0xa5a5a5);
		}
		
		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				message.isreaded = true;
				db.update(message);
				Bundle params = null;
				switch (Integer.parseInt(message.mtype)) {
				case C.task.notice:
				case C.task.sys_notice:
					params = new Bundle();
					params.putString("id", message.source);
					activity.overlay(UiNoticeDetail.class, params);
					break;
				case C.task.partner_discuss:
					params = new Bundle();
					params.putString("id", message.source);
					activity.overlay(UiParnerCom.class, params);
					break;
				case C.task.calender_reply:
					params = new Bundle();
					params.putString("id", message.source);
					activity.overlay(UiCalenderDetail.class, params);
					break;
				case C.task.flow:
					params = new Bundle();
					params.putString("id", message.source);
					params.putBoolean("show", true);
					activity.overlay(UiShowHandlerFlow.class, params);
					break;
				case C.task.pro_message:
				case C.task.pro_discuss:
					params = new Bundle();
					params.putString("id", message.source);
					activity.overlay(UiProjectDetail.class, params);
					break;
				
				case C.task.task_discuss:
					params = new Bundle();
					params.putString("id", message.source);
					activity.overlay(UiTaskDetail.class, params);
					break;
				case C.task.personal_message:
					params = new Bundle();
					params.putString("id", message.source);
					activity.overlay(UiPersonSetting.class, params);
					break;
				default:
					break;
				}
			}
		});
		return view;
	}

}
