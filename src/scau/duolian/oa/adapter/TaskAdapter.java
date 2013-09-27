package scau.duolian.oa.adapter;

import java.util.List;

import scau.duolian.oa.R;
import scau.duolian.oa.model.Wdrw;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TaskAdapter extends BaseAdapter{

	private List<Wdrw> tasks = null;
	private Activity activity = null;
	private LayoutInflater inflater =null;
	
	
	public TaskAdapter( Activity activity, List<Wdrw> tasks) {
		super();
		this.tasks = tasks;
		this.activity = activity;
		this.inflater = LayoutInflater.from(activity);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return tasks.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return tasks.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	private class ViewHolder{
		TextView tv_task_title;
		TextView tv_task_state;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		ViewHolder holder = null;
		if(view == null){
			view = this.inflater.inflate(R.layout.item_task,null);
			holder = new ViewHolder();
			holder.tv_task_title = (TextView) view.findViewById(R.id.tv_task_title);
			holder.tv_task_state = (TextView) view.findViewById(R.id.tv_task_state);
			view.setTag(holder);
		}else
			holder = (ViewHolder) view.getTag();
	
		holder.tv_task_title.setText(tasks.get(position).title);
		holder.tv_task_state.setText(tasks.get(position).status);
		
		return view;
	}

}
