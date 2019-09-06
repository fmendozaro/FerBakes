package com.fer_mendoza.ferbakes.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Serializable {
    private final long id;
    private final String name;
    private final List<Ingredient> ingredients;
    private final List<Step> steps;

    public Recipe(long id, String name, List<Ingredient> ingredients, List<Step> steps) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static ArrayList<Recipe> createRecipes(JSONArray jsonArray){
        ArrayList<Recipe> recipes = new ArrayList<>();
        List<Ingredient> ingredients;
        List<Step> steps;

        for (int i = 0 ; i < jsonArray.length(); i++) {
            try {
                JSONObject obj = jsonArray.getJSONObject(i);
                JSONArray ingredientsArray = obj.getJSONArray("ingredients");
                JSONArray stepsArray = obj.getJSONArray("steps");
                ingredients = new ArrayList<>();
                steps = new ArrayList<>();
                
                for (int x = 0 ; x < ingredientsArray.length(); x++) {
                    JSONObject ingredient = ingredientsArray.getJSONObject(x);
                    ingredients.add(new Ingredient(
                            ingredient.getDouble("quantity"),
                            ingredient.getString("measure"),
                            ingredient.getString("ingredient")
                    ));
                }

                for (int y = 0 ; y < stepsArray.length(); y++) {
                    JSONObject step = stepsArray.getJSONObject(y);
                    steps.add(new Step(
                            step.getLong("id"),
                            step.getString("shortDescription"),
                            step.getString("description"),
                            step.getString("videoURL"),
                            step.getString("thumbnailURL")
                    ));
                }

                recipes.add(new Recipe(
                    Long.parseLong(obj.getString("id")),
                    obj.getString("name"),
                        ingredients,
                        steps
                ));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return recipes;
    }
}
