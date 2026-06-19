import pytest
import pandas as pd
from .test_data import (
    SAMPLE_PEAK_DELIVERY, SAMPLE_REVENUE, SAMPLE_CUSTOMER_SEGMENTATION,
    SAMPLE_ANOMALY, SAMPLE_INGEST, SAMPLE_STATS, SAMPLE_DELIVERY_EFFICIENCY,
    SAMPLE_SEASONAL_DEMAND, SAMPLE_REGIONAL_PERFORMANCE
)


class TestPeakDeliveries:
    def test_hourly_dataframe(self):
        hd = SAMPLE_PEAK_DELIVERY["hourly_distribution"]
        df = pd.DataFrame(list(hd.items()), columns=["Hour", "Orders"]).sort_values("Hour")
        assert len(df) == 24
        assert df["Orders"].sum() == SAMPLE_PEAK_DELIVERY["total_orders"]

    def test_daily_dataframe_truncation(self):
        dd = SAMPLE_PEAK_DELIVERY["daily_distribution"]
        df = pd.DataFrame(list(dd.items()), columns=["Date", "Orders"])
        df = df.sort_values("Date", ascending=False).head(30).sort_values("Date")
        assert len(df) == 3

    def test_day_of_week_dataframe(self):
        dw = SAMPLE_PEAK_DELIVERY["day_of_week_distribution"]
        df = pd.DataFrame(list(dw.items()), columns=["Day", "Orders"])
        assert len(df) == 7
        assert df[df["Day"] == "Friday"]["Orders"].values[0] == 850


class TestRevenueAnalysis:
    def test_monthly_dataframe(self):
        mr = SAMPLE_REVENUE["monthly_revenue"]
        df = pd.DataFrame(list(mr.items()), columns=["Month", "Revenue"]).sort_values("Month")
        assert len(df) == 3
        assert df["Revenue"].sum() == pytest.approx(SAMPLE_REVENUE["total_revenue"], rel=0.1)

    def test_payment_type_breakdown(self):
        pts = SAMPLE_REVENUE["payment_type_analysis"]
        df = pd.DataFrame([
            {"Payment Type": k, "Count": v["count"], "Total Value": v["total_value"]}
            for k, v in pts.items()
        ])
        assert len(df) == 4
        assert df[df["Payment Type"] == "credit_card"]["Count"].values[0] == 3000


class TestCustomerSegmentation:
    def test_segment_counts(self):
        segs = SAMPLE_CUSTOMER_SEGMENTATION["segments"]
        df = pd.DataFrame([
            {"Segment": k, "Count": v["count"], "Avg Spend": v["avg_spend"]}
            for k, v in segs.items()
        ])
        assert len(df) == 3
        assert df["Count"].sum() == SAMPLE_CUSTOMER_SEGMENTATION["total_customers"]

    def test_state_distribution(self):
        sd = SAMPLE_CUSTOMER_SEGMENTATION["state_distribution"]
        df = pd.DataFrame(list(sd.items()), columns=["State", "Customers"])
        assert df["Customers"].sum() == 1000


class TestAnomalyDetection:
    def test_high_price_filter(self):
        anomalies = SAMPLE_ANOMALY["anomalies"]
        high_price = [a for a in anomalies if a["type"] == "HIGH_PRICE"]
        assert len(high_price) == 2
        for a in high_price:
            assert a["value"] >= 8500

    def test_anomaly_dataframe(self):
        df = pd.DataFrame(SAMPLE_ANOMALY["anomalies"])
        assert len(df) == 4
        assert list(df.columns) == ["type", "order_id", "value", "date"]

    def test_anomaly_types(self):
        types = [a["type"] for a in SAMPLE_ANOMALY["anomalies"]]
        assert types == ["HIGH_PRICE", "HIGH_PRICE", "DELIVERY_DELAY", "UNUSUAL_QUANTITY"]


class TestIngestionResults:
    def test_valid_counts(self):
        d = SAMPLE_INGEST
        assert d["customers_total"] > d["customers_valid"]
        assert d["orders_total"] > d["orders_valid"]
        assert d["products_total"] == d["products_valid"]

    def test_processing_time_present(self):
        assert "totalProcessingTimeMs" in SAMPLE_INGEST

    def test_total_valid_records(self):
        d = SAMPLE_INGEST
        total = (d["customers_valid"] + d["orders_valid"] + d["products_valid"]
                 + d["order_items_valid"] + d["payments_valid"] + d["sellers_valid"])
        assert total > 0


class TestDeliveryEfficiency:
    def test_on_time_rate(self):
        assert SAMPLE_DELIVERY_EFFICIENCY["on_time_rate"] > 90

    def test_status_breakdown(self):
        ds = SAMPLE_DELIVERY_EFFICIENCY["delivery_status"]
        assert ds["delivered"] + ds["processing"] + ds["cancelled"] == \
               SAMPLE_DELIVERY_EFFICIENCY["total_orders"]


class TestSeasonalDemand:
    def test_monthly_volume_length(self):
        assert len(SAMPLE_SEASONAL_DEMAND["monthly_volume"]) == 4

    def test_seasonal_peaks(self):
        peaks = SAMPLE_SEASONAL_DEMAND["seasonal_peaks"]
        assert peaks["January"] == "High"
        assert peaks["December"] == "High"


class TestRegionalPerformance:
    def test_regional_stats_dataframe(self):
        rs = SAMPLE_REGIONAL_PERFORMANCE["regional_stats"]
        df = pd.DataFrame([
            {"Region": k, **v} for k, v in rs.items()
        ])
        assert len(df) == 3
        sp = df[df["Region"] == "SP"]
        assert sp["deliveries"].values[0] == 1800

    def test_total_deliveries_match(self):
        rs = SAMPLE_REGIONAL_PERFORMANCE["regional_stats"]
        total = sum(v["deliveries"] for v in rs.values())
        assert total == SAMPLE_REGIONAL_PERFORMANCE["total_deliveries"]


class TestEdgeCases:
    def test_empty_hourly_distribution(self):
        hd = {}
        df = pd.DataFrame(list(hd.items()), columns=["Hour", "Orders"])
        assert df.empty

    def test_missing_key_defaults(self):
        data = {}
        assert data.get("total_customers", 0) == 0
        assert data.get("total_orders", 0) == 0

    def test_partial_stats(self):
        partial = {"total_customers": 500, "total_orders": 2000}
        assert partial.get("total_customers", 0) == 500
        assert partial.get("total_orders", 0) == 2000
        assert partial.get("total_products", 0) == 0

    def test_zero_values(self):
        for k in ["customers_total", "orders_total", "products_total"]:
            assert SAMPLE_INGEST.get(k, 0) >= 0

    def test_all_int_metrics(self):
        metrics = [
            SAMPLE_STATS["total_customers"],
            SAMPLE_STATS["total_orders"],
            SAMPLE_STATS["total_products"],
            SAMPLE_STATS["total_deliveries"]
        ]
        for v in metrics:
            assert isinstance(v, int)
            assert v >= 0
