package net.simplifiedcoding.navigationdrawerexample.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.simplifiedcoding.navigationdrawerexample.Constant.Constant;
import net.simplifiedcoding.navigationdrawerexample.Model.FaqCommon;
import net.simplifiedcoding.navigationdrawerexample.Model.Search;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.activities.MainActivity;
import net.simplifiedcoding.navigationdrawerexample.fragments.FragmentCreateTask;
import net.simplifiedcoding.navigationdrawerexample.util.AndroidUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by vibes on 6/3/17.
 */

public class AdapterFavTask extends RecyclerView.Adapter<AdapterFavTask.MyViewHolder> {

    private Context context;
    private ArrayList<FaqCommon> faqModelsList;
    List<Search> data;
    String status;
    String user_id;
    public ProgressDialog pDialog;
    private static ClickListener clickListener;
    SharedPreferences sharedpreferences;


    //    public AdapterNewTask(Context context, FaqModel data) {
//        this.context = context;
//        if (data != null) {
//            faqModelsList = data.getFaq();
//           // Log.e("check data on adapter",faqModelsList.toString());
//        }
//    }
    public AdapterFavTask(Context context, ArrayList<Search> data) {
        this.context = context;
        this.data = data;
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_fav_list, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (data != null && data.size() > 0) {
            holder.delfav_tv.setTag(position);
            if (data.get(position).getUser_phone() != null && !data.get(position).getUser_phone().isEmpty()) {
                holder.designation_tv.setText(data.get(position).getUser_designation());
            }
            if (data.get(position).getUser_email() != null && !data.get(position).getUser_email().isEmpty()) {
                holder.tvMail.setText(data.get(position).getUser_email());
            }
//            if (data.get(position).getUser_designation() != null && !data.get(position).getUser_designation().isEmpty()) {
//                holder.tvDescription.setText(data.get(position).getUser_designation());
//            }
            if (data.get(position).getUser_name() != null && !data.get(position).getUser_name().isEmpty()) {
                holder.tvName.setText(data.get(position).getUser_name());
            }
            holder.tvAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("email", data.get(position).getUser_email());
                    bundle.putString("id", data.get(position).getUser_id());
                    Fragment fragment = new FragmentCreateTask();
                    fragment.setArguments(bundle);
                    ((MainActivity) context).replacefragment(fragment);
                }
            });
            holder.delfav_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String del_task_id = data.get(position).getUser_id();
//
                    deleteTask(del_task_id, position);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        AdapterFavTask.clickListener = clickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView designation_tv, tvMail, tvName, delfav_tv;
        TextView delete_tv;
        AppCompatButton tvAction;
        String user_id;
        CardView card_view;

        public MyViewHolder(View v) {
            super(v);

            designation_tv = (TextView) v.findViewById(R.id.designation_tv);
            tvMail = (TextView) v.findViewById(R.id.tv_mail);
//            tvDescription = (TextView) v.findViewById(R.id.tv_description);
            tvName = (TextView) v.findViewById(R.id.tv_name);
            tvAction = (AppCompatButton) v.findViewById(R.id.btn_action);
            delfav_tv = (TextView) v.findViewById(R.id.delfav_tv);
            delete_tv = (TextView) v.findViewById(R.id.delete_tv);
            card_view = (CardView) v.findViewById(R.id.card_view);
//            delfav_tv.setOnClickListener(this);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
//            switch (view.getId()) {
//                case R.id.delfav_tv:
//                    int pos = (int) view.getTag();
//                    String del_task_id = data.get(pos).getUser_id();
//
//                    deleteTask(del_task_id, pos);
//
//                    break;
//
//            }
            clickListener.onItemClick(getAdapterPosition(),view);

        }


    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public interface IUSERLIST {
        @GET(Constant.WebUrl.REMOVE_FAV_URL)
        Call<ResponseBody> getData(@Query("for") String type, @Query("id") String id);


    }
    public void deleteTask(final String del_task_id, final int pos) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        // Setting Dialog Title
        alertDialog.setTitle("Delete");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to delete?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (AndroidUtil.isConnectingToInternet(context)) {
                    deleteFav(del_task_id, pos);
                } else {
                    Toast.makeText(context, Constant.check_internet_connection, Toast.LENGTH_SHORT).show();
                }

            }
        });

        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }


    public void deleteFav(String taskid, final int pos) {

        pDialog = new ProgressDialog(context, R.style.DialogTheme);
        pDialog.setCancelable(true);
        if (pDialog != null) {
            pDialog.setMessage(context.getString(R.string.loading));
            if (!pDialog.isShowing())
                pDialog.show();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.WebUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        IUSERLIST iuserlist = retrofit.create(IUSERLIST.class);
        if (sharedpreferences.getString("User_id", "") != null) {
            user_id = sharedpreferences.getString("User_id", "");
        }

        Call<ResponseBody> call = iuserlist.getData(taskid, user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (pDialog != null) {
                    pDialog.dismiss();
                }
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    try {
                        String data = response.body().string();
                        JSONObject jsonObject = new JSONObject(data);
                        if (jsonObject.getInt("status") == Constant.StatusCode.CLEANMONEY_CODE_SUCCESS) {
                            deleteItem(pos);


//                                notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, jsonObject.getString("tag") + "", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (pDialog != null) {
                    pDialog.dismiss();
                }
            }
        });
    }

    public void deleteItem(int childposition) {
        try {
            data.remove(childposition);
            //            int size = getItemCount();
            notifyDataSetChanged();
            notifyItemRemoved(childposition);
//                if (delete_tv.getVisibility() == View.GONE) {
//            if (data.size() == 0) {
//                delete_tv.setVisibility(View.VISIBLE);
//                card_view.setVisibility(View.GONE);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //        notifyItemRemoved(index);
    }


}

