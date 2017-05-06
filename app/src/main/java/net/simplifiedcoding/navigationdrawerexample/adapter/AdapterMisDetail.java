package net.simplifiedcoding.navigationdrawerexample.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.simplifiedcoding.navigationdrawerexample.Model.Datum;
import net.simplifiedcoding.navigationdrawerexample.Model.FaqCommon;
import net.simplifiedcoding.navigationdrawerexample.Model.NewTaskData;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.util.AndroidUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vibes on 6/3/17.
 */
/*
public class AdapterMisDetail extends RecyclerView.Adapter<AdapterMisDetail.MyViewHolder> {

    private Context context;
    private ArrayList<FaqCommon> faqModelsList;
    List<Datum> data;
    String status;
    private static ClickListener clickListener;


    public AdapterMisDetail(Context context, List<Datum> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mis_detail_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (data.get(position).getName() != null && !data.get(position).getName().isEmpty()) {
            holder.mis_name_tv.setText(data.get(position).getName());
        }
        if (data.get(position).getVal() != null && !data.get(position).getVal().isEmpty()) {
            String value = AndroidUtil.formatDoubleData(Double.valueOf(data.get(position).getVal()));
            holder.mis_value_tv.setText(value);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        AdapterMisDetail.clickListener = clickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mis_name_tv, mis_value_tv;


        public MyViewHolder(View v) {
            super(v);
            mis_name_tv = (TextView) v.findViewById(R.id.mis_name_tv);
            mis_value_tv = (TextView) v.findViewById(R.id.mis_value_tv);


        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view);
        }
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

}*/

