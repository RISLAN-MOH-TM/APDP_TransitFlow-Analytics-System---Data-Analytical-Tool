package com.transitflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RootController {
    
    @GetMapping("/")
    @ResponseBody
    public String welcome() {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>TransitFlow Analytics API</title>
                <style>
                    * {
                        margin: 0;
                        padding: 0;
                        box-sizing: border-box;
                    }
                    body {
                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                        background: linear-gradient(135deg, #0E1117 0%, #1a1f2e 100%);
                        color: #ffffff;
                        min-height: 100vh;
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        padding: 20px;
                    }
                    .container {
                        max-width: 900px;
                        background: linear-gradient(135deg, #1E2530 0%, #252d3d 100%);
                        padding: 3rem;
                        border-radius: 20px;
                        border: 2px solid #2d3548;
                        box-shadow: 0 10px 40px rgba(0, 0, 0, 0.5);
                    }
                    h1 {
                        color: #00D9FF;
                        font-size: 2.5rem;
                        margin-bottom: 1rem;
                        text-align: center;
                    }
                    .subtitle {
                        text-align: center;
                        color: #8b95a5;
                        font-size: 1.1rem;
                        margin-bottom: 2rem;
                    }
                    .status {
                        background: linear-gradient(135deg, #1a3d2b 0%, #245236 100%);
                        padding: 1.5rem;
                        border-radius: 10px;
                        border-left: 4px solid #00D97F;
                        margin-bottom: 2rem;
                        text-align: center;
                    }
                    .status h2 {
                        color: #00D97F;
                        font-size: 1.5rem;
                        margin-bottom: 0.5rem;
                    }
                    .endpoints {
                        background: #1a2332;
                        padding: 2rem;
                        border-radius: 10px;
                        border: 1px solid #2d3548;
                        margin-bottom: 2rem;
                    }
                    .endpoints h3 {
                        color: #00D9FF;
                        margin-bottom: 1rem;
                        font-size: 1.3rem;
                    }
                    .endpoint {
                        background: #252d3d;
                        padding: 1rem;
                        margin: 0.75rem 0;
                        border-radius: 8px;
                        border-left: 3px solid #00D9FF;
                        display: flex;
                        align-items: center;
                        gap: 1rem;
                    }
                    .method {
                        background: #00D9FF;
                        color: #0E1117;
                        padding: 0.3rem 0.8rem;
                        border-radius: 5px;
                        font-weight: bold;
                        font-size: 0.85rem;
                    }
                    .method.post {
                        background: #00D97F;
                    }
                    .path {
                        font-family: 'Courier New', monospace;
                        color: #00D9FF;
                    }
                    .description {
                        color: #8b95a5;
                        font-size: 0.9rem;
                    }
                    .features {
                        display: grid;
                        grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
                        gap: 1rem;
                        margin-top: 2rem;
                    }
                    .feature {
                        background: linear-gradient(135deg, #1E2530 0%, #252d3d 100%);
                        padding: 1.5rem;
                        border-radius: 10px;
                        border: 1px solid #2d3548;
                        text-align: center;
                    }
                    .feature-icon {
                        font-size: 2rem;
                        margin-bottom: 0.5rem;
                    }
                    .feature h4 {
                        color: #00D9FF;
                        margin-bottom: 0.5rem;
                    }
                    .feature p {
                        color: #8b95a5;
                        font-size: 0.9rem;
                    }
                    .footer {
                        text-align: center;
                        margin-top: 2rem;
                        padding-top: 2rem;
                        border-top: 1px solid #2d3548;
                        color: #6c757d;
                    }
                    a {
                        color: #00D9FF;
                        text-decoration: none;
                    }
                    a:hover {
                        text-decoration: underline;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1>📦 TransitFlow Analytics API</h1>
                    <p class="subtitle">Enterprise-Level Logistics & E-Commerce Intelligence Platform</p>
                    
                    <div class="status">
                        <h2>✅ API is Running</h2>
                        <p>Spring Boot 3.3.5 | Java 21 | H2 Database</p>
                    </div>
                    
                    <div class="endpoints">
                        <h3>🔌 Available API Endpoints</h3>
                        
                        <div class="endpoint">
                            <span class="method post">POST</span>
                            <span class="path">/api/dataset/ingest</span>
                            <span class="description">Ingest datasets from directory</span>
                        </div>
                        
                        <div class="endpoint">
                            <span class="method">GET</span>
                            <span class="path">/api/dataset/stats</span>
                            <span class="description">Get database statistics</span>
                        </div>
                        
                        <div class="endpoint">
                            <span class="method">GET</span>
                            <span class="path">/api/analytics/peak-delivery</span>
                            <span class="description">Peak delivery time analysis</span>
                        </div>
                        
                        <div class="endpoint">
                            <span class="method">GET</span>
                            <span class="path">/api/analytics/customer-segmentation</span>
                            <span class="description">Customer segmentation insights</span>
                        </div>
                        
                        <div class="endpoint">
                            <span class="method">GET</span>
                            <span class="path">/api/analytics/delivery-efficiency</span>
                            <span class="description">Delivery efficiency metrics</span>
                        </div>
                        
                        <div class="endpoint">
                            <span class="method">GET</span>
                            <span class="path">/api/analytics/revenue-analysis</span>
                            <span class="description">Revenue analysis dashboard</span>
                        </div>
                        
                        <div class="endpoint">
                            <span class="method">GET</span>
                            <span class="path">/api/analytics/anomaly-detection</span>
                            <span class="description">Anomaly detection results</span>
                        </div>
                        
                        <div class="endpoint">
                            <span class="method">GET</span>
                            <span class="path">/api/analytics/seasonal-demand</span>
                            <span class="description">Seasonal demand patterns</span>
                        </div>
                        
                        <div class="endpoint">
                            <span class="method">GET</span>
                            <span class="path">/api/analytics/regional-performance</span>
                            <span class="description">Regional performance metrics</span>
                        </div>
                    </div>
                    
                    <div class="features">
                        <div class="feature">
                            <div class="feature-icon">⚡</div>
                            <h4>High Performance</h4>
                            <p>Chunk-based processing with 5K records/chunk</p>
                        </div>
                        <div class="feature">
                            <div class="feature-icon">🧠</div>
                            <h4>Smart Processing</h4>
                            <p>Automatic validation & duplicate removal</p>
                        </div>
                        <div class="feature">
                            <div class="feature-icon">📊</div>
                            <h4>Rich Analytics</h4>
                            <p>Multiple analytics strategies available</p>
                        </div>
                    </div>
                    
                    <div class="footer">
                        <p><strong>TransitFlow Analytics System v1.0.0</strong></p>
                        <p>Access the <a href="/h2-console" target="_blank">H2 Database Console</a></p>
                        <p style="margin-top: 1rem;">Frontend: <a href="http://localhost:8501" target="_blank">http://localhost:8501</a></p>
                    </div>
                </div>
            </body>
            </html>
            """;
    }
}
