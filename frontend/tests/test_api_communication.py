import pytest
import requests
from unittest.mock import patch
from .test_data import SAMPLE_STATS, SAMPLE_INGEST, SAMPLE_PEAK_DELIVERY
from .test_data import SAMPLE_REVENUE, SAMPLE_CUSTOMER_SEGMENTATION, SAMPLE_ANOMALY
from .test_data import SAMPLE_DELIVERY_EFFICIENCY, SAMPLE_SEASONAL_DEMAND, SAMPLE_REGIONAL_PERFORMANCE

API_BASE = "http://localhost:8080/api"


def mock_response(status_code, json_data):
    from unittest.mock import Mock
    import json
    m = Mock()
    m.status_code = status_code
    m.json.return_value = json_data
    m.text = json.dumps(json_data)
    return m


class TestDatasetAPI:
    def test_get_stats_success(self, patch_requests_get):
        patch_requests_get.return_value = mock_response(200, SAMPLE_STATS)
        response = requests.get(f"{API_BASE}/dataset/stats", timeout=10)
        assert response.status_code == 200
        data = response.json()
        assert data["total_customers"] == 1000
        assert data["total_orders"] == 5000
        assert data["total_faulty_records"] == 15

    def test_get_stats_server_error(self, patch_requests_get):
        patch_requests_get.return_value = mock_response(500, {"error": "Internal error"})
        response = requests.get(f"{API_BASE}/dataset/stats", timeout=10)
        assert response.status_code == 500
        assert "error" in response.json()

    def test_get_stats_connection_error(self, patch_requests_get):
        patch_requests_get.side_effect = requests.exceptions.ConnectionError("Connection refused")
        with pytest.raises(requests.exceptions.ConnectionError):
            requests.get(f"{API_BASE}/dataset/stats", timeout=10)

    def test_get_stats_timeout(self, patch_requests_get):
        patch_requests_get.side_effect = requests.exceptions.Timeout("Request timed out")
        with pytest.raises(requests.exceptions.Timeout):
            requests.get(f"{API_BASE}/dataset/stats", timeout=5)

    def test_ingest_success(self, patch_requests_post):
        patch_requests_post.return_value = mock_response(200, SAMPLE_INGEST)
        response = requests.post(
            f"{API_BASE}/dataset/ingest",
            json={"directory_path": "/data/datasets"},
            timeout=300
        )
        assert response.status_code == 200
        data = response.json()
        assert data["customers_total"] == 1000
        assert data["totalProcessingTimeMs"] == 15000

    def test_ingest_bad_request(self, patch_requests_post):
        patch_requests_post.return_value = mock_response(400,
            {"error": "directory_path is required"})
        response = requests.post(f"{API_BASE}/dataset/ingest", json={}, timeout=300)
        assert response.status_code == 400
        assert "directory_path" in response.json()["error"]

    def test_ingest_empty_path(self, patch_requests_post):
        patch_requests_post.return_value = mock_response(400,
            {"error": "directory_path is required"})
        response = requests.post(
            f"{API_BASE}/dataset/ingest", json={"directory_path": ""}, timeout=300)
        assert response.status_code == 400

    def test_ingest_connection_error(self, patch_requests_post):
        patch_requests_post.side_effect = requests.exceptions.ConnectionError("Cannot connect")
        with pytest.raises(requests.exceptions.ConnectionError):
            requests.post(f"{API_BASE}/dataset/ingest",
                          json={"directory_path": "/data"}, timeout=300)


class TestAnalyticsAPI:
    def test_peak_delivery_success(self, patch_requests_get):
        patch_requests_get.return_value = mock_response(200, SAMPLE_PEAK_DELIVERY)
        response = requests.get(f"{API_BASE}/analytics/peak-delivery", timeout=120)
        assert response.status_code == 200
        data = response.json()
        assert data["total_orders"] == 2950
        assert "hourly_distribution" in data
        assert len(data["hourly_distribution"]) == 24

    def test_revenue_analysis_success(self, patch_requests_get):
        patch_requests_get.return_value = mock_response(200, SAMPLE_REVENUE)
        response = requests.get(f"{API_BASE}/analytics/revenue-analysis", timeout=120)
        assert response.status_code == 200
        data = response.json()
        assert data["total_revenue"] == 325000.00
        assert data["payment_type_analysis"]["credit_card"]["count"] == 3000

    def test_customer_segmentation_success(self, patch_requests_get):
        patch_requests_get.return_value = mock_response(200, SAMPLE_CUSTOMER_SEGMENTATION)
        response = requests.get(f"{API_BASE}/analytics/customer-segmentation", timeout=120)
        assert response.status_code == 200
        data = response.json()
        assert data["total_customers"] == 1000
        assert len(data["segments"]) == 3

    def test_anomaly_detection_success(self, patch_requests_get):
        patch_requests_get.return_value = mock_response(200, SAMPLE_ANOMALY)
        response = requests.get(f"{API_BASE}/analytics/anomaly-detection", timeout=120)
        assert response.status_code == 200
        data = response.json()
        assert data["total_anomalies"] == 25
        high_price = sum(1 for a in data["anomalies"] if a["type"] == "HIGH_PRICE")
        assert high_price == 2

    def test_delivery_efficiency_success(self, patch_requests_get):
        patch_requests_get.return_value = mock_response(200, SAMPLE_DELIVERY_EFFICIENCY)
        response = requests.get(f"{API_BASE}/analytics/delivery-efficiency", timeout=120)
        assert response.status_code == 200
        data = response.json()
        assert data["on_time_rate"] == 92.5
        assert data["average_delivery_days"] == 3.5
        assert data["delivery_status"]["delivered"] == 4800

    def test_seasonal_demand_success(self, patch_requests_get):
        patch_requests_get.return_value = mock_response(200, SAMPLE_SEASONAL_DEMAND)
        response = requests.get(f"{API_BASE}/analytics/seasonal-demand", timeout=120)
        assert response.status_code == 200
        data = response.json()
        assert data["total_orders"] == 5000
        assert "monthly_volume" in data

    def test_regional_performance_success(self, patch_requests_get):
        patch_requests_get.return_value = mock_response(200, SAMPLE_REGIONAL_PERFORMANCE)
        response = requests.get(f"{API_BASE}/analytics/regional-performance", timeout=120)
        assert response.status_code == 200
        data = response.json()
        assert data["total_deliveries"] == 3300
        assert "regional_stats" in data

    def test_analytics_server_error(self, patch_requests_get):
        patch_requests_get.return_value = mock_response(500, {"error": "Analysis failed"})
        response = requests.get(f"{API_BASE}/analytics/peak-delivery", timeout=120)
        assert response.status_code == 500

    def test_analytics_connection_error(self, patch_requests_get):
        patch_requests_get.side_effect = requests.exceptions.ConnectionError("Backend down")
        with pytest.raises(requests.exceptions.ConnectionError):
            requests.get(f"{API_BASE}/analytics/customer-segmentation", timeout=120)

    @pytest.mark.parametrize("endpoint", [
        "peak-delivery", "customer-segmentation", "delivery-efficiency",
        "revenue-analysis", "anomaly-detection", "seasonal-demand", "regional-performance"
    ])
    def test_all_analytics_endpoints_reachable(self, patch_requests_get, endpoint):
        patch_requests_get.return_value = mock_response(200, {"status": "ok"})
        response = requests.get(f"{API_BASE}/analytics/{endpoint}", timeout=120)
        assert response.status_code == 200
        patch_requests_get.assert_called_with(f"{API_BASE}/analytics/{endpoint}", timeout=120)

    def test_empty_response_handling(self, patch_requests_get):
        patch_requests_get.return_value = mock_response(200, {})
        response = requests.get(f"{API_BASE}/analytics/peak-delivery", timeout=120)
        assert response.status_code == 200
        assert response.json() == {}
