package scau.duolian.oa.model;

import java.util.ArrayList;
import java.util.List;

import scau.duolian.oa.base.BaseModel;

public class Wdxm extends BaseModel {
	public String id;
	public String title;
	public String author;
	public String status;
	public String member;
	public String visitor;
	public String ksrq;
	public String jsrq;
	public String xj;
	public String bz;
	public String attach;

	public List<Wdrw> wdrws = new ArrayList<Wdrw>();

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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMember() {
		return member;
	}

	public void setMember(String member) {
		this.member = member;
	}

	public String getVisitor() {
		return visitor;
	}

	public void setVisitor(String visitor) {
		this.visitor = visitor;
	}

	public String getKsrq() {
		return ksrq;
	}

	public void setKsrq(String ksrq) {
		this.ksrq = ksrq;
	}

	public String getJsrq() {
		return jsrq;
	}

	public void setJsrq(String jsrq) {
		this.jsrq = jsrq;
	}

	public String getXj() {
		return xj;
	}

	public void setXj(String xj) {
		this.xj = xj;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

}