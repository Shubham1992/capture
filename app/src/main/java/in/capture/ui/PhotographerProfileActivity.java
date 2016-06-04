package in.capture.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
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

import java.util.HashMap;
import java.util.Map;

import in.capture.R;
import in.capture.adapter.RecyclerViewAdapterProfile;
import in.capture.model.PhotographerModel;
import in.capture.utils.Constants;
import in.capture.utils.Parser;

public class PhotographerProfileActivity extends AppCompatActivity {

    private GridView gridView;
    private ImageView coverpic , profilePic; TextView name, charges;
    private String email;
    private Button bookBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        coverpic = (ImageView) findViewById(R.id.coverpic);
        name = (TextView) findViewById(R.id.name);
        charges = (TextView) findViewById(R.id.charges);
        profilePic = (ImageView) findViewById(R.id.profilepic);
        email = getIntent().getStringExtra("email");

        bookBtn = (Button) findViewById(R.id.btnbook);
        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhotographerProfileActivity.this, BookingActivity.class);
                intent.putExtra("email",email);
                intent.putExtra("name", name.getText());
                intent.putExtra("rate",charges.getText().toString());
                startActivity(intent);
            }
        });
        makeJsonRequest(Constants.urlGetPhotographer);

        RecyclerView rv = (RecyclerView) findViewById(R.id.rv_portfolio);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        rv.setLayoutManager(layoutManager);
        RecyclerViewAdapterProfile recyclerViewAdapter = new RecyclerViewAdapterProfile(PhotographerProfileActivity.this);
        rv.setAdapter(recyclerViewAdapter);



    }

    private void makeJsonRequest(String url) {


        final ProgressDialog progressDialog =  new ProgressDialog(PhotographerProfileActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        url = url +"?email="+email ;
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
                        Toast.makeText(PhotographerProfileActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(PhotographerProfileActivity.this);
        requestQueue.add(stringRequest);

    }

    private void setPhotographerProfile(PhotographerModel photographerModel) {
        Picasso.with(PhotographerProfileActivity.this).load(Constants.imageBaseUrl+photographerModel.getCoverpic()).into(coverpic);
        name.setText(photographerModel.getName());
    }
}
