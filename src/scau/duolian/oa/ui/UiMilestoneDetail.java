package scau.duolian.oa.ui;

import net.tsz.afinal.FinalDb;
import scau.duolian.oa.R;
import scau.duolian.oa.base.BaseUiAuth;
import scau.duolian.oa.model.Wdlcb;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 获取指定里程碑详情
 */
public class UiMilestoneDetail extends BaseUiAuth {
	private TextView xmmc, title, rq, tx, xx, xj;
	FinalDb db = null;
	private Wdlcb wdlcb;
	private String id;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_addmilestone);
		init();
		refresh();
	}

	private void init() {
		Intent intent = getIntent();
		id = intent.getStringExtra("id");

		xmmc = (TextView) findViewById(R.id.tv_projectname);
		title = (TextView) findViewById(R.id.tv_title);
		rq = (TextView) findViewById(R.id.tv_rq);
		tx = (TextView) findViewById(R.id.tv_tx);
		xx = (TextView) findViewById(R.id.tv_xx);
		xj = (TextView) findViewById(R.id.tv_xj);
	}

	private void refresh() {

		wdlcb = db.findById(id, Wdlcb.class);
		title.setText(wdlcb.title);
		rq.setText(wdlcb.dt);
		tx.setText(wdlcb.tx);
		xj.setText(wdlcb.xj);
	}
}
