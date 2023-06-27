#!/usr/bin/python3
import time
import board
import busio
import adafruit_scd30
import RPi.GPIO as GPIO
import requests
import datetime
from requests.auth import HTTPBasicAuth

# SCD-30 has tempremental I2C with clock stretching, datasheet recommends
# starting at 50KHz
i2c = busio.I2C(board.SCL, board.SDA, frequency=50000)
scd = adafruit_scd30.SCD30(i2c)

scd.temperature_offset = 2
print("Temperature offset:", scd.temperature_offset)

scd.humidity_offset = 0
print("Humidity offset:", scd.humidity_offset)

# scd.measurement_interval = 4
#print("Measurement interval:", scd.measurement_interval)

#scd.self_calibration_enabled = True
#print("Self-calibration enabled:", scd.self_calibration_enabled)

# scd.ambient_pressure = 1100
#print("Ambient Pressure:", scd.ambient_pressure)

#scd.altitude = 300
print("Altitude:", scd.altitude, "meters above sea level")

# scd.forced_recalibration_reference = 409
#print("Forced recalibration reference:", scd.forced_recalibration_reference)
print("")

# Datenabfrage
data = scd.data_available
print("CO2:", scd.CO2)
print("Temperature:", scd.temperature-scd.temperature_offset)
print("Humidity::", scd.relative_humidity+scd.humidity_offset)
print("")

# Daten senden
url = 'http://141.147.6.122:8080/parameters'
jsonData = {'temperature':scd.temperature-scd.temperature_offset,'humidity':scd.relative_humidity+scd.humidity_offset,'co2':scd.CO2,'timestamp':datetime.datetime.now().isoformat()}
x = requests.post(url, json = jsonData, auth=HTTPBasicAuth('admin','admin'))


#GPiO vorbereiten
GPIO.setmode(GPIO.BCM) #Modus Board
GPIO.setup(17,GPIO.OUT) #Pin 11 als OUT

#print("start") #gib 'start' in der Konsole aus
if (scd.temperature-scd.temperature_offset) > 24 or (scd.relative_humidity+scd.humidity_offset) > 60 or scd.CO2 > 1000:
  for x in range (10): # for-Schleife
      GPIO.output(17,True) # Signal an!
      time.sleep(1) # Warte 1 Sekunde
      GPIO.output(17,False) # Signal aus
      time.sleep(1) # Warte 1 Sekunde
#print("end") # gib 'end' in der Konsole aus
GPIO.cleanup() # Aufräumen, Status der Pins wird zurückgesetzt, das ist wichtig!
