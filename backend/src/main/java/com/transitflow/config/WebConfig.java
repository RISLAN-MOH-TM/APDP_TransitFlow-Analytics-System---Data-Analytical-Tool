package com.transitflow.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web Configuration - CORS and MVC Settings
 * 
 * PURPOSE:
 * Configures Cross-Origin Resource Sharing (CORS) to allow the frontend 
 * (Streamlit on port 8501) to communicate with this backend API (port 8080).
 * 
 * CURRENT CONFIGURATION:
 * - Allows all origins using wildcard pattern (*)
 * - Supports all standard HTTP methods (GET, POST, PUT, DELETE, OPTIONS)
 * - Allows all headers
 * - Credentials disabled for broader compatibility
 * 
 * SECURITY CONSIDERATIONS:
 * This is a development/demo configuration. For production use:
 * 
 * FUTURE UPGRADES:
 * 1. RESTRICT ORIGINS: Replace "*" with specific frontend domains
 *    Example: .allowedOrigins("https://yourdomain.com")
 * 
 * 2. ENABLE CREDENTIALS: If using cookies/sessions
 *    .allowCredentials(true)
 *    .allowedOrigins("http://localhost:8501") // Must specify exact origins
 * 
 * 3. LIMIT METHODS: Restrict to only needed HTTP methods
 *    .allowedMethods("GET", "POST") // Remove PUT, DELETE if not used
 * 
 * 4. ADD SECURITY HEADERS:
 *    - Content-Security-Policy
 *    - X-Frame-Options
 *    - X-Content-Type-Options
 * 
 * 5. IMPLEMENT RATE LIMITING: Prevent API abuse
 * 
 * @author TransitFlow Team
 * @version 1.0.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    /**
     * Configure CORS mappings for all endpoints
     * 
     * NOTE: Using allowedOriginPatterns("*") instead of allowedOrigins("*")
     * to avoid conflicts with allowCredentials in future configurations
     * 
     * @param registry CORS registry to configure
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false);
    }
}
