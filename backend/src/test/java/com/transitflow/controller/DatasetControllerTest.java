package com.transitflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transitflow.facade.TransitFlowFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Map;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Dataset Controller Tests")
class DatasetControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransitFlowFacade facade;

    @InjectMocks
    private DatasetController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("POST /api/dataset/ingest with valid path returns 200")
    void testIngestSuccess() throws Exception {
        when(facade.ingestDatasets("/data/path"))
            .thenReturn(Map.of("status", "SUCCESS", "totalProcessingTimeMs", 15000));

        mockMvc.perform(post("/api/dataset/ingest")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"directory_path\": \"/data/path\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("SUCCESS"));
    }

    @Test
    @DisplayName("POST /api/dataset/ingest with empty path returns 400")
    void testIngestEmptyPath() throws Exception {
        mockMvc.perform(post("/api/dataset/ingest")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"directory_path\": \"\"}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/dataset/ingest with missing path returns 400")
    void testIngestMissingPath() throws Exception {
        mockMvc.perform(post("/api/dataset/ingest")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/dataset/ingest returns 500 on service error")
    void testIngestInternalError() throws Exception {
        when(facade.ingestDatasets(anyString()))
            .thenThrow(new RuntimeException("Ingestion failed"));

        mockMvc.perform(post("/api/dataset/ingest")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"directory_path\": \"/data/path\"}"))
            .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("GET /api/dataset/stats returns 200")
    void testGetStats() throws Exception {
        when(facade.getIngestionStats())
            .thenReturn(Map.of("total_customers", 1000, "total_orders", 5000));

        mockMvc.perform(get("/api/dataset/stats"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.total_customers").value(1000));
    }

    @Test
    @DisplayName("GET /api/dataset/stats returns 500 on service error")
    void testGetStatsInternalError() throws Exception {
        when(facade.getIngestionStats())
            .thenThrow(new RuntimeException("DB unavailable"));

        mockMvc.perform(get("/api/dataset/stats"))
            .andExpect(status().isInternalServerError());
    }
}
