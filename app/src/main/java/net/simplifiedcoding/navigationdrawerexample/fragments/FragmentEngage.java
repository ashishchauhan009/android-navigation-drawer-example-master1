package net.simplifiedcoding.navigationdrawerexample.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/*import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;*/

import net.simplifiedcoding.navigationdrawerexample.Constant.Constant;
import net.simplifiedcoding.navigationdrawerexample.Model.MisLevel;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.activities.MainActivity;
import net.simplifiedcoding.navigationdrawerexample.adapter.AdapterImageEngage;
import net.simplifiedcoding.navigationdrawerexample.util.Config;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class FragmentEngage extends Fragment implements Callback<MisLevel>, View.OnClickListener {

    List<MisLevel.Datum> datumList;
    private ProgressDialog pDialog;
    private GridView gridView;
    private RecyclerView recyclerView;
    //    CallbackManager callbackManager;
    TextView tv_count_engage, tv_join_people, tv_btnTag_yourself;

    public FragmentEngage() {
        // Required empty public constructor
    }

    public static FragmentEngage newInstance() {
        FragmentEngage fragment = new FragmentEngage();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_engage, container, false);
//        callbackManager = CallbackManager.Factory.create();
        initView(rootView);
//        FacebookLogin();
        return rootView;
    }


    void initView(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setPadding(0, 5, 10, 5);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(null);
        ((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ImageView imageView = (ImageView) view.findViewById(R.id.btn_drawer);

        //textview ids
        tv_btnTag_yourself = (TextView) view.findViewById(R.id.tv_btnTag_yourself);
        tv_join_people = (TextView) view.findViewById(R.id.tv_join_people);
        tv_count_engage = (TextView) view.findViewById(R.id.tv_count_engage);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).clickEventSlide();
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        pDialog = new ProgressDialog(getContext(), R.style.DialogTheme);
        pDialog.setCancelable(true);

        newTaskApiCall(Config.Engage);
        setClickAction();
    }

    private void setClickAction() {
        tv_btnTag_yourself.setOnClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main_drawer, menu); // removed to not double the menu items
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    void setallcontent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_btnTag_yourself:
                Toast.makeText(getActivity(), "Tag YourSelf", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    /* public void FacebookLogin() {
         FacebookSdk.sdkInitialize(getContext());

         callbackManager = CallbackManager.Factory.create();

         LoginManager.getInstance().registerCallback(callbackManager,
                 new FacebookCallback<LoginResult>() {
                     @Override
                     public void onSuccess(LoginResult loginResult) {
                         Log.d("Success", "Login");

                     }

                     @Override
                     public void onCancel() {
                         Toast.makeText(getActivity(), "Login Cancel", Toast.LENGTH_LONG).show();
                     }

                     @Override
                     public void onError(FacebookException exception) {
                         Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();
                     }
                 });


         tv_btnTag_yourself.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 LoginManager.getInstance().logInWithReadPermissions(
                         getActivity(), Arrays.asList("public_profile", "email", "user_friends"));
             }
         });
     }

     @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         callbackManager.onActivityResult(requestCode, resultCode, data);
     }
 */
    @Override
    public void onResponse(Call<MisLevel> call, Response<MisLevel> response) {
        MisLevel engageSupportData = response.body();

        if (engageSupportData != null) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (Integer.parseInt(engageSupportData.getStatus()) == Constant.StatusCode.CLEANMONEY_CODE_SUCCESS) {

                if (engageSupportData.getData() != null && engageSupportData.getData().size() > 0) {
                    datumList = engageSupportData.getData();
                    AdapterImageEngage adapter = new AdapterImageEngage(getContext(), datumList);
                    recyclerView.setAdapter(adapter);
                    setallcontent();
                }

                if (!engageSupportData.getCount().isEmpty()) {
                    tv_count_engage.setText(engageSupportData.getCount().toString() + " People");
                }
            }
        }
        Log.d("Result Engage", response.toString());
    }

    @Override
    public void onFailure(Call<MisLevel> call, Throwable t) {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void newTaskApiCall(String type) {

        if (pDialog != null) {
            pDialog.setMessage(getString(R.string.loading));
            if (!pDialog.isShowing())
                pDialog.show();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.WebUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        IENGAGEANDSUPPORT iengageandsupport = retrofit.create(IENGAGEANDSUPPORT.class);
        Call<MisLevel> call = iengageandsupport.getTask(type);
        Log.e("chek id", type.toString());
        call.enqueue(this);
    }


    private void applyFacebookApi(String type, String name, String facebook_id, String mail, String pic_url) {

        if (pDialog != null) {
            pDialog.setMessage(getString(R.string.loading));
            if (!pDialog.isShowing())
                pDialog.show();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.WebUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        IENGAGEANDSUPPORT iengageandsupport = retrofit.create(IENGAGEANDSUPPORT.class);
        Call<ResponseBody> call = iengageandsupport.getTaskFacebook(type, name, mail, pic_url, facebook_id);
        Log.e("chek id", type.toString());


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
                        if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                            Toast.makeText(getActivity(), jsonObject.getString("tag"), Toast.LENGTH_LONG).show();
                            getFragmentManager().popBackStackImmediate();
                        } else {
                            Toast.makeText(getActivity(), jsonObject.getString("tag"), Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }


    public interface IENGAGEANDSUPPORT {
        @POST(Constant.WebUrl.ENGAGEANDSUPPORT)
        Call<MisLevel> getTask(@Query("type") String type);

        @POST(Constant.WebUrl.ENGAGEFACEBOOK)
        Call<ResponseBody> getTaskFacebook(@Query("type") String type, @Query("name") String name, @Query("mail") String mail,
                                           @Query("pic_url") String pic_url, @Query("fbid") String fbid);
    }

    public void showMessage(View view, String tag) {
        if (view != null && view.isShown()) {
            Snackbar snack = Snackbar.make(view, tag, Snackbar.LENGTH_LONG);
            View v = snack.getView();
            TextView tv = (TextView) v.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }
    }
}
