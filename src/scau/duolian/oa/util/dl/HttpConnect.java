package scau.duolian.oa.util.dl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Environment;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.Toast;

public class HttpConnect {
	private static final String TAG = HttpConnect.class.getSimpleName();
	private static HashMap<String, String> ckmap = new HashMap();
	public static HttpConnect instance = null;
	public double nscale = 0;
	public float ndensity = 1;
	public double nLatitude = 0;
	public double nLongitude = 0;
	public String sAddr = "";
	public Activity act = null;
	public View view = null;
	public String smac = "";
	public String sroot = Environment.getExternalStorageDirectory().getPath() + "/duolian";
	public boolean bGpsStart = false;
	/** Called when the activity is first created. */
	public LocationManager locationManager;
	public LocationListener llistener;
	public String provider;

	public HttpConnect() {
	}

	public static HttpConnect getInstance() {
		if (instance == null)
			instance = new HttpConnect();
		return instance;
	}

	public static Runnable mWaitRunnableGPS = new Runnable() {
		public void run() {
			if(instance == null)
				return;
			if(instance.act == null)
				return;
			Toast.makeText(instance.act, "GPS成功定位，可以进行考勤!", 0).show();
		}
	};

	public void addCookies(HttpGet paramHttpGet) {
		StringBuilder sb = new StringBuilder();
		Iterator iter = ckmap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry en = (Map.Entry) iter.next();
			String str1 = en.getKey().toString();
			String str2 = en.getValue().toString();
			sb.append(str1);
			sb.append("=");
			sb.append(str2);
			sb.append(";");
		}
		paramHttpGet.addHeader("cookie", sb.toString());
	}

	public void addCookies(HttpPost paramHttpPost) {
		StringBuilder sb = new StringBuilder();
		Iterator iter = ckmap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry localEntry = (Map.Entry) iter.next();
			String str1 = localEntry.getKey().toString();
			String str2 = localEntry.getValue().toString();
			sb.append(str1);
			sb.append("=");
			sb.append(str2);
			sb.append(";");
		}
		paramHttpPost.addHeader("cookie", sb.toString());
	}

	public void saveCookies(String sck) {
		if (sck == null || sck.length() <= 0)
			return;
		String[] as1 = sck.split(";");
		if (as1.length <= 0) {
			return;
		}
		for (int n = 0; n < as1.length; n++) {
			String[] as2 = as1[n].split("=");
			if (as2.length <= 1)
				continue;
			ckmap.put(as2[0].trim(), as2[1].trim());
		}
	}

	public boolean saveCookies(HttpResponse hr) {
		// QLog.i(TAG, "saveCookies1");
		Header[] hdrs = hr.getHeaders("Set-Cookie");
		if (hdrs == null)
			return false;
		// QLog.i(TAG, "saveCookies2");
		String[] arrayOfString1;
		for (int i = 0; i < hdrs.length; i++) {
			arrayOfString1 = hdrs[i].getValue().split(";");
			if (arrayOfString1.length > 0) {
				// QLog.i(TAG, "saveCookies3");
				String[] pair = arrayOfString1[0].split("=");
				if (pair.length <= 1)
					break;
				// QLog.i(TAG, "saveCookies4 " + arrayOfString1[0]);
				ckmap.put(pair[0].trim(), pair[1].trim());
				return true;
			}
		}
		// QLog.i(TAG, "saveCookies5");
		return false;
	}

	public boolean login(String sname, String spass, String smac) {
		QLog.i(TAG, "<br>login");
		this.clear();
		if(sname == null || sname.length() <= 0 || spass == null || spass.length() <= 0 || smac == null || smac.length() <= 0)
			return false;
		String str1 = "http://www.duolia.com/yun/pad/ywyl.jsp?c=sync&a=loginweb&username="
				+ sname + "&password=" + spass + "&mac=" + smac;
		HttpGet localHttpGet = new HttpGet(str1);
		QLog.i(TAG, "<br>login3"+str1);
		try {
			HttpResponse hr = new DefaultHttpClient().execute(localHttpGet);
			QLog.i(TAG, "<br>login4 sc:" + hr.getStatusLine().getStatusCode());
			if (hr.getStatusLine().getStatusCode() == 200) {
				// QLog.i(TAG, "login5");
				saveCookies(hr);
				String sjson = EntityUtils.toString(hr.getEntity());
				QLog.i(TAG, "login8" + sjson);
				try {
					JSONObject j1 = new JSONObject(sjson);
					int i = j1.getInt("code");
					this.setCode(i);
					this.setDesc(j1.getString("desc"));
					JSONObject j2 = j1.getJSONObject("data");
					String str3 = (String) j2.get("dlyid");
					String str4 = (String) j2.get("customer");
					this.setUid(j2.getString("uid"));
					this.setDlyid(str3);
					this.setCustomer(str4);
					long newcount = j2.getLong("newmessage");
					this.setmmcount(newcount);
					long newdt = j2.getLong("newdt");
					this.setnewdt(newdt);
					String strnewtitle = (String) j2.get("newtitle");
					this.setnewtitle(strnewtitle);
					String strnewdesc = (String) j2.get("newdesc");
					this.setnewdesc(strnewdesc);
					if (i == 999) {
						// QLog.i(TAG, "login10");
						return true;
					}
				} catch (JSONException je) {
					QLog.i(TAG, "login exception:" + je.getMessage());
				}
				QLog.i(TAG, "login9:" + this.getCode());
				return false;
			}
		} catch (IOException e) {
			QLog.i(TAG, "login7:" + e.getMessage());
		}
		QLog.i(TAG, "login6");
		return false;
	}

	public boolean downloadUrl(String surl, String starget) {
		// QLog.i(TAG, "downloadUrl1 ");
		HttpGet httpget = new HttpGet(surl);
		CookieManager cookieManager = CookieManager.getInstance();
		// QLog.i(TAG, "downloadUrl hasCookies1");
		if (cookieManager.hasCookies()) {
			// QLog.i(TAG, "downloadUrl hasCookies2");
			String s = "";// cookieManager.getCookie(this.geturl());
			// QLog.i(TAG, "downloadUrl cookie " + s);
			// 获取cookie
			if (s == null || s.length() <= 0) {
				login("", "", "");
			}
			saveCookies(s);
			addCookies(httpget);
		} else {
			// QLog.i(TAG, "login cookie1 ");
			if (login("", "", "")) {
				// QLog.i(TAG, "login cookie2 ");
				addCookies(httpget);
			}
			// QLog.i(TAG, "login cookie3 ");
		}
		DefaultHttpClient client = new DefaultHttpClient();
		boolean bool = false;
		// QLog.i(TAG, "downloadUrl start ");
		FileOutputStream fos = null;
		try {
			HttpResponse httpres = client.execute(httpget);
			// QLog.i(TAG, "downloadUrl status " + httpres.getStatusLine().getStatusCode());
			if (httpres.getStatusLine().getStatusCode() == 200) {
				// QLog.i(TAG, "downloadUrl status ok ");
				File ftarget = new File(starget);
				if (ftarget.exists() == false)
					ftarget.createNewFile();
				InputStream is = httpres.getEntity().getContent();
				fos = new FileOutputStream(ftarget);
				byte[] ab = new byte[4096];
				int i = 0;
				int n = 0;
				while ((i = is.read(ab)) >= 0) {
					// QLog.i(TAG, "downloadUrl " + (n++) + "=" + i + "bytes");
					if (i > 0)
						fos.write(ab, 0, i);
				}
				fos.flush();
				bool = true;
				// QLog.i(TAG, "downloadUrl complete");
			}
			// QLog.i(TAG, "downloadUrl2 ");
		} catch (ClientProtocolException cpe) {
			// QLog.i(TAG, "downloadUrl cpe " + cpe.getMessage());
			bool = false;
		} catch (IOException e) {
			// QLog.i(TAG, "downloadUrl ioe " + e.getMessage());
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// QLog.i(TAG, "downloadUrl ioe2 " + e.getMessage());
				}
			}
		}
		return bool;
	}

	public String addjf(String skh, String sxlh, String smm, String smac) {
		this.clear();
		String str1 = "http://www.duolia.com/yun/pad/jfdy.jsp?c=sync&a=add&dlyid="
				+ this.getDlyid()
				+ "&uid="
				+ this.getUid()
				+ "&kh="
				+ skh
				+ "&xlh=" + sxlh + "&mm=" + smm + "&mac=" + smac;
		try {
			JSONObject json = getJSONFromURL(str1);
			if (json == null)
				return "增加积分失败";
			int i = json.getInt("code");
			this.setCode(i);
			if (i != 999) {
				return json.getString("desc");
			} else {
				this.setXJf(json.getString("data"));
				this.setJf(json.getString("zjf"));
				this.setmc(json.getString("mc"));
				this.setxh(json.getString("xh"));
				return json.getString("desc");
			}
		} catch (JSONException e) {
			// QLog.i(TAG, "addjf"+e.getMessage());
			return "增加积分失败";
		}
	}

	public String getkqdt(String smac, double nlo, double nla, String saddr) {
		this.clear();
		String str1 = "http://www.duolia.com/yun/pad/ywykq.jsp?c=sync&a=add&dlyid="
				+ this.getDlyid()
				+ "&uid="
				+ this.getUid()
				+ "&cdjs=&mac=" + smac
				+ "&addr=" + saddr
				+ "&lo=" + nlo + "&la=" + nla;
		try {
			JSONObject json = getJSONFromURL(str1);
			if (json == null)
			{
				this.setDesc("获取考勤时间失败");
				return "获取考勤时间失败";
			}
			int i = json.getInt("code");
			this.setCode(i);
			if (i != 999) {
				QLog.i(TAG, "getkqdt:"+i+" "+json.getString("desc"));
				return json.getString("desc");
			} else {
				this.setkqdt(json.getString("rq"));
				this.setkqtime(json.getString("sj"));
				this.setkqip(json.getString("ip"));
				this.setkqgsd(json.getString("gsd"));
				this.setkqcdzt(json.getString("cdzt"));
				this.setkqreason(json.getString("cdjs"));
				this.setDesc(json.getString("desc"));
				return json.getString("desc");
			}
		} catch (JSONException e) {
			QLog.i(TAG, "getkqdt"+e.getMessage());
			this.setDesc("获取考勤时间失败");
			return "获取考勤时间失败";
		}
	}

	public String addkq(String sreason, String smac, double nlo, double nla, String saddr) {
		this.clear();
		try {
			sreason = URLEncoder.encode(sreason, "UTF-8");
		} catch (Exception e) {
			QLog.i(TAG, "addkq"+e.getMessage());
			this.setDesc("考勤失败");
			return "考勤失败";
		}
		String str1 = "http://www.duolia.com/yun/pad/ywykq.jsp?c=sync&a=update&dlyid="
				+ this.getDlyid()
				+ "&uid="
				+ this.getUid()
				+ "&cdjs="
				+ sreason
				+ "&mac=" + smac
				+ "&addr=" + saddr
				+ "&lo=" + nlo + "&la=" + nla;
		try {
			JSONObject json = getJSONFromURL(str1);
			if (json == null)
			{
				this.setDesc("考勤失败");
				return "考勤失败";
			}
			int i = json.getInt("code");
			this.setCode(i);
			if (i != 999) {
				QLog.i(TAG, "getkq:"+i+" "+json.getString("desc"));
				return json.getString("desc");
			} else {
				this.setkqdt(json.getString("rq"));
				this.setkqtime(json.getString("sj"));
				this.setkqip(json.getString("ip"));
				String sgsd = json.getString("gsd");
				if(sgsd.length() > 0)
					sgsd = sgsd.replace("&nbsp;", " ");
				this.setkqgsd(sgsd);
				this.setkqcdzt(json.getString("cdzt"));
				this.setkqreason(json.getString("cdjs"));
				this.setDesc(json.getString("desc"));
				return json.getString("desc");
			}
		} catch (JSONException e) {
			QLog.i(TAG, "addkq"+e.getMessage());
			this.setDesc("考勤失败");
			return "考勤失败";
		}
	}

	public String addrb(String sreason, String smac) {
		this.clear();
		try {
			sreason = URLEncoder.encode(sreason, "UTF-8");
		} catch (Exception e) {
			QLog.i(TAG, "addrb"+e.getMessage());
			this.setDesc("提交日报失败");
			return "提交日报失败";
		}
		String str1 = "http://www.duolia.com/yun/pad/ywyrb.jsp?c=sync&a=add&dlyid="
				+ this.getDlyid()
				+ "&uid="
				+ this.getUid()
				+ "&content="
				+ sreason
				+ "&mac=" + smac;
		try {
			JSONObject json = getJSONFromURL(str1);
			if (json == null)
			{
				this.setDesc("提交日报失败");
				return "提交日报失败";
			}
			int i = json.getInt("code");
			this.setCode(i);
			if (i != 999) {
				QLog.i(TAG, "addrb:"+i+" "+json.getString("desc"));
				this.setDesc(json.getString("desc"));
				return json.getString("desc");
			} else {
				this.setkqdt(json.getString("rq"));
				this.setkqreason(json.getString("data"));
				this.setDesc(json.getString("desc"));
				return json.getString("desc");
			}
		} catch (JSONException e) {
			QLog.i(TAG, "addrb"+e.getMessage());
			this.setDesc("提交日报失败");
			return "提交日报失败";
		}
	}

	public String getdyjf(String smac) {
		this.clear();
		String str1 = "http://www.duolia.com/yun/pad/jfdy.jsp?c=sync&a=search&dlyid="
				+ this.getDlyid() + "&uid=" + this.getUid() + "&mac=" + smac;
		try {
			JSONObject json = getJSONFromURL(str1);
			if (json == null)
				return "查询积分失败";
			int i = json.getInt("code");
			this.setCode(i);
			if (i != 999) {
				return json.getString("desc");
			} else {
				this.setJf(json.getString("zjf"));
				return json.getString("desc");
			}
		} catch (JSONException e) {
			// QLog.i(TAG, "getdyjf"+e.getMessage());
		}
		return "查询积分失败";
	}

	public String getHyInfoByPhone(String skh, String smac) {
		this.clear();
		String str1 = "http://www.duolia.com/yun/pad/l.jsp?c=sync&a=search&dlyid="
				+ this.getDlyid()
				+ "&uid="
				+ this.getUid()
				+ "&kh=&sj="
				+ skh
				+ "&mac=" + smac;
		try {
			JSONObject json = getJSONFromURL(str1);
			if (json == null)
				return "查询会员失败";
			int i = json.getInt("code");
			this.setCode(i);
			if (i != 999) {
				return json.getString("desc");
			} else {
				JSONObject json2 = json.getJSONObject("data");
				this.setHyid(json2.getString("hyid"));
				this.setHyname(json2.getString("hyname"));
				this.setJf(json2.getString("jf"));
				return json.getString("desc");
			}
		} catch (JSONException e) {
			// QLog.i(TAG, "getHyInfoByPhone"+e.getMessage());
			return "查询会员失败";
		}
	}

	public JSONObject getJSONFromURL(String surl) {
		HttpGet localHttpGet = new HttpGet(surl);
		addCookies(localHttpGet);
		DefaultHttpClient hc = new DefaultHttpClient();
		try {
			HttpResponse hr = hc.execute(localHttpGet);
			if (hr.getStatusLine().getStatusCode() == 200) {
				String sjson = EntityUtils.toString(hr.getEntity());
				// QLog.i(TAG, "getJSONFromURL" + sjson);
				try {
					return new JSONObject(sjson);
				} catch (JSONException je) {
					QLog.i(TAG, "getJSONFromURL1:"+je.getMessage());
				}
			}
		} catch (ClientProtocolException e) {
			QLog.i(TAG, "getJSONFromURL2:"+e.getMessage());
		} catch (IOException ie) {
			QLog.i(TAG, "getJSONFromURL3:"+ie.getMessage());
		}
		return null;
	}

	public boolean getnotices(int npageid) {
		// QLog.i(TAG, "getnotices");
		boolean bool = false;
//		String str = "http://www.duolia.com/yun/pad/ddy.jsp?c=sync&a=getupdate&dlyid="
//				+ getDlyid()
//				+ "&uid="
//				+ getUid()
//				+ "&pageid="
//				+ npageid
//				+ "&lastdt="
//				+ noticedao.slastdt
//				+ "&mac="
//				+ LoginActivity.smac;
//		try {
//			// QLog.i(TAG, "getnotices1"+str);
//			JSONObject json = getJSONFromURL(str);
//			if ((json != null) && (json.getInt("code") == 999))
//			{
//				// QLog.i(TAG, "getnotices2");
//				noticedao.npages = json.getInt("pages");
//				if(noticedao.npages > 0)
//				{
//					noticedao.slastdt = json.getString("lastdatatime");
//					JSONObject duolian = json.getJSONObject("data").getJSONObject("duolian");
//					JSONArray jnews = duolian.getJSONArray("news");
//					if(jnews.length() > 0)
//					{
//						noticedao.loadFormJSONArray(this, jnews);
////						noticedao.saveBrand();
//						bool = true;
//						// QLog.i(TAG, "getnotices succ");
//					}
//				}
//			}
//		} catch (JSONException e) {
//			// QLog.i(TAG, "getnotices"+e.getMessage());
//		}
		return bool;
	}

	public boolean getnotice(Notice b) {
		// QLog.i(TAG, "getnotices");
		boolean bool = false;
//		String str = "http://www.duolia.com/yun/pad/ddyc.jsp?c=sync&a=getupdate&dlyid="
//				+ getDlyid()
//				+ "&uid="
//				+ getUid()
//				+ "&newsid="
//				+ b.getId()
//				+ "&mac="
//				+ LoginActivity.smac;
//		try {
//			// QLog.i(TAG, "getnotice1");
//			JSONObject json = getJSONFromURL(str);
//			if ((json != null) && (json.getInt("code") == 999))
//			{
//				// QLog.i(TAG, "getnotice2");
//				String sbase64 = json.getString("data");
//				if(sbase64.length() > 0)
//				{
//					String s = "";
//					try {
//						s = new String(Base64.decode(sbase64, 0), "utf-8");
//						//s = URLEncoder.encode(s);
//						//// QLog.i(TAG, "Base64 "+s);
//					} catch (Exception e1) {
//						// QLog.i(TAG, "Base64 "+e1.getMessage());
//					}
//					b.setdesc(s);
//					b.setisread("1");
//					noticedao.saveBrand(b);
//					bool = true;
//				}
//			}
//		} catch (JSONException e) {
//			// QLog.i(TAG, "getnotice"+e.getMessage());
//		}
		return bool;
	}


	public boolean getwukkongs(String swlm) {
		QLog.i(TAG, "getwukkongs");
		boolean bool = false;
//		wukongdao.listBrand.clear();
//		String str = "http://www.duolia.com/yun/pad/ywywlgz.jsp?c=sync&a=getall&dlyid="
//				+ getDlyid()
//				+ "&uid="
//				+ getUid()
//				+ "&wlm="
//				+ swlm
//				+ "&mac="
//				+ LoginActivity.smac;
//		try {
//			// QLog.i(TAG, "getwukkongs1"+str);
//			JSONObject json = getJSONFromURL(str);
//			if ((json != null) && (json.getInt("code") == 999))
//			{
//				String sspmc = json.getString("spmc");
//				String sspxh = json.getString("spxh");
//				JSONArray duolian = json.getJSONArray("data");
//				if(duolian.length() > 0)
//				{
//					wukongdao.loadFormJSONArray(this, duolian, sspmc, sspxh, swlm);
//					bool = true;
//					// QLog.i(TAG, "getwukkongs succ");
//				}
//			}
//		} catch (JSONException e) {
//			QLog.i(TAG, "getwukkongs"+e.getMessage());
//		}
		return bool;
	}
	
	public boolean getFileFromURL(String surl, String spath,
			String sfile) {
		// QLog.i(TAG, "getFileFromURL" + surl + " to " + spath + sfile);
		HttpGet hg = new HttpGet(surl);
		addCookies(hg);
		DefaultHttpClient dhc = new DefaultHttpClient();
		boolean bool = false;
		try {
			HttpResponse hr = dhc.execute(hg);
			if (hr.getStatusLine().getStatusCode() == 200) {
				File lfile = new File(spath + sfile);
				if (lfile.exists() == false)
				{
					// QLog.i(TAG, "getFileFromURL3");
					lfile.createNewFile();
					InputStream is1 = hr.getEntity().getContent();
					// QLog.i(TAG, "getFileFromURL4");
					FileOutputStream fos1 = new FileOutputStream(lfile);
					byte[] ab1 = new byte[4096];
					// QLog.i(TAG, "getFileFromURL5");
					while (true) {
						int i = is1.read(ab1);
						if (i == -1) {
							fos1.flush();
							fos1.close();
							// QLog.i(TAG, "getFileFromURL succ");
							bool = true;
							break;
						}
						fos1.write(ab1, 0, i);
					}
				}
			}
		} catch (ClientProtocolException ce) {
			// QLog.i(TAG, "getFileFromURL" + ce.getMessage());
			bool = false;
		} catch (IOException e1) {
			// QLog.i(TAG, "getFileFromURL2" + e1.getMessage());
		}
		return bool;
	}

	public String downloadFile(String surl, String spath) {
		// QLog.i(TAG, surl);
		String sfile;
		if ((surl == null) || (surl.length() < 30))
		{
			return "";
		}
		sfile = getFilename(surl);
		// QLog.i(TAG, "file");
		getFileFromURL(surl, spath, sfile);
		return sfile;
	}

	public String[] downloadImage(String[] surls, String spath) {
		if(surls == null || spath == null || spath.length() < 2)
			return null;
		String[] r = new String[surls.length];
		for (int j = 0; j < surls.length; j++) {
			if ((surls[j] != null) && (surls[j].length() > 10)) {
				// QLog.i(TAG+j, surls[j]);
				r[j] = getFilename(surls[j]);
				if(r[j].length() > 0)
				{
					File f = new File(spath + r[j]);
					// 已存在不下载
					if(f.exists() == false)
						getFileFromURL(surls[j], spath, r[j]);
					r[j] = spath + r[j];
					// QLog.i(TAG+j, r[j]);
				}
			}
		}
		return r;
	}

	private String getFilename(String surl) {
		for (int i = surl.length() - 1; i > 0; i--) {
			if (surl.charAt(i) == '/') {
				String str = surl.substring(i);
				return str;
			}
		}
		return "";
	}

	private int code = 0;
	private long mmcount = 0;
	private long nnewdt = 0;
	private String customer = "";
	private String desc = "";
	private String dlyid = "3";
	private String uid = "";
	private String hyid = "";
	private String hyname = "";
	private String jf = "";
	private String xjf = "";
	private String mc = "";
	private String xh = "";
	private String skqdt = "";
	private String skqtime = "";
	private String skqip = "";
	private String skqgsd = "";
	private String skqreason = "";
	private String skqcdzt = "";
	private String newtitle = "";
	private String newdesc = "";

	public void clear() {
		code = 0;
		desc = "";
		hyid = "";
		hyname = "";
		xjf = "";
		mc = "";
		xh = "";
	}

	public int getCode() {
		return this.code;
	}

	public String getHyid() {
		return this.hyid;
	}

	public String getHyname() {
		return this.hyname;
	}

	public void setHyid(String paramString) {
		this.hyid = paramString;
	}

	public void setHyname(String paramString) {
		this.hyname = paramString;
	}

	public String getJf() {
		return this.jf;
	}

	public void setJf(String paramString) {
		this.jf = paramString;
	}

	public String getmc() {
		return this.mc;
	}

	public void setmc(String paramString) {
		this.mc = paramString;
	}

	public String getxh() {
		return this.xh;
	}

	public void setxh(String paramString) {
		this.xh = paramString;
	}

	public String getCustomer() {
		return this.customer;
	}

	public String getDesc() {
		return this.desc;
	}

	public String getDlyid() {
		return this.dlyid;
	}

	public void setCode(int paramInt) {
		this.code = paramInt;
	}

	public void setCustomer(String paramString) {
		this.customer = paramString;
	}

	public void setDesc(String paramString) {
		this.desc = paramString;
	}

	public void setDlyid(String paramString) {
		this.dlyid = paramString;
	}

	public String getUid() {
		return this.uid;
	}

	public void setUid(String paramString) {
		this.uid = paramString;
	}

	public long getmmcount() {
		return this.mmcount;
	}

	public void setmmcount(long n) {
		this.mmcount = n;
	}

	public long getnewdt() {
		return this.nnewdt;
	}

	public void setnewdt(long n) {
		this.nnewdt = n;
	}

	public void setnewtitle(String paramString) {
		this.newtitle = paramString;
	}

	public String getnewtitle() {
		return this.newtitle;
	}

	public void setnewdesc(String paramString) {
		this.newdesc = paramString;
	}

	public String getnewdesc() {
		return this.newdesc;
	}

	public void setXJf(String paramString) {
		this.xjf = paramString;
	}

	public String getXJf() {
		return this.xjf;
	}

	public String getkqdt() {
		return this.skqdt;
	}

	public void setkqdt(String s) {
		this.skqdt = s;
	}

	public String getkqtime() {
		return this.skqtime;
	}

	public void setkqtime(String s) {
		this.skqtime = s;
	}

	public String getkqip() {
		return this.skqip;
	}

	public void setkqip(String s) {
		this.skqip = s;
	}

	public String getkqgsd() {
		return this.skqgsd;
	}

	public void setkqgsd(String s) {
		this.skqgsd = s;
	}

	public String getkqreason() {
		return this.skqreason;
	}

	public void setkqreason(String s) {
		this.skqreason = s;
	}

	public String getkqcdzt() {
		return this.skqcdzt;
	}

	public void setkqcdzt(String s) {
		this.skqcdzt = s;
	}
}
