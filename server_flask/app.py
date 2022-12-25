from flask import Flask, jsonify, request
from flask_restful import Resource, Api
import jwt
from datetime import datetime, timedelta
from functools import wraps

app = Flask(__name__)
api = Api(app)

app.config['SECRET_KEY'] = 'your secret key'
PUBLIC_EVENTS = [
    {
        "_id": "1",
        "name": "Repair car",
        "description": "Repair the blue car.",
        "date": "2023-08-08"
    },
    {
        "_id": "2",
        "name": "Repair car",
        "description": "Repair the faster red car.",
        "date": "2023-08-10"
    }
]
PRIVATE_EVENTS = [
    {
        "_id": "1",
        "name": "Pet dog",
        "description": "Pet the dog",
        "date": "2023-10-08"
    },
    {
        "_id": "2",
        "name": "Prepair the tank for combat",
        "description": "Resupply the Leopard tank, and clean the barrel.",
        "date": "2023-09-10"
    }
]

PREFIX = 'Bearer'

def get_token(header):
    bearer, _, token = header.partition(' ')
    if bearer != PREFIX:
        raise ValueError('Invalid token')
    return token

@app.route('/', methods = ['GET'])
def alive():
    msg = "Server is alive"
    return jsonify({'message': msg})

def token_check(func):
    @wraps(func)
    def decorated(*args, **kwargs):
        if 'Authorization' in request.headers:
            authHeader = request.headers['Authorization']
            token = get_token(authHeader)
        # return 401
        if not token:
            return {'message': "Try loging in first!"}, 401
        
        try:
            data = jwt.decode(token, options={"verify_signature": False}, key=app.config['SECRET_KEY'])
            print("decoded token: ", data)
            user = data
        except Exception as e:
            print(e)
            return {'message': 'Bad token!'}, 401
        return func(*args, **kwargs)
    return decorated

class Auth(Resource):
    def post(self):
        json = request.json
        email, password = json['email'], json['password']
        if email == "b@b.com" and password == 'a':
            token = jwt.encode({
                'public_id': 'admin',
                'exp' : datetime.utcnow() + timedelta(minutes = 30)
            }, app.config['SECRET_KEY'], algorithm="HS512")
            return token
        return ""

class Event(Resource):
    def get(self):
        return PUBLIC_EVENTS

class PrivateEvent(Resource):
    @token_check
    def get(self):
        return PRIVATE_EVENTS

api.add_resource(Event, '/api/events')
api.add_resource(PrivateEvent, '/api/special')
api.add_resource(Auth, '/api/login')

if __name__ == '__main__':
    app.run(debug = True)