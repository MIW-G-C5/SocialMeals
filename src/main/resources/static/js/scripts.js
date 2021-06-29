$( function() {
    $( "#ingredientAuto" ).autocomplete({
        source: "/recipe/update/ingredientAutocomplete?urlId=" + $('#urlId').val(),
        minLength: 3
    });
} );

$(".confirmDelete").confirm("Are you sure?");

