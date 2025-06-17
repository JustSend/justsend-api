import requests
from dotenv import load_dotenv
import os

load_dotenv()

def get_firebase_token():
    url = f"https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key={os.getenv('FIREBASE_API_KEY')}"
    payload = {
        "email": "token@test.com",
        "password": "test123",
        "returnSecureToken": True
    }
    response = requests.post(url, json=payload)
    try:
        response.raise_for_status()
        return response.json()["idToken"]
    except requests.exceptions.HTTPError:
        print("Status code:", response.status_code)
        print("Response:", response.text)
        raise