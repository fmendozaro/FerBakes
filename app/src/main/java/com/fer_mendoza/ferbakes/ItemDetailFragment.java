package com.fer_mendoza.ferbakes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.fer_mendoza.ferbakes.models.Ingredient;
import com.fer_mendoza.ferbakes.models.Recipe;
import com.fer_mendoza.ferbakes.models.Step;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "recipe_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Recipe recipe;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().getSerializable(ARG_ITEM_ID) != null) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            recipe = (Recipe) getArguments().getSerializable(ARG_ITEM_ID);
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            System.out.println("recipe = " + recipe.getName());
            if (appBarLayout != null) {
                appBarLayout.setTitle(recipe.getName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);
        LinearLayout layout = (LinearLayout) rootView;

        if (recipe != null) {
            String ingredientsTxt = "Ingredients: \n\n";
            for (Ingredient ingredient: recipe.getIngredients()) {
                ingredientsTxt += String.format("• %s %s. of %s. \n", ingredient.getQuantity(), ingredient.getMeasure().toLowerCase(), ingredient.getIngredient());
            }

            ((TextView) rootView.findViewById(R.id.item_detail_txt)).setText(ingredientsTxt);


            for (final Step step: recipe.getSteps()){
                Button btn = new Button(rootView.getContext());
                btn.setText("Step " + (step.getId()+1) + ": " + step.getShortDescription());
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loadStepDetail(step.getId());
                    }
                });
                layout.addView(btn);
            }
        }

        return rootView;
    }

    private void loadStepDetail(long stepId) {
        Intent stepDetails = new Intent(getActivity(), StepActivity.class);
        stepDetails.putExtra("recipe", recipe);
        stepDetails.putExtra("stepId", stepId);
        startActivity(stepDetails);
    }
}
