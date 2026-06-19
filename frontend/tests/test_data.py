SAMPLE_STATS = {
    "total_customers": 1000,
    "total_orders": 5000,
    "total_products": 300,
    "total_sellers": 100,
    "total_order_items": 8000,
    "total_payments": 5000,
    "total_deliveries": 4800,
    "total_faulty_records": 15
}

SAMPLE_INGEST = {
    "totalProcessingTimeMs": 15000,
    "customers_total": 1000, "customers_valid": 995, "customers_duplicates": 5,
    "orders_total": 5000, "orders_valid": 4980, "orders_duplicates": 20,
    "products_total": 300, "products_valid": 300, "products_duplicates": 0,
    "order_items_total": 8000, "order_items_valid": 7950,
    "payments_total": 5000, "payments_valid": 5000,
    "sellers_total": 100, "sellers_valid": 100
}

SAMPLE_PEAK_DELIVERY = {
    "total_orders": 2950,
    "hourly_distribution": {str(h): 100 + (50 if 8 <= h <= 18 else 0) for h in range(24)},
    "daily_distribution": {"2024-01-01": 150, "2024-01-02": 165, "2024-01-03": 140},
    "day_of_week_distribution": {"Monday": 800, "Tuesday": 750, "Wednesday": 720,
                                  "Thursday": 780, "Friday": 850, "Saturday": 600, "Sunday": 500}
}

SAMPLE_REVENUE = {
    "total_revenue": 325000.00,
    "average_order_value": 250.10,
    "payment_type_analysis": {
        "credit_card": {"count": 3000, "total_value": 750000.00},
        "boleto": {"count": 1000, "total_value": 250000.00},
        "voucher": {"count": 500, "total_value": 125000.50},
        "debit_card": {"count": 500, "total_value": 125000.00}
    },
    "monthly_revenue": {"2024-01": 120000.00, "2024-02": 95000.00, "2024-03": 110000.00}
}

SAMPLE_CUSTOMER_SEGMENTATION = {
    "total_customers": 1000,
    "segments": {
        "High Value": {"count": 150, "avg_spend": 2500.00},
        "Medium Value": {"count": 350, "avg_spend": 1200.00},
        "Low Value": {"count": 500, "avg_spend": 300.00}
    },
    "state_distribution": {"SP": 400, "RJ": 200, "MG": 150, "RS": 100, "PR": 80, "Other": 70}
}

SAMPLE_ANOMALY = {
    "total_anomalies": 25,
    "anomalies": [
        {"type": "HIGH_PRICE", "order_id": "1001", "value": 9999.99, "date": "2024-01-15"},
        {"type": "HIGH_PRICE", "order_id": "1002", "value": 8500.00, "date": "2024-01-16"},
        {"type": "DELIVERY_DELAY", "order_id": "2001", "value": 0, "date": "2024-01-20"},
        {"type": "UNUSUAL_QUANTITY", "order_id": "3001", "value": 500, "date": "2024-01-25"}
    ]
}

SAMPLE_DELIVERY_EFFICIENCY = {
    "total_orders": 5000,
    "on_time_rate": 92.5,
    "average_delivery_days": 3.5,
    "delivery_status": {"delivered": 4800, "processing": 150, "cancelled": 50}
}

SAMPLE_SEASONAL_DEMAND = {
    "total_orders": 5000,
    "monthly_volume": {"2024-01": 450, "2024-02": 380, "2024-03": 420, "2024-04": 400},
    "seasonal_peaks": {"January": "High", "December": "High", "July": "Medium"}
}

SAMPLE_REGIONAL_PERFORMANCE = {
    "total_deliveries": 3300,
    "regional_stats": {
        "SP": {"deliveries": 1800, "on_time_rate": 94.0, "avg_days": 2.8},
        "RJ": {"deliveries": 900, "on_time_rate": 91.0, "avg_days": 3.2},
        "MG": {"deliveries": 600, "on_time_rate": 93.0, "avg_days": 3.0}
    }
}
