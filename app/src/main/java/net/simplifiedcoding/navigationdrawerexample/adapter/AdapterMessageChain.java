package net.simplifiedcoding.navigationdrawerexample.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.simplifiedcoding.navigationdrawerexample.Model.FaqCommon;
import net.simplifiedcoding.navigationdrawerexample.Model.NewTaskData;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.util.AndroidUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vibes on 6/3/17.
 */

public class AdapterNewTask extends RecyclerView.Adapter<AdapterNewTask.MyViewHolder> {

    private Context context;
    private ArrayList<FaqCommon> faqModelsList;
    List<NewTaskData.Datum> data;
    String status;
    private static ClickListener clickListener;


    //    public AdapterNewTask(Context context, FaqModel data) {
//        this.context = context;
//        if (data != null) {
//            faqModelsList = data.getFaq();
//           // Log.e("check data on adapter",faqModelsList.toString());
//        }
//    }
    public AdapterNewTask(Context context, List<NewTaskData.Datum> data, String status) {
        this.context = context;
        this.data = data;
        this.status = status;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.newtask_fragment, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (data.get(position).getAssign_to() != null && !data.get(position).getAssign_to().isEmpty() && data.get(position).getAssign_to_designation() != null && !data.get(position).getAssign_to_designation().isEmpty()) {
            holder.name_tv.setText(data.get(position).getAssign_to() + "-" + data.get(position).getAssign_to_designation());
        }/*else {
            holder.ll_subject.setVisibility(View.GONE);
        }*/
        if (data.get(position).getTask_completion_date() != null && !data.get(position).getTask_completion_date().isEmpty()) {
            holder.date_tv.setText(AndroidUtil.formatMMDate(data.get(position).getTask_completion_date(), "dd-MM-yyyy", "MMM dd"));
        }
        if (data.get(position).getTaskSubject() != null && !data.get(position).getTaskSubject().isEmpty()) {
            holder.subject_tv.setText(data.get(position).getTaskSubject());
        }/*else {
            holder.rl_name.setVisibility(View.GONE);
        }*/
//
//        if (status.equalsIgnoreCase("open")||status.equalsIgnoreCase("pending")||status.equalsIgnoreCase("wip")) {
//            holder.ll_assignedto.setVisibility(View.GONE);
//            holder.ll_taskcomon.setVisibility(View.GONE);
//        } else {
//            holder.ll_assignedto.setVisibility(View.VISIBLE);
//            holder.ll_taskcomon.setVisibility(View.VISIBLE);
//            if (data.get(position).getAssign_to() != null && !data.get(position).getAssign_to().isEmpty()) {
//                holder.assigned_by_name_tv.setText(data.get(position).getAssign_to());
//            }
//        }
//        if(status.equalsIgnoreCase("newtaskAssign")){
//            holder.ll_taskcomon.setVisibility(View.GONE);
//        }

        //if (status.equalsIgnoreCase("task_assign_date")) {
        //  holder.ll_taskcomdate.setVisibility(View.VISIBLE);
        // holder.task_comp_date_tv.setText(AndroidUtil.formatDate(data.get(position).getTask_completion_date(), "yyyy-dd-MM", "MM-dd-yyyy"));
//        if (data.get(position).getTask_completion_date() != null && !data.get(position).getTask_completion_date().isEmpty()) {
//            holder.task_comp_date_tv.setText(" " + data.get(position).getTask_completion_date());
//        }
//        if (data.get(position).getTask_completed_on() != null && !data.get(position).getTask_completed_on().isEmpty()) {
//            holder.task_compon_date_tv.setText(" " + data.get(position).getTask_completed_on());
//        }
//       // } else {
//          //  holder.ll_taskcomdate.setVisibility(View.GONE);
//       // }
//        holder.status_ll.setVisibility(View.VISIBLE);
//        if (data.get(position).getTaskStatus() != null && !data.get(position).getTaskStatus().isEmpty()) {
//            holder.status_tv.setText(" " + data.get(position).getTaskStatus());
//        }
//        if (data.get(position).getTaskId() != null && !data.get(position).getTaskId().isEmpty()) {
//            holder.count_tv.setText(" " + data.get(position).getTaskId());
//        }
//        if (data.get(position).getCreatedOn() != null && !data.get(position).getCreatedOn().isEmpty()) {
//            holder.assigned_date_tv.setText(" " + data.get(position).getCreatedOn());
//        }
//        if (data.get(position).getCreatedBy() != null && !data.get(position).getCreatedBy().isEmpty()) {
//            holder.assigned_name_tv.setText(" " + data.get(position).getCreatedBy());
//        }
//        if (data.get(position).getTaskSubject() != null && !data.get(position).getTaskSubject().isEmpty()) {
//            holder.subject_tv.setText(" " + data.get(position).getTaskSubject());
//        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        AdapterNewTask.clickListener = clickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView count_tv, name_tv,
                assigned_date_tv, assigned_name_tv, subject_tv, status_tv,
                task_comp_date_tv, date_tv, task_comp_tv, assigned_by_name_tv, assigned_by_tv, task_compon_date_tv;
        LinearLayout status_ll, ll_assignedto, ll_taskcomdate, ll_taskcomon,ll_subject;
        RelativeLayout rl_name;


        public MyViewHolder(View v) {
            super(v);

//            count_tv = (TextView) v.findViewById(R.id.count_tv);
//            assigned_date_tv = (TextView) v.findViewById(R.id.assigned_date_tv);
//            assigned_name_tv = (TextView) v.findViewById(R.id.assigned_name_tv);
//            subject_tv = (TextView) v.findViewById(R.id.subject_tv);
//            status_ll = (LinearLayout) v.findViewById(R.id.status_ll);
//            ll_assignedto = (LinearLayout) v.findViewById(R.id.ll_assignedto);
//            ll_taskcomdate = (LinearLayout) v.findViewById(R.id.ll_taskcomdate);
//            ll_taskcomon = (LinearLayout) v.findViewById(R.id.ll_taskcomon);
//            status_tv = (TextView) v.findViewById(R.id.status_tv);
//            assigned_by_name_tv = (TextView) v.findViewById(R.id.assigned_by_name_tv);
//            assigned_by_tv = (TextView) v.findViewById(R.id.assigned_by_tv);
//            task_comp_date_tv = (TextView) v.findViewById(R.id.task_comp_date_tv);
//            task_comp_tv = (TextView) v.findViewById(R.id.task_comp_tv);
//            task_compon_date_tv = (TextView) v.findViewById(R.id.task_compon_date_tv);
            name_tv = (TextView) v.findViewById(R.id.name_tv);
            date_tv = (TextView) v.findViewById(R.id.date_tv);
            subject_tv = (TextView) v.findViewById(R.id.subject_tv);
            ll_subject = (LinearLayout) v.findViewById(R.id.ll_subject);
            rl_name = (RelativeLayout) v.findViewById(R.id.rl_name);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view);
        }
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

}

