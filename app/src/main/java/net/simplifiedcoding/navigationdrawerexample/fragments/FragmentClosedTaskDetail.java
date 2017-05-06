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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.simplifiedcoding.navigationdrawerexample.Constant.Constant;
import net.simplifiedcoding.navigationdrawerexample.Model.PendingTaskDetail;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.activities.MainActivity;
import net.simplifiedcoding.navigationdrawerexample.activities.WebView_Activity;
import net.simplifiedcoding.navigationdrawerexample.adapter.AdapterMessageChain;
import net.simplifiedcoding.navigationdrawerexample.service.DownloadTaskCleanMoney;
import net.simplifiedcoding.navigationdrawerexample.util.AndroidUtil;
import net.simplifiedcoding.navigationdrawerexample.util.Config;

import java.io.File;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;
import retrofit2.http.Query;


public class FragmentClosedTaskDetail extends Fragment implements Callback<PendingTaskDetail>, View.OnClickListener {

    private static final String TAG = FragmentClosedTaskDetail.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private TextView tvTitleName, countTv, assignedDateTv, assignedNameTv, subjectTv, taskCompDate, taskComponTv, tvMessageUpper,
            taskRespondby, taskRespondedon, taskStatus, tvMessageBottom, tvAttachment, tvAttachmentBottom, taskPrevrespondon,
            tvPrevmessage, prevattachmentTv, tv_viewattachment;
    private ProgressDialog pDialog;
    private String task_id;
    String message = "";
    String status = "";
    public static final int rcCC = 33;
    private Uri FileUri = null;
    private Call<ResponseBody> submitTask = null;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private LinearLayout llCurrentresponse, llPreviousresponse;
    private File file;
    private String user_id;
    private PendingTaskDetail newTaskData;
    private List<PendingTaskDetail.Datum> compTaskdataData;
    private List<PendingTaskDetail.Response> compTaskdataResponse;
    private RecyclerView recyclerView;
    private AdapterMessageChain adapterMessageChain;
    private TextView message_chain_tv;


    public FragmentClosedTaskDetail() {
        // Required empty public constructor
    }


    public static FragmentClosedTaskDetail newInstance() {
        FragmentClosedTaskDetail fragment = new FragmentClosedTaskDetail();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_completed_taskdetail, container, false);
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
        tvTitleName = (TextView) view.findViewById(R.id.task_name_tv);
        tvMessageUpper = (TextView) view.findViewById(R.id.tv_message_upper);
        countTv = (TextView) view.findViewById(R.id.tv_count);
        assignedDateTv = (TextView) view.findViewById(R.id.tv_assigned_date);
        assignedNameTv = (TextView) view.findViewById(R.id.tv_assigned_name);
        subjectTv = (TextView) view.findViewById(R.id.tv_subject);
        taskCompDate = (TextView) view.findViewById(R.id.tv_comp_task);
        tvAttachment = (TextView) view.findViewById(R.id.attachment_tv);
        taskComponTv = (TextView) view.findViewById(R.id.task_compon_tv);
        message_chain_tv = (TextView) view.findViewById(R.id.message_chain_tv);
        tv_viewattachment = (TextView) view.findViewById(R.id.tv_viewattachment);
        message_chain_tv.setText(getString(R.string.response_chain));


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_detail);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        taskStatus = (TextView) view.findViewById(R.id.task_status);
        tvTitleName.setText(getString(R.string.closed_task_details));

        ((TextView) view.findViewById(R.id.attachment_tv)).setOnClickListener(this);
        newTaskApiCall(task_id);

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
                taskCompDate.setText(" " + compTaskdataData.get(0).getTaskCompletionDate());
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
                tv_viewattachment.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_attachment, 0, 0, 0);
                tv_viewattachment.setVisibility(View.VISIBLE);
                tv_viewattachment.setOnClickListener(this);
            }
        }
        //response settext
//
//        if(compTaskdataResponse!=null) {
//            if ( !compTaskdataResponse.get(0).getPreviousAttach().isEmpty() || !compTaskdataResponse.get(0).getPreviousRemark().isEmpty() || !compTaskdataResponse.get(0).getPreviousRemarkAdded().isEmpty()) {
//                llPreviousresponse.setVisibility(View.VISIBLE);
//                String[] date2 = AndroidUtil.formatDate(compTaskdataResponse.get(0).getPreviousRemarkAdded(), "yyyy-dd-mm HH:MM:SS", "dd-mm-yyyy HH:MM:SS").split(" ");
//                if (date2.length > 0) {
//                    taskPrevrespondon.setText(" " + date2[0]);
//                }
//                if (compTaskdataResponse.get(0).getPreviousRemark() != null && !compTaskdataResponse.get(0).getPreviousRemark().isEmpty()) {
//                    tvPrevmessage.setText(" " + compTaskdataResponse.get(0).getPreviousRemark());
//                }
//                if (compTaskdataResponse.get(0).getPreviousAttach() != null && !compTaskdataResponse.get(0).getPreviousAttach().isEmpty()) {
//                    prevattachmentTv.setVisibility(View.VISIBLE);
//                    prevattachmentTv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_attachment, 0, 0, 0);
//                    // prevattachmentTv.setText(" " + compTaskdataResponse.get(0).getPreviousAttach());
//                    prevattachmentTv.setText(" Click here");
//                }
//            }
//
//            if (compTaskdataResponse.get(0).getCreatedBy() != null && !compTaskdataResponse.get(0).getCreatedBy().isEmpty()) {
//                taskRespondby.setText(" " + compTaskdataResponse.get(0).getCreatedBy());
//            }
//
//            /*String[] date2 = AndroidUtil.formatDate(compTaskdataResponse.get(0).getUpdatedOn(), "yyyy-dd-mm HH:MM:SS", "dd-mm-yyyy HH:MM:SS").split(" ");
//            if (date2.length > 0) {
//                taskRespondedon.setText(" " + date2[0]);
//            }*/
//
//            if (compTaskdataResponse.get(0).getUpdatedOn() != null && !compTaskdataResponse.get(0).getUpdatedOn().isEmpty()) {
//                taskRespondedon.setText(" " + compTaskdataResponse.get(0).getUpdatedOn());
//            }
//
//            if (compTaskdataResponse.get(0).getRemark() != null && !compTaskdataResponse.get(0).getRemark().isEmpty()) {
//                tvMessageBottom.setText(" " + compTaskdataResponse.get(0).getRemark());
//            }
//
//            if (compTaskdataResponse.get(0).getDoc() != null && !compTaskdataResponse.get(0).getDoc().isEmpty()) {
//                tvAttachmentBottom.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_attachment, 0, 0, 0);
//                // tvAttachmentBottom.setText(" " + compTaskdataResponse.get(0).getDoc());
//                tvAttachmentBottom.setText(" Click here");
//            }
//        }
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

    private void newTaskApiCall(String task_id) {

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
        INEWTASK inewtask = retrofit.create(INEWTASK.class);
        Call<PendingTaskDetail> call = inewtask.getTask(task_id);
        Log.e(TAG + "chek id", task_id.toString());
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<PendingTaskDetail> call, Response<PendingTaskDetail> response) {
        newTaskData = response.body();

        if (newTaskData != null) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (Integer.parseInt(newTaskData.getStatus()) == Constant.StatusCode.CLEANMONEY_CODE_SUCCESS) {
                if (newTaskData.getData() != null && newTaskData.getData().size() > 0) {
                    compTaskdataData = newTaskData.getData();
                    if (newTaskData.getResponse() != null && newTaskData.getResponse().size() > 0) {
                        compTaskdataResponse = newTaskData.getResponse();
                        adapterMessageChain = new AdapterMessageChain(getContext(), compTaskdataResponse);
                        recyclerView.setAdapter(adapterMessageChain);
                        adapterMessageChain.notifyDataSetChanged();
                        setallcontent();
                    }
                }
            }
        }
    }

    @Override
    public void onFailure(Call<PendingTaskDetail> call, Throwable t) {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    public interface INEWTASK {
        @POST(Constant.WebUrl.TASK_DETAIL)
        Call<PendingTaskDetail> getTask(@Query("task_id") String task_id);

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
