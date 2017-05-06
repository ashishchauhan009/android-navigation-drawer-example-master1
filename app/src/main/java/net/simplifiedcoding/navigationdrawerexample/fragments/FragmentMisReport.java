package net.simplifiedcoding.navigationdrawerexample.fragments;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.simplifiedcoding.navigationdrawerexample.Constant.Constant;
import net.simplifiedcoding.navigationdrawerexample.Model.VisionModel;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.activities.MainActivity;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

import static android.content.Context.DOWNLOAD_SERVICE;


public class FragmentMisReport extends Fragment implements Callback<VisionModel> {



    private TextView tvTitleName,tvTittle;
    private ProgressDialog pDialog;
    private LinearLayout llMisReport;
    private VisionModel visionModel;
    private String tittlename;
    private ImageView ivMisreport;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public FragmentMisReport() {
        // Required empty public constructor
    }


    public static FragmentMisReport newInstance() {
        FragmentMisReport fragment = new FragmentMisReport();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_misreport, container, false);
        initView(rootView);
        return rootView;
    }


    void initView(View view) {

        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedpreferences.edit();
        tittlename = sharedpreferences.getString("TittleName", "").toString();
       // Log.e("check shared preference", sharedpreferences.getString("TittleName", "").toString());
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
        tvTittle = (TextView) view.findViewById(R.id.tv_tittle);
        ivMisreport = (ImageView) view.findViewById(R.id.iv_misreport);
        llMisReport = (LinearLayout) view.findViewById(R.id.ll_misreports);
        tvTitleName.setText("MIS REPORT");


        getDataFronServer();

    }

    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main_drawer, menu); // removed to not double the menu items

    }


    @Override
    public void onDetach() {
        super.onDetach();

    }

    void setAllVisionData() {
         if(tittlename!=null&&!tittlename.equals("")) {
             tvTittle.setText(tittlename);
         }
      if (visionModel.getMis() != null && visionModel.getMis()!=null) {
          /*    if (android.os.Build.VERSION.SDK_INT > 8)
            {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            URL imageUrl = null;
            Bitmap imageBitmap = null;
            try {

                String campaignImageUrl = visionModel.getMis().get(0).getImage();

                imageUrl = new URL(campaignImageUrl);
                imageBitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
            }catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            ivMisreport.setImageBitmap(imageBitmap);
            ivMisreport.setAdjustViewBounds(true);*/
          if(visionModel.getMis().get(0).getImage()!=""||visionModel.getMis().get(0).getImage()!=null)
            Picasso.with(this.getContext())
                    .load(visionModel.getMis().get(0).getImage())
                    .placeholder(R.drawable.ic_no_image)
                    .resize(0, 500)
       .into(ivMisreport);
        }
        LayoutInflater layoutInflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (visionModel != null||visionModel.getMis()!=null) {

            for (int i = 0; i < visionModel.getMis().size(); i++) {
                final int pos = i;
                View view = layoutInflater.inflate(R.layout.layout_mis_reports, llMisReport, false);
                if(visionModel.getMis().get(i).getFile().length()>0&&!visionModel.getMis().get(i).getFile().equals("")) {
                    ((TextView) view.findViewById(R.id.tv_misreports_dinamic)).setText(visionModel.getMis().get(i).getTitle());
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(visionModel.getMis().get(pos).getFile()));
                            getContext().startActivity(browserIntent);
                            /*Bundle bundle = new Bundle();
                            bundle.putString("link", visionModel.getMis().get(pos).getFile());
                            Fragment fragment = new FragmentMisReportWebView();
                            fragment.setArguments(bundle);
                            ((MainActivity) getContext()).replacefragment(fragment);*//*
                            String url = visionModel.getMis().get(pos).getFile();
                            String[] urlArray = url.split("/");
                            String pdfFileName = urlArray[urlArray.length-1];
                            Uri downloadUrl = Uri.parse(url);

                            long downloadReference;
                            // Create request for android download manager


                            DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(DOWNLOAD_SERVICE);
                            DownloadManager.Request request = new DownloadManager.Request(downloadUrl);

                            //Setting title of request
                            request.setTitle(pdfFileName);

                            //Setting description of request
                            request.setDescription(pdfFileName);
                           *//* File file = new File(Environment.getExternalStorageDirectory()+ "/CleanMoney/" + pdfFileName);
                            Intent intent = new Intent(Intent.ACTION_VIEW,downloadUrl);
                            intent.setDataAndType(Uri.fromFile(file),"application/vnd.ms-excel");*//*
                            File dir =  new File(Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DOWNLOADS + File.separator + "CleanMoney" + File.separator );
                            if(!dir.exists()){
                                dir.mkdir();
                            }
                            String filepath = File.separator + "CleanMoney" + File.separator + pdfFileName;
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filepath);
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            *//*File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
                            if(!file.exists()){
                                file.mkdir();
                            }

                            request.setDestinationInExternalFilesDir(v.getContext(),Environment.getExternalStorageDirectory().getAbsolutePath(), pdfFileName);
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);*//*

                            //Enqueue download and save into referenceId
                            downloadReference = downloadManager.enqueue(request);*/

                        }
                    });
                    llMisReport.addView(view);
                }
            }
        }
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    void getDataFronServer() {
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
        IVISIONDATA ivisiondata = retrofit.create(IVISIONDATA.class);
        Call<VisionModel> call = ivisiondata.getData();
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<VisionModel> call, Response<VisionModel> response) {
        visionModel = response.body();

        if (visionModel != null) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (Integer.parseInt(visionModel.getStatus()) == Constant.StatusCode.CLEANMONEY_CODE_SUCCESS) {
                setAllVisionData();
                Log.e("mis data",visionModel.toString());
            }else{
                showMessage(getView(),"Something went wrong...");
            }
        }
    }

    @Override
    public void onFailure(Call<VisionModel> call, Throwable t) {

    }

    public interface IVISIONDATA {
        @GET(Constant.WebUrl.MIS)
        Call<VisionModel> getData();
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
