package net.simplifiedcoding.navigationdrawerexample.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.simplifiedcoding.navigationdrawerexample.Constant.Constant;
import net.simplifiedcoding.navigationdrawerexample.Model.DashboardData;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.activities.MainActivity;
import net.simplifiedcoding.navigationdrawerexample.util.Config;

import java.lang.ref.WeakReference;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class FragmentNewDashBoard extends Fragment implements Callback<DashboardData>, View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    private ImageButton ivBackBtn;
    public TextView tv_newtask_count, tv_pendingtask_count, tv_forreview_count, tv_pendingtask2_count;
    private ProgressDialog pDialog;
    SharedPreferences sharedpreferences;
    private LinearLayout ll_newtask, ll_pendingtask, ll_todotask, ll_taskmonotoring, ll_forreview, ll_pendingtask2;

    private static WeakReference<FragmentNewDashBoard> homeFragmentWeakReference;
    String user_id;
    DashboardData dashboardData;
    RelativeLayout mis_relative, favour_relatiiteve, create_task_relative, task_dash_relative,
            rl_designation, rl_createtask, rl_favorite, rl_taskdashbord, rl_mis,
            rl_outreach, rl_successstry;

    public FragmentNewDashBoard() {
        // Required empty public constructor
    }


    public static FragmentNewDashBoard newInstance() {
        if (homeFragmentWeakReference == null) {
            homeFragmentWeakReference = new WeakReference<>(new FragmentNewDashBoard());
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
        View rootView = inflater.inflate(R.layout.fragment_newdashboard, container, false);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        initView(rootView);
        return rootView;
    }


    void initView(View view) {
        //tvActionbarTitle = (TextView) view.findViewById(R.id.tv_actionbar_title);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setPadding(0, 5, 10, 5);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(null);
        ((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        pDialog = new ProgressDialog(getActivity(), R.style.DialogTheme);
        pDialog.setCancelable(true);
        ImageView imageView = (ImageView) view.findViewById(R.id.btn_drawer);


        tv_pendingtask2_count = (TextView) view.findViewById(R.id.tv_pendingtask2_count);
        tv_newtask_count = (TextView) view.findViewById(R.id.tv_newtask_count);
        tv_pendingtask_count = (TextView) view.findViewById(R.id.tv_pendingtask_count);
        tv_forreview_count = (TextView) view.findViewById(R.id.tv_forreview_count);

        ll_newtask = (LinearLayout) view.findViewById(R.id.ll_newtask);
        ll_pendingtask = (LinearLayout) view.findViewById(R.id.ll_pendingtask);
        ll_todotask = (LinearLayout) view.findViewById(R.id.ll_todotask);
        ll_taskmonotoring = (LinearLayout) view.findViewById(R.id.ll_taskmonotoring);
        ll_forreview = (LinearLayout) view.findViewById(R.id.ll_forreview);
        ll_pendingtask2 = (LinearLayout) view.findViewById(R.id.ll_pendingtask2);

        rl_designation = (RelativeLayout) view.findViewById(R.id.rl_designation);
        rl_createtask = (RelativeLayout) view.findViewById(R.id.rl_createtask);
        rl_favorite = (RelativeLayout) view.findViewById(R.id.rl_favorite);
        rl_taskdashbord = (RelativeLayout) view.findViewById(R.id.rl_taskdashbord);
        rl_mis = (RelativeLayout) view.findViewById(R.id.rl_mis);
        rl_outreach = (RelativeLayout) view.findViewById(R.id.rl_outreach);
        rl_successstry = (RelativeLayout) view.findViewById(R.id.rl_successstry);


        ll_newtask.setOnClickListener(this);
        ll_pendingtask.setOnClickListener(this);
        ll_todotask.setOnClickListener(this);
        ll_taskmonotoring.setOnClickListener(this);
        ll_forreview.setOnClickListener(this);
        ll_pendingtask2.setOnClickListener(this);
        rl_designation.setOnClickListener(this);
        rl_createtask.setOnClickListener(this);
        rl_favorite.setOnClickListener(this);
        rl_taskdashbord.setOnClickListener(this);
        rl_mis.setOnClickListener(this);
        rl_outreach.setOnClickListener(this);
        rl_successstry.setOnClickListener(this);


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
            tv_newtask_count.setText(dashboardData.getData().getTaskforme().getOpen());
        }
        if (dashboardData.getData().getTaskforme().getPending() != null && !dashboardData.getData().getTaskforme().getPending().isEmpty()) {
            tv_pendingtask_count.setText(dashboardData.getData().getTaskforme().getPending());
        }
        if (dashboardData.getData().getAssignedbyme().getPending() != null && !dashboardData.getData().getAssignedbyme().getPending().isEmpty()) {
            tv_forreview_count.setText(dashboardData.getData().getAssignedbyme().getPending());
        }
        if (dashboardData.getData().getAssignedbyme().getWip() != null && !dashboardData.getData().getAssignedbyme().getWip().isEmpty()) {
            tv_pendingtask2_count.setText(dashboardData.getData().getAssignedbyme().getWip());
        }

    }

    @Override
    public void onFailure(Call<DashboardData> call, Throwable t) {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_mis:
                FragmentMis fragmentMis = new FragmentMis();
                ((MainActivity) getActivity()).replacefragment(fragmentMis);
                break;
            case R.id.rl_favorite:
                FragmentFavTask fragmentFavTask = new FragmentFavTask();
                ((MainActivity) getActivity()).replacefragment(fragmentFavTask);
                break;
            case R.id.rl_createtask:
                FragmentSearchTask fragmentSearchTask = new FragmentSearchTask();
                ((MainActivity) getActivity()).replacefragment(fragmentSearchTask);
                break;
            case R.id.rl_taskdashbord:
                FragmentTaskDashBoard fragmentAssignedNewTask = new FragmentTaskDashBoard();
                ((MainActivity) getActivity()).replacefragment(fragmentAssignedNewTask);
                break;
            case R.id.ll_newtask:
                FragmentNewTask fragmentNewTask = new FragmentNewTask();
                Bundle bundle6 = new Bundle();
                bundle6.putBoolean(Config.ColorValue, true);
                bundle6.putBoolean(Config.Todo_monitering, true);
                fragmentNewTask.setArguments(bundle6);
                ((MainActivity) getActivity()).replacefragment(fragmentNewTask);
                break;
            case R.id.ll_pendingtask:
                FragmentPendingTask fragmentPendingTask = new FragmentPendingTask();
                Bundle bundle7 = new Bundle();
                bundle7.putBoolean(Config.ColorValue, true);
                bundle7.putBoolean(Config.Todo_monitering, true);
                fragmentPendingTask.setArguments(bundle7);
                ((MainActivity) getActivity()).replacefragment(fragmentPendingTask);
                break;

            case R.id.ll_forreview:
                FragmentAssignReadyUpdateTask fragmentAssignReadyUpdateTask = new FragmentAssignReadyUpdateTask();
                Bundle bundle2 = new Bundle();
                bundle2.putBoolean(Config.ColorValue, true);
                bundle2.putBoolean(Config.Todo_monitering, false);
                fragmentAssignReadyUpdateTask.setArguments(bundle2);
                ((MainActivity) getActivity()).replacefragment(fragmentAssignReadyUpdateTask);
                break;

            case R.id.ll_pendingtask2:
                FragmentWIPTask fragmentWIPTask = new FragmentWIPTask();
                Bundle bundle5 = new Bundle();
                bundle5.putBoolean(Config.ColorValue, true);
                bundle5.putBoolean(Config.Todo_monitering, false);
                fragmentWIPTask.setArguments(bundle5);
                ((MainActivity) getActivity()).replacefragment(fragmentWIPTask);
                break;

            case R.id.rl_designation:
                FragmentSearchByDesignation fragmentSearchByDesignation = new FragmentSearchByDesignation();
                ((MainActivity) getActivity()).replacefragment(fragmentSearchByDesignation);
                //showMessage(view,getString(R.string.under_development));
                break;

            case R.id.rl_outreach:
                FragmentAddOutreachList fragmentAddOutreachList = new FragmentAddOutreachList();
                ((MainActivity) getActivity()).replacefragment(fragmentAddOutreachList);
//                showMessage(view,getString(R.string.under_development));
                break;

            case R.id.rl_successstry:
                FragmentSuccessStoriesList fragmentSuccessStoriesList = new FragmentSuccessStoriesList();
                ((MainActivity) getActivity()).replacefragment(fragmentSuccessStoriesList);
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