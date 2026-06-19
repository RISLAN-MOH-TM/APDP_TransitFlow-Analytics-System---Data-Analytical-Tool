# TransitFlow Analytics Frontend

Interactive Streamlit dashboard for visualizing logistics and e-commerce analytics.

## Features

- **Real-Time Dashboards:** Live data visualization with Plotly
- **Multiple Analytics Views:** 6 specialized dashboard pages
- **Premium Design:** Deep navy, charcoal, and electric cyan theme
- **Responsive Layout:** Optimized for desktop viewing

## Prerequisites

- Python 3.10+
- pip package manager

## Quick Start

### 1. Install Dependencies

```bash
cd frontend
pip install -r requirements.txt
```

### 2. Run the Application

```bash
streamlit run streamlit_app.py
```

The frontend will open automatically in your browser at **http://localhost:8501**

## Dashboard Pages

### 1. Dataset Upload (01_Dataset_Upload.py)
- Specify local directory path
- View ingestion progress
- Monitor chunk processing and validation
- View duplicates removed and DLQ metrics

### 2. Peak Deliveries (02_Peak_Deliveries.py)
- Hourly order distribution
- Day of week trends
- Daily volume analysis

### 3. Customer Segmentation (03_Customer_Segmentation.py)
- Geographic distribution by state
- Customer frequency segments
- One-time vs repeat customers

### 4. Delivery Efficiency (04_Delivery_Efficiency.py)
- Average transit times
- Delay analysis
- On-time vs delayed metrics

### 5. Revenue Analysis (05_Revenue_Analysis.py)
- Total revenue metrics
- Payment type distribution
- Monthly revenue trends

### 6. Anomaly Detection (06_Anomaly_Detection.py)
- High-price anomalies
- Extreme delivery delays
- Statistical outlier detection

## Configuration

The frontend connects to the backend at `http://localhost:8080/api`

To change the backend URL, edit the `API_BASE` variable in each page file.

## Customization

### Theme Colors

Edit the CSS in `streamlit_app.py`:
- Background: `#0E1117` (dark navy)
- Accent: `#00D9FF` (electric cyan)
- Cards: `#1E2530` (charcoal)

### Chart Styles

All Plotly charts use the dark theme configuration:
```python
fig.update_layout(
    plot_bgcolor="#1E2530",
    paper_bgcolor="#1E2530",
    font_color="#FFFFFF"
)
```

## Troubleshooting

**Connection Refused:**
- Ensure the Spring Boot backend is running on port 8080
- Check firewall settings

**Slow Loading:**
- Large datasets may take time to process
- Progress bars show ingestion status

**Charts Not Rendering:**
- Clear browser cache
- Update Plotly: `pip install --upgrade plotly`

## Development

To add a new page:
1. Create `pages/0X_PageName.py`
2. Import necessary libraries
3. Use the existing page structure as a template
4. Apply the consistent theme and layout
