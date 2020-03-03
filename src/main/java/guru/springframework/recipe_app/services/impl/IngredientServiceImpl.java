package guru.springframework.recipe_app.services.impl;

import guru.springframework.recipe_app.commands.IngredientCommand;
import guru.springframework.recipe_app.converters.IngredientCommandToIngredient;
import guru.springframework.recipe_app.converters.IngredientToIngredientCommand;
import guru.springframework.recipe_app.domain.Ingredient;
import guru.springframework.recipe_app.domain.Recipe;
import guru.springframework.recipe_app.repositories.RecipeRepository;
import guru.springframework.recipe_app.repositories.UnitOfMeasureRepository;
import guru.springframework.recipe_app.services.IngredientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand, IngredientCommandToIngredient ingredientCommandToIngredient, RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
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

        if (!ingredientCommandOptional.isPresent()) {
            log.error("Ingredient id not found : " + ingredientId);
            return null;
        }
        return ingredientCommandOptional.get();
    }

    @Override
    @Transactional
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());

        if (!recipeOptional.isPresent()) {
            log.error("Recipe not found for id: " + command.getRecipeId());
            return new IngredientCommand();
        } else {
            Recipe recipe = recipeOptional.get();

            Optional<Ingredient> ingredientOptional = recipe.getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(command.getId()))
                    .findFirst();

            if (ingredientOptional.isPresent()) {
                Ingredient ingredientFound = ingredientOptional.get();
                ingredientFound.setDescription(command.getDescription());
                ingredientFound.setAmount(command.getAmount());
                ingredientFound.setUom(unitOfMeasureRepository
                        .findById(command.getUom().getId())
                        .orElseThrow(() -> new RuntimeException("UOM NOT FOUND id:" + command.getUom().getId())));
            } else {
                Ingredient ingredient = ingredientCommandToIngredient.convert(command);
                ingredient.setRecipe(recipe);
                recipe.addIngredient(ingredient);
            }
            Recipe savedRecipe = recipeRepository.save(recipe);

            Optional<Ingredient> savedIngedientOptional = savedRecipe.getIngredients().stream()
                    .filter(recipeIngredient -> recipeIngredient.getId().equals(command.getId()))
                    .findFirst();

            if(!savedIngedientOptional.isPresent()){
                savedIngedientOptional = savedRecipe.getIngredients().stream()
                        .filter(recipeIngredient -> recipeIngredient.getDescription().equals(command.getDescription()))
                        .filter(recipeIngredient -> recipeIngredient.getAmount().equals(command.getAmount()))
                        .filter(recipeIngredient -> recipeIngredient.getUom().getId().equals(command.getUom().getId()))
                        .findFirst();
            }
            return ingredientToIngredientCommand.convert(savedIngedientOptional.get());
        }
    }

    @Override
    public void deleteById(Long recipeId, Long ingredientId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if(recipeOptional.isPresent()){
            Recipe recipe = recipeOptional.get();
            Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientId))
                    .findFirst();
            if(ingredientOptional.isPresent()){
                Ingredient ingredientToDelete = ingredientOptional.get();
                ingredientToDelete.setRecipe(null);
                recipe.getIngredients().remove(ingredientOptional.get());
                recipeRepository.save(recipe);
            }
        } else {
            log.debug("Recipe Id not found. Id: "+recipeId);
        }
    }
}
