package net.simplifiedcoding.navigationdrawerexample.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.simplifiedcoding.navigationdrawerexample.Constant.Constant;
import net.simplifiedcoding.navigationdrawerexample.Model.VisionModel;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.activities.MainActivity;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;


public class FragmentHelpDialog extends DialogFragment implements View.OnClickListener, Callback<VisionModel> {


    private OnFragmentInteractionListener mListener;
    private TextView tvTitleName,helptext;
    private ImageButton ivBackbtn;
    private ProgressDialog pDialog;
    private VisionModel visionModel;


    public FragmentHelpDialog() {
        // Required empty public constructor
    }



    public static FragmentHelpDialog newInstance() {
        FragmentHelpDialog fragment = new FragmentHelpDialog();
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
        View rootView = inflater.inflate(R.layout.fragment_help_dialog, container, false);
        initView(rootView);
        return rootView;
    }


    void initView(View view){
       /* WebView webView = (WebView) view.findViewById(R.id.webview_help);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webView.setWebViewClient(new WebViewClient());


            webView.loadUrl("file:///android_asset/help.html");


        view.findViewById(R.id.imgbtn_navigation_up).setOnClickListener(this);
        view.findViewById(R.id.btn_dismiss).setOnClickListener(this);*/
        ivBackbtn = (ImageButton) view.findViewById(R.id.iv_back_btn);
     //   helptext  = (TextView) view.findViewById(R.id.help);
        pDialog = new ProgressDialog(getContext(), R.style.DialogTheme);
        pDialog.setCancelable(true);
        ivBackbtn.setOnClickListener(this);
        getDataFronServer();


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
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_back_btn:
                getDialog().dismiss();
                break;
        }

    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    void getDataFronServer(){
        if (pDialog != null) {
            pDialog.setMessage(getString(R.string.loading));
            if (!pDialog.isShowing())
                pDialog.show();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.WebUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
       IVISIONDATA ivisiondata = retrofit.create(IVISIONDATA.class);
        Call<VisionModel> call = ivisiondata.getData();
        call.enqueue(this);
    }
    @Override
    public void onResponse(Call<VisionModel> call, Response<VisionModel> response) {
        visionModel = response.body();

        if(visionModel!=null){
            if (pDialog.isShowing())
                pDialog.dismiss();

            if(Integer.parseInt(visionModel.getStatus()) == Constant.StatusCode.CLEANMONEY_CODE_SUCCESS){


            }
        }
    }

    @Override
    public void onFailure(Call<VisionModel> call, Throwable t) {

    }
    public interface IVISIONDATA {
        @GET(Constant.WebUrl.VISION)
        Call<VisionModel> getData();
    }
}
