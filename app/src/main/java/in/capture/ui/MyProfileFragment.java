package in.capture.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import in.capture.R;
import in.capture.adapter.RecyclerViewAdapterProfile;
import in.capture.model.PhotographerModel;
import in.capture.utils.Constants;
import in.capture.utils.Parser;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * e
 * to handle interaction events.
 * Use the {@link MyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ImageView coverpic;
    private ImageView profilePic; TextView name, charges; RatingBar rating;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    SharedPreferences sharedPreferences;
    private int GET_FROM_GALLERY = 101;
    private PopupMenu popup;
    private Bitmap coverbitmap;
    private String UPLOAD_URL;
    private int clicked = 0;
    private Bitmap profileBitmap;
    private TextView tvuploadPortfolioPic;
    private Bitmap portfolioBitmap;


    public MyProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyProfileFragment newInstance(String param1, String param2) {
        MyProfileFragment fragment = new MyProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_my_profile, container, false);

        sharedPreferences = getActivity().getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE);

        coverpic = (ImageView) view.findViewById(R.id.coverpic);
        profilePic = (ImageView) view.findViewById(R.id.profilepic);

        name = (TextView) view.findViewById(R.id.name);
        charges = (TextView) view.findViewById(R.id.charges);
        ImageView logout = (ImageView) view.findViewById(R.id.logout);

        tvuploadPortfolioPic = (TextView) view.findViewById(R.id.tvUploadPic);
        tvuploadPortfolioPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked = 3;
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup = new PopupMenu(getActivity(), v);
                MenuInflater menuInflater = popup.getMenuInflater();
                menuInflater.inflate(R.menu.my_profile__menu, popup.getMenu());
                popup.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if(coverbitmap != null)
                            uploadImage("cover", coverbitmap, 50);
                        if(profileBitmap != null)
                            uploadImage("profile", profileBitmap, 40);
                        return true;
                    }
                });

                popup.getMenu().getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();
                        ((SignupFragment.OnClickSignup)getActivity()).gotologin();
                        return true;
                    }
                });

                popup.show();
            }
        });

        makeJsonRequest(Constants.urlGetPhotographer);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv_portfolio);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        rv.setLayoutManager(layoutManager);RecyclerViewAdapterProfile recyclerViewAdapter = new RecyclerViewAdapterProfile(getActivity());
        rv.setAdapter(recyclerViewAdapter);

        coverpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clicked = 1;
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);

            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clicked = 2;
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);

            }
        });

        return view;
    }


    private void makeJsonRequest(String url) {


        final ProgressDialog progressDialog =  new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        url = url +"?email="+ sharedPreferences.getString("email","");
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("response", response.toString());
                        progressDialog.dismiss();
                        PhotographerModel photographerModel = Parser.parse_photographer_data(response);
                        setPhotographerProfile(photographerModel);

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
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

    private void setPhotographerProfile(PhotographerModel photographerModel) {
        Picasso.with(getActivity()).load(Constants.imageBaseUrl+photographerModel.getCoverpic()).into(coverpic);
        Picasso.with(getActivity()).load(Constants.imageBaseUrl+photographerModel.getProfilepic()).into(profilePic);
        name.setText(photographerModel.getName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                if(clicked ==1){
                coverbitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                coverpic.setImageBitmap(coverbitmap);
                }else if (clicked == 2)
                {
                    profileBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                    profilePic.setImageBitmap(profileBitmap);
                }else if (clicked == 3)
                {
                    portfolioBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                    AlertDialog.Builder alertadd = new AlertDialog.Builder(
                           getActivity());
                    LayoutInflater factory = LayoutInflater.from(getActivity());
                    final View view = factory.inflate(R.layout.imagepopup, null);
                    ImageView imageView = (ImageView) view.findViewById(R.id.img);

                    imageView.setImageBitmap(portfolioBitmap);
                    alertadd.setView(view);
                    alertadd.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dlg, int sumthin) {

                            if (portfolioBitmap != null)
                            uploadImage("portfolio", portfolioBitmap, 60);
                        }
                    });

                    alertadd.show();
                }


            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    public String getStringImage(Bitmap bmp , int resize){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, resize, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(final String imageType, final Bitmap bitmap, final int resize){
        //Showing the progress dialog
        final ProgressDialog loading = new ProgressDialog(getActivity());
        loading.setMessage( "Uploading...");
        loading.show();

        if(imageType.equalsIgnoreCase("cover"))
            UPLOAD_URL = Constants.urluploadCover;
        else if(imageType.equalsIgnoreCase("profile"))
            UPLOAD_URL = Constants.urluploadProfile;
        else if (imageType.equalsIgnoreCase("portfolio"))
            UPLOAD_URL = Constants.urluploadPortfolioPic;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(getActivity(), s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(getActivity(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap, resize);

                String name= null;
                //Getting Image Name
                String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                timeStamp = timeStamp.replace(" ","").replace(".","_").trim();

                if (imageType.contains("cover"))
                    name = sharedPreferences.getString("email","").substring(0, sharedPreferences.getString("email","").indexOf(".")) + "coverpic.png";

                else if (imageType.contains("profile"))
                    name = sharedPreferences.getString("email","").substring(0, sharedPreferences.getString("email","").indexOf(".")) + "profilepic.png";


                else if (imageType.contains("portfolio"))
                    name = sharedPreferences.getString("email","").substring(0, sharedPreferences.getString("email","").indexOf(".")) +timeStamp+".png" ;

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("coverpic", name);
                params.put("coverimage", image);
                params.put("email", sharedPreferences.getString("email",""));

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                200000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }




}


