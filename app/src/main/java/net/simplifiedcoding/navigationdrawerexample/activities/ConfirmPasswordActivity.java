package net.simplifiedcoding.navigationdrawerexample.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import net.simplifiedcoding.navigationdrawerexample.Constant.Constant;
import net.simplifiedcoding.navigationdrawerexample.Model.LoginModel;
import net.simplifiedcoding.navigationdrawerexample.R;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class ConfirmPasswordActivity extends Activity implements Callback<LoginModel>, View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    private TextView tvTitleName, tvPageTitle;
    private AppCompatEditText etFirstPass, etSecondPass;
    private ProgressDialog pDialog;
    private String tittlename,email;
    private AppCompatButton btnSubmit;
   // private RelativeLayout itdlogBtn;
    View root_view;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public ConfirmPasswordActivity() {
        // Required empty public constructor
    }


    public static ConfirmPasswordActivity newInstance() {
        ConfirmPasswordActivity fragment = new ConfirmPasswordActivity();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_confirm_password);
        initView();

    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_confirm_password, container, false);
//        initView(rootView);
//        return rootView;
//    }


    void initView() {
        try {
            tittlename=    getIntent().getStringExtra("tittlename");
            email=    getIntent().getStringExtra("email");
//            tittlename = getArguments().getString("tittlename");
//            email = getArguments().getString("email");
            Log.e("check group id", tittlename);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedpreferences.edit();

        tvTitleName = (TextView) findViewById(R.id.task_name_tv);
        tvPageTitle = (TextView) findViewById(R.id.tv_page_title);
        btnSubmit = (AppCompatButton) findViewById(R.id.btn_submit);
        etFirstPass = (AppCompatEditText) findViewById(R.id.et_firstpassword);
        etSecondPass = (AppCompatEditText) findViewById(R.id.et_secondpassword);
        //itdlogBtn = (RelativeLayout) view.findViewById(R.id.rl_itdlogin);

        root_view=(CoordinatorLayout)findViewById(R.id.root_view);
        tvTitleName.setText("CREATE PASSWORD");
        tvPageTitle.setText(tittlename);

        btnSubmit.setOnClickListener(this);

        // getDataFronServer();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                if ((etFirstPass.getText() != null && !etFirstPass.getText().toString().equals(""))
                        && (etSecondPass.getText() != null && !etSecondPass.getText().toString().equals(""))) {
                    if (etFirstPass.getText().toString().equals(etSecondPass.getText().toString())) {
                        createPassword(email,etFirstPass.getText().toString());
                    } else {
                        showMessage(v, getString(R.string.password_notmatch));
                    }
                } else {
                    showMessage(v, getString(R.string.fill_password));
                }

                break;
        }
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    void createPassword(String email, String password ) {
        pDialog = new ProgressDialog(this, R.style.DialogTheme);
        pDialog.setCancelable(false);
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
        ICREATEPASSWORD icreatepassword = retrofit.create(ICREATEPASSWORD.class);
        Call<LoginModel> call = icreatepassword.getData(email,password);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
        LoginModel responsedata = response.body();

        if (responsedata != null) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (Integer.parseInt(responsedata.getStatus()) == Constant.StatusCode.CLEANMONEY_CODE_SUCCESS) {

                if (sharedpreferences.getString("isValidUser", "") == "") {
                    editor.putString("isValidUser", responsedata.getData().getValiduser());
                    editor.commit();
                }
                if(sharedpreferences.getString("TittleName","") == "") {
                    editor.putString("TittleName", responsedata.getData().getMessage());
                    editor.commit();
                }
                if(sharedpreferences.getString("User_id","") == "") {
                    editor.putString("User_id", responsedata.getData().getId());
                    editor.commit();
                }
                if (sharedpreferences.getString("designation", "") == "") {
                    editor.putString("designation", responsedata.getData().getDesignation());
                    editor.commit();
                }
                if (sharedpreferences.getString("name", "") == "") {
                    editor.putString("name", responsedata.getData().getName());
                    editor.commit();
                }
                Intent intent = new Intent(ConfirmPasswordActivity.this,MainActivity.class);
                intent.putExtra("name",tittlename);
                startActivity(intent);

//                TextView textView = new TextView(getContext());
              //  ((MainActivity)getActivity()).hideItemlogin();
                //itdlogBtn.setVisibility(View.GONE);
//                Bundle bundle = new Bundle();
//                bundle.putString("name",tittlename);
//                Fragment fragment = new FragmentTaskDashBoard();
//                fragment.setArguments(bundle);
//                ((MainActivity) getContext()).replacefragment(fragment);
//                etFirstPass.setText("");
//                etSecondPass.setText("");
                showMessage(root_view,getString(R.string.sccessfullogin));
            }else{
                showMessage(root_view,"Something went wrong....");
            }
        }
    }

    @Override
    public void onFailure(Call<LoginModel> call, Throwable t) {

    }

    public interface ICREATEPASSWORD {
        @GET(Constant.WebUrl.UPDATEPASSWORD)
        Call<LoginModel> getData(@Query("email") String email, @Query("password") String password);
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
