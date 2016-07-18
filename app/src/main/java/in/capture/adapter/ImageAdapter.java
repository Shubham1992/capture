package in.capture.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.capture.R;
import in.capture.model.PhotoModel;
import in.capture.model.PhotographerModel;
import in.capture.utils.Constants;
import in.capture.utils.Parser;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<PhotoModel> photoModels;
    private TextView textViewName;

    // Constructor
    public ImageAdapter(Context c, ArrayList<PhotoModel> photoModels)
    {
        mContext = c;
        this.photoModels = photoModels;
    }
 
    @Override
    public int getCount() {
        return photoModels.size();
    }
 
    @Override
    public Object getItem(int position)
    {
        return photoModels.get(position);
    }
 
    @Override
    public long getItemId(int position)
    {
        return Integer.parseInt(photoModels.get(position).getId());
    }
 
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.grid_image_item, parent, false);


        final ImageView imageView = (ImageView) view.findViewById(R.id.img);
        Picasso.with(mContext).load(Constants.imageBaseUrl+photoModels.get(position).getImage()).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertadd = new AlertDialog.Builder(
                       mContext);
                LayoutInflater factory = LayoutInflater.from(mContext);
                final View view = factory.inflate(R.layout.imagepopup2, null);
                ImageView imView = (ImageView) view.findViewById(R.id.img);
                textViewName = (TextView) view.findViewById(R.id.txtname);
                Picasso.with(mContext).load(Constants.imageBaseUrl+photoModels.get(position).getImage()).into(imView);
                alertadd.setView(view);
                alertadd.show();

                makeJsonRequest(Constants.urlGetPhotographer, photoModels.get(position).getEmail());
            }
        });


        return view;
    }

    private void makeJsonRequest(String url, String email) {



        url = url +"?email="+email ;
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("response", response.toString());
                        PhotographerModel photographerModel = Parser.parse_photographer_data(response);
                        textViewName.setText(photographerModel.getName());
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);

    }
 
}