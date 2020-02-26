package guru.springframework.recipe_app.services;

import guru.springframework.recipe_app.commands.IngredientCommand;

public interface IngredientService {

    IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);
}
