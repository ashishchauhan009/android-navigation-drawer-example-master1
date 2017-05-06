package net.simplifiedcoding.navigationdrawerexample.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.simplifiedcoding.navigationdrawerexample.Model.Contribute;
import net.simplifiedcoding.navigationdrawerexample.Model.Contribution;
import net.simplifiedcoding.navigationdrawerexample.R;

import java.util.ArrayList;

public class AdapterContribute extends RecyclerView.Adapter<AdapterContribute.MyViewHolder> {

    private Context context;
    private ArrayList<Contribute> contributions;


    public AdapterContribute(Context context, ArrayList<Contribute> contributions) {
        this.context = context;
        this.contributions = contributions;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_state, tv_contribution;

        public MyViewHolder(View v) {
            super(v);
            tv_state = (TextView) v.findViewById(R.id.tv_state);
            tv_contribution = (TextView) v.findViewById(R.id.tv_contribution);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_contribution, parent, false);
        return new AdapterContribute.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Contribute contribution = contributions.get(position);


            holder.tv_contribution.setText(contribution.getObjective());
            holder.tv_state.setText(contribution.getState());

    }

    @Override
    public int getItemCount() {
        return contributions.size();
    }


}

