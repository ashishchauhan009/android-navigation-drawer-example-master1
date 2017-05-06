package net.simplifiedcoding.navigationdrawerexample.fragments;

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
import net.simplifiedcoding.navigationdrawerexample.Model.DownloadModel;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.activities.MainActivity;
import net.simplifiedcoding.navigationdrawerexample.adapter.AdapterDownload;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;


public class FragmentDownload extends Fragment implements Callback<DownloadModel> {

    private static final String TAG = FragmentDownload.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private TextView tvTitleName;
    private RecyclerView recyclerView;
    private AdapterDownload adapterDownload;
    private DownloadModel downloadModelData;

    public FragmentDownload() {
        // Required empty public constructor
    }


    public static FragmentDownload newInstance() {
        FragmentDownload fragment = new FragmentDownload();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_download, container, false);
        initView(rootView);
        return rootView;
    }

    public void onClickDownloadPDF(View view)
    {
        //view.getVerticalScrollbarPosition();
        Log.e("ASHIESH",""+view.getVerticalScrollbarPosition());

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
        tvTitleName.setText("DOWNLOADS");

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_download);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        getDownloadServerData();

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

    private void getDownloadServerData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.WebUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        IDATA idata = retrofit.create(IDATA.class);
        Call<DownloadModel> call = idata.getData();
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<DownloadModel> call, Response<DownloadModel> response) {

        downloadModelData = response.body();

       // Log.e(TAG, downloadModelData.toString());
        if (downloadModelData != null) {
            if (Integer.parseInt(downloadModelData.getStatus()) == Constant.StatusCode.CLEANMONEY_CODE_SUCCESS) {
                adapterDownload = new AdapterDownload(getContext(), downloadModelData, FragmentDownload.this);
                recyclerView.setAdapter(adapterDownload);
            }
        }
    }

    @Override
    public void onFailure(Call<DownloadModel> call, Throwable t) {

    }
    public interface IDATA {
        @GET(Constant.WebUrl.DOWNLOADS)
        Call<DownloadModel> getData();
    }
}
