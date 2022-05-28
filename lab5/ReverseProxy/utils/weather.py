from geopy.geocoders import Nominatim
import ssl
import certifi
import geopy.geocoders
import requests

from config import WEATHER_API_KEY


def handle_weather_forecast(location_string, server_no):
    ctx = ssl.create_default_context(cafile=certifi.where())
    geopy.geocoders.options.default_ssl_context = ctx
    geolocator = Nominatim(user_agent="reverse_proxy_server")
    location = geolocator.geocode(location_string)
    lat = int(location.latitude)
    lon = int(location.longitude)
    URL = f"https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={WEATHER_API_KEY}"
    response = requests.get(url=URL)
    data = response.json()
    return f"[response from weather server {server_no}]\n" \
           f"Weather in {location}: {int(data['main']['temp'] - 273)}°C, {data['weather'][0]['description']}"