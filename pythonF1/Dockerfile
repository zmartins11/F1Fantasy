FROM python:3.8-slim

WORKDIR /app

COPY requirements.txt .

COPY config.py .

RUN pip install --no-cache-dir -r requirements.txt

COPY config.py .
COPY app.py /app.py

CMD ["python", "/app.py"]
