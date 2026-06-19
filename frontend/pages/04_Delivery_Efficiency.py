import streamlit as st
import requests
import plotly.express as px
import plotly.graph_objects as go
import pandas as pd

st.set_page_config(page_title="Delivery Efficiency", page_icon="🚚", layout="wide")

st.title("🚚 Delivery Efficiency Analysis")
st.markdown("### Transit times, delays, and operational bottlenecks")

API_BASE = "http://localhost:8080/api"

try:
    response = requests.get(f"{API_BASE}/analytics/delivery-efficiency", timeout=120)
    
    if response.status_code == 200:
        data = response.json()
        
        # Key metrics
        st.markdown("---")
        col1, col2, col3, col4 = st.columns(4)
        
        with col1:
            st.metric("Total Deliveries", data.get("total_deliveries", 0))
        with col2:
            avg_transit = data.get("average_transit_time_hours", 0)
            st.metric("Avg Transit Time", f"{avg_transit:.1f} hrs")
        with col3:
            st.metric("Delayed Deliveries", data.get("delayed_deliveries", 0))
        with col4:
            delay_rate = data.get("delay_rate_percent", 0)
            st.metric("Delay Rate", f"{delay_rate:.1f}%")
        
        # On-time vs Delayed
        st.markdown("---")
        st.markdown("### ⏱️ Delivery Performance")
        
        delay_dist = data.get("delay_distribution", {})
        if delay_dist:
            dist_df = pd.DataFrame(list(delay_dist.items()), columns=["Status", "Count"])
            
            fig_dist = px.pie(
                dist_df,
                values="Count",
                names="Status",
                title="On-Time vs Delayed Deliveries",
                color_discrete_map={"on_time": "#00D9FF", "delayed": "#FF6B6B"}
            )
            fig_dist.update_layout(
                plot_bgcolor="#1E2530",
                paper_bgcolor="#1E2530",
                font_color="#FFFFFF"
            )
            st.plotly_chart(fig_dist, use_container_width=True)
        
        # Top delays table
        st.markdown("---")
        st.markdown("### 🔴 Top 10 Delayed Orders")
        
        top_delays = data.get("top_delays", [])
        if top_delays:
            delays_df = pd.DataFrame(top_delays)
            delays_df["delay_days"] = delays_df["delay_hours"] / 24
            delays_df["delay_days"] = delays_df["delay_days"].round(1)
            
            st.dataframe(
                delays_df[["order_id", "delay_hours", "delay_days"]],
                use_container_width=True,
                height=400
            )
            
            # Delay distribution chart
            fig_delays = px.bar(
                delays_df,
                x="order_id",
                y="delay_hours",
                title="Top Delayed Orders (Hours)",
                color="delay_hours",
                color_continuous_scale="Reds"
            )
            fig_delays.update_layout(
                plot_bgcolor="#1E2530",
                paper_bgcolor="#1E2530",
                font_color="#FFFFFF",
                xaxis_title="Order ID",
                yaxis_title="Delay (Hours)"
            )
            st.plotly_chart(fig_delays, use_container_width=True)
        
    else:
        st.error(f"Failed to fetch data: {response.status_code}")
        
except requests.exceptions.ConnectionError:
    st.error("❌ Cannot connect to backend. Make sure it's running on port 8080")
except Exception as e:
    st.error(f"❌ Error: {str(e)}")

st.markdown("---")
st.info("""
**Efficiency Metrics:**
- **Average Transit Time:** Time from purchase to delivery
- **Delay Rate:** Percentage of orders delivered after estimated date
- **Bottleneck Analysis:** Identify problematic routes and carriers
- **SLA Compliance:** Track performance against service agreements
""")
