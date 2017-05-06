package net.simplifiedcoding.navigationdrawerexample.fragments;

import android.Manifest;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import net.simplifiedcoding.navigationdrawerexample.Constant.Constant;
import net.simplifiedcoding.navigationdrawerexample.Model.CreateTask;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

public class FragmentPendingTaskDetail extends Fragment implements Callback<PendingTaskDetail>, View.OnClickListener {

    private static final String TAG = FragmentPendingTaskDetail.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private PendingTaskDetail pendingTaskDetailData;
    List<PendingTaskDetail.Response> getResponses = new ArrayList<PendingTaskDetail.Response>();
    private ProgressDialog pDialog;
    SharedPreferences sharedpreferences;
    LinearLayout ll_message_area;
    String user_id;
    AppCompatButton btn_reply, btn_choosefile, btn_submittask;
    AppCompatEditText et_message, et_attachment;
    TextView assigned_date_tv, message_chain_tv, assigned_name_tv, task_comp_date_tv, task_no_tv, subject_tv,
            desc_tv, attachment_tv, task_name_tv, tvTitleName, tv_viewattachment;
    private File file;
    Spinner task_status_spinner;
    TextView tv_contentReassign;
    private Call<ResponseBody> submitTask = null;
    String[] taskStatusArray = {"Work In Progress", "Complete"};
    String path;
    String[] concat;
    RecyclerView recyclerView;
    AdapterMessageChain adapterMessageChain;
    String strReassign;

    // holder.assigned_by_tv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_assignedby, 0, 0, 0);
    public FragmentPendingTaskDetail() {
        // Required empty public constructor
    }


    public static FragmentPendingTaskDetail newInstance() {
        FragmentPendingTaskDetail fragment = new FragmentPendingTaskDetail();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pendingtask_detail, container, false);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Bundle bundle = getArguments();
        String task_id = bundle.getString(Constant.TASK_ID);
        strReassign = bundle.getString(Config.Reassign);
        initView(rootView, task_id);

        return rootView;
    }


    void initView(View view, String task_id) {
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
        pDialog.setCancelable(false);

        ll_message_area = (LinearLayout) view.findViewById(R.id.ll_message_area);
//        ll_recycler = (LinearLayout) view.findViewById(R.id.ll_recycler);

        tv_contentReassign = (TextView) view.findViewById(R.id.tv_contentReassign);
        task_no_tv = (TextView) view.findViewById(R.id.count_tv);
        assigned_date_tv = (TextView) view.findViewById(R.id.assigned_date_tv);
        assigned_name_tv = (TextView) view.findViewById(R.id.assigned_name_tv);
        task_comp_date_tv = (TextView) view.findViewById(R.id.task_comp_date_tv);
        subject_tv = (TextView) view.findViewById(R.id.subject_tv);
        desc_tv = (TextView) view.findViewById(R.id.tv_message);
        attachment_tv = (TextView) view.findViewById(R.id.attachment_tv);
        tvTitleName = (TextView) view.findViewById(R.id.task_name_tv);
        et_message = (AppCompatEditText) view.findViewById(R.id.et_message);
        et_attachment = (AppCompatEditText) view.findViewById(R.id.et_attachment);
        btn_reply = (AppCompatButton) view.findViewById(R.id.btn_reply);
        btn_choosefile = (AppCompatButton) view.findViewById(R.id.btn_choosefile);
        btn_submittask = (AppCompatButton) view.findViewById(R.id.btn_submit);
        task_status_spinner = (Spinner) view.findViewById(R.id.spn_status);
        message_chain_tv = (TextView) view.findViewById(R.id.message_chain_tv);
        tv_viewattachment = (TextView) view.findViewById(R.id.tv_viewattachment);
        btn_reply.setOnClickListener(this);
        btn_choosefile.setOnClickListener(this);
        btn_submittask.setOnClickListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_detail);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        ((TextView) view.findViewById(R.id.attachment_tv)).setOnClickListener(this);

        if (strReassign.contains("Reassign")) {
            tv_contentReassign.setVisibility(View.VISIBLE);
        } else {
            tv_contentReassign.setVisibility(View.GONE);
        }

        tvTitleName.setText("PENDING TASK DETAILS");
        tv_contentReassign.setText(R.string.reassign_content);
        getNewTaskServerData(task_id);
        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, taskStatusArray);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        task_status_spinner.setAdapter(aa);
        tv_viewattachment.setOnClickListener(this);
//
    }

    private void getNewTaskServerData(String task_id) {

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
        Call<PendingTaskDetail> call = ifaqdata.getData(task_id);
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

    String attachment_url = "";

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reply:
                if (ll_message_area.getVisibility() == View.INVISIBLE) {
                    ll_message_area.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    message_chain_tv.setText(getString(R.string.response_chain));
                    et_message.setText(" ");
                }
                break;
            case R.id.btn_choosefile:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        ///method to get Images
                        getAttachmentPdf();
                    } else {
                        if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            Toast.makeText(getActivity(), "Your Permission is needed to get " + "access the camera", Toast.LENGTH_LONG).show();
                        }
                        FragmentPendingTaskDetail.this.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }
                } else {
                    getAttachmentPdf();
                }

                break;
            case R.id.btn_submit:
                createTaskApiCall();
                break;
            case R.id.attachment_tv:
                if (!pendingTaskDetailData.getData().get(0).getDoc().isEmpty()) {
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pendingTaskDetailData.getData().get(0).getDoc()));
//                    getContext().startActivity(browserIntent);

                    attachment_url = pendingTaskDetailData.getData().get(0).getDoc().toString();

                    if (isConnectingToInternet()) {
                        final int id = 1;
                        final NotificationManager mNotifyManager =
                                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                        final android.support.v4.app.NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity());
                        mBuilder.setContentTitle("File Download")
                                .setContentText("Download in progress")
                                .setSmallIcon(R.drawable.pdf_icon);
                        new Thread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        int incr;
                                        for (incr = 0; incr <= 100; incr += 25) {
                                            mBuilder.setProgress(100, incr, false);
                                            mNotifyManager.notify(id, mBuilder.build());
                                            try {
                                                Thread.sleep(5 * 100);
                                            } catch (InterruptedException e) {
                                                Log.d(TAG, "sleep failure");
                                            }
                                        }
                                        mBuilder.setContentText("Download complete")
                                                .setProgress(0, 0, false);
                                        mNotifyManager.notify(id, mBuilder.build());
                                    }
                                }
                        ).start();

                        new DownloadTaskCleanMoney(getActivity(), pendingTaskDetailData.getData().get(0).getDoc());

                        Toast.makeText(getActivity(), "File Download..", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), Config.Interet_Error, Toast.LENGTH_LONG).show();
                    }
                }
                break;

            case R.id.tv_viewattachment:
                Intent in = new Intent(getActivity(), WebView_Activity.class);
                in.putExtra(Config.Webview_url, pendingTaskDetailData.getData().get(0).getDoc().toString());
                startActivity(in);
                break;
        }
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
    public void onRequestPermissionsResult(int permsRequestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (permsRequestCode) {

            case 1:
                boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (cameraAccepted) {
                    getAttachmentPdf();
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


    //Added by Mahendra 14-4-2017
    private void getAttachmentPdf() {
        et_attachment.setText("");
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("application/pdf");
            intent.setType("application/msword,application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(intent, 1);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    path = uri.getPath();
                    if (path.startsWith("/file")) {
                        path = path.replaceFirst("/file", "");
                    }
                    if (path != null && !path.isEmpty()) {
                        concat = path.split("/");
                    }
                    file = new File(path);
                    Log.e("check file path", path.toString());

                    String extension = getExtension(file);
                    if (file != null) {
                        et_attachment.setText(file.toString());
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


    @Override
    public void onResponse(Call<PendingTaskDetail> call, Response<PendingTaskDetail> response) {
        pendingTaskDetailData = response.body();
        //Log.e(TAG,faqModelData.toString());
        if (pendingTaskDetailData != null) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (Integer.parseInt(pendingTaskDetailData.getStatus()) == Constant.StatusCode.CLEANMONEY_CODE_SUCCESS) {
                if (pendingTaskDetailData.getResponse() != null)
                    getResponses = pendingTaskDetailData.getResponse();
                adapterMessageChain = new AdapterMessageChain(getContext(), getResponses);

                recyclerView.setAdapter(adapterMessageChain);
                adapterMessageChain.notifyDataSetChanged();
                setTextData();
            }
        }
    }

    private void setTextData() {

        if (pendingTaskDetailData.getData().get(0).getDoc() != null && !pendingTaskDetailData.getData().get(0).getDoc().isEmpty()) {
            attachment_tv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_attachment, 0, 0, 0);
            attachment_tv.setText(" " + pendingTaskDetailData.getData().get(0).getDoc());
            attachment_tv.setText("Click here to download");

        }
        if (pendingTaskDetailData.getData().get(0).getDoc() != null && !pendingTaskDetailData.getData().get(0).getDoc().isEmpty()) {
            tv_viewattachment.setVisibility(View.VISIBLE);
            tv_viewattachment.setOnClickListener(this);
        }

        if (pendingTaskDetailData.getData().get(0).getTaskId() != null && !pendingTaskDetailData.getData().get(0).getTaskId().isEmpty()) {
            task_no_tv.setText(" " + pendingTaskDetailData.getData().get(0).getTaskId());
        }
       /* String[] date = AndroidUtil.formatDate(pendingTaskDetailData.getData().get(0).getCreatedOn(), "yyyy-mm-dd HH:MM:SS", "dd-mm-yyyy HH:MM:SS").split(" ");
        if (date.length > 0) {
            assigned_date_tv.setText(" " + date[0]);
        }*/
        if (pendingTaskDetailData.getData().get(0).getCreatedOn() != null && !pendingTaskDetailData.getData().get(0).getCreatedOn().isEmpty()) {
            assigned_date_tv.setText(pendingTaskDetailData.getData().get(0).getCreatedOn());
        }
        if (pendingTaskDetailData.getData().get(0).getCreatedBy() != null && !pendingTaskDetailData.getData().get(0).getCreatedBy().isEmpty()) {
            assigned_name_tv.setText(pendingTaskDetailData.getData().get(0).getCreatedBy());
        }
        /*String[] task_com_date = AndroidUtil.formatDate(pendingTaskDetailData.getResponse().get(0).getUpdatedOn(), "yyyy-mm-dd", "dd-mm-yyyy").split(" ");
        if (task_com_date.length > 0) {
            task_comp_date_tv.setText(" " + task_com_date[0]);
        }*/
        if (pendingTaskDetailData.getData().get(0).getTaskCompletionDate() != null && !pendingTaskDetailData.getData().get(0).getTaskCompletionDate().isEmpty()) {
            task_comp_date_tv.setText(" " + pendingTaskDetailData.getData().get(0).getTaskCompletionDate());
        }
        if (pendingTaskDetailData.getData().get(0).getTaskSubject() != null && !pendingTaskDetailData.getData().get(0).getTaskSubject().isEmpty()) {
            subject_tv.setText(" " + pendingTaskDetailData.getData().get(0).getTaskSubject());
        }
        if (pendingTaskDetailData.getData().get(0).getTaskDescription() != null &&
                !pendingTaskDetailData.getData().get(0).getTaskDescription().isEmpty()) {
            desc_tv.setText(" " + pendingTaskDetailData.getData().get(0).getTaskDescription());
        }
        /*if (pendingTaskDetailData.getResponse().get(0).getRemark() != null &&
                !pendingTaskDetailData.getResponse().get(0).getRemark().isEmpty()) {
//            et_message.setText(" " + pendingTaskDetailData.getResponse().get(0).getRemark());
            et_message.setText(" ");
        }*/
    }

    @Override
    public void onFailure(Call<PendingTaskDetail> call, Throwable t) {

    }

    public interface IFAQDATA {
        @GET(Constant.WebUrl.TASK_DETAIL)
        Call<PendingTaskDetail> getData(@Query("task_id") String task_id);
    }


    private void createTaskApiCall() {
        if (pDialog != null) {
            pDialog.setMessage("Updating...");
            if (!pDialog.isShowing())
                pDialog.show();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.WebUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient().newBuilder().writeTimeout(1, TimeUnit.MINUTES).connectTimeout(1, TimeUnit.MINUTES).readTimeout(1, TimeUnit.MINUTES).build())
                .build();
        ICREATETASK icreatetask = retrofit.create(ICREATETASK.class);

        // create RequestBody instance from file
        if (file != null) {
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            Log.e("check image uri", file.toString());

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            submitTask = icreatetask.submitTask(pendingTaskDetailData.getData().get(0).getTaskId(), user_id, task_status_spinner.getSelectedItem().toString(), et_message.getText().toString(), body);
            //Log.e("check pic file", body + "");
        } else {
            submitTask = icreatetask.submitTask(pendingTaskDetailData.getData().get(0).getTaskId(), user_id, task_status_spinner.getSelectedItem().toString(), et_message.getText().toString());
        }
        submitTask.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (pDialog != null) {
                    pDialog.dismiss();
                }
                try {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        try {
                            String data = response.body().string();
                            JSONObject jsonObject = new JSONObject(data);
                            if (jsonObject.getInt("status") == Constant.StatusCode.CLEANMONEY_CODE_SUCCESS) {
                                getFragmentManager().popBackStack();
                                Toast.makeText(getContext(), jsonObject.getString("tag") + "", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), jsonObject.getString("tag") + "", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (pDialog != null) {
                    pDialog.dismiss();
                }
            }
        });
    }

    public interface ICREATETASK {
        @Multipart
        @POST(Constant.WebUrl.COMPLETETASK)
        Call<ResponseBody> submitTask(@Query("task_id") String taskid, @Query("id") String userId, @Query("status") String status, @Query("reply") String message, @Part MultipartBody.Part file);

        @GET(Constant.WebUrl.COMPLETETASK)
        Call<ResponseBody> submitTask(@Query("task_id") String taskid, @Query("id") String userId, @Query("status") String status, @Query("reply") String message);
    }


}
