from locust import constant
from wallet_user import WalletUser

class StressTestUser(WalletUser):
    wait_time = constant(0.1)
