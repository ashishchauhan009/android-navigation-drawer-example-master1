package net.simplifiedcoding.navigationdrawerexample.adapter;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.simplifiedcoding.navigationdrawerexample.Model.DownloadCommon;
import net.simplifiedcoding.navigationdrawerexample.Model.DownloadModel;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.activities.MainActivity;
import net.simplifiedcoding.navigationdrawerexample.fragments.FragmentDownload;

import java.io.File;
import java.util.ArrayList;

import static android.R.attr.onClick;
import static android.content.Context.DOWNLOAD_SERVICE;
import android.content.Context;

/**
 * Created by vibes on 6/3/17.
 */

public class AdapterDownload extends RecyclerView.Adapter<AdapterDownload.MyViewHolder> {

    private Context context;
    private FragmentDownload fragmentDownload;
    private ArrayList<DownloadCommon> downloadModelsList;


    public AdapterDownload(Context context, DownloadModel data, FragmentDownload fragmentDownload) {
        this.context = context;
        this.fragmentDownload = fragmentDownload;
        if (data != null) {
            downloadModelsList = data.getData();
           //Log.e("check data on adapter",downloadModelsList.toString());
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_download_list, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        if (android.os.Build.VERSION.SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
            //Log.e("INFO","PDF URL:" + downloadModelsList.get(position).getField_url());


        holder.bodyText.setText(downloadModelsList.get(position).getBodyText());
        holder.titleText.setText(downloadModelsList.get(position).getTitle());

        holder.bodyText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // do something here.
                String url = downloadModelsList.get(position).getField_url();
                String[] urlArray = url.split("/");
                String pdfFileName = urlArray[urlArray.length-1];
                Uri downloadUrl = Uri.parse(url);

                long downloadReference;
                // Create request for android download manager

                DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(downloadUrl);

                //Setting title of request
                request.setTitle(pdfFileName);

                //Setting description of request
                request.setDescription(pdfFileName);

                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/CleanMoney");
                if(!file.exists()){
                    file.mkdir();
                }
                request.setDestinationInExternalFilesDir(v.getContext(),Environment.getExternalStorageDirectory().getAbsolutePath()+"/CleanMoney", pdfFileName);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                //Enqueue download and save into referenceId
                downloadReference = downloadManager.enqueue(request);
            }
        });
    }

    @Override
    public int getItemCount() {
        return downloadModelsList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayoutCompat linearLayoutClickArea;
        AppCompatTextView bodyText,
        titleText;

        public MyViewHolder(View v) {
            super(v);

            bodyText = (AppCompatTextView) v.findViewById(R.id.bodyText);
            titleText = (AppCompatTextView) v.findViewById(R.id.titleText);

        }
    }
}

