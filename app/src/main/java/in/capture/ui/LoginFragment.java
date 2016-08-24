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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.capture.R;
import in.capture.utils.Constants;
import in.capture.utils.Utility;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    SharedPreferences sharedPreferences;
    private EditText email, password;
    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        sharedPreferences = getActivity().getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE);

        if(!sharedPreferences.getString("email","").equalsIgnoreCase(""))
        {
            ((OnClickLogin) getActivity()).loginsuccessful();
        }
        email = (EditText) view.findViewById(R.id.input_email);
        password = (EditText) view.findViewById(R.id.input_password);
        final CheckBox checkBoxPhotographer = (CheckBox) view.findViewById(R.id.checkboxIsPhotographer);

        final Button btnlogin = (Button) view.findViewById(R.id.btnLogin);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(email.getText().toString().length()>=5 && password.getText().length()>3)
                {
                    if(checkBoxPhotographer.isChecked())
                        login();
                    else
                        loginUser();
                }
            }
        });
        TextView gotosignup = (TextView)view.findViewById(R.id.gotosignup);
        gotosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnClickLogin)getActivity()).gotosignup();
            }
        });

        return view;
    }


    public interface OnClickLogin
    {
        void gotosignup();
        void loginsuccessful();

    }



    public void login() {
        String GET_URL = "http://captureapp.in/api/login_photographer.php?email="+email.getText().toString().toLowerCase()+"&password="+password.getText().toString();

        final ProgressDialog progressDialog =  new ProgressDialog(getActivity());
        progressDialog.setMessage("Signing in");
        progressDialog.show();




        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, GET_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("response", response.toString());
                        progressDialog.dismiss();
                        if(response.optInt("success")== 0)
                            Utility.showToast(getActivity(), "Could not login. Try again");
                        else
                        {   SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("email",email.getText().toString());
                            editor.putString(Constants.USER_TYPE,Constants.PHOTOGRAPHER);
                            editor.commit();

                            ((OnClickLogin) getActivity()).loginsuccessful();

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

    public void loginUser() {
        String GET_URL = "http://captureapp.in/api/login_photo_user.php?email="+email.getText().toString().toLowerCase()+"&password="+password.getText().toString();

        final ProgressDialog progressDialog =  new ProgressDialog(getActivity());
        progressDialog.setMessage("Signing in");
        progressDialog.show();




        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, GET_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("response", response.toString());
                        progressDialog.dismiss();
                        if(response.optInt("success")== 0)
                            Utility.showToast(getActivity(), "Email already exists");
                        else
                        {   SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("email",email.getText().toString());
                            editor.putString(Constants.USER_TYPE,Constants.ENDUSER);
                            editor.commit();

                            ((OnClickLogin) getActivity()).loginsuccessful();

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
