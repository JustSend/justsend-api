from locust import between
from wallet_user import WalletUser

class LoadTestUser(WalletUser):
    wait_time = between(1, 3)
