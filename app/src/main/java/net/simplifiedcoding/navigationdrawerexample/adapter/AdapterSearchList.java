package net.simplifiedcoding.navigationdrawerexample.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.simplifiedcoding.navigationdrawerexample.Constant.Constant;
import net.simplifiedcoding.navigationdrawerexample.Model.Search;
import net.simplifiedcoding.navigationdrawerexample.Model.UserSearch;
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
 * Created by vibes on 16/3/17.
 */

public class AdapterSearchList extends RecyclerView.Adapter<AdapterSearchList.MyViewHolder> {

    private Context context;
    public ProgressDialog pDialog;
    private ArrayList<Search> userSearches = new ArrayList<>();
    SharedPreferences sharedpreferences;
    private static AdapterSearchList.ClickListener clickListener;

    public AdapterSearchList(Context context, UserSearch data) {
        this.context = context;
       /* if (data != null) {
            userSearches = data.getData();
            // Log.e("check data on adapter",faqModelsList.toString());
        }*/
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void clearAllData() {
        userSearches.clear(); //clear list
        notifyDataSetChanged(); //let  adapter know about the changes and reload view.
    }

    public void addItem(Search result) {
        userSearches.add(result);
        if (userSearches.size() > 1)
            notifyItemChanged(getItemCount() - 1);
        else
            notifyDataSetChanged();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_list, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Search search_by_name = userSearches.get(position);

        if (search_by_name.getUser_name() != null && !search_by_name.getUser_name().isEmpty()) {
            holder.tvName.setText(search_by_name.getUser_name());
            changeDrawable(holder);
        }

        /*if (userSearches.get(position).getDate() != null && !userSearches.get(position).getDate().isEmpty()) {
            holder.tv_date.setText(AndroidUtil.formatMMDate(userSearches.get(position).getDate(), "dd-MM-yyyy", "MMM dd"));
        }*/
        if (search_by_name.getUser_designation() != null && !search_by_name.getUser_designation().isEmpty()) {
            holder.tv_designation.setText(search_by_name.getUser_designation());
            changeDrawabledesig(holder);
        }
    }

    private void changeDrawable(MyViewHolder holder) {
        Drawable img = context.getResources().getDrawable(R.mipmap.icon_user_grey);
        holder.tvName.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
    }

    private void changeDrawabledesig(MyViewHolder holder) {
        Drawable img = context.getResources().getDrawable(R.mipmap.icon_level);
        holder.tv_designation.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
    }

    @Override
    public int getItemCount() {
        return userSearches.size();
    }

    public void setOnItemClickListener(AdapterSearchList.ClickListener clickListener) {
        AdapterSearchList.clickListener = clickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName, tv_designation, tv_date;
        String user_id;


        public MyViewHolder(View v) {
            super(v);

            tvName = (TextView) v.findViewById(R.id.tv_name);
            tv_designation = (TextView) v.findViewById(R.id.tv_designation);
            tv_date = (TextView) v.findViewById(R.id.tv_date);
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
           /* switch (view.getId()) {
                case R.id.addtofav_tv:
                    int pos = (int) view.getTag();
                    String taskId = userSearches.get(pos).getUser_id();
                    if (userSearches.get(pos).getFav().equalsIgnoreCase("1")) {
                        Toast.makeText(context, "Already added as favorite", Toast.LENGTH_SHORT).show();
                    } else {
                        if (AndroidUtil.isConnectingToInternet(context)) {
                            addToFav(taskId, pos);
                        } else {
                            Toast.makeText(context, Constant.check_internet_connection, Toast.LENGTH_SHORT).show();
                        }
                    }

                    return;
            }*/
            clickListener.onItemClick(getAdapterPosition(), view, userSearches);
        }


    }


    public interface IUSERLIST {
        @GET(Constant.WebUrl.ADD_TO_FAV_URL)
        Call<ResponseBody> getData(@Query("for") String type, @Query("id") String id);


    }

    public interface ClickListener {
        void onItemClick(int position, View v, List<Search> userList);
    }
}
