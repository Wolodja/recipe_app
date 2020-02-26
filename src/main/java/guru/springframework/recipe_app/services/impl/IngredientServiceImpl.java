package guru.springframework.recipe_app.services.impl;

import guru.springframework.recipe_app.commands.IngredientCommand;
import guru.springframework.recipe_app.converters.IngredientToIngredientCommand;
import guru.springframework.recipe_app.domain.Recipe;
import guru.springframework.recipe_app.repositories.RecipeRepository;
import guru.springframework.recipe_app.services.IngredientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final RecipeRepository recipeRepository;

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand, RecipeRepository recipeRepository) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {

        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (!recipeOptional.isPresent()) {
            log.error("Recipe id is not found : " + recipeId);
            return null;
        }
        Recipe recipe = recipeOptional.get();

        Optional<IngredientCommand> ingredientCommandOptional = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(ingredientToIngredientCommand::convert)
                .findFirst();

        if(!ingredientCommandOptional.isPresent()){
            log.error("Ingredient id not found : " + ingredientId);
            return null;
        }
        return ingredientCommandOptional.get();
    }
}
