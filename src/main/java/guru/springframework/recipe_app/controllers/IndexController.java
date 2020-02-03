package guru.springframework.recipe_app.controllers;

import guru.springframework.recipe_app.domain.Category;
import guru.springframework.recipe_app.domain.UnitOfMeasure;
import guru.springframework.recipe_app.repositories.CategoryRepository;
import guru.springframework.recipe_app.repositories.UnitOfMeasureRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
public class IndexController {

    private CategoryRepository categoryRepository;
    private UnitOfMeasureRepository unitOfMeasureRepository;

    public IndexController(CategoryRepository categoryRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @RequestMapping({"", "/", "/index"})
    public String getIndexPage() {

        Optional<Category> categoryOptional = categoryRepository.findByDescription("American");
        Optional<UnitOfMeasure> unitOfMeasureOptional = unitOfMeasureRepository.findByDescription("Teaspoon");
        System.out.println(categoryOptional.toString());
        System.out.println(unitOfMeasureOptional.toString());
        return "index";
    }
}
