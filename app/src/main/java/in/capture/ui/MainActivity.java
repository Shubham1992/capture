package in.capture.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.capture.adapter.ImageAdapter;
import in.capture.R;
import in.capture.adapter.PhotographersListRVAdapter;
import in.capture.model.PhotoModel;
import in.capture.model.PhotographerModel;
import in.capture.utils.Constants;
import in.capture.utils.Parser;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnClickLogin, SignupFragment.OnClickSignup, NavigationView.OnNavigationItemSelectedListener{

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    public  TabLayout tabLayout;
    ImageView imgPgraph, imgphoto, imgUser;
    private GridView gridView;
    private LinearLayout signupfragContainer;
    private SharedPreferences sharedPreferences;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);



        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        gridView = (GridView) findViewById(R.id.grid_view);

        imgPgraph = (ImageView) findViewById(R.id.imgPhotographer);
        imgphoto = (ImageView) findViewById(R.id.imgPictures);
        imgUser = (ImageView) findViewById(R.id.imgUser);

        signupfragContainer = (LinearLayout) findViewById(R.id.signupfragContainer);

        imgPgraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imgPgraph.setAlpha(1.0f);
                imgphoto.setAlpha(0.5f);
                imgUser.setAlpha(0.5f);

                mViewPager.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.GONE);
                signupfragContainer.setVisibility(View.GONE);
            }
        });

        imgphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imgphoto.setAlpha(1.0f);
                imgPgraph.setAlpha(0.5f);
                imgUser.setAlpha(0.5f);

                mViewPager.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                signupfragContainer.setVisibility(View.GONE);
                gridView.setVisibility(View.VISIBLE);

                getAllPhotos();
                // Instance of ImageAdapter Class

            }
        });

        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imgUser.setAlpha(1.0f);
                imgPgraph.setAlpha(0.5f);
                imgphoto.setAlpha(0.5f);

                mViewPager.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                gridView.setVisibility(View.GONE);
                signupfragContainer.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.signupfragContainer, new LoginFragment()).commit();
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(8);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        if(getIntent().getBooleanExtra("gotoLogin", false) == true)
        {
            imgUser.performClick();
        }

    }

    private void getAllPhotos() {

        final ProgressDialog progressDialog =  new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String url = Constants.urlAllPhotos;
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("response", response.toString());
                        progressDialog.dismiss();
                        ArrayList<PhotoModel> photoModels = Parser.parse_all_photos(response);

                        gridView.setAdapter(new ImageAdapter(MainActivity.this, photoModels));
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void gotosignup() {
        getSupportFragmentManager().beginTransaction().replace(R.id.signupfragContainer, new SignupFragment()).commit();
    }

    @Override
    public void loginsuccessful() {
        sharedPreferences = getSharedPreferences(Constants.SHAREDPREF, MODE_PRIVATE);
        String userType = sharedPreferences.getString(Constants.USER_TYPE, Constants.PHOTOGRAPHER);

            if(userType.equalsIgnoreCase(Constants.PHOTOGRAPHER))
                getSupportFragmentManager().beginTransaction().replace(R.id.signupfragContainer, new MyProfileFragment()).commit();
            else
                getSupportFragmentManager().beginTransaction().replace(R.id.signupfragContainer, new EndUserProfileFragment()).commit();
    }

    @Override
    public void gotologin() {
        getSupportFragmentManager().beginTransaction().replace(R.id.signupfragContainer, new LoginFragment()).commit();
    }

    @Override
    public void reload() {

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        if(item.getItemId() == R.id.home)
        {
            imgPgraph.performClick();
        }
        else if(item.getItemId() == R.id.browse)
        {
            imgphoto.performClick();
        }
        else if(item.getItemId() == R.id.profile)
        {
            imgUser.performClick();
        }
        else if(item.getItemId() == R.id.share)
        {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey, checkout this app for photographers. Its awesome http://goo.gl/qBk8NZ");
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share via"));
        }
        else if(item.getItemId() == R.id.rate){
            String url = "http://goo.gl/qBk8NZ";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
        else if(item.getItemId() == R.id.mobile){

            Intent intent = new Intent(MainActivity.this, ContactActivity.class);
            startActivity(intent);
        }

        return true;
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private RecyclerView rv;
        private String url;
        private SwipeRefreshLayout swiperefresh;
        private TextView textViewNoDAta;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            int sectionNo = getArguments().getInt(ARG_SECTION_NUMBER);


            textViewNoDAta = (TextView) rootView.findViewById(R.id.tvnodata);
            swiperefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
            swiperefresh.setOnRefreshListener(this);

            if(sectionNo == 1)
            {
               url = Constants.urlPhotographersList+"?category=wedding";
             }
            if(sectionNo == 2)
            {
                url = Constants.urlPhotographersList+"?category=fashion_and_portfolio";
            }
            if(sectionNo == 3)
            {
                url = Constants.urlPhotographersList+"?category=commercial";
            }
            if(sectionNo == 4)
            {
                url = Constants.urlPhotographersList+"?category=corporate_events";
            }
            if(sectionNo == 5)
            {
                url = Constants.urlPhotographersList+"?category=special_occassions";
            }
            if(sectionNo == 6)
            {
                url = Constants.urlPhotographersList+"?category=babies_and_kids";
            }
            if(sectionNo == 7)
            {
                url = Constants.urlPhotographersList+"?category=travel";
            }
            if(sectionNo == 8)
            {
                url = Constants.urlPhotographersList+"?category=nature";
            }

            final String finalUrl = url;
            if(url !=  null)
                makeJsonRequest(url);


            rv = (RecyclerView) rootView.findViewById(R.id.rv_portfolio);
            rv.setLayoutManager(new LinearLayoutManager(getActivity()));

            return rootView;
        }



        public void makeJsonRequest(String url) {

            swiperefresh.setRefreshing(true);

            textViewNoDAta.setText("Loading...");



            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e("response", response.toString());
                            swiperefresh.setRefreshing(false);

                            if (response.optString("success").equalsIgnoreCase("1")) {
                                ArrayList<PhotographerModel>  photographerModelArrayList = Parser.parse_photographer_list_data(response);

                                textViewNoDAta.setVisibility(View.GONE);
                                rv.setVisibility(View.VISIBLE);
                                PhotographersListRVAdapter recyclerViewAdapter = new PhotographersListRVAdapter(getActivity(), photographerModelArrayList);
                                rv.setAdapter(recyclerViewAdapter);
                            }else {
                                textViewNoDAta.setVisibility(View.VISIBLE);
                                textViewNoDAta.setText("Sorry,no photographers in this category yet");
                                rv.setVisibility(View.GONE);
                            }
                        }




                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json; charset=utf-8");
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);

        }



        @Override
        public void onRefresh() {


            makeJsonRequest(url);
        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 8;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "    WEDDING    ";
                case 1:
                    return "    FASHION & PORTFOLIO    ";
                case 2:
                    return "    COMMERCIAL    ";
                case 3:
                    return "     CORPORATE EVENTS    ";
                case 4:
                    return "     SPECIAL OCCASSIONS    ";
                case 5:
                    return "     BABIES & KIDS    ";
                case 6:
                    return "     TRAVEL    ";
                case 7:
                    return "     NATURE    ";
            }
            return null;
        }
    }
}
