import streamlit as st
import requests
import plotly.express as px
import plotly.graph_objects as go
import pandas as pd

st.set_page_config(page_title="Seasonal Demand Analysis", page_icon="🌦️", layout="wide")

st.title("🌦️ Seasonal Demand Analysis")
st.markdown("### Holiday trends, cyclical patterns, and seasonal variations")

API_BASE = "http://localhost:8080/api"

try:
    response = requests.get(f"{API_BASE}/analytics/seasonal-demand", timeout=120)
    
    if response.status_code == 200:
        data = response.json()
        
        # Key metrics
        st.markdown("---")
        col1, col2, col3 = st.columns(3)
        
        with col1:
            st.metric("Total Orders", data.get("total_orders", 0))
        with col2:
            monthly_orders = data.get("monthly_orders", {})
            st.metric("Months Analyzed", len(monthly_orders))
        with col3:
            peak_seasons = data.get("peak_seasons", [])
            if peak_seasons:
                st.metric("Peak Month", peak_seasons[0].get("month", "N/A"))
        
        # Monthly order trends
        st.markdown("---")
        st.markdown("### 📈 Monthly Order Volume Trends")
        
        if monthly_orders:
            monthly_df = pd.DataFrame(
                list(monthly_orders.items()), 
                columns=["Month", "Orders"]
            )
            monthly_df = monthly_df.sort_values("Month")
            
            fig_monthly = px.line(
                monthly_df,
                x="Month",
                y="Orders",
                title="Monthly Order Volume",
                markers=True
            )
            fig_monthly.update_layout(
                plot_bgcolor="#1E2530",
                paper_bgcolor="#1E2530",
                font_color="#FFFFFF",
                xaxis_title="Month",
                yaxis_title="Number of Orders"
            )
            fig_monthly.update_traces(line_color='#00D9FF', marker=dict(size=8))
            st.plotly_chart(fig_monthly, use_container_width=True)
        
        # Month-over-month growth
        st.markdown("---")
        st.markdown("### 📊 Month-over-Month Growth Rate")
        
        monthly_growth = data.get("monthly_growth_percent", {})
        if monthly_growth:
            growth_df = pd.DataFrame(
                list(monthly_growth.items()), 
                columns=["Month", "Growth %"]
            )
            growth_df = growth_df.sort_values("Month")
            
            # Color based on positive/negative growth
            colors = ['#00D9FF' if x >= 0 else '#FF6B6B' for x in growth_df["Growth %"]]
            
            fig_growth = go.Figure(data=[
                go.Bar(
                    x=growth_df["Month"],
                    y=growth_df["Growth %"],
                    marker_color=colors,
                    text=growth_df["Growth %"].round(1),
                    textposition='outside'
                )
            ])
            fig_growth.update_layout(
                title="Monthly Growth Rate (%)",
                plot_bgcolor="#1E2530",
                paper_bgcolor="#1E2530",
                font_color="#FFFFFF",
                xaxis_title="Month",
                yaxis_title="Growth Rate (%)",
                showlegend=False
            )
            st.plotly_chart(fig_growth, use_container_width=True)
        
        # Seasonal patterns
        st.markdown("---")
        st.markdown("### 🍂 Seasonal Distribution")
        
        seasonal_patterns = data.get("seasonal_patterns", {})
        if seasonal_patterns:
            col_a, col_b = st.columns(2)
            
            with col_a:
                seasonal_df = pd.DataFrame(
                    list(seasonal_patterns.items()), 
                    columns=["Season", "Orders"]
                )
                
                fig_seasonal = px.pie(
                    seasonal_df,
                    values="Orders",
                    names="Season",
                    title="Order Distribution by Season",
                    color_discrete_sequence=px.colors.sequential.Teal
                )
                fig_seasonal.update_layout(
                    plot_bgcolor="#1E2530",
                    paper_bgcolor="#1E2530",
                    font_color="#FFFFFF"
                )
                st.plotly_chart(fig_seasonal, use_container_width=True)
            
            with col_b:
                fig_seasonal_bar = px.bar(
                    seasonal_df,
                    x="Season",
                    y="Orders",
                    title="Orders by Season",
                    color="Orders",
                    color_continuous_scale="Viridis"
                )
                fig_seasonal_bar.update_layout(
                    plot_bgcolor="#1E2530",
                    paper_bgcolor="#1E2530",
                    font_color="#FFFFFF"
                )
                st.plotly_chart(fig_seasonal_bar, use_container_width=True)
        
        # Quarterly trends
        st.markdown("---")
        st.markdown("### 📅 Quarterly Trends")
        
        quarterly_trends = data.get("quarterly_trends", {})
        if quarterly_trends:
            quarterly_df = pd.DataFrame(
                list(quarterly_trends.items()), 
                columns=["Quarter", "Orders"]
            )
            quarterly_df = quarterly_df.sort_values("Quarter")
            
            fig_quarterly = px.bar(
                quarterly_df,
                x="Quarter",
                y="Orders",
                title="Orders by Quarter",
                color="Orders",
                color_continuous_scale="Teal"
            )
            fig_quarterly.update_layout(
                plot_bgcolor="#1E2530",
                paper_bgcolor="#1E2530",
                font_color="#FFFFFF"
            )
            st.plotly_chart(fig_quarterly, use_container_width=True)
        
        # Holiday analysis
        st.markdown("---")
        st.markdown("### 🎉 Holiday Period Analysis")
        
        holiday_analysis = data.get("holiday_analysis", {})
        if holiday_analysis:
            holiday_data = []
            for holiday_name, holiday_info in holiday_analysis.items():
                holiday_data.append({
                    "Holiday": holiday_name,
                    "Month": holiday_info.get("month", "N/A"),
                    "Order Count": holiday_info.get("order_count", 0)
                })
            
            holiday_df = pd.DataFrame(holiday_data)
            holiday_df = holiday_df.sort_values("Order Count", ascending=False)
            
            fig_holiday = px.bar(
                holiday_df,
                x="Holiday",
                y="Order Count",
                title="Orders During Holiday Periods",
                color="Order Count",
                color_continuous_scale="Oranges"
            )
            fig_holiday.update_layout(
                plot_bgcolor="#1E2530",
                paper_bgcolor="#1E2530",
                font_color="#FFFFFF"
            )
            st.plotly_chart(fig_holiday, use_container_width=True)
            
            # Holiday data table
            st.markdown("### 📋 Holiday Period Details")
            st.dataframe(holiday_df, use_container_width=True)
        
        # Peak seasons
        st.markdown("---")
        st.markdown("### 🔥 Top 5 Peak Months")
        
        if peak_seasons:
            # Create DataFrame from list of dictionaries
            peak_df = pd.DataFrame(peak_seasons)
            
            # Rename columns for display
            if 'month' in peak_df.columns and 'order_count' in peak_df.columns:
                peak_df = peak_df.rename(columns={'month': 'Month', 'order_count': 'Order Count'})
                
                # Ensure Order Count is numeric
                peak_df['Order Count'] = pd.to_numeric(peak_df['Order Count'], errors='coerce')
                
                st.dataframe(
                    peak_df.style.background_gradient(cmap="YlOrRd", subset=["Order Count"]),
                    use_container_width=True
                )
            else:
                # Fallback if column names are different
                st.dataframe(peak_df, use_container_width=True)
        
    else:
        st.error(f"Failed to fetch data: {response.status_code}")
        
except requests.exceptions.ConnectionError:
    st.error("❌ Cannot connect to backend. Make sure it's running on port 8080")
except Exception as e:
    st.error(f"❌ Error: {str(e)}")

st.markdown("---")
st.info("""
**Seasonal Insights:**
- **Monthly Trends:** Track volume changes over time
- **Growth Rate:** Identify expanding or declining periods
- **Holiday Impact:** Measure demand during special events
- **Seasonal Patterns:** Understand cyclical behavior
- **Peak Identification:** Plan resources for high-demand periods
""")
