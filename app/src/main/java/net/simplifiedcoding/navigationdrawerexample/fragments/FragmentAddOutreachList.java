package net.simplifiedcoding.navigationdrawerexample.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
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
import net.simplifiedcoding.navigationdrawerexample.Model.OutReachData;
import net.simplifiedcoding.navigationdrawerexample.Model.SuccessStoriesData;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.activities.MainActivity;
import net.simplifiedcoding.navigationdrawerexample.adapter.AdapterOutReach;
import net.simplifiedcoding.navigationdrawerexample.adapter.AdapterSuccessStories;
import net.simplifiedcoding.navigationdrawerexample.util.AndroidUtil;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class FragmentAddOutreachList extends Fragment implements Callback<OutReachData>, AdapterOutReach.ClickListener, View.OnClickListener {

    private static final String TAG = FragmentAddOutreachList.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private TextView tvTitleName;
    private RecyclerView recyclerView;
    private AdapterOutReach adapterOutReach;
    private OutReachData outReachData;
    private ProgressDialog pDialog;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String user_id;
    List<OutReachData.Datum> datumList;
    TextView no_task_tv, task_name_tv;
    private ImageView add_success_iv;
    View rootView;

    public FragmentAddOutreachList() {
        // Required empty public constructor
    }


    public static FragmentAddOutreachList newInstance() {
        FragmentAddOutreachList fragment = new FragmentAddOutreachList();


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

        task_name_tv.setText(getString(R.string.out_reach));
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
//        layoutManager.setSmoothScrollbarEnabled(true);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());

//        tvTitleName.setText("FAQs");
        if (AndroidUtil.isConnectingToInternet(getActivity())) {
            getOutReachListServerData();
        } else {
            Toast.makeText(getActivity(), Constant.check_internet_connection, Toast.LENGTH_SHORT).show();

        }
        add_success_iv.setOnClickListener(this);
//        adapterNewTask = new AdapterNewTask(getContext());
//        recyclerView.setAdapter(adapterNewTask);

    }

    private void getOutReachListServerData() {
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
        Call<OutReachData> call = ifaqdata.getData(user_id);
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
                Fragment fragment = new FragmentAddOutReach();
                ((MainActivity) getActivity()).replacefragment(fragment);
                break;
        }
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onResponse(Call<OutReachData> call, Response<OutReachData> response) {
        outReachData = response.body();
        if (outReachData != null) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (Integer.parseInt(outReachData.getStatus()) == Constant.StatusCode.CLEANMONEY_CODE_SUCCESS) {
                if (outReachData.getData() != null && outReachData.getData().size() > 0) {
                    datumList = outReachData.getData();


                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    layoutManager.setSmoothScrollbarEnabled(true);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());

                    adapterOutReach = new AdapterOutReach(getContext(), datumList);
                    recyclerView.setAdapter(adapterOutReach);
                    adapterOutReach.setOnItemClickListener(FragmentAddOutreachList.this);
                } else {
                    no_task_tv.setText(getString(R.string.no_outreachlist));
                    no_task_tv.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onFailure(Call<OutReachData> call, Throwable t) {
        if (outReachData != null) {
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }

    public interface IFAQDATA {
        @GET(Constant.WebUrl.OUT_REACH)
        Call<OutReachData> getData(@Query("id") String id);
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
