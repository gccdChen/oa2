package scau.duolian.oa.adapter;

import java.util.List;

import scau.duolian.oa.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	List<Bitmap> bitmaps = null;
	private Activity activity = null;
	private LayoutInflater inflater = null;
	int len;

	public ImageAdapter(Activity activity, List<Bitmap> bitmaps) {
		super();
		this.activity = activity;
		this.bitmaps = bitmaps;
		len = bitmaps.size();
	}

	public int getCount() {
		return bitmaps.size();

	}

	public Object getItem(int position) {

		return position;
	}

	public long getItemId(int position) {

		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView iv;
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.item_gallery, null);
			iv = (ImageView) view.findViewById(R.id.iv);
			view.setTag(iv);
		} else
			iv = (ImageView) view.getTag();
		Bitmap bitmap = bitmaps.get(position);
		iv.setImageBitmap(bitmap);
		return view;
	}

}
