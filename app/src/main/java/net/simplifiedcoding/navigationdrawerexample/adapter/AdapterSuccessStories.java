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
import net.simplifiedcoding.navigationdrawerexample.Model.Datum;
import net.simplifiedcoding.navigationdrawerexample.Model.FaqCommon;
import net.simplifiedcoding.navigationdrawerexample.Model.Search;
import net.simplifiedcoding.navigationdrawerexample.Model.SuccessStoriesData;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.activities.MainActivity;
import net.simplifiedcoding.navigationdrawerexample.fragments.FragmentCreateTask;
import net.simplifiedcoding.navigationdrawerexample.fragments.FragmentSuccessStories;
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

public class AdapterSuccessStories extends RecyclerView.Adapter<AdapterSuccessStories.MyViewHolder> {

    private Context context;
    List<SuccessStoriesData.Datum> data;
    String status;
    private static ClickListener clickListener;
    SharedPreferences sharedpreferences;


    public AdapterSuccessStories(Context context, List<SuccessStoriesData.Datum> data) {
        this.context = context;
        this.data = data;
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sucessstories, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (data != null && data.size() > 0) {

            holder.read_more_tv.setTag(position);

            if (data.get(position).getTitle() != null && !data.get(position).getTitle().isEmpty()) {
                holder.title_name_tv.setText(" " + data.get(position).getTitle());
            }
            if (data.get(position).getAuthor() != null && !data.get(position).getAuthor().isEmpty()) {
                holder.author_tv.setText(" " + data.get(position).getAuthor());
            }
            String[] date = data.get(position).getCreatedOn().split(" ");
            if (date.length > 0)
                holder.created_date_tv.setText(" " + AndroidUtil.formatDate(date[0], "yyyy-mm-dd", "dd-mm-yyyy"));
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        AdapterSuccessStories.clickListener = clickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title_name_tv, author_tv, created_date_tv, read_more_tv;

        public MyViewHolder(View v) {
            super(v);
            title_name_tv = (TextView) v.findViewById(R.id.title_name_tv);
            author_tv = (TextView) v.findViewById(R.id.author_name_tv);
            created_date_tv = (TextView) v.findViewById(R.id.created_date_tv);
            read_more_tv = (TextView) v.findViewById(R.id.read_more_tv);
//            v.setOnClickListener(this);
            read_more_tv.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.read_more_tv:
                    int pos = (int) view.getTag();
                    String title = data.get(pos).getTitle();
                    String heading = data.get(pos).getHeading();
                    String desc = data.get(pos).getDescription();
                    String author = data.get(pos).getAuthor();
                    String url = data.get(pos).getUrl();
                    String success_id = data.get(pos).getSuccessId();
                    String image = data.get(pos).getImage();

                    Fragment fragment = new FragmentSuccessStories();
                    Bundle bundle = new Bundle();
                    bundle.putString("frag", "frag_edit");
                    bundle.putString("title", title);
                    bundle.putString("heading", heading);
                    bundle.putString("desc", desc);
                    bundle.putString("author", author);
                    bundle.putString("url", url);
                    bundle.putString("success_id",success_id);
                    bundle.putString("image",image);
                    fragment.setArguments(bundle);
                    ((MainActivity) context).replacefragment(fragment);
                    break;
            }
//            clickListener.onItemClick(getAdapterPosition(), view);
        }

    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }


}

