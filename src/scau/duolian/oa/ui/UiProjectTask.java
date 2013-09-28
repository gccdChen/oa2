package scau.duolian.oa.ui;

import java.util.List;

import net.tsz.afinal.FinalDb;
import scau.duolian.oa.R;
import scau.duolian.oa.adapter.ProTaskListAdapter;
import scau.duolian.oa.adapter.TaskAdapter;
import scau.duolian.oa.base.BaseUiAuth;
import scau.duolian.oa.model.Wdrw;
import scau.duolian.oa.model.Wdrwlx;
import scau.duolian.oa.model.Wdxm;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TabHost;

public class UiProjectTask extends BaseUiAuth {

	private FinalDb db = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_project_task);

		db = FinalDb.create(this);
		initCon();
		initdata();
	}

	private ExpandableListView el_pro_task;
	private ProTaskListAdapter ptAdapter;
	private ListView lv_task;
	private TaskAdapter tAdapter;
	private RadioButton rb_pro_selected;
	private RadioButton rb_task_selected;
	private TabHost mTabHost = null;
	private Spinner spi_task_type = null;

	private void initCon() {
		// TODO Auto-generated method stub
		mTabHost = (TabHost) findViewById(R.id.tabhost);
		mTabHost.setup();

		mTabHost.addTab(mTabHost.newTabSpec("task").setIndicator("world").setContent(R.id.el_pro_task_list));
		mTabHost.addTab(mTabHost.newTabSpec("pro").setIndicator("hello").setContent(R.id.lv_task_list));
		spi_task_type = (Spinner) findViewById(R.id.spi_task_type);
	}

	private List<Wdrwlx> wdrwlxs = null;
	private ArrayAdapter<Wdrwlx> adapter = null;
	private List<Wdrw> wdrws = null;
	private List<Wdxm> wdxms = null;

	private void initdata() {
		// TODO Auto-generated method stub
		el_pro_task = (ExpandableListView) findViewById(R.id.el_pro_task_list);
		wdxms = db.findAll(Wdxm.class, "ksrq");
		wdrws = db.findAll(Wdrw.class, "xmid");
		for (int i = 0; i < wdrws.size(); i++) {
			Wdrw rw = wdrws.get(i);
			for (int j = 0; j < wdxms.size(); j++) {
				if (wdxms.get(j).id.equals(rw.xmid)) {
					wdxms.get(j).wdrws.add(rw);
					break;
				}
			}
		}
		ptAdapter = new ProTaskListAdapter(this, wdxms);
		el_pro_task.setAdapter(ptAdapter);
		lv_task = (ListView) findViewById(R.id.lv_task_list);
		tAdapter = new TaskAdapter(this, wdrws);
		lv_task.setAdapter(tAdapter);

		rb_pro_selected = (RadioButton) findViewById(R.id.rb_pro_selected);
		rb_task_selected = (RadioButton) findViewById(R.id.rb_task_selected);

		rb_task_selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				mTabHost.setCurrentTab(0);
			}
		});
		rb_pro_selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				mTabHost.setCurrentTab(1);
			}
		});

		rb_pro_selected.setChecked(true);

		wdrwlxs = db.findAll(Wdrwlx.class);

		adapter = new ArrayAdapter<Wdrwlx>(this, android.R.layout.simple_spinner_item, wdrwlxs);
		spi_task_type.setAdapter(adapter);

		super.menu = findViewById(R.id.menu);

	}

	/*
	 * public List<Wdxm> getWdxmList(){ List<Wdxm> projects = new ArrayList<Wdxm>(); for( int i=0;i<5;i++){ Wdxm project
	 * = new Wdxm(); project.title = "project"+i; for( int j=0;j<5;j++){ Wdrw task = new Wdrw(); task.title = "task"
	 * +i+"_"+j; task.status = "进行中"; project.wdrws.add(task); } projects.add(project); } return projects; }
	 * 
	 * public List<Wdrw> getWdrwList(){ List<Wdrw> tasks = new ArrayList<Wdrw>(); for(int i=0;i<10;i++){ Wdrw task =new
	 * Wdrw(); task.title = "title"+i; task.status = "[进行中]"; tasks.add(task); } return tasks; }
	 */

	// btn
	public void doAdd(View view) {
		// TODO Auto-generated method stub
		overlay(UIAddProject.class);
	}
}
