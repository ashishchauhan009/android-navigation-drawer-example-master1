package net.simplifiedcoding.navigationdrawerexample.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import net.simplifiedcoding.navigationdrawerexample.Constant.Constant;
import net.simplifiedcoding.navigationdrawerexample.Model.CampaignModel;
import net.simplifiedcoding.navigationdrawerexample.Model.CommonHomeModel;
import net.simplifiedcoding.navigationdrawerexample.Model.HomeDataModel;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.activities.MainActivity;
import net.simplifiedcoding.navigationdrawerexample.adapter.PBViewPagerAdapter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentHomeScreen.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentHomeScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHomeScreen extends Fragment implements View.OnClickListener, Callback<HomeDataModel> {

    private static final String TAG = FragmentHomeScreen.class.getSimpleName();
    private ViewPager viewPager;
    private CirclePageIndicator titleIndicator;
    private static WeakReference<FragmentHomeScreen> homeFragmentWeakReference;
    private LinearLayout llPledge, llCampaign, llNewsupdates,llVerification,llVision;
    LinearLayout llTweet;
    private NestedScrollView scrollView;

    private TextView tvReport1, tvReport2, tvReport3, tvTraining1, tvTraining2, tvTraining3, tvLnews1, tvLnews2, tvLnews3, tvActivities1,
            tvActivities2, tvActivities3, tvAchievment1, tvAchievment2, tvAchievment3;


    private LinearLayout llContribute;
    private ImageView ivDashboard,ivPledge;
    private OnFragmentInteractionListener mListener;
    private ImageView ivPledgeARight, ivPledgeADown, ivCampaignARight, ivCampaignADown,
            ivNewsARight, ivNewsADown, ivTestimonialsARright,
            ivTestimonialsADown, ivTweetADown,ivTweetARight;

    private List<CommonHomeModel> reportsList, activitiesList, achievmentsList, trainingList, newsupdatesList;
    private TextView tvTitleName;
    private int size = 0;
    private Timer timer;
    //private RelativeLayout llItdLogin;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public FragmentHomeScreen() {
        // Required empty public constructor
    }

    public static FragmentHomeScreen getInstance() {
        if (homeFragmentWeakReference == null) {
            homeFragmentWeakReference = new WeakReference<>(new FragmentHomeScreen());
        }
        return homeFragmentWeakReference.get();
    }

    public static FragmentHomeScreen newInstance() {
        FragmentHomeScreen fragment = new FragmentHomeScreen();
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
        View rootView = inflater.inflate(R.layout.fragment_home_screen, container, false);
        ((MainActivity) getActivity()).setTitle("Home");
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

        //llClickableReports = (LinearLayout) view.findViewById(R.id.ll_clickable_reports);
        llPledge = (LinearLayout) view.findViewById(R.id.ll_pledge);
        llCampaign = (LinearLayout) view.findViewById(R.id.ll_campaign);
        llNewsupdates = (LinearLayout) view.findViewById(R.id.ll_newsupdates);
        //llGallery = (LinearLayout) view.findViewById(R.id.ll_gallery);
        llTweet = (LinearLayout) view.findViewById(R.id.ll_tweets);
        llContribute = (LinearLayout) view.findViewById(R.id.ll_Contribute);
        llVerification = (LinearLayout) view.findViewById(R.id.ll_Verification);
        llVision = (LinearLayout) view.findViewById(R.id.ll_Vision);


        viewPager = (ViewPager) view.findViewById(R.id.viewPagerHome);
        titleIndicator = (CirclePageIndicator) view.findViewById(R.id.page_indicator);
        /*viewPager.setAdapter(new PBViewPagerAdapter(getContext()));
        titleIndicator.setViewPager(viewPager);*/



        ivPledgeARight = (ImageView) view.findViewById(R.id.iv_pledge_arrow_right);
        ivPledgeADown = (ImageView) view.findViewById(R.id.iv_pledge_arrow_down);
        ivCampaignARight = (ImageView) view.findViewById(R.id.iv_campaign_arrow_right);
        ivCampaignADown = (ImageView) view.findViewById(R.id.iv_campaign_arrow_down);
        ivNewsARight = (ImageView) view.findViewById(R.id.iv_latestupdate_arrow_right);
        ivNewsADown = (ImageView) view.findViewById(R.id.iv_latestupdate_arrow_down);

        //ivGalleryADown = (ImageView) view.findViewById(R.id.iv_gallery_arrow_down);
        //ivGalleryARight = (ImageView) view.findViewById(R.id.iv_gallery_arrow_right);
        /*ivTweetADown = (ImageView) view.findViewById(R.id.iv_tweets_arrow_down);
        ivTweetARight = (ImageView) view.findViewById(R.id.iv_tweets_arrow_right);*/
        ivDashboard = (ImageView) view.findViewById(R.id.iv_dashboard);
        //ivGallery = (ImageView) view.findViewById(R.id.iv_gallery);
        ivPledge = (ImageView) view.findViewById(R.id.iv_pledge);
        scrollView = (NestedScrollView) view.findViewById(R.id.scrollView);
        //llItdLogin = (RelativeLayout) view.findViewById(R.id.rl_itdlogin);

        tvTitleName = (TextView) view.findViewById(R.id.tv_actionbar_title);
        tvTitleName.setText("CLEAN MONEY");



        llPledge.setVisibility(View.GONE);
        llCampaign.setVisibility(View.GONE);
        llNewsupdates.setVisibility(View.GONE);
        //llGallery.setVisibility(View.GONE);
        //llTweet.setVisibility(View.GONE);
        //imageView34.setVisibility(View.GONE);


        ivPledgeADown.setVisibility(View.GONE);
        ivCampaignADown.setVisibility(View.GONE);
        ivNewsADown.setVisibility(View.GONE);
        //ivGalleryADown.setVisibility(View.GONE);
        //ivTweetADown.setVisibility(View.GONE);



        //llContribute.setOnClickListener(this);
        llTweet.setOnClickListener(this);
        //llItdLogin.setOnClickListener(this);
        //llVision.setOnClickListener(this);

       final Handler handler = new Handler();

       final Runnable update = new Runnable() {
           int currentPage = 0;
            public void run() {

                if (currentPage == size) {
                 currentPage=0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };


        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);

            }
        }, 100, 5000);


       /* sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedpreferences.edit();
        if(sharedpreferences.getString("TittleName","") == "") {
           llItdLogin.setVisibility(View.VISIBLE);
        }else {
            llItdLogin.setVisibility(View.GONE);
        }*/


        // call to api
        getPromotionalBanner();
        getHomeScreenData();
    }

    @Override
    public void onStop() {
        super.onStop();
        timer.cancel();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    void allclicks()
    {
        ivPledgeARight.setOnClickListener(this);
        ivPledgeADown.setOnClickListener(this);
        ivCampaignARight.setOnClickListener(this);
        ivCampaignADown.setOnClickListener(this);
        ivNewsARight.setOnClickListener(this);
        ivNewsADown.setOnClickListener(this);
        //ivGalleryARight.setOnClickListener(this);
        //ivGalleryADown.setOnClickListener(this);
//        ivTweetADown.setOnClickListener(this);
//        ivTweetARight.setOnClickListener(this);
    }

   /* public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu); // removed to not double the menu items

    }*/


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_pledge_arrow_right:
                llPledge.setVisibility(View.VISIBLE);
                ivPledgeADown.setVisibility(View.VISIBLE);
                ivPledgeARight.setVisibility(View.GONE);
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(NestedScrollView.FOCUS_DOWN);
                    }
                });
                break;
            case R.id.iv_pledge_arrow_down:
                llPledge.setVisibility(View.GONE);
                ivPledgeADown.setVisibility(View.GONE);
                ivPledgeARight.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_campaign_arrow_right:
                llCampaign.setVisibility(View.VISIBLE);
                ivCampaignADown.setVisibility(View.VISIBLE);
                ivCampaignARight.setVisibility(View.GONE);
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(NestedScrollView.FOCUS_DOWN);
                    }
                });
                break;
            case R.id.iv_campaign_arrow_down:
                llCampaign.setVisibility(View.GONE);
                ivCampaignADown.setVisibility(View.GONE);
                ivCampaignARight.setVisibility(View.VISIBLE);

                break;
            /*case R.id.iv_gallery_arrow_right:
                llGallery.setVisibility(View.VISIBLE);
                ivGalleryADown.setVisibility(View.VISIBLE);
                ivGalleryARight.setVisibility(View.GONE);
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(NestedScrollView.FOCUS_DOWN);
                    }
                });
                break;
            case R.id.iv_gallery_arrow_down:
                llGallery.setVisibility(View.GONE);
                ivGalleryADown.setVisibility(View.GONE);
                ivGalleryARight.setVisibility(View.VISIBLE);
                break;*/
            /*case R.id.iv_tweets_arrow_right:
                llTweet.setVisibility(View.VISIBLE);
                ivTweetADown.setVisibility(View.VISIBLE);
                ivTweetARight.setVisibility(View.GONE);
                mWebView.getSettings().setDomStorageEnabled(true);
                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.loadDataWithBaseURL(baseURl, widgetInfo, "text/html", "UTF-8", null);
                break;
            case R.id.iv_tweets_arrow_down:
                //llTweet.setVisibility(View.GONE);
                ivTweetADown.setVisibility(View.GONE);
                ivTweetARight.setVisibility(View.VISIBLE);
                break;*/
            case R.id.iv_latestupdate_arrow_right:
                llNewsupdates.setVisibility(View.VISIBLE);
                ivNewsADown.setVisibility(View.VISIBLE);
                ivNewsARight.setVisibility(View.GONE);
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(NestedScrollView.FOCUS_DOWN);
                    }
                });
                break;
            case R.id.iv_latestupdate_arrow_down:
                llNewsupdates.setVisibility(View.GONE);
                ivNewsADown.setVisibility(View.GONE);
                ivNewsARight.setVisibility(View.VISIBLE);
                break;

           /* case R.id.rl_itdlogin:
                Fragment fragment1 = new FragmentLoginITD();
                ((MainActivity) getContext()).replacefragment(fragment1);
                break;
*/
            case R.id.ll_Contribute:

                break;
            case R.id.ll_Verification:
                Fragment fragment = new FragmentAboutEverification();
                ((MainActivity) getContext()).replacefragment(fragment);
                break;
           /* case R.id.ll_pmgky:
                Fragment fragment1 = new FragmentAboutPMGKY();
                ((MainActivity) getContext()).replacefragment(fragment1);
                break;*/
            case R.id.ll_Vision:
                Fragment fragment2 = new FragmentVision();
                ((MainActivity) getContext()).replacefragment(fragment2);
                break;
            case R.id.ll_tweets:
                Fragment fragment3 = new FragmentTweets();
                ((MainActivity) getContext()).replacefragment(fragment3);
                break;

        }
    }

    private void getHomeScreenData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.WebUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        IHOMESCREENDATA ihomescreendata = retrofit.create(IHOMESCREENDATA.class);
        Call<HomeDataModel> call = ihomescreendata.getData();
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<HomeDataModel> call, Response<HomeDataModel> response) {
        HomeDataModel homeDataModel = response.body();

        //Log.e(TAG, homeDataModel.toString());
        if (homeDataModel != null) {
            if (Integer.parseInt(homeDataModel.getStatus()) == Constant.StatusCode.CLEANMONEY_CODE_SUCCESS) {
                setAllContents(homeDataModel);
                allclicks();
            }

        }
    }

    @Override
    public void onFailure(Call<HomeDataModel> call, Throwable t) {

    }

    void setAllContents(final HomeDataModel homeDataModel) {
        LayoutInflater layoutInflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        /*if (homeDataModel.getReport() != null) {
            for (int i = 0; i < homeDataModel.getReport().size(); i++) {
                final int pos = i;
                View view = layoutInflater.inflate(R.layout.dynamic_view, llReports, false);
                if (homeDataModel.getReport().get(i).getTitle().length() > 0) {
                    ((TextView) view.findViewById(R.id.tv_reports1)).setText(homeDataModel.getReport().get(i).getTitle());
                    if (homeDataModel.getReport().get(i).getField_url().length() > 1)
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(homeDataModel.getReport().get(pos).getField_url()));
                            startActivity(browserIntent);
                        }
                    });
                    llReports.addView(view);
                }
            }
        }*/
        if (homeDataModel.getDashboard() != null && homeDataModel.getDashboard().size() != 0) {
           /* if (android.os.Build.VERSION.SDK_INT > 8)
            {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            URL imageUrl = null;
            Bitmap imageBitmap = null;
            try {

                String campaignImageUrl = homeDataModel.getDashboard().get(0).getImage();

                imageUrl = new URL(campaignImageUrl);
                imageBitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
            }catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            ivDashboard.setImageBitmap(imageBitmap);
            ivDashboard.setAdjustViewBounds(true);*/
            Picasso.with(this.getContext())
                    .load(homeDataModel.getDashboard().get(0).getImage())
                    .placeholder(R.drawable.ic_no_image)
                    .resize(0, 500)
                    .into(ivDashboard);
        }

       /* if ("https://www.cleanmoney.in/beta-pwc-v/images/gallery.jpg" !=null) {
            if (android.os.Build.VERSION.SDK_INT > 8)
            {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            URL imageUrl = null;
            Bitmap imageBitmap = null;
            try {

                String campaignImageUrl = "https://www.cleanmoney.in/beta-pwc-v/images/gallery.jpg";

                imageUrl = new URL(campaignImageUrl);
                imageBitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
            }catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            ivGallery.setImageBitmap(imageBitmap);
            ivGallery.setAdjustViewBounds(true);
        }*/
        if ("https://www.cleanmoney.in/beta-pwc-v/images/take-a-pledge.jpg" !=null) {
            if (android.os.Build.VERSION.SDK_INT > 8)
            {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            URL imageUrl = null;
            Bitmap imageBitmap = null;
            try {

                String campaignImageUrl = "https://www.cleanmoney.in/beta-pwc-v/images/take-a-pledge.jpg";

                imageUrl = new URL(campaignImageUrl);
                imageBitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
            }catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            ivPledge.setImageBitmap(imageBitmap);
            ivPledge.setAdjustViewBounds(true);
        }
        if (homeDataModel.getNews() != null) {

            for (int i = 0; i < homeDataModel.getNews().size(); i++) {
                final int pos = i;
                View view = layoutInflater.inflate(R.layout.dynamic_view, llNewsupdates, false);
                if(homeDataModel.getNews().get(i).getTitle().length()>0) {
                    ((TextView) view.findViewById(R.id.tv_reports1)).setText(homeDataModel.getNews().get(i).getTitle());
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                               /* Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(homeDataModel.getNews().get(pos).getField_url()));
                                startActivity(browserIntent);*/
                                Fragment fragment = new FragmentNewsListing();
                                ((MainActivity) getContext()).replacefragment(fragment);
                            }
                        });
                    llNewsupdates.addView(view);
                }
            }
        }
        if (homeDataModel.getCampaign() != null) {

            for (int i = 0; i < homeDataModel.getCampaign().size(); i++) {
                final int pos = i;
                View view = layoutInflater.inflate(R.layout.dynamic_view, llCampaign, false);
                if(homeDataModel.getCampaign().get(i).getTitle().length()>0) {
                    ((TextView) view.findViewById(R.id.tv_reports1)).setText(homeDataModel.getCampaign().get(i).getTitle());
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                               /* Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(homeDataModel.getNews().get(pos).getField_url()));
                                startActivity(browserIntent);*/
                            Fragment fragment = new FragmentCampaign();
                            ((MainActivity) getContext()).replacefragment(fragment);
                        }
                    });
                    llCampaign.addView(view);
                }
            }
        }
    }
       /* if (homeDataModel.getTraining() != null) {

            for (int i = 0; i < homeDataModel.getTraining().size(); i++) {
                final int pos = i;
                View view = layoutInflater.inflate(R.layout.dynamic_view, llTraining, false);
                if (homeDataModel.getNews().get(i).getTitle().length() > 0) {
                    ((TextView) view.findViewById(R.id.tv_reports1)).setText(homeDataModel.getTraining().get(i).getTitle());
                    if (homeDataModel.getTraining().get(i).getField_url().length() > 1)
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(homeDataModel.getTraining().get(pos).getField_url()));
                                startActivity(browserIntent);
                            }
                        });
                    llTraining.addView(view);
                }
            }
        }
        if (homeDataModel.getActivities() != null) {

            for (int i = 0; i < homeDataModel.getActivities().size(); i++) {
                final int pos = i;
                View view = layoutInflater.inflate(R.layout.dynamic_view, llActivities, false);
                if (homeDataModel.getActivities().get(i).getTitle().length() > 0) {
                    ((TextView) view.findViewById(R.id.tv_reports1)).setText(homeDataModel.getActivities().get(i).getTitle());

                    if (homeDataModel.getActivities().get(i).getField_url().length() > 1)
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(homeDataModel.getActivities().get(pos).getField_url()));
                                startActivity(browserIntent);
                            }
                        });
                    llActivities.addView(view);
                }
            }
        }
        if (homeDataModel.getTestimonial() != null) {

            for (int i = 0; i < homeDataModel.getTestimonial().size(); i++) {
                View view = layoutInflater.inflate(R.layout.dynamic_view, llTestimonials, false);
                ((TextView) view.findViewById(R.id.tv_reports1)).setText(homeDataModel.getTestimonial().get(i).getBody());

                llTestimonials.addView(view);
            }
        }


    }*/
    private void getPromotionalBanner() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.WebUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        IBanner iBanner = retrofit.create(IBanner.class);


            Call<CampaignModel> call = iBanner.getBanner();
            call.enqueue(new Callback<CampaignModel>() {
                @Override
                public void onResponse(Call<CampaignModel> call, Response<CampaignModel> response) {
                    CampaignModel banners = response.body();

                    if (banners != null && banners.getData()!=null) {
                        Log.e(TAG, response.body().toString());
                        viewPager.setAdapter(new PBViewPagerAdapter(getContext(), banners));
                        titleIndicator.setViewPager(viewPager);

                        size = banners.getData().size();
                    }
                }

                @Override
                public void onFailure(Call<CampaignModel> call, Throwable t) {
                    Log.e(TAG, t.getMessage() + "" + t.getCause());
                }
            });
    }

    public interface IHOMESCREENDATA {
        @GET(Constant.WebUrl.HOME_SCREEN)
        Call<HomeDataModel> getData();
    }
    public interface IBanner {
        @GET(Constant.WebUrl.BANNER)
        Call<CampaignModel> getBanner();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
