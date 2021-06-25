$( function() {
    $( "#ingredientAuto" ).autocomplete({
        source: "ingredientAutocomplete?urlId=" + $('#urlId').val()
    });
} );

$(".confirmDelete").confirm("Are you sure?");

