package scau.duolian.oa.adapter;

import java.util.ArrayList;
import java.util.List;

import scau.duolian.oa.base.BaseUi;
import scau.duolian.oa.model.Wdhb;
import android.R;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class MutiSelHbAdapter extends BaseAdapter{

	private BaseUi baseUi = null;
	private List<Wdhb> wdhbs = null;
	public List<Wdhb> sels = new ArrayList<Wdhb>();
	public MutiSelHbAdapter(BaseUi baseUi, List<Wdhb> wdhbs) {
		super();
		this.baseUi = baseUi;
		this.wdhbs = wdhbs;
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
		return Long.parseLong(wdhbs.get(position).id);
	}

	TextView view = null;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Wdhb wdhb = wdhbs.get(position);
		view = new TextView(baseUi);
		view.setText(wdhb.name);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(sels.contains(wdhb)){
					view.setBackgroundResource(R.color.background_light);
					sels.remove(wdhb);
				}else{
					view.setBackgroundResource(R.color.background_dark);
					sels.add(wdhb);
				}
			}
		});
		return convertView;
	}
	
}
