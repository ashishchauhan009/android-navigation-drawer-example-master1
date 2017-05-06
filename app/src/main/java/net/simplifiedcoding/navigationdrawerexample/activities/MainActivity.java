package net.simplifiedcoding.navigationdrawerexample.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import net.simplifiedcoding.navigationdrawerexample.Constant.Constant;
import net.simplifiedcoding.navigationdrawerexample.R;
import net.simplifiedcoding.navigationdrawerexample.fragments.FragmentAddOutreachList;
import net.simplifiedcoding.navigationdrawerexample.fragments.FragmentFavTask;
import net.simplifiedcoding.navigationdrawerexample.fragments.FragmentHomeScreen;
import net.simplifiedcoding.navigationdrawerexample.fragments.FragmentMis;
import net.simplifiedcoding.navigationdrawerexample.fragments.FragmentNewDashBoard;
import net.simplifiedcoding.navigationdrawerexample.fragments.FragmentSearchByDesignation;
import net.simplifiedcoding.navigationdrawerexample.fragments.FragmentSearchTask;
import net.simplifiedcoding.navigationdrawerexample.fragments.FragmentSuccessStoriesList;
import net.simplifiedcoding.navigationdrawerexample.fragments.FragmentTaskDashBoard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout mDrawerLayout;
    Boolean mSlideState = false;
    private FragmentManager fragmentManager;
    private NavigationView navigationView;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    TextView tv_loginusername;
    View headerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar();

        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(new ActionBarDrawerToggle(this,
                mDrawerLayout,
                null,
                0,
                0) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mSlideState = false;//is Closed
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mSlideState = true;//is Opened
            }
        });

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerLayout = navigationView.getHeaderView(0);
        tv_loginusername = (TextView) headerLayout.findViewById(R.id.tv_loginusername);

        //add this line to display menu1 when the activity is loaded

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedpreferences.edit();
//        if (sharedpreferences.getString("TittleName", "") == "") {
        //hideItemlogoutmis(tv_loginusername);
//            Fragment fragment = new FragmentMis().newInstance();
//            replacefragment(fragment);
//        } else {
        //hideItemlogin();
        displaySelectedScreen(R.id.menu_dashboard);
//        }

        //hide home screen

        checkForApplicationUpgrade();
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.menu_home).setVisible(false);

        String user_name = sharedpreferences.getString("TittleName", "");
        if (user_name != null)
            setWelcomeText(user_name);
    }

    private void setActionBar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusbarcolor));
        }
    }

    public void clickEventSlide() {
        if (mSlideState) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    boolean doubleClickToExit = false;

    public void setWelcomeText(String name) {
//    tv_loginusername=(TextView)headerLayout.findViewById(R.id.tv_loginusername);
        tv_loginusername.setText(name);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(Gravity.LEFT)) {
            drawer.closeDrawer(Gravity.LEFT);
        } else {
            if (FragmentTaskDashBoard.newInstance() != null && FragmentTaskDashBoard.newInstance().isVisible()) {
                // if (FragmentHomeScreen.getInstance() != null && FragmentHomeScreen.getInstance().isVisible()) {
                displaySelectedScreen(R.id.menu_dashboard);
            } else {
                // displaySelectedScreen(R.id.menu_loginitduser);
                displaySelectedScreen(R.id.menu_dashboard);
            }
        }
        if (doubleClickToExit) {
            finishAffinity();
            System.exit(0);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleClickToExit = !doubleClickToExit;
            }
        }, 1000);
        doubleClickToExit = !doubleClickToExit;
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

       //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    private void displaySelectedScreen(int itemId) {
        View v;
        Fragment fragment = null;
        fragmentManager = getSupportFragmentManager();
        //initializing the fragment object which is selected
        switch (itemId) {

            case R.id.menu_helpdesk:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.cleanmoney.in/beta-pwc-v/?q=content/helpdesk-0"));
                startActivity(browserIntent);
                break;

            case R.id.menu_home:
                fragment = FragmentHomeScreen.getInstance();
                break;
            case R.id.menu_dashboard:
                fragment = FragmentNewDashBoard.newInstance();
                break;

            case R.id.menu_createtask:
                fragment = new FragmentSearchTask().newInstance();
                break;
            case R.id.menu_deskboard:
                fragment = new FragmentTaskDashBoard().newInstance();
                break;
            case R.id.menu_designation:
                fragment = new FragmentSearchByDesignation().newInstance();
                break;


            case R.id.menu_add_stories:
                fragment = new FragmentSuccessStoriesList().newInstance();
                break;
            case R.id.menu_add_outreach:
                fragment = new FragmentAddOutreachList().newInstance();
                break;
            case R.id.menu_favtask:
                fragment = new FragmentFavTask().newInstance();
                break;
            case R.id.menu_mis:
                fragment = new FragmentMis().newInstance();
                break;
            case R.id.menu_logout:
                if (sharedpreferences.getString("TittleName", "") != "") {
                    editor.clear().commit();
                    Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(this, "Successfully Logout", Toast.LENGTH_SHORT).show();
                }

                break;
            /*case R.id.menu_engage:
                fragment = new FragmentEngage().newInstance();
                break;

            case R.id.menu_support:
                fragment = new FragmentISupport().newInstance();
                break;

            case R.id.menu_contribute:
                fragment = new FragmentContribute().newInstance();
                break;
*/

            default:
                break;
        }

        //replacing the fragment
        replacefragment(fragment);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    public void replacefragment(Fragment fragment) {
        if (fragment != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                fragment.setEnterTransition(new Slide(Gravity.LEFT).setDuration(800));
                fragment.setEnterTransition(new Fade().setDuration(300));
                fragment.setExitTransition(new Fade().setDuration(350));
            }

            String backstackname = fragment.getClass().getName();

//            boolean fragmentpop = fragmentManager.popBackStackImmediate(backstackname, 0);
//            if (!fragmentpop) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(backstackname);
            fragmentTransaction.commit();
//            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        //make this method blank
        return true;
    }

    public void hideItemlogin() {
        if (tv_loginusername != null) {
            tv_loginusername.setVisibility(View.VISIBLE);
        }
        Menu nav_Menu = navigationView.getMenu();
//        nav_Menu.findItem(R.id.menu_loginitduser).setVisible(true);
        nav_Menu.findItem(R.id.menu_logout).setVisible(true);
        nav_Menu.findItem(R.id.menu_mis).setVisible(true);
        nav_Menu.findItem(R.id.menu_createtask).setVisible(true);
//        nav_Menu.findItem(R.id.menu_add_stories).setVisible(true);
        nav_Menu.findItem(R.id.menu_favtask).setVisible(true);
//        nav_Menu.findItem(R.id.menu_edit_stories).setVisible(true);
//        nav_Menu.findItem(R.id.menu_newtask).setVisible(true);
//        nav_Menu.findItem(R.id.menu_pendingtask).setVisible(true);
//        nav_Menu.findItem(R.id.menu_completedtask).setVisible(true);
//        nav_Menu.findItem(R.id.menu_closedtask).setVisible(true);
//        nav_Menu.findItem(R.id.menu_assig_newtask).setVisible(true);
//        nav_Menu.findItem(R.id.menu_readyforupdate).setVisible(true);
        nav_Menu.findItem(R.id.menu_dashboard).setVisible(true);
//        nav_Menu.findItem(R.id.menu_wip).setVisible(true);
//        nav_Menu.findItem(R.id.menu_closed).setVisible(true);
//        nav_Menu.findItem(R.id.menu_add_outreach).setVisible(true);
//        nav_Menu.findItem(R.id.menu_edit_outreach).setVisible(true);
//        nav_Menu.findItem(R.id.menu_engage).setVisible(true);
//        nav_Menu.findItem(R.id.menu_isupport).setVisible(true);
    }

    public void hideItemlogoutmis(TextView tv_loginusername) {
        tv_loginusername.setVisibility(View.GONE);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.menu_mis).setVisible(false);
        nav_Menu.findItem(R.id.menu_logout).setVisible(false);
        nav_Menu.findItem(R.id.menu_createtask).setVisible(false);
//        nav_Menu.findItem(R.id.menu_add_stories).setVisible(false);
        nav_Menu.findItem(R.id.menu_favtask).setVisible(false);
//        nav_Menu.findItem(R.id.menu_newtask).setVisible(false);
//        nav_Menu.findItem(R.id.menu_edit_stories).setVisible(false);
//        nav_Menu.findItem(R.id.menu_pendingtask).setVisible(false);
//        nav_Menu.findItem(R.id.menu_completedtask).setVisible(false);
//        nav_Menu.findItem(R.id.menu_closedtask).setVisible(false);
//        nav_Menu.findItem(R.id.menu_assig_newtask).setVisible(false);
//        nav_Menu.findItem(R.id.menu_readyforupdate).setVisible(false);
        nav_Menu.findItem(R.id.menu_dashboard).setVisible(false);
//        nav_Menu.findItem(R.id.menu_closed).setVisible(false);
//        nav_Menu.findItem(R.id.menu_wip).setVisible(false);
//        nav_Menu.findItem(R.id.menu_add_outreach).setVisible(false);
//        nav_Menu.findItem(R.id.menu_edit_outreach).setVisible(false);
//        nav_Menu.findItem(R.id.menu_engage).setVisible(false);
//        nav_Menu.findItem(R.id.menu_isupport).setVisible(false);
//        nav_Menu.findItem(R.id.menu_loginitduser).setVisible(true);

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

    public void checkForApplicationUpgrade() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constant.WebUrl.APP_INFO)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                try {
                    JSONArray jsonArray = new JSONArray(data);
                    JSONObject object = jsonArray.getJSONObject(0);

                    int appVersion = object.getInt("appVersion");
                    int recommendedVersion = object.getInt("recommendedVersion");
                    int forceUpgradeVersion = object.getInt("forceUpgadeVersion");
                    PackageInfo pInfo = null;
                    try {
                        pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                        String version = pInfo.versionName;
                        int verCode = pInfo.versionCode;

                        // Log.e(TAG, "version: " + version + "\n" + "verCode: " + verCode);
                        if (appVersion > verCode) {
                            //   Log.e(TAG, "you have older version of app");
                            new ReceiverThread().run();
                        } else {
                            //   Log.e(TAG, "Latest Version is :" + appVersion + "\n" + "Current version is:" + verCode);
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class ReceiverThread extends Thread {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (!MainActivity.this.isFinishing())
                        askToUpgrade();
                }
            });
        }
    }

    private void askToUpgrade() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setMessage("It seems you are using older version of CleanMoney. Please upgrade for new features and fixed bug.");
        builder.setNegativeButton("Remind me later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("Upgrade Now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               /* Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.facebook.katana"));
                startActivity(i);*/

                // shareExperienceOnPlayStore();
            }
        });
        builder.setTitle("Upgrade CleanMoney");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.create().show();
    }
}
