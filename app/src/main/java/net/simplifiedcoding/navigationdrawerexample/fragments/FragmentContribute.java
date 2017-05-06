package net.simplifiedcoding.navigationdrawerexample.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.simplifiedcoding.navigationdrawerexample.Constant.Constant;
import net.simplifiedcoding.navigationdrawerexample.Model.Contribute;
import net.simplifiedcoding.navigationdrawerexample.Model.Contribution;
import net.simplifiedcoding.navigationdrawerexample.Model.State;
import net.simplifiedcoding.navigationdrawerexample.Model.UserSearch;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.activities.MainActivity;
import net.simplifiedcoding.navigationdrawerexample.adapter.AdapterContribute;
import net.simplifiedcoding.navigationdrawerexample.util.AppContent;
import net.simplifiedcoding.navigationdrawerexample.util.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class FragmentContribute extends Fragment implements View.OnClickListener, Callback<Contribution> {

    Spinner spinner_state, spinner_contribute;
    EditText et_pincode;
    TextView btn_submit, txtPeople, tv_contribute_count;
    RecyclerView recyclist;
    private OnFragmentInteractionListener mListener;
    private AppCompatButton imgSubmitbtn;
    private ImageButton imgBackbtn;
    AdapterContribute adapterContribute;
    ArrayList<Contribute> arrayList;
    ArrayList<State> arrayState = new ArrayList<>();
    ArrayList<Contribute> arrayContribute = new ArrayList<>();
    Context context;
    View mySpinner;
    private ProgressDialog pDialog;
    private final String type = "contributedashboard";
    Contribution contribution;
    StateAdatper stateAdatper;
    ContributeAdatper contributeAdatper;

    public FragmentContribute() {
        // Required empty public constructor
    }

    public static FragmentContribute newInstance() {
        FragmentContribute fragment = new FragmentContribute();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contributes, container, false);
        initView(rootView);
        getNewTaskServerData();

        return rootView;
    }


    void initView(View view) {
        spinner_state = (Spinner) view.findViewById(R.id.spinner_state);
        spinner_contribute = (Spinner) view.findViewById(R.id.spinner_contribute);
        et_pincode = (EditText) view.findViewById(R.id.et_pincode);
        btn_submit = (TextView) view.findViewById(R.id.btn_summit);
        txtPeople = (TextView) view.findViewById(R.id.tv_contr);
        tv_contribute_count = (TextView) view.findViewById(R.id.tv_contribute_count);
        recyclist = (RecyclerView) view.findViewById(R.id.recycler_contrib);

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

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contributeDataSubmit();
            }
        });
    }


    private void getNewTaskServerData() {
        pDialog = new ProgressDialog(getContext(), R.style.DialogTheme);
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
        ICONTRIBUTE icreatetask = retrofit.create(ICONTRIBUTE.class);

        Call<Contribution> call = icreatetask.getContrinbutionData();
        call.enqueue(this);
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        }
    }

    private void contributeDataSubmit() {
        View view = new View(context);
        int sp_number_state = spinner_state.getSelectedItemPosition();
        int sp_number_contribute = spinner_contribute.getSelectedItemPosition();

        String state_id = "", contribute_objective_id = "", pin = "";
        state_id = arrayState.get(sp_number_state).getState_id();
        contribute_objective_id = arrayContribute.get(sp_number_contribute).getId();

        pin = et_pincode.getText().toString().trim();

        System.out.println("State_id" + state_id);
        System.out.println("Contribute" + contribute_objective_id);
        System.out.println("pin_code" + pin);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).readTimeout(180, TimeUnit.SECONDS).connectTimeout(120, TimeUnit.SECONDS).build();
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Constant.WebUrl.BASE_URL).addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.client(client).build();

        FragmentContribute.ICONTRIBUTE icontribute = retrofit.create(FragmentContribute.ICONTRIBUTE.class);

        if (!TextUtils.isEmpty(pin) && !TextUtils.isEmpty(state_id) && !TextUtils.isEmpty(contribute_objective_id)) {
            pDialog = new ProgressDialog(getContext(), R.style.DialogTheme);
            pDialog.setCancelable(true);
            if (pDialog != null) {
                pDialog.setMessage("Loading...");
                if (!pDialog.isShowing())
                    pDialog.show();
            }
            Call<Contribution> submitContributeTask = icontribute.getContributionSubmit(pin, state_id, contribute_objective_id);
            submitContributeTask.enqueue(
                    new Callback<Contribution>() {
                        @Override
                        public void onResponse(Call<Contribution> call, Response<Contribution> response) {
                            if (pDialog != null) {
                                pDialog.dismiss();
                            }
                            Contribution contribution_submition = response.body();
                            et_pincode.setText("");
                            adapterContribute = new AdapterContribute(context, contribution_submition.getRecentcontribute());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            recyclist.setLayoutManager(mLayoutManager);
                            recyclist.setItemAnimator(new DefaultItemAnimator());
                            recyclist.setAdapter(adapterContribute);
                            recyclist.setNestedScrollingEnabled(false);
                            adapterContribute.notifyDataSetChanged();
                            Log.d("SubmitData", response.toString());
                        }

                        @Override
                        public void onFailure(Call<Contribution> call, Throwable t) {
                        }
                    }
            );
        } else {
            Toast.makeText(context, Config.Interet_Error, Toast.LENGTH_SHORT).show();
        }
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

    @Override
    public void onResponse(Call<Contribution> call, Response<Contribution> response) {
        if (pDialog != null) {
            pDialog.dismiss();
        }
        contribution = response.body();
        if (contribution.getStatus().equals("1")) {
            stateValue();
            contributeValue();

            adapterContribute = new AdapterContribute(context, contribution.getRecentcontribute());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclist.setLayoutManager(mLayoutManager);
            recyclist.setItemAnimator(new DefaultItemAnimator());
            recyclist.setAdapter(adapterContribute);
            recyclist.setNestedScrollingEnabled(false);
            adapterContribute.notifyDataSetChanged();

            tv_contribute_count.setText(contribution.getCount() + "People");
        } else {
            Toast.makeText(context, "Network problem", Toast.LENGTH_SHORT).show();
        }
    }

    private void stateValue() {
        arrayState.clear();
        arrayState = new ArrayList<State>();
        arrayState = contribution.getState();
        State state = new State();
        state.setState_name("State");
        arrayState.add(0, state);
        stateAdatper = new FragmentContribute.StateAdatper(getActivity(), R.layout.cus_spinner_contribute, arrayState);
        spinner_state.setAdapter(stateAdatper);
        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void contributeValue() {
        arrayContribute.clear();
        arrayContribute = new ArrayList<Contribute>();
        arrayContribute = contribution.getContribute();
        Contribute contribute = new Contribute();
        contribute.setName("Contribute");
        arrayContribute.add(0, contribute);
        contributeAdatper = new FragmentContribute.ContributeAdatper(getActivity(), R.layout.cus_spinner_contribute, arrayContribute);
        spinner_contribute.setAdapter(contributeAdatper);
        spinner_contribute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onFailure(Call<Contribution> call, Throwable t) {

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public class StateAdatper extends ArrayAdapter<State> {
        List<State> objects;

        public StateAdatper(Context ctx, int txtViewResourceId, List<State> objects) {

            super(ctx, txtViewResourceId, objects);
            this.objects = objects;
        }

        //getting the dropdown of spinner
        @Override
        public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
            return getChildView(position, cnvtView, prnt);
        }

        @Override
        public View getView(int pos, View cnvtView, ViewGroup prnt) {
            return getCustomView(pos, cnvtView, prnt);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            if (getActivity() != null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                mySpinner = inflater.inflate(R.layout.spinner_item_contribute, parent, false);
                TextView city_parent_text = (TextView) mySpinner.findViewById(R.id.tv);
                try {
                    city_parent_text.setText(objects.get(position).getState_name());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return mySpinner;
        }

        public View getChildView(int position, View convertView, ViewGroup parent) {
            if (getActivity() != null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                mySpinner = inflater.inflate(R.layout.spinner_item_edit, parent, false);
                TextView city_child_text = (TextView) mySpinner.findViewById(R.id.tv);
                city_child_text.setText(objects.get(position).getState_name());
            }
            return mySpinner;
        }
    }


    public class ContributeAdatper extends ArrayAdapter<Contribute> {
        List<Contribute> objects;

        public ContributeAdatper(Context ctx, int txtViewResourceId, List<Contribute> objects) {

            super(ctx, txtViewResourceId, objects);
            this.objects = objects;
        }

        //getting the dropdown of spinner
        @Override
        public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
            return getChildView(position, cnvtView, prnt);
        }

        @Override
        public View getView(int pos, View cnvtView, ViewGroup prnt) {
            return getCustomView(pos, cnvtView, prnt);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            if (getActivity() != null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                mySpinner = inflater.inflate(R.layout.spinner_item_contribute, parent, false);
                TextView city_parent_text = (TextView) mySpinner.findViewById(R.id.tv);
                try {
                    city_parent_text.setText(objects.get(position).getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return mySpinner;
        }

        public View getChildView(int position, View convertView, ViewGroup parent) {
            if (getActivity() != null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                mySpinner = inflater.inflate(R.layout.spinner_item_edit, parent, false);
                TextView city_child_text = (TextView) mySpinner.findViewById(R.id.tv);
                city_child_text.setText(objects.get(position).getName());
            }
            return mySpinner;
        }
    }

    public interface ICONTRIBUTE {
        @GET(Constant.WebUrl.CONTRIBUTION)
        Call<Contribution> getContrinbutionData();

        @GET(Constant.WebUrl.CONTRIBUTION_SUBMIT)
        Call<Contribution> getContributionSubmit(@Query("pin") String pin, @Query("state") String state, @Query("objective") String objective);

    }
}