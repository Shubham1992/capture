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

import org.json.JSONArray;

import java.util.ArrayList;

import in.capture.R;
import in.capture.model.BookingModel;
import in.capture.model.PhotographerModel;
import in.capture.ui.BookingActivity;
import in.capture.ui.PhotographerProfileActivity;
import in.capture.utils.Constants;
import in.capture.utils.Utility;


/**
 * {@link RecyclerView.Adapter} that can display a and makes a call to the
 *
 * TODO: Replace the implementation with code for your data type.
 */
public class BookingListRVAdapter extends RecyclerView.Adapter<BookingListRVAdapter.ViewHolder> {
//

    Context context;
    ArrayList<BookingModel> arrayBookingList;
    public BookingListRVAdapter(Context context, ArrayList<BookingModel> arrayBookingList)
    {
        this.context = context;
        this.arrayBookingList = arrayBookingList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booking_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.name.setText(Utility.capitalizeFirstLetter(arrayBookingList.get(position).getPname()));
        holder.rate.setText(arrayBookingList.get(position).getRate());
        holder.time.setText("Booking for: "+arrayBookingList.get(position).getDate());
        Picasso.with(context).load(Constants.imageBaseUrl + arrayBookingList.get(position).getImage()).placeholder(R.drawable.userplaceholder).into(holder.profilePic);
   /*     holder.mview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, PhotographerProfileActivity.class);
                intent.putExtra("email",photographerModels.get(position).getEmail());
                context.startActivity(intent);
            }
        });
*/


    }

    @Override
    public int getItemCount() {
//        return mValues.size();
        return arrayBookingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

//        public DummyItem mItem;

        TextView name, time, rate; RatingBar vote;
        ImageView profilePic, coverPic;
        Button book;
        View mview;


        public ViewHolder(View view) {
            super(view);
            mview = view;
        name = (TextView) view.findViewById(R.id.name);
        rate = (TextView) view.findViewById(R.id.charges);
        profilePic = (ImageView) view.findViewById(R.id.profilepic);
        time = (TextView) view.findViewById(R.id.time);

        }

        @Override
        public String toString() {
            return super.toString() ;
        }
    }
}
