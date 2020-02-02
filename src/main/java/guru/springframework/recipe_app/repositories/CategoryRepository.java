package guru.springframework.recipe_app.repositories;

import guru.springframework.recipe_app.domain.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
