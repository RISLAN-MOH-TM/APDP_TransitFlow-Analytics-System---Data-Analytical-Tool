package com.transitflow.strategy;

import java.util.Map;

/**
 * Strategy Pattern Interface: Allows switching analytical algorithms dynamically
 */
public interface AnalyticsStrategy {
    
    /**
     * Execute the analytical computation
     * @return Map containing the analytical results
     */
    Map<String, Object> execute();
    
    /**
     * Get the strategy name
     * @return Strategy identifier
     */
    String getStrategyName();
}
