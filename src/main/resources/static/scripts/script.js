$(window).on('load', function () {

    $('#dayButton').on('click', function () {
        toggleDay();
    });

    $('#hourButton').on('click', function () {
        toggleHour();
    });

    setLatestData();
    setHourlyData();
    setDailyData();
    setDailyDataOutside();

});

function getTime(time) {
    return new Date(time).getHours() + ':' + new Date(time).getMinutes();
}

function setLatestData() {
    $.ajax({
        url: '/lastRecord',
        type: "GET",
        success: function (data) {
            setLatestHumidity(Math.round(data.relative_humidity));
            setLatestTemperature(Math.round(data.temperature));
            setLatestGas(Math.round(data.gas));
        }
    });
}

function setLatestHumidity(data) {

    $('#humiditySmiley').removeClass();
    $('#humidityProgress').removeClass();
    $('#humidityValue').removeClass();

    if (data >= 40 && data <= 60) {
        // good
        $('#humiditySmiley').addClass('bi bi-emoji-smile-fill text-success position-absolute');
        $('#humidityProgress').addClass('progress-bar progress-bar--green');
        $('#humidityValue').addClass('text-success');
    } else if ((data >= 35 && data < 40) || (data > 60 && data <= 65)) {
        // middle
        $('#humiditySmiley').addClass('bi bi-emoji-neutral-fill color-orange position-absolute');
        $('#humidityProgress').addClass('progress-bar progress-bar--warning');
        $('#humidityValue').addClass('color-orange');
    } else if (data < 35 || data > 65) {
        // bad
        $('#humiditySmiley').addClass('bi bi-emoji-frown-fill text-danger position-absolute');
        $('#humidityProgress').addClass('progress-bar progress-bar--danger');
        $('#humidityValue').addClass('text-danger');
    }

    $('#humidityProgress').css('width', data + '%');
    $('#humidityValue').text(data + '%');

}

function setLatestTemperature(data) {

    $('#temperatureSmiley').removeClass();
    $('#temperatureProgress').removeClass();
    $('#temperatureValue').removeClass();

    if (data >= 20 && data <= 23) {
        // good
        $('#temperatureSmiley').addClass('bi bi-emoji-smile-fill text-success position-absolute');
        $('#temperatureProgress').addClass('progress-bar progress-bar--green');
        $('#temperatureValue').addClass('text-success');
    } else if ((data >= 18 && data < 20) || (data > 23 && data <= 25)) {
        // middle
        $('#temperatureSmiley').addClass('bi bi-emoji-neutral-fill color-orange position-absolute');
        $('#temperatureProgress').addClass('progress-bar progress-bar--warning');
        $('#temperatureValue').addClass('color-orange');
    } else if (data < 18 || data > 25) {
        // bad
        $('#temperatureSmiley').addClass('bi bi-emoji-frown-fill text-danger position-absolute');
        $('#temperatureProgress').addClass('progress-bar progress-bar--danger');
        $('#temperatureValue').addClass('text-danger');
    }

    $('#temperatureProgress').css('width', (data * 2) + '%');
    $('#temperatureValue').text(data + '°');

}

function setLatestGas(data) {
    // todo: when gas sensor is delivered
    console.log(data);
}

function setHourlyData() {

    $.ajax({
        url: '/lastHourRecords',
        type: "GET",
        success: function (reversedData) {

            let data = reversedData.reverse();

            let ctx = document.getElementById('hourChart');

            let labels = getFromParams(data, data.length, 'timestamp');
            let temperature = getFromParams(data, data.length, 'temperature');
            let humidity = getFromParams(data, data.length, 'relative_humidity');

            new Chart(ctx, {
                type: 'line',
                data: {
                    labels: labels,
                    datasets: [{
                        label: 'Temperatur (°C)',
                        data: temperature,
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
    });
}

function setDailyData() {

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

}

function setDailyDataOutside() {

    $.ajax({
        url: 'https://api.open-meteo.com/v1/forecast?latitude=50.26&longitude=10.96&hourly=temperature_2m,relativehumidity_2m,windspeed_10m&daily=sunrise,sunset&current_weather=true&forecast_days=1&timezone=Europe%2FBerlin',
        type: "GET",
        success: function (data) {
            $('#sunrise').text(getSunTimes(data['daily']['sunrise'][0]));
            $('#sunset').text(getSunTimes(data['daily']['sunset'][0]));
            setOutsideChart(data);
        }
    });

}

function setOutsideChart(data) {

    const elem = document.getElementById('outsideDay');

    let labels = getHourlyParamsOutside(data, 24, 'time');
    let temperature = getHourlyParamsOutside(data, 24, 'temperature_2m');
    let humidity = getHourlyParamsOutside(data, 24, 'relativehumidity_2m');

    new Chart(elem, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: 'Temperatur (°C)',
                data: temperature,
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

function getHourlyParamsOutside(data, count, type) {
    let tempArray = [];
    for (let i = 0; i < count; i++) {
        if (type === 'time') {
            tempArray[i] = getTime(data['hourly'][type][i]) + '0';
        } else {
            tempArray[i] = data['hourly'][type][i];
        }
    }
    return tempArray;
}

function getFromParams(data, count, type) {
    let tempArray = [];
    for (let i = 0; i < count; i++) {
        if (type === 'timestamp') {
            tempArray[i] = getTime(data[i].timestamp);
        } else if (type === 'temperature') {
            tempArray[i] = data[i].temperature;
        } else if (type === 'relative_humidity') {
            tempArray[i] = data[i].relative_humidity;
        }
    }
    return tempArray;
}

function getSunTimes(time) {
    return new Date(time).getHours() + ':' + (new Date(time).getMinutes() < 10 ? '0' : '') + new Date(time).getMinutes();
}

function toggleDay() {

    $('#dayButton').addClass('btn-secondary').removeClass('btn-outline-secondary');
    $('#hourButton').removeClass('btn-secondary').addClass('btn-outline-secondary');
    $('#dayContainer').removeClass('d-none');
    $('#hourContainer').addClass('d-none');

}

function toggleHour() {

    $('#dayButton').removeClass('btn-secondary').addClass('btn-outline-secondary');
    $('#hourButton').addClass('btn-secondary').removeClass('btn-outline-secondary');
    $('#dayContainer').addClass('d-none');
    $('#hourContainer').removeClass('d-none');

}