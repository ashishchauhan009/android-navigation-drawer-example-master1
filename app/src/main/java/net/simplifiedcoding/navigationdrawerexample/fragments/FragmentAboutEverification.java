package net.simplifiedcoding.navigationdrawerexample.fragments;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class FragmentAboutEverification extends Fragment  implements Callback<VisionModel> {


    private OnFragmentInteractionListener mListener;
    private TextView tvTitleName,tvVerification;
    private LinearLayout llImageEverification;
    private VisionModel visionModel;
    private static final String TAG = FragmentAboutEverification.class.getSimpleName();
    private ProgressDialog pDialog;


    public FragmentAboutEverification() {
        // Required empty public constructor
    }


    public static FragmentAboutEverification newInstance() {
        FragmentAboutEverification fragment = new FragmentAboutEverification();
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
        View rootView = inflater.inflate(R.layout.fragment_about_everification, container, false);
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
        pDialog = new ProgressDialog(getContext(), R.style.DialogTheme);
        pDialog.setCancelable(true);
        llImageEverification = (LinearLayout) view.findViewById(R.id.ll_everification);
        tvVerification = (TextView) view.findViewById(R.id.tv_aboutverification);
        llImageEverification.setVisibility(View.GONE);
        tvTitleName = (TextView) view.findViewById(R.id.tv_actionbar_title);
        tvTitleName.setText("About");

        //call to api
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


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        IPMGKYDATA ipmgkydata = retrofit.create(IPMGKYDATA.class);
        Call<VisionModel> call = ipmgkydata.getData();
        call.enqueue(this);
    }
    @Override
    public void onResponse(Call<VisionModel> call, Response<VisionModel> response) {
        visionModel = response.body();
       // Log.e(TAG,visionModel.toString());
        if(visionModel!=null || visionModel.getData()!=null){
            if (pDialog.isShowing())
                pDialog.dismiss();
            if(Integer.parseInt(visionModel.getStatus()) == Constant.StatusCode.CLEANMONEY_CODE_SUCCESS){

                setAllPmgkyData();
            }
        }
    }

    @Override
    public void onFailure(Call<VisionModel> call, Throwable t) {

    }

    void setAllPmgkyData(){
        if(visionModel.getData()!=null||visionModel.getData().size()>0)
            tvVerification.setText(visionModel.getData().get(0).getBody());
        llImageEverification.setVisibility(View.VISIBLE);
    }
    public interface IPMGKYDATA {
        @GET(Constant.WebUrl.ABOUTVERFICATION)
        Call<VisionModel> getData();
    }
}
