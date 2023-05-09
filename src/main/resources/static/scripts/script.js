$(window).on('load', function () {

    $('#name-button').on('click', function () {
        $.post("/name",
            {
                name: $('#name-input').val()
            },
            function(data, status){
                console.log("Data: " + data + "\nStatus: " + status);
                window.location.reload();
            });
    });

    $.ajax({
        url: 'https://api.open-meteo.com/v1/forecast?latitude=50.26&longitude=10.96&hourly=temperature_2m,relativehumidity_2m,windspeed_10m&daily=sunrise,sunset&current_weather=true&forecast_days=1&timezone=Europe%2FBerlin',
        type: "GET",
        success: function (data) {
            $('#sunrise').text(getTime(data['daily']['sunrise'][0]));
            $('#sunset').text(getTime(data['daily']['sunset'][0]));
        }
    });

});

function getTime(time) {
    return new Date(time).getHours() + ':' + new Date(time).getMinutes();
}