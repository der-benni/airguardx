let dayChart = null;
let hourChart = null;
let dayChartOutside = null;

$(window).on('load', function () {

    $('#dayButton').on('click', function () {
        toggleDay();
    });

    $('#hourButton').on('click', function () {
        toggleHour();
    });

    setSensorStatus();
    setLatestData();
    setHourlyData();
    setDailyData();
    setDailyDataOutside();

    setInterval(function () {
        setSensorStatus();
        setLatestData();
        setHourlyData();
        setDailyData();
        setDailyDataOutside();
        }, 30000);
});

function getTime(time) {
    return new Date(time).getHours() + ':' + new Date(time).getMinutes();
}

function getDayMonthYear(data) {
    let date = new Date(data);
    return date.getDate() + '.' + (date.getMonth() + 1) + '.' + date.getFullYear();
}

function setSensorStatus() {
    $.ajax({
        url: '/sensorStatus',
        type: "GET",
        success: function (data) {

            $('#statusIndicator').removeClass();
            $('#statusText').removeClass();

            if (data.status === 1) {
                $('#statusText').text('Sensor online');
                $('#statusText').addClass('ps-2 text-success');
                $('#statusIndicator').addClass('bi bi-wifi text-success');
            } else {
                $('#statusText').text('Sensor offline');
                $('#statusText').addClass('ps-2 text-danger');
                $('#statusIndicator').addClass('bi bi-wifi text-danger');
            }

        }
    });
}

function setLatestData() {
    $.ajax({
        url: '/getProfileValues',
        type: "GET",
        success: function (values) {
            $.ajax({
                url: '/latestRecord',
                type: "GET",
                success: function (data) {
                    setLatestHumidity(round(data.humidity, 1), values);
                    setLatestTemperature(round(data.temperature, 1), values);
                    setLatestGas(round(data.co2, 1));
                }
            });
        }
    });
}

function setLatestHumidity(data, values) {

    $('#humiditySmiley').removeClass();
    $('#humidityProgress').removeClass();
    $('#humidityValue').removeClass();

    if (data >= values[0] && data <= values[1]) {
        // good
        $('#humiditySmiley').addClass('bi bi-emoji-smile-fill text-success position-absolute');
        $('#humidityProgress').addClass('progress-bar progress-bar--green');
        $('#humidityValue').addClass('text-success');
    } else if ((data >= values[0] - 5 && data < values[0]) || (data > values[1] && data <= values[1] + 5)) {
        // middle
        $('#humiditySmiley').addClass('bi bi-emoji-neutral-fill color-orange position-absolute');
        $('#humidityProgress').addClass('progress-bar progress-bar--warning');
        $('#humidityValue').addClass('color-orange');
    } else if (data < values[0] - 5 || data > values[1] + 5) {
        // bad
        $('#humiditySmiley').addClass('bi bi-emoji-frown-fill text-danger position-absolute');
        $('#humidityProgress').addClass('progress-bar progress-bar--danger');
        $('#humidityValue').addClass('text-danger');
    }

    $('#humidityProgress').css('width', data + '%');
    $('#humidityValue').text(String(data).replace('.', ',') + ' %');

}

function setLatestTemperature(data, values) {

    $('#temperatureSmiley').removeClass();
    $('#temperatureProgress').removeClass();
    $('#temperatureValue').removeClass();

    if (data >= values[2] && data <= values[3]) {
        // good
        $('#temperatureSmiley').addClass('bi bi-emoji-smile-fill text-success position-absolute');
        $('#temperatureProgress').addClass('progress-bar progress-bar--green');
        $('#temperatureValue').addClass('text-success');
    } else if ((data >= values[2] - 2 && data < values[2]) || (data > values[3] && data <= values[3] + 2)) {
        // middle
        $('#temperatureSmiley').addClass('bi bi-emoji-neutral-fill color-orange position-absolute');
        $('#temperatureProgress').addClass('progress-bar progress-bar--warning');
        $('#temperatureValue').addClass('color-orange');
    } else if (data < values[2] - 2 || data > values[3] + 2) {
        // bad
        $('#temperatureSmiley').addClass('bi bi-emoji-frown-fill text-danger position-absolute');
        $('#temperatureProgress').addClass('progress-bar progress-bar--danger');
        $('#temperatureValue').addClass('text-danger');
    }

    $('#temperatureProgress').css('width', (data * 2) + '%');
    $('#temperatureValue').text(String(data).replace('.', ',') + ' °');

}

function setLatestGas(data) {
    $('#co2Smiley').removeClass();
    $('#co2Progress').removeClass();
    $('#co2Value').removeClass();

    if (data < 1000) {
        // good
        $('#co2Smiley').addClass('bi bi-emoji-smile-fill text-success position-absolute');
        $('#co2Progress').addClass('progress-bar progress-bar--green');
        $('#co2Value').addClass('text-success');
    } else if (data >= 1000 && data < 2000) {
        // middle
        $('#co2Smiley').addClass('bi bi-emoji-neutral-fill color-orange position-absolute');
        $('#co2Progress').addClass('progress-bar progress-bar--warning');
        $('#co2Value').addClass('color-orange');
    } else if (data > 2000) {
        // bad
        $('#co2Smiley').addClass('bi bi-emoji-frown-fill text-danger position-absolute');
        $('#co2Progress').addClass('progress-bar progress-bar--danger');
        $('#co2Value').addClass('text-danger');
    }

    $('#co2Progress').css('width', (data < 2000 ? (data / 2000) * 100 : 100) + '%');
    $('#co2Value').text(String(data).replace('.', ',') + ' ppm');
}

function setHourlyData() {

    $.ajax({
        url: '/hourRecords',
        type: "GET",
        success: function (reversedData) {

            let data = reversedData.reverse();
            let elem = document.getElementById('hourChart');
            let labels = getFromParams(data, data.length, 'timestamp');
            let temperature = getFromParams(data, data.length, 'temperature');
            let humidity = getFromParams(data, data.length, 'humidity');
            let co2 = getFromParams(data, data.length, 'co2');

            if (!hourChart) {
                hourChart = createChart(elem, labels, temperature, humidity, co2);
            } else {
                updateChart(hourChart, labels, temperature, humidity, co2);
            }

        }
    });
}

function setDailyData() {

    $.ajax({
        url: '/dayRecords',
        type: "GET",
        success: function (reversedData) {

            let data = reversedData.reverse();
            let elem = document.getElementById('dayChart');
            let labels = getFromParams(data, data.length, 'timestamp');
            let temperature = getFromParams(data, data.length, 'temperature');
            let humidity = getFromParams(data, data.length, 'humidity');
            let co2 = getFromParams(data, data.length, 'co2');

            if (!dayChart) {
                dayChart = createChart(elem, labels, temperature, humidity, co2);
            } else {
                updateChart(dayChart, labels, temperature, humidity, co2);
            }

        }
    });

}

function setDailyDataOutside() {

    $.ajax({
        url: 'https://api.open-meteo.com/v1/forecast?latitude=50.26&longitude=10.96&hourly=temperature_2m,relativehumidity_2m,windspeed_10m&daily=sunrise,sunset&current_weather=true&forecast_days=1&timezone=Europe%2FBerlin',
        type: "GET",
        success: function (data) {
            $('#sunrise').text(formatTimes(data['daily']['sunrise'][0]));
            $('#sunset').text(formatTimes(data['daily']['sunset'][0]));
            $('#dateOutside').text(getDayMonthYear(data['current_weather']['time']));
            $('#outsideHumidity').text(String(data['hourly']['relativehumidity_2m'][data['hourly']['time'].indexOf(data['current_weather']['time'])]).replace('.', ',') + ' %');
            $('#outsideTemperature').text(String(data['current_weather']['temperature']).replace('.', ',') + ' °C');
            $('#outsideWindspeed').text(String(data['current_weather']['windspeed']).replace('.', ',') + ' km/h');
            setOutsideChart(data);
        }
    });

}

function setOutsideChart(data) {

    const elem = document.getElementById('outsideDay');

    let labels = getHourlyParamsOutside(data, 24, 'time');
    let temperature = getHourlyParamsOutside(data, 24, 'temperature_2m');
    let humidity = getHourlyParamsOutside(data, 24, 'relativehumidity_2m');

    if (!dayChartOutside) {
        dayChartOutside = createChart(elem, labels, temperature, humidity);
    } else {
        updateChart(dayChartOutside, labels, temperature, humidity);
    }

}

function createChart(elem, labels, temperature, humidity, co2) {

    return new Chart(elem, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: 'Temperatur (°C)',
                data: temperature,
                borderWidth: 3,
                fill: false,
                borderColor: '#ffbf69',
                tension: 0.2,
                yAxisID: 'A'
            },{
                label: 'Luftfeuchtigkeit (%)',
                data: humidity,
                borderWidth: 3,
                fill: false,
                borderColor: '#93aeed',
                tension: 0.2,
                yAxisID: 'B'
            },{
                label: 'CO2 (ppm)',
                data: co2,
                borderWidth: 3,
                fill: false,
                borderColor: '#787878',
                tension: 0.2,
                yAxisID: 'C'
            }]
        },
        options: {
            scales: {
                A: {
                    type: 'linear',
                    position: 'left',
                    min: 0,
                    max: 50,
                    border: {
                        color: '#ffbf69',
                        width: 3,
                    },
                },
                B: {
                    type: 'linear',
                    position: 'left',
                    min: 0,
                    max: 100,
                    border: {
                        color: '#93aeed',
                        width: 3,
                    },
                },
                C: {
                    type: 'linear',
                    position: 'right',
                    min: 0,
                    max: 3000,
                    border: {
                        color: '#787878',
                        width: 3,
                    },
                }
            },
            aspectRatio: 4
        }
    });

}

function updateChart(chart, labels, temperature, humidity, co2) {
    chart.data.labels = labels;
    chart.data.datasets[0].data = temperature;
    chart.data.datasets[1].data = humidity;
    chart.data.datasets[2].data = co2;
    chart.update();
}

function getHourlyParamsOutside(data, count, type) {
    let tempArray = [];
    for (let i = 0; i < count; i++) {
        if (type === 'time') {
            tempArray[i] = formatTimes(data['hourly'][type][i]);
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
            tempArray[i] = formatTimes(data[i].timestamp);
        } else if (type === 'temperature') {
            tempArray[i] = data[i].temperature;
        } else if (type === 'humidity') {
            tempArray[i] = data[i].humidity;
        } else if (type === 'co2') {
            tempArray[i] = data[i].co2;
        }
    }
    return tempArray;
}

function formatTimes(time) {
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

function round(value, precision) {
    let multiplier = Math.pow(10, precision || 0);
    return Math.round(value * multiplier) / multiplier;
}