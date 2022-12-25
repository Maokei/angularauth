# server_flask
Serves events and protects routes with JWT.
```
python3 -m venv env
source env/bin/activate
pip install -r requirements.txt
python server.py
```


```
docker build -t flask-tutorial:latest .
docker run -d -p 5000:5000 flask-tutorial
```