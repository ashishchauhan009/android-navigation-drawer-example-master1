package net.simplifiedcoding.navigationdrawerexample.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.simplifiedcoding.navigationdrawerexample.Constant.Constant;
import net.simplifiedcoding.navigationdrawerexample.Model.OutReachData;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.activities.MainActivity;
import net.simplifiedcoding.navigationdrawerexample.util.AndroidUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by vibes on 25/3/17.
 */

public class FragmentAddOutReach extends Fragment implements View.OnClickListener {
    View view;
    LinearLayout image_layout;
    AppCompatEditText officer_name_ed, designation_ed, venue_ed, audience_ed;
    static EditText date_meeting_ed;
    AppCompatImageView date_meeting_iv;
    AppCompatButton btn_choose_iv, btn_submit;
    ProgressDialog pDialog;
    ImageView cross_image1, cross_image2, cross_image3, cross_image4, cross_image5, product_image5, product_image1, product_image2, product_image3, product_image4;
    RelativeLayout image1_layout, image2_layout, image3_layout, image4_layout, image5_layout;
    File image_file, document_file;
    ArrayList<File> document_list = new ArrayList<File>();
    HashMap<Integer, File> image_file_hashmap = new HashMap<Integer, File>();
    private int lastmapkey, mapkey, first_image = 0, second_image = 0, third_image = 0, fourth_image = 0, fifth_image = 0;
    private static final int PICK_DOCUMENT_FROM_CAMERA = 1;
    private static final int PICK_DOCUMENT_FROM_GALLERY = 2;
    private static final int CROP_FROM_CAMERA = 3;
    private Uri imageUri;
    String path, realPath, user_id;
    DialogFragment newFragment;
    HashMap<Integer, Uri> uri_hashmap = new HashMap<Integer, Uri>();
    SharedPreferences sharedpreferences;
    private int year;
    private int month;
    private int day;
    String frag_edit_outreach, officer_name, designation, date_of_meeting, venue, target_audience, outreach_id;
    List<OutReachData.Img> image;
    ArrayList<String> deleteimagelist = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_outreachactivity, container, false);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        initView(view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static FragmentAddOutReach newInstance() {
        FragmentAddOutReach fragment = new FragmentAddOutReach();


        return fragment;
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
        officer_name_ed = (AppCompatEditText) view.findViewById(R.id.officer_name_ed);
        designation_ed = (AppCompatEditText) view.findViewById(R.id.designation_ed);
        date_meeting_ed = (EditText) view.findViewById(R.id.date_meeting_ed);
        audience_ed = (AppCompatEditText) view.findViewById(R.id.audience_ed);
        venue_ed = (AppCompatEditText) view.findViewById(R.id.venue_ed);
        btn_choose_iv = (AppCompatButton) view.findViewById(R.id.btn_choose_iv);
        btn_submit = (AppCompatButton) view.findViewById(R.id.btn_submit);
        date_meeting_iv = (AppCompatImageView) view.findViewById(R.id.date_meeting_iv);

        cross_image1 = (ImageView) view.findViewById(R.id.cross_image1);
        cross_image2 = (ImageView) view.findViewById(R.id.cross_image2);
        cross_image3 = (ImageView) view.findViewById(R.id.cross_image3);
        cross_image4 = (ImageView) view.findViewById(R.id.cross_image4);
        cross_image5 = (ImageView) view.findViewById(R.id.cross_image5);

        product_image1 = (ImageView) view.findViewById(R.id.product_image1);
        product_image2 = (ImageView) view.findViewById(R.id.product_image2);
        product_image3 = (ImageView) view.findViewById(R.id.product_image3);
        product_image4 = (ImageView) view.findViewById(R.id.product_image4);
        product_image5 = (ImageView) view.findViewById(R.id.product_image5);

        image1_layout = (RelativeLayout) view.findViewById(R.id.image1_layout);
        image2_layout = (RelativeLayout) view.findViewById(R.id.image2_layout);
        image3_layout = (RelativeLayout) view.findViewById(R.id.image3_layout);
        image4_layout = (RelativeLayout) view.findViewById(R.id.image4_layout);
        image5_layout = (RelativeLayout) view.findViewById(R.id.image5_layout);


        Bundle bundle = getArguments();
        if (bundle != null) {
            frag_edit_outreach = bundle.getString("frag_edit_outreach");
            officer_name = bundle.getString("officer_name");
            designation = bundle.getString("designation");
            date_of_meeting = bundle.getString("date_of_meeting");
            venue = bundle.getString("venue");
            target_audience = bundle.getString("target_audience");
            outreach_id = bundle.getString("outreach_id");
            image = bundle.getParcelableArrayList("image");
        }


        btn_choose_iv.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        date_meeting_iv.setOnClickListener(this);


        if (frag_edit_outreach != null && frag_edit_outreach.equalsIgnoreCase("frag_edit_outreach")) {
            if (officer_name != null && !officer_name.isEmpty()) {
                officer_name_ed.setText(officer_name);
            }
            if (designation != null && !designation.isEmpty()) {
                designation_ed.setText(designation);

            }
            if (date_of_meeting != null && !date_of_meeting.isEmpty()) {
                date_meeting_ed.setText(date_of_meeting);
            }
            if (venue != null && !venue.isEmpty()) {
                venue_ed.setText(venue);
            }
            if (target_audience != null && !target_audience.isEmpty()) {
                audience_ed.setText(target_audience);
            }
            if (image != null && image.size() > 0) {
//                Picasso.with(this)
//                        .load("")
//                        .into(imageView);
                setImage();
            }


        } else {
            if (sharedpreferences.getString("name", "") != null) {
                officer_name_ed.setText(sharedpreferences.getString("name", ""));
            }
            if (sharedpreferences.getString("designation", "") != null) {
                designation_ed.setText(sharedpreferences.getString("designation", ""));
            }
        }


        final Calendar cal = Calendar.getInstance();

        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        setcrossClickListener();
    }

    private void setImage() {

        if (image.size() > 0 && image.get(0).getFile() != null && !image.get(0).getFile().equalsIgnoreCase("")) {
            if (image1_layout.getVisibility() == View.GONE) {
                image1_layout.setVisibility(View.VISIBLE);
                Picasso.with(getActivity())
                        .load(image.get(0).getFile())
                        .into(product_image1);
//
            }
        } else {
            image1_layout.setVisibility(View.GONE);
        }

        if (image.size() > 1 && image.get(1).getFile() != null && !image.get(1).getFile().equalsIgnoreCase("")) {
            if (image2_layout.getVisibility() == View.GONE) {
                image2_layout.setVisibility(View.VISIBLE);
                Picasso.with(getActivity())
                        .load(image.get(1).getFile())
                        .into(product_image2);
            }
        } else {
            image2_layout.setVisibility(View.GONE);
        }
        if (image.size() > 2 && image.get(2).getFile() != null && !image.get(2).getFile().equalsIgnoreCase("")) {
            if (image3_layout.getVisibility() == View.GONE) {
                image3_layout.setVisibility(View.VISIBLE);
                Picasso.with(getActivity())
                        .load(image.get(2).getFile())
                        .into(product_image3);
            }
        } else {
            image3_layout.setVisibility(View.GONE);
        }
        if (image.size() > 3 && image.get(3).getFile() != null && !image.get(3).getFile().equalsIgnoreCase("")) {
            if (image4_layout.getVisibility() == View.GONE) {
                image4_layout.setVisibility(View.VISIBLE);
                Picasso.with(getActivity())
                        .load(image.get(3).getFile())
                        .into(product_image4);
            }
        } else {
            image4_layout.setVisibility(View.GONE);
        }

        if (image.size() > 4 && image.get(4).getFile() != null && !image.get(4).getFile().equalsIgnoreCase("")) {
            if (image5_layout.getVisibility() == View.GONE) {
                image5_layout.setVisibility(View.VISIBLE);
                Picasso.with(getActivity())
                        .load(image.get(4).getFile())
                        .into(product_image5);
            }
        } else {
            image5_layout.setVisibility(View.GONE);
        }


    }

    private void setcrossClickListener() {
        cross_image1.setOnClickListener(this);
        cross_image2.setOnClickListener(this);
        cross_image3.setOnClickListener(this);
        cross_image4.setOnClickListener(this);
        cross_image5.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_choose_iv:
                if (image_file_hashmap != null && image_file_hashmap.size() < 5) {
                    if (!(view.findViewById(R.id.footer_dialog).getVisibility() == View.VISIBLE)) {
                        initFooter(view.findViewById(R.id.footer_dialog), "document");
                    }
                } else {
                    Toast.makeText(getActivity(), "You can upload maximum 5 images", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_submit:
                checkEditText();
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


            case R.id.cross_image1:

                if (image_file_hashmap != null && image_file_hashmap.size() > 0 && image1_layout.getVisibility() == View.VISIBLE) {
                    image_file_hashmap.remove(0);
                    uri_hashmap.remove(0);
                    first_image = 0;
                }

                image1_layout.setVisibility(View.GONE);
                if (image_file_hashmap.size() == 0 || image_file_hashmap.size() == 1) {
                } else {
                }
                if (image != null && image.size() > 0 && image.get(0).getId() != null) {
                    deleteimagelist.add(image.get(0).getId());
                }

                break;
            case R.id.cross_image2:
                if (image_file_hashmap != null && image_file_hashmap.size() > 0 && image2_layout.getVisibility() == View.VISIBLE) {
                    image_file_hashmap.remove(1);
                    uri_hashmap.remove(1);
                    second_image = 0;
                }
                image2_layout.setVisibility(View.GONE);
                if (image_file_hashmap.size() == 0 || image_file_hashmap.size() == 1) {
                } else {
                }

                if (image != null && image.size() > 1 && image.get(1).getId() != null) {
                    deleteimagelist.add(image.get(1).getId());
                }

                break;
            case R.id.cross_image3:

                if (image_file_hashmap != null && image_file_hashmap.size() > 0 && image3_layout.getVisibility() == View.VISIBLE) {
                    image_file_hashmap.remove(2);
                    uri_hashmap.remove(2);
                    third_image = 0;
                }
                image3_layout.setVisibility(View.GONE);
                if (image_file_hashmap.size() == 0 || image_file_hashmap.size() == 1) {

                } else {
                }


                if (image != null && image.size() > 2 && image.get(2).getId() != null) {
                    deleteimagelist.add(image.get(2).getId());
                }
                break;
            case R.id.cross_image4:

                if (image_file_hashmap != null && image_file_hashmap.size() > 0 && image4_layout.getVisibility() == View.VISIBLE) {
                    image_file_hashmap.remove(3);
                    uri_hashmap.remove(3);
                    fourth_image = 0;
                }
                image4_layout.setVisibility(View.GONE);
                if (image_file_hashmap.size() == 0 || image_file_hashmap.size() == 1) {

                } else {
                }

                if (image != null && image.size() > 3 && image.get(3).getId() != null) {
                    deleteimagelist.add(image.get(3).getId());
                }
                break;
            case R.id.cross_image5:

                if (image_file_hashmap != null && image_file_hashmap.size() > 0 && image5_layout.getVisibility() == View.VISIBLE) {
                    image_file_hashmap.remove(4);
                    uri_hashmap.remove(4);
                    fifth_image = 0;
                }
                image5_layout.setVisibility(View.GONE);
                if (image_file_hashmap.size() == 0 || image_file_hashmap.size() == 1) {

                } else {
                }
                if (image != null && image.size() > 4 && image.get(4).getId() != null) {
                    deleteimagelist.add(image.get(4).getId());
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
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (getActivity().checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            ///method to get Images
                            takeimage(type);
                        } else {
                            if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                                Toast.makeText(getActivity(), "Your Permission is needed to get " + "access the camera", Toast.LENGTH_LONG).show();
                            }
                            FragmentAddOutReach.this.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA}, PICK_DOCUMENT_FROM_CAMERA);
                        }
                    } else {
                        takeimage(type);
                    }


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

    // getting the result of camera and gallery on base of request code
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle extras = null;

        if (requestCode == PICK_DOCUMENT_FROM_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    if (data != null) {
                        extras = data.getExtras();

                    }

                    if (imageUri != null) {
                        performCrop(imageUri);
                    }


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


                        if (imageUri != null && image_file_hashmap.size() < 5) {
                            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

                            //
                            if (imageUri.toString().startsWith("file://")) {
                                realPath = imageUri.toString().replace("file://", "");
                            } else {
                                realPath = getImagePath(imageUri);
                            }


                            File f = new File(realPath);
                            Uri contentUri = Uri.fromFile(f);


                            performCrop(contentUri);

                            document_file = new File(realPath);

                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == CROP_FROM_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    imageUri = data.getData();
                    //

                    if (imageUri != null && image_file_hashmap.size() < 5) {
                        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
                        if (imageUri.toString().startsWith("file:")) {
                            realPath = imageUri.toString().replace("file://", "");
                        } else {
                            realPath = getImagePath(imageUri);
                        }
                        document_file = new File(realPath);
                        document_file = Compressor.getDefault(getActivity()).compressToFile(document_file);
                        Log.v("File size in kb after ", String.valueOf(document_file.length() / 1024));


                        if (first_image == 0) {
                            lastmapkey = 0;
                            image_file_hashmap.put(lastmapkey, document_file);
                            uri_hashmap.put(lastmapkey, imageUri);
                            first_image = 1;
                        } else if (second_image == 0) {
                            lastmapkey = 1;
                            image_file_hashmap.put(lastmapkey, document_file);
                            uri_hashmap.put(lastmapkey, imageUri);
                            second_image = 1;
                        } else if (third_image == 0) {
                            lastmapkey = 2;
                            image_file_hashmap.put(lastmapkey, document_file);
                            uri_hashmap.put(lastmapkey, imageUri);
                            third_image = 1;
                            //                            }
                        } else if (fourth_image == 0) {
                            lastmapkey = 3;
                            image_file_hashmap.put(lastmapkey, document_file);
                            uri_hashmap.put(lastmapkey, imageUri);
                            fourth_image = 1;
                            //                            }
                        } else if (fifth_image == 0) {
                            lastmapkey = 4;
                            image_file_hashmap.put(lastmapkey, document_file);
                            uri_hashmap.put(lastmapkey, imageUri);
                            fifth_image = 1;
                            //                            }
                        }


                    }

                    if (imageUri != null) {
                        settingCropedImage(imageUri);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    private void settingCropedImage(Uri imageUri) {


        try {
            if (image1_layout.getVisibility() == View.GONE) {
                image1_layout.setVisibility(View.VISIBLE);
                Bitmap bitmap = null;

                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);

                product_image1.setImageBitmap(bitmap);

            } else if (image2_layout.getVisibility() == View.GONE) {
                image2_layout.setVisibility(View.VISIBLE);
                Bitmap bitmap = null;

                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);

                product_image2.setImageBitmap(bitmap);

            } else if (image3_layout.getVisibility() == View.GONE) {
                image3_layout.setVisibility(View.VISIBLE);
                Bitmap bitmap = null;

                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);

                product_image3.setImageBitmap(bitmap);

            } else if (image4_layout.getVisibility() == View.GONE) {
                image4_layout.setVisibility(View.VISIBLE);
                Bitmap bitmap = null;

                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);

                product_image4.setImageBitmap(bitmap);

            } else if (image5_layout.getVisibility() == View.GONE) {
                image5_layout.setVisibility(View.VISIBLE);
                Bitmap bitmap = null;

                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);

                product_image5.setImageBitmap(bitmap);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void takeimage(String type) {


        // calling the camera activity
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // get current timestamp
        String milliStr = Calendar.getInstance().getTimeInMillis() + "";

        File f = new File(android.os.Environment.getExternalStorageDirectory(), "SWACCH_" + milliStr + "_.jpg");
        imageUri = Uri.fromFile(f);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        try {

            if (type.equalsIgnoreCase("document")) {
                startActivityForResult(intent, PICK_DOCUMENT_FROM_CAMERA);
            }

        } catch (ActivityNotFoundException e) {
        }
    }

    private void performCrop(Uri picUri) {
        try {
            //
            Intent cropIntent;
            //Start Crop Activity
            cropIntent = new Intent("com.android.camera.action.CROP");
            //            cropIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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

        if (officer_name_ed.getText().length() == 0) {
            Toast.makeText(getActivity(), "Please enter name", Toast.LENGTH_SHORT).show();
        } else if (designation_ed.getText().length() == 0) {
            Toast.makeText(getActivity(), "Please enter designation", Toast.LENGTH_SHORT).show();
        } else if (date_meeting_ed.getText().toString().equalsIgnoreCase("Date Of Meeting*")) {
            Toast.makeText(getActivity(), "Please select meeting date", Toast.LENGTH_SHORT).show();
        } else if (venue_ed.getText().length() == 0) {
            Toast.makeText(getActivity(), "Please enter venue details", Toast.LENGTH_SHORT).show();
        } else {
            if (AndroidUtil.isConnectingToInternet(getActivity())) {
                addOutReachServerData();
            } else {
                Toast.makeText(getActivity(), Constant.check_internet_connection, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addOutReachServerData() {
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
        FragmentAddOutReach.IFAQDATA ifaqdata = retrofit.create(FragmentAddOutReach.IFAQDATA.class);
        if (sharedpreferences.getString("User_id", "") != null) {
            user_id = sharedpreferences.getString("User_id", "");
        }

        MultipartBody.Part body1 = null, body2 = null, body3 = null, body4 = null, body5 = null;

        if (image_file_hashmap.size() > 0) {
            File[] filesArray = new File[image_file_hashmap.size()];
            document_list.clear();
            ArrayList<Uri> uriList = new ArrayList<>();

            //                int i = 0;
            for (Map.Entry<Integer, File> map : image_file_hashmap.entrySet()) {
                document_list.add(map.getValue());
            }

            filesArray = document_list.toArray(filesArray);

            try {
                if (filesArray.length > 0) {
                    body1 = prepareFilePart("pic[]", filesArray[0]);
                }
                if (filesArray.length > 1) {
                    body2 = prepareFilePart("pic[]", filesArray[1]);
                }
                if (filesArray.length > 2) {
                    body3 = prepareFilePart("pic[]", filesArray[2]);
                }
                if (filesArray.length > 3) {
                    body4 = prepareFilePart("pic[]", filesArray[3]);
                }
                if (filesArray.length > 4) {
                    body5 = prepareFilePart("pic[]", filesArray[4]);
                }
                //                nameValuePair.put("image[]", filesArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        Call<ResponseBody> call = ifaqdata.getData(user_id, officer_name_ed.getText().toString(), designation_ed.getText().toString(), date_meeting_ed.getText().toString(), venue_ed.getText().toString(), audience_ed.getText().toString(), body1, body2, body3, body4, body5);
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
                            venue_ed.setText("");
                            audience_ed.setText("");
                            date_meeting_ed.setHint("Date of meeting*");
                            getFragmentManager().popBackStack();
//                            if (sharedpreferences.getString("name", "") != null&&sharedpreferences.getString("name", "").equalsIgnoreCase(officer_name_ed.getText().toString()) ) {
//                                officer_name_ed.setText(sharedpreferences.getString("name", ""));
//                            }else{
//                                officer_name_ed.setText("");
//                            }
//                            if (sharedpreferences.getString("designation", "") != null) {
//                                designation_ed.setText(sharedpreferences.getString("designation", ""));
//                            }
                        } else {
                            Toast.makeText(getActivity(), jsonObject.getString("tag"), Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
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

    }

    public interface IFAQDATA {
        @Multipart
        @POST(Constant.WebUrl.CREATE_OUTREACH)
        Call<ResponseBody> getData(@Query("id") String user_id, @Query("officer_name") String officer_name, @Query("designation") String designation, @Query("date") String date, @Query("venue") String venue, @Query("target_group") String target_group, @Part MultipartBody.Part file, @Part MultipartBody.Part file1, @Part MultipartBody.Part file2, @Part MultipartBody.Part file3, @Part MultipartBody.Part file4);
    }


    @NonNull
    public static MultipartBody.Part prepareFilePart(String partName, File file) {


        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse(Constant.MULTIPART_FILE_FORM_DATA), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
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
}