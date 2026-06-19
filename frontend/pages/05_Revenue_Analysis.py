import streamlit as st
import requests
import plotly.express as px
import plotly.graph_objects as go
import pandas as pd

st.set_page_config(page_title="Revenue Analysis", page_icon="💰", layout="wide")

st.title("💰 Revenue Analysis")
st.markdown("### Financial performance and payment insights")

API_BASE = "http://localhost:8080/api"

try:
    with st.spinner("🔄 Analyzing revenue data... This may take a moment..."):
        response = requests.get(f"{API_BASE}/analytics/revenue-analysis", timeout=120)
    
    if response.status_code == 200:
        data = response.json()
        
        # Key financial metrics
        st.markdown("---")
        col1, col2, col3 = st.columns(3)
        
        with col1:
            total_rev = data.get("total_revenue", 0)
            st.metric("Total Revenue", f"R$ {total_rev:,.2f}")
        with col2:
            avg_order = data.get("average_order_value", 0)
            st.metric("Avg Order Value", f"R$ {avg_order:,.2f}")
        with col3:
            payment_types = data.get("payment_type_analysis", {})
            st.metric("Payment Types", len(payment_types))
        
        # Monthly revenue trend
        st.markdown("---")
        st.markdown("### 📈 Monthly Revenue Trend")
        
        monthly_rev = data.get("monthly_revenue", {})
        if monthly_rev:
            monthly_df = pd.DataFrame(list(monthly_rev.items()), columns=["Month", "Revenue"])
            monthly_df = monthly_df.sort_values("Month")
            
            fig_monthly = px.line(
                monthly_df,
                x="Month",
                y="Revenue",
                title="Revenue by Month",
                markers=True
            )
            fig_monthly.update_layout(
                plot_bgcolor="#1E2530",
                paper_bgcolor="#1E2530",
                font_color="#FFFFFF",
                xaxis_title="Month",
                yaxis_title="Revenue (R$)"
            )
            fig_monthly.update_traces(line_color='#00D9FF', marker=dict(size=10))
            st.plotly_chart(fig_monthly, use_container_width=True)
        
        # Payment type analysis
        st.markdown("---")
        st.markdown("### 💳 Payment Type Distribution")
        
        if payment_types:
            payment_data = []
            for ptype, info in payment_types.items():
                payment_data.append({
                    "Payment Type": ptype,
                    "Count": info.get("count", 0),
                    "Total Value": info.get("total_value", 0)
                })
            
            payment_df = pd.DataFrame(payment_data)
            
            col_a, col_b = st.columns(2)
            
            with col_a:
                fig_pay_count = px.pie(
                    payment_df,
                    values="Count",
                    names="Payment Type",
                    title="Payment Type Usage",
                    color_discrete_sequence=px.colors.sequential.Teal
                )
                fig_pay_count.update_layout(
                    plot_bgcolor="#1E2530",
                    paper_bgcolor="#1E2530",
                    font_color="#FFFFFF"
                )
                st.plotly_chart(fig_pay_count, use_container_width=True)
            
            with col_b:
                fig_pay_value = px.bar(
                    payment_df,
                    x="Payment Type",
                    y="Total Value",
                    title="Revenue by Payment Type",
                    color="Total Value",
                    color_continuous_scale="Viridis"
                )
                fig_pay_value.update_layout(
                    plot_bgcolor="#1E2530",
                    paper_bgcolor="#1E2530",
                    font_color="#FFFFFF"
                )
                st.plotly_chart(fig_pay_value, use_container_width=True)
            
            # Payment details table
            st.markdown("---")
            st.markdown("### 📊 Payment Type Details")
            st.dataframe(payment_df, use_container_width=True)
        
    else:
        st.error(f"Failed to fetch data: {response.status_code}")
        
except requests.exceptions.ConnectionError:
    st.error("❌ Cannot connect to backend. Make sure it's running on port 8080")
except Exception as e:
    st.error(f"❌ Error: {str(e)}")

st.markdown("---")
st.info("""
**Revenue Insights:**
- **Total Revenue:** Cumulative sales across all orders
- **Monthly Trends:** Track growth and seasonality
- **Payment Preferences:** Understand customer payment behavior
- **Average Order Value:** Measure transaction size
""")
