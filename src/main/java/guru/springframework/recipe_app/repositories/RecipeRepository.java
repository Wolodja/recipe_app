package guru.springframework.recipe_app.repositories;

import guru.springframework.recipe_app.domain.Recipe;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    Optional<Recipe> findById(Long id);
}
