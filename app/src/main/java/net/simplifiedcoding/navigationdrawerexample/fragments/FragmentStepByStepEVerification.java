package net.simplifiedcoding.navigationdrawerexample.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.activities.MainActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class FragmentStepByStepEVerification extends Fragment {

    public WebView mWebView;
    private TextView tvTitleName;
    private ProgressDialog progressDialog;
    private String urlEncodedPdfUrl = "";
    private String path = "";


    public FragmentStepByStepEVerification() {
        // Required empty public constructor
    }


    public static FragmentStepByStepEVerification newInstance() {
        FragmentStepByStepEVerification fragment = new FragmentStepByStepEVerification();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stepbystep_everification, container, false);
        initView(rootView);

        return rootView;
    }


    void initView(View view) {
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
        tvTitleName.setText("Step By Step eVerification");

        mWebView = (WebView) view.findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String pdfUrl = "https://incometaxindiaefiling.gov.in/eFiling/Portal/StaticPDF/e-Verification_User_Manual.pdf";

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
                Toast.makeText(getContext(), "Error:" + description, Toast.LENGTH_SHORT).show();

            }
        });
        mWebView.loadUrl(path);
    }





    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main_drawer, menu); // removed to not double the menu items
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }
   /* private OnFragmentInteractionListener mListener;
    private TextView tvTitleName;

    public FragmentStepByStepEVerification() {
        // Required empty public constructor
    }


    public static FragmentStepByStepEVerification newInstance() {
        FragmentStepByStepEVerification fragment = new FragmentStepByStepEVerification();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stepbystep_everification, container, false);
        initView(rootView);
        return rootView;
    }



    void initView(View view){
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
        tvTitleName.setText("Step By Step");

    }
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main_drawer, menu); // removed to not double the menu items

    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }*/
}
