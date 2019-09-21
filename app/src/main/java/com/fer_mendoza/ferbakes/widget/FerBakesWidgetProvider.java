package com.fer_mendoza.ferbakes.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.fer_mendoza.ferbakes.R;
import com.fer_mendoza.ferbakes.models.Recipe;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class FerBakesWidgetProvider extends AppWidgetProvider {

    static List<Recipe> recipes;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);

        Intent recipeIntent = new Intent(context, GetRecipesService.class);
        recipeIntent.setAction(GetRecipesService.ACTION_GET_RECIPES);
        PendingIntent recipePendingIntent = PendingIntent.getService(
                context,
                0,
                recipeIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.fer_bakes_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);
        views.setOnClickPendingIntent(R.id.refresh_btn, recipePendingIntent);

        if(recipes != null){
            for (Recipe r: recipes) {
                System.out.println("r.getName() = " + r.getName());
            }
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, List<Recipe> latestRecipes) {
        recipes = latestRecipes;
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        GetRecipesService.startActionGet(context);
        // There may be multiple widgets active, so update all of them
//        for (int appWidgetId : appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId);
//        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

