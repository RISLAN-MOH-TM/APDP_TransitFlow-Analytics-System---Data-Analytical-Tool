package com.transitflow.service;

import com.transitflow.adapter.DatasetAdapter;
import com.transitflow.factory.DatasetLoaderFactory;
import com.transitflow.model.*;
import com.transitflow.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
@Service
public class DatasetService {
    
    private static final int CHUNK_SIZE = 5000;
    
    @Autowired private CustomerRepository customerRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderItemRepository orderItemRepository;
    @Autowired private PaymentRepository paymentRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private SellerRepository sellerRepository;
    @Autowired private DeliveryRepository deliveryRepository;
    @Autowired private FaultyRecordRepository faultyRecordRepository;
    
    @Transactional
    public Map<String, Object> ingestFromDirectory(String directoryPath) {
        Map<String, Object> result = new HashMap<>();
        long startTime = System.currentTimeMillis();
        
        try {
            Path dirPath = Paths.get(directoryPath);
            
            if (!Files.exists(dirPath) || !Files.isDirectory(dirPath)) {
                throw new IllegalArgumentException("Invalid directory path");
            }
            
            // Process each dataset file
            processCustomers(dirPath, result);
            processOrders(dirPath, result);
            processOrderItems(dirPath, result);
            processPayments(dirPath, result);
            processProducts(dirPath, result);
            processSellers(dirPath, result);
            
            // Generate delivery analytics
            generateDeliveryMetrics();
            
            long endTime = System.currentTimeMillis();
            result.put("totalProcessingTimeMs", endTime - startTime);
            result.put("status", "SUCCESS");
            
            log.info("Dataset ingestion completed in {} ms", endTime - startTime);
            
        } catch (Exception e) {
            log.error("Error during dataset ingestion", e);
            result.put("status", "FAILED");
            result.put("error", e.getMessage());
        }
        
        return result;
    }
    
    private void processCustomers(Path dirPath, Map<String, Object> result) throws Exception {
        Path customerFile = findFile(dirPath, "olist_customers_dataset.csv");
        if (customerFile == null) {
            log.warn("Customer dataset not found");
            return;
        }
        
        log.info("Processing customers from: {}", customerFile);
        DatasetAdapter<Customer> adapter = DatasetLoaderFactory.createCustomerLoader(customerFile);
        
        List<List<Customer>> chunks = adapter.parseInChunks(CHUNK_SIZE);
        int totalRecords = 0;
        int validRecords = 0;
        int duplicatesRemoved = 0;
        Set<String> seenIds = new HashSet<>();
        
        for (List<Customer> chunk : chunks) {
            totalRecords += chunk.size();
            List<Customer> validChunk = new ArrayList<>();
            
            for (Customer customer : chunk) {
                if (isValidCustomer(customer) && !seenIds.contains(customer.getCustomerId())) {
                    validChunk.add(customer);
                    seenIds.add(customer.getCustomerId());
                    validRecords++;
                } else if (seenIds.contains(customer.getCustomerId())) {
                    duplicatesRemoved++;
                }
            }
            
            if (!validChunk.isEmpty()) {
                customerRepository.saveAll(validChunk);
            }
        }
        
        result.put("customers_total", totalRecords);
        result.put("customers_valid", validRecords);
        result.put("customers_duplicates", duplicatesRemoved);
        log.info("Customers processed: {} total, {} valid, {} duplicates", totalRecords, validRecords, duplicatesRemoved);
    }
    
    private void processOrders(Path dirPath, Map<String, Object> result) throws Exception {
        Path orderFile = findFile(dirPath, "olist_orders_dataset.csv");
        if (orderFile == null) {
            log.warn("Order dataset not found");
            return;
        }
        
        log.info("Processing orders from: {}", orderFile);
        DatasetAdapter<Order> adapter = DatasetLoaderFactory.createOrderLoader(orderFile);
        
        List<List<Order>> chunks = adapter.parseInChunks(CHUNK_SIZE);
        int totalRecords = 0;
        int validRecords = 0;
        int duplicatesRemoved = 0;
        Set<String> seenIds = new HashSet<>();
        
        for (List<Order> chunk : chunks) {
            totalRecords += chunk.size();
            List<Order> validChunk = new ArrayList<>();
            
            for (Order order : chunk) {
                if (isValidOrder(order) && !seenIds.contains(order.getOrderId())) {
                    validChunk.add(order);
                    seenIds.add(order.getOrderId());
                    validRecords++;
                } else if (seenIds.contains(order.getOrderId())) {
                    duplicatesRemoved++;
                }
            }
            
            if (!validChunk.isEmpty()) {
                orderRepository.saveAll(validChunk);
            }
        }
        
        result.put("orders_total", totalRecords);
        result.put("orders_valid", validRecords);
        result.put("orders_duplicates", duplicatesRemoved);
        log.info("Orders processed: {} total, {} valid, {} duplicates", totalRecords, validRecords, duplicatesRemoved);
    }
    
    private void processOrderItems(Path dirPath, Map<String, Object> result) throws Exception {
        Path orderItemFile = findFile(dirPath, "olist_order_items_dataset.csv");
        if (orderItemFile == null) {
            log.warn("Order items dataset not found");
            return;
        }
        
        log.info("Processing order items from: {}", orderItemFile);
        DatasetAdapter<OrderItem> adapter = DatasetLoaderFactory.createOrderItemLoader(orderItemFile);
        
        List<List<OrderItem>> chunks = adapter.parseInChunks(CHUNK_SIZE);
        int totalRecords = 0;
        int validRecords = 0;
        
        for (List<OrderItem> chunk : chunks) {
            totalRecords += chunk.size();
            List<OrderItem> validChunk = new ArrayList<>();
            
            for (OrderItem item : chunk) {
                if (isValidOrderItem(item)) {
                    validChunk.add(item);
                    validRecords++;
                }
            }
            
            if (!validChunk.isEmpty()) {
                orderItemRepository.saveAll(validChunk);
            }
        }
        
        result.put("order_items_total", totalRecords);
        result.put("order_items_valid", validRecords);
        log.info("Order items processed: {} total, {} valid", totalRecords, validRecords);
    }
    
    private void processPayments(Path dirPath, Map<String, Object> result) throws Exception {
        Path paymentFile = findFile(dirPath, "olist_order_payments_dataset.csv");
        if (paymentFile == null) {
            log.warn("Payment dataset not found");
            return;
        }
        
        log.info("Processing payments from: {}", paymentFile);
        DatasetAdapter<Payment> adapter = DatasetLoaderFactory.createPaymentLoader(paymentFile);
        
        List<List<Payment>> chunks = adapter.parseInChunks(CHUNK_SIZE);
        int totalRecords = 0;
        int validRecords = 0;
        
        for (List<Payment> chunk : chunks) {
            totalRecords += chunk.size();
            List<Payment> validChunk = new ArrayList<>();
            
            for (Payment payment : chunk) {
                if (isValidPayment(payment)) {
                    validChunk.add(payment);
                    validRecords++;
                }
            }
            
            if (!validChunk.isEmpty()) {
                paymentRepository.saveAll(validChunk);
            }
        }
        
        result.put("payments_total", totalRecords);
        result.put("payments_valid", validRecords);
        log.info("Payments processed: {} total, {} valid", totalRecords, validRecords);
    }
    
    private void processProducts(Path dirPath, Map<String, Object> result) throws Exception {
        Path productFile = findFile(dirPath, "olist_products_dataset.csv");
        if (productFile == null) {
            log.warn("Product dataset not found");
            return;
        }
        
        log.info("Processing products from: {}", productFile);
        DatasetAdapter<Product> adapter = DatasetLoaderFactory.createProductLoader(productFile);
        
        List<List<Product>> chunks = adapter.parseInChunks(CHUNK_SIZE);
        int totalRecords = 0;
        int validRecords = 0;
        int duplicatesRemoved = 0;
        Set<String> seenIds = new HashSet<>();
        
        for (List<Product> chunk : chunks) {
            totalRecords += chunk.size();
            List<Product> validChunk = new ArrayList<>();
            
            for (Product product : chunk) {
                if (isValidProduct(product) && !seenIds.contains(product.getProductId())) {
                    validChunk.add(product);
                    seenIds.add(product.getProductId());
                    validRecords++;
                } else if (seenIds.contains(product.getProductId())) {
                    duplicatesRemoved++;
                }
            }
            
            if (!validChunk.isEmpty()) {
                productRepository.saveAll(validChunk);
            }
        }
        
        result.put("products_total", totalRecords);
        result.put("products_valid", validRecords);
        result.put("products_duplicates", duplicatesRemoved);
        log.info("Products processed: {} total, {} valid, {} duplicates", totalRecords, validRecords, duplicatesRemoved);
    }
    
    private void processSellers(Path dirPath, Map<String, Object> result) throws Exception {
        Path sellerFile = findFile(dirPath, "olist_sellers_dataset.csv");
        if (sellerFile == null) {
            log.warn("Seller dataset not found");
            return;
        }
        
        log.info("Processing sellers from: {}", sellerFile);
        DatasetAdapter<Seller> adapter = DatasetLoaderFactory.createSellerLoader(sellerFile);
        
        List<List<Seller>> chunks = adapter.parseInChunks(CHUNK_SIZE);
        int totalRecords = 0;
        int validRecords = 0;
        int duplicatesRemoved = 0;
        Set<String> seenIds = new HashSet<>();
        
        for (List<Seller> chunk : chunks) {
            totalRecords += chunk.size();
            List<Seller> validChunk = new ArrayList<>();
            
            for (Seller seller : chunk) {
                if (isValidSeller(seller) && !seenIds.contains(seller.getSellerId())) {
                    validChunk.add(seller);
                    seenIds.add(seller.getSellerId());
                    validRecords++;
                } else if (seenIds.contains(seller.getSellerId())) {
                    duplicatesRemoved++;
                }
            }
            
            if (!validChunk.isEmpty()) {
                sellerRepository.saveAll(validChunk);
            }
        }
        
        result.put("sellers_total", totalRecords);
        result.put("sellers_valid", validRecords);
        result.put("sellers_duplicates", duplicatesRemoved);
        log.info("Sellers processed: {} total, {} valid, {} duplicates", totalRecords, validRecords, duplicatesRemoved);
    }
    
    private void generateDeliveryMetrics() {
        log.info("Generating delivery metrics...");
        List<Order> orders = orderRepository.findAll();
        List<Delivery> deliveries = new ArrayList<>();
        
        for (Order order : orders) {
            if (order.getOrderStatus().equals("delivered") && 
                order.getOrderDeliveredCustomerDate() != null) {
                
                Delivery delivery = new Delivery();
                delivery.setOrderId(order.getOrderId());
                delivery.setCustomerId(order.getCustomerId());
                delivery.setOrderStatus(order.getOrderStatus());
                delivery.setPurchaseTimestamp(order.getOrderPurchaseTimestamp());
                delivery.setDeliveredTimestamp(order.getOrderDeliveredCustomerDate());
                delivery.setEstimatedDelivery(order.getOrderEstimatedDeliveryDate());
                
                // Calculate transit time
                if (order.getOrderPurchaseTimestamp() != null && order.getOrderDeliveredCustomerDate() != null) {
                    long transitTime = ChronoUnit.HOURS.between(
                        order.getOrderPurchaseTimestamp(), 
                        order.getOrderDeliveredCustomerDate()
                    );
                    delivery.setTransitTimeHours(transitTime);
                }
                
                // Calculate delay
                if (order.getOrderEstimatedDeliveryDate() != null && order.getOrderDeliveredCustomerDate() != null) {
                    long delay = ChronoUnit.HOURS.between(
                        order.getOrderEstimatedDeliveryDate(),
                        order.getOrderDeliveredCustomerDate()
                    );
                    delivery.setDelayHours(delay);
                    delivery.setIsDelayed(delay > 0);
                } else {
                    delivery.setDelayHours(0L);
                    delivery.setIsDelayed(false);
                }
                
                deliveries.add(delivery);
            }
        }
        
        deliveryRepository.saveAll(deliveries);
        log.info("Delivery metrics generated: {} records", deliveries.size());
    }
    
    // Validation methods
    private boolean isValidCustomer(Customer customer) {
        return customer != null && 
               customer.getCustomerId() != null && 
               !customer.getCustomerId().trim().isEmpty();
    }
    
    private boolean isValidOrder(Order order) {
        return order != null && 
               order.getOrderId() != null && 
               !order.getOrderId().trim().isEmpty();
    }
    
    private boolean isValidOrderItem(OrderItem item) {
        return item != null && 
               item.getOrderId() != null && 
               !item.getOrderId().trim().isEmpty();
    }
    
    private boolean isValidPayment(Payment payment) {
        return payment != null && 
               payment.getOrderId() != null && 
               !payment.getOrderId().trim().isEmpty() &&
               payment.getPaymentValue() != null &&
               payment.getPaymentValue() >= 0;
    }
    
    private boolean isValidProduct(Product product) {
        return product != null && 
               product.getProductId() != null && 
               !product.getProductId().trim().isEmpty();
    }
    
    private boolean isValidSeller(Seller seller) {
        return seller != null && 
               seller.getSellerId() != null && 
               !seller.getSellerId().trim().isEmpty();
    }
    
    // Helper method to find files
    private Path findFile(Path directory, String fileName) {
        try (Stream<Path> paths = Files.walk(directory, 1)) {
            return paths
                .filter(Files::isRegularFile)
                .filter(p -> p.getFileName().toString().equalsIgnoreCase(fileName))
                .findFirst()
                .orElse(null);
        } catch (Exception e) {
            log.error("Error finding file: {}", fileName, e);
            return null;
        }
    }
    
    public Map<String, Object> getIngestionStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total_customers", customerRepository.count());
        stats.put("total_orders", orderRepository.count());
        stats.put("total_order_items", orderItemRepository.count());
        stats.put("total_payments", paymentRepository.count());
        stats.put("total_products", productRepository.count());
        stats.put("total_sellers", sellerRepository.count());
        stats.put("total_deliveries", deliveryRepository.count());
        stats.put("faulty_records", faultyRecordRepository.count());
        return stats;
    }
}
