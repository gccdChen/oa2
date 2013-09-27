package scau.duolian.oa.model;

import scau.duolian.oa.base.BaseModel;

/**
 * 问卷
 */
public class Wdwj extends BaseModel {
	public String id;
	public String title;
	public String enddt;
	public String attend;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEnddt() {
		return enddt;
	}

	public void setEnddt(String enddt) {
		this.enddt = enddt;
	}

	public String getAttend() {
		return attend;
	}

	public void setAttend(String attend) {
		this.attend = attend;
	}

}