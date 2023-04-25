
$(' #deleteButton').on('click', function(event) {
    event.preventDefault();
    var href = $(this).attr('href');
   $('#deleteModal #delRef').attr('href', href);
   $('#deleteModal').modal();

});


$('#deleteChemButton').on('click', function(event) {
    event.preventDefault();
    var href = $(this).attr('href');
   $('#deleteChemicalModal #delChem').attr('href', href);
   $('#deleteChemicalModal').modal();

});




