package net.simplifiedcoding.navigationdrawerexample.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.simplifiedcoding.navigationdrawerexample.Constant.Constant;
import net.simplifiedcoding.navigationdrawerexample.Model.DashboardData;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.activities.MainActivity;

import java.lang.ref.WeakReference;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class FragmentTaskDashBoard extends Fragment implements Callback<DashboardData>, View.OnClickListener {


    private OnFragmentInteractionListener mListener;
    private ImageButton ivBackBtn;
    public TextView my_new_task_tv, my_pending_task_tv, my_closed_task_tv, assigned_closed_task_tv, assigned_pending_task_tv,
            assigned_new_task_tv, my_complete_tv, assigned_wip_tv;
    private ProgressDialog pDialog;
    SharedPreferences sharedpreferences;

    private static WeakReference<FragmentTaskDashBoard> homeFragmentWeakReference;
    String user_id;
    DashboardData dashboardData;
    RelativeLayout mynewtask_relative, myclosedtask_relative, mypendingtask_relative, assign_closedtask_relative,
            assign_pendingtask_relative, assign_newtask_relative, mycompletetask_relative, assign_wip_relative;

    public FragmentTaskDashBoard() {
        // Required empty public constructor
    }


    /*public static FragmentTaskDashBoard newInstance() {
        FragmentTaskDashBoard fragment = new FragmentTaskDashBoard();
        Bundle args = new Bundle();

        return fragment;
    }*/
    public static FragmentTaskDashBoard newInstance() {
        if (homeFragmentWeakReference == null) {
            homeFragmentWeakReference = new WeakReference<>(new FragmentTaskDashBoard());
        }
        return homeFragmentWeakReference.get();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_taskdashboard, container, false);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
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
        //tvActionbarTitle = (TextView) view.findViewById(R.id.tv_actionbar_title);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setPadding(0, 5, 10, 5);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(null);
        ((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        pDialog = new ProgressDialog(getContext(), R.style.DialogTheme);
        pDialog.setCancelable(true);
        ImageView imageView = (ImageView) view.findViewById(R.id.btn_drawer);
        my_new_task_tv = (TextView) view.findViewById(R.id.my_new_task_tv);
        my_pending_task_tv = (TextView) view.findViewById(R.id.my_pending_task_tv);
        my_closed_task_tv = (TextView) view.findViewById(R.id.my_closed_task_tv);
        assigned_closed_task_tv = (TextView) view.findViewById(R.id.assigned_closed_task_tv);
        assigned_pending_task_tv = (TextView) view.findViewById(R.id.assigned_pending_task_tv);
        assigned_new_task_tv = (TextView) view.findViewById(R.id.assigned_new_task_tv);
        my_complete_tv = (TextView) view.findViewById(R.id.my_complete_tv);
        assigned_wip_tv = (TextView) view.findViewById(R.id.assigned_wip_tv);

        mynewtask_relative = (RelativeLayout) view.findViewById(R.id.mynewtask_relative);
        myclosedtask_relative = (RelativeLayout) view.findViewById(R.id.myclosedtask_relative);
        mypendingtask_relative = (RelativeLayout) view.findViewById(R.id.mypendingtask_relative);
        assign_closedtask_relative = (RelativeLayout) view.findViewById(R.id.assign_closedtask_relative);
        assign_pendingtask_relative = (RelativeLayout) view.findViewById(R.id.assign_pendingtask_relative);
        assign_newtask_relative = (RelativeLayout) view.findViewById(R.id.assign_newtask_relative);
        mycompletetask_relative = (RelativeLayout) view.findViewById(R.id.mycompletetask_relative);
        assign_wip_relative = (RelativeLayout) view.findViewById(R.id.assign_wip_relative);

        mynewtask_relative.setOnClickListener(this);
        myclosedtask_relative.setOnClickListener(this);
        mypendingtask_relative.setOnClickListener(this);
        assign_closedtask_relative.setOnClickListener(this);
        assign_pendingtask_relative.setOnClickListener(this);
        assign_newtask_relative.setOnClickListener(this);
        mycompletetask_relative.setOnClickListener(this);
        assign_wip_relative.setOnClickListener(this);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).clickEventSlide();
            }
        });

        //tvActionbarTitle.setText("Dashboard");
        getNewTaskServerData();

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
    public void onResponse(Call<DashboardData> call, Response<DashboardData> response) {
        dashboardData = response.body();
        //Log.e(TAG,faqModelData.toString());
        if (dashboardData != null) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (Integer.parseInt(dashboardData.getStatus()) == Constant.StatusCode.CLEANMONEY_CODE_SUCCESS) {
                setData();
            }
        }
    }

    private void setData() {


        if (dashboardData.getData().getTaskforme().getOpen() != null && !dashboardData.getData().getTaskforme().getOpen().isEmpty()) {
            my_new_task_tv.setText(dashboardData.getData().getTaskforme().getOpen());
        }
        if (dashboardData.getData().getTaskforme().getPending() != null && !dashboardData.getData().getTaskforme().getPending().isEmpty()) {
            my_pending_task_tv.setText(dashboardData.getData().getTaskforme().getPending());
        }
        if (dashboardData.getData().getTaskforme().getClosed() != null && !dashboardData.getData().getTaskforme().getClosed().isEmpty()) {
            my_closed_task_tv.setText(dashboardData.getData().getTaskforme().getClosed());
        }

        if (dashboardData.getData().getAssignedbyme().getOpen() != null && !dashboardData.getData().getAssignedbyme().getOpen().isEmpty()) {
            assigned_new_task_tv.setText(dashboardData.getData().getAssignedbyme().getOpen());
        }
        if (dashboardData.getData().getAssignedbyme().getPending() != null && !dashboardData.getData().getAssignedbyme().getPending().isEmpty()) {
            assigned_pending_task_tv.setText(dashboardData.getData().getAssignedbyme().getPending());
        }
        if (dashboardData.getData().getAssignedbyme().getClosed() != null && !dashboardData.getData().getAssignedbyme().getClosed().isEmpty()) {
            assigned_closed_task_tv.setText(dashboardData.getData().getAssignedbyme().getClosed());
        }

        //new added ----------------------------------------------------------------------------------------------------------------------------------
        if (dashboardData.getData().getAssignedbyme().getWip() != null && !dashboardData.getData().getAssignedbyme().getWip().isEmpty()) {
            assigned_wip_tv.setText(dashboardData.getData().getAssignedbyme().getWip());
        }
        if (dashboardData.getData().getTaskforme().getComplete() != null && !dashboardData.getData().getTaskforme().getComplete().isEmpty()) {
            my_complete_tv.setText(dashboardData.getData().getTaskforme().getComplete());
        }


    }

    @Override
    public void onFailure(Call<DashboardData> call, Throwable t) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mynewtask_relative:
                FragmentNewTask fragmentNewTask = new FragmentNewTask();
                ((MainActivity) getActivity()).replacefragment(fragmentNewTask);
                break;
            case R.id.mypendingtask_relative:
                FragmentPendingTask fragmentPendingTask = new FragmentPendingTask();
                ((MainActivity) getActivity()).replacefragment(fragmentPendingTask);
                break;
            case R.id.myclosedtask_relative:
                FragmentClosedTask fragmentClosedTask = new FragmentClosedTask();
                ((MainActivity) getActivity()).replacefragment(fragmentClosedTask);
                break;
            case R.id.assign_closedtask_relative:
                FragmentAssignCloseTask fragmentAssignedNewTask = new FragmentAssignCloseTask();
                ((MainActivity) getActivity()).replacefragment(fragmentAssignedNewTask);
                break;
            case R.id.assign_pendingtask_relative:
                FragmentAssignReadyUpdateTask fragmentAssignReadyUpdateTask = new FragmentAssignReadyUpdateTask();
                ((MainActivity) getActivity()).replacefragment(fragmentAssignReadyUpdateTask);
                break;
            case R.id.assign_newtask_relative:
                FragmentAssignedNewTask assignedNewTask = new FragmentAssignedNewTask();
                ((MainActivity) getActivity()).replacefragment(assignedNewTask);
                break;
            case R.id.mycompletetask_relative:
                FragmentCompletedTask fragmentCompletedTask = new FragmentCompletedTask();
                ((MainActivity) getActivity()).replacefragment(fragmentCompletedTask);
                break;
            case R.id.assign_wip_relative:
                FragmentWIPTask fragmentWIPTask = new FragmentWIPTask();
                ((MainActivity) getActivity()).replacefragment(fragmentWIPTask);
                break;

        }
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
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
        Call<DashboardData> call = ifaqdata.getData(user_id);
        call.enqueue(this);

    }

    public interface IFAQDATA {
        @GET(Constant.WebUrl.DASHBOARD_URL)
        Call<DashboardData> getData(@Query("id") String id);
    }
}
