package net.simplifiedcoding.navigationdrawerexample.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.simplifiedcoding.navigationdrawerexample.Constant.Constant;
import net.simplifiedcoding.navigationdrawerexample.Model.LoginModel;
import net.simplifiedcoding.navigationdrawerexample.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class SignInActivity extends Activity implements View.OnClickListener, Callback<LoginModel> {

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
    View parentLayout;

    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 123456;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar();
        setContentView(R.layout.activity_sign_in);

        initView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final List<String> permissionsList = new ArrayList<String>();
            addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE);
            addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION);
            addPermission(permissionsList, Manifest.permission.ACCESS_NETWORK_STATE);
            addPermission(permissionsList, Manifest.permission.CAMERA);
            if (permissionsList.isEmpty()) {

                btnDonthavepass.setOnClickListener(this);
                btnLoginOtp.setOnClickListener(this);
            } else {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
        } else {

            btnDonthavepass.setOnClickListener(this);
            btnLoginOtp.setOnClickListener(this);
        }
    }

    private void setActionBar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusbarcolor));
        }
    }


    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, permission))
                    return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_NETWORK_STATE, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
                        ) {
                    // All Permissions Granted

                    btnDonthavepass.setOnClickListener(this);
                    btnLoginOtp.setOnClickListener(this);

                } else {
                    Toast.makeText(getApplicationContext(), "Please Allow Required Permissions to App", Toast.LENGTH_LONG).show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    void initView() {

        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedpreferences.edit();

        parentLayout = findViewById(R.id.root_view);

        pDialog = new ProgressDialog(this, R.style.DialogTheme);
        pDialog.setCancelable(true);
        tvTitleName = (TextView) findViewById(R.id.task_name_tv);
        llLoginOtp = (LinearLayout) findViewById(R.id.ll_login_otp);
        btnDonthavepass = (AppCompatButton) findViewById(R.id.btn_donthavepass);
        btnLoginOtp = (AppCompatButton) findViewById(R.id.btn_login_otp);
        etEmail = (AppCompatEditText) findViewById(R.id.et_sign_in_user_name);
        etPassword = (AppCompatEditText) findViewById(R.id.et_password);
        etOTP = (AppCompatEditText) findViewById(R.id.et_enter_otp);
        tvLoginusername = (TextView) findViewById(R.id.tv_loginusername);
        //itdlogBtn = (RelativeLayout) view.findViewById(R.id.rl_itdlogin);

        llLoginOtp.setVisibility(View.GONE);

        tvTitleName.setText("LOGIN");

        ((TextView) findViewById(R.id.tv_resend_otp)).setOnClickListener(this);
        ((AppCompatButton) findViewById(R.id.btn_login)).setOnClickListener(this);

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
                if ((etEmail.getText() != null && !etEmail.getText().toString().equals(""))
                        && (etPassword.getText() != null && !etPassword.getText().toString().equals(""))) {
                    loginItdUser(etEmail.getText().toString(), etPassword.getText().toString());
                } else {
                    showMessage(v, getString(R.string.valid_email_password));
                }
                break;
            case R.id.btn_donthavepass:
                String email = etEmail.getText().toString();
                if (email != null && !email.equals("")) {
                    sendOtpToUser(email);
                } else {
                    showMessage(v, getString(R.string.valid_email));
                }
                break;
            case R.id.btn_login_otp:
                String email2 = etEmail.getText().toString();
                String otp = etOTP.getText().toString();
                if ((otp != null && !otp.equals("")) && (email2 != null && !email2.equals(""))) {
                    sendWithOtp(email2, otp);
                } else {
                    showMessage(v, getString(R.string.valid_emailotp));
                }

                break;
            case R.id.tv_resend_otp:
                String email1 = etEmail.getText().toString();
                if (email1 != null && !email1.equals("")) {
                    sendOtpToUser(email1);
                } else {
                    showMessage(v, getString(R.string.valid_email));
                }
                break;

        }
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
                showMessage(parentLayout, loginModel.getTag());
                // Toast.makeText(SignInActivity.this,loginModel.getTag(),Toast.LENGTH_SHORT).show();
                if (pDialog.isShowing())
                    pDialog.dismiss();
            }
        }
    }

    @Override
    public void onFailure(Call<LoginModel> call, Throwable t) {
        if (pDialog.isShowing())
            pDialog.dismiss();
        showMessage(parentLayout, getString(R.string.wentwrongmsg));
    }


    public void sendWithOtp(String email, String otp) {
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
        ILOGINWITHOTP iloginwithotp = retrofit.create(ILOGINWITHOTP.class);
        retrofit2.Call<LoginModel> call = null;

        call = iloginwithotp.getData(email, otp);
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
                    if (sharedpreferences.getString("TittleName", "") == "") {
                        editor.putString("TittleName", sendotpdata.getData().getMessage());
                        editor.commit();
                    }
                    if (sharedpreferences.getString("User_id", "") == "") {
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

                    // ((MainActivity)getActivity()).setWelcomeText(sharedpreferences.getString("TittleName", ""));


                    Intent intent = new Intent(SignInActivity.this, ConfirmPasswordActivity.class);
                    intent.putExtra("tittlename", sendotpdata.getData().getMessage());
                    intent.putExtra("email", etEmail.getText().toString());
                    startActivity(intent);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("tittlename", sendotpdata.getData().getMessage());
//                    bundle.putString("email",etEmail.getText().toString());
//                    Fragment fragment = new ConfirmPasswordActivity();
//                    fragment.setArguments(bundle);
                    etOTP.setText("");
                    etEmail.setText("");
                    // ((MainActivity) getContext()).replacefragment(fragment);
                } else {
                    showMessage(parentLayout, sendotpdata.getTag());

                    //Toast.makeText(SignInActivity.this,getString(R.string.went_wrong),Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(retrofit2.Call<LoginModel> call, Throwable t) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            showMessage(parentLayout, getString(R.string.wentwrongmsg));
        }
    };

    public void loginItdUser(String email, String password) {
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

        call = iloginitduser.getData(email, password);
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
                    //Added by Mahendra 14-4-2017
//                    etPassword.setText("");
                    if (sharedpreferences.getString("TittleName", "") == "") {
                        editor.putString("TittleName", logindata.getData().getMessage());
                        editor.commit();
                    }
                    if (sharedpreferences.getString("User_id", "") == "") {
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
                    if (sharedpreferences.getString("isValidUser", "") == "") {
                        editor.putString("isValidUser", logindata.getData().getValiduser());
                        editor.commit();
                    }
//                    if(logindata.getData().getMessage()!=null){
//                        tvLoginusername.setText(logindata.getData().getMessage());
//                    }
//                    TextView textView = new TextView(getContext());
                    //((MainActivity)getActivity()).hideItemlogin();

                    //  ((MainActivity)getActivity()).setWelcomeText(logindata.getData().getMessage());

//                    if(sharedpreferences.getString("TittleName", "")!=null) {
//                        tvLoginusername.setText(sharedpreferences.getString("TittleName", "").toString());
//                    }
                    //itdlogBtn.setVisibility(View.GONE);

                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    intent.putExtra("name", logindata.getData().getMessage());
                    startActivity(intent);
                    finish();
                   /* Bundle bundle = new Bundle();
                    bundle.putString("name",logindata.getData().getMessage());
                    Fragment fragment = new FragmentTaskDashBoard();
                    fragment.setArguments(bundle);
                    ((MainActivity) getContext()).replacefragment(fragment);*/
                    //  showMessage(v1,getString(R.string.sccessfullogin));
//                    Toast.makeText(SignInActivity.this,getString(R.string.sccessfullogin),Toast.LENGTH_SHORT).show();
                } else {
                    showMessage(parentLayout, logindata.getTag());
                    // Toast.makeText(SignInActivity.this,getString(R.string.wentwrongmsg),Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(retrofit2.Call<LoginModel> call, Throwable t) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            showMessage(parentLayout, getString(R.string.wentwrongmsg));
        }
    };

    public interface ILOGINDONTPASSWORD {
        @GET(Constant.WebUrl.LOGINDONTHAVEPASSWORD)
        Call<LoginModel> getData(@Query("email") String email);
    }

    public interface ILOGINWITHOTP {
        @GET(Constant.WebUrl.LOGINWITHOTP)
        Call<LoginModel> getData(@Query("email") String email, @Query("otp") String otp);
    }

    public interface ILOGINITDUSER {
        @GET(Constant.WebUrl.LOGINITD)
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
