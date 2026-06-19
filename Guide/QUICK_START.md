# TransitFlow Analytics - Quick Start Guide

## 🚀 Fastest Way to Run

### Option 1: One-Click Launcher (Windows)
Double-click **`RUN_APPLICATION.bat`** in the root directory

This will:
1. Start the Spring Boot backend on port 8080
2. Wait 30 seconds for initialization
3. Start the Streamlit frontend on port 8501

---

### Option 2: Manual Start

#### Step 1: Start Backend
```bash
cd backend
mvnw.cmd spring-boot:run
```

Wait until you see: `Started TransitFlowApplication in X seconds`

#### Step 2: Start Frontend (in new terminal)
```bash
cd frontend
pip install -r requirements.txt
streamlit run streamlit_app.py
```

---

## 📊 Using the Application

### 1. Upload Your Data

1. Navigate to **"Dataset Upload"** in the Streamlit sidebar
2. Enter your dataset directory path:
   ```
   c:\Users\User\User\Desktop\APDP\Data set
   ```
3. Click **"Ingest Datasets"**
4. Watch the progress bar and view results

### 2. Explore Analytics

After ingestion completes, explore these dashboards:

- **Peak Deliveries:** See hourly/daily order patterns
- **Customer Segmentation:** Geographic and behavioral insights
- **Delivery Efficiency:** Transit times and delay analysis
- **Revenue Analysis:** Financial performance metrics
- **Anomaly Detection:** Identify outliers

---

## ✅ Verify Installation

### Check Java Version
```bash
java -version
```
Should show Java 21+

### Check Python Version
```bash
python --version
```
Should show Python 3.10+

### Test Backend
Open browser: http://localhost:8080/api/dataset/stats

Should return JSON with database statistics

### Test Frontend
Open browser: http://localhost:8501

Should show the TransitFlow dashboard

---

## ⚠️ Troubleshooting

### Backend Won't Start
- **Issue:** Port 8080 already in use
- **Fix:** Find and stop the process using port 8080, or change port in `application.properties`

### Frontend Connection Error
- **Issue:** Cannot connect to backend
- **Fix:** Ensure backend is running first. Check http://localhost:8080/api/dataset/stats

### Ingestion Fails
- **Issue:** Invalid directory path
- **Fix:** Verify the path exists and contains CSV files

### Out of Memory
- **Issue:** Large datasets cause memory errors
- **Fix:** Increase JVM heap: 
  ```bash
  set MAVEN_OPTS=-Xmx4G
  mvnw spring-boot:run
  ```

---

## 📁 Dataset Files Required

Place these CSV files in your data directory:
- `olist_customers_dataset.csv`
- `olist_orders_dataset.csv`
- `olist_order_items_dataset.csv`
- `olist_order_payments_dataset.csv`
- `olist_products_dataset.csv`
- `olist_sellers_dataset.csv`

---

## 🎯 Key Features

✅ Memory-efficient chunk processing (5,000 records/chunk)  
✅ Automatic duplicate removal  
✅ Dead-Letter Queue for error tracking  
✅ In-memory H2 database (no external DB needed)  
✅ Interactive Plotly visualizations  
✅ Dark-themed premium UI  

---

## 📞 Need Help?

- Check the main **README.md** for detailed documentation
- Review backend logs in the console
- Open H2 console: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:transitflowdb`
  - Username: `sa`
  - Password: (empty)

---

**Ready to analyze your logistics data!** 🚀
