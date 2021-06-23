$( function() {
    $( "#ingredientAuto" ).autocomplete({
        source: "ingredientAutocomplete?recipeName=" + $('#name').val()
    });
} );

$(".confirmDelete").confirm("Are you sure?");

