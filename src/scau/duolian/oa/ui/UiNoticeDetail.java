package scau.duolian.oa.ui;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import net.tsz.afinal.http.AjaxParams;

import scau.duolian.oa.R;
import scau.duolian.oa.base.BaseMessage;
import scau.duolian.oa.base.BaseUiAuth;
import scau.duolian.oa.base.C;
import scau.duolian.oa.base.MyCallBack;
import android.os.Bundle;
import android.util.Base64;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 通知详情
 *	参数 id 通知id
 */
public class UiNoticeDetail extends BaseUiAuth{
	private WebView webView = null;
	private String html = "";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_notice);
		initCon();
		initdata();
	}
	
	private String id = null;
	private void initCon() {
		// TODO Auto-generated method stub
		id = getIntent().getStringExtra("id");
		webView = (WebView) findViewById(R.id.wb_content);
		webView.getSettings().setJavaScriptEnabled(true);
	}
	
	private void refresh(){
		String content = getUnicodeContent() ;
		webView.loadDataWithBaseURL(null,content, "text/html", "UTF-8",null);
	}
	
	private void initdata() {
		// TODO Auto-generated method stub
		AjaxParams params = new AjaxParams();
		params.put("a","getupdate");
		params.put(C.params.dlyid,getUser().dlyid);
		params.put(C.params.uid,getUser().uid);
		params.put(C.params.id,id);
		params.put(C.params.mac,getMac());
		get(C.api.noticeDetail, params, new MyCallBack(this, true){
			@Override
			public void onResult(BaseMessage message) {
				// TODO Auto-generated method stub
				super.onResult(message);
				toast(message.getMessage());
				if(message.isSuccess()){
					html =new String( Base64.decode(message.getResult(), Base64.DEFAULT));
					refresh();
				}
			}
		});
	}
	private String getUnicodeContent() {
		// TODO Auto-generated method stub
//		return "<html><h1>helloworld</h1>你好~<img src=\"http://www.duolia.com/yun/upfile/uphoto.png\"></html>";
		return html;
	}
	/*private class HelloWebViewClient extends WebViewClient { 
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) { 
            view.loadUrl(url); 
            return true; 
        } 
    } */
}
