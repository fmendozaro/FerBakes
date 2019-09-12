package com.fer_mendoza.ferbakes.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.fer_mendoza.ferbakes.ApiTask;
import com.fer_mendoza.ferbakes.OnTaskCompleted;
import com.fer_mendoza.ferbakes.models.Recipe;
import com.fer_mendoza.ferbakes.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;

public class GetRecipesSerivce extends IntentService implements OnTaskCompleted {

    static List<Recipe> recipes;

    public static final String ACTION_WATER_PLANTS = "";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GetRecipesSerivce(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_WATER_PLANTS.equals(action)) {
                handleActionWaterPlants();
            }
        }
    }

    public static void startActionWaterPlants(Context context) {
//        Intent intent = new Intent(context, PlantWateringService.class);
//        intent.setAction(ACTION_WATER_PLANTS);
//        context.startService(intent);
    }

    private void handleActionWaterPlants() {
        HashMap<String, String> params = new HashMap<>();
        params.put("orderBy","asc");
        ApiTask apiTask = new ApiTask(GetRecipesSerivce.this);
        apiTask.execute(NetworkUtils.parseURL("d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json", params));

        // Update only plants that are still alive
//        getContentResolver().update(
//                PLANTS_URI,
//                contentValues,
//                PlantContract.PlantEntry.COLUMN_LAST_WATERED_TIME+">?",
//                new String[]{String.valueOf(timeNow - PlantUtils.MAX_AGE_WITHOUT_WATER)});
    }

    @Override
    public void onTaskCompleted(String response) {
        try {
            recipes = Recipe.createRecipes(new JSONArray(response));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
