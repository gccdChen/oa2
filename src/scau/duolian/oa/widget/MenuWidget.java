package scau.duolian.oa.widget;

import scau.duolian.oa.R;
import scau.duolian.oa.base.BaseUi;
import scau.duolian.oa.ui.UiCalender;
import scau.duolian.oa.ui.UiMessageCenter;
import scau.duolian.oa.ui.UiNoticeDetail;
import scau.duolian.oa.ui.UiNoticeList;
import scau.duolian.oa.ui.UiPartnerList;
import scau.duolian.oa.ui.UiFlow;
import scau.duolian.oa.ui.UiProjectTask;
import scau.duolian.oa.ui.UiSignin;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class MenuWidget extends LinearLayout{
	
	private int current = R.id.btn_to_message;
	private BaseUi context;
	public MenuWidget(Context context) {
		super(context);
		this.context = (BaseUi) context;
//		LayoutInflater inflater = LayoutInflater.from(context);
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.widget_menu, this);
		
		initCon();
	}
	
	
	public MenuWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = (BaseUi) context;
//		LayoutInflater inflater = LayoutInflater.from(context);
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.widget_menu, this);
		
		initCon();
	}


	OnClickListener clickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btn_to_message:
				context.forward(UiMessageCenter.class);
				break;
			case R.id.btn_to_partner:
				context.overlay(UiPartnerList.class);
				break;
			case R.id.btn_to_check:
				context.forward(UiSignin.class);
				break;
			case R.id.btn_to_ks:
				context.forward(UiMessageCenter.class);
				break;
			case R.id.btn_to_pro:
				context.forward(UiProjectTask.class);
				break;
			case R.id.btn_to_proccess:
				context.forward(UiFlow.class);
				break;
			case R.id.btn_to_notice:
				context.forward(UiNoticeList.class);
				break;
			case R.id.btn_to_backup:
				context.forward(UiCalender.class);
				break;
			default:
				break;
			}
		}
	};
	private void initCon() {
		// TODO Auto-generated method stub
		findViewById(R.id.btn_to_message).setOnClickListener(clickListener);
		findViewById(R.id.btn_to_partner).setOnClickListener(clickListener);
		findViewById(R.id.btn_to_check).setOnClickListener(clickListener);
		findViewById(R.id.btn_to_ks).setOnClickListener(clickListener);
		findViewById(R.id.btn_to_pro).setOnClickListener(clickListener);
		findViewById(R.id.btn_to_proccess).setOnClickListener(clickListener);
		findViewById(R.id.btn_to_notice).setOnClickListener(clickListener);
		findViewById(R.id.btn_to_backup).setOnClickListener(clickListener);
	}

}
