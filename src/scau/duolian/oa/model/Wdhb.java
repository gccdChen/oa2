package scau.duolian.oa.model;

import net.tsz.afinal.annotation.sqlite.Id;
import scau.duolian.oa.base.BaseModel;

/**
 * 伙伴
 */
public class Wdhb extends BaseModel {
	@Id
	public String id;
	public String name;
	public String photo;
	public String dept;
	public String job;
	public String email;
	public String mobile;
	public String kdp;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getKdp() {
		return kdp;
	}

	public void setKdp(String kdp) {
		this.kdp = kdp;
	}

}