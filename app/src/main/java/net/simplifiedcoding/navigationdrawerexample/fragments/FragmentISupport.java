package net.simplifiedcoding.navigationdrawerexample.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import net.simplifiedcoding.navigationdrawerexample.Constant.Constant;
import net.simplifiedcoding.navigationdrawerexample.Model.MisLevel;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.activities.MainActivity;
import net.simplifiedcoding.navigationdrawerexample.adapter.AdapterImageEngage;
import net.simplifiedcoding.navigationdrawerexample.util.Config;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;
import retrofit2.http.Query;


public class FragmentISupport extends Fragment implements Callback<MisLevel>, View.OnClickListener {


    List<MisLevel.Datum> datumList;
    private ProgressDialog pDialog;
    private GridView gridView;
    private RecyclerView recyclerView;
    TextView tv_btnSupport, tv_count_support;


    public FragmentISupport() {
        // Required empty public constructor
    }


    public static FragmentISupport newInstance() {
        FragmentISupport fragment = new FragmentISupport();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_isupport, container, false);
        initView(rootView);
        return rootView;
    }


    void initView(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setPadding(0, 5, 10, 5);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(null);
        ((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ImageView imageView = (ImageView) view.findViewById(R.id.btn_drawer);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).clickEventSlide();
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        tv_btnSupport = (TextView) view.findViewById(R.id.tv_btnSupport);
        tv_count_support = (TextView) view.findViewById(R.id.tv_count_support);


        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        pDialog = new ProgressDialog(getContext(), R.style.DialogTheme);
        pDialog.setCancelable(true);


        newTaskApiCall(Config.Support);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main_drawer, menu); // removed to not double the menu items

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    void setallcontent() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.attachment_tv:
        }
    }

    @Override
    public void onResponse(Call<MisLevel> call, Response<MisLevel> response) {
        MisLevel engageSupportData = response.body();

        if (engageSupportData != null) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (Integer.parseInt(engageSupportData.getStatus()) == Constant.StatusCode.CLEANMONEY_CODE_SUCCESS) {
                if (engageSupportData.getData() != null && engageSupportData.getData().size() > 0) {
                    datumList = engageSupportData.getData();
                    AdapterImageEngage adapter = new AdapterImageEngage(getContext(), datumList);
                    recyclerView.setAdapter(adapter);
                    setallcontent();

                    if (!engageSupportData.getCount().isEmpty()) {
                        tv_count_support.setText(engageSupportData.getCount().toString().trim() + "People");
                    }
                }
            }
        }
    }

    @Override
    public void onFailure(Call<MisLevel> call, Throwable t) {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void newTaskApiCall(String type) {

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
        IENGAGEANDSUPPORT iengageandsupport = retrofit.create(IENGAGEANDSUPPORT.class);
        Call<MisLevel> call = iengageandsupport.getTask(type);
        Log.e(getContext() + "chek id", type.toString());
        call.enqueue(this);
    }


    public interface IENGAGEANDSUPPORT {
        @POST(Constant.WebUrl.ENGAGEANDSUPPORT)
        Call<MisLevel> getTask(@Query("type") String type);

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
