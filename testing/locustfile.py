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
        self.client.get("/api/wallet", headers=self.headers)

    @task(2)
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
            "money":{
                "currency": "USD",
                "amount": 1.0
            }

        }
        self.client.post("/api/wallet/send", json=payload, headers=self.headers)

    @task(2)
    def get_transactions(self):
        self.client.get("/api/wallet/transactions", headers=self.headers)

    @task(1)
    def withdraw(self):
        payload = {
            "amount": 5.0,
            "currency": "USD"
        }
        self.client.post("/api/wallet/withdraw", json=payload, headers=self.headers)

