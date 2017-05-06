package net.simplifiedcoding.navigationdrawerexample.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.simplifiedcoding.navigationdrawerexample.Constant.Constant;
import net.simplifiedcoding.navigationdrawerexample.Model.MisLevel;
import net.simplifiedcoding.navigationdrawerexample.Model.Search;
import net.simplifiedcoding.navigationdrawerexample.Model.UserSearch;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.activities.MainActivity;
import net.simplifiedcoding.navigationdrawerexample.adapter.AdapterSearchList;
import net.simplifiedcoding.navigationdrawerexample.util.AndroidUtil;
import net.simplifiedcoding.navigationdrawerexample.util.EndlessRecyclerViewScrollListener;

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

public class FragmentSearchTask extends Fragment implements Callback<UserSearch>, View.OnClickListener, AdapterSearchList.ClickListener {

    private static final String TAG = FragmentSearchTask.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private TextView tvTitleName, addtofav_tv;
    private RecyclerView recyclerView;
    private AdapterSearchList adapterSearchList;
    private ProgressDialog pDialog;
    private UserSearch userSearch;
    private List<Search> searchResultData;
    private RadioGroup radioGroup;
    // private RadioButton radioN, radioUI, radioD, radioL;
    private AppCompatButton btnSearch;
    TextView tv_NORecord;
    private String type = "", value = "";
    private AppCompatEditText etSearch;
    private LinearLayout llHeader;
    // Spinner level_spinner;
    SharedPreferences sharedpreferences;
    String user_id;
    View mySpinner;
    MISLevelAdapter MISLevelAdapter;
    List<MisLevel.Datum> leveldataList = new ArrayList<>();
    String level = "";
    int checkedId;
    int checkedstate;
    boolean flagstop = true;
    private SwipeRefreshLayout swipeRefreshLayout;
    boolean flagsearch = false;

    public FragmentSearchTask() {
        // Required empty public constructor
    }

    public static FragmentSearchTask newInstance() {
        FragmentSearchTask fragment = new FragmentSearchTask();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_searchtask, container, false);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        initView(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    void initView(View view) {

        tv_NORecord = (TextView) view.findViewById(R.id.tv_NORecord);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        getFragmentManager().popBackStackImmediate();
                        return true;
                    }
                }
                return false;
            }
        });

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

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_userlist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.e(TAG, "Page: " + page + " Count: " + totalItemsCount);
                if (etSearch.getText().toString() != null)
                    if (flagsearch == true) {
                        getUserlistData("name", etSearch.getText().toString(), page + 1);
                    }
            }

            @Override
            public void onScrolled(RecyclerView view, int dx, int dy) {
                super.onScrolled(view, dx, dy);
                flagsearch = true;
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                flagsearch = true;
            }
        });

        adapterSearchList = new AdapterSearchList(getContext(), null);
        recyclerView.setAdapter(adapterSearchList);
        adapterSearchList.setOnItemClickListener(FragmentSearchTask.this);


        pDialog = new ProgressDialog(getContext(), R.style.DialogTheme);
        pDialog.setCancelable(true);
        tvTitleName = (TextView) view.findViewById(R.id.task_name_tv);

        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);

        btnSearch = (AppCompatButton) view.findViewById(R.id.btn_search);
        etSearch = (AppCompatEditText) view.findViewById(R.id.et_search);
        llHeader = (LinearLayout) view.findViewById(R.id.ll_header);
        //llListHeader = (LinearLayout) view.findViewById(R.id.ll_list_header);
        //level_spinner = (Spinner) view.findViewById(R.id.level_spinner);

        tvTitleName.setText(R.string.searchby_name);

        btnSearch.setOnClickListener(this);
        if (sharedpreferences.getString("User_id", "") != null) {
            user_id = sharedpreferences.getString("User_id", "");
        }

        if (AndroidUtil.isConnectingToInternet(getActivity())) {
            //MislevelApiCall();
        } else {
            Toast.makeText(getActivity(), Constant.check_internet_connection, Toast.LENGTH_SHORT).show();
        }
//        if (AndroidUtil.isConnectingToInternet(getActivity())) {
//            getUserlistData("all", "all");
//        } else {
//            Toast.makeText(getActivity(), Constant.check_internet_connection, Toast.LENGTH_SHORT).show();
//        }
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
            case R.id.btn_search:
                if (etSearch.getText().length() != 0) {
                    value = etSearch.getText().toString();
                    flagstop = true;
                }
                if (type.equalsIgnoreCase("Name") && etSearch.getText().length() == 0) {
                    Toast.makeText(getActivity(), Constant.check_name, Toast.LENGTH_SHORT).show();
                } else if (type.equalsIgnoreCase("Email") && etSearch.getText().length() == 0) {
                    Toast.makeText(getActivity(), Constant.check_email, Toast.LENGTH_SHORT).show();
                } else if (type.equalsIgnoreCase("Designation") && etSearch.getText().length() == 0) {
                    Toast.makeText(getActivity(), Constant.check_designation, Toast.LENGTH_SHORT).show();
                } else {
                    if (AndroidUtil.isConnectingToInternet(getActivity())) {
                        adapterSearchList.clearAllData();
                        getUserlistData("name", etSearch.getText().toString(), 1);
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(btnSearch.getWindowToken(), 0);
                    } else {
                        Toast.makeText(getActivity(), Constant.check_internet_connection, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.addtofav_tv:
                int pos = (int) v.getTag();
                String taskId = searchResultData.get(pos).getUser_id();
                if (searchResultData.get(pos).getFav().equalsIgnoreCase("1")) {
                    Toast.makeText(getContext(), "Already added as favorite", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (AndroidUtil.isConnectingToInternet(getContext())) {
                        addToFav(taskId, pos);
                    } else {
                        Toast.makeText(getContext(), Constant.check_internet_connection, Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    protected void showDialouge(final int position, final List<Search> userList) {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(getContext()) {
            @Override
            public void onStart() {
                super.onStart();
                Dialog dialog = this;
                if (dialog != null) {
                    int width = ViewGroup.LayoutParams.MATCH_PARENT;
                    int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    dialog.getWindow().setLayout(width, height);
                }
            }
        };
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_user_information);
        Button btn_action;
        ImageView btn_cancel;
        TextView tv_name, tv_email, tv_personal_email, tv_call, tv_designation, tv_level;
        btn_action = (Button) dialog.findViewById(R.id.btn_action);
        btn_cancel = (ImageView) dialog.findViewById(R.id.btn_cancel);
        tv_name = (TextView) dialog.findViewById(R.id.tv_name);
        tv_email = (TextView) dialog.findViewById(R.id.tv_email);
        tv_personal_email = (TextView) dialog.findViewById(R.id.tv_personal_email);
        tv_call = (TextView) dialog.findViewById(R.id.tv_call);
        tv_designation = (TextView) dialog.findViewById(R.id.tv_designation);
        tv_level = (TextView) dialog.findViewById(R.id.tv_level);
        addtofav_tv = (TextView) dialog.findViewById(R.id.addtofav_tv);
        tv_level.setVisibility(View.GONE);
        if (userList.get(position).getShowicon() == 0) {
            addtofav_tv.setVisibility(View.VISIBLE);
            btn_action.setVisibility(View.GONE);
        }

        addtofav_tv.setTag(position);
        btn_action.setTag(position);
        btn_action.setOnClickListener(this);
        addtofav_tv.setOnClickListener(this);
        /*btn_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("email", userSearch.getData().get(position).getUser_email());
                bundle.putString("id", userSearch.getData().get(position).getUser_id());
                Fragment fragment = new FragmentCreateTask();
                fragment.setArguments(bundle);
                ((MainActivity) getContext()).replacefragment(fragment);
            }
        });
*/


            /*tv_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    new AlertDialog.Builder(getContext())
                            .setMessage(getContext().getString(R.string.confirmation_dialog))
                            .setIcon(AndroidUtil.setTint(getContext().getResources().getDrawable(R.mipmap.ic_launcher_call), R.color.green))
                            .setMessage(getContext().getString(R.string.do_you_want_to_make_call))
                            .setPositiveButton(getContext().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (userSearch.getData().get(position).getUser_phone() != null&&!userSearch.getData().get(position).getUser_phone().isEmpty())
                                        actionCall(v, userSearch.getData().get(position).getUser_phone());
                                    //make call from phone
                                }
                            })
                            .setNegativeButton(getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();

                }
            });*/

        if (userList.get(position).getUser_name() != null && !userList.get(position).getUser_name().isEmpty()) {
            tv_name.setText(" " + userList.get(position).getUser_name());
        }
        if (userList.get(position).getUser_email() != null && !userList.get(position).getUser_email().isEmpty()) {
            tv_email.setText(" " + userList.get(position).getUser_email());
        }
        //Added by Mahendra 15-04-2017
        if (userList.get(position).getPersonal_email() != null && !userList.get(position).getPersonal_email().isEmpty()) {
            tv_personal_email.setText(" " + userList.get(position).getPersonal_email());
        } else {
            tv_personal_email.setVisibility(View.GONE);
        }
        if (userList.get(position).getUser_phone() != null && !userList.get(position).getUser_phone().isEmpty()) {
            tv_call.setText(" " + userList.get(position).getUser_phone());
        }
        if (userList.get(position).getUser_designation() != null && !userList.get(position).getUser_designation().isEmpty()) {
            tv_designation.setText(" " + userList.get(position).getUser_designation());
        }
        /*  if (userSearch.getData().get(position).getLevel()!=null && !userSearch.getData().get(position).getLevel().isEmpty()) {
            tv_level.setText(" "+userSearch.getData().get(position).getLevel());
        }*/
        if (userList.get(position).getFav().equalsIgnoreCase("1")) {
            Drawable img = getContext().getResources().getDrawable(R.mipmap.icon_colorstar);
            addtofav_tv.setCompoundDrawablesWithIntrinsicBounds(null, img, null, null);
            addtofav_tv.setText("Added to Favourites");

        } else {
            Drawable img = getContext().getResources().getDrawable(R.mipmap.icon_blnkstar);
            addtofav_tv.setCompoundDrawablesWithIntrinsicBounds(null, img, null, null);
            addtofav_tv.setText("Add to Favourite");
        }
        btn_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("email", userList.get(position).getUser_email());
                bundle.putString("id", userList.get(position).getUser_id());
                Fragment fragment = new FragmentCreateTask();
                fragment.setArguments(bundle);
                ((MainActivity) getContext()).replacefragment(fragment);
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void addToFav(String taskid, final int pos) {

        pDialog = new ProgressDialog(getContext(), R.style.DialogTheme);
        pDialog.setCancelable(true);
        if (pDialog != null) {
            pDialog.setMessage(getContext().getString(R.string.loading));
            if (!pDialog.isShowing())
                pDialog.show();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.WebUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        IUSERLIST1 iuserlist = retrofit.create(IUSERLIST1.class);
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
                            changeDrawable();
                            searchResultData.get(pos).setFav("1");

                        } else {
                            Toast.makeText(getContext(), jsonObject.getString("tag") + "", Toast.LENGTH_LONG).show();
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

    private void changeDrawable() {
        Drawable img = getContext().getResources().getDrawable(R.mipmap.icon_colorstar);
        addtofav_tv.setCompoundDrawablesWithIntrinsicBounds(null, img, null, null);
    }

    private void retainDrawable() {
        Drawable img = getContext().getResources().getDrawable(R.mipmap.icon_blnkstar);
        addtofav_tv.setCompoundDrawablesWithIntrinsicBounds(null, img, null, null);
    }

    @Override
    public void onItemClick(int position, View v, List<Search> userList) {

        showDialouge(position, userList);
    }

   /* @Override
    public void onRefresh() {
        adapterSearchList.clearAllData();
        getUserlistData("name", value,1);
    }*/


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void getUserlistData(String type, String value, int page) {
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
        IUSERLIST iuserlist = retrofit.create(IUSERLIST.class);
        Call<UserSearch> call = iuserlist.getData(type, value, user_id, page + "");
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<UserSearch> call, Response<UserSearch> response) {
        userSearch = response.body();
        //Log.e(TAG,faqModelData.toString());
        if (userSearch != null) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (Integer.parseInt(userSearch.getStatus()) == Constant.StatusCode.CLEANMONEY_CODE_SUCCESS) {

                searchResultData = userSearch.getData();
                if (searchResultData != null && isVisible() && getContext() != null) {
                    tv_NORecord.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    Log.e(TAG + "check ", searchResultData.toString() + "");
                    for (Search coach : searchResultData) {
                        adapterSearchList.addItem(coach);
                    }
                    flagsearch = false;
                    flagstop = false;
                    adapterSearchList.notifyDataSetChanged();

                    /*if (userSearch.getData().size() <= 0 || userSearch.getData().isEmpty()) {
                        flagsearch = true;
                        adapterSearchList.notifyDataSetChanged();

                        tv_NORecord.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        tv_NORecord.setText("No Result Found");
                    }*/

                } /*else if (userSearch.getStatus().contains("0")) {

                    //flagsearch = true;

                    if(flagstop){
                        tv_NORecord.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        tv_NORecord.setText("No Result Found");
//                        tv_NORecord.setText(userSearch.get"");
                    }


                }*/
            } else if (userSearch.getStatus().contains("0")) {
                if (flagstop) {
                    tv_NORecord.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    tv_NORecord.setText("No Result Found");
//                        tv_NORecord.setText(userSearch.get"");
                }
            }
        }
    }

    @Override
    public void onFailure(Call<UserSearch> call, Throwable t) {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


   /* private void MislevelApiCall() {

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
                .client(new OkHttpClient().newBuilder().writeTimeout(1, TimeUnit.MINUTES).connectTimeout(1, TimeUnit.MINUTES).readTimeout(1, TimeUnit.MINUTES).build())
                .build();
        IUSERLIST icreatetask = retrofit.create(IUSERLIST.class);

        Call<MisLevel> submitTask = icreatetask.getData(user_id);

        submitTask.enqueue(new Callback<MisLevel>() {
            @Override
            public void onResponse(Call<MisLevel> call, Response<MisLevel> response) {
                if (pDialog != null) {
                    pDialog.dismiss();
                }
                try {
                    MisLevel misLevel = response.body();
                    if (misLevel != null) {

                        if (Integer.parseInt(misLevel.getStatus()) == Constant.StatusCode.CLEANMONEY_CODE_SUCCESS) {


                            leveldataList = misLevel.getData();
                            MisLevel.Datum datum = new MisLevel().new Datum();
                            datum.setName("Select Level");
                            leveldataList.add(0, datum);
                            MISLevelAdapter = new MISLevelAdapter(getActivity(), R.layout.cus_spinner, leveldataList);
                            level_spinner.setAdapter(MISLevelAdapter);


                            level_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                    if (position == 0) {
//                                        type="";
                                        return;
                                    }
                                    etSearch.setText("");
                                    RadioButton rb = (RadioButton) radioGroup.findViewById(checkedId);
                                    if (rb != null && checkedId > -1) {
                                        if (rb.isChecked()) {
                                            rb.setChecked(false);
                                        }
                                    }


                                    type = "level";

                                    level = leveldataList.get(position).getId();
                                    value = level;

//                                    radioGroup.clearCheck();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });

                        } else {

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onFailure(Call<MisLevel> call, Throwable t) {
                if (pDialog != null) {
                    pDialog.dismiss();
                }

            }
        });
    }*/


    public interface IUSERLIST {
        @GET(Constant.WebUrl.USERLIST)
        Call<UserSearch> getData(@Query("type") String type, @Query("value") String value, @Query("id") String id, @Query("page") String page);

        @GET(Constant.WebUrl.MIS_LEVEL)
        Call<MisLevel> getData(@Query("id") String id);
    }

    public interface IUSERLIST1 {
        @GET(Constant.WebUrl.ADD_TO_FAV_URL)
        Call<ResponseBody> getData(@Query("for") String type, @Query("id") String id);
    }

    // adapter for the member mislevel spinner
    public class MISLevelAdapter extends ArrayAdapter<MisLevel.Datum> {
        List<MisLevel.Datum> data;

        public MISLevelAdapter(Context ctx, int txtViewResourceId, List<MisLevel.Datum> data) {

            super(ctx, txtViewResourceId, data);
            this.data = data;
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

            checkedstate = position;
            if (getActivity() != null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                mySpinner = inflater.inflate(R.layout.spinner_item, parent, false);
                TextView city_parent_text = (TextView) mySpinner.findViewById(R.id.tv);
                try {
//                    if (checkedstate!=position) {
                    city_parent_text.setText(data.get(position).getName());
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                if (position == 0) {
//                    city_parent_text.setTextColor(getResources().getColor(R.color.edit_text_color));
//                } else {
                city_parent_text.setTextSize(13);
                city_parent_text.setTextColor(getResources().getColor(R.color.textviewcolor));
//                }
            }
            return mySpinner;
        }

        public View getChildView(int position, View convertView, ViewGroup parent) {


            if (getActivity() != null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                mySpinner = inflater.inflate(R.layout.spinner_item_edit, parent, false);
                TextView city_child_text = (TextView) mySpinner.findViewById(R.id.tv);
                city_child_text.setTextSize(13);

                city_child_text.setText(data.get(position).getName());
                city_child_text.setTextColor(getResources().getColor(R.color.textviewcolor));
            }
            return mySpinner;
        }

    }

}