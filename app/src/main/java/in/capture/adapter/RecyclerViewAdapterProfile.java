package in.capture.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import in.capture.R;


/**
 * {@link RecyclerView.Adapter} that can display a and makes a call to the
 *
 * TODO: Replace the implementation with code for your data type.
 */
public class RecyclerViewAdapterProfile extends RecyclerView.Adapter<RecyclerViewAdapterProfile.ViewHolder> {
//

    Context context;
    public Integer[] mThumbIds = {
            R.drawable.a, R.drawable.c,
            R.drawable.b, R.drawable.d,
            R.drawable.f, R.drawable.e,
            R.drawable.g, R.drawable.h,
            R.drawable.i, R.drawable.j,
            R.drawable.k, R.drawable.l,
            R.drawable.m, R.drawable.a,
            R.drawable.b
    };

    public RecyclerViewAdapterProfile(Context context)
    {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_image_item2, parent, false);



        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
//        holder.mItem = mValues.get(position);
//        holder.mIdView.setText(mValues.get(position).id);
//        holder.mContentView.setText(mValues.get(position).content);
       holder.imageView.setImageResource(mThumbIds[position]);



    }

    @Override
    public int getItemCount() {
//        return mValues.size();
        return mThumbIds.length;
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
