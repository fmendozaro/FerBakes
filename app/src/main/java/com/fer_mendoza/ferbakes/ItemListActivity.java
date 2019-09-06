package com.fer_mendoza.ferbakes;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.fer_mendoza.ferbakes.utils.NetworkUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity implements OnTaskCompleted{

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        getRecipes();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, JSONArray jsonArray) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, jsonArray, mTwoPane));
    }

    public void getRecipes(){
        HashMap<String, String> params = new HashMap<>();
        params.put("orderBy","asc");
        ApiTask apiTask = new ApiTask(ItemListActivity.this, "movie");
        apiTask.execute(NetworkUtils.parseURL("d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json", params));
    }


    @Override
    public void onTaskCompleted(String jsonString) {
        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        try {
            setupRecyclerView((RecyclerView) recyclerView, new JSONArray(jsonString));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
