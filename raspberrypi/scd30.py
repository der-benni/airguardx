#!/usr/bin/python3

import time
import board
import busio
import adafruit_scd30
import RPi.GPIO as GPIO
import requests
import datetime
from requests.auth import HTTPBasicAuth

# SCD-30 Sensor initialisieren
i2c = busio.I2C(board.SCL, board.SDA, frequency=50000)
scd = adafruit_scd30.SCD30(i2c)

# Offset-Werte für Temperatur und Luftfeuchtigkeit einstellen
scd.temperature_offset = 2
scd.humidity_offset = 0

# Aktuelle Höhe über dem Meeresspiegel einstellen
scd.altitude = 300

# Funktion für Daten senden
def send_data():
    url = 'http://141.147.6.122:8080/parameters'
    jsonData = {
        'temperature': temperature,
        'humidity': humidity,
        'co2': co2,
        'timestamp': datetime.datetime.now().isoformat()
    }
    x = requests.post(url, json=jsonData, auth=HTTPBasicAuth('admin', 'admin'))
    time.sleep(5)  # 5 Sekunden warten, um aktuelle Werte beim roomStatus-Abruf zu berücksichtigen

# Funktion für LED-Blinken
def blink_led():
    for x in range(10):
        GPIO.output(17, True)
        time.sleep(1)
        GPIO.output(17, False)
        time.sleep(1)

# Funktion für das Lesen der Schwellwerte aus der externen Datei
def read_thresholds():
    try:
        with open('schwellwerte.txt', 'r') as file:
            data = file.read()
            thresholds = data.split(',')
            lower_temperature_threshold = float(thresholds[0])
            upper_temperature_threshold = float(thresholds[1])
            lower_humidity_threshold = float(thresholds[2])
            upper_humidity_threshold = float(thresholds[3])
            co2_threshold = float(thresholds[4])
    except FileNotFoundError:
        # Falls die Datei nicht vorhanden ist, verwende Standardwerte
        lower_temperature_threshold = 20.0
        upper_temperature_threshold = 25.0
        lower_humidity_threshold = 40.0
        upper_humidity_threshold = 60.0
        co2_threshold = 1000.0
    
    return lower_temperature_threshold, upper_temperature_threshold, lower_humidity_threshold, upper_humidity_threshold, co2_threshold

# Funktion für das Speichern der Schwellwerte in die externe Datei
def save_thresholds(lower_temperature_threshold, upper_temperature_threshold, lower_humidity_threshold, upper_humidity_threshold, co2_threshold):
    with open('schwellwerte.txt', 'w') as file:
        file.write(f"{lower_temperature_threshold},{upper_temperature_threshold},{lower_humidity_threshold},{upper_humidity_threshold},{co2_threshold}")

# GPIO vorbereiten
GPIO.setmode(GPIO.BCM)
GPIO.setup(17, GPIO.OUT)

# Datenabfrage
data = scd.data_available
co2 = scd.CO2
temperature = scd.temperature - scd.temperature_offset
humidity = scd.relative_humidity + scd.humidity_offset

# Daten senden
send_data()

# Abfrage an http://141.147.6.122:8080/roomStatus
try:
    response = requests.get('http://141.147.6.122:8080/roomStatus')
    if response.text == '1':
        blink_led()
except requests.exceptions.RequestException:
    # Bei Nichterreichbarkeit des Servers, Schwellwerte aus der externen Datei lesen
    lower_temperature_threshold, upper_temperature_threshold, lower_humidity_threshold, upper_humidity_threshold, co2_threshold = read_thresholds()
    if temperature > upper_temperature_threshold or temperature < lower_temperature_threshold or humidity > upper_humidity_threshold or humidity < lower_humidity_threshold or co2 > co2_threshold:
        blink_led()

# Schwellwerte abfragen und speichern
try:
    profile_values_response = requests.get('http://141.147.6.122:8080/getProfileValues')
    profile_values = profile_values_response.json()
    lower_temperature_threshold = profile_values.get('lower_temperature_threshold', 20.0)  # Standardwert: 20 Grad Celsius
    upper_temperature_threshold = profile_values.get('upper_temperature_threshold', 25.0)  # Standardwert: 25 Grad Celsius
    lower_humidity_threshold = profile_values.get('lower_humidity_threshold', 40.0)  # Standardwert: 40 % Luftfeuchtigkeit
    upper_humidity_threshold = profile_values.get('upper_humidity_threshold', 60.0)  # Standardwert: 60 % Luftfeuchtigkeit
    co2_threshold = profile_values.get('co2_threshold', 1000.0)  # Standardwert: 1000 ppm CO2
    save_thresholds(lower_temperature_threshold, upper_temperature_threshold, lower_humidity_threshold, upper_humidity_threshold, co2_threshold)
except requests.exceptions.RequestException:
    # Falls das Abrufen der Schwellwerte nicht möglich ist, verwende die zuvor gespeicherten Werte
    pass

GPIO.cleanup()
