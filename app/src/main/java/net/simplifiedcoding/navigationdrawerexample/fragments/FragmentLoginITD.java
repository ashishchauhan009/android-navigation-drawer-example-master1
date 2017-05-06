package net.simplifiedcoding.navigationdrawerexample.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.simplifiedcoding.navigationdrawerexample.Constant.Constant;
import net.simplifiedcoding.navigationdrawerexample.Model.LoginModel;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.activities.MainActivity;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class FragmentLoginITD extends Fragment implements Callback<LoginModel>, View.OnClickListener {


    private OnFragmentInteractionListener mListener;
    private TextView tvTitleName;
    private AppCompatTextView tvVision;
    private LoginModel loginModel;
    private ProgressDialog pDialog;
    private LinearLayout llLoginOtp;
    private AppCompatButton btnDonthavepass, btnLoginOtp;
    private AppCompatEditText etEmail, etOTP, etPassword;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private TextView tvLoginusername;

    //private RelativeLayout itdlogBtn;

    public FragmentLoginITD() {
        // Required empty public constructor
    }


    public static FragmentLoginITD newInstance() {
        FragmentLoginITD fragment = new FragmentLoginITD();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login_itd, container, false);
        initView(rootView);
        return rootView;
    }


    void initView(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setPadding(0, 5, 10, 5);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(null);
        ((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ImageView imageView = (ImageView) view.findViewById(R.id.btn_drawer);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).clickEventSlide();
            }
        });

        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedpreferences.edit();

        pDialog = new ProgressDialog(getContext(), R.style.DialogTheme);
        pDialog.setCancelable(true);
        tvTitleName = (TextView) view.findViewById(R.id.task_name_tv);
        llLoginOtp = (LinearLayout) view.findViewById(R.id.ll_login_otp);
        btnDonthavepass = (AppCompatButton) view.findViewById(R.id.btn_donthavepass);
        btnLoginOtp = (AppCompatButton) view.findViewById(R.id.btn_login_otp);
        etEmail = (AppCompatEditText) view.findViewById(R.id.et_sign_in_user_name);
        etPassword = (AppCompatEditText) view.findViewById(R.id.et_password);
        etOTP = (AppCompatEditText) view.findViewById(R.id.et_enter_otp);
        tvLoginusername = (TextView)view.findViewById(R.id.tv_loginusername);
        //itdlogBtn = (RelativeLayout) view.findViewById(R.id.rl_itdlogin);

        llLoginOtp.setVisibility(View.GONE);

        tvTitleName.setText("LOGIN");

        btnDonthavepass.setOnClickListener(this);
        btnLoginOtp.setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tv_resend_otp)).setOnClickListener(this);
        ((AppCompatButton) view.findViewById(R.id.btn_login)).setOnClickListener(this);

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main_drawer, menu); // removed to not double the menu items

    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    void setAllVisionData() {
        if (loginModel.getData() != null)
            tvVision.setText("");
        llLoginOtp.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if ((etEmail.getText() != null && !etEmail.getText().toString().equals("")) && (etPassword.getText() != null && !etPassword.getText().toString().equals(""))) {
                    loginItdUser(etEmail.getText().toString(), etPassword.getText().toString());
                }else{
                    showMessage(v,getString(R.string.valid_email_password));
                }
                break;
            case R.id.btn_donthavepass:
                String email = etEmail.getText().toString();
                if(email!=null&&!email.equals("")) {
                    sendOtpToUser(email);
                }else {
                    showMessage(v,getString(R.string.valid_email));
                }
                break;
            case R.id.btn_login_otp:
                String email2 = etEmail.getText().toString();
                String otp = etOTP.getText().toString();
                if((otp!=null&&!otp.equals(""))&&(email2!=null&&!email2.equals(""))){
                    sendWithOtp(email2,otp);
                }else{
                   showMessage(v,getString(R.string.valid_emailotp));
                }

                break;
            case R.id.tv_resend_otp:
                String email1 = etEmail.getText().toString();
                if(email1!=null&&!email1.equals("")) {
                    sendOtpToUser(email1);
                }else {
                    showMessage(v,getString(R.string.valid_email));
                }
                break;

        }
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    void sendOtpToUser(String email) {
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
        ILOGINDONTPASSWORD ilogindontpassword = retrofit.create(ILOGINDONTPASSWORD.class);
        Call<LoginModel> call = ilogindontpassword.getData(email);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
        loginModel = response.body();

        if (loginModel != null) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (Integer.parseInt(loginModel.getStatus()) == Constant.StatusCode.CLEANMONEY_CODE_SUCCESS) {
                llLoginOtp.setVisibility(View.VISIBLE);
                Log.e("check data login", loginModel.toString());
            } else {
                showMessage(getView(),loginModel.getTag());
                if (pDialog.isShowing())
                    pDialog.dismiss();
            }
        }
    }

    @Override
    public void onFailure(Call<LoginModel> call, Throwable t) {

    }


    public void sendWithOtp(String email,String otp) {
        if (pDialog != null) {
            pDialog.setMessage(getString(R.string.loading));
            if (!pDialog.isShowing())
                pDialog.show();
        }
        Log.e("check unfriend", email);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.WebUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        FragmentLoginITD.ILOGINWITHOTP iloginwithotp = retrofit.create(FragmentLoginITD.ILOGINWITHOTP.class);
        retrofit2.Call<LoginModel> call = null;

        call = iloginwithotp.getData(email,otp);
        call.enqueue(logindata);

    }
    retrofit2.Callback<LoginModel> logindata = new retrofit2.Callback<LoginModel>() {

        @Override
        public void onResponse(retrofit2.Call<LoginModel> call, retrofit2.Response<LoginModel> response) {
            LoginModel sendotpdata = response.body();
            if (sendotpdata != null) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                if (Integer.parseInt(sendotpdata.getStatus()) == Constant.StatusCode.CLEANMONEY_CODE_SUCCESS) {
                    if(sharedpreferences.getString("TittleName","") == "") {
                        editor.putString("TittleName", sendotpdata.getData().getMessage());
                        editor.commit();
                    }
                    if(sharedpreferences.getString("User_id","") == "") {
                        editor.putString("User_id", sendotpdata.getData().getId());
                        editor.commit();
                    }
                    if (sharedpreferences.getString("designation", "") == "") {
                        editor.putString("designation", sendotpdata.getData().getDesignation());
                        editor.commit();
                    }
                    if (sharedpreferences.getString("name", "") == "") {
                        editor.putString("name", sendotpdata.getData().getName());
                        editor.commit();
                    }
                    //sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    //editor = sharedpreferences.edit();
                    Log.e("check shared preference", sharedpreferences.getString("TittleName", "").toString());
//                    if(sharedpreferences.getString("TittleName", "").toString()!=null) {
//                        tvLoginusername.setText(sharedpreferences.getString("TittleName", "").toString());
//                    }

                    ((MainActivity)getActivity()).setWelcomeText(sharedpreferences.getString("TittleName", ""));

//                    Bundle bundle = new Bundle();
//                    bundle.putString("tittlename", sendotpdata.getData().getMessage());
//                    bundle.putString("email",etEmail.getText().toString());
//                    Fragment fragment = new ConfirmPasswordActivity();
//                    fragment.setArguments(bundle);
                    etOTP.setText("");
                    etEmail.setText("");
//                    ((MainActivity) getContext()).replacefragment(fragment);
                }  else {
                    showMessage(getView(),sendotpdata.getTag());
                }
            }
        }

        @Override
        public void onFailure(retrofit2.Call<LoginModel> call, Throwable t) {
            showMessage(getView(),"Something went wrong...");
        }
    };

    public void loginItdUser(String email,String password) {
        if (pDialog != null) {
            pDialog.setMessage(getString(R.string.loading));
            if (!pDialog.isShowing())
                pDialog.show();
        }
        Log.e("check unfriend", email);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.WebUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        ILOGINITDUSER iloginitduser = retrofit.create(ILOGINITDUSER.class);
        retrofit2.Call<LoginModel> call = null;

        call = iloginitduser.getData(email,password);
        call.enqueue(loginitd);
    }
    retrofit2.Callback<LoginModel> loginitd = new retrofit2.Callback<LoginModel>() {

        @Override
        public void onResponse(retrofit2.Call<LoginModel> call, retrofit2.Response<LoginModel> response) {
            LoginModel logindata = response.body();
            if (logindata != null) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                if (Integer.parseInt(logindata.getStatus()) == Constant.StatusCode.CLEANMONEY_CODE_SUCCESS) {
                    etPassword.setText("");
                    if(sharedpreferences.getString("TittleName","") == "") {
                        editor.putString("TittleName", logindata.getData().getMessage());
                        editor.commit();
                    }
                    if(sharedpreferences.getString("User_id","") == "") {
                        editor.putString("User_id", logindata.getData().getId());
                        editor.commit();
                    }
                    if (sharedpreferences.getString("designation", "") == "") {
                        editor.putString("designation", logindata.getData().getDesignation());
                        editor.commit();
                    }
                    if (sharedpreferences.getString("name", "") == "") {
                        editor.putString("name", logindata.getData().getName());
                        editor.commit();
                    }
//                    if(logindata.getData().getMessage()!=null){
//                        tvLoginusername.setText(logindata.getData().getMessage());
//                    }
//                    TextView textView = new TextView(getContext());
                   // ((MainActivity)getActivity()).hideItemlogin();

                    ((MainActivity)getActivity()).setWelcomeText(logindata.getData().getMessage());

//                    if(sharedpreferences.getString("TittleName", "")!=null) {
//                        tvLoginusername.setText(sharedpreferences.getString("TittleName", "").toString());
//                    }
                    //itdlogBtn.setVisibility(View.GONE);

                    Bundle bundle = new Bundle();
                    bundle.putString("name",logindata.getData().getMessage());
                    Fragment fragment = new FragmentTaskDashBoard();
                    fragment.setArguments(bundle);
                    ((MainActivity) getContext()).replacefragment(fragment);
                    showMessage(getView(),getString(R.string.sccessfullogin));
                }  else {
                    showMessage(getView(),logindata.getTag());
                }
            }
        }

        @Override
        public void onFailure(retrofit2.Call<LoginModel> call, Throwable t) {
            showMessage(getView(),"Something went wrong...");
        }
    };

    public interface ILOGINDONTPASSWORD {
        @GET(Constant.WebUrl.LOGINDONTHAVEPASSWORD)
        Call<LoginModel> getData(@Query("email") String email);
    }
    public interface ILOGINWITHOTP {
        @GET(Constant.WebUrl.LOGINWITHOTP)
        Call<LoginModel> getData(@Query("email") String email,@Query("otp") String otp);
    }
    public interface ILOGINITDUSER {
        @GET(Constant.WebUrl.LOGINITD)
        Call<LoginModel> getData(@Query("email") String email,@Query("password") String password);
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
