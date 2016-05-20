package in.capture.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.capture.R;
import in.capture.ui.PhotographerProfileActivity;


/**
 * {@link RecyclerView.Adapter} that can display a and makes a call to the
 *
 * TODO: Replace the implementation with code for your data type.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
//

    Context context;

    public RecyclerViewAdapter(Context context)
    {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, PhotographerProfileActivity.class);
                context.startActivity(intent);
            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
//        holder.mItem = mValues.get(position);
//        holder.mIdView.setText(mValues.get(position).id);
//        holder.mContentView.setText(mValues.get(position).content);



    }

    @Override
    public int getItemCount() {
//        return mValues.size();
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

//        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);

        }

        @Override
        public String toString() {
            return super.toString() ;
        }
    }
}
