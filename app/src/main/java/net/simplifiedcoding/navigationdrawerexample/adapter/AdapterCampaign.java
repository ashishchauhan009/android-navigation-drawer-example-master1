package net.simplifiedcoding.navigationdrawerexample.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.simplifiedcoding.navigationdrawerexample.Model.CampaignCommon;
import net.simplifiedcoding.navigationdrawerexample.Model.CampaignModel;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.fragments.FragmentCampaign;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by vibes on 6/3/17.
 */

public class AdapterCampaign extends RecyclerView.Adapter<AdapterCampaign.MyViewHolder> {

    private Context context;
    private FragmentCampaign fragmentCampaignActivities;
    private ArrayList<CampaignCommon> campaignModelsList;


    public AdapterCampaign(Context context, CampaignModel data, FragmentCampaign fragmentCampaignActivities) {
        this.context = context;
        this.fragmentCampaignActivities = fragmentCampaignActivities;
        if (data != null) {
            campaignModelsList = data.getData();
           Log.e("check data on adapter",campaignModelsList.toString());
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_campaign_list, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if (android.os.Build.VERSION.SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        URL imageUrl = null;
        Bitmap imageBitmap = null;
        try {

            String campaignImageUrl = campaignModelsList.get(position).getImage();

            imageUrl = new URL(campaignImageUrl);
            imageBitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        holder.campaignImage.setImageBitmap(imageBitmap);
        holder.campaignImage.setAdjustViewBounds(true);
        holder.bodyText.setText(campaignModelsList.get(position).getTitle());
        holder.dateText.setText(campaignModelsList.get(position).getCreated());
    }

    @Override
    public int getItemCount() {
        return campaignModelsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayoutCompat linearLayoutClickArea;
        AppCompatImageView campaignImage;
        AppCompatTextView bodyText,
                dateText,
        titleText;


        public MyViewHolder(View v) {
            super(v);

            campaignImage = (AppCompatImageView) v.findViewById(R.id.campaignImage);
            bodyText = (AppCompatTextView) v.findViewById(R.id.bodyText);
            dateText = (AppCompatTextView) v.findViewById(R.id.dateText);
            titleText = (AppCompatTextView) v.findViewById(R.id.titleText);

        }
    }

}

