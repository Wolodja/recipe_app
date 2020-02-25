package guru.springframework.recipe_app.services;

import guru.springframework.recipe_app.commands.RecipeCommand;
import guru.springframework.recipe_app.domain.Recipe;

import java.util.Set;

public interface RecipeService {

    Set<Recipe> getRecipes();

    Recipe findById(Long id);

    RecipeCommand saveRecipeCommand(RecipeCommand command);
}
