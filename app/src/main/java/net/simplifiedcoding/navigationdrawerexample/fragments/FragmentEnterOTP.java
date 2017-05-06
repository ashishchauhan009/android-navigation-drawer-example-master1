package net.simplifiedcoding.navigationdrawerexample.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import net.simplifiedcoding.navigationdrawerexample.R;


public class FragmentEnterOTP extends Fragment implements View.OnClickListener {


    private OnFragmentInteractionListener mListener;
    private AppCompatButton imgSubmitbtn;
    private ImageButton imgBackbtn;

    public FragmentEnterOTP() {
        // Required empty public constructor
    }


    public static FragmentEnterOTP newInstance() {
        FragmentEnterOTP fragment = new FragmentEnterOTP();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_enter_otp, container, false);
        initView(rootView);
        return rootView;
    }


    void initView(View view) {
        imgSubmitbtn = (AppCompatButton) view.findViewById(R.id.btn_submit);
        imgBackbtn = (ImageButton) view.findViewById(R.id.iv_back_btn);

        imgSubmitbtn.setOnClickListener(this);
        imgBackbtn.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                break;
            case R.id.iv_back_btn:
                getFragmentManager().popBackStackImmediate();
                break;
        }
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
