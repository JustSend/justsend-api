from locust import HttpUser, task
from get_Firebase_Token import get_firebase_token
import requests
import os
import random
import string

FIREBASE_TOKEN = get_firebase_token()
FIREBASE_API_KEY = os.getenv("FIREBASE_API_KEY")

class WalletUser(HttpUser):
    host = "http://localhost:8080"

    def on_start(self):
        self.headers = {
            "Authorization": f"Bearer {FIREBASE_TOKEN}"
        }

    @task(4)
    def register(self):
        email = f"locustuser_{''.join(random.choices(string.ascii_lowercase, k=8))}@example.com"
        password = "TestPassword123!"
        url = f"https://identitytoolkit.googleapis.com/v1/accounts:signUp?key={FIREBASE_API_KEY}"
        payload = {"email": email, "password": password, "returnSecureToken": True}
        response = requests.post(url, json=payload)
        if response.status_code == 200:
            print(f"Registered user: {email}")
        else:
            print(f"Failed to register user: {email}, {response.text}")

    @task(2)
    def get_balances(self):
        self.client.get("/api/wallet", headers=self.headers)

    @task(3)
    def deposit(self):
        payload = {
            "amount": 10.0,
            "currency": "USD",
            "bank_routing": 111111111
        }
        response = self.client.post("/api/wallet/deposit", json=payload, headers=self.headers)
        if response.status_code == 200:
            print(f"[deposit] Success: {response.json()}")
        else:
            print(f"[deposit] Failed: {response.status_code} {response.text}")

    @task(1)
    def send(self):
        payload = {
            "to": {
                "email": "iii@iii.com"
            },
            "money": {
                "currency": "USD",
                "amount": 1.0
            }
        }
        response = self.client.post("/api/wallet/send", json=payload, headers=self.headers)
        if response.status_code == 200:
            print(f"[send] Success: {response.json()}")
        else:
            print(f"[send] Failed: {response.status_code} {response.text}")

    @task(1)
    def get_transactions(self):
        response = self.client.get("/api/wallet/transactions", headers=self.headers)
        if response.status_code == 200:
            print(f"[get_transactions] Success: {response.json()}")
        else:
            print(f"[get_transactions] Failed: {response.status_code} {response.text}")

    @task(1)
    def withdraw(self):
        payload = {
            "amount": 5.0,
            "currency": "USD"
        }
        self.client.post("/api/wallet/withdraw", json=payload, headers=self.headers)
