package net.simplifiedcoding.navigationdrawerexample.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.simplifiedcoding.navigationdrawerexample.Model.CampaignCommon;
import net.simplifiedcoding.navigationdrawerexample.Model.CampaignModel;
import net.simplifiedcoding.navigationdrawerexample.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * Created by vibes on 27/2/17.
 */

public class PBViewPagerAdapter extends PagerAdapter implements View.OnClickListener {
    //private static final String TAG = PBViewPagerAdapter.class.getSimpleName();

    private Context context;
    private List<CampaignCommon> bannerList = Collections.EMPTY_LIST;

    public PBViewPagerAdapter(Context context , CampaignModel promotionalBanners) {
        this.context = context;
        if (bannerList == null || bannerList.isEmpty()) {
            this.bannerList = promotionalBanners.getData();
        }

    }


    @Override
    public int getCount() {
        if (bannerList == null) {
            return 0;
        } else {
            return bannerList.size();
        }
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.layout_promotional_banner, container, false);
        if (bannerList.get(position).getImage() != null) {
            Picasso.with(context).load(bannerList.get(position).getImage()).into(((ImageView) layout.findViewById(R.id.iv_banner_image)));
        }
        /*((TextView) layout.findViewById(R.id.tv_banner_header)).setText(bannerList.get(position).getBannerHeader());
        ((TextView) layout.findViewById(R.id.tv_banner_description)).setText(bannerList.get(position).getBannerDescription());

        ((TextView) layout.findViewById(R.id.tv_banner_description)).setText(bannerList.get(position).getBannerDescription());*/
       /* if (bannerList.get(position).getImage() != null) {
           // Picasso.with(context).load(bannerList.get(position).getImage()).into(((ImageView) layout.findViewById(R.id.iv_banner_image)));
            if (android.os.Build.VERSION.SDK_INT > 8)
            {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            URL imageUrl = null;
            Bitmap imageBitmap = null;
            try {

                String campaignImageUrl = bannerList.get(position).getImage();

                imageUrl = new URL(campaignImageUrl);
                imageBitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
            }catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            ((ImageView) layout.findViewById(R.id.iv_banner_image)).setImageBitmap(imageBitmap);
            ((ImageView) layout.findViewById(R.id.iv_banner_image)).setAdjustViewBounds(true);
        }*/
        layout.setOnClickListener(this);
        layout.setTag(bannerList.get(position).getImage());
        container.addView(layout);
        return layout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }



    @Override
    public void onClick(View v) {
        //Toast.makeText(context, "" + v.getTag(), Toast.LENGTH_SHORT).show();
    }
}
