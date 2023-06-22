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
        url: '/latestRecord',
        type: "GET",
        success: function (data) {
            setLatestHumidity(round(data.humidity, 1));
            setLatestTemperature(round(data.temperature, 1));
            setLatestGas(round(data.co2, 1));
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
    $('#humidityValue').text(String(data).replace('.', ',') + ' %');

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
    $('#temperatureValue').text(String(data).replace('.', ',') + ' °');

}

function setLatestGas(data) {
    // todo: when gas sensor is delivered
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

            if (!hourChart) {
                hourChart = createChart(elem, labels, temperature, humidity);
            } else {
                updateChart(hourChart, labels, temperature, humidity);
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

            if (!dayChart) {
                dayChart = createChart(elem, labels, temperature, humidity);
            } else {
                updateChart(dayChart, labels, temperature, humidity);
            }

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

function createChart(elem, labels, temperature, humidity) {

    return new Chart(elem, {
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

function updateChart(chart, labels, temperature, humidity) {
    chart.data.labels = labels;
    chart.data.datasets[0].data = temperature;
    chart.data.datasets[1].data = humidity;
    chart.update();
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

function round(value, precision) {
    let multiplier = Math.pow(10, precision || 0);
    return Math.round(value * multiplier) / multiplier;
}