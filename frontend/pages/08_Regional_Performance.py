import streamlit as st
import requests
import plotly.express as px
import plotly.graph_objects as go
import pandas as pd

st.set_page_config(page_title="Regional Performance", page_icon="🗺️", layout="wide")

st.title("🗺️ Regional Performance Analysis")
st.markdown("### Compare delivery success, revenue, and efficiency across regions")

API_BASE = "http://localhost:8080/api"

try:
    response = requests.get(f"{API_BASE}/analytics/regional-performance", timeout=120)
    
    if response.status_code == 200:
        data = response.json()
        
        # Key metrics
        st.markdown("---")
        col1, col2, col3, col4 = st.columns(4)
        
        with col1:
            st.metric("Total Regions", data.get("total_regions", 0))
        with col2:
            top_regions = data.get("top_performing_regions", [])
            st.metric("Top Region", top_regions[0] if top_regions else "N/A")
        with col3:
            revenue_by_region = data.get("revenue_by_region", {})
            total_revenue = sum(revenue_by_region.values())
            st.metric("Total Revenue", f"R$ {total_revenue:,.2f}")
        with col4:
            orders_by_region = data.get("orders_by_region", {})
            total_orders = sum(orders_by_region.values())
            st.metric("Total Orders", f"{total_orders:,}")
        
        # Regional rankings table
        st.markdown("---")
        st.markdown("### 🏆 Regional Performance Rankings")
        
        regional_rankings = data.get("regional_rankings", [])
        if regional_rankings:
            rankings_df = pd.DataFrame(regional_rankings)
            rankings_df["rank"] = range(1, len(rankings_df) + 1)
            
            # Reorder columns
            display_df = rankings_df[[
                "rank", "state", "customers", "orders", "revenue", 
                "success_rate", "performance_score"
            ]].copy()
            
            display_df.columns = [
                "Rank", "State", "Customers", "Orders", "Revenue (R$)",
                "Success Rate (%)", "Performance Score"
            ]
            
            st.dataframe(
                display_df.style.background_gradient(
                    cmap="RdYlGn", subset=["Performance Score", "Success Rate (%)"]
                ),
                use_container_width=True,
                height=400
            )
        
        # Top vs Bottom performers
        st.markdown("---")
        col_perf1, col_perf2 = st.columns(2)
        
        with col_perf1:
            st.markdown("### ✅ Top 5 Performing Regions")
            if regional_rankings:
                top_5 = regional_rankings[:5]
                top_df = pd.DataFrame(top_5)[["state", "performance_score"]]
                top_df.columns = ["State", "Score"]
                
                fig_top = px.bar(
                    top_df,
                    x="Score",
                    y="State",
                    orientation='h',
                    title="Top Performers",
                    color="Score",
                    color_continuous_scale="Greens"
                )
                fig_top.update_layout(
                    plot_bgcolor="#1E2530",
                    paper_bgcolor="#1E2530",
                    font_color="#FFFFFF"
                )
                st.plotly_chart(fig_top, use_container_width=True)
        
        with col_perf2:
            st.markdown("### ⚠️ Bottom 5 Performing Regions")
            if regional_rankings and len(regional_rankings) >= 5:
                bottom_5 = regional_rankings[-5:]
                bottom_df = pd.DataFrame(bottom_5)[["state", "performance_score"]]
                bottom_df.columns = ["State", "Score"]
                
                fig_bottom = px.bar(
                    bottom_df,
                    x="Score",
                    y="State",
                    orientation='h',
                    title="Areas for Improvement",
                    color="Score",
                    color_continuous_scale="Reds"
                )
                fig_bottom.update_layout(
                    plot_bgcolor="#1E2530",
                    paper_bgcolor="#1E2530",
                    font_color="#FFFFFF"
                )
                st.plotly_chart(fig_bottom, use_container_width=True)
        
        # Revenue by region
        st.markdown("---")
        st.markdown("### 💰 Revenue Distribution by Region")
        
        if revenue_by_region:
            revenue_df = pd.DataFrame(
                list(revenue_by_region.items()), 
                columns=["State", "Revenue"]
            )
            revenue_df = revenue_df.sort_values("Revenue", ascending=False).head(15)
            
            col_rev1, col_rev2 = st.columns(2)
            
            with col_rev1:
                fig_rev_bar = px.bar(
                    revenue_df,
                    x="State",
                    y="Revenue",
                    title="Top 15 States by Revenue",
                    color="Revenue",
                    color_continuous_scale="Viridis"
                )
                fig_rev_bar.update_layout(
                    plot_bgcolor="#1E2530",
                    paper_bgcolor="#1E2530",
                    font_color="#FFFFFF"
                )
                st.plotly_chart(fig_rev_bar, use_container_width=True)
            
            with col_rev2:
                fig_rev_pie = px.pie(
                    revenue_df.head(10),
                    values="Revenue",
                    names="State",
                    title="Top 10 States Revenue Share",
                    color_discrete_sequence=px.colors.sequential.Teal
                )
                fig_rev_pie.update_layout(
                    plot_bgcolor="#1E2530",
                    paper_bgcolor="#1E2530",
                    font_color="#FFFFFF"
                )
                st.plotly_chart(fig_rev_pie, use_container_width=True)
        
        # Delivery success rates
        st.markdown("---")
        st.markdown("### 📦 Delivery Success Rates by Region")
        
        delivery_performance = data.get("delivery_performance_by_region", {})
        if delivery_performance:
            perf_data = []
            for state, perf in delivery_performance.items():
                perf_data.append({
                    "State": state,
                    "Total Deliveries": perf.get("total_deliveries", 0),
                    "On-Time Deliveries": perf.get("on_time_deliveries", 0),
                    "Success Rate %": perf.get("success_rate_percent", 0)
                })
            
            perf_df = pd.DataFrame(perf_data)
            perf_df = perf_df.sort_values("Success Rate %", ascending=False).head(20)
            
            fig_success = px.bar(
                perf_df,
                x="State",
                y="Success Rate %",
                title="Top 20 States by Delivery Success Rate",
                color="Success Rate %",
                color_continuous_scale="RdYlGn",
                text="Success Rate %"
            )
            fig_success.update_traces(texttemplate='%{text:.1f}%', textposition='outside')
            fig_success.update_layout(
                plot_bgcolor="#1E2530",
                paper_bgcolor="#1E2530",
                font_color="#FFFFFF",
                xaxis_tickangle=-45
            )
            st.plotly_chart(fig_success, use_container_width=True)
        
        # Regional efficiency
        st.markdown("---")
        st.markdown("### ⚡ Regional Efficiency Metrics")
        
        efficiency_by_region = data.get("efficiency_by_region", {})
        if efficiency_by_region:
            eff_data = []
            for state, eff in efficiency_by_region.items():
                eff_data.append({
                    "State": state,
                    "Avg Transit Time (hrs)": eff.get("average_transit_hours", 0),
                    "Delivery Count": eff.get("delivery_count", 0)
                })
            
            eff_df = pd.DataFrame(eff_data)
            eff_df = eff_df.sort_values("Avg Transit Time (hrs)").head(15)
            
            fig_efficiency = px.scatter(
                eff_df,
                x="Avg Transit Time (hrs)",
                y="State",
                size="Delivery Count",
                title="Regional Transit Time Efficiency (Lower is Better)",
                color="Avg Transit Time (hrs)",
                color_continuous_scale="RdYlGn_r"
            )
            fig_efficiency.update_layout(
                plot_bgcolor="#1E2530",
                paper_bgcolor="#1E2530",
                font_color="#FFFFFF"
            )
            st.plotly_chart(fig_efficiency, use_container_width=True)
        
        # Summary comparison
        st.markdown("---")
        st.markdown("### 📊 Multi-Metric Regional Comparison")
        
        customers_by_region = data.get("customers_by_region", {})
        if customers_by_region and orders_by_region and revenue_by_region:
            # Normalize data for radar chart (top 5 regions)
            top_states = list(revenue_by_region.keys())[:5]
            
            comparison_data = []
            for state in top_states:
                customers = customers_by_region.get(state, 0)
                orders = orders_by_region.get(state, 0)
                revenue = revenue_by_region.get(state, 0)
                
                comparison_data.append({
                    "State": state,
                    "Customers": customers,
                    "Orders": orders,
                    "Revenue": revenue / 1000  # Scale down for visualization
                })
            
            comp_df = pd.DataFrame(comparison_data)
            
            fig_comparison = go.Figure()
            
            for _, row in comp_df.iterrows():
                fig_comparison.add_trace(go.Scatterpolar(
                    r=[row["Customers"], row["Orders"], row["Revenue"]],
                    theta=["Customers", "Orders", "Revenue (K)"],
                    name=row["State"],
                    fill='toself'
                ))
            
            fig_comparison.update_layout(
                polar=dict(
                    radialaxis=dict(visible=True)
                ),
                title="Top 5 Regions - Multi-Metric Comparison",
                plot_bgcolor="#1E2530",
                paper_bgcolor="#1E2530",
                font_color="#FFFFFF",
                showlegend=True
            )
            st.plotly_chart(fig_comparison, use_container_width=True)
        
    else:
        st.error(f"Failed to fetch data: {response.status_code}")
        
except requests.exceptions.ConnectionError:
    st.error("❌ Cannot connect to backend. Make sure it's running on port 8080")
except Exception as e:
    st.error(f"❌ Error: {str(e)}")

st.markdown("---")
st.info("""
**Regional Performance Insights:**
- **Performance Score:** Composite metric combining customers, orders, revenue, and success rate
- **Success Rate:** Percentage of on-time deliveries per region
- **Efficiency:** Average transit time (lower is better)
- **Revenue Distribution:** Identify high-value markets
- **Strategic Focus:** Allocate resources to high-potential regions
""")
