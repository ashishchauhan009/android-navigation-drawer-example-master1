package net.simplifiedcoding.navigationdrawerexample.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.simplifiedcoding.navigationdrawerexample.Constant.Constant;
import net.simplifiedcoding.navigationdrawerexample.Model.VisionModel;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.activities.MainActivity;
import net.simplifiedcoding.navigationdrawerexample.util.AndroidUtil;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;


public class FragmentHelpDesk extends Fragment implements Callback<VisionModel>, View.OnClickListener {


    private OnFragmentInteractionListener mListener;
    private TextView tvTitleName;
    private AppCompatTextView tvVision;
    private VisionModel visionModel;
    private ProgressDialog pDialog;
    private TextView tvEmail, tvCall;
    private ImageView ivCall;
    private String mobileNumber = "";
    private String emailAdd = "";

    public FragmentHelpDesk() {
        // Required empty public constructor
    }

    public static FragmentHelpDesk newInstance() {
        FragmentHelpDesk fragment = new FragmentHelpDesk();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_helpdesk, container, false);
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
        pDialog = new ProgressDialog(getContext(), R.style.DialogTheme);
        pDialog.setCancelable(true);
        tvTitleName = (TextView) view.findViewById(R.id.tv_actionbar_title);
        tvEmail = (TextView) view.findViewById(R.id.tv_email);
        tvCall = (TextView) view.findViewById(R.id.tv_phone);
        ivCall = (ImageView) view.findViewById(R.id.iv_id);
        tvTitleName.setText("Helpdesk");

        tvEmail.setOnClickListener(this);
        ivCall.setOnClickListener(this);


        getDataFronServer();

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
        if (visionModel.getData() != null || visionModel.getData().size() > 0)
            mobileNumber = visionModel.getData().get(0).getContact();
        emailAdd = visionModel.getData().get(0).getBody();
        tvCall.setText(mobileNumber);
        tvEmail.setText(emailAdd);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.tv_email:

                break;
            case R.id.iv_id: {
                new AlertDialog.Builder(getContext())
                        .setMessage(getContext().getString(R.string.confirmation_dialog))
                        .setIcon(AndroidUtil.setTint(getContext().getResources().getDrawable(R.mipmap.ic_launcher_call), R.color.green))
                        .setMessage(getContext().getString(R.string.do_you_want_to_make_call))
                        .setPositiveButton(getContext().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (mobileNumber != null)
                                    actionCall(v, mobileNumber);
                                //make call from phone
                            }
                        })
                        .setNegativeButton(getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();

            }


        }
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    void getDataFronServer() {
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
        IVISIONDATA ivisiondata = retrofit.create(IVISIONDATA.class);
        Call<VisionModel> call = ivisiondata.getData();
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<VisionModel> call, Response<VisionModel> response) {
        visionModel = response.body();

        if (visionModel != null) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (Integer.parseInt(visionModel.getStatus()) == Constant.StatusCode.CLEANMONEY_CODE_SUCCESS) {

                setAllVisionData();

            }
        }
    }

    void actionCall(View view, String number) {
        int checkPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE},
                    Constant.Fields.ACTION_CALL_REQUEST_CODE);
        } else {
            callNumber(view, number);
        }
    }

    private void callNumber(View view, String number) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        getContext().startActivity(intent);
    }

    @Override
    public void onFailure(Call<VisionModel> call, Throwable t) {

    }

    public interface IVISIONDATA {
        @GET(Constant.WebUrl.HELPDESK)
        Call<VisionModel> getData();
    }
}
