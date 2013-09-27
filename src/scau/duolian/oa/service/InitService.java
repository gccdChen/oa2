package scau.duolian.oa.service;

import java.util.List;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.http.AjaxParams;
import scau.duolian.oa.base.BaseApp;
import scau.duolian.oa.base.BaseMessage;
import scau.duolian.oa.base.BaseMessage.EntityEmptyException;
import scau.duolian.oa.base.BaseModel;
import scau.duolian.oa.base.BaseService;
import scau.duolian.oa.base.C;
import scau.duolian.oa.base.MyCallBackForService;
import scau.duolian.oa.model.Message;
import scau.duolian.oa.model.User;
import scau.duolian.oa.model.Wdbm;
import scau.duolian.oa.model.Wdhb;
import scau.duolian.oa.model.Wdks;
import scau.duolian.oa.model.Wdlc;
import scau.duolian.oa.model.Wdlcb;
import scau.duolian.oa.model.Wdlclx;
import scau.duolian.oa.model.Wdrc;
import scau.duolian.oa.model.Wdrw;
import scau.duolian.oa.model.Wdrwlx;
import scau.duolian.oa.model.Wdwj;
import scau.duolian.oa.model.Wdxm;
import scau.duolian.oa.ui.UiMessageCenter;
import scau.duolian.oa.util.ImageLoader;
import scau.duolian.oa.util.ImgDownload;
import android.content.Intent;
import android.util.Log;

/**
 * 
 *	获取数据初始化数据
 */
public class InitService extends BaseService{
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.i("InitService", "onCreate");
		updateDataFromWeb();
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}
	
	public User getUser() {
		// TODO Auto-generated method stub
		return ((BaseApp)getApplication()).owner;
	}
	
	public void updateDataFromWeb(){
		String dlyid = getUser().dlyid;
		String username = getUser().username;
		String password = getUser().password;
		
		AjaxParams params =	new AjaxParams();
		params.put("a", "login");
		params.put(C.params.dlyid, dlyid);
		params.put(C.params.username, username);
		params.put(C.params.password, password);
		params.put(C.params.mac, getMac());
		get(C.api.data, params, new MyCallBackForService(this){
			@Override
			public void onResult(BaseMessage message) {
				// TODO Auto-generated method stub
				super.onResult(message);
				try {
					User user =getUser() ;
					user.newmessage  = (Integer) message.getResult("newmessage"); 
					user.isadmin  = (Integer) message.getResult("isadmin") ==1; 
					
					saveInfo(message);
					
					android.os.Message msg = new android.os.Message();
					msg.what = UiMessageCenter.REFRESH_CODE;
					UiMessageCenter.handler.sendMessage(msg);
					
//					List<String> list = ImageLoader.getUrls(result);
//					ImgDownload.dir = C.dir.temp;
//					ImgDownload download = new ImgDownload(list);
//					download.start();
				} catch (EntityEmptyException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	FinalDb db = null;
	private int threadnum =2;
	String result  = null;
	private void saveInfo(BaseMessage message) {
		// TODO Auto-generated method stub
			List<Message> messages = (List<Message>)message.getResultList(C.model.message);
			List<Wdhb> wdhbs = (List<Wdhb>) message.getResultList(C.model.wdhb);
			List<Wdxm> wdxms = (List<Wdxm>) message.getResultList(C.model.wdxm);
			List<Wdrwlx> wdrwlxs = (List<Wdrwlx>) message.getResultList(C.model.wdrwlx);
			List<Wdrw> wdrws = (List<Wdrw>) message.getResultList(C.model.wdrw);
			List<Wdlcb> wdlcbs = (List<Wdlcb>) message.getResultList(C.model.wdlcb);
			List<Wdlclx> wdlclxs = (List<Wdlclx>) message.getResultList(C.model.wdlclx);
			List<Wdlc> wdlcs = (List<Wdlc>) message.getResultList(C.model.wdlc);
			List<Wdwj> wdwjs = (List<Wdwj>) message.getResultList(C.model.wdwj);
			List<Wdks> wdksList = (List<Wdks>) message.getResultList(C.model.wdks);
			List<Wdbm> wdbms = (List<Wdbm>) message.getResultList(C.model.wdbm);
			List<Wdrc> wdrcs = (List<Wdrc>) message.getResultList(C.model.wdrc);
			
			db = FinalDb.create(this);
			
			save(db, messages);
			save(db, wdhbs);
			save(db, wdxms);
			save(db, wdrwlxs);
			save(db, wdlcbs);
			save(db, wdrws);
			save(db, wdlclxs);
			save(db, wdlcs);
			save(db, wdwjs);
			save(db, wdksList);
			save(db, wdbms);
			save(db, wdrcs);
			result = message.getResult();
			
	}
	private <T> void save(FinalDb db,List<T> list){
		if(list!=null){
			for(T t:list){
//				if(t instanceof BaseModel){
					BaseModel bm = (BaseModel) t;
					Object o = db.findById(bm.getId(), t.getClass());
					if(o == null)
						db.save(t);
					else{
						if(t.getClass() == Message.class){
							Message m = (Message) o;
							Message l = (Message) t;
							l.isreaded = m.isreaded;
							db.update(l);
						}else
							db.update(t);
					}
//				}
			}
		}
	}
	
}
