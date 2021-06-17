package nl.miwgroningen.cohort5.socialmeals.service.dtoconverter;

import nl.miwgroningen.cohort5.socialmeals.dto.IngredientDTO;
import nl.miwgroningen.cohort5.socialmeals.model.Ingredient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class IngredientConverterTest {

    @Test
    void toDTO() {
        //arrange
        Ingredient testIngredient = new Ingredient("testIngredient");
        IngredientConverter testObj = new IngredientConverter();

        //act
        IngredientDTO testIngredientDTO = testObj.toDTO(testIngredient);

        //assert
        Assertions.assertEquals(testIngredient.getIngredientName(), testIngredientDTO.getIngredientName());
    }

    @Test
    void toListDTO() {
        //arrange
        List<Ingredient> testIngredientList = new ArrayList<>();
        testIngredientList.add(new Ingredient("testIngredient"));
        testIngredientList.add(new Ingredient("testIngredient2"));
        testIngredientList.add(new Ingredient("testIngredient3"));
        IngredientConverter testObj = new IngredientConverter();

        //act
        List<IngredientDTO> testListObjDTO = testObj.toListDTO(testIngredientList);

        //assert
        for (int i = 0; i < testListObjDTO.size(); i++) {
            Assertions.assertEquals(
                    testIngredientList.get(i).getIngredientName(), testListObjDTO.get(i).getIngredientName());
        }
    }
}