import streamlit as st
import requests
import time
import os
from pathlib import Path
import tkinter as tk
from tkinter import filedialog

st.set_page_config(page_title="Dataset Upload", page_icon="📤", layout="wide")

# Enhanced custom styling
st.markdown("""
<style>
    .main {
        background-color: #0E1117;
    }
    .stApp {
        background: linear-gradient(135deg, #0E1117 0%, #1a1f2e 100%);
    }
    h1, h2, h3, h4 {
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
    div[data-testid="stExpander"] {
        background-color: #1E2530;
        border-radius: 10px;
        border: 1px solid #2d3548;
    }
    .stTextInput>div>div>input {
        background-color: #1E2530;
        color: #FFFFFF;
        border: 2px solid #2d3548;
        border-radius: 8px;
        padding: 0.6rem;
    }
    .stTextInput>div>div>input:focus {
        border-color: #00D9FF;
        box-shadow: 0 0 10px rgba(0, 217, 255, 0.3);
    }
    .upload-section {
        background: linear-gradient(135deg, #1E2530 0%, #252d3d 100%);
        padding: 2rem;
        border-radius: 12px;
        border: 2px solid #2d3548;
        margin: 1rem 0;
    }
    .info-box {
        background: linear-gradient(135deg, #1a2332 0%, #252d3d 100%);
        padding: 1.5rem;
        border-radius: 10px;
        border-left: 4px solid #00D9FF;
        margin: 1rem 0;
    }
    .success-box {
        background: linear-gradient(135deg, #1a3d2b 0%, #245236 100%);
        padding: 1.5rem;
        border-radius: 10px;
        border-left: 4px solid #00D97F;
    }
</style>
""", unsafe_allow_html=True)

st.title("📤 Dataset Upload & Ingestion")
st.markdown("### Load datasets from local directory for high-performance analysis")

# Backend API URL
API_BASE = "http://localhost:8080/api"

# Function to browse directory
def browse_folder():
    """Open a folder browser dialog"""
    root = tk.Tk()
    root.withdraw()
    root.wm_attributes('-topmost', 1)
    folder_path = filedialog.askdirectory(master=root)
    root.destroy()
    return folder_path

# Directory input section
st.markdown("---")
st.markdown('<div class="upload-section">', unsafe_allow_html=True)
st.markdown("#### 📁 Specify Dataset Directory")
st.info("💡 Point to the directory containing your CSV files (e.g., olist_customers_dataset.csv)")

# Create columns for input and browse button
col_input, col_browse = st.columns([4, 1])

with col_input:
    # Initialize session state for directory path
    if 'directory_path' not in st.session_state:
        st.session_state.directory_path = ""
    
    directory_path = st.text_input(
        "Dataset Directory Path",
        value=st.session_state.directory_path,
        placeholder="Click Browse button or enter path here (e.g., C:\\Users\\User\\Desktop\\Data)",
        help="Full path to the directory containing CSV datasets",
        key="dir_input"
    )
    st.session_state.directory_path = directory_path

with col_browse:
    st.markdown("<br>", unsafe_allow_html=True)  # Spacing
    if st.button("📂 Browse", key="browse_btn"):
        folder = browse_folder()
        if folder:
            st.session_state.directory_path = folder
            st.rerun()

# Display selected path
if st.session_state.directory_path and os.path.exists(st.session_state.directory_path):
    st.success(f"✅ Valid directory: `{st.session_state.directory_path}`")
    # List CSV files in directory
    try:
        csv_files = [f for f in os.listdir(st.session_state.directory_path) if f.endswith('.csv')]
        if csv_files:
            with st.expander(f"📄 Found {len(csv_files)} CSV file(s)", expanded=False):
                for file in csv_files:
                    st.markdown(f"- {file}")
        else:
            st.warning("⚠️ No CSV files found in the selected directory")
    except Exception as e:
        st.error(f"Cannot read directory: {str(e)}")
elif st.session_state.directory_path:
    st.error("❌ Invalid directory path - please check the path or use Browse button")
else:
    st.info("📂 Please select your dataset directory using the Browse button or enter the path manually")

st.markdown('</div>', unsafe_allow_html=True)

# Action buttons
st.markdown("---")
col1, col2, col3 = st.columns([2, 2, 3])
with col1:
    ingest_button = st.button("🚀 Ingest Datasets", type="primary", use_container_width=True)
with col2:
    stats_button = st.button("📊 View Current Stats", use_container_width=True)

if stats_button:
    try:
        with st.spinner("Fetching statistics..."):
            response = requests.get(f"{API_BASE}/dataset/stats", timeout=10)
            if response.status_code == 200:
                stats = response.json()
                st.markdown("---")
                st.markdown("### 📊 Current Database Statistics")
                
                col_a, col_b, col_c, col_d = st.columns(4)
                with col_a:
                    st.metric("👥 Customers", f"{stats.get('total_customers', 0):,}")
                with col_b:
                    st.metric("📦 Orders", f"{stats.get('total_orders', 0):,}")
                with col_c:
                    st.metric("🛍️ Products", f"{stats.get('total_products', 0):,}")
                with col_d:
                    st.metric("🏪 Sellers", f"{stats.get('total_sellers', 0):,}")
                
                col_e, col_f, col_g, col_h = st.columns(4)
                with col_e:
                    st.metric("📋 Order Items", f"{stats.get('total_order_items', 0):,}")
                with col_f:
                    st.metric("💳 Payments", f"{stats.get('total_payments', 0):,}")
                with col_g:
                    st.metric("🚚 Deliveries", f"{stats.get('total_deliveries', 0):,}")
                with col_h:
                    st.metric("⚠️ Faulty Records", f"{stats.get('total_faulty_records', 0):,}")
            else:
                st.error(f"❌ Backend error: {response.status_code}")
    except Exception as e:
        st.error(f"❌ Error: {str(e)}")
        st.info("Make sure the Spring Boot backend is running on port 8080")

# Ingestion process
if ingest_button:
    if not st.session_state.directory_path or not os.path.exists(st.session_state.directory_path):
        st.error("❌ Please provide a valid directory path")
    else:
        st.markdown("---")
        st.markdown("### 🔄 Ingestion Progress")
        
        progress_bar = st.progress(0)
        status_text = st.empty()
        
        try:
            status_text.text("🔌 Connecting to backend...")
            progress_bar.progress(10)
            time.sleep(0.3)
            
            # Call backend API
            status_text.text("📤 Sending dataset path to backend...")
            progress_bar.progress(20)
            
            response = requests.post(
                f"{API_BASE}/dataset/ingest",
                json={"directory_path": st.session_state.directory_path},
                timeout=300
            )
            
            progress_bar.progress(60)
            status_text.text("⚙️ Processing datasets... This may take a few minutes...")
            time.sleep(0.5)
            
            if response.status_code == 200:
                result = response.json()
                
                progress_bar.progress(100)
                status_text.text("✅ Ingestion completed successfully!")
                time.sleep(0.5)
                
                # Success message
                st.markdown('<div class="success-box">', unsafe_allow_html=True)
                st.success(f"🎉 Dataset ingestion completed in {result.get('totalProcessingTimeMs', 0):,} ms ({result.get('totalProcessingTimeMs', 0)/1000:.2f} seconds)")
                st.markdown('</div>', unsafe_allow_html=True)
                
                # Display results
                st.markdown("---")
                st.markdown("### 📈 Ingestion Results")
                
                # Row 1
                col1, col2, col3 = st.columns(3)
                
                with col1:
                    st.markdown("#### 👥 Customers")
                    st.metric("Total Records", f"{result.get('customers_total', 0):,}")
                    st.metric("Valid Records", f"{result.get('customers_valid', 0):,}", 
                             delta=f"-{result.get('customers_duplicates', 0):,} duplicates")
                
                with col2:
                    st.markdown("#### 📦 Orders")
                    st.metric("Total Records", f"{result.get('orders_total', 0):,}")
                    st.metric("Valid Records", f"{result.get('orders_valid', 0):,}",
                             delta=f"-{result.get('orders_duplicates', 0):,} duplicates")
                
                with col3:
                    st.markdown("#### 🛍️ Products")
                    st.metric("Total Records", f"{result.get('products_total', 0):,}")
                    st.metric("Valid Records", f"{result.get('products_valid', 0):,}",
                             delta=f"-{result.get('products_duplicates', 0):,} duplicates")
                
                st.markdown("---")
                
                # Row 2
                col4, col5, col6 = st.columns(3)
                
                with col4:
                    st.markdown("#### 📋 Order Items")
                    st.metric("Total Records", f"{result.get('order_items_total', 0):,}")
                    st.metric("Valid Records", f"{result.get('order_items_valid', 0):,}")
                
                with col5:
                    st.markdown("#### 💳 Payments")
                    st.metric("Total Records", f"{result.get('payments_total', 0):,}")
                    st.metric("Valid Records", f"{result.get('payments_valid', 0):,}")
                
                with col6:
                    st.markdown("#### 🏪 Sellers")
                    st.metric("Total Records", f"{result.get('sellers_total', 0):,}")
                    st.metric("Valid Records", f"{result.get('sellers_valid', 0):,}")
                
            else:
                progress_bar.progress(0)
                status_text.text("")
                st.error(f"❌ Ingestion failed with status code: {response.status_code}")
                with st.expander("View error details"):
                    st.code(response.text)
                
        except requests.exceptions.Timeout:
            progress_bar.progress(0)
            status_text.text("")
            st.error("❌ Request timed out. The dataset might be too large or the backend is not responding.")
        except requests.exceptions.ConnectionError:
            progress_bar.progress(0)
            status_text.text("")
            st.error("❌ Cannot connect to backend. Make sure the Spring Boot application is running on port 8080.")
        except Exception as e:
            progress_bar.progress(0)
            status_text.text("")
            st.error(f"❌ Error during ingestion: {str(e)}")

# Information panel
st.markdown("---")
st.markdown('<div class="info-box">', unsafe_allow_html=True)
st.markdown("### ℹ️ System Information")

col_info1, col_info2 = st.columns(2)

with col_info1:
    st.markdown("""
    **📂 Supported Datasets:**
    - `olist_customers_dataset.csv`
    - `olist_orders_dataset.csv`
    - `olist_order_items_dataset.csv`
    - `olist_order_payments_dataset.csv`
    - `olist_products_dataset.csv`
    - `olist_sellers_dataset.csv`
    """)

with col_info2:
    st.markdown("""
    **⚡ Performance Features:**
    - ✅ Chunk-based processing (5,000 records/chunk)
    - ✅ Automatic duplicate removal
    - ✅ Data validation and cleansing
    - ✅ Dead-Letter Queue for faulty records
    - ✅ Memory-efficient streaming
    - ✅ High-performance H2 database
    """)

st.markdown('</div>', unsafe_allow_html=True)
