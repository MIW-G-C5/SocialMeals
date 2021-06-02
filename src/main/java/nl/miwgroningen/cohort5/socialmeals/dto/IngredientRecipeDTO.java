package nl.miwgroningen.cohort5.socialmeals.dto;

/**
 * @author Wessel van Dommelen <w.r.van.dommelen@st.hanze.nl>
 */
public class IngredientRecipeDTO {

    private IngredientDTO ingredientDTO;
    private RecipeDTO recipeDTO;
    private double quantity;
    private String quantityType;

    public IngredientRecipeDTO(IngredientDTO ingredientDTO, RecipeDTO recipeDTO,
                                    double quantity, String quantityType) {
        this.ingredientDTO = ingredientDTO;
        this.recipeDTO = recipeDTO;
        this.quantity = quantity;
        this.quantityType = quantityType;
    }

    public IngredientDTO getIngredientDTO() {
        return ingredientDTO;
    }

    public RecipeDTO getRecipeDTO() {
        return recipeDTO;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getQuantityType() {
        return quantityType;
    }

    public void setIngredientDTO(IngredientDTO ingredientDTO) {
        this.ingredientDTO = ingredientDTO;
    }

    public void setRecipeDTO(RecipeDTO recipeDTO) {
        this.recipeDTO = recipeDTO;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setQuantityType(String quantityType) {
        this.quantityType = quantityType;
    }
}
