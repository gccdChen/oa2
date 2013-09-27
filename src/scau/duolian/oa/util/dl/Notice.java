package scau.duolian.oa.util.dl;

import android.view.View;

public class Notice {
	private long id;
	private String is_del;
	private String name;
	private String xh;
	private String dt;
	private String desc;
	private String img;
	private String httpimg;
	private String slt;
	private String isread;
	public View used;

	public long getId() {
		return this.id;
	}

	public void setId(long n) {
		this.id = n;
	}

	public String getIs_del() {
		return this.is_del;
	}

	public void setIs_del(String paramString) {
		this.is_del = paramString;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String paramString) {
		this.name = paramString;
	}

	public String getxh() {
		return this.xh;
	}

	public void setxh(String s) {
		this.xh = s;
	}

	public String getdt() {
		return this.dt;
	}

	public void setdt(String s) {
		this.dt = s;
	}

	public String getdesc() {
		return this.desc;
	}

	public void setdesc(String s) {
		this.desc = s;
	}

	public String getimg() {
		return this.img;
	}

	public void setimg(String s) {
		this.img = s;
	}

	public String gethttpimg() {
		return this.httpimg;
	}

	public void sethttpimg(String s) {
		this.httpimg = s;
	}

	public String getslt() {
		return this.slt;
	}

	public void setslt(String s) {
		this.slt = s;
	}

	public String getisread() {
		return this.isread;
	}

	public void setisread(String s) {
		this.isread = s;
	}
}
