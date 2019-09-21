package com.fer_mendoza.ferbakes.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
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

public class GetRecipesService extends IntentService implements OnTaskCompleted {

    static List<Recipe> recipes;

    public static final String ACTION_GET_RECIPES = "com.fer_mendoza.ferbakes.action.get_recipes";
    public static final String ACTION_UPDATE_RECIPES = "com.fer_mendoza.ferbakes.action.update_recipes";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GetRecipesService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        System.out.println("onHandleIntent");
        System.out.println("intent.getAction() = " + intent.getAction());
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_RECIPES.equals(action)) {
                handleAction();
            }else if(ACTION_UPDATE_RECIPES.equals(action)){
                handleActionUpdate();
            }
        }
    }

    private void handleActionUpdate() {
        System.out.println("handleActionUpdate");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, GetRecipesService.class));
        FerBakesWidgetProvider.updateRecipeWidgets(this, appWidgetManager, appWidgetIds, recipes);
    }

    public static void startActionGet(Context context) {
        Intent intent = new Intent(context, GetRecipesService.class);
        intent.setAction(ACTION_GET_RECIPES);
        context.startService(intent);
    }

    public static void startActionUpdate(Context context) {
        Intent intent = new Intent(context, GetRecipesService.class);
        intent.setAction(ACTION_UPDATE_RECIPES);
        context.startService(intent);
    }

    private void handleAction() {
        System.out.println("handleAction");
        HashMap<String, String> params = new HashMap<>();
        params.put("orderBy","asc");
        ApiTask apiTask = new ApiTask(GetRecipesService.this);
        apiTask.execute(NetworkUtils.parseURL("d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json", params));
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
