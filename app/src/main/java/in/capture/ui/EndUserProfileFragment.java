package in.capture.ui;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.capture.R;
import in.capture.adapter.BookingListRVAdapter;
import in.capture.adapter.PhotographersListRVAdapter;
import in.capture.model.BookingModel;
import in.capture.model.PhotographerModel;
import in.capture.utils.Constants;
import in.capture.utils.Parser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EndUserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EndUserProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SharedPreferences sharedPreferences;
    private RecyclerView rvBooking;


    public EndUserProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EndUserProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EndUserProfileFragment newInstance(String param1, String param2) {
        EndUserProfileFragment fragment = new EndUserProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_end_user_profile, container, false);


        rvBooking = (RecyclerView) view.findViewById(R.id.rvBookings);
        rvBooking.setLayoutManager(new LinearLayoutManager(getActivity()));
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE);
        String url = Constants.urlBookings+"?email="+sharedPreferences.getString("email","");
        makeJsonRequest(url);
        return view;
    }

    public void makeJsonRequest(String url) {


        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("response", response.toString());

                        if (response.optString("success").equalsIgnoreCase("1")) {
                            rvBooking.setVisibility(View.VISIBLE);
                            ArrayList<BookingModel> bookingModelArrayList = Parser.parse_booking_list_data(response);
                            BookingListRVAdapter bookingListRVAdapter = new BookingListRVAdapter(getActivity(), bookingModelArrayList);
                            rvBooking.setAdapter(bookingListRVAdapter);
                         }else {

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

}
