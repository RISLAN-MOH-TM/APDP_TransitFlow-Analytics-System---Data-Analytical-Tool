import pytest
import json
from unittest.mock import patch, Mock


API_BASE = "http://localhost:8080/api"


@pytest.fixture
def mock_stats_response():
    return {
        "total_customers": 1000,
        "total_orders": 5000,
        "total_products": 300,
        "total_sellers": 100,
        "total_order_items": 8000,
        "total_payments": 5000,
        "total_deliveries": 4800,
        "total_faulty_records": 15
    }


@pytest.fixture
def mock_ingest_response():
    return {
        "totalProcessingTimeMs": 15000,
        "customers_total": 1000,
        "customers_valid": 995,
        "customers_duplicates": 5,
        "orders_total": 5000,
        "orders_valid": 4980,
        "orders_duplicates": 20,
        "products_total": 300,
        "products_valid": 300,
        "products_duplicates": 0,
        "order_items_total": 8000,
        "order_items_valid": 7950,
        "payments_total": 5000,
        "payments_valid": 5000,
        "sellers_total": 100,
        "sellers_valid": 100
    }


@pytest.fixture
def mock_peak_delivery_response():
    return {
        "total_orders": 5000,
        "hourly_distribution": {
            "0": 120, "1": 80, "2": 45, "3": 30, "4": 25, "5": 40,
            "6": 90, "7": 200, "8": 350, "9": 420, "10": 450, "11": 430,
            "12": 380, "13": 400, "14": 390, "15": 370, "16": 340,
            "17": 320, "18": 280, "19": 250, "20": 220, "21": 190,
            "22": 160, "23": 130
        },
        "daily_distribution": {
            "2024-01-01": 150, "2024-01-02": 165, "2024-01-03": 140
        },
        "day_of_week_distribution": {
            "Monday": 800, "Tuesday": 750, "Wednesday": 720,
            "Thursday": 780, "Friday": 850, "Saturday": 600,
            "Sunday": 500
        }
    }


@pytest.fixture
def mock_revenue_response():
    return {
        "total_revenue": 1250000.50,
        "average_order_value": 250.10,
        "payment_type_analysis": {
            "credit_card": {"count": 3000, "total_value": 750000.00},
            "boleto": {"count": 1000, "total_value": 250000.00},
            "voucher": {"count": 500, "total_value": 125000.50},
            "debit_card": {"count": 500, "total_value": 125000.00}
        },
        "monthly_revenue": {
            "2024-01": 120000.00,
            "2024-02": 95000.00,
            "2024-03": 110000.00
        }
    }


@pytest.fixture
def mock_customer_segmentation_response():
    return {
        "total_customers": 1000,
        "segments": {
            "High Value": {"count": 150, "avg_spend": 2500.00},
            "Medium Value": {"count": 350, "avg_spend": 1200.00},
            "Low Value": {"count": 500, "avg_spend": 300.00}
        },
        "state_distribution": {
            "SP": 400, "RJ": 200, "MG": 150, "RS": 100, "PR": 80, "Other": 70
        }
    }


@pytest.fixture
def mock_anomaly_response():
    return {
        "total_anomalies": 25,
        "anomalies": [
            {"type": "HIGH_PRICE", "order_id": "1001", "value": 9999.99, "date": "2024-01-15"},
            {"type": "HIGH_PRICE", "order_id": "1002", "value": 8500.00, "date": "2024-01-16"},
            {"type": "DELIVERY_DELAY", "order_id": "2001", "delay_days": 15, "date": "2024-01-20"},
            {"type": "UNUSUAL_QUANTITY", "order_id": "3001", "quantity": 500, "date": "2024-01-25"}
        ]
    }


@pytest.fixture
def mock_empty_response():
    return {}


@pytest.fixture
def mock_error_response():
    return {"error": "Internal server error"}


def create_mock_response(status_code, json_data):
    mock_resp = Mock()
    mock_resp.status_code = status_code
    mock_resp.json.return_value = json_data
    mock_resp.text = json.dumps(json_data)
    return mock_resp


@pytest.fixture
def patch_requests_get():
    with patch("requests.get") as mock:
        yield mock


@pytest.fixture
def patch_requests_post():
    with patch("requests.post") as mock:
        yield mock
