package scau.duolian.oa.adapter;

import java.util.List;

import scau.duolian.oa.base.BaseUi;
import scau.duolian.oa.model.Wdhb;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
/**
 * 
 *
 */
public class SelHbAdapter extends BaseAdapter{

	private BaseUi baseUi = null;
	private List<Wdhb> wdhbs = null;
	private TextView tv_name = null; 
	private Dialog dialog = null;
	public String selId;
	
	public SelHbAdapter(BaseUi baseUi, List<Wdhb> wdhbs,TextView tv_name,Dialog dialog) {
		super();
		this.baseUi = baseUi;
		this.wdhbs = wdhbs;
		this.tv_name = tv_name;
		this.dialog = dialog;
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

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Wdhb wdhb = wdhbs.get(position);
		TextView view = new TextView(baseUi);
		view.setText(wdhb.name);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tv_name.setText(wdhb.name);
				if(selId != null)
					selId = wdhb.id;
				if(dialog != null)
					dialog.dismiss();
			}
		});
		return convertView;
	}
	
}
