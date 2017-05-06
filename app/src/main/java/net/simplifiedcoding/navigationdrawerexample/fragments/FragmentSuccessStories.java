package net.simplifiedcoding.navigationdrawerexample.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.simplifiedcoding.navigationdrawerexample.Constant.Constant;
import net.simplifiedcoding.navigationdrawerexample.Model.NewTaskData;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.activities.MainActivity;
import net.simplifiedcoding.navigationdrawerexample.util.AndroidUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import id.zelory.compressor.Compressor;
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

/**
 * Created by Vibes37 on 21/3/2017.
 */

public class FragmentSuccessStories extends Fragment implements Callback<ResponseBody>, View.OnClickListener {
    private ProgressDialog pDialog;
    SharedPreferences sharedpreferences;
    TextView tvTitleName, image_tv;
    String user_id;
    File image_file, document_file;
    AppCompatEditText title_ed, heading_ed, desc_ed, author_ed, url_ed;
    AppCompatButton btn_submit, btn_browse;
    private FragmentPendingTask.OnFragmentInteractionListener mListener;
    private Uri imageUri;
    String realPath, path;

    private static final int PICK_DOCUMENT_FROM_CAMERA = 1;
    private static final int PICK_DOCUMENT_FROM_GALLERY = 2;
    private static final int CROP_FROM_CAMERA = 3;
    View view;
    String frag, title, heading, desc, author, url, image, success_id;


    public FragmentSuccessStories() {
        // Required empty public constructor
    }


    public static FragmentSuccessStories newInstance() {
        FragmentSuccessStories fragment = new FragmentSuccessStories();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_successstories, container, false);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Bundle bundle = getArguments();
        if (bundle != null) {
            frag = bundle.getString("frag");
            title = bundle.getString("title");
            heading = bundle.getString("heading");
            desc = bundle.getString("desc");
            author = bundle.getString("author");
            url = bundle.getString("url");
            success_id = bundle.getString("success_id");
            image = bundle.getString("image");
        }
        initView(view);

        return view;
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


        tvTitleName = (TextView) view.findViewById(R.id.tv_actionbar_title);
        title_ed = (AppCompatEditText) view.findViewById(R.id.title_ed);
        heading_ed = (AppCompatEditText) view.findViewById(R.id.heading_ed);
        desc_ed = (AppCompatEditText) view.findViewById(R.id.desc_ed);
        author_ed = (AppCompatEditText) view.findViewById(R.id.author_ed);
        url_ed = (AppCompatEditText) view.findViewById(R.id.url_ed);
        image_tv = (TextView) view.findViewById(R.id.image_tv);
        btn_submit = (AppCompatButton) view.findViewById(R.id.btn_submit);
        btn_browse = (AppCompatButton) view.findViewById(R.id.btn_browse);
        btn_submit.setOnClickListener(this);
        btn_browse.setOnClickListener(this);


//        tvTitleName.setText("FAQs");
//        getNewTaskServerData();

        if (frag != null && frag.equalsIgnoreCase("frag_edit")) {
            if (title != null && !title.isEmpty()) {
                title_ed.setText(title);
            }
            if (heading != null && !heading.isEmpty()) {
                heading_ed.setText(heading);

            }
            if (desc != null && !desc.isEmpty()) {
                desc_ed.setText(desc);
            }
            if (author != null && !author.isEmpty()) {
                author_ed.setText(author);
            }
            if (url != null && !url.isEmpty()) {
                url_ed.setText(url);
            }
            if (image != null && !image.isEmpty()) {
                image_tv.setText("You have 1 attachment");
                image_tv.setTextColor(getResources().getColor(R.color.green));
            }


        } else {
            if (sharedpreferences.getString("name", "") != null && sharedpreferences.getString("designation", "") != null) {
                author_ed.setText(sharedpreferences.getString("name", "") + ", " + sharedpreferences.getString("designation", ""));
            }
        }


    }

    private void createSuccessStories() {
        pDialog = new ProgressDialog(getContext(), R.style.DialogTheme);
        pDialog.setCancelable(false);
        if (pDialog != null) {
            pDialog.setMessage(getString(R.string.loading));
            if (!pDialog.isShowing())
                pDialog.show();
        }
        MultipartBody.Part body = null;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.WebUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        IFAQDATA ifaqdata = retrofit.create(IFAQDATA.class);
        if (sharedpreferences.getString("User_id", "") != null) {
            user_id = sharedpreferences.getString("User_id", "");
        }
        if (document_file != null) {
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), document_file);
            Log.e("check image uri", document_file.toString());

            // MultipartBody.Part is used to send also the actual file name
            body = MultipartBody.Part.createFormData("pic", document_file.getName(), requestFile);

        }
        Call<ResponseBody> call = ifaqdata.getData(user_id, title_ed.getText().toString(), desc_ed.getText().toString(), heading_ed.getText().toString(), author_ed.getText().toString(), url_ed.getText().toString(), body);

        call.enqueue(this);

        //ody> getData(@Query("title") String title, @Query("description") String description, @Query("heading") String heading,@Query("author") String author,@Query("url") String url);


    }


    private void editSuccessStoriesDetail() {
        pDialog = new ProgressDialog(getContext(), R.style.DialogTheme);
        pDialog.setCancelable(false);
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
        MultipartBody.Part body = null;
        if (document_file != null) {
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), document_file);
            Log.e("check image uri", document_file.toString());

            // MultipartBody.Part is used to send also the actual file name
            body = MultipartBody.Part.createFormData("pic", document_file.getName(), requestFile);
        }
        Call<ResponseBody> call = ifaqdata.sendEditData(success_id, user_id, title_ed.getText().toString(), desc_ed.getText().toString(), heading_ed.getText().toString(), author_ed.getText().toString(), url_ed.getText().toString(), body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (pDialog != null) {
                    pDialog.dismiss();
                }
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    try {
                        String data = response.body().string();
                        JSONObject jsonObject = new JSONObject(data);
                        if (jsonObject.getInt("status") == Constant.StatusCode.CLEANMONEY_CODE_SUCCESS) {
                            Toast.makeText(getActivity(), jsonObject.getString("tag"), Toast.LENGTH_LONG).show();
                            getFragmentManager().popBackStack();

                        } else {
                            Toast.makeText(getActivity(), jsonObject.getString("tag"), Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (pDialog != null) {
                    pDialog.dismiss();
                }
            }
        });


        //ody> getData(@Query("title") String title, @Query("description") String description, @Query("heading") String heading,@Query("author") String author,@Query("url") String url);


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
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

        if (pDialog != null) {
            pDialog.dismiss();
        }
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            try {
                String data = response.body().string();
                JSONObject jsonObject = new JSONObject(data);
                if (jsonObject.getInt("status") == Constant.StatusCode.CLEANMONEY_CODE_SUCCESS) {
                    Toast.makeText(getActivity(), jsonObject.getString("tag"), Toast.LENGTH_LONG).show();
                    title_ed.setText("");
                    heading_ed.setText("");
                    desc_ed.setText("");
                    url_ed.setText("");
                    document_file = null;
                    getFragmentManager().popBackStack();

                } else {
                    Toast.makeText(getActivity(), jsonObject.getString("tag"), Toast.LENGTH_LONG).show();
                }

            } catch (IOException e) {
                e.printStackTrace();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:

                checkEditText();

                break;
            case R.id.btn_browse:
                if (!(view.findViewById(R.id.footer_dialog).getVisibility() == View.VISIBLE)) {
                    initFooter(view.findViewById(R.id.footer_dialog), "document");
                }
                break;
        }
    }


    public void initFooter(final View view, final String type) {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
            // slide up animation on the dialog
            Animation slideUp = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up_bounce);
            view.startAnimation(slideUp);
            ImageView downimage = (ImageView) view.findViewById(R.id.down_imageview);
            ImageView cameraimage = (ImageView) view.findViewById(R.id.camera_imageview);
            ImageView galleryimage = (ImageView) view.findViewById(R.id.gallery_imageview);
            cameraimage.setOnClickListener(new View.OnClickListener() {
                //                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {

                    // slide down animation for the footer after  clicking
                    Animation slideDown = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
                    view.startAnimation(slideDown);
                    if (view.getVisibility() == View.VISIBLE) {
                        view.setVisibility(View.GONE);
                    }
                    //
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            ///method to get Images
                            takeimage(type);
                        } else {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                                Toast.makeText(getActivity(), "Your Permission is needed to get " + "access the camera", Toast.LENGTH_LONG).show();
                            }
                            FragmentSuccessStories.this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PICK_DOCUMENT_FROM_CAMERA);
                        }
                    } else {
                        takeimage(type);
                    }


                    //
                }

            });
            galleryimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // slide down animation for footer dialog
                    Animation slideDown = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
                    view.startAnimation(slideDown);
                    if (view.getVisibility() == View.VISIBLE) {
                        view.setVisibility(View.GONE);
                    }


                    Intent intent = new Intent();
                    try {
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        if (type.equalsIgnoreCase("document")) {
                            //                            intent.setType("*/*");
                            startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_DOCUMENT_FROM_GALLERY);
                        }

                    } catch (ActivityNotFoundException e) {

                    }

                }


            });

            downimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Animation slideDown = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
                    view.startAnimation(slideDown);
                    if (view.getVisibility() == View.VISIBLE) {
                        view.setVisibility(View.GONE);
                    }
                }
            });


        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (permsRequestCode) {

            case PICK_DOCUMENT_FROM_CAMERA:


                boolean cameraAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                if (cameraAccepted) {
                    takeimage("document");
                }

                break;
            case CROP_FROM_CAMERA:

                boolean cropAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                break;
        }
    }

    private void takeimage(String type) {


        // calling the camera activity
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // get current timestamp
        String milliStr = Calendar.getInstance().getTimeInMillis() + "";

        File f = new File(android.os.Environment.getExternalStorageDirectory(), "Swacch_" + milliStr + "_.jpg");
        imageUri = Uri.fromFile(f);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        //        intent.putExtra(MediaStore.EXTRA_OUTPUT, MediaStore.Images.Media.INTERNAL_CONTENT_URI.toString());
        try {

            //            intent.putExtra("return-data", true);

            if (type.equalsIgnoreCase("document")) {
                startActivityForResult(intent, PICK_DOCUMENT_FROM_CAMERA);
            }

        } catch (ActivityNotFoundException e) {
        }
    }


    // getting the result of camera and gallery on base of request code
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle extras = null;
        //        imageUri = null;

        if (requestCode == PICK_DOCUMENT_FROM_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    if (data != null) {
                        extras = data.getExtras();

                    }

                    if (imageUri != null) {
                        performCrop(imageUri);
                    }

                    //


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else if (requestCode == PICK_DOCUMENT_FROM_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    if (data != null) {
                        imageUri = null;

                        imageUri = data.getData();

                        //


                        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

                        //
                        if (imageUri.toString().startsWith("file://")) {
                            realPath = imageUri.toString().replace("file://", "");
                        } else {
                            realPath = getImagePath(imageUri);
                        }
                        //                        }


                        File f = new File(realPath);
                        Uri contentUri = Uri.fromFile(f);

                        //

                        performCrop(contentUri);
                        //                        }

                        document_file = new File(realPath);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } else if (requestCode == CROP_FROM_CAMERA)

        {
            if (resultCode == Activity.RESULT_OK) {
                imageUri = data.getData();
                //

                final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

                if (imageUri.toString().startsWith("file:")) {
                    realPath = imageUri.toString().replace("file://", "");
                } else {
                    realPath = getImagePath(imageUri);
                }


                document_file = Compressor.getDefault(getActivity()).compressToFile(new File(realPath));
                Log.v("File size in kb after ", String.valueOf(document_file.length() / 1024));
                image_tv.setText("You have uploaded 1 image");
                image_tv.setTextColor(getResources().getColor(R.color.green));

                //                    uri_hashmap.put(lastmapkey, imageUri);
                //                    image_file_hashmap.put(lastmapkey, document_file);
            }


        }
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


    private void performCrop(Uri picUri) {
        try {
            //
            Intent cropIntent;
            //Start Crop Activity
            cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");

            startActivityForResult(cropIntent, CROP_FROM_CAMERA);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "your device doesn't support the crop action!";
        }
    }


    private void checkEditText() {

        if (title_ed.getText().length() == 0) {
            Toast.makeText(getActivity(), "Please enter title", Toast.LENGTH_LONG).show();
        } else if (desc_ed.getText().length() == 0) {
            Toast.makeText(getActivity(), "Please enter description", Toast.LENGTH_LONG).show();
        } else if (url_ed.getText().length() == 0) {

            Toast.makeText(getActivity(), "Please enter url", Toast.LENGTH_LONG).show();
        } else if (url_ed.getText().length() != 0) {
            if (AndroidUtil.isValidUrl(url_ed.getText().toString())) {
                if (frag != null && frag.equalsIgnoreCase("edit_frag")) {
                    if (AndroidUtil.isConnectingToInternet(getActivity())) {
                        editSuccessStoriesDetail();
                    } else {
                        Toast.makeText(getActivity(), Constant.check_internet_connection, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (AndroidUtil.isConnectingToInternet(getActivity())) {
                        createSuccessStories();
                    } else {
                        Toast.makeText(getActivity(), Constant.check_internet_connection, Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(getActivity(), "Please enter valid url", Toast.LENGTH_LONG).show();
            }
        }
    }

    public interface IFAQDATA {
        @Multipart
        @POST(Constant.WebUrl.CREATE_SUCCESS_STORIES)
        Call<ResponseBody> getData(@Query("id") String user_id, @Query("title") String title, @Query("description") String description, @Query("heading") String heading, @Query("author") String author, @Query("url") String url, @Part MultipartBody.Part file);


        @GET(Constant.WebUrl.EDIT_SUCCESS_STORIES)
        Call<ResponseBody> sendEditData(@Query("success_id") String success_id, @Query("id") String user_id, @Query("title") String title, @Query("description") String description, @Query("heading") String heading, @Query("author") String author, @Query("url") String url, @Part MultipartBody.Part file);
    }

    @Override
    public void onResume() {
        super.onResume();

//        ((MainActivity)getActivity()).PopFragment(view);


    }
}
