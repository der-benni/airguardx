#!/usr/bin/python3
#Module importieren
import RPi.GPIO as GPIO
import time #für Code mit Zeit
import board
from busio import I2C
import adafruit_bme680
import requests
import datetime

# Create library object using our Bus I2C port
i2c = I2C(board.SCL, board.SDA)
bme680 = adafruit_bme680.Adafruit_BME680_I2C(i2c, debug=False)

# change this to match the location's pressure (hPa) at sea level
bme680.sea_level_pressure = 1000

# You will usually have to add an offset to account for the temperature of
# the sensor. This is usually around 5 degrees but varies by use. Use a
# separate temperature sensor to calibrate this one.
#temperature_offset = -5
temperature_offset = 0

print("Temperature: %0.1f C" % (bme680.temperature + temperature_offset))
print("Gas: %d ohm" % bme680.gas)
print("Humidity: %0.1f %%" % bme680.humidity)
print("Humidity rel: %0.1f %%" % bme680.relative_humidity)
print("Pressure: %0.3f hPa" % bme680.pressure)
print("Altitude = %0.2f meters" % bme680.altitude)

url = 'http://141.147.6.122:8080/parameters'
jsonData = {'temperature':bme680.temperature,'gas':bme680.gas,'humidity':bme680.humidity,'relative_humidity':bme680.relative_humidity,'pressure':bme680.pressure,'altitude':bme680.altitude,'timestamp':datetime.datetime.now().isoformat()}
x = requests.post(url, json = jsonData)
#print(x.text)

#GPiO vorbereiten
GPIO.setmode(GPIO.BCM) #Modus Board
GPIO.setup(17,GPIO.OUT) #Pin 11 als OUT

#print("start") #gib 'start' in der Konsole aus
if bme680.temperature > 20:
  for x in range (10): # for-Schleife
      GPIO.output(17,True) # Signal an!
      time.sleep(1) # Warte 1 Sekunde
      GPIO.output(17,False) # Signal aus
      time.sleep(1) # Warte 1 Sekunde

#print("end") # gib 'end' in der Konsole aus
GPIO.cleanup() # Aufräumen, Status der Pins wird zurückgesetzt, das ist wichtig!
