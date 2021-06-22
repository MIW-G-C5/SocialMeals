$( function() {
    $( "#ingredientAuto" ).autocomplete({
        source: "ingredientAutocomplete?recipeName=" + $('#name').val()
    });
} );
