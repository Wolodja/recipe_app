package guru.springframework.recipe_app.repositories;

import guru.springframework.recipe_app.domain.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
}
