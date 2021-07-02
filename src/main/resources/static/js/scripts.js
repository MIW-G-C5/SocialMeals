$(function () {
    $("#ingredientAuto").autocomplete({
        source: "/recipe/update/ingredientAutocomplete?urlId=" + $('#urlId').val(),
        minLength: 3
    });
});

$(".confirmDelete").confirm("Are you sure?");

$(function () {
    $(".radioRating").on('change', function () {
        $(this).parents('form').submit();
    });
});

$(function () {
    $(".uploadImg").on('change', function () {
        if (this.files[0].size > 1048576
    ) {
            alert("Image file is too big! (Max = 1MB)")
            this.value = null;
        }
    });
});