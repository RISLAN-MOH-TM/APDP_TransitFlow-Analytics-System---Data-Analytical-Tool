import streamlit as st
import requests
import plotly.express as px
import pandas as pd

st.set_page_config(page_title="Customer Segmentation", page_icon="👥", layout="wide")

st.title("👥 Customer Segmentation Analysis")
st.markdown("### Geographic and behavioral customer insights")

API_BASE = "http://localhost:8080/api"

try:
    with st.spinner("🔄 Analyzing customer data... This may take a moment..."):
        response = requests.get(f"{API_BASE}/analytics/customer-segmentation", timeout=120)
    
    if response.status_code == 200:
        data = response.json()
        
        # Summary metrics
        st.markdown("---")
        col1, col2, col3 = st.columns(3)
        
        with col1:
            st.metric("Total Customers", data.get("total_customers", 0))
        
        freq_segments = data.get("frequency_segments", {})
        with col2:
            st.metric("Repeat Customers", freq_segments.get("repeat", 0))
        with col3:
            st.metric("Frequent Buyers", freq_segments.get("frequent", 0))
        
        # Frequency segmentation
        st.markdown("---")
        st.markdown("### 📊 Customer Frequency Segments")
        
        if freq_segments:
            freq_df = pd.DataFrame(list(freq_segments.items()), columns=["Segment", "Count"])
            
            fig_freq = px.bar(
                freq_df,
                x="Segment",
                y="Count",
                title="Customer Segmentation by Purchase Frequency",
                color="Count",
                color_continuous_scale="Viridis"
            )
            fig_freq.update_layout(
                plot_bgcolor="#1E2530",
                paper_bgcolor="#1E2530",
                font_color="#FFFFFF"
            )
            st.plotly_chart(fig_freq, use_container_width=True)
        
        # Geographic distribution
        st.markdown("---")
        st.markdown("### 🗺️ Geographic Distribution (Top States)")
        
        state_dist = data.get("state_distribution", {})
        if state_dist:
            state_df = pd.DataFrame(list(state_dist.items()), columns=["State", "Customers"])
            
            col_a, col_b = st.columns(2)
            
            with col_a:
                fig_state_bar = px.bar(
                    state_df.head(15),
                    x="Customers",
                    y="State",
                    orientation='h',
                    title="Top 15 States by Customer Count",
                    color="Customers",
                    color_continuous_scale="Teal"
                )
                fig_state_bar.update_layout(
                    plot_bgcolor="#1E2530",
                    paper_bgcolor="#1E2530",
                    font_color="#FFFFFF",
                    height=500
                )
                st.plotly_chart(fig_state_bar, use_container_width=True)
            
            with col_b:
                fig_state_pie = px.pie(
                    state_df.head(10),
                    values="Customers",
                    names="State",
                    title="Top 10 States Distribution",
                    color_discrete_sequence=px.colors.sequential.Teal
                )
                fig_state_pie.update_layout(
                    plot_bgcolor="#1E2530",
                    paper_bgcolor="#1E2530",
                    font_color="#FFFFFF",
                    height=500
                )
                st.plotly_chart(fig_state_pie, use_container_width=True)
        
        # Data table
        st.markdown("---")
        st.markdown("### 📋 Detailed State Breakdown")
        if state_dist:
            st.dataframe(state_df, use_container_width=True, height=400)
        
    else:
        st.error(f"Failed to fetch data: {response.status_code}")
        
except requests.exceptions.ConnectionError:
    st.error("❌ Cannot connect to backend. Make sure it's running on port 8080")
except Exception as e:
    st.error(f"❌ Error: {str(e)}")

st.markdown("---")
st.info("""
**Segmentation Insights:**
- **One-Time Customers:** First-time buyers - focus on retention
- **Repeat Customers:** 2-3 purchases - encourage loyalty
- **Frequent Buyers:** 4+ purchases - VIP treatment
- **Geographic Focus:** Target high-density regions for marketing
""")
