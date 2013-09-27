package scau.duolian.oa.model;

import scau.duolian.oa.base.BaseModel;

/**
 * 日程
 */
public class Wdrc extends BaseModel {
	public String id;
	public String sfqd;
	public String title;
	public String bz;
	public String xdsj;
	public String txsj;
	public String week;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSfqd() {
		return sfqd;
	}

	public void setSfqd(String sfqd) {
		this.sfqd = sfqd;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getXdsj() {
		return xdsj;
	}

	public void setXdsj(String xdsj) {
		this.xdsj = xdsj;
	}

	public String getTxsj() {
		return txsj;
	}

	public void setTxsj(String txsj) {
		this.txsj = txsj;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

}