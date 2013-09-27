package scau.duolian.oa.model;
import scau.duolian.oa.base.BaseModel;
public class User extends BaseModel{
		public String dlyid;
		public String customer;
		public String uid;
		public String zdzk;
		public String tcfm;
		public int newmessage;
		public int newdt;
		public String newtitle;
		public String newdesc;
		public String username;
		public String password;
		public String phone;
		public String qq;
		
		
		public boolean isadmin;
		
		public User() {
			// TODO Auto-generated constructor stub
		}
		
		
		
		public User(String dlyid, String uid, String username) {
			super();
			this.dlyid = dlyid;
			this.uid = uid;
			this.username = username;
		}



		public String getDlyid() {
			return dlyid;
		}



		public void setDlyid(String dlyid) {
			this.dlyid = dlyid;
		}



		public String getCustomer() {
			return customer;
		}



		public void setCustomer(String customer) {
			this.customer = customer;
		}



		public String getUid() {
			return uid;
		}



		public void setUid(String uid) {
			this.uid = uid;
		}



		public String getZdzk() {
			return zdzk;
		}



		public void setZdzk(String zdzk) {
			this.zdzk = zdzk;
		}



		public String getTcfm() {
			return tcfm;
		}



		public void setTcfm(String tcfm) {
			this.tcfm = tcfm;
		}



		public int getNewmessage() {
			return newmessage;
		}



		public void setNewmessage(int newmessage) {
			this.newmessage = newmessage;
		}



		public int getNewdt() {
			return newdt;
		}



		public void setNewdt(int newdt) {
			this.newdt = newdt;
		}



		public String getNewtitle() {
			return newtitle;
		}



		public void setNewtitle(String newtitle) {
			this.newtitle = newtitle;
		}



		public String getNewdesc() {
			return newdesc;
		}



		public void setNewdesc(String newdesc) {
			this.newdesc = newdesc;
		}



		public String getUsername() {
			return username;
		}



		public void setUsername(String username) {
			this.username = username;
		}



		public String getPassword() {
			return password;
		}



		public void setPassword(String password) {
			this.password = password;
		}



		public boolean isIsadmin() {
			return isadmin;
		}



		public void setIsadmin(boolean isadmin) {
			this.isadmin = isadmin;
		}



	
}