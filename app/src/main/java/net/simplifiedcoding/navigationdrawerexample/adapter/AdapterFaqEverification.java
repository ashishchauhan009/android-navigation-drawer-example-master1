package net.simplifiedcoding.navigationdrawerexample.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.simplifiedcoding.navigationdrawerexample.Model.FaqCommon;
import net.simplifiedcoding.navigationdrawerexample.Model.FaqModel;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.fragments.FragmentFAQs_EVerification;
import net.simplifiedcoding.navigationdrawerexample.fragments.FragmentFAQs_PMGKY;

import java.util.ArrayList;

/**
 * Created by vibes on 6/3/17.
 */

public class AdapterFaqEverification extends RecyclerView.Adapter<AdapterFaqEverification.MyViewHolder> {

    private Context context;
    private FragmentFAQs_EVerification fragmentFAQspmgky;
    private ArrayList<FaqCommon> faqModelsList;


    public AdapterFaqEverification(Context context, FaqModel data, FragmentFAQs_EVerification fragmentFAQsPmgky) {
        this.context = context;
        this.fragmentFAQspmgky = fragmentFAQsPmgky;
        if (data != null) {
            faqModelsList = data.getFaq();
            //Log.e("chek  adapetr evar",faqModelsList.toString());
        }
    }
    @Override
    public AdapterFaqEverification.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_faq_list, parent, false);
        AdapterFaqEverification.MyViewHolder holder = new AdapterFaqEverification.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AdapterFaqEverification.MyViewHolder holder, int position) {

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
           // linearLayoutClickArea = (LinearLayoutCompat) v.findViewById(R.id.ll_clickable);
            tvQuestion = (TextView) v.findViewById(R.id.tv_question);
            tvAnswer = (TextView) v.findViewById(R.id.tv_answer);

        }
    }

}

