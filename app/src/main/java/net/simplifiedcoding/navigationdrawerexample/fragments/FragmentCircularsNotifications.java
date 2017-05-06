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
import net.simplifiedcoding.navigationdrawerexample.Model.VisionModel;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.activities.MainActivity;
import net.simplifiedcoding.navigationdrawerexample.adapter.AdapterImportantLinks;
import net.simplifiedcoding.navigationdrawerexample.adapter.Adaptercircularsnotifications;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;


public class FragmentCircularsNotifications extends Fragment implements Callback<VisionModel> {

    private static final String TAG = FragmentCircularsNotifications.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private VisionModel visionModel;
    private ProgressDialog pDialog;

    private RecyclerView recyclerView;
    private Adaptercircularsnotifications adaptercircularsnotifications;
    private TextView tvTitleName;

    public FragmentCircularsNotifications() {
        // Required empty public constructor
    }


    public static FragmentCircularsNotifications newInstance() {
        FragmentCircularsNotifications fragment = new FragmentCircularsNotifications();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_circular_notifications, container, false);
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
        pDialog = new ProgressDialog(getContext(), R.style.DialogTheme);
        pDialog.setCancelable(true);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_imp_links_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        tvTitleName.setText("Circulars &amp; Notifications");
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
        //Log.e(TAG,visionModel.toString());
        if(visionModel!=null || visionModel.getData()!=null){
            if (pDialog.isShowing())
                pDialog.dismiss();
            if(Integer.parseInt(visionModel.getStatus()) == Constant.StatusCode.CLEANMONEY_CODE_SUCCESS){

                adaptercircularsnotifications = new Adaptercircularsnotifications(getActivity(),visionModel);
                recyclerView.setAdapter(adaptercircularsnotifications);

            }
        }
    }

    @Override
    public void onFailure(Call<VisionModel> call, Throwable t) {

    }

    public interface IPMGKYDATA {
        @GET(Constant.WebUrl.CIRCULARS)
        Call<VisionModel> getData();
    }

}
