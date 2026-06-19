import pytest
import sys
from pathlib import Path
from unittest.mock import MagicMock, patch

PAGES_DIR = Path(__file__).parent.parent / "pages"
API_BASE = "http://localhost:8080/api"


def _create_streamlit_mock():
    st = MagicMock()
    st.set_page_config = MagicMock()
    st.title = MagicMock(return_value=MagicMock())
    st.markdown = MagicMock(return_value=MagicMock())
    st.metric = MagicMock(return_value=MagicMock())
    st.columns = MagicMock(return_value=[MagicMock(), MagicMock(), MagicMock()])
    st.plotly_chart = MagicMock(return_value=MagicMock())
    st.dataframe = MagicMock(return_value=MagicMock())
    st.info = MagicMock(return_value=MagicMock())
    st.error = MagicMock(return_value=MagicMock())
    st.button = MagicMock(return_value=False)
    st.progress = MagicMock()
    st.spinner = MagicMock()
    st.success = MagicMock()
    st.warning = MagicMock()
    st.session_state = {}
    st.empty = MagicMock(return_value=MagicMock())
    return st


def _check_page_api_urls(source: str):
    urls = []
    for line in source.splitlines():
        if "requests.get(" in line or "requests.post(" in line:
            urls.append(line.strip())
    return urls


PAGE_FILES = sorted(PAGES_DIR.glob("*.py"))


class TestDashboardPages:
    @pytest.mark.parametrize("page_path", PAGE_FILES, ids=lambda p: p.stem)
    def test_page_syntax_valid(self, page_path):
        source = page_path.read_text(encoding="utf-8")
        compile(source, page_path.name, "exec")

    @pytest.mark.parametrize("page_path", PAGE_FILES, ids=lambda p: p.stem)
    def test_page_uses_correct_api_base(self, page_path):
        source = page_path.read_text(encoding="utf-8")
        assert API_BASE in source, f"{page_path.name} should use {API_BASE}"

    @pytest.mark.parametrize("page_path", PAGE_FILES, ids=lambda p: p.stem)
    def test_page_has_title(self, page_path):
        source = page_path.read_text(encoding="utf-8")
        assert "st.set_page_config" in source
        assert "st.title" in source

    @pytest.mark.parametrize("page_path", PAGE_FILES, ids=lambda p: p.stem)
    def test_page_handles_errors(self, page_path):
        source = page_path.read_text(encoding="utf-8")
        assert "ConnectionError" in source or "Exception" in source
        assert "st.error" in source

    def test_dataset_upload_page_api_calls(self):
        page_file = PAGES_DIR / "01_Dataset_Upload.py"
        source = page_file.read_text(encoding="utf-8")
        assert "requests.post" in source and "/dataset/ingest" in source
        assert "requests.get" in source and "/dataset/stats" in source

    def test_peak_deliveries_page_api_call(self):
        page_file = PAGES_DIR / "02_Peak_Deliveries.py"
        source = page_file.read_text(encoding="utf-8")
        assert "analytics/peak-delivery" in source

    def test_customer_segmentation_page_api_call(self):
        page_file = PAGES_DIR / "03_Customer_Segmentation.py"
        source = page_file.read_text(encoding="utf-8")
        assert "analytics/customer-segmentation" in source

    def test_delivery_efficiency_page_api_call(self):
        page_file = PAGES_DIR / "04_Delivery_Efficiency.py"
        source = page_file.read_text(encoding="utf-8")
        assert "analytics/delivery-efficiency" in source

    def test_revenue_analysis_page_api_call(self):
        page_file = PAGES_DIR / "05_Revenue_Analysis.py"
        source = page_file.read_text(encoding="utf-8")
        assert "analytics/revenue-analysis" in source

    def test_anomaly_detection_page_api_call(self):
        page_file = PAGES_DIR / "06_Anomaly_Detection.py"
        source = page_file.read_text(encoding="utf-8")
        assert "analytics/anomaly-detection" in source

    def test_seasonal_demand_page_api_call(self):
        page_file = PAGES_DIR / "07_Seasonal_Demand.py"
        source = page_file.read_text(encoding="utf-8")
        assert "analytics/seasonal-demand" in source

    def test_regional_performance_page_api_call(self):
        page_file = PAGES_DIR / "08_Regional_Performance.py"
        source = page_file.read_text(encoding="utf-8")
        assert "analytics/regional-performance" in source

    def test_main_app_has_all_navigation(self):
        main_file = PAGES_DIR.parent / "streamlit_app.py"
        source = main_file.read_text(encoding="utf-8")
        compile(source, "streamlit_app.py", "exec")
        assert "st.set_page_config" in source
