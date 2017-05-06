package net.simplifiedcoding.navigationdrawerexample.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.simplifiedcoding.navigationdrawerexample.Constant.Constant;
import net.simplifiedcoding.navigationdrawerexample.Model.Datum;
import net.simplifiedcoding.navigationdrawerexample.Model.Search;
import net.simplifiedcoding.navigationdrawerexample.Model.SuccessStoriesData;
import net.simplifiedcoding.navigationdrawerexample.Model.UserSearch;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.activities.MainActivity;
import net.simplifiedcoding.navigationdrawerexample.adapter.AdapterFavTask;
import net.simplifiedcoding.navigationdrawerexample.adapter.AdapterSuccessStories;
import net.simplifiedcoding.navigationdrawerexample.util.AndroidUtil;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class FragmentSuccessStoriesList extends Fragment implements Callback<SuccessStoriesData>, AdapterSuccessStories.ClickListener, View.OnClickListener {

    private static final String TAG = FragmentSuccessStoriesList.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private TextView tvTitleName;
    private RecyclerView recyclerView;
    private AdapterSuccessStories adapterSuccessStories;
    private SuccessStoriesData successStoriesData;
    private ProgressDialog pDialog;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String user_id;
    List<SuccessStoriesData.Datum> datumList;
    TextView no_task_tv, task_name_tv;
    private ImageView add_success_iv;
    View rootView;

    public FragmentSuccessStoriesList() {
        // Required empty public constructor
    }


    public static FragmentSuccessStoriesList newInstance() {
        FragmentSuccessStoriesList fragment = new FragmentSuccessStoriesList();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_success_stories_list, container, false);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        editor = sharedpreferences.edit();
        initView(rootView);

        return rootView;
    }


    void initView(View view) {

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        getFragmentManager().popBackStackImmediate();
                        return true;
                    }
                }
                return false;
            }
        });
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


        tvTitleName = (TextView) view.findViewById(R.id.tv_actionbar_title);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_sstory);
        no_task_tv = (TextView) view.findViewById(R.id.no_task_tv);
        task_name_tv = (TextView) view.findViewById(R.id.task_name_tv);
        add_success_iv = (ImageView) view.findViewById(R.id.add_success_iv);
        add_success_iv.setVisibility(View.VISIBLE);

        task_name_tv.setText(getString(R.string.success_storiess));
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
//        layoutManager.setSmoothScrollbarEnabled(true);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());

//        tvTitleName.setText("FAQs");
        if (AndroidUtil.isConnectingToInternet(getActivity())) {
            getSuccessListServerData();
        } else {
            Toast.makeText(getActivity(), Constant.check_internet_connection, Toast.LENGTH_SHORT).show();

        }
        add_success_iv.setOnClickListener(this);
//        adapterNewTask = new AdapterNewTask(getContext());
//        recyclerView.setAdapter(adapterNewTask);

    }

    private void getSuccessListServerData() {
        pDialog = new ProgressDialog(getContext(), R.style.DialogTheme);
        pDialog.setCancelable(true);
        if (pDialog != null) {
            pDialog.setMessage(getString(R.string.loading));
            if (!pDialog.isShowing())
                pDialog.show();
        }
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.WebUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(new OkHttpClient())
                .build();
        IFAQDATA ifaqdata = retrofit.create(IFAQDATA.class);
        if (sharedpreferences.getString("User_id", "") != null) {
            user_id = sharedpreferences.getString("User_id", "");
        }
        Call<SuccessStoriesData> call = ifaqdata.getData(user_id);
//        Call<NewTaskData> call = ifaqdata.getData("Open", "2");
        call.enqueue(this);

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

    @Override
    public void onItemClick(int position, View v) {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_success_iv:
                Fragment fragment = new FragmentSuccessStories();
                ((MainActivity) getActivity()).replacefragment(fragment);
                break;
        }
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onResponse(Call<SuccessStoriesData> call, Response<SuccessStoriesData> response) {
        successStoriesData = response.body();
        if (successStoriesData != null) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (Integer.parseInt(successStoriesData.getStatus()) == Constant.StatusCode.CLEANMONEY_CODE_SUCCESS) {
                if (successStoriesData.getData() != null && successStoriesData.getData().size() > 0) {
                    datumList = successStoriesData.getData();


                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    layoutManager.setSmoothScrollbarEnabled(true);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());

                    adapterSuccessStories = new AdapterSuccessStories(getContext(), datumList);
                    recyclerView.setAdapter(adapterSuccessStories);
                    adapterSuccessStories.setOnItemClickListener(FragmentSuccessStoriesList.this);
                } else {
                    no_task_tv.setText(getString(R.string.no_favtask));
                    no_task_tv.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onFailure(Call<SuccessStoriesData> call, Throwable t) {

    }

    public interface IFAQDATA {
        @GET(Constant.WebUrl.SUCCESS_STORIES)
        Call<SuccessStoriesData> getData(@Query("id") String id);
    }

    @Override
    public void onResume() {
        super.onResume();
//        ((MainActivity) getActivity()).PopFragment(rootView);
//        getView().setFocusableInTouchMode(true);
//        getView().requestFocus();
//        getView().setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
//
//
//                    getFragmentManager().popBackStack();
//
//
//                    //                    getActivity().getSupportFragmentManager().popBackStack(null, getActivity().getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
//                    return true;
//                }
//                return false;
//            }
//        });

    }
}
