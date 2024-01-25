from flask import Flask, jsonify, request
from youtube_search import YoutubeSearch
from flask_cors import CORS  
import requests
from datetime import datetime
from config import open_weather_api
import wikipedia

app = Flask(__name__)
CORS(app)

@app.route('/video-search', methods=['POST'])
def video_search():
    try:
        query = request.json['query']
        video_id = search_videos(query)
        return jsonify({'videoId': video_id})
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/weather', methods=['GET'])
def weather():
    try:
        print('Calling the APi Weather')
        country = request.args.get('country', type=str)
        city = request.args.get('city', default='Lisbon', type=str)
        hour = request.args.get('hour', type=str)
        day = request.args.get('day', type=str)
        month = request.args.get('month', type=str)
        print('city: ' + city + '||' + 'hour:' + hour +  '||' + 'day:' + day + '||' + 'month:' + month )
        weather_data = get_weather(country, city, hour, day, month)
        return jsonify({'weather': weather_data})
    except Exception as e:
        return jsonify({'error': str(e)}), 500

def search_videos(query):
    videos = YoutubeSearch(query, max_results=1).to_dict()
    return videos[0]['id'] if videos else None

def get_weather(country, city, hour, day, month):
    print('GETTING WEATHER...')
    weather_url = f'https://api.openweathermap.org/data/2.5/forecast?q={city}&appid={open_weather_api}'
    try : 
        weather_response = requests.get(weather_url)
        weather_data = weather_response.json()
        # in case the city is not in open weather api
        if weather_response.status_code == 404:
            print(f"City '{city}' not found.")
            capital_for_city_not_found = wikipedia.page(f'capital of {country}').title
            weather_url = f'https://api.openweathermap.org/data/2.5/forecast?q={capital_for_city_not_found}&appid={open_weather_api}'
            weather_response = requests.get(weather_url)
            weather_data = weather_response.json()
        forecast = []
        for item in weather_data['list']:
            date_str = item['dt_txt']
            date = datetime.strptime(date_str, '%Y-%m-%d %H:%M:%S')
            weather = item['weather'][0]['description']
            temperature_kelvin = item['main']['temp']
            humidity = item['main']['humidity']
            wind_speed = item['wind']['speed']
            temperature_celcius = round(temperature_kelvin - 273.15, 1)

            if date.hour == hour and date.day == day and date.month == month:
                forecast.append({
                    'date': date,
                    'weather': weather,
                    'temperature': temperature_celcius,
                    'humidity' : humidity,
                    'wind_speed' : wind_speed,
                    'current_weather': False
                })
        if not forecast:
            print(f'No forecast found for {day}/{month}. Getting current weather.')
            current_weather_url = f'https://api.openweathermap.org/data/2.5/weather?q={city}&appid={open_weather_api}'
            current_weather_response = requests.get(current_weather_url)
            current_weather_data = current_weather_response.json()

            current_weather = {
                'weather': current_weather_data['weather'][0]['description'],
                'temperature': round(current_weather_data['main']['temp'] - 273.15, 1),
                'humidity': current_weather_data['main']['humidity'],
                'wind_speed': current_weather_data['wind']['speed'],
                'current_weather': True
            }
            print(current_weather)
            return [current_weather]
        print('PRINTING FORECAST....')
        print(forecast)
        return forecast
    except Exception as e:
        print(f'Error: {e}')



if __name__ == '__main__':
    app.run(debug=True)
