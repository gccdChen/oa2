package scau.duolian.oa.model;
import net.tsz.afinal.annotation.sqlite.Id;
import scau.duolian.oa.base.BaseModel;
import scau.duolian.oa.util.DateUtil;
public class Message extends BaseModel{
		@Id
		public String id;
		public String mtype;
		public String author;
		public String source;
		public String title;
		public String subtitle;
		public String dt;
		
		public boolean isreaded = false;
		public Message() {
			// TODO Auto-generated constructor stub
		}
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public boolean isIsreaded() {
			return isreaded;
		}
		public void setIsreaded(boolean isreaded) {
			this.isreaded = isreaded;
		}
		public String getMtype() {
			return mtype;
		}
		public void setMtype(String mtype) {
			this.mtype = mtype;
		}
		public String getAuthor() {
			return author;
		}
		public void setAuthor(String author) {
			this.author = author;
		}
		public String getSource() {
			return source;
		}
		public void setSource(String source) {
			this.source = source;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getSubtitle() {
			return subtitle;
		}
		public void setSubtitle(String subtitle) {
			this.subtitle = subtitle;
		}
		public String getDt() {
			return dt;
		}
		public void setDt(String dt) {
			this.dt = dt;
		}
		
		public boolean find(String keyword){
			if(title !=null && title.contains(keyword))
				return true;
			if(subtitle !=null && subtitle.contains(keyword))
				return true;
			if(DateUtil.longStrToStr(dt).contains(keyword))
				return true;
			return false;
		}
}