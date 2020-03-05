package guru.springframework.recipe_app.services.impl;

import guru.springframework.recipe_app.services.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {
    @Override
    public void saveImageFile(Long recipeId, MultipartFile file) {

        log.debug("Received file");
    }
}
