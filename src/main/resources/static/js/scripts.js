$( function() {
    $( "#ingredientAuto" ).autocomplete({
        source: "ingredientAutocomplete?recipeName=" + $('#name').val(),
        change: function(event, ui) {
            if (ui.item == null) {
                event.currentTarget.value = '';
                event.currentTarget.focus();
            }
        }
    });
} );
