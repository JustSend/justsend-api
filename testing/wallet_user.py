from locust import HttpUser, task
from get_Firebase_Token import get_firebase_token
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

    @task(2)
    def register(self):
        email = f"locustuser_{''.join(random.choices(string.ascii_lowercase, k=8))}@example.com"
        uuid = ''.join(random.choices(string.ascii_lowercase + string.digits, k=32))
        payload = {"email": email, "uid": uuid}
        url = "/api/user/register"
        response = self.client.post(url, json=payload)
        if response.status_code == 200 or response.status_code == 201:
            print(f"[register] Success")
        else:
            print(f"[register] Failed: {email} {response.status_code} {response.text}")

    @task(5)
    def get_balances(self):
        self.client.get("/api/wallet", headers=self.headers)

    @task(2)
    def debin(self):
        payload = {
            "amount": 10.0,
            "currency": "USD",
            "bank_routing": 111111111
        }
        response = self.client.post("/api/wallet/debin", json=payload, headers=self.headers)
        if response.status_code == 200:
            print(f"[debin] Success")
        elif response.status_code == 400:
            print(f"[debin] Bad Request: {response.text}")
        elif response.status_code == 500:
            print(f"[debin] Server Error: {response.text}")
        else:
            print(f"[debin] Failed: {response.status_code} {response.text}")

    @task(3)
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
            print(f"[send] Success")
        elif response.status_code == 400:
            print(f"[send] Bad Request: {response.text}")
        else:
            print(f"[send] Failed: {response.status_code} {response.text}")

    @task(1)
    def get_transactions(self):
        response = self.client.get("/api/wallet/transactions", headers=self.headers)
        if response.status_code == 200:
            print(f"[get_transactions] Success")
        else:
            print(f"[get_transactions] Failed: {response.status_code} {response.text}")

    @task(2)
    def withdraw(self):
        payload = {
            "amount": 5.0,
            "currency": "USD"
        }
        response = self.client.post("/api/wallet/withdraw", json=payload, headers=self.headers)
        if response.status_code == 200:
            print(f"[withdraw] Success")
        elif response.status_code == 400:
            print(f"[withdraw] Bad Request: {response.text}")
        else:
            print(f"[withdraw] Failed: {response.status_code} {response.text}")
