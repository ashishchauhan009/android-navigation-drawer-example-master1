package net.simplifiedcoding.navigationdrawerexample.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.simplifiedcoding.navigationdrawerexample.Model.CampaignCommon;
import net.simplifiedcoding.navigationdrawerexample.Model.CampaignModel;
import net.simplifiedcoding.navigationdrawerexample.Model.TrainingLearning;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.activities.MainActivity;
import net.simplifiedcoding.navigationdrawerexample.adapter.Adaptertraininglearning;
import net.simplifiedcoding.navigationdrawerexample.fragments.FragmentNewsDetails;
import net.simplifiedcoding.navigationdrawerexample.fragments.FragmentNewsListing;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by vibes on 28/2/17.
 */
public class AdapterNewsListing extends RecyclerView.Adapter<AdapterNewsListing.MyViewHolder> implements View.OnClickListener{


    Context context;

    private ArrayList<CampaignCommon> latestNewsList = new ArrayList<>();
    private ArrayList<CampaignCommon> arraylist = new ArrayList<>();

    public AdapterNewsListing(Context context, CampaignModel data) {
      this.context = context;
        latestNewsList = data.getData();
        arraylist = latestNewsList;
    }


    @Override
    public AdapterNewsListing.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_newslisting_list, parent, false);
        AdapterNewsListing.MyViewHolder holder = new AdapterNewsListing.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvTitleNews.setText(latestNewsList.get(position).getTitle());
        if (latestNewsList.get(position) != null && latestNewsList.get(position).getImage().length() != 0) {
            Picasso.with(context).load(latestNewsList.get(position).getImage()).noFade().placeholder(R.drawable.ic_no_image).resize(100, 0).into(holder.ivNews);
        } else {
            Picasso.with(context).load(latestNewsList.get(position).getImage()).noFade().placeholder(R.drawable.ic_no_image).resize(100, 0).into(holder.ivNews);
        }

    }

    @Override
    public int getItemCount() {
        return latestNewsList.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_clickable:
                Fragment fragment = new FragmentNewsDetails();
                ((MainActivity) context).replacefragment(fragment);
        }

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayoutClickArea;
        TextView tvTitleNews;
        ImageView ivNews;


        public MyViewHolder(View v) {
            super(v);
            linearLayoutClickArea = (LinearLayout) v.findViewById(R.id.ll_clickable);
            linearLayoutClickArea.setOnClickListener(AdapterNewsListing.this);
            tvTitleNews = (TextView) v.findViewById(R.id.tv_titlenews);
            ivNews = (ImageView) v.findViewById(R.id.iv_news);
        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        latestNewsList.clear();
        if (charText.length() == 0) {
            latestNewsList.addAll(arraylist);
        } else {
            for (CampaignCommon wp : arraylist) {
                if (wp.getTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    latestNewsList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
