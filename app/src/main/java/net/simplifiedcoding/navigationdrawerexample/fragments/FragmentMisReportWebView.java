package net.simplifiedcoding.navigationdrawerexample.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.simplifiedcoding.navigationdrawerexample.Constant.Constant;
import net.simplifiedcoding.navigationdrawerexample.Model.VisionModel;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.activities.MainActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;


public class FragmentMisReportWebView extends Fragment {



    public WebView mWebView;
    private TextView tvTitleName;
    private ProgressDialog progressDialog;
    private String urlEncodedPdfUrl = "";
    private String path = "";
    private String link;

    public FragmentMisReportWebView() {
        // Required empty public constructor
    }


    public static FragmentMisReportWebView newInstance() {
        FragmentMisReportWebView fragment = new FragmentMisReportWebView();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_misreport_webview, container, false);
        initView(rootView);
        return rootView;
    }


    void initView(View view) {
        try {
            link = getArguments().getString("link");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setPadding(0, 5, 10, 5);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(null);
        ((MainActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ImageView imageView = (ImageView) view.findViewById(R.id.btn_drawer);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).clickEventSlide();
            }
        });

        tvTitleName = (TextView) view.findViewById(R.id.tv_actionbar_title);
        tvTitleName.setText("MIS REPORT");

        mWebView = (WebView) view.findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String pdfUrl = link;

        try {
            urlEncodedPdfUrl = URLEncoder.encode(pdfUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        path = "http://docs.google.com/gview?embedded=true&url=" + urlEncodedPdfUrl;


        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(path);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                showMessage(view,"Something went wrong...");
            }
        });
        mWebView.loadUrl(path);

    }


    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main_drawer, menu); // removed to not double the menu items

    }


    @Override
    public void onDetach() {
        super.onDetach();

    }



    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    public void showMessage(View view, String tag) {
        if (view != null && view.isShown()) {
            Snackbar snack = Snackbar.make(view, tag, Snackbar.LENGTH_LONG);
            View v = snack.getView();
            TextView tv = (TextView) v.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }
    }
}
