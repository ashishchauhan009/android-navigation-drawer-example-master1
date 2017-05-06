package net.simplifiedcoding.navigationdrawerexample.fragments;

import android.app.Dialog;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.simplifiedcoding.navigationdrawerexample.Constant.Constant;
import net.simplifiedcoding.navigationdrawerexample.Model.Search;
import net.simplifiedcoding.navigationdrawerexample.Model.UserSearch;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.activities.MainActivity;
import net.simplifiedcoding.navigationdrawerexample.adapter.AdapterFavTask;
import net.simplifiedcoding.navigationdrawerexample.util.AndroidUtil;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class FragmentFavTask extends Fragment implements Callback<UserSearch>, AdapterFavTask.ClickListener {

    private static final String TAG = FragmentFavTask.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private TextView tvTitleName;
    private RecyclerView recyclerView;
    private AdapterFavTask adapterNewTask;
    private UserSearch userSearchData;
    private ProgressDialog pDialog;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String user_id;
    ArrayList<Search> favTaskList;
    TextView no_task_tv, task_name_tv;

    public FragmentFavTask() {
        // Required empty public constructor
    }


    public static FragmentFavTask newInstance() {
        FragmentFavTask fragment = new FragmentFavTask();


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
//        editor = sharedpreferences.edit();
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

        pDialog = new ProgressDialog(getContext(), R.style.DialogTheme);
        pDialog.setCancelable(true);
        tvTitleName = (TextView) view.findViewById(R.id.tv_actionbar_title);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_faq_pmgky);
        no_task_tv = (TextView) view.findViewById(R.id.no_task_tv);
        task_name_tv = (TextView) view.findViewById(R.id.task_name_tv);
        task_name_tv.setText(getString(R.string.fav_task));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

//        tvTitleName.setText("FAQs");
        if (AndroidUtil.isConnectingToInternet(getActivity())) {
            getFavListServerData();
        } else {
            Toast.makeText(getActivity(), Constant.check_internet_connection, Toast.LENGTH_SHORT).show();

        }

//        adapterNewTask = new AdapterNewTask(getContext());
//        recyclerView.setAdapter(adapterNewTask);

    }

    private void getFavListServerData() {

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
        Call<UserSearch> call = ifaqdata.getData(user_id);
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
        showDialouge(position);


    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onResponse(Call<UserSearch> call, Response<UserSearch> response) {
        userSearchData = response.body();
        if (userSearchData != null) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (Integer.parseInt(userSearchData.getStatus()) == Constant.StatusCode.CLEANMONEY_CODE_SUCCESS) {
                if (userSearchData.getData() != null && userSearchData.getData().size() > 0) {
                    favTaskList = userSearchData.getData();
                    adapterNewTask = new AdapterFavTask(getContext(), favTaskList);
                    recyclerView.setAdapter(adapterNewTask);
                    adapterNewTask.setOnItemClickListener(FragmentFavTask.this);
                } else {
                    no_task_tv.setText(getString(R.string.no_favtask));
                    no_task_tv.setVisibility(View.VISIBLE);
                }
            }
        }
    }






    @Override
    public void onFailure(Call<UserSearch> call, Throwable t) {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }





    protected void showDialouge(final int position) {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(getContext()) {
            @Override
            public void onStart() {
                super.onStart();
                Dialog dialog = this;
                if (dialog != null) {
                    int width = ViewGroup.LayoutParams.MATCH_PARENT;
                    int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    dialog.getWindow().setLayout(width, height);
                }
            }
        };
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_user_information);
        TextView tv_name,tv_email,tv_call,tv_designation,tv_level;
        tv_name = (TextView)dialog.findViewById(R.id.tv_name);
        tv_email = (TextView)dialog.findViewById(R.id.tv_email);
        tv_call = (TextView)dialog.findViewById(R.id.tv_call);
        tv_designation = (TextView)dialog.findViewById(R.id.tv_designation);
        tv_level = (TextView)dialog.findViewById(R.id.tv_level);



        if (userSearchData.getData().get(position).getUser_name()!=null && !userSearchData.getData().get(position).getUser_name().isEmpty()) {
            tv_name.setText(" "+userSearchData.getData().get(position).getUser_name());
        }
        if (userSearchData.getData().get(position).getUser_email()!=null && !userSearchData.getData().get(position).getUser_email().isEmpty()) {
            tv_email.setText(" "+userSearchData.getData().get(position).getUser_email());
        }
        if (userSearchData.getData().get(position).getUser_phone()!=null && !userSearchData.getData().get(position).getUser_phone().isEmpty()) {
            tv_call.setText(" "+userSearchData.getData().get(position).getUser_phone());
        }
        if (userSearchData.getData().get(position).getUser_designation()!=null && !userSearchData.getData().get(position).getUser_designation().isEmpty()) {
            tv_designation.setText(" "+userSearchData.getData().get(position).getUser_designation());
        }
        if (userSearchData.getData().get(position).getLevel()!=null && !userSearchData.getData().get(position).getLevel().isEmpty()) {
            tv_level.setText(" "+userSearchData.getData().get(position).getLevel());
        }
        Button btncancel = (Button) dialog.findViewById(R.id.btn_cancel);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }










    public interface IFAQDATA {
        @GET(Constant.WebUrl.MY_FAV_URL)
        Call<UserSearch> getData(@Query("id") String id);
    }
}
