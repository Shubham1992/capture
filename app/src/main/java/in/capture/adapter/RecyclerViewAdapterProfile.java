package in.capture.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.capture.R;
import in.capture.model.PhotoModel;
import in.capture.utils.Constants;


/**
 * {@link RecyclerView.Adapter} that can display a and makes a call to the
 *
 * TODO: Replace the implementation with code for your data type.
 */
public class RecyclerViewAdapterProfile extends RecyclerView.Adapter<RecyclerViewAdapterProfile.ViewHolder> {
//

    Context context;
    ArrayList<PhotoModel> photoModels ;

    public RecyclerViewAdapterProfile(Context context, ArrayList<PhotoModel> photoModels)

    {
        this.context = context;
        this.photoModels = photoModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_image_item2, parent, false);



        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        holder.mItem = mValues.get(position);
//        holder.mIdView.setText(mValues.get(position).id);
//        holder.mContentView.setText(mValues.get(position).content);

        Picasso.with(context).load(Constants.imageBaseUrl + photoModels.get(position).getImage()).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertadd = new AlertDialog.Builder(
                        context);
                LayoutInflater factory = LayoutInflater.from(context);
                final View view = factory.inflate(R.layout.imagepopup2, null);
                ImageView imView = (ImageView) view.findViewById(R.id.img);

                Picasso.with(context).load(Constants.imageBaseUrl+photoModels.get(position).getImage()).into(imView);
                alertadd.setView(view);
                alertadd.show();
            }
        });


    }

    @Override
    public int getItemCount() {
//        return mValues.size();
        return photoModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

//        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);

            imageView = (ImageView) view.findViewById(R.id.img);
        }

        @Override
        public String toString() {
            return super.toString() ;
        }
    }
}
