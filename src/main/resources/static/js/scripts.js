$( function() {
    $( "#ingredientAuto" ).autocomplete({
        source: "/recipe/update/ingredientAutocomplete?urlId=" + $('#urlId').val()
    });
} );

$(".confirmDelete").confirm("Are you sure?");

