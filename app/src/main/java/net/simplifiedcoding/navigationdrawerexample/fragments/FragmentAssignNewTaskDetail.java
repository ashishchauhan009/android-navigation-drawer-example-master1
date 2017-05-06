package net.simplifiedcoding.navigationdrawerexample.fragments;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v7.app.NotificationCompat;
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
import android.widget.Toast;

import net.simplifiedcoding.navigationdrawerexample.Constant.Constant;
import net.simplifiedcoding.navigationdrawerexample.Model.PendingTaskDetail;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.activities.MainActivity;
import net.simplifiedcoding.navigationdrawerexample.activities.WebView_Activity;
import net.simplifiedcoding.navigationdrawerexample.service.DownloadTaskCleanMoney;
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

public class FragmentAssignNewTaskDetail extends Fragment implements Callback<PendingTaskDetail>, View.OnClickListener {
    private NotificationManager mNotifyManager;
    private Builder mBuilder;
    int id = 1;
    private static final String TAG = FragmentAssignNewTaskDetail.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private TextView tvTitleName, countTv, assignedDateTv, assignedNameTv, subjectTv, taskCompDate, tvMessageUpper,
            taskRespondby, taskRespondedon, taskStatus, tvMessageBottom, tvAttachment, tvAttachmentBottom,
            taskPrevrespondon, tvPrevmessage, prevattachmentTv, tv_viewattachment;
    private ProgressDialog pDialog;
    private String task_id;
    String message = "";
    String status = "";
    public static final int rcCC = 33;
    private Uri FileUri = null;
    private static final int READ_WRITE_PERMISSION = 12221;
    private Call<ResponseBody> submitTask = null;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    Context context;
    private File file;
    private String user_id;
    private PendingTaskDetail newTaskData;
    private List<PendingTaskDetail.Datum> compTaskdataData;
    private List<PendingTaskDetail.Response> compTaskdataResponse;

    public FragmentAssignNewTaskDetail() {
        // Required empty public constructor
    }

    public static FragmentAssignNewTaskDetail newInstance() {
        FragmentAssignNewTaskDetail fragment = new FragmentAssignNewTaskDetail();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_assign_newtaskdetail, container, false);
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
        tvTitleName = (TextView) view.findViewById(R.id.task_name_tv);
        tv_viewattachment = (TextView) view.findViewById(R.id.tv_viewattachment);
        tvMessageUpper = (TextView) view.findViewById(R.id.tv_message_upper);
        countTv = (TextView) view.findViewById(R.id.tv_count);
        assignedDateTv = (TextView) view.findViewById(R.id.tv_assigned_date);
        assignedNameTv = (TextView) view.findViewById(R.id.tv_assigned_name);
        subjectTv = (TextView) view.findViewById(R.id.tv_subject);
        taskCompDate = (TextView) view.findViewById(R.id.task_comp_date);
        tvAttachment = (TextView) view.findViewById(R.id.attachment_tv);
        taskStatus = (TextView) view.findViewById(R.id.task_status);
        tvTitleName.setText(getString(R.string.assign_task));
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
            if (compTaskdataData.get(0).getDoc() != null && !compTaskdataData.get(0).getDoc().isEmpty()) {
                tvAttachment.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_attachment, 0, 0, 0);
                tvAttachment.setText("Click here to download");
            }
            if (compTaskdataData.get(0).getDoc() != null && !compTaskdataData.get(0).getDoc().isEmpty()) {
                tv_viewattachment.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_attachment, 0, 0, 0);
                tv_viewattachment.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.attachment_tv:
                if (!compTaskdataData.get(0).getDoc().isEmpty()) {
                    if (isConnectingToInternet()) {
                        final int id = 1;
                        mNotifyManager =
                                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                        mBuilder = new NotificationCompat.Builder(getActivity());
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
                        new DownloadTaskCleanMoney(context, compTaskdataData.get(0).getDoc());
                        Toast.makeText(context, "Downloading file", Toast.LENGTH_SHORT).show();
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


    @Override
    public void onRequestPermissionsResult(int permsRequestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (permsRequestCode) {

            case READ_WRITE_PERMISSION:

                boolean readWriteAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;
//                addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//                addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE);
                if (readWriteAccepted) {

                }
                break;

        }
    }


    private boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
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
                    setallcontent();
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
