import streamlit as st
import requests
import pandas as pd
import plotly.express as px

st.set_page_config(page_title="Anomaly Detection", page_icon="⚠️", layout="wide")

st.title("⚠️ Anomaly Detection")
st.markdown("### Identify outliers and unusual patterns")

API_BASE = "http://localhost:8080/api"

try:
    response = requests.get(f"{API_BASE}/analytics/anomaly-detection", timeout=120)
    
    if response.status_code == 200:
        data = response.json()
        
        # Summary metrics
        st.markdown("---")
        col1, col2 = st.columns(2)
        
        with col1:
            st.metric("Total Anomalies Detected", data.get("total_anomalies", 0))
        with col2:
            anomalies = data.get("anomalies", [])
            high_price = sum(1 for a in anomalies if a.get("type") == "HIGH_PRICE")
            st.metric("High Price Anomalies", high_price)
        
        # Anomaly list
        st.markdown("---")
        st.markdown("### 🚨 Detected Anomalies")
        
        if anomalies:
            # Create DataFrame
            anomalies_df = pd.DataFrame(anomalies)
            
            # Display by type
            anomaly_types = anomalies_df["type"].unique()
            
            for anomaly_type in anomaly_types:
                st.markdown(f"#### {anomaly_type.replace('_', ' ').title()}")
                
                type_df = anomalies_df[anomalies_df["type"] == anomaly_type]
                
                if anomaly_type == "HIGH_PRICE":
                    display_df = type_df[["order_id", "value", "threshold"]].copy()
                    display_df["value"] = display_df["value"].round(2)
                    display_df["threshold"] = display_df["threshold"].round(2)
                    st.dataframe(display_df, use_container_width=True)
                    
                    # Price anomaly chart
                    fig = px.scatter(
                        type_df,
                        x=range(len(type_df)),
                        y="value",
                        title="High Price Anomalies",
                        color="value",
                        color_continuous_scale="Reds",
                        size="value"
                    )
                    fig.add_hline(
                        y=type_df["threshold"].iloc[0],
                        line_dash="dash",
                        line_color="yellow",
                        annotation_text="Threshold"
                    )
                    fig.update_layout(
                        plot_bgcolor="#1E2530",
                        paper_bgcolor="#1E2530",
                        font_color="#FFFFFF",
                        xaxis_title="Anomaly Index",
                        yaxis_title="Payment Value (R$)"
                    )
                    st.plotly_chart(fig, use_container_width=True)
                
                elif anomaly_type == "EXTREME_DELAY":
                    display_df = type_df[["order_id", "delay_hours", "delay_days"]].copy()
                    display_df["delay_hours"] = display_df["delay_hours"].astype(int)
                    st.dataframe(display_df, use_container_width=True)
                    
                    # Delay anomaly chart
                    fig = px.bar(
                        type_df,
                        x="order_id",
                        y="delay_hours",
                        title="Extreme Delivery Delays",
                        color="delay_hours",
                        color_continuous_scale="Oranges"
                    )
                    fig.update_layout(
                        plot_bgcolor="#1E2530",
                        paper_bgcolor="#1E2530",
                        font_color="#FFFFFF",
                        xaxis_title="Order ID",
                        yaxis_title="Delay (Hours)"
                    )
                    st.plotly_chart(fig, use_container_width=True)
                
                st.markdown("---")
        else:
            st.success("✅ No anomalies detected!")
        
    else:
        st.error(f"Failed to fetch data: {response.status_code}")
        
except requests.exceptions.ConnectionError:
    st.error("❌ Cannot connect to backend. Make sure it's running on port 8080")
except Exception as e:
    st.error(f"❌ Error: {str(e)}")

st.markdown("---")
st.info("""
**Anomaly Detection Methods:**
- **High Price Anomalies:** Orders exceeding 3 standard deviations from mean
- **Extreme Delays:** Deliveries delayed more than 1 week
- **Volume Spikes:** Unusual order volume patterns
- **Statistical Outliers:** Using threshold-based detection
""")
