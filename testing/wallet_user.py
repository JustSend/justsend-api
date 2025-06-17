from locust import HttpUser, task
from get_Firebase_Token import get_firebase_token

FIREBASE_TOKEN = get_firebase_token()

class WalletUser(HttpUser):
    host = "http://localhost:8080"

    def on_start(self):
        self.headers = {
            "Authorization": f"Bearer {FIREBASE_TOKEN}"
        }

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
        self.client.post("/api/wallet/deposit", json=payload, headers=self.headers)

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
        self.client.post("/api/wallet/send", json=payload, headers=self.headers)

    @task(1)
    def get_transactions(self):
        self.client.get("/api/wallet/transactions", headers=self.headers)

    @task(1)
    def withdraw(self):
        payload = {
            "amount": 5.0,
            "currency": "USD"
        }
        self.client.post("/api/wallet/withdraw", json=payload, headers=self.headers)
