import pytest
import pandas as pd
import plotly.express as px
import plotly.graph_objects as go
from .test_data import (
    SAMPLE_PEAK_DELIVERY, SAMPLE_REVENUE, SAMPLE_CUSTOMER_SEGMENTATION,
    SAMPLE_DELIVERY_EFFICIENCY, SAMPLE_ANOMALY, SAMPLE_SEASONAL_DEMAND,
    SAMPLE_REGIONAL_PERFORMANCE
)


class TestHourlyDistributionChart:
    def test_bar_chart_creation(self):
        hourly = SAMPLE_PEAK_DELIVERY["hourly_distribution"]
        df = pd.DataFrame(list(hourly.items()), columns=["Hour", "Orders"])
        fig = px.bar(df, x="Hour", y="Orders", title="Orders by Hour of Day")
        assert len(fig.data) == 1
        assert fig.data[0].type == "bar"
        assert len(fig.data[0].x) == 24

    def test_chart_has_dark_theme(self):
        hourly = SAMPLE_PEAK_DELIVERY["hourly_distribution"]
        df = pd.DataFrame(list(hourly.items()), columns=["Hour", "Orders"])
        fig = px.bar(df, x="Hour", y="Orders")
        fig.update_layout(plot_bgcolor="#1E2530", paper_bgcolor="#1E2530", font_color="#FFFFFF")
        assert fig.layout.plot_bgcolor == "#1E2530"
        assert fig.layout.paper_bgcolor == "#1E2530"


class TestDayOfWeekChart:
    def test_pie_chart_creation(self):
        dow = SAMPLE_PEAK_DELIVERY["day_of_week_distribution"]
        df = pd.DataFrame(list(dow.items()), columns=["Day", "Orders"])
        fig = px.pie(df, values="Orders", names="Day", title="Order Distribution by Day of Week")
        assert fig.data[0].type == "pie"
        assert len(fig.data[0].labels) == 7


class TestDailyTrendChart:
    def test_line_chart_creation(self):
        daily = SAMPLE_PEAK_DELIVERY["daily_distribution"]
        df = pd.DataFrame(list(daily.items()), columns=["Date", "Orders"])
        fig = px.line(df, x="Date", y="Orders", title="Daily Order Volume", markers=True)
        assert fig.data[0].type == "scatter"
        assert "lines" in fig.data[0].mode and "markers" in fig.data[0].mode


class TestRevenueLineChart:
    def test_monthly_revenue_line_chart(self):
        monthly = SAMPLE_REVENUE["monthly_revenue"]
        df = pd.DataFrame(list(monthly.items()), columns=["Month", "Revenue"])
        df = df.sort_values("Month")
        fig = px.line(df, x="Month", y="Revenue", title="Revenue by Month", markers=True)
        assert fig.data[0].type == "scatter"
        assert len(fig.data[0].x) == 3
        fig.update_traces(line_color='#00D9FF')
        assert fig.data[0].line.color == '#00D9FF'


class TestPaymentTypeCharts:
    def test_payment_type_pie_chart(self):
        payments = SAMPLE_REVENUE["payment_type_analysis"]
        data = [{"Payment Type": pt, "Count": info["count"]} for pt, info in payments.items()]
        df = pd.DataFrame(data)
        fig = px.pie(df, values="Count", names="Payment Type", title="Payment Type Usage")
        assert fig.data[0].type == "pie"
        assert len(fig.data[0].labels) == 4

    def test_revenue_by_payment_bar_chart(self):
        payments = SAMPLE_REVENUE["payment_type_analysis"]
        data = [{"Payment Type": pt, "Total Value": info["total_value"]} for pt, info in payments.items()]
        df = pd.DataFrame(data)
        fig = px.bar(df, x="Payment Type", y="Total Value", title="Revenue by Payment Type")
        assert fig.data[0].type == "bar"
        assert len(fig.data[0].x) == 4


class TestCustomerSegmentationCharts:
    def test_segment_bar_chart(self):
        segments = SAMPLE_CUSTOMER_SEGMENTATION["segments"]
        data = [{"Segment": seg, "Count": info["count"]} for seg, info in segments.items()]
        df = pd.DataFrame(data)
        fig = px.bar(df, x="Segment", y="Count", title="Customer Segments")
        assert fig.data[0].type == "bar"
        assert len(fig.data[0].x) == 3

    def test_state_distribution_chart(self):
        states = SAMPLE_CUSTOMER_SEGMENTATION["state_distribution"]
        df = pd.DataFrame(list(states.items()), columns=["State", "Customers"])
        fig = px.bar(df, x="State", y="Customers", title="Customers by State")
        assert fig.data[0].type == "bar"
        assert len(fig.data[0].x) == 6


class TestAnomalyDetectionCharts:
    def test_scatter_plot_high_price(self):
        anomalies = SAMPLE_ANOMALY["anomalies"]
        df = pd.DataFrame(anomalies)
        high_price = df[df["type"] == "HIGH_PRICE"]
        fig = px.scatter(high_price, x="order_id", y="value", title="High Price Anomalies")
        assert fig.data[0].type == "scatter"
        assert len(fig.data[0].x) == 2


class TestSeasonalDemandCharts:
    def test_monthly_line_chart(self):
        monthly = SAMPLE_SEASONAL_DEMAND["monthly_volume"]
        df = pd.DataFrame(list(monthly.items()), columns=["Month", "Orders"])
        df = df.sort_values("Month")
        fig = px.line(df, x="Month", y="Orders", title="Monthly Order Volume", markers=True)
        assert fig.data[0].type == "scatter"
        assert len(fig.data[0].x) == 4

    def test_seasonal_pie_chart(self):
        peaks = SAMPLE_SEASONAL_DEMAND["seasonal_peaks"]
        df = pd.DataFrame(list(peaks.items()), columns=["Season", "Demand"])
        fig = px.pie(df, values="Demand", names="Season", title="Seasonal Distribution")
        assert fig.data[0].type == "pie"


class TestRegionalPerformanceCharts:
    def test_regional_bar_chart(self):
        stats = SAMPLE_REGIONAL_PERFORMANCE["regional_stats"]
        data = [{"State": s, "Deliveries": info["deliveries"]} for s, info in stats.items()]
        df = pd.DataFrame(data)
        fig = px.bar(df, x="State", y="Deliveries", title="Deliveries by State")
        assert fig.data[0].type == "bar"
        assert len(fig.data[0].x) == 3

    def test_regional_revenue_pie_chart(self):
        stats = SAMPLE_REGIONAL_PERFORMANCE["regional_stats"]
        data = [{"State": s, "Deliveries": info["deliveries"]} for s, info in stats.items()]
        df = pd.DataFrame(data)
        fig = px.pie(df, values="Deliveries", names="State", title="Delivery Distribution")
        assert fig.data[0].type == "pie"
        assert len(fig.data[0].labels) == 3


class TestDeliveryEfficiencyCharts:
    def test_status_pie_chart(self):
        status = SAMPLE_DELIVERY_EFFICIENCY["delivery_status"]
        df = pd.DataFrame(list(status.items()), columns=["Status", "Count"])
        fig = px.pie(df, values="Count", names="Status", title="Delivery Status Distribution")
        assert fig.data[0].type == "pie"
        assert len(fig.data[0].labels) == 3


class TestDataTransformations:
    def test_hourly_dataframe_from_dict(self):
        hourly = SAMPLE_PEAK_DELIVERY["hourly_distribution"]
        df = pd.DataFrame(list(hourly.items()), columns=["Hour", "Orders"])
        assert list(df.columns) == ["Hour", "Orders"]
        assert df["Orders"].dtype in ["int64", "float64", "object"]

    def test_daily_dataframe_truncation(self):
        daily = SAMPLE_PEAK_DELIVERY["daily_distribution"]
        df = pd.DataFrame(list(daily.items()), columns=["Date", "Orders"])
        truncated = df.sort_values("Date", ascending=False).head(30)
        truncated = truncated.sort_values("Date")
        assert len(truncated) == len(daily)

    def test_payment_type_dataframe_building(self):
        payments = SAMPLE_REVENUE["payment_type_analysis"]
        data = [{"Payment Type": pt, "Count": info["count"], "Total Value": info["total_value"]}
                for pt, info in payments.items()]
        df = pd.DataFrame(data)
        assert list(df.columns) == ["Payment Type", "Count", "Total Value"]
        assert len(df) == 4

    def test_anomaly_filter_by_type(self):
        anomalies = SAMPLE_ANOMALY["anomalies"]
        df = pd.DataFrame(anomalies)
        high_price = df[df["type"] == "HIGH_PRICE"]
        assert len(high_price) == 2

    def test_seasonal_monthly_volume_length(self):
        monthly = SAMPLE_SEASONAL_DEMAND["monthly_volume"]
        assert len(monthly) == 4
