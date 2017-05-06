package net.simplifiedcoding.navigationdrawerexample.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.simplifiedcoding.navigationdrawerexample.Model.DataMis;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.util.AndroidUtil;

import java.util.ArrayList;

public class AdapterDialogMis extends RecyclerView.Adapter<AdapterDialogMis.MyViewHolder> {

    private Context context;
    private ArrayList<DataMis> misArrayList;


    public AdapterDialogMis(Context context, ArrayList<DataMis> misArrayList) {
        this.context = context;
        this.misArrayList = misArrayList;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_mis_name, tv_mis_value;

        public MyViewHolder(View v) {
            super(v);
            tv_mis_name = (TextView) v.findViewById(R.id.tv_mis_name);
            tv_mis_value = (TextView) v.findViewById(R.id.tv_mis_value);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_mis_dialog, parent, false);
        return new AdapterDialogMis.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DataMis showdataMis = misArrayList.get(position);

        holder.tv_mis_name.setText(showdataMis.getName());
        holder.tv_mis_value.setText(AndroidUtil.formatDoubleData(Double.valueOf(showdataMis.getVal())));
    }

    @Override
    public int getItemCount() {
        return misArrayList.size();
    }


}

