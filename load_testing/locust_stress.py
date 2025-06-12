from locust import HttpUser, task, constant_pacing
from get_Firebase_Token import get_firebase_token

class WalletStressUser(HttpUser):
    wait_time = constant_pacing(0.05)
    host = "http://localhost:8080"

    def on_start(self):
        self.token = get_firebase_token()
        self.headers = {
            "Authorization": f"Bearer {self.token}"
        }

    @task(4)
    def get_balances(self):
        self.client.get("/api/wallet", headers=self.headers)

    @task(5)
    def deposit(self):
        payload = {
            "amount": 10.0,
            "currency": "USD",
            "bank_routing": 111111111
        }
        self.client.post("/api/wallet/deposit", json=payload, headers=self.headers)

    @task(2)
    def get_transactions(self):
        self.client.get("/api/wallet/transactions", headers=self.headers)

