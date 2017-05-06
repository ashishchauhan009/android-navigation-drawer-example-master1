package net.simplifiedcoding.navigationdrawerexample.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.simplifiedcoding.navigationdrawerexample.Model.FaqCommon;
import net.simplifiedcoding.navigationdrawerexample.Model.FaqModel;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.fragments.FragmentFAQs_PMGKY;

import java.util.ArrayList;

/**
 * Created by vibes on 6/3/17.
 */

public class AdapterFaqPmgky extends RecyclerView.Adapter<AdapterFaqPmgky.MyViewHolder> {

    private Context context;
    private ArrayList<FaqCommon> faqModelsList;


    public AdapterFaqPmgky(Context context, FaqModel data) {
        this.context = context;
        if (data != null) {
            faqModelsList = data.getFaq();
           // Log.e("check data on adapter",faqModelsList.toString());
        }
    }

    @Override
    public AdapterFaqPmgky.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_faq_list, parent, false);
        AdapterFaqPmgky.MyViewHolder holder = new AdapterFaqPmgky.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AdapterFaqPmgky.MyViewHolder holder, int position) {

        holder.tvQuestion.setText(faqModelsList.get(position).getQuestion());
        holder.tvAnswer.setText(faqModelsList.get(position).getAnswer());
    }

    @Override
    public int getItemCount() {
        return faqModelsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayoutCompat linearLayoutClickArea;
        TextView tvQuestion,
                tvAnswer;


        public MyViewHolder(View v) {
            super(v);

            tvQuestion = (TextView) v.findViewById(R.id.tv_question);
            tvAnswer = (TextView) v.findViewById(R.id.tv_answer);

        }
    }

}

