package scau.duolian.oa.model;

import scau.duolian.oa.base.BaseModel;

/**
 * 里程碑
 */
public class Wdlcb extends BaseModel {
	public String id;
	public String xmid;
	public String title;
	public String status;
	public String xj;
	public String dt;
	public String tx;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getXmid() {
		return xmid;
	}

	public void setXmid(String xmid) {
		this.xmid = xmid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getXj() {
		return xj;
	}

	public void setXj(String xj) {
		this.xj = xj;
	}

	public String getDt() {
		return dt;
	}

	public void setDt(String dt) {
		this.dt = dt;
	}

	public String getTx() {
		return tx;
	}

	public void setTx(String tx) {
		this.tx = tx;
	}

}