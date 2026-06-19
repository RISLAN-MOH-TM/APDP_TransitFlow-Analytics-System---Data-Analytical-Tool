# TransitFlow Analytics - Troubleshooting Guide

## Common Issues and Solutions

---

## 🔴 Backend Issues

### Issue 1: Backend Won't Start

**Error:** "Port 8080 already in use"

**Solution:**
```bash
# Option A: Find and kill process using port 8080
netstat -ano | findstr :8080
taskkill /PID <process_id> /F

# Option B: Change port in application.properties
server.port=8081
```

---

### Issue 2: Java Version Error

**Error:** "Unsupported class file major version 65"

**Cause:** Java version is less than 21

**Solution:**
```bash
# Check Java version
java -version

# Should show: java version "21" or higher
# If not, download and install Java 21 from:
# https://adoptium.net/
```

---

### Issue 3: Maven Build Fails

**Error:** "mvnw.cmd is not recognized"

**Solution:**
```bash
# Make sure you're in the backend directory
cd backend

# Try using the full path
.\mvnw.cmd clean install

# Or use system Maven if installed
mvn clean install
```

---

### Issue 4: Out of Memory During Ingestion

**Error:** "java.lang.OutOfMemoryError: Java heap space"

**Solution:**
```bash
# Increase JVM heap size
set MAVEN_OPTS=-Xmx4G

# Then run
mvnw.cmd spring-boot:run

# OR edit application.properties to reduce chunk size
# In DatasetService.java, change:
# private static final int CHUNK_SIZE = 5000;
# To:
# private static final int CHUNK_SIZE = 2500;
```

---

### Issue 5: H2 Console Not Opening

**Error:** Cannot access http://localhost:8080/h2-console

**Solution:**
1. Verify backend is running
2. Check application.properties has:
   ```properties
   spring.h2.console.enabled=true
   ```
3. Try: http://localhost:8080/h2-console
4. Use these credentials:
   - JDBC URL: `jdbc:h2:mem:transitflowdb`
   - Username: `sa`
   - Password: (leave empty)

---

## 🔵 Frontend Issues

### Issue 6: Frontend Won't Start

**Error:** "streamlit: command not found"

**Solution:**
```bash
# Install Python dependencies
cd frontend
pip install -r requirements.txt

# If pip is not found, try:
python -m pip install -r requirements.txt

# Or use Python 3 explicitly:
pip3 install -r requirements.txt
```

---

### Issue 7: Connection Refused Error

**Error:** "❌ Cannot connect to backend. Make sure it's running on port 8080"

**Solution:**
1. **Check backend is running:**
   - Open http://localhost:8080/api/dataset/stats
   - Should return JSON
   
2. **Check firewall:**
   - Allow port 8080 in Windows Firewall
   
3. **Check CORS:**
   - WebConfig.java should allow http://localhost:8501

---

### Issue 8: Charts Not Rendering

**Error:** Blank pages or broken visualizations

**Solution:**
```bash
# Update Plotly
pip install --upgrade plotly

# Clear browser cache
# Press Ctrl + Shift + Delete in browser

# Restart Streamlit
streamlit run streamlit_app.py
```

---

### Issue 9: Python Version Too Old

**Error:** "Python 3.10+ required"

**Solution:**
```bash
# Check Python version
python --version

# Should show: Python 3.10 or higher
# If not, download from: https://www.python.org/downloads/

# Make sure to check "Add to PATH" during installation
```

---

## 📁 Data Issues

### Issue 10: Ingestion Fails - Invalid Directory

**Error:** "Invalid directory path"

**Solution:**
1. Use absolute path with double backslashes:
   ```
   c:\\Users\\User\\User\\Desktop\\APDP\\Data set
   ```
   
2. Or use forward slashes:
   ```
   c:/Users/User/User/Desktop/APDP/Data set
   ```

3. Verify directory exists and contains CSV files

---

### Issue 11: CSV Files Not Found

**Error:** "Customer dataset not found"

**Solution:**
1. Check file names match exactly:
   - `olist_customers_dataset.csv`
   - `olist_orders_dataset.csv`
   - `olist_order_items_dataset.csv`
   - `olist_order_payments_dataset.csv`
   - `olist_products_dataset.csv`
   - `olist_sellers_dataset.csv`

2. Files are case-sensitive on some systems

---

### Issue 12: Slow Ingestion

**Symptom:** Processing takes too long

**Solution:**
1. **For large files:**
   - Increase chunk size in `DatasetService.java`
   - Change: `CHUNK_SIZE = 5000` to `CHUNK_SIZE = 10000`

2. **Reduce logging:**
   - In `application.properties`:
   ```properties
   spring.jpa.show-sql=false
   logging.level.com.transitflow=WARN
   ```

3. **Disable H2 console during ingestion:**
   ```properties
   spring.h2.console.enabled=false
   ```

---

## 🔧 General Issues

### Issue 13: RUN_APPLICATION.bat Doesn't Work

**Error:** Windows script fails

**Solution:**
1. **Run components manually:**
   ```bash
   # Terminal 1
   cd backend
   mvnw.cmd spring-boot:run
   
   # Terminal 2 (wait 30 seconds)
   cd frontend
   streamlit run streamlit_app.py
   ```

2. **Check paths in script:**
   - Edit RUN_APPLICATION.bat
   - Verify paths match your setup

---

### Issue 14: Empty Analytics Pages

**Error:** No data in dashboards

**Solution:**
1. **Ingest data first:**
   - Go to "Dataset Upload"
   - Complete ingestion
   - Then view analytics

2. **Check ingestion was successful:**
   - Visit http://localhost:8080/api/dataset/stats
   - Should show non-zero counts

---

### Issue 15: Duplicate Records

**Symptom:** Seeing duplicate data

**Solution:**
1. **Re-ingest from scratch:**
   - Restart backend (clears H2 in-memory DB)
   - Re-run ingestion
   
2. **Check DLQ:**
   - Visit Dataset Upload page
   - Check "Duplicates Removed" metric

---

## 🛠️ Advanced Troubleshooting

### Check Backend Logs
```bash
# Look for errors in console where backend is running
# Common patterns to search for:
# - ERROR
# - Exception
# - Failed
```

### Test Backend API Directly
```bash
# Using curl
curl http://localhost:8080/api/dataset/stats

# Using PowerShell
Invoke-WebRequest -Uri http://localhost:8080/api/dataset/stats
```

### Check Database Contents
1. Open H2 Console: http://localhost:8080/h2-console
2. Run queries:
   ```sql
   SELECT COUNT(*) FROM CUSTOMERS;
   SELECT COUNT(*) FROM ORDERS;
   SELECT * FROM FAULTY_RECORDS;
   ```

### Clear Everything and Start Fresh
```bash
# Stop both applications
# Restart backend (clears in-memory DB)
cd backend
mvnw.cmd spring-boot:run

# Restart frontend
cd frontend
streamlit run streamlit_app.py

# Re-ingest data
```

---

## 📞 Still Having Issues?

### Checklist:
- [ ] Java 21+ installed and in PATH
- [ ] Python 3.10+ installed and in PATH
- [ ] Backend running on port 8080
- [ ] Frontend running on port 8501
- [ ] CSV files in correct directory
- [ ] File names match exactly
- [ ] No firewall blocking ports
- [ ] Sufficient RAM available (4GB+ recommended)

### System Requirements:
- **OS:** Windows 10/11
- **RAM:** 4GB minimum, 8GB recommended
- **Disk:** 500MB free space
- **Java:** 21 or higher
- **Python:** 3.10 or higher
- **Network:** Ports 8080 and 8501 available

---

## 🔍 Debug Mode

### Enable Detailed Logging

**Backend (application.properties):**
```properties
logging.level.com.transitflow=DEBUG
spring.jpa.show-sql=true
```

**Frontend:**
Add to streamlit_app.py:
```python
import logging
logging.basicConfig(level=logging.DEBUG)
```

---

## ✅ Verification Steps

After fixing issues, verify:

1. **Backend health:**
   ```bash
   curl http://localhost:8080/api/dataset/stats
   ```

2. **Frontend loads:**
   - Open http://localhost:8501
   - Should see main dashboard

3. **Data ingestion works:**
   - Upload page accessible
   - Can specify directory
   - Progress bar appears

4. **Analytics render:**
   - All 6 pages load
   - Charts display
   - No console errors

---

**Most issues are resolved by:**
1. Restarting both services
2. Checking Java/Python versions
3. Verifying paths and file names
4. Ensuring ports are available
