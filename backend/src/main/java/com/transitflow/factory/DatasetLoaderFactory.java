package com.transitflow.factory;

import com.transitflow.adapter.CsvDatasetAdapter;
import com.transitflow.adapter.DatasetAdapter;
import com.transitflow.model.*;
import org.apache.commons.csv.CSVRecord;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

/**
 * Factory Pattern: Instantiates the correct DatasetAdapter based on file extension
 */
public class DatasetLoaderFactory {
    
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public static DatasetAdapter<Customer> createCustomerLoader(Path filePath) {
        String fileName = filePath.getFileName().toString().toLowerCase();
        
        if (fileName.endsWith(".csv")) {
            return new CsvDatasetAdapter<>(filePath, customerMapper());
        }
        throw new UnsupportedOperationException("File type not supported: " + fileName);
    }
    
    public static DatasetAdapter<Order> createOrderLoader(Path filePath) {
        if (filePath.getFileName().toString().toLowerCase().endsWith(".csv")) {
            return new CsvDatasetAdapter<>(filePath, orderMapper());
        }
        throw new UnsupportedOperationException("File type not supported");
    }
    
    public static DatasetAdapter<OrderItem> createOrderItemLoader(Path filePath) {
        if (filePath.getFileName().toString().toLowerCase().endsWith(".csv")) {
            return new CsvDatasetAdapter<>(filePath, orderItemMapper());
        }
        throw new UnsupportedOperationException("File type not supported");
    }
    
    public static DatasetAdapter<Payment> createPaymentLoader(Path filePath) {
        if (filePath.getFileName().toString().toLowerCase().endsWith(".csv")) {
            return new CsvDatasetAdapter<>(filePath, paymentMapper());
        }
        throw new UnsupportedOperationException("File type not supported");
    }
    
    public static DatasetAdapter<Product> createProductLoader(Path filePath) {
        if (filePath.getFileName().toString().toLowerCase().endsWith(".csv")) {
            return new CsvDatasetAdapter<>(filePath, productMapper());
        }
        throw new UnsupportedOperationException("File type not supported");
    }
    
    public static DatasetAdapter<Seller> createSellerLoader(Path filePath) {
        if (filePath.getFileName().toString().toLowerCase().endsWith(".csv")) {
            return new CsvDatasetAdapter<>(filePath, sellerMapper());
        }
        throw new UnsupportedOperationException("File type not supported");
    }
    
    // Mapper functions
    private static Function<CSVRecord, Customer> customerMapper() {
        return record -> {
            try {
                Customer customer = new Customer();
                customer.setCustomerId(record.get("customer_id"));
                customer.setCustomerUniqueId(record.get("customer_unique_id"));
                customer.setCustomerZipCodePrefix(record.get("customer_zip_code_prefix"));
                customer.setCustomerCity(record.get("customer_city"));
                customer.setCustomerState(record.get("customer_state"));
                return customer;
            } catch (Exception e) {
                return null;
            }
        };
    }
    
    private static Function<CSVRecord, Order> orderMapper() {
        return record -> {
            try {
                Order order = new Order();
                order.setOrderId(record.get("order_id"));
                order.setCustomerId(record.get("customer_id"));
                order.setOrderStatus(record.get("order_status"));
                order.setOrderPurchaseTimestamp(parseTimestamp(record.get("order_purchase_timestamp")));
                order.setOrderApprovedAt(parseTimestamp(record.get("order_approved_at")));
                order.setOrderDeliveredCarrierDate(parseTimestamp(record.get("order_delivered_carrier_date")));
                order.setOrderDeliveredCustomerDate(parseTimestamp(record.get("order_delivered_customer_date")));
                order.setOrderEstimatedDeliveryDate(parseTimestamp(record.get("order_estimated_delivery_date")));
                return order;
            } catch (Exception e) {
                return null;
            }
        };
    }
    
    private static Function<CSVRecord, OrderItem> orderItemMapper() {
        return record -> {
            try {
                OrderItem item = new OrderItem();
                item.setOrderId(record.get("order_id"));
                item.setOrderItemId(parseInteger(record.get("order_item_id")));
                item.setProductId(record.get("product_id"));
                item.setSellerId(record.get("seller_id"));
                item.setShippingLimitDate(parseTimestamp(record.get("shipping_limit_date")));
                item.setPrice(parseDouble(record.get("price")));
                item.setFreightValue(parseDouble(record.get("freight_value")));
                return item;
            } catch (Exception e) {
                return null;
            }
        };
    }
    
    private static Function<CSVRecord, Payment> paymentMapper() {
        return record -> {
            try {
                Payment payment = new Payment();
                payment.setOrderId(record.get("order_id"));
                payment.setPaymentSequential(parseInteger(record.get("payment_sequential")));
                payment.setPaymentType(record.get("payment_type"));
                payment.setPaymentInstallments(parseInteger(record.get("payment_installments")));
                payment.setPaymentValue(parseDouble(record.get("payment_value")));
                return payment;
            } catch (Exception e) {
                return null;
            }
        };
    }
    
    private static Function<CSVRecord, Product> productMapper() {
        return record -> {
            try {
                Product product = new Product();
                product.setProductId(record.get("product_id"));
                product.setProductCategoryName(record.get("product_category_name"));
                product.setProductNameLength(parseInteger(record.get("product_name_lenght")));
                product.setProductDescriptionLength(parseInteger(record.get("product_description_lenght")));
                product.setProductPhotosQty(parseInteger(record.get("product_photos_qty")));
                product.setProductWeightG(parseInteger(record.get("product_weight_g")));
                product.setProductLengthCm(parseInteger(record.get("product_length_cm")));
                product.setProductHeightCm(parseInteger(record.get("product_height_cm")));
                product.setProductWidthCm(parseInteger(record.get("product_width_cm")));
                return product;
            } catch (Exception e) {
                return null;
            }
        };
    }
    
    private static Function<CSVRecord, Seller> sellerMapper() {
        return record -> {
            try {
                Seller seller = new Seller();
                seller.setSellerId(record.get("seller_id"));
                seller.setSellerZipCodePrefix(record.get("seller_zip_code_prefix"));
                seller.setSellerCity(record.get("seller_city"));
                seller.setSellerState(record.get("seller_state"));
                return seller;
            } catch (Exception e) {
                return null;
            }
        };
    }
    
    // Helper methods
    private static LocalDateTime parseTimestamp(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(value, TIMESTAMP_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }
    
    private static Integer parseInteger(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    private static Double parseDouble(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
