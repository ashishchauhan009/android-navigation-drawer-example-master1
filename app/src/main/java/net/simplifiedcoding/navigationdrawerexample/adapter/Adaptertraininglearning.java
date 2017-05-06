package net.simplifiedcoding.navigationdrawerexample.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class Adaptertraininglearning extends RecyclerView.Adapter<Adaptertraininglearning.MyViewHolder> {

    private Context context;
    private ArrayList<Vision> trainingList = new ArrayList<>();


    public Adaptertraininglearning(Context context, VisionModel data) {
        this.context = context;
        if(data!=null){
            trainingList = data.getData();
        }
    }




    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_trainglearing_list, parent, false);
        Adaptertraininglearning.MyViewHolder holder = new Adaptertraininglearning.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,final int position) {
        holder.tvTitle.setText(trainingList.get(position).getTitle());
        holder.tvLink.setText(trainingList.get(position).getField_url());
        holder.tvCount.setText(position+1+"");
        holder.linearLayoutClickArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(trainingList.get(position).getField_url()));
                context.startActivity(browserIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return trainingList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayoutCompat linearLayoutClickArea;
        TextView tvCount,
                tvTitle,
                tvLink;
         ImageView  tvImage;

        public MyViewHolder(View v) {
            super(v);
            linearLayoutClickArea = (LinearLayoutCompat) v.findViewById(R.id.ll_clickable);
            tvCount = (TextView) v.findViewById(R.id.tv_count);
            tvTitle = (TextView) v.findViewById(R.id.tv_title);
            tvLink = (TextView) v.findViewById(R.id.tv_link);
            tvImage = (ImageView) v.findViewById(R.id.iv_image);
            //linearLayoutClickArea.setOnClickListener();
        }
    }
}
