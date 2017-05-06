package net.simplifiedcoding.navigationdrawerexample.fragments;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.simplifiedcoding.navigationdrawerexample.Constant.Constant;
import net.simplifiedcoding.navigationdrawerexample.Model.CampaignModel;
import net.simplifiedcoding.navigationdrawerexample.Model.FaqModel;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.activities.MainActivity;
import net.simplifiedcoding.navigationdrawerexample.adapter.AdapterCampaign;


import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;


public class FragmentCampaign extends Fragment implements Callback<CampaignModel> {

    private static final String TAG = FragmentCampaign.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private TextView tvTitleName;
    private RecyclerView recyclerView;
    private AdapterCampaign adapterCampaignActivites;
    private CampaignModel campaignModelData;
    private ProgressDialog pDialog;

    public FragmentCampaign() {
        // Required empty public constructor
    }


    public static FragmentCampaign newInstance() {
        FragmentCampaign fragment = new FragmentCampaign();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_campaign, container, false);
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
        tvTitleName.setText("CAMPAIGNS");
        pDialog = new ProgressDialog(getContext(), R.style.DialogTheme);
        pDialog.setCancelable(true);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_campaign_activities);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        getCampaignServerData();

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

    private void getCampaignServerData() {

        if(pDialog != null){
            pDialog.setMessage(getString(R.string.loading));
            if(!pDialog.isShowing()){
                pDialog.show();
            }
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.WebUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        IDATA idata = retrofit.create(IDATA.class);
        Call<CampaignModel> call = idata.getData();
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<CampaignModel> call, Response<CampaignModel> response) {
        campaignModelData = response.body();
       // Log.e(TAG, campaignModelData.toString());
        if (campaignModelData != null) {
            if(pDialog.isShowing()){
                pDialog.dismiss();
            }
            if (Integer.parseInt(campaignModelData.getStatus()) == Constant.StatusCode.CLEANMONEY_CODE_SUCCESS) {

                adapterCampaignActivites = new AdapterCampaign(getContext(), campaignModelData, FragmentCampaign.this);
                recyclerView.setAdapter(adapterCampaignActivites);
            }
        }
    }

    @Override
    public void onFailure(Call<CampaignModel> call, Throwable t) {

    }
    public interface IDATA {
        @GET(Constant.WebUrl.CAMPAIGNS)
        Call<CampaignModel> getData();
    }
}
