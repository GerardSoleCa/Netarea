package com.grayditch.netarea;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class StatisticsActivity extends Activity{
	
	private WebView webView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		this.setTitle(extras.getString("TITLE"));
		
		final String mimeType = "text/html";
		
		setContentView(R.layout.webviewstatistics);
		
		extras = getIntent().getExtras();
		String source = "<HTTP><BODY><TABLE>"+extras.getString("SOURCE")+"</TABLE></BODY></HTTP>";
		webView = (WebView)findViewById(R.id.webview);		
		webView.getSettings().setJavaScriptEnabled(true);  
		webView.loadDataWithBaseURL("https://mitra.upc.es", source, mimeType, "utf-8", null);
		
	}
	
	

}
