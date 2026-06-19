import streamlit as st
import sys
from pathlib import Path

# Configure page
st.set_page_config(
    page_title="TransitFlow Analytics System",
    page_icon="📦",
    layout="wide",
    initial_sidebar_state="expanded"
)

# Apply enhanced custom styling
st.markdown("""
<style>
    .main {
        background-color: #0E1117;
    }
    .stApp {
        background: linear-gradient(135deg, #0E1117 0%, #1a1f2e 100%);
    }
    h1, h2, h3 {
        color: #00D9FF;
        font-family: 'Helvetica Neue', sans-serif;
        font-weight: 600;
    }
    .stButton>button {
        background: linear-gradient(135deg, #00D9FF 0%, #00B8D9 100%);
        color: #0E1117;
        font-weight: bold;
        border-radius: 10px;
        border: none;
        padding: 0.6rem 2.5rem;
        box-shadow: 0 4px 15px rgba(0, 217, 255, 0.3);
        transition: all 0.3s ease;
    }
    .stButton>button:hover {
        background: linear-gradient(135deg, #00B8D9 0%, #0099B8 100%);
        box-shadow: 0 6px 20px rgba(0, 217, 255, 0.5);
        transform: translateY(-2px);
    }
    .metric-card {
        background: linear-gradient(135deg, #1E2530 0%, #252d3d 100%);
        padding: 1.5rem;
        border-radius: 12px;
        border-left: 4px solid #00D9FF;
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.3);
    }
    .stMetric {
        background: linear-gradient(135deg, #1E2530 0%, #252d3d 100%);
        padding: 1rem;
        border-radius: 10px;
        border: 1px solid #2d3548;
    }
    .stMetric label {
        color: #00D9FF !important;
        font-weight: 600;
    }
    .stMetric [data-testid="stMetricValue"] {
        color: #FFFFFF;
        font-size: 2rem;
    }
    .sidebar .sidebar-content {
        background-color: #1a1f2e;
    }
    .feature-box {
        background: linear-gradient(135deg, #1E2530 0%, #252d3d 100%);
        padding: 1.5rem;
        border-radius: 12px;
        border: 2px solid #2d3548;
        margin: 1rem 0;
        transition: all 0.3s ease;
    }
    .feature-box:hover {
        border-color: #00D9FF;
        box-shadow: 0 8px 25px rgba(0, 217, 255, 0.2);
        transform: translateY(-5px);
    }
    .welcome-header {
        background: linear-gradient(135deg, #00D9FF 0%, #00B8D9 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-clip: text;
        font-size: 2.5rem;
        font-weight: 700;
        margin-bottom: 1rem;
    }
</style>
""", unsafe_allow_html=True)

# Application header
st.markdown('<h1 class="welcome-header">📦 TransitFlow Analytics System</h1>', unsafe_allow_html=True)
st.markdown("### Enterprise-Level Logistics & E-Commerce Intelligence Platform")

# Sidebar
with st.sidebar:
    st.markdown("### ⚡ Quick Links")
    st.markdown("---")
    st.markdown("### 🧭 Navigation")
    st.markdown("""
    <div style='background: linear-gradient(135deg, #1E2530 0%, #252d3d 100%); 
                padding: 1rem; border-radius: 10px; border-left: 4px solid #00D9FF;'>
    <b>📤 Dataset Upload</b><br/>
    <small>Ingest CSV datasets</small><br/><br/>
    <b>📊 Analytics Dashboards</b><br/>
    <small>Real-time insights</small><br/><br/>
    <b>📈 Reports & Exports</b><br/>
    <small>Generate PDF/CSV reports</small>
    </div>
    """, unsafe_allow_html=True)
    st.markdown("---")
    st.markdown("""
    <div style='text-align: center; padding: 1rem; background: #1E2530; border-radius: 8px;'>
    <b style='color: #00D9FF;'>Backend API</b><br/>
    <small>http://localhost:8080</small><br/><br/>
    <b style='color: #00D9FF;'>Version</b><br/>
    <small>1.0.0</small>
    </div>
    """, unsafe_allow_html=True)

# Main content
st.markdown("---")
st.markdown('<div class="feature-box">', unsafe_allow_html=True)
st.markdown("""
## 🌟 Welcome to TransitFlow Analytics

This production-ready system processes **large-scale logistics and e-commerce datasets** 
to provide high-impact operational intelligence with enterprise-grade performance.
""")
st.markdown('</div>', unsafe_allow_html=True)

# Key Features in columns
st.markdown("### 🚀 Key Features")
col1, col2 = st.columns(2)

with col1:
    st.markdown("""
    <div class="feature-box">
    <h4>⚡ High-Performance Backend</h4>
    <ul>
        <li>Spring Boot with chunk-based ingestion</li>
        <li>5,000 records per chunk processing</li>
        <li>Memory-efficient streaming</li>
        <li>H2 in-memory database</li>
    </ul>
    </div>
    """, unsafe_allow_html=True)
    
    st.markdown("""
    <div class="feature-box">
    <h4>📊 Interactive Dashboards</h4>
    <ul>
        <li>Real-time analytics with Plotly</li>
        <li>Beautiful Matplotlib visualizations</li>
        <li>Streamlit modern UI</li>
        <li>Responsive charts & graphs</li>
    </ul>
    </div>
    """, unsafe_allow_html=True)

with col2:
    st.markdown("""
    <div class="feature-box">
    <h4>🧠 Smart Processing</h4>
    <ul>
        <li>Automatic duplicate removal</li>
        <li>Data validation & cleansing</li>
        <li>Dead-Letter Queue for errors</li>
        <li>Fault-tolerant architecture</li>
    </ul>
    </div>
    """, unsafe_allow_html=True)
    
    st.markdown("""
    <div class="feature-box">
    <h4>📈 Advanced Analytics</h4>
    <ul>
        <li>Peak delivery time analysis</li>
        <li>Customer segmentation</li>
        <li>Revenue & efficiency metrics</li>
        <li>Anomaly detection</li>
    </ul>
    </div>
    """, unsafe_allow_html=True)

# Getting Started
st.markdown("---")
st.markdown("### 🎯 Getting Started")

step_col1, step_col2, step_col3 = st.columns(3)

with step_col1:
    st.markdown("""
    <div class="feature-box" style="text-align: center;">
    <h2 style="color: #00D9FF;">1️⃣</h2>
    <h4>Upload Datasets</h4>
    <p>Navigate to <b>Dataset Upload</b> to ingest your CSV data files</p>
    </div>
    """, unsafe_allow_html=True)

with step_col2:
    st.markdown("""
    <div class="feature-box" style="text-align: center;">
    <h2 style="color: #00D9FF;">2️⃣</h2>
    <h4>Explore Analytics</h4>
    <p>View insights in the various <b>Analytics Dashboards</b></p>
    </div>
    """, unsafe_allow_html=True)

with step_col3:
    st.markdown("""
    <div class="feature-box" style="text-align: center;">
    <h2 style="color: #00D9FF;">3️⃣</h2>
    <h4>Generate Reports</h4>
    <p>Export <b>PDF/CSV Reports</b> for stakeholders</p>
    </div>
    """, unsafe_allow_html=True)

st.markdown("---")

# Quick stats placeholder
st.markdown("### 🔍 System Status")
if st.button("🔄 Check Backend Status", use_container_width=False):
    import requests
    try:
        with st.spinner("Connecting to backend..."):
            response = requests.get("http://localhost:8080/api/dataset/stats", timeout=5)
            if response.status_code == 200:
                st.success("✅ Backend is running!")
                stats = response.json()
                
                st.markdown("---")
                col1, col2, col3, col4 = st.columns(4)
                with col1:
                    st.metric("👥 Customers", f"{stats.get('total_customers', 0):,}")
                with col2:
                    st.metric("📦 Orders", f"{stats.get('total_orders', 0):,}")
                with col3:
                    st.metric("🛍️ Products", f"{stats.get('total_products', 0):,}")
                with col4:
                    st.metric("🚚 Deliveries", f"{stats.get('total_deliveries', 0):,}")
            else:
                st.error("❌ Backend responded with error")
    except Exception as e:
        st.error(f"❌ Cannot connect to backend: {str(e)}")
        st.info("💡 Make sure the Spring Boot backend is running on port 8080")

# Footer
st.markdown("---")
st.markdown("""
<div style='text-align: center; padding: 2rem; color: #6c757d;'>
    <p><b>TransitFlow Analytics System v1.0.0</b></p>
    <p>Built with Spring Boot 3.3.5 | Streamlit | Plotly | H2 Database</p>
    <p style='color: #00D9FF;'>⚡ High-Performance | 🔒 Secure | 📊 Scalable</p>
</div>
""", unsafe_allow_html=True)
