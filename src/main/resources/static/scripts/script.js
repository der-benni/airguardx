$(window).on('load', function () {

    $('#name-button').on('click', function (event) {
        $.ajax({
            url: '/name',
            type: "POST",
            dataType: "json",
            success: function (data) {

            }
        });
        $.post("/name",
            {
                name: $('#name-input').val()
            },
            function(data, status){
                console.log("Data: " + data + "\nStatus: " + status);
                window.location.reload();
            });
    })

});