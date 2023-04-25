$(' #deleteButton').on('click', function(event) {
        event.preventDefault();
        var href = $(this).attr('href');
       $('#deleteProjectModal #delCourse').attr('href', href);
       $('#deleteProjectModal').modal();

    });