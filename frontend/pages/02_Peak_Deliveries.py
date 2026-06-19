import streamlit as st
import requests
import plotly.express as px
import plotly.graph_objects as go
import pandas as pd

st.set_page_config(page_title="Peak Deliveries", page_icon="📊", layout="wide")

st.title("📊 Peak Delivery Analysis")
st.markdown("### Workload trends and demand patterns")

API_BASE = "http://localhost:8080/api"

# Fetch data
try:
    response = requests.get(f"{API_BASE}/analytics/peak-delivery", timeout=120)
    
    if response.status_code == 200:
        data = response.json()
        
        # Summary metrics
        st.markdown("---")
        col1, col2, col3 = st.columns(3)
        
        with col1:
            st.metric("Total Orders", data.get("total_orders", 0))
        with col2:
            hourly_dist = data.get("hourly_distribution", {})
            peak_hour = max(hourly_dist, key=hourly_dist.get) if hourly_dist else "N/A"
            st.metric("Peak Hour", f"{peak_hour}:00" if peak_hour != "N/A" else "N/A")
        with col3:
            daily_dist = data.get("daily_distribution", {})
            st.metric("Days Analyzed", len(daily_dist))
        
        # Hourly distribution chart
        st.markdown("---")
        st.markdown("### 📈 Hourly Order Distribution")
        
        if hourly_dist:
            hourly_df = pd.DataFrame(list(hourly_dist.items()), columns=["Hour", "Orders"])
            hourly_df = hourly_df.sort_values("Hour")
            
            fig_hourly = px.bar(
                hourly_df,
                x="Hour",
                y="Orders",
                title="Orders by Hour of Day",
                color="Orders",
                color_continuous_scale="Teal"
            )
            fig_hourly.update_layout(
                plot_bgcolor="#1E2530",
                paper_bgcolor="#1E2530",
                font_color="#FFFFFF",
                xaxis_title="Hour of Day",
                yaxis_title="Number of Orders"
            )
            st.plotly_chart(fig_hourly, use_container_width=True)
        
        # Day of week distribution
        st.markdown("---")
        st.markdown("### 📅 Day of Week Distribution")
        
        dow_dist = data.get("day_of_week_distribution", {})
        if dow_dist:
            dow_df = pd.DataFrame(list(dow_dist.items()), columns=["Day", "Orders"])
            
            fig_dow = px.pie(
                dow_df,
                values="Orders",
                names="Day",
                title="Order Distribution by Day of Week",
                color_discrete_sequence=px.colors.sequential.Teal
            )
            fig_dow.update_layout(
                plot_bgcolor="#1E2530",
                paper_bgcolor="#1E2530",
                font_color="#FFFFFF"
            )
            st.plotly_chart(fig_dow, use_container_width=True)
        
        # Daily trends (showing last 30 days)
        st.markdown("---")
        st.markdown("### 📆 Daily Order Trends (Last 30 Days)")
        
        if daily_dist:
            daily_df = pd.DataFrame(list(daily_dist.items()), columns=["Date", "Orders"])
            daily_df = daily_df.sort_values("Date", ascending=False).head(30)
            daily_df = daily_df.sort_values("Date")
            
            fig_daily = px.line(
                daily_df,
                x="Date",
                y="Orders",
                title="Daily Order Volume",
                markers=True
            )
            fig_daily.update_layout(
                plot_bgcolor="#1E2530",
                paper_bgcolor="#1E2530",
                font_color="#FFFFFF",
                xaxis_title="Date",
                yaxis_title="Number of Orders"
            )
            fig_daily.update_traces(line_color='#00D9FF', marker=dict(size=8))
            st.plotly_chart(fig_daily, use_container_width=True)
        
    else:
        st.error(f"Failed to fetch data: {response.status_code}")
        
except requests.exceptions.ConnectionError:
    st.error("❌ Cannot connect to backend. Make sure it's running on port 8080")
except Exception as e:
    st.error(f"❌ Error: {str(e)}")

# Info section
st.markdown("---")
st.info("""
**Analysis Insights:**
- Identify peak operational hours for resource allocation
- Understand weekly demand patterns
- Track daily order volume trends
- Optimize staffing based on demand forecasts
""")
