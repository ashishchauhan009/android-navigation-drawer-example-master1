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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.simplifiedcoding.navigationdrawerexample.Constant.Constant;
import net.simplifiedcoding.navigationdrawerexample.Model.NewTaskData;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.activities.MainActivity;
import net.simplifiedcoding.navigationdrawerexample.adapter.AdapterNewTask;
import net.simplifiedcoding.navigationdrawerexample.util.Config;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class FragmentAssignCloseTask extends Fragment implements Callback<NewTaskData>, AdapterNewTask.ClickListener {

    private static final String TAG = FragmentAssignCloseTask.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private TextView tvTitleName;
    private RecyclerView recyclerView;
    private AdapterNewTask adapterNewTask;
    private NewTaskData newTaskData;
    private ProgressDialog pDialog;
    SharedPreferences sharedpreferences;
    String user_id;
    TextView no_task_tv, task_name_tv;
    private String task_id;

    boolean Todo_monitering;
    boolean color_value;

    public FragmentAssignCloseTask() {
        // Required empty public constructor
    }


    public static FragmentAssignCloseTask newInstance() {
        FragmentAssignCloseTask fragment = new FragmentAssignCloseTask();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_task, container, false);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        color_value = getArguments().getBoolean(Config.ColorValue);
        Todo_monitering = getArguments().getBoolean(Config.Todo_monitering);
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

        pDialog = new ProgressDialog(getContext(), R.style.DialogTheme);
        pDialog.setCancelable(true);
        tvTitleName = (TextView) view.findViewById(R.id.task_name_tv);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_faq_pmgky);
        no_task_tv = (TextView) view.findViewById(R.id.no_task_tv);
        task_name_tv = (TextView) view.findViewById(R.id.task_name_tv);
        task_name_tv.setText(getString(R.string.closed_task));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        tvTitleName.setText("ASSIGNED CLOSED TASKS");
        getNewTaskServerData();

//        adapterNewTask = new AdapterNewTask(getContext());
//        recyclerView.setAdapter(adapterNewTask);

    }

    private void getNewTaskServerData() {

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
        IFAQDATA ifaqdata = retrofit.create(IFAQDATA.class);
        if (sharedpreferences.getString("User_id", "") != null) {
            user_id = sharedpreferences.getString("User_id", "");
        }
        Log.e(TAG, user_id.toString());
        Call<NewTaskData> call = ifaqdata.getData("close", user_id);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<NewTaskData> call, Response<NewTaskData> response) {
        newTaskData = response.body();
        //Log.e(TAG,faqModelData.toString());
        if (newTaskData != null) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (Integer.parseInt(newTaskData.getStatus()) == Constant.StatusCode.CLEANMONEY_CODE_SUCCESS) {
                if (newTaskData.getData() != null && newTaskData.getData().size() > 0) {
                    adapterNewTask = new AdapterNewTask(getContext(), newTaskData.getData(), "task_assign_date",color_value,Todo_monitering);
                    recyclerView.setAdapter(adapterNewTask);
                    adapterNewTask.setOnItemClickListener(FragmentAssignCloseTask.this);
                } else {
                    no_task_tv.setText(getString(R.string.no_assignedclose));
                    no_task_tv.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onFailure(Call<NewTaskData> call, Throwable t) {

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
        if (newTaskData.getData() != null && !newTaskData.getData().get(position).getTaskId().isEmpty())
            task_id = newTaskData.getData().get(position).getTaskId();
        Fragment fragment2 = new FragmentClosedTaskDetail();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.TASK_ID, task_id);
        fragment2.setArguments(bundle);
        ((MainActivity) getContext()).replacefragment(fragment2);
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    public interface IFAQDATA {
        @GET(Constant.WebUrl.NEW_TASK_LIST)
        Call<NewTaskData> getData(@Query("type") String type, @Query("id") String id);
    }
}
