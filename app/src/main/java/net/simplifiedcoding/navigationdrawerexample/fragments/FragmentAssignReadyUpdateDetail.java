package net.simplifiedcoding.navigationdrawerexample.fragments;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.simplifiedcoding.navigationdrawerexample.Constant.Constant;
import net.simplifiedcoding.navigationdrawerexample.Model.NewTaskData;
import net.simplifiedcoding.navigationdrawerexample.Model.PendingTaskDetail;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.activities.MainActivity;
import net.simplifiedcoding.navigationdrawerexample.activities.WebView_Activity;
import net.simplifiedcoding.navigationdrawerexample.adapter.AdapterMessageChain;
import net.simplifiedcoding.navigationdrawerexample.adapter.AdapterNewTask;
import net.simplifiedcoding.navigationdrawerexample.service.DownloadTaskCleanMoney;
import net.simplifiedcoding.navigationdrawerexample.util.AndroidUtil;
import net.simplifiedcoding.navigationdrawerexample.util.Config;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class FragmentAssignReadyUpdateDetail extends Fragment implements Callback<PendingTaskDetail>, View.OnClickListener {

    private static final String TAG = FragmentAssignReadyUpdateDetail.class.getSimpleName();
    private FragmentComletedTaskDetail.OnFragmentInteractionListener mListener;
    private TextView tvTitleName, countTv, assignedDateTv, assignedNameTv, subjectTv, taskCompdate, taskComponTv, tvMessageUpper,
            taskRespondby, tv_viewattachment, taskStatus, tvMessageBottom, tvAttachment, tvAttachmentBottom, taskPrevrespondon, tvPrevmessage, prevattachmentTv;
    private ProgressDialog pDialog;
    private String task_id;
    String status = "";
    EditText et_reassign;
    public static final int rcCC = 33;
    private Call<ResponseBody> submitTask = null;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private LinearLayout llCurrentresponse, llPreviousresponse, linear_reasign;
    private File file;
    private String user_id;
    private PendingTaskDetail newTaskData;
    private List<PendingTaskDetail.Datum> compTaskdataData;
    private List<PendingTaskDetail.Response> compTaskdataResponse;
    private Spinner spnStatus;
    private RecyclerView recyclerView;
    private AdapterMessageChain adapterMessageChain;
    private TextView message_chain_tv;
    Context context;

    public FragmentAssignReadyUpdateDetail() {
        // Required empty public constructor
    }


    public static FragmentAssignReadyUpdateDetail newInstance() {
        FragmentAssignReadyUpdateDetail fragment = new FragmentAssignReadyUpdateDetail();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_assign_readyupdatedetail, container, false);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        context = getActivity();
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

        try {
            task_id = getArguments().getString(Constant.TASK_ID);
            Log.e("check task_id ", task_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedpreferences.edit();
        user_id = sharedpreferences.getString("User_id", "").toString();
        Log.e("check shared preference", sharedpreferences.getString("TittleName", "").toString() + sharedpreferences.getString("User_id", "").toString());
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
        linear_reasign = (LinearLayout) view.findViewById(R.id.linear_reasign);
        tvTitleName = (TextView) view.findViewById(R.id.task_name_tv);
        tvMessageUpper = (TextView) view.findViewById(R.id.tv_message_upper);
        countTv = (TextView) view.findViewById(R.id.tv_count);
        assignedDateTv = (TextView) view.findViewById(R.id.tv_assigned_date);
        assignedNameTv = (TextView) view.findViewById(R.id.tv_assigned_name);
        subjectTv = (TextView) view.findViewById(R.id.tv_subject);
        taskCompdate = (TextView) view.findViewById(R.id.tv_comp_task);
        taskComponTv = (TextView) view.findViewById(R.id.task_compon_tv);
        taskStatus = (TextView) view.findViewById(R.id.task_status);
        et_reassign = (EditText) view.findViewById(R.id.et_reassign);
        tv_viewattachment = (TextView) view.findViewById(R.id.tv_viewattachment);

        message_chain_tv = (TextView) view.findViewById(R.id.message_chain_tv);
        message_chain_tv.setText(getString(R.string.response_chain));

        tvAttachment = (TextView) view.findViewById(R.id.attachment_tv);

        spnStatus = (Spinner) view.findViewById(R.id.spn_status);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_detail);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        tvTitleName.setText(getString(R.string.for_reaview_task_details));

        String[] mystatus = {"Close", "reassign"};

        final ArrayAdapter<String> arradApterStatus = new ArrayAdapter<String>(getContext(),
                R.layout.layout_simple_textview_black, mystatus);
        spnStatus.setAdapter(arradApterStatus);

        spnStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status = spnStatus.getSelectedItem().toString();
                if (status.contains("reassign")) {
                    linear_reasign.setVisibility(View.VISIBLE);
                } else {
                    linear_reasign.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ((TextView) view.findViewById(R.id.attachment_tv)).setOnClickListener(this);
        ((AppCompatButton) view.findViewById(R.id.btn_submit)).setOnClickListener(this);
        getNewTaskServerData();
    }


    void setallcontent() {
        if (compTaskdataData != null) {

            if (compTaskdataData.get(0).getTaskId() != null && !compTaskdataData.get(0).getTaskId().isEmpty()) {
                countTv.setText(" " + compTaskdataData.get(0).getTaskId());
            }
            /*String[] date = AndroidUtil.formatDate(compTaskdataData.get(0).getCreatedOn(), "yyyy-dd-mm HH:MM:SS", "dd-mm-yyyy HH:MM:SS").split(" ");
            if (date.length > 0) {
                assignedDateTv.setText(" " + date[0]);
            }*/
            if (compTaskdataData.get(0).getCreatedOn() != null && !compTaskdataData.get(0).getCreatedOn().isEmpty()) {
                assignedDateTv.setText(" " + compTaskdataData.get(0).getCreatedOn());
            }
            if (compTaskdataData.get(0).getCreatedBy() != null && !compTaskdataData.get(0).getCreatedBy().isEmpty()) {
                assignedNameTv.setText(" " + compTaskdataData.get(0).getCreatedBy());
            }
            if (compTaskdataData.get(0).getTaskSubject() != null && !compTaskdataData.get(0).getTaskSubject().isEmpty()) {
                subjectTv.setText(" " + compTaskdataData.get(0).getTaskSubject());
            }
            if (compTaskdataData.get(0).getTaskDescription() != null && !compTaskdataData.get(0).getTaskDescription().isEmpty()) {
                tvMessageUpper.setText(" " + compTaskdataData.get(0).getTaskDescription());
            }
            if (compTaskdataData.get(0).getTaskStatus() != null && !compTaskdataData.get(0).getTaskStatus().isEmpty()) {
                taskStatus.setText(" " + compTaskdataData.get(0).getTaskStatus());
            }
            /*String[] date1 = AndroidUtil.formatDate(compTaskdataData.get(0).getTaskCompletionDate(), "yyyy-dd-mm", "dd-mm-yyyy").split(" ");
            if (date.length > 0) {
                taskCompOn.setText(" " + date1[0]);
            }*/
            if (compTaskdataData.get(0).getTaskCompletionDate() != null && !compTaskdataData.get(0).getTaskCompletionDate().isEmpty()) {
                taskCompdate.setText(" " + compTaskdataData.get(0).getTaskCompletionDate());
            }
            if (compTaskdataData.get(0).getTask_completed_on() != null && !compTaskdataData.get(0).getTask_completed_on().isEmpty()) {
                taskComponTv.setText(" " + compTaskdataData.get(0).getTask_completed_on());
            }
            if (compTaskdataData.get(0).getDoc() != null && !compTaskdataData.get(0).getDoc().isEmpty()) {
                tvAttachment.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_attachment, 0, 0, 0);
               /* String url = compTaskdataData.get(0).getDoc();
                String fileName = url.substring(url.lastIndexOf('/') + 1);
                tvAttachment.setText(" " + fileName);*/
                tvAttachment.setText("Click here to download");
            }
            if (compTaskdataData.get(0).getDoc() != null && !compTaskdataData.get(0).getDoc().isEmpty()) {
//                tv_viewattachment.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_attachment, 0, 0, 0);
                tv_viewattachment.setVisibility(View.VISIBLE);

                tv_viewattachment.setOnClickListener(this);
            }
        }
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
        Call<PendingTaskDetail> call = ifaqdata.getData("complete", task_id);
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

    private boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.attachment_tv:
                if (!compTaskdataData.get(0).getDoc().isEmpty()) {
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(compTaskdataData.get(0).getDoc()));
//                    getContext().startActivity(browserIntent);

                    if (isConnectingToInternet()) {
                        final int id = 1;
                        final NotificationManager mNotifyManager =
                                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                        final android.support.v4.app.NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity());
                        mBuilder.setContentTitle("File Download")
                                .setContentText("Download in progress")
                                .setSmallIcon(R.drawable.pdf_icon);
// Start a lengthy operation in a background thread
                        new Thread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        int incr;
                                        // Do the "lengthy" operation 20 times
                                        for (incr = 0; incr <= 100; incr += 25) {
                                            // Sets the progress indicator to a max value, the
                                            // current completion percentage, and "determinate"
                                            // state
                                            mBuilder.setProgress(100, incr, false);
                                            // Displays the progress bar for the first time.
                                            mNotifyManager.notify(id, mBuilder.build());
                                            // Sleeps the thread, simulating an operation
                                            // that takes time
                                            try {
                                                // Sleep for 5 seconds
                                                Thread.sleep(5 * 100);
                                            } catch (InterruptedException e) {
                                                Log.d(TAG, "sleep failure");
                                            }
                                        }
                                        // When the loop is finished, updates the notification
                                        mBuilder.setContentText("Download complete")
                                                // Removes the progress bar
                                                .setProgress(0, 0, false);
                                        mNotifyManager.notify(id, mBuilder.build());
                                    }
                                }
// Starts the thread by calling the run() method in its Runnable
                        ).start();
                        new DownloadTaskCleanMoney(getActivity(), compTaskdataData.get(0).getDoc());
                        Toast.makeText(getActivity(), "File Download..", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), Config.Interet_Error, Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.btn_submit:
                if (!TextUtils.isEmpty(status)) {
                    String strRessign = et_reassign.getText().toString().trim();
                    if (status.contains("reassign")) {
                        if (!et_reassign.getText().toString().isEmpty())
                            saveReadyToUpdateData(user_id, task_id, status, strRessign);
                        else
                            showMessage(getView(), "Message cannot be blank");
                    } else
                        saveReadyToUpdateData(user_id, task_id, status, strRessign);
                } else
                    showMessage(getView(), "Please select your option value");
                break;

            case R.id.tv_viewattachment:
                Intent in = new Intent(getActivity(), WebView_Activity.class);
                in.putExtra(Config.Webview_url, compTaskdataData.get(0).getDoc().toString());
                startActivity(in);

                break;
        }
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    void saveReadyToUpdateData(String user_id, String task_id, String status, String reassign) {
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
        ICLOSEDTASK iclosedtask = retrofit.create(ICLOSEDTASK.class);


        retrofit2.Call<PendingTaskDetail> call = null;

        call = iclosedtask.getData(user_id, task_id, status, reassign);
        call.enqueue(submitData);
    }


    retrofit2.Callback<PendingTaskDetail> submitData = new retrofit2.Callback<PendingTaskDetail>() {

        @Override
        public void onResponse(retrofit2.Call<PendingTaskDetail> call, retrofit2.Response<PendingTaskDetail> response) {
            PendingTaskDetail logindata = response.body();
            if (logindata != null) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                if (Integer.parseInt(logindata.getStatus()) == Constant.StatusCode.CLEANMONEY_CODE_SUCCESS) {
                    Toast.makeText(getContext(), "Successfully submit", Toast.LENGTH_SHORT).show();
                    getFragmentManager().popBackStackImmediate();
                } else {
                    showMessage(getView(), "Something went wrong...");
                }
            }
        }

        @Override
        public void onFailure(retrofit2.Call<PendingTaskDetail> call, Throwable t) {
            showMessage(getView(), "Something went wrong...");
        }
    };

    @Override
    public void onResponse(Call<PendingTaskDetail> call, Response<PendingTaskDetail> response) {
        newTaskData = response.body();

        if (newTaskData != null) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (Integer.parseInt(newTaskData.getStatus()) == Constant.StatusCode.CLEANMONEY_CODE_SUCCESS) {
                if (newTaskData.getData() != null && newTaskData.getData().size() > 0) {
                    compTaskdataData = newTaskData.getData();
                    setallcontent();
                    if (newTaskData.getResponse() != null) {
                        compTaskdataResponse = newTaskData.getResponse();
                        adapterMessageChain = new AdapterMessageChain(getContext(), compTaskdataResponse);
                        recyclerView.setAdapter(adapterMessageChain);
                        adapterMessageChain.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    @Override
    public void onFailure(Call<PendingTaskDetail> call, Throwable t) {

    }

    public interface IFAQDATA {
        @GET(Constant.WebUrl.TASK_DETAIL)
        Call<PendingTaskDetail> getData(@Query("type") String type, @Query("task_id") String task_id);
    }

    public interface ICLOSEDTASK {
        @GET(Constant.WebUrl.CLOSED_TASK)
        Call<PendingTaskDetail> getData(@Query("id") String id, @Query("task_id") String task_id, @Query("status") String status, @Query("reassign") String reassign);
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
