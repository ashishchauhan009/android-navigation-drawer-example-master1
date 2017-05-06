package net.simplifiedcoding.navigationdrawerexample.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.simplifiedcoding.navigationdrawerexample.Model.Datum;
import net.simplifiedcoding.navigationdrawerexample.Model.MisLevel;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.activities.MainActivity;
import net.simplifiedcoding.navigationdrawerexample.fragments.FragmentNewsDetails;

import java.util.List;

/**
 * Created by vibes on 24/3/17.
 */

public class AdapterImageEngage extends RecyclerView.Adapter<AdapterImageEngage.MyViewHolder> implements View.OnClickListener{


    Context context;
    List<MisLevel.Datum> data;



    public AdapterImageEngage(Context context, List<MisLevel.Datum> data) {
        this.context = context;
        this.data = data;

    }


    @Override
    public AdapterImageEngage.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_gridview, parent, false);
        AdapterImageEngage.MyViewHolder holder = new AdapterImageEngage.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AdapterImageEngage.MyViewHolder holder, int position) {
        holder.grid_text.setText(data.get(position).getName());
        if (data.get(position) != null && data.get(position).getPic().length() != 0) {
            Picasso.with(context).load(data.get(position).getPic()).noFade().placeholder(R.drawable.ic_no_image).resize(100, 0).into(holder.grid_image);
        } else {
            Picasso.with(context).load(data.get(position).getPic()).noFade().placeholder(R.drawable.ic_no_image).resize(100, 0).into(holder.grid_image);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
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
        TextView grid_text;
        ImageView grid_image;


        public MyViewHolder(View v) {
            super(v);
            linearLayoutClickArea = (LinearLayout) v.findViewById(R.id.ll_clickable);
           // linearLayoutClickArea.setOnClickListener(AdapterImageEngage.this);
            grid_text = (TextView) v.findViewById(R.id.grid_text);
            grid_image = (ImageView) v.findViewById(R.id.grid_image);
        }
    }

}

