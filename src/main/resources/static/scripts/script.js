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
            setOutsideChart(data);
        }
    });

    let ctx = document.getElementById('hourChart');

    new Chart(ctx, {
        type: 'line',
        data: {
            labels: [
                '0 Uhr',
                '1 Uhr',
                '2 Uhr',
                '3 Uhr',
                '4 Uhr',
                '5 Uhr',
                '6 Uhr',
                '7 Uhr',
                '8 Uhr',
                '9 Uhr',
                '10 Uhr',
                '11 Uhr',
                '12 Uhr',
                '13 Uhr',
                '14 Uhr',
                '15 Uhr',
                '16 Uhr',
                '17 Uhr',
                '18 Uhr',
                '19 Uhr',
                '20 Uhr',
                '21 Uhr',
                '22 Uhr',
                '23 Uhr',
            ],
            datasets: [{
                label: 'Temperatur (°C)',
                data: [20, 25, 23, 23, 19, 20, 20, 25.5, 23, 23, 19, 20, 13, 30, 19, 20, 20, 25, 23, 23, 19, 20, 13, 19],
                borderWidth: 1,
                fill: false,
                borderColor: '#006ee0',
                tension: 0.2
            },{
                label: 'Luftfeuchtigkeit (%)',
                data: [40, 50, 55, 35, 40, 50, 55, 35, 40, 50, 55, 35, 40, 50, 55, 35, 40, 50, 55, 35, 40, 50, 55, 35],
                borderWidth: 1,
                fill: false,
                borderColor: '#198754',
                tension: 0.2
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            },
            aspectRatio: 4
        }
    });

    ctx = document.getElementById('dayChart');

    new Chart(ctx, {
        type: 'line',
        data: {
            labels: [
                '0 Uhr',
                '1 Uhr',
                '2 Uhr',
                '3 Uhr',
                '4 Uhr',
                '5 Uhr',
                '6 Uhr',
                '7 Uhr',
                '8 Uhr',
                '9 Uhr',
                '10 Uhr',
                '11 Uhr',
                '12 Uhr',
                '13 Uhr',
                '14 Uhr',
                '15 Uhr',
                '16 Uhr',
                '17 Uhr',
                '18 Uhr',
                '19 Uhr',
                '20 Uhr',
                '21 Uhr',
                '22 Uhr',
                '23 Uhr',
            ],
            datasets: [{
                label: 'Temperatur (°C)',
                data: [20, 25, 23, 23, 19, 20, 20, 25.5, 23, 23, 19, 20, 13, 30, 19, 20, 20, 25, 23, 23, 19, 20, 13, 19],
                borderWidth: 1,
                fill: false,
                borderColor: '#006ee0',
                tension: 0.2
            },{
                label: 'Luftfeuchtigkeit (%)',
                data: [40, 50, 55, 35, 40, 50, 55, 35, 40, 50, 55, 35, 40, 50, 55, 35, 40, 50, 55, 35, 40, 50, 55, 35],
                borderWidth: 1,
                fill: false,
                borderColor: '#198754',
                tension: 0.2
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            },
            aspectRatio: 4
        }
    });

});

function getTime(time) {
    return new Date(time).getHours() + ':' + new Date(time).getMinutes();
}

function setOutsideChart(data) {

    const elem = document.getElementById('outsideDay');

    let times = getHourlyParam(data, 24, 'time');
    let temperatures = getHourlyParam(data, 24, 'temperature_2m');
    let humidity = getHourlyParam(data, 24, 'relativehumidity_2m');

    new Chart(elem, {
        type: 'line',
        data: {
            labels: times,
            datasets: [{
                label: 'Temperatur (°C)',
                data: temperatures,
                borderWidth: 1,
                fill: false,
                borderColor: '#006ee0',
                tension: 0.2
            },{
                label: 'Luftfeuchtigkeit (%)',
                data: humidity,
                borderWidth: 1,
                fill: false,
                borderColor: '#198754',
                tension: 0.2
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            },
            aspectRatio: 4
        }
    });

}

function getHourlyParam(data, count, type) {
    let tempArray = [];
    for (let i = 0; i < count; i++) {
        if (type === 'time') {
            tempArray[i] = getTime(data['hourly'][type][i]) + '0';
        } else {
            tempArray[i] = getTime(data['hourly'][type][i]);
        }
    }
    return tempArray;
}