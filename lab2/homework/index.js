const express = require('express');
const bodyParser = require('body-parser');
const fetch = require('node-fetch');
const port = 3000;
const app = express();
const encodeUrl = bodyParser.urlencoded({ extended: false });

require('dotenv').config();
app.use(express.static(__dirname + '/'));
app.use(bodyParser.urlencoded({extend:true}));
app.engine('html', require('ejs').renderFile);
app.set('views', __dirname);

const getDataFromUrl = async (url) => {
  try {
    let response = await fetch(url);
    return response.json();
  } catch (error) {
    return error;
  }
}

const getIssData = async () => {
  const issUrl = 'http://api.open-notify.org/iss-now.json';

  return await getDataFromUrl(issUrl);
}

const getWeatherData = async (data) => {
  const lat = data[0].latlng[0];
  const lon = data[0].latlng[1];
  const weatherUrl = `https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&appid=${process.env.WEATHER_API_KEY}`;

  return await getDataFromUrl(weatherUrl);
}

const getLocationData = async (data) => {
  const lon = data.iss_position.longitude;
  const lat = data.iss_position.latitude;
  const locationUrl = `https://api.geoapify.com/v1/geocode/reverse?lat=${lat}&lon=${lon}&apiKey=${process.env.GEOLOCATION_API_KEY}`;
  
  return await getDataFromUrl(locationUrl);
}

const getCountryData = async (country) => {
  const countryUrl = `https://restcountries.com/v2/name/${country}`;

  return await getDataFromUrl(countryUrl);
}

const getAirPollutionData = async (data) => {
  const countryData = await getCountryData(data.location);
  const lat = countryData[0].latlng[0];
  const lon = countryData[0].latlng[1];
  const start = new Date(data.startDate).getTime() / 1000;
  const end = new Date(data.endDate).getTime() / 1000;
  const pollutionUrl = `https://api.openweathermap.org/data/2.5/air_pollution/history?lat=${lat}&lon=${lon}&start=${start}&end=${end}&appid=${process.env.WEATHER_API_KEY}`;

  let pollutionData = await getDataFromUrl(pollutionUrl);
  let sum = pollutionData.list.map(entry => entry.main.aqi).reduce((prev, next) => prev + next);
  let average = sum/pollutionData.list.length;

  return parseInt(average);
}

const getExchangeRateData = async (data) => {
  const countryData = await getCountryData(data.location);
  const currencyCode = countryData[0].currencies[0].code;
  const start = data.startDate;
  const end = data.endDate;
  const exchangeRateUrl = `http://api.nbp.pl/api/exchangerates/rates/a/${currencyCode}/${start}/${end}/?format=json`;
  console.log(exchangeRateUrl);
  
  let exchangeRateData = await getDataFromUrl(exchangeRateUrl);
  let sum = exchangeRateData.rates.map(entry => entry.mid).reduce((prev, next) => prev + next);
  let average = sum/exchangeRateData.rates.length

  return [exchangeRateData.code, average.toFixed(2)];
}

app.get('/', async (req, res) => {
  const issData = await getIssData();  
  const locationData = await getLocationData(issData);

  let location = locationData.features[0].properties.country;
  if (!location) {
    location = locationData.features[0].properties.name;
  }
  res.render('form.html', {location: location});
});

app.post('/', encodeUrl, async (req, res) => {
  console.log('Form request:', req.body);
  let data = {...req.body};
  let location = data.location;

  if (location.toLowerCase().includes("sea") || location.toLowerCase().includes("ocean")) {
    res.send({error: "unable to conduct operation for ocean/sea"});
  } else {
    let measurementData;

    switch (data.measurement) {
      case 'Exchange rate':
        const [currencyCode, averageExchangeRate]= await getExchangeRateData(data);

        measurement = `average exchange rate form ${data.startDate} to ${data.startDate}`
        measurementData = "1 " + currencyCode + " ➝ " + averageExchangeRate + " PLN";
        break;
      default:
        const averageAirQuality = await getAirPollutionData(data);

        switch (averageAirQuality) {
          case 1:
            measurementData = "Good (5/5)";
            break;
          case 2:
            measurementData = "Fair (4/5)";
            break;
          case 2:
            measurementData = "Moderate (3/5)";
            break;
          case 2:
            measurementData = "Poor (2/5)";
            break;
          default:
            measurementData = "Very Poor (1/5)";
        }

        measurement = `average air quality form ${data.startDate} to ${data.endDate}`
    }

    let countryData = await getCountryData(location);
    let weatherData = await getWeatherData(countryData);

    res.render('country.html', {
      countryName: countryData[0].name, 
      flagUrl: countryData[0].flag, 
      capital: countryData[0].capital, 
      population: countryData[0].population,
      weather: weatherData.weather[0].icon,
      temperature: `${Math.round(weatherData.main.temp - 273)}°C`,
      weatherDescription: weatherData.weather[0].description,
      measurement: measurement,
      measurementData: measurementData
    });
  }
});

app.listen(port, () => {
  console.log(`Example app listening on port ${port}`);
});
