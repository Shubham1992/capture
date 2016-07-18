package in.capture.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.capture.R;
import in.capture.utils.Constants;
import in.capture.utils.Utility;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *  interface
 * to handle interaction events.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView gotologin;

    private EditText name, email, password;
    private Button btnSignup;
    private Spinner category;
    private LinearLayout photographerSpecificLayout;
    private CheckBox chkIsPhotographer;
    private EditText rates;
    private EditText location;
    private Spinner state;
    private Spinner category2;

    public SignupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignupFragment newInstance(String param1, String param2) {
        SignupFragment fragment = new SignupFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        name = (EditText) view.findViewById(R.id.input_name);
        email = (EditText) view.findViewById(R.id.input_email);
        password = (EditText) view.findViewById(R.id.input_password);
        photographerSpecificLayout = (LinearLayout)view.findViewById(R.id.photographerExtras);

        category = (Spinner) view.findViewById(R.id.category);
        category2 = (Spinner) view.findViewById(R.id.category2);
        List<String> listCategory = new ArrayList<>();
        listCategory.add("Wedding");
        listCategory.add("Fashion & Portfolio");
        listCategory.add("Commercial");
        listCategory.add("Corporate Events");
        listCategory.add("Special Occassions");
        listCategory.add("Babies & Kids");
        listCategory.add("Travel");

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.simple_spinner_item, listCategory);
        category.setAdapter(arrayAdapter);
        category2.setAdapter(arrayAdapter);

        state = (Spinner) view.findViewById(R.id.state);
        listCategory = new ArrayList<>();
        listCategory.add("NCR Region");
        listCategory.add("Delhi");
        listCategory.add("Ahmedabad");
        listCategory.add("Gurgaon");
        listCategory.add("Mumbai");
        listCategory.add("Benguluru");
        listCategory.add("Pune");
        listCategory.add("Hyderabad");
        listCategory.add("Kolkata");
        listCategory.add("Surat");
        listCategory.add("Chandigarh");
        listCategory.add("Jaipur");
        listCategory.add("Lucknow");
        listCategory.add("Nagpur");
        listCategory.add("Thiruvananthapuram");
        listCategory.add("Goa");
        listCategory.add("Himachal Pradesh");
        listCategory.add("Uttaranchal");
        listCategory.add("West Bengal");
        listCategory.add("Karnataka");
        listCategory.add("Haryana");
        listCategory.add("Uttar Pradesh");

        arrayAdapter = new ArrayAdapter(getActivity(), R.layout.simple_spinner_item, listCategory);
        state.setAdapter(arrayAdapter);


        rates = (EditText)view.findViewById(R.id.input_rate);


        chkIsPhotographer = (CheckBox) view.findViewById(R.id.checkboxIsPhotographer);
        chkIsPhotographer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    photographerSpecificLayout.setVisibility(View.VISIBLE);
                else
                    photographerSpecificLayout.setVisibility(View.GONE);
            }
        });


        btnSignup = (Button) view.findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid())
                {
                    if(chkIsPhotographer.isChecked())
                        signupPhotographer();
                    else
                        signupUser();
                }
            }
        });
        gotologin = (TextView) view.findViewById(R.id.gotologin);
        gotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((OnClickSignup)getActivity()).gotologin();
            }
        });
        return view;
    }

    public interface OnClickSignup
    {
        void gotologin();
        void reload();

    }


    boolean isValid()
    {
        if(name.getText().toString().length() < 2)
            return false;
        if (email.getText().toString().length()<=5 )
            return false;
        if(password.getText().toString().length() < 4)
            return false;
        return true;
    }


    public void signupPhotographer() {
        String POST_URL = "http://captureapp.in/api/register_photographer.php/";

        final ProgressDialog progressDialog =  new ProgressDialog(getActivity());
        progressDialog.setMessage("Signing in");
        progressDialog.show();

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name.getText().toString());
            jsonObject.put("email", email.getText().toString().toLowerCase());
            jsonObject.put("password", password.getText().toString());
            jsonObject.put("category", category.getSelectedItem().toString().toLowerCase().replace(" ","_").replace("&","and")+" , "+category2.getSelectedItem().toString().toLowerCase().replace(" ","_").replace("&","and"));
            jsonObject.put("rates", rates.getText().toString());
            jsonObject.put("location", state.getSelectedItem().toString().toLowerCase().replace(" ", "_"));


        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest stringRequest = new JsonObjectRequest(POST_URL, jsonObject ,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("response", response.toString());
                        progressDialog.dismiss();
                        if(response.optInt("success") == 0)
                           Utility.showToast(getActivity(), "Email already exists");
                        else
                        {
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("email", email.getText().toString().toLowerCase());
                            editor.putString(Constants.USER_TYPE, Constants.PHOTOGRAPHER);
                            editor.commit();

                            ((OnClickSignup)getActivity()).gotologin();
                            ((OnClickSignup)getActivity()).reload();

                        }


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
    public void signupUser() {
        String POST_URL = "http://captureapp.in/api/register_photo_user.php/";

        final ProgressDialog progressDialog =  new ProgressDialog(getActivity());
        progressDialog.setMessage("Signing in");
        progressDialog.show();

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name.getText().toString());
            jsonObject.put("email", email.getText().toString().toLowerCase());
            jsonObject.put("password", password.getText().toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest stringRequest = new JsonObjectRequest(POST_URL, jsonObject ,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("response", response.toString());
                        progressDialog.dismiss();
                        if(response.optInt("success") == 0)
                            Utility.showToast(getActivity(), "Email already exists");
                        else
                        {
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("email", email.getText().toString().toLowerCase());
                            editor.putString(Constants.USER_TYPE, Constants.ENDUSER);
                            editor.commit();

                            ((OnClickSignup)getActivity()).gotologin();
                            ((OnClickSignup)getActivity()).reload();

                        }


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

}
