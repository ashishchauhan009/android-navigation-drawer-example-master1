package net.simplifiedcoding.navigationdrawerexample.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;

import java.text.SimpleDateFormat;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import net.simplifiedcoding.navigationdrawerexample.Constant.Constant;
import net.simplifiedcoding.navigationdrawerexample.Model.CreateTask;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.activities.MainActivity;
import net.simplifiedcoding.navigationdrawerexample.util.AndroidUtil;
import net.simplifiedcoding.navigationdrawerexample.util.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
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

public class FragmentCreateTask extends Fragment implements View.OnClickListener {

    private static final String TAG = FragmentCreateTask.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private TextView tvTitleName;
    private AppCompatEditText etAttachment, etToemail, etSubject, etMessage;
    private ProgressDialog pDialog;
    private AppCompatImageView date_meeting_iv;
    private String email, user_id_to, user_id;
    public static final int rcCC = 33;
    private Uri FileUri = null;
    private Call<ResponseBody> submitTask = null;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private File file;
    private AppCompatImageView changeDate;
    static EditText date_meeting_ed;
    FragmentCreateTask fragmentCreateTask;
    String userChoosenTask;
    private static final int REQUEST_CAMERA = 121;
    private static final int RESULT_LOAD = 1211;
    private static final int FILE_SELECT_CODE = 1212;
String path;

    //for date
    private int year;
    private int month;
    private int day;

    static final int DATE_PICKER_ID = 1111;
    private DialogFragment newFragment;


    public FragmentCreateTask() {
        // Required empty public constructor
    }


    public static FragmentCreateTask newInstance() {
        FragmentCreateTask fragment = new FragmentCreateTask();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_createtask, container, false);
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
            email = getArguments().getString("email");
            user_id_to = getArguments().getString("id");
            Log.d("check email id ", email + user_id_to);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedpreferences.edit();
        user_id = sharedpreferences.getString("User_id", "").toString();
        Log.d("check shared preference", sharedpreferences.getString("TittleName", "").toString() + sharedpreferences.getString("User_id", "").toString());
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
        tvTitleName = (TextView) view.findViewById(R.id.task_name_tv);
        date_meeting_ed = (EditText) view.findViewById(R.id.date_meeting_ed);
        etToemail = (AppCompatEditText) view.findViewById(R.id.et_toemail);
        etSubject = (AppCompatEditText) view.findViewById(R.id.et_subject);
        etMessage = (AppCompatEditText) view.findViewById(R.id.et_message);
        changeDate = (AppCompatImageView) view.findViewById(R.id.iv_date);
        date_meeting_iv = (AppCompatImageView) view.findViewById(R.id.date_meeting_iv);

        etAttachment = (AppCompatEditText) view.findViewById(R.id.et_attachment);

        tvTitleName.setText(getString(R.string.create_task_detail));
        if (email != null) {
            etToemail.setText(email);
            etToemail.setEnabled(false);
        }

        view.findViewById(R.id.btn_choosefile).setOnClickListener(this);
        view.findViewById(R.id.btn_createtask).setOnClickListener(this);


        //date set
        // Get current date by calender

        /*final Calendar c = Calendar.getInstance();
         year = c.get(Calendar.YEAR);
         month = c.get(Calendar.MONTH);
         day = c.get(Calendar.DAY_OF_MONTH);
         etDate.setText(day + "-" + (month+1) + "-" + year);*/

        // Button listener to show date picker dialog

//        changeDate.setOnClickListener(this);
        date_meeting_iv.setOnClickListener(this);

    }


   /* public void showDatePicker(View v) {
        DatePickerDate newFragment = new DatePickerDate();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        newFragment.setArguments(args);
        */

    /**
     * Set Call back to capture selected date
     *//*
        newFragment.setCallBack(ondate);
        newFragment.show(getFragmentManager(), "datePicker");

    }*/

   /* DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            etDate.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(year));
        }
    };*/
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
            case R.id.btn_choosefile:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        ///method to get Images
                        getAttachmentPdf();
                    } else {
                        if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            Toast.makeText(getActivity(), "Your Permission is needed to get " + "access the camera", Toast.LENGTH_LONG).show();
                        }
                        FragmentCreateTask.this.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }
                } else {
                    getAttachmentPdf();
                }

//                setDialog();
                break;
            case R.id.btn_createtask:

                String strSubject = etSubject.getText().toString();

                CreateTask createTask = new CreateTask();
                createTask.setMessage(etMessage.getText().toString());
                createTask.setTask_subject(etSubject.getText().toString());


                if (user_id != null && user_id_to != null) {
                    createTask.setId(user_id);
                    createTask.setTask_for(user_id_to);
                }
                if (date_meeting_ed.getText().toString() != null && date_meeting_ed.getText().toString() != "") {
                    createTask.setDate(date_meeting_ed.getText().toString());
                } else {
                    showMessage(v, getString(R.string.choosedate));
                    return;
                }
                createTaskApiCall(createTask);
                break;

            case R.id.date_meeting_iv:

                try {
                    newFragment = new SelectDateFragment();
                    //                newFragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
                    newFragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
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

    //Added by Mahendra 14-4-2017
    private void getAttachmentPdf() {
        etAttachment.setText("");
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("application/pdf");
//            intent.setType("application/msword,application/pdf");
            intent.setType("*/*");
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
                    /*Uri uri = data.getData();
                    String path = uri.getPath();
                    if (path.startsWith("/file")) {
                        path = path.replaceFirst("/file", "");
                    }
                    file = new File(path);
                    Log.e("check file path", path.toString());

                    String extension = getExtension(file);
                    if (file != null) {
                        etAttachment.setText(file.toString());
                    }*/

                    path="";

                    Uri filePath = data.getData();

                    if (filePath.toString().startsWith("file://")) {
                        file = new File(filePath.getPath());
                    } else {
                        String str = getImagePath(filePath);

                        if(str!=null){
                            file = new File(str);


                        }
                        else{
                            Toast.makeText(getActivity(), "please select file from file manager only", Toast.LENGTH_SHORT).show();
                        }

                    }

                    if(file!=null){

                     if (file.toString().contains(".pdf")) {
                        etAttachment.setText(filePath.toString());
                        if (file.length() <= 1048576) {
                            Log.d(TAG, "file size is max for this file option");
                        } else {
                            etAttachment.setText("");
                            Toast.makeText(getActivity(), "PDF cannot be more than 1 MB, please select another", Toast.LENGTH_SHORT).show();
                        }
                    } else if (file.toString().contains(".doc") && file.toString().contains(".docx")) {
                        etAttachment.setText(filePath.toString());
                        if (file.length() <= 1048576) {
                            Log.d(TAG, "file size is max for this file option");
                        }
                    } else if (file.toString().contains(".jpeg") || file.toString().contains(".png") || file.toString().contains(".jpg")) {
                        etAttachment.setText(filePath.toString());
                                /* if (file.length() <= 1048576) {
                                Log.d(TAG, "file size is max for this file option");
                                } else {
                                etAttachment.setText("");

                                Toast.makeText(getActivity(), "Doc cannot be more than 1 MB, please select another", Toast.LENGTH_SHORT).show();
                                }*/
                    } else {
                        Toast.makeText(getActivity(), "Can only upload PDF,Doc And Images file ", Toast.LENGTH_SHORT).show();
                    }
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


    /*void picPhoto() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.Fields.WRITE_FILE_PERMISSION);
            return;
        } else
            performImgPicAction(1);
    }

    void performImgPicAction(int which) {
        Intent in;
        if (which == 1) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*//*");
    //        startActivityForResult(intent, rcCC);
   //     }
   // }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == rcCC) {
            try {
                if (resultCode == Activity.RESULT_OK) {
                    final Uri imageUri = data.getData();
                   *//**//* final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                     final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    ivProfilePicture.setImageBitmap(selectedImage);*//**//*
                    //Picasso.with(getContext()).load(imageUri).resize(200, 0).into(ivProfilePicture);
                    etAttachment.setText(imageUri+"");
                    profileImageUri = imageUri;
                    Log.e(TAG, imageUri.toString());
                }
            } catch (Exception e) {
               // FirebaseCrash.report(e);
                e.printStackTrace();
            }
            //ivProfilePicture.setImageBitmap(uImage);
        }
        // super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = (Fragment) getFragmentManager().findFragmentByTag(FragmentCreateTask.class.getSimpleName());
    }*/

    /*  void picPhoto(){
          Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
          intent.addCategory(Intent.CATEGORY_OPENABLE);
          intent.setType("**//*");
        Intent i = Intent.createChooser(intent, "File");
        startActivityForResult(i, 1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri uri  = intent.getData();

                String fileImagePath = getRealPathFromURI(uri);
                String type = intent.getType();

                Log.d("Hello", fileImagePath + "");
                if (uri != null) {
                    String path = uri.toString();
                    if (path.toLowerCase().startsWith("file://")) {
                        // Selected file/directory path is below
                        path = (new File(URI.create(path))).getAbsolutePath();
                        Log.d("Hello path", path);
                    }

                }
            } else
                Log.d("Hello", "Back from pick with cancel status");
        }
    }
    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = (getContext().getContentResolver().query(contentUri, proj, null, null, null));
        if(cursor.moveToFirst()){;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }*/
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void createTaskApiCall(final CreateTask createTask) {
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


        Gson gson = new Gson();
        String jsonData = gson.toJson(createTask);


        Log.d(TAG, jsonData);

        // File file = new File(AndroidUtil.getRealPathFromURI(getContext(), FileUri));
        // create RequestBody instance from file
        if (file != null) {
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            Log.d("check image uri", file.toString());

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            submitTask = icreatetask.submitTask(jsonData, body);
        } else {
            submitTask = icreatetask.submitTask(jsonData);
        }
//        Log.e("check pic file", body + "");

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
                                Toast.makeText(getContext(), jsonObject.getString("tag") + "", Toast.LENGTH_LONG).show();
                                getFragmentManager().popBackStackImmediate();
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
                if (!call.isExecuted() && createTask != null && isVisible() && AndroidUtil.isOnline(getContext())) {
                    // updateProfile(profile);
                } else {
                    call.cancel();
                }
            }
        });
    }

    public interface ICREATETASK {
        @Multipart
        @POST(Constant.WebUrl.CREATETASK)
        Call<ResponseBody> submitTask(@Query("data") String data, @Part MultipartBody.Part file);

        @GET(Constant.WebUrl.CREATETASK)
        Call<ResponseBody> submitTask(@Query("data") String data);
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

    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), this, yy, mm, dd);

            mDatePicker.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationEditProfile;
            mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
            //            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());

            return mDatePicker;
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm + 1, dd);
        }

        public void populateSetDate(int year, int month, int day) {
            date_meeting_ed.setText(day + "-" + month + "-" + year);

        }
    }

    public void checkCameraPermission() {
        boolean isGranted;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Camera permission has not been granted.
            try {
//                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},
//                        REQUEST_CAMERA);
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQUEST_CAMERA);
            } catch (Exception e) {
            }
        } else {
            takePicture();
        }
    }

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }


    void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri file = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD);
    }

    private void selectImage() {
        final CharSequence[] items = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity());
                if (items[item].equals("Camera")) {
                    userChoosenTask = "Camera";
                    if (result)
//                        takeImageFromCamera();
                        checkCameraPermission();
                } else if (items[item].equals("Gallery")) {
                    userChoosenTask = "Gallery";
                    if (result)
                        galleryIntent();
                }
            }
        });
        builder.show();
    }

    public void selectVideoFromGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, FILE_SELECT_CODE);
    }

    private void setDialog() {
        final CharSequence[] items = {"Images", "Video", "Document"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity());

                if (items[item].equals("Images")) {
                    userChoosenTask = "Images";
                    if (result)
                        selectImage();
                } else if (items[item].equals("Video")) {
                    userChoosenTask = "Video";
                    if (result)
                        selectVideoFromGallery();

                } else if (items[item].equals("Document")) {
                    userChoosenTask = "Document";
                    if (result)
                        getAttachmentPdf();
                }
            }
        });
        builder.show();
    }

    public String getImagePath(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();

        if (cursor != null && cursor.getCount() > 0) {
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

            cursor.close();
        }
        return path;
    }


}