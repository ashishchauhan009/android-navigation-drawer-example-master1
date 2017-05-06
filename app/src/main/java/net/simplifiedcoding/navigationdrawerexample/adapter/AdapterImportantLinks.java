package net.simplifiedcoding.navigationdrawerexample.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.simplifiedcoding.navigationdrawerexample.Model.TrainingLearning;
import net.simplifiedcoding.navigationdrawerexample.Model.Vision;
import net.simplifiedcoding.navigationdrawerexample.Model.VisionModel;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.fragments.FragmentTraningLearning;

import java.util.ArrayList;

/**
 * Created by vibes on 28/2/17.
 */

public class AdapterImportantLinks extends RecyclerView.Adapter<AdapterImportantLinks.MyViewHolder> {

    private Context context;
    private ArrayList<Vision> impLinksList = new ArrayList<>();

    public AdapterImportantLinks(Context context, VisionModel data) {
        this.context = context;
        if(data!=null)
        impLinksList = data.getData();
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_importantlinks_list, parent, false);
        AdapterImportantLinks.MyViewHolder holder = new AdapterImportantLinks.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,final int position) {
        holder.tvTitle.setText(impLinksList.get(position).getBody());
        holder.linearLayoutClickArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(impLinksList.get(position).getField_url()));
                context.startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return impLinksList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayoutCompat linearLayoutClickArea;
        TextView tvTitle;
       // ImageView ivLinks;


        public MyViewHolder(View v) {
            super(v);
            linearLayoutClickArea = (LinearLayoutCompat) v.findViewById(R.id.ll_clickable);
            tvTitle = (TextView) v.findViewById(R.id.tv_title);
          //ivLinks = (ImageView) v.findViewById(R.id.iv_links);
        }
    }
}
