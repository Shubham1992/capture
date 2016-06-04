package in.capture.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.capture.R;
import in.capture.model.PhotoModel;
import in.capture.utils.Constants;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<PhotoModel> photoModels;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.grid_image_item, parent, false);


        ImageView imageView = (ImageView) view.findViewById(R.id.img);
        Picasso.with(mContext).load(Constants.imageBaseUrl+photoModels.get(position).getImage()).into(imageView);



        return view;
    }
 
}