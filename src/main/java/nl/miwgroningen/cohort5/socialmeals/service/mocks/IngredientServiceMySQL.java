package nl.miwgroningen.cohort5.socialmeals.service.mocks;

import nl.miwgroningen.cohort5.socialmeals.dto.IngredientDTO;
import nl.miwgroningen.cohort5.socialmeals.model.Ingredient;
import nl.miwgroningen.cohort5.socialmeals.repository.IngredientRepository;
import nl.miwgroningen.cohort5.socialmeals.service.IngredientService;
import nl.miwgroningen.cohort5.socialmeals.service.dtoconverter.IngredientConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Britt van Mourik
 *
 *  Collects and stores Ingredients in the MySQL Database
 */

@Service
public class IngredientServiceMySQL implements IngredientService {

    private final IngredientRepository ingredientRepository;

    private IngredientConverter ingredientConverter;

    @Autowired
    public IngredientServiceMySQL(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
        ingredientConverter = new IngredientConverter();
    }

    @Override
    public List<IngredientDTO> getAll() {
        List<Ingredient> ingredientList = ingredientRepository.findAll();
        return ingredientConverter.toListDTO(ingredientList);
    }

    @Override
    public IngredientDTO addNew(IngredientDTO ingredientDTO) {
        formatIngredientName(ingredientDTO);
        Ingredient ingredient = ingredientConverter.fromDTO(ingredientDTO);
        ingredientRepository.save(ingredient);
        return ingredientDTO;
    }

    @Override
    public IngredientDTO findByIngredientName(String ingredientName) {

        Optional<Ingredient> ingredient = ingredientRepository.findByIngredientName(ingredientName);
        IngredientDTO ingredientDTO = null;

        if (ingredient.isPresent()){
            ingredientDTO = ingredientConverter.toDTO(ingredient.get());
        }

        return ingredientDTO;
    }

    public Ingredient getIngredientByIngredientDTO(IngredientDTO ingredientDTO) {

        Optional<Ingredient> ingredient = ingredientRepository.findByIngredientName(ingredientDTO.getIngredientName());

        return ingredient.orElse(null);
    }

    @Override
    public List<String> search(String keyword) {
        return ingredientRepository.search(keyword);
    }

    private IngredientDTO formatIngredientName(IngredientDTO ingredientDTO) {
        String ingredientName = ingredientDTO.getIngredientName().trim();
        ingredientName = ingredientName.substring(0, 1).toUpperCase() + ingredientName.substring(1);

        ingredientDTO.setIngredientName(ingredientName);
        return ingredientDTO;
    }
}
