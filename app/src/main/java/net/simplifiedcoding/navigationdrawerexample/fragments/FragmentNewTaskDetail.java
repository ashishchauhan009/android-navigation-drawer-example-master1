package net.simplifiedcoding.navigationdrawerexample.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
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
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NotificationCompat.Builder;

import com.google.gson.Gson;

import net.simplifiedcoding.navigationdrawerexample.Constant.Constant;
import net.simplifiedcoding.navigationdrawerexample.Model.CreateTask;
import net.simplifiedcoding.navigationdrawerexample.Model.LoginModel;
import net.simplifiedcoding.navigationdrawerexample.Model.NewTaskData;
import net.simplifiedcoding.navigationdrawerexample.Model.PendingTaskDetail;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.activities.MainActivity;
import net.simplifiedcoding.navigationdrawerexample.activities.WebView_Activity;
import net.simplifiedcoding.navigationdrawerexample.adapter.AdapterNewTask;
import net.simplifiedcoding.navigationdrawerexample.service.DownloadTaskCleanMoney;
import net.simplifiedcoding.navigationdrawerexample.util.AndroidUtil;
import net.simplifiedcoding.navigationdrawerexample.util.Config;
import net.simplifiedcoding.navigationdrawerexample.util.DatePickerDate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

import static android.app.Activity.RESULT_OK;
import static net.simplifiedcoding.navigationdrawerexample.R.id.no_task_tv;

public class FragmentNewTaskDetail extends Fragment implements View.OnClickListener, Callback<PendingTaskDetail> {

    private static final String TAG = FragmentNewTaskDetail.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private TextView tvTitleName, countTv, assignedDateTv, assignedNameTv, subjectTv, taskCompOn, tvMessage, tvAttachment, tv_viewattachment;
    private AppCompatEditText etAttachment, etMessage, etSubject;
    private ProgressDialog pDialog;
    PendingTaskDetail logindata;
    private String task_id;
    String message = "";
    Context context;
    String status = "";
    public static final int rcCC = 33;
    private Uri FileUri = null;
    private Call<ResponseBody> submitTask = null;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private File file;
    private LinearLayout llMessageArea, llHeader, llButton;
    private String user_id;
    private PendingTaskDetail newTaskData;
    private List<PendingTaskDetail.Datum> newTaskdata;
    private Spinner spnStatus;
    private NotificationManager mNotifyManager;
    private Builder mBuilder;

    public FragmentNewTaskDetail() {
        // Required empty public constructor
    }


    public static FragmentNewTaskDetail newInstance() {
        FragmentNewTaskDetail fragment = new FragmentNewTaskDetail();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_newtask_detail, container, false);
        initView(rootView);
        context = getActivity();
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
        try {
            task_id = getArguments().getString("task_id");

            Log.e("check task_id ", task_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedpreferences.edit();
        user_id = sharedpreferences.getString("User_id", "").toString();
        Log.e("check shared preference", sharedpreferences.getString("TittleName", "").toString() + sharedpreferences.getString("User_id", "").toString());


        pDialog = new ProgressDialog(getContext(), R.style.DialogTheme);
        pDialog.setCancelable(false);
        tvTitleName = (TextView) view.findViewById(R.id.task_name_tv);
        etMessage = (AppCompatEditText) view.findViewById(R.id.et_message);
        llMessageArea = (LinearLayout) view.findViewById(R.id.ll_message_area);
        countTv = (TextView) view.findViewById(R.id.count_tv);
        assignedDateTv = (TextView) view.findViewById(R.id.assigned_date_tv);
        assignedNameTv = (TextView) view.findViewById(R.id.assigned_name_tv);
        subjectTv = (TextView) view.findViewById(R.id.subject_tv);
        taskCompOn = (TextView) view.findViewById(R.id.task_comp_tv);
        tvAttachment = (TextView) view.findViewById(R.id.attachment_tv);
        tv_viewattachment = (TextView) view.findViewById(R.id.tv_viewattachment);

        etSubject = (AppCompatEditText) view.findViewById(R.id.et_subject);
        tvMessage = (TextView) view.findViewById(R.id.tv_message);
        spnStatus = (Spinner) view.findViewById(R.id.spn_status);
        // llHeader = (LinearLayout) view.findViewById(R.id.ll_header);
        // llButton = (LinearLayout) view.findViewById(R.id.ll_button);

        // llHeader.setVisibility(View.GONE);
        // llButton.setVisibility(View.GONE);

        etAttachment = (AppCompatEditText) view.findViewById(R.id.et_attachment);
        String[] mystatus = new String[2];
        String value = "Work in Progress";
        mystatus[0] = value;
        value = "Complete";
        mystatus[1] = value;
        final ArrayAdapter<String> arradApterStatus = new ArrayAdapter<String>(getContext(), R.layout.layout_simple_textview_black, mystatus);
        spnStatus.setAdapter(arradApterStatus);

        spnStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status = spnStatus.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tvTitleName.setText("NEW TASK DETAILS");

        view.findViewById(R.id.btn_choosefile).setOnClickListener(this);
        view.findViewById(R.id.btn_submit).setOnClickListener(this);
        view.findViewById(R.id.btn_reply).setOnClickListener(this);

        ((TextView) view.findViewById(R.id.attachment_tv)).setOnClickListener(this);
        llMessageArea.setVisibility(View.GONE);


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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.attachment_tv:
                if (!newTaskdata.get(0).getDoc().isEmpty()) {
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
                        new DownloadTaskCleanMoney(context, newTaskdata.get(0).getDoc());
                        Toast.makeText(context, "Downloading file", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), Config.Interet_Error, Toast.LENGTH_LONG).show();
                    }
                }
                break;

            case R.id.btn_choosefile:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        ///method to get Images
                        picPhoto();
                    } else {
                        if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            Toast.makeText(getActivity(), "Your Permission is needed to get " + "access the camera", Toast.LENGTH_LONG).show();
                        }
                        FragmentNewTaskDetail.this.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }
                } else {
                    picPhoto();
                }

                break;
            case R.id.btn_submit:
                if (task_id != null && etMessage.getText().toString() != null && status != null && user_id != null) {
                    if (!etMessage.getText().toString().isEmpty()) {
                        saveNewtask(task_id, etMessage.getText().toString(), status, user_id);
                    } else
                        showMessage(getView(), getString(R.string.error_message));
                } else {
                    showMessage(v, "Something went wrong...");
                }
                break;
            case R.id.btn_reply:
                llMessageArea.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_viewattachment:
                Intent in = new Intent(getActivity(), WebView_Activity.class);
                in.putExtra(Config.Webview_url, newTaskdata.get(0).getDoc().toString());
                startActivity(in);
                break;
        }
    }

    /*public void showMessage(View view, String tag) {
        if (view != null && view.isShown()) {
            Snackbar snack = Snackbar.make(view, tag, Snackbar.LENGTH_LONG);
            View v = snack.getView();
            TextView tv = (TextView) v.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }
    }*/

    void setallcontent() {


        if (newTaskdata.get(0).getTaskId() != null && !newTaskdata.get(0).getTaskId().isEmpty()) {
            countTv.setText(" " + newTaskdata.get(0).getTaskId());
        }
        /*String[] date = AndroidUtil.formatDate(newTaskdata.get(0).getCreatedOn(), "yyyy-dd-mm HH:MM:SS", "dd-mm-yyyy HH:MM:SS").split(" ");
        if (date.length > 0) {
            assignedDateTv.setText(" " + date[0]);
        }*/
        if (newTaskdata.get(0).getCreatedOn() != null && !newTaskdata.get(0).getCreatedOn().isEmpty()) {
            assignedDateTv.setText(" " + newTaskdata.get(0).getCreatedOn());
        }
        if (newTaskdata.get(0).getCreatedBy() != null && !newTaskdata.get(0).getCreatedBy().isEmpty()) {
            assignedNameTv.setText(" " + newTaskdata.get(0).getCreatedBy());
        }
        if (newTaskdata.get(0).getTaskSubject() != null && !newTaskdata.get(0).getTaskSubject().isEmpty()) {
            subjectTv.setText(" " + newTaskdata.get(0).getTaskSubject());
        }
        if (newTaskdata.get(0).getTaskDescription() != null && !newTaskdata.get(0).getTaskDescription().isEmpty()) {
            tvMessage.setText(" " + newTaskdata.get(0).getTaskDescription());
        }
        /*String[] date1 = AndroidUtil.formatDate(newTaskdata.get(0).getTaskCompletionDate(), "yyyy-dd-mm", "dd-mm-yyyy").split(" ");
        if (date.length > 0) {
            taskCompOn.setText(" " + date1[0]);
        }*/
        if (newTaskdata.get(0).getTaskCompletionDate() != null && !newTaskdata.get(0).getTaskCompletionDate().isEmpty()) {
            taskCompOn.setText(" " + newTaskdata.get(0).getTaskCompletionDate());
        }
        if (newTaskdata.get(0).getDoc() != null && !newTaskdata.get(0).getDoc().isEmpty()) {
            tvAttachment.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_attachment, 0, 0, 0);
            tvAttachment.setText("Click here to download");
        }
        if (newTaskdata.get(0).getDoc() != null && !newTaskdata.get(0).getDoc().isEmpty()) {
            tv_viewattachment.setVisibility(View.VISIBLE);
            tv_viewattachment.setOnClickListener(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (permsRequestCode) {

            case 1:


                boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (cameraAccepted) {
                    picPhoto();
                }

                break;
        }
    }

    private void picPhoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    String path = uri.getPath();
                    if (path.startsWith("/file")) {
                        path = path.replaceFirst("/file", "");
                    }
                    file = new File(path);
                    Log.e("check file path", path.toString());

                    String extension = getExtension(file);
                    if (file != null) {
                        etAttachment.setText(file.toString());
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
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
                    newTaskdata = newTaskData.getData();
                    //llHeader.setVisibility(View.VISIBLE);
                    //llButton.setVisibility(View.VISIBLE);
                    Log.e(TAG, newTaskdata.toString());
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


    public void saveNewtask(String task_id, String reply, String status, String user_id) {

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
        INEWTASKSUBMIT inewtasksubmit = retrofit.create(INEWTASKSUBMIT.class);
        retrofit2.Call<PendingTaskDetail> call = null;
        if (file != null) {
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            call = inewtasksubmit.submitTask(task_id, reply, status, user_id, body);
        } else {
            call = inewtasksubmit.submitTask(task_id, reply, status, user_id);
        }

        call.enqueue(submitData);

    }

    retrofit2.Callback<PendingTaskDetail> submitData = new retrofit2.Callback<PendingTaskDetail>() {

        @Override
        public void onResponse(retrofit2.Call<PendingTaskDetail> call, retrofit2.Response<PendingTaskDetail> response) {
            logindata = response.body();
            if (logindata != null) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                if (Integer.parseInt(logindata.getStatus()) == Constant.StatusCode.CLEANMONEY_CODE_SUCCESS) {
                    showMessage(getView(), getString(R.string.submit));
                    getFragmentManager().popBackStackImmediate();
                } else {
                    showMessage(getView(), logindata.getTag());
                }
            }
        }

        @Override
        public void onFailure(retrofit2.Call<PendingTaskDetail> call, Throwable t) {
            showMessage(getView(), t.getMessage());
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    };

    public interface INEWTASK {
        @POST(Constant.WebUrl.TASK_DETAIL)
        Call<PendingTaskDetail> getTask(@Query("task_id") String task_id);

    }

    public interface INEWTASKSUBMIT {
        @Multipart
        @POST(Constant.WebUrl.ACTION_TASK)
        Call<PendingTaskDetail> submitTask(@Query("task_id") String task_id, @Query("reply") String reply, @Query("status") String status, @Query("id") String id, @Part MultipartBody.Part file);

        @GET(Constant.WebUrl.ACTION_TASK)
        Call<PendingTaskDetail> submitTask(@Query("task_id") String task_id, @Query("reply") String reply, @Query("status") String status, @Query("id") String id);
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

    private boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

}