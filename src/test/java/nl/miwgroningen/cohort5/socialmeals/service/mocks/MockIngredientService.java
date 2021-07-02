package nl.miwgroningen.cohort5.socialmeals.service.mocks;

import nl.miwgroningen.cohort5.socialmeals.dto.IngredientDTO;
import nl.miwgroningen.cohort5.socialmeals.model.Ingredient;
import nl.miwgroningen.cohort5.socialmeals.service.IngredientService;

import java.util.List;

/**
 * @author Wessel van Dommelen <w.r.van.dommelen@st.hanze.nl>
 *
 * Mocks the IngredientService for testing.
 */

public class MockIngredientService implements IngredientService {

    @Override
    public List<IngredientDTO> getAll() {
        return null;
    }

    @Override
    public IngredientDTO addNew(IngredientDTO ingredientDTO) {
        return null;
    }

    @Override
    public IngredientDTO findByIngredientName(String ingredientName) {
        return null;
    }

    @Override
    public Ingredient getIngredientByIngredientDTO(IngredientDTO ingredientDTO) {
        return new Ingredient(ingredientDTO.getIngredientName());
    }

    @Override
    public List<String> search(String keyword) {
        return null;
    }
}
