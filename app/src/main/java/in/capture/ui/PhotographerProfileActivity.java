package in.capture.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.GridView;

import in.capture.R;

public class PhotographerProfileActivity extends AppCompatActivity {

    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       /* gridView = (GridView) findViewById(R.id.grid_view);
        gridView.setAdapter(new ImageAdapter(PhotographerProfileActivity.this));*/

        RecyclerView rv = (RecyclerView) findViewById(R.id.section_label);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        rv.setLayoutManager(layoutManager);
        RecyclerViewAdapterProfile recyclerViewAdapter = new RecyclerViewAdapterProfile(PhotographerProfileActivity.this);
        rv.setAdapter(recyclerViewAdapter);

    }
}
