package scau.duolian.oa.model;

import scau.duolian.oa.base.BaseModel;

/**
 * 流程类别
 */
public class Wdlclx extends BaseModel {
	public String id;
	public String title;

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

	@Override
	public String toString() {
		return title;
	}

	
}