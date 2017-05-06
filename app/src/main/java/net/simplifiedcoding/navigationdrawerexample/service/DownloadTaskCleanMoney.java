package net.simplifiedcoding.navigationdrawerexample.service;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.util.Config;
import net.simplifiedcoding.navigationdrawerexample.util.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * Created by Vibes on 14-/04/17.
 */

//Added by Mahendra 14-04-2017
public class DownloadTaskCleanMoney {

    private static final String TAG = "Download Task";
    private Context context;
    private String downloadUrl = "";
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotifyManager;
    private String title = "";
    private int id;


    public DownloadTaskCleanMoney(Context context, String downloadUrl) {
        this.context = context;
        this.downloadUrl = downloadUrl;
        new DownloadingTask().execute();
    }

    private class DownloadingTask extends AsyncTask<String, Integer, String> {
        //        File apkStorage = null;
        File outputFile = null;
        long FileSize;

        private NotificationCompat.Builder mBuilder;
        private NotificationManager mNotifyManager;
        private final String mTitle = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e(TAG, "Download PreExecutes");
        }

        @Override
        protected String doInBackground(String... arg0) {
            try {
                URL url = new URL(downloadUrl);//Create Download URl
                System.out.println("download url is :-----   " + url.toString());
                HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                c.connect();//connect the URL Connection
                //If Connection response is not OK then show Logs
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.d(TAG, "Server returned HTTP " + "" + c.getResponseCode()
                            + " " + c.getResponseMessage());
                }
                FileSize = c.getContentLength();
                System.out.println("Download FileSize" + FileSize);
                try {
                    String extr = Environment.getExternalStorageDirectory().toString();
                    File apkStorage = new File(extr + File.separator + Utils.downloadDirectory + File.separator + "Downloads");
                    System.out.println("file path is:-" + apkStorage.toString());
                    if (!apkStorage.exists()) {
                        apkStorage.mkdirs();
                        Log.d(TAG, "Directory Created.");
                    }
                    String fileExtention = downloadUrl.substring(downloadUrl.lastIndexOf(".") - 1, downloadUrl.length());
                    outputFile = new File(apkStorage + File.separator + "CM" + String.valueOf(new Date().getTime()) + fileExtention);
                    //Create Output file in Main File
                    if (!outputFile.exists()) {
                        outputFile.createNewFile();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location
                InputStream is = c.getInputStream();//Get InputStream for connection
                byte[] buffer = new byte[1024];//Set buffer type
                int totalRead = 0;
                try {
                    int len1 = 0;//init length
                    int bytesRead = 0;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        totalRead = totalRead + bytesRead;
                        publishProgress((int) ((totalRead * 100) / FileSize));
                        fos.write(buffer, 0, bytesRead);
                        Log.d(TAG, "progress " + "" + bytesRead);
                    }
                    fos.flush();
                    fos.close();
                    is.close();
                } catch (Exception e) {
                    Log.d("progress", "" + e.getMessage());
                    fos.close();
                    e.printStackTrace();
                    buffer = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                outputFile = null;
                Log.e(TAG, "Download Error Exception " + "" + e.getMessage());
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d("Download:-", values[0] + "");
            Intent pushNotification = new Intent(Config.NOTIFY_UPDATE_PROGRESS_MISSING_PERSON);
            pushNotification.putExtra("Progress", values[0]);
            pushNotification.putExtra("file_path", outputFile.getPath());
            LocalBroadcastManager.getInstance(context).sendBroadcast(pushNotification);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (outputFile != null) {
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                        }
                    }, 3000);
                    Log.e(TAG, "Download Failed");
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    }
                }, 3000);
                Log.e(TAG, "Download Failed with Exception - " + "" + e.getLocalizedMessage());
            }
            super.onPostExecute(result);
        }
    }
}