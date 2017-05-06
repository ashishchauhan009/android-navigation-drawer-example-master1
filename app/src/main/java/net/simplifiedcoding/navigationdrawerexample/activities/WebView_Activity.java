package net.simplifiedcoding.navigationdrawerexample.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.util.Config;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WebView_Activity extends Activity {

    private String path = "";
    private String urlEncodedPdfUrl = "";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attachment_view);
        WebView mainWebView = (WebView) findViewById(R.id.webView1);
        WebSettings webSettings = mainWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mainWebView.setWebViewClient(new MyCustomWebViewClient());
        mainWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        String str = getIntent().getStringExtra(Config.Webview_url);

        // String pdfUrl = "http://che.org.il/wp-content/uploads/2016/12/pdf-sample.pdf";

        if (str.contains("jpeg") || str.contains("png") || str.contains("jpg")) {
            String imgSrcHtml = "<html><img src='" + str + "' /></html>";
            mainWebView.getSettings().setBuiltInZoomControls(true);
            mainWebView.loadData(imgSrcHtml, "text/html", "UTF-8");
        } else {

            try {
                urlEncodedPdfUrl = URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            path = "http://docs.google.com/gview?embedded=true&url=" + urlEncodedPdfUrl;
            mainWebView.loadUrl(path);
        }
    }

    private class MyCustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}