package net.simplifiedcoding.navigationdrawerexample.fragments;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.simplifiedcoding.navigationdrawerexample.Constant.Constant;
import net.simplifiedcoding.navigationdrawerexample.Model.CampaignModel;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.activities.MainActivity;
import net.simplifiedcoding.navigationdrawerexample.adapter.AdapterCampaign;
import net.simplifiedcoding.navigationdrawerexample.adapter.AdapterNewsListing;
import net.simplifiedcoding.navigationdrawerexample.adapter.Adaptertraininglearning;

import java.util.Locale;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

import static android.content.Context.SEARCH_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentNewsListing.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentNewsListing#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentNewsListing extends Fragment implements Callback<CampaignModel> {

    private static final String TAG = FragmentHomeScreen.class.getSimpleName();
    private RecyclerView recyclerView;
    private TextView tvActionbarTitle1,tvActionbarTitle2;
    private AdapterNewsListing adapterNewsListing;


    private OnFragmentInteractionListener mListener;
    private SearchView searchView;
    private CampaignModel campaignModelData;
    private ProgressDialog pDialog;

    public FragmentNewsListing() {
        // Required empty public constructor
    }


    public static FragmentNewsListing newInstance() {
        FragmentNewsListing fragment = new FragmentNewsListing();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_listing, container, false);
        initView(rootView);
        return rootView;
    }


    void initView(View view){
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setPadding(0, 5, 10, 5);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(null);
        ((MainActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ImageView imageView = (ImageView) view.findViewById(R.id.btn_drawer);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).clickEventSlide();
            }
        });

        pDialog = new ProgressDialog(getContext(), R.style.DialogTheme);
        pDialog.setCancelable(true);
        tvActionbarTitle1 = (TextView) view.findViewById(R.id.tv_actionbar_title1);
        tvActionbarTitle2 = (TextView) view.findViewById(R.id.tv_actionbar_title2);
        tvActionbarTitle2.setVisibility(View.GONE);
        tvActionbarTitle1.setText("News");

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_news_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.setNestedScrollingEnabled(true);

        getCampaignServerData();


    }
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu); // removed to not double the menu items
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                Log.e("check", query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("check", newText);
                String text = newText.toLowerCase(Locale.getDefault());

                    adapterNewsListing.filter(text);

                return true;
            }
        });
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void getCampaignServerData() {
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
        IDATA idata = retrofit.create(IDATA.class);
        Call<CampaignModel> call = idata.getData();
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<CampaignModel> call, Response<CampaignModel> response) {
        campaignModelData = response.body();
        //Log.e(TAG, campaignModelData.toString());
        if (campaignModelData != null) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (Integer.parseInt(campaignModelData.getStatus()) == Constant.StatusCode.CLEANMONEY_CODE_SUCCESS) {

                adapterNewsListing = new AdapterNewsListing(getContext(), campaignModelData);
                recyclerView.setAdapter(adapterNewsListing);
            }
        }
    }

    @Override
    public void onFailure(Call<CampaignModel> call, Throwable t) {

    }
    public interface IDATA {
        @GET(Constant.WebUrl.LATESTNEWS)
        Call<CampaignModel> getData();
    }
}
