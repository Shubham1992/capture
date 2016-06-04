package in.capture.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.capture.R;
import in.capture.model.PhotographerModel;
import in.capture.ui.BookingActivity;
import in.capture.ui.PhotographerProfileActivity;
import in.capture.utils.Constants;


/**
 * {@link RecyclerView.Adapter} that can display a and makes a call to the
 *
 * TODO: Replace the implementation with code for your data type.
 */
public class PhotographersListRVAdapter extends RecyclerView.Adapter<PhotographersListRVAdapter.ViewHolder> {
//

    Context context;
    ArrayList<PhotographerModel> photographerModels = new ArrayList<>();
    public PhotographersListRVAdapter(Context context, ArrayList<PhotographerModel> photographerModels)
    {
        this.context = context;
        this.photographerModels = photographerModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.name.setText(photographerModels.get(position).getName());
        holder.rate.setText(photographerModels.get(position).getRate());
        Picasso.with(context).load(Constants.imageBaseUrl+photographerModels.get(position).getCoverpic()).into(holder.coverPic);
        holder.mview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, PhotographerProfileActivity.class);
                intent.putExtra("email",photographerModels.get(position).getEmail());
                context.startActivity(intent);
            }
        });
        holder.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BookingActivity.class);
                intent.putExtra("email",photographerModels.get(position).getEmail());
                intent.putExtra("name", photographerModels.get(position).getName());
                intent.putExtra("rate",photographerModels.get(position).getRate());
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
//        return mValues.size();
        return photographerModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

//        public DummyItem mItem;

        TextView name, rate; RatingBar vote;
        ImageView profilePic, coverPic;
        Button book;
        View mview;


        public ViewHolder(View view) {
            super(view);
            mview = view;
        name = (TextView) view.findViewById(R.id.name);
        vote = (RatingBar) view.findViewById(R.id.vote);
        rate = (TextView) view.findViewById(R.id.rate);
        profilePic = (ImageView) view.findViewById(R.id.profilepic);
        coverPic = (ImageView) view.findViewById(R.id.coverpic);
        book = (Button) view.findViewById(R.id.btnbook);

        }

        @Override
        public String toString() {
            return super.toString() ;
        }
    }
}
