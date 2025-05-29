from locust import HttpUser, task, between

from get_Firebase_Token import get_firebase_token

class WalletUser(HttpUser):
    wait_time = between(1, 3)
    host = "http://localhost:8080"

    def on_start(self):
        self.token = get_firebase_token()
        self.headers = {
            "Authorization": f"Bearer {self.token}"
        }

    @task(1)
    def get_balances(self):
        self.client.get("/api/wallet/balances", headers=self.headers)

    @task(2)
    def deposit(self):
        payload = {
            "amount": 10.0,
            "currency": "USD"
        }
        self.client.post("/api/wallet/deposit", json=payload, headers=self.headers)

    @task(1)
    def withdraw(self):
        payload = {
            "amount": 5.0,
            "currency": "USD"
        }
        self.client.post("/api/wallet/withdraw", json=payload, headers=self.headers)

