package scau.duolian.oa.test;

import java.util.ArrayList;
import java.util.List;

import scau.duolian.oa.R;
import scau.duolian.oa.adapter.ProTaskListAdapter;
import scau.duolian.oa.model.Project;
import scau.duolian.oa.model.Task;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;

public class UiTest extends Activity{
	ExpandableListView listView = null;
	ProTaskListAdapter adapter = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		
		listView = (ExpandableListView) findViewById(R.id.exp);
		adapter = new  ProTaskListAdapter(this, getProject());
		listView.setAdapter(adapter);
	}
	
	public List<Project> getProject(){
		List<Project> projects = new ArrayList<Project>();
		for( int i=0;i<5;i++){
			Project project = new Project();
			project.title = "project"+i;
			for( int j=0;j<5;j++){
				Task task = new Task();
				task.title = "task" +i+"_"+j;
				task.state = "进行中";
				project.tasks.add(task);
			}
			projects.add(project);
		}
		return projects;
	}
}
