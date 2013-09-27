package scau.duolian.oa.model;

import net.tsz.afinal.annotation.sqlite.Id;
import scau.duolian.oa.base.BaseModel;

/**
 * 部门
 */
public class Wdbm extends BaseModel {
	@Id
	public String id;
	public String title;
	public String parentid;
	public String layer;
	public String child;

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

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getLayer() {
		return layer;
	}

	public void setLayer(String layer) {
		this.layer = layer;
	}

	public String getChild() {
		return child;
	}

	public void setChild(String child) {
		this.child = child;
	}

}