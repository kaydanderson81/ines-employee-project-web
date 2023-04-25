//$(document).ready(function() {
//    $('.table .eBtn').on('click', function(event){
//
//        $('.employeeProjectEdit #employeeProjectEditModal').modal();
//        event.preventDefault();
//        var href = $(this).attr('href');
//
//        $.get(href, function(employeeProject, status){
//            $('.employeeProjectEdit #projectName').val(employeeProject.employeeProjectName);
//            $('.employeeProjectEdit #employeeProjectStartDate').val(employeeProject.employeeProjectStartDate);
//            $('.employeeProjectEdit #employeeProjectEndDate').val(employeeProject.employeeProjectEndDate);
//        });
//    });
//});

$('#deleteProjectButton').on('click', function(event) {
        event.preventDefault();
        var href = $(this).attr('href');
        $('#deleteEmployeeProjectModal #delProjRef').attr('href', href);
        $('#deleteEmployeeProjectModal').modal();
    });

setInterval(function(){ $(".alert").fadeOut(); }, 3000);

$('#startDatePicker').datepicker({
    dateFormat: 'dd-mm-yy',
    beforeShowDay: function(d) {
    console.log("in"+d.getDate());
       if (d.getDate() == 2 || d.getDate() == 16) {
         return [true, "" ];
       } else {
          return [false, "" ];
       }
    }
});

