# TransitFlow Analytics System - UML Diagrams

This document contains all PlantUML diagrams for the TransitFlow Analytics System.
Each diagram supports specific assignment requirements (P3, P4, M2).

## 📋 How to Use These Diagrams

1. **Copy the PlantUML code** from any section below
2. **Paste into PlantUML editor**: 
   - Online: https://www.plantuml.com/plantuml/uml/
   - VS Code: Install "PlantUML" extension
   - IntelliJ: Install "PlantUML integration" plugin
3. **Generate diagram** - The tool will render it automatically

## 📑 Table of Contents

1. [Use Case Diagram](#1-use-case-diagram) - ✅ Functional Requirements
2. [MVC Architecture Diagram](#2-mvc-architecture-diagram) - ✅ P3 Architecture
3. [Class Diagram](#3-class-diagram) - ✅ OOP Concepts, SOLID
4. [Factory Pattern Diagram](#4-factory-pattern-diagram) - ✅ M2 Design Patterns
5. [Strategy Pattern Diagram](#5-strategy-pattern-diagram) - ✅ M2 Design Patterns
6. [Adapter Pattern Diagram](#6-adapter-pattern-diagram) - ✅ M2 Design Patterns
7. [Facade Pattern Diagram](#7-facade-pattern-diagram) - ✅ M2 Design Patterns
8. [Activity Diagram](#8-activity-diagram-dataset-upload) - ✅ P3 Workflows
9. [Sequence Diagram - Dataset Upload](#9-sequence-diagram---dataset-upload) - ✅ P3
10. [Sequence Diagram - Analytics](#10-sequence-diagram---analytics-workflow) - ✅ P3
11. [Entity Relationship Diagram](#11-entity-relationship-diagram-erd) - ✅ Data Model
12. [Testing Architecture Diagram](#12-testing-architecture-diagram) - ✅ P4
13. [Automated Testing Workflow](#13-automated-testing-workflow-diagram) - ✅ P4
14. [Component Diagram](#14-component-diagram) - ⭐ Distinction Level
15. [Deployment Diagram](#15-deployment-diagram) - ⭐ Distinction Level

---

## 1. Use Case Diagram

**Purpose**: Shows how users interact with the system

**Assignment Mapping**: ✅ Functional Requirements, ✅ System Scope, ✅ User Interactions

```plantuml
@startuml UseCase
left to right direction
skinparam packageStyle rectangle

actor "Administrator" as Admin
actor "Data Analyst" as Analyst

rectangle "TransitFlow Analytics System" {
  usecase "Upload Dataset" as UC1
  usecase "Validate Dataset" as UC2
  usecase "Clean Data" as UC3
  usecase "Process Analytics" as UC4
  usecase "View Dashboard" as UC5
  usecase "Generate Reports" as UC6
  usecase "Download Reports" as UC7
  usecase "View DLQ Records" as UC8
  usecase "Detect Anomalies" as UC9
  usecase "Track Performance" as UC10
}

Admin --> UC1
Admin --> UC2
Admin --> UC3
Admin --> UC8

Analyst --> UC4
Analyst --> UC5
Analyst --> UC6
Analyst --> UC7
Analyst --> UC9
Analyst --> UC10

UC1 ..> UC2 : <<include>>
UC2 ..> UC3 : <<include>>
UC3 ..> UC4 : <<extend>>

note right of UC8
  Dead-Letter Queue
  for faulty records
end note

@enduml
```

---

## 2. MVC Architecture Diagram

**Purpose**: Shows overall system architecture

**Assignment Mapping**: ✅ P3 Architecture Design, ✅ Clean Code Design

```plantuml
@startuml MVCArchitecture
!define RECTANGLE rectangle

skinparam {
  RectangleBorderColor #00D9FF
  RectangleBackgroundColor #1E2530
  RectangleFontColor #FFFFFF
}

package "Presentation Layer" {
  [Streamlit Dashboard] as Frontend
}

package "Controller Layer" {
  [DatasetController] as DC
  [AnalyticsController] as AC
  [RootController] as RC
}

package "Facade Layer" {
  [TransitFlowFacade] as Facade
}

package "Service Layer" {
  [DatasetService] as DS
  [AnalyticsStrategies] as AS
}

package "Repository Layer" {
  [CustomerRepository] as CR
  [OrderRepository] as OR
  [PaymentRepository] as PR
  [DeliveryRepository] as DR
}

package "Database Layer" {
  database "H2 In-Memory\nDatabase" as DB
}

Frontend -down-> DC : HTTP REST API
Frontend -down-> AC : HTTP REST API
DC -down-> Facade
AC -down-> Facade
Facade -down-> DS
Facade -down-> AS
DS -down-> CR
DS -down-> OR
AS -down-> PR
AS -down-> DR
CR -down-> DB
OR -down-> DB
PR -down-> DB
DR -down-> DB

note right of Facade
  Facade Pattern:
  Simplifies complex
  subsystem interactions
end note

note right of AS
  Strategy Pattern:
  Interchangeable
  analytics algorithms
end note

@enduml
```

---

## 3. Complete Layer-by-Layer Class Diagram

**Purpose**: Shows comprehensive OOP Design across all system layers from bottom (Database) to top (Controllers)

**Assignment Mapping**: ✅ OOP Concepts, ✅ SOLID Principles, ✅ Low Level Design, ✅ Design Patterns

```plantuml
@startuml CompleteLayeredClassDiagram
!theme blueprint
scale 0.9

title TransitFlow Analytics System - Complete Layer-by-Layer Architecture\n(Bottom to Top: Database → Model → Repository → Service/Strategy → Facade → Controller)

' ========================================
' LAYER 1: DATABASE LAYER (BOTTOM)
' ========================================
package "LAYER 1: DATABASE" #1a1f2e {
  database "H2 In-Memory Database" as DB {
    storage "customers table" as tCustomers
    storage "orders table" as tOrders
    storage "order_items table" as tOrderItems
    storage "payments table" as tPayments
    storage "products table" as tProducts
    storage "sellers table" as tSellers
    storage "deliveries table" as tDeliveries
    storage "faulty_records table" as tFaulty
  }
}

' ========================================
' LAYER 2: MODEL LAYER (ENTITY/DOMAIN)
' ========================================
package "LAYER 2: MODEL (ENTITY/DOMAIN)" #1E2530 {
  
  class Customer {
    ' @Entity, @Table, @Data
    -String customerId
    -String customerUniqueId
    -String customerZipCodePrefix
    -String customerCity
    -String customerState
    +getCustomerId(): String
    +setCustomerId(String): void
    +getCustomerState(): String
  }
  
  class Order {
    ' @Entity, @Table, @Data
    -String orderId
    -String customerId
    -String orderStatus
    -LocalDateTime orderPurchaseTimestamp
    -LocalDateTime orderApprovedAt
    -LocalDateTime orderDeliveredCarrierDate
    -LocalDateTime orderDeliveredCustomerDate
    -LocalDateTime orderEstimatedDeliveryDate
    +getOrderId(): String
    +getCustomerId(): String
    +getOrderPurchaseTimestamp(): LocalDateTime
  }
  
  class OrderItem {
    ' @Entity, @Table, @Data
    -Long id
    -String orderId
    -String productId
    -String sellerId
    -Integer orderItemId
    -LocalDateTime shippingLimitDate
    -Double price
    -Double freightValue
    +getOrderId(): String
    +getPrice(): Double
  }
  
  class Payment {
    ' @Entity, @Table, @Data
    -Long id
    -String orderId
    -Integer paymentSequential
    -String paymentType
    -Integer paymentInstallments
    -Double paymentValue
    +getPaymentValue(): Double
    +getPaymentType(): String
  }
  
  class Product {
    ' @Entity, @Table, @Data
    -String productId
    -String productCategoryName
    -Integer productNameLength
    -Integer productDescriptionLength
    -Integer productPhotosQty
    -Integer productWeightG
    -Integer productLengthCm
    -Integer productHeightCm
    -Integer productWidthCm
    +getProductId(): String
    +getProductCategoryName(): String
  }
  
  class Seller {
    ' @Entity, @Table, @Data
    -String sellerId
    -String sellerZipCodePrefix
    -String sellerCity
    -String sellerState
    +getSellerId(): String
    +getSellerState(): String
  }
  
  class Delivery {
    ' @Entity, @Table, @Data
    -String orderId
    -String customerId
    -String orderStatus
    -LocalDateTime purchaseTimestamp
    -LocalDateTime deliveredTimestamp
    -LocalDateTime estimatedDelivery
    -Long transitTimeHours
    -Long delayHours
    -Boolean isDelayed
    +getTransitTimeHours(): Long
    +getIsDelayed(): Boolean
  }
  
  class FaultyRecord {
    ' @Entity, @Table, @Data
    -Long id
    -String sourceFile
    -String rawContent
    -String failureReason
    -LocalDateTime timestamp
    +getSourceFile(): String
  }
}

' ========================================
' LAYER 3: REPOSITORY LAYER (DATA ACCESS)
' ========================================
package "LAYER 3: REPOSITORY (DATA ACCESS)" #252d3d {
  
  interface CustomerRepository {
    {abstract} +findAll(): List<Customer>
    {abstract} +save(Customer): Customer
    {abstract} +count(): Long
    {abstract} +findByCustomerState(String): List<Customer>
    {abstract} +countByState(): List<Object[]>
    {abstract} +countByCity(): List<Object[]>
  }
  
  interface OrderRepository {
    {abstract} +findAll(): List<Order>
    {abstract} +save(Order): Order
    {abstract} +findByOrderStatus(String): List<Order>
    {abstract} +findByOrderPurchaseTimestampBetween(): List<Order>
    {abstract} +countByStatus(): List<Object[]>
    {abstract} +countByCustomerId(String): Long
    {abstract} +countOrdersByCustomer(): List<Object[]>
  }
  
  interface OrderItemRepository {
    {abstract} +findAll(): List<OrderItem>
    {abstract} +findByOrderId(String): List<OrderItem>
    {abstract} +getTopProducts(): List<Object[]>
  }
  
  interface PaymentRepository {
    {abstract} +findAll(): List<Payment>
    {abstract} +findByOrderId(String): List<Payment>
    {abstract} +aggregateByPaymentType(): List<Object[]>
    {abstract} +sumByOrderId(String): Double
    {abstract} +sumAllPayments(): Double
    {abstract} +sumRevenueByMonth(): List<Object[]>
  }
  
  interface ProductRepository {
    {abstract} +findAll(): List<Product>
    {abstract} +countByCategory(): List<Object[]>
  }
  
  interface SellerRepository {
    {abstract} +findAll(): List<Seller>
    {abstract} +findBySellerState(String): List<Seller>
    {abstract} +countByState(): List<Object[]>
  }
  
  interface DeliveryRepository {
    {abstract} +findAll(): List<Delivery>
    {abstract} +findByIsDelayed(Boolean): List<Delivery>
    {abstract} +getAverageTransitTime(): Double
    {abstract} +findDelayedDeliveries(): List<Delivery>
  }
  
  interface FaultyRecordRepository {
    {abstract} +findAll(): List<FaultyRecord>
    {abstract} +findBySourceFile(String): List<FaultyRecord>
    {abstract} +countBySourceFile(): List<Object[]>
  }
}

' ========================================
' LAYER 4A: ADAPTER & FACTORY LAYER
' ========================================
package "LAYER 4A: ADAPTER & FACTORY (DATA INGESTION)" #2d3548 {
  
  interface DatasetAdapter<T> {
    {abstract} +parseInChunks(int): List<List<T>>
    {abstract} +getTotalRecords(): long
  }
  
  class CsvDatasetAdapter<T> implements DatasetAdapter {
    -Path filePath
    -Function<CSVRecord,T> mapper
    -String sourceIdentifier
    +parseInChunks(int): List<List<T>>
    +getTotalRecords(): long
  }
  
  class DatasetLoaderFactory {
    {static} +createCustomerLoader(Path): DatasetAdapter<Customer>
    {static} +createOrderLoader(Path): DatasetAdapter<Order>
    {static} +createOrderItemLoader(Path): DatasetAdapter<OrderItem>
    {static} +createPaymentLoader(Path): DatasetAdapter<Payment>
    {static} +createProductLoader(Path): DatasetAdapter<Product>
    {static} +createSellerLoader(Path): DatasetAdapter<Seller>
    {static} -customerMapper(): Function<CSVRecord,Customer>
    {static} -orderMapper(): Function<CSVRecord,Order>
    {static} -parseTimestamp(String): LocalDateTime
    {static} -parseInteger(String): Integer
    {static} -parseDouble(String): Double
  }
}

' ========================================
' LAYER 4B: SERVICE LAYER
' ========================================
package "LAYER 4B: SERVICE (BUSINESS LOGIC)" #2d3548 {
  
  class DatasetService {
    {static} -int CHUNK_SIZE = 5000
    -CustomerRepository customerRepository
    -OrderRepository orderRepository
    -OrderItemRepository orderItemRepository
    -PaymentRepository paymentRepository
    -ProductRepository productRepository
    -SellerRepository sellerRepository
    -DeliveryRepository deliveryRepository
    -FaultyRecordRepository faultyRecordRepository
    +ingestFromDirectory(String): Map<String,Object>
    +getIngestionStats(): Map<String,Object>
    -processCustomers(Path, Map): void
    -processOrders(Path, Map): void
    -processOrderItems(Path, Map): void
    -processPayments(Path, Map): void
    -processProducts(Path, Map): void
    -processSellers(Path, Map): void
    -generateDeliveryMetrics(): void
    -findFile(Path, String): Path
    -isValidCustomer(Customer): boolean
    -isValidOrder(Order): boolean
  }
}

' ========================================
' LAYER 4C: STRATEGY LAYER
' ========================================
package "LAYER 4C: STRATEGY (ANALYTICS ALGORITHMS)" #2d3548 {
  
  interface AnalyticsStrategy {
    {abstract} +execute(): Map<String,Object>
    {abstract} +getStrategyName(): String
  }
  
  class PeakDeliveryStrategy implements AnalyticsStrategy {
    -OrderRepository orderRepository
    +execute(): Map<String,Object>
    +getStrategyName(): String
    -analyzeHourlyPeaks(): Map
    -analyzeDailyPeaks(): Map
    -analyzeDayOfWeek(): Map
  }
  
  class CustomerSegmentationStrategy implements AnalyticsStrategy {
    -CustomerRepository customerRepository
    -OrderRepository orderRepository
    -PaymentRepository paymentRepository
    +execute(): Map<String,Object>
    +getStrategyName(): String
    -segmentByGeography(): Map
    -segmentByFrequency(): Map
  }
  
  class DeliveryEfficiencyStrategy implements AnalyticsStrategy {
    -DeliveryRepository deliveryRepository
    +execute(): Map<String,Object>
    +getStrategyName(): String
    -calculateAverageTransitTime(): Double
    -calculateDelayRate(): Double
  }
  
  class RevenueAnalysisStrategy implements AnalyticsStrategy {
    -PaymentRepository paymentRepository
    -OrderRepository orderRepository
    +execute(): Map<String,Object>
    +getStrategyName(): String
    -calculateTotalRevenue(): Double
    -analyzeMonthlyTrends(): List
    -analyzePaymentTypes(): Map
  }
  
  class AnomalyDetectionStrategy implements AnalyticsStrategy {
    -PaymentRepository paymentRepository
    -DeliveryRepository deliveryRepository
    +execute(): Map<String,Object>
    +getStrategyName(): String
    -detectPriceAnomalies(): List
    -detectDelayAnomalies(): List
    -calculateStdDev(List, double): double
  }
  
  class SeasonalDemandStrategy implements AnalyticsStrategy {
    -OrderRepository orderRepository
    +execute(): Map<String,Object>
    +getStrategyName(): String
    -calculateMonthlyOrders(): Map
    -calculateMonthlyGrowth(): Map
    -analyzeHolidayPeriods(): Map
    -analyzeSeasonalPatterns(): Map
    -analyzeQuarterlyTrends(): Map
  }
  
  class RegionalPerformanceStrategy implements AnalyticsStrategy {
    -CustomerRepository customerRepository
    -OrderRepository orderRepository
    -DeliveryRepository deliveryRepository
    -PaymentRepository paymentRepository
    -SellerRepository sellerRepository
    +execute(): Map<String,Object>
    +getStrategyName(): String
    -calculateCustomersByRegion(): Map
    -calculateOrdersByRegion(): Map
    -calculateRevenueByRegion(): Map
    -calculateDeliveryPerformance(): Map
    -generateRegionalRankings(): List
  }
}

' ========================================
' LAYER 5: FACADE LAYER
' ========================================
package "LAYER 5: FACADE (UNIFIED INTERFACE)" #3d4758 {
  
  class TransitFlowFacade {
    -DatasetService datasetService
    -PeakDeliveryStrategy peakDeliveryStrategy
    -CustomerSegmentationStrategy customerSegmentationStrategy
    -DeliveryEfficiencyStrategy deliveryEfficiencyStrategy
    -RevenueAnalysisStrategy revenueAnalysisStrategy
    -AnomalyDetectionStrategy anomalyDetectionStrategy
    -SeasonalDemandStrategy seasonalDemandStrategy
    -RegionalPerformanceStrategy regionalPerformanceStrategy
    +ingestDatasets(String): Map<String,Object>
    +getIngestionStats(): Map<String,Object>
    +executeAnalysis(String): Map<String,Object>
  }
}

' ========================================
' LAYER 6: CONTROLLER LAYER (TOP)
' ========================================
package "LAYER 6: CONTROLLER (REST API)" #4d5768 {
  
  class DatasetController {
    -TransitFlowFacade facade
    +ingestDataset(Map): ResponseEntity<Map>
    +getStats(): ResponseEntity<Map>
  }
  
  class AnalyticsController {
    -TransitFlowFacade facade
    +getPeakDelivery(): ResponseEntity<Map>
    +getCustomerSegmentation(): ResponseEntity<Map>
    +getDeliveryEfficiency(): ResponseEntity<Map>
    +getRevenueAnalysis(): ResponseEntity<Map>
    +getAnomalyDetection(): ResponseEntity<Map>
    +getSeasonalDemand(): ResponseEntity<Map>
    +getRegionalPerformance(): ResponseEntity<Map>
  }
  
  class RootController {
    +welcome(): String
  }
}

' ========================================
' LAYER 7: CONFIGURATION LAYER
' ========================================
package "LAYER 7: CONFIGURATION" #5d6778 {
  
  class WebConfig {
    +addCorsMappings(CorsRegistry): void
  }
}

' ========================================
' RELATIONSHIPS - LAYER BY LAYER
' ========================================

' Model → Database
Customer ..> tCustomers : maps to
Order ..> tOrders : maps to
OrderItem ..> tOrderItems : maps to
Payment ..> tPayments : maps to
Product ..> tProducts : maps to
Seller ..> tSellers : maps to
Delivery ..> tDeliveries : maps to
FaultyRecord ..> tFaulty : maps to

' Domain Model Relationships
Customer "1" --> "0..*" Order : places
Order "1" --> "1..*" OrderItem : contains
Order "1" --> "1..*" Payment : paid by
Order "1" --> "0..1" Delivery : tracked as
Product "1" --> "0..*" OrderItem : ordered as
Seller "1" --> "0..*" OrderItem : sells

' Repository → Model
CustomerRepository ..> Customer : manages
OrderRepository ..> Order : manages
OrderItemRepository ..> OrderItem : manages
PaymentRepository ..> Payment : manages
ProductRepository ..> Product : manages
SellerRepository ..> Seller : manages
DeliveryRepository ..> Delivery : manages
FaultyRecordRepository ..> FaultyRecord : manages

' Repository → Database
CustomerRepository ..> DB
OrderRepository ..> DB
OrderItemRepository ..> DB
PaymentRepository ..> DB
ProductRepository ..> DB
SellerRepository ..> DB
DeliveryRepository ..> DB
FaultyRecordRepository ..> DB

' Adapter/Factory → Model
DatasetAdapter ..> Customer : creates
DatasetAdapter ..> Order : creates
DatasetAdapter ..> Payment : creates
DatasetLoaderFactory ..> CsvDatasetAdapter : creates
DatasetLoaderFactory ..> Customer : maps to
DatasetLoaderFactory ..> Order : maps to
DatasetLoaderFactory ..> OrderItem : maps to
DatasetLoaderFactory ..> Payment : maps to
DatasetLoaderFactory ..> Product : maps to
DatasetLoaderFactory ..> Seller : maps to

' Service → Repository
DatasetService --> CustomerRepository : uses
DatasetService --> OrderRepository : uses
DatasetService --> OrderItemRepository : uses
DatasetService --> PaymentRepository : uses
DatasetService --> ProductRepository : uses
DatasetService --> SellerRepository : uses
DatasetService --> DeliveryRepository : uses
DatasetService --> FaultyRecordRepository : uses
DatasetService --> DatasetLoaderFactory : uses
DatasetService --> CsvDatasetAdapter : uses

' Strategy → Repository
PeakDeliveryStrategy --> OrderRepository : queries
CustomerSegmentationStrategy --> CustomerRepository : queries
CustomerSegmentationStrategy --> OrderRepository : queries
CustomerSegmentationStrategy --> PaymentRepository : queries
DeliveryEfficiencyStrategy --> DeliveryRepository : queries
RevenueAnalysisStrategy --> PaymentRepository : queries
RevenueAnalysisStrategy --> OrderRepository : queries
AnomalyDetectionStrategy --> PaymentRepository : queries
AnomalyDetectionStrategy --> DeliveryRepository : queries
SeasonalDemandStrategy --> OrderRepository : queries
RegionalPerformanceStrategy --> CustomerRepository : queries
RegionalPerformanceStrategy --> OrderRepository : queries
RegionalPerformanceStrategy --> DeliveryRepository : queries
RegionalPerformanceStrategy --> PaymentRepository : queries
RegionalPerformanceStrategy --> SellerRepository : queries

' Facade → Service & Strategy
TransitFlowFacade --> DatasetService : delegates
TransitFlowFacade --> PeakDeliveryStrategy : executes
TransitFlowFacade --> CustomerSegmentationStrategy : executes
TransitFlowFacade --> DeliveryEfficiencyStrategy : executes
TransitFlowFacade --> RevenueAnalysisStrategy : executes
TransitFlowFacade --> AnomalyDetectionStrategy : executes
TransitFlowFacade --> SeasonalDemandStrategy : executes
TransitFlowFacade --> RegionalPerformanceStrategy : executes

' Controller → Facade
DatasetController --> TransitFlowFacade : uses
AnalyticsController --> TransitFlowFacade : uses

' Design Pattern Annotations
note right of DatasetAdapter
  Adapter Pattern:
  Converts CSV format
  to entity objects
end note

note right of DatasetLoaderFactory
  Factory Pattern:
  Creates appropriate
  adapter instances
end note

note right of AnalyticsStrategy
  Strategy Pattern:
  Interchangeable
  analytics algorithms
end note

note right of TransitFlowFacade
  Facade Pattern:
  Simplifies complex
  subsystem interactions
end note

note right of CustomerRepository
  Repository Pattern:
  Abstracts data
  access layer
end note

@enduml
```

---

## 4. Factory Pattern Diagram

**Purpose**: Shows how Factory creates appropriate adapters based on file type

**Assignment Mapping**: ✅ M2 Design Pattern Evidence, ✅ SOLID Principles

```plantuml
@startuml FactoryPattern
!theme sketchy-outline

interface DatasetAdapter<T> {
  +parseInChunks(int): List<List<T>>
  +getTotalRecords(): long
  +getSourceIdentifier(): String
}

class CsvDatasetAdapter<T> implements DatasetAdapter {
  -Path filePath
  -Function<CSVRecord,T> mapper
  +parseInChunks(int): List<List<T>>
  +getTotalRecords(): long
  +getSourceIdentifier(): String
}

class JsonDatasetAdapter<T> implements DatasetAdapter {
  -Path filePath
  +parseInChunks(int): List<List<T>>
  +getTotalRecords(): long
  +getSourceIdentifier(): String
}

class ExcelDatasetAdapter<T> implements DatasetAdapter {
  -Path filePath
  +parseInChunks(int): List<List<T>>
  +getTotalRecords(): long
  +getSourceIdentifier(): String
}

class DatasetLoaderFactory {
  +createCustomerLoader(Path): DatasetAdapter<Customer>
  +createOrderLoader(Path): DatasetAdapter<Order>
  +createPaymentLoader(Path): DatasetAdapter<Payment>
  +createProductLoader(Path): DatasetAdapter<Product>
  +createSellerLoader(Path): DatasetAdapter<Seller>
}

DatasetLoaderFactory ..> DatasetAdapter : creates
DatasetLoaderFactory ..> CsvDatasetAdapter : creates
DatasetLoaderFactory ..> JsonDatasetAdapter : creates
DatasetLoaderFactory ..> ExcelDatasetAdapter : creates

note right of DatasetLoaderFactory
  Factory Pattern Benefits:
  1. Encapsulates object creation
  2. Easy to add new file formats
  3. Decouples client from concrete classes
  4. Follows Open-Closed Principle
end note

note bottom of CsvDatasetAdapter
  Currently Implemented:
  - CSV file parsing
  - Apache Commons CSV library
end note

note bottom of JsonDatasetAdapter
  Future Implementation:
  - JSON file parsing
  - Jackson library
end note

@enduml
```

---

## 5. Strategy Pattern Diagram

**Purpose**: Shows interchangeable analytics algorithms

**Assignment Mapping**: ✅ M2 Design Pattern Evidence, ✅ SOLID Principles

```plantuml
@startuml StrategyPattern
!theme toy

interface AnalyticsStrategy {
  +execute(): Map<String,Object>
  +getStrategyName(): String
}

class PeakDeliveryStrategy implements AnalyticsStrategy {
  -DeliveryRepository deliveryRepo
  +execute(): Map
  +getStrategyName(): String
  -analyzeHourlyPeaks(): List
  -analyzeDailyPeaks(): List
}

class CustomerSegmentationStrategy implements AnalyticsStrategy {
  -CustomerRepository customerRepo
  -OrderRepository orderRepo
  +execute(): Map
  +getStrategyName(): String
  -segmentByGeography(): Map
  -segmentByBehavior(): Map
}

class RevenueAnalysisStrategy implements AnalyticsStrategy {
  -PaymentRepository paymentRepo
  +execute(): Map
  +getStrategyName(): String
  -calculateTotalRevenue(): Double
  -analyzeMonthlyTrends(): List
  -analyzePaymentTypes(): Map
}

class DeliveryEfficiencyStrategy implements AnalyticsStrategy {
  -DeliveryRepository deliveryRepo
  +execute(): Map
  +getStrategyName(): String
  -calculateAverageTransitTime(): Double
  -calculateDelayRate(): Double
}

class AnomalyDetectionStrategy implements AnalyticsStrategy {
  -PaymentRepository paymentRepo
  +execute(): Map
  +getStrategyName(): String
  -detectOutliers(): List
  -calculateStatistics(): Map
}

class SeasonalDemandStrategy implements AnalyticsStrategy {
  -OrderRepository orderRepo
  +execute(): Map
  +getStrategyName(): String
  -analyzeMonthlyPatterns(): List
}

class RegionalPerformanceStrategy implements AnalyticsStrategy {
  -CustomerRepository customerRepo
  +execute(): Map
  +getStrategyName(): String
  -analyzeByState(): List
  -analyzeByCity(): List
}



class TransitFlowFacade {
  -PeakDeliveryStrategy peakStrategy
  -CustomerSegmentationStrategy customerStrategy
  -RevenueAnalysisStrategy revenueStrategy
  +executeAnalysis(String): Map
}

TransitFlowFacade --> AnalyticsStrategy : uses

note right of TransitFlowFacade
  Context:
  Selects and executes
  appropriate strategy
  based on request
end note

note bottom of AnalyticsStrategy
  Strategy Pattern Benefits:
  1. Interchangeable algorithms
  2. Easy to add new analytics
  3. Each strategy is independent
  4. Follows Open-Closed Principle
  5. Testable in isolation
end note

@enduml
```

---

## 6. Adapter Pattern Diagram

**Purpose**: Converts different file formats into standardized entity objects

**Assignment Mapping**: ✅ M2 Design Pattern Evidence

```plantuml
@startuml AdapterPattern
!theme vibrant

interface DatasetAdapter<T> {
  +parseInChunks(int chunkSize): List<List<T>>
  +getTotalRecords(): long
  +getSourceIdentifier(): String
}

class CsvDatasetAdapter<T> implements DatasetAdapter {
  -Path filePath
  -Function<CSVRecord,T> mapper
  +parseInChunks(int): List<List<T>>
  +getTotalRecords(): long
  +getSourceIdentifier(): String
  -readCsvFile(): List<CSVRecord>
}

class DatasetService {
  -CustomerRepository customerRepo
  -OrderRepository orderRepo
  +ingestFromDirectory(String): Map
  +processDataset(DatasetAdapter): void
}

class "Apache Commons CSV" as CSV {
  +CSVFormat
  +CSVParser
  +CSVRecord
}

class "Entity Classes" as Entity {
  Customer
  Order
  Payment
  Product
  Seller
}

DatasetService --> DatasetAdapter : uses
CsvDatasetAdapter --> CSV : adapts
CsvDatasetAdapter ..> Entity : creates

note right of CsvDatasetAdapter
  Adapter Pattern:
  Converts CSV records to
  standardized entity objects
end note

note bottom of DatasetAdapter
  Benefits:
  1. Decouples file format from business logic
  2. Easy to add JSON, Excel adapters
  3. Client code doesn't change
  4. Each adapter handles its format
end note

@enduml
```

---

## 7. Facade Pattern Diagram

**Purpose**: Simplifies complex subsystem interactions for controllers

**Assignment Mapping**: ✅ M2 Design Pattern Evidence

```plantuml
@startuml FacadePattern
!theme cerulean

package "Controller Layer (Client)" {
  class DatasetController {
    +ingestDatasets()
    +getStats()
  }
  
  class AnalyticsController {
    +executeAnalysis()
  }
}

package "Facade" {
  class TransitFlowFacade {
    -DatasetService datasetService
    -PeakDeliveryStrategy peakStrategy
    -CustomerSegmentationStrategy customerStrategy
    -RevenueAnalysisStrategy revenueStrategy
    -DeliveryEfficiencyStrategy deliveryStrategy
    -AnomalyDetectionStrategy anomalyStrategy
    -SeasonalDemandStrategy seasonalStrategy
    -RegionalPerformanceStrategy regionalStrategy
    +ingestDatasets(String): Map
    +executeAnalysis(String): Map
    +getIngestionStats(): Map
  }
}

package "Complex Subsystems" {
  class DatasetService {
    +ingestFromDirectory()
    +processChunk()
    +validateData()
  }
  
  class PeakDeliveryStrategy {
    +execute()
  }
  
  class CustomerSegmentationStrategy {
    +execute()
  }
  
  class RevenueAnalysisStrategy {
    +execute()
  }
  
  class "7 Strategy Classes" as Strategies
}

DatasetController --> TransitFlowFacade : uses
AnalyticsController --> TransitFlowFacade : uses

TransitFlowFacade --> DatasetService
TransitFlowFacade --> PeakDeliveryStrategy
TransitFlowFacade --> CustomerSegmentationStrategy
TransitFlowFacade --> RevenueAnalysisStrategy
TransitFlowFacade --> Strategies

note right of TransitFlowFacade
  Facade Pattern Benefits:
  1. Simplified interface
  2. Controllers don't know subsystems
  3. Easy to add caching/logging here
  4. Single point for cross-cutting concerns
  5. Reduces dependencies
end note

@enduml
```

---

## 8. Activity Diagram (Dataset Upload Process)

**Purpose**: Shows workflow for dataset ingestion process

**Assignment Mapping**: ✅ P3 Process Flow

```plantuml
@startuml ActivityDiagram
!theme plain

start

:User Opens Dataset Upload Page;

:Browse and Select Directory;

if (Directory Path Valid?) then (yes)
  :Display Available CSV Files;
  :User Clicks "Ingest Dataset";
  :Frontend Sends POST Request;
  
  partition Backend {
    :DatasetController Receives Request;
    :TransitFlowFacade Delegates to DatasetService;
    
    fork
      :Load customers.csv;
      :Parse in Chunks (5000 records);
      :Validate & Clean Data;
      :Save to Customer Table;
    fork again
      :Load orders.csv;
      :Parse in Chunks (5000 records);
      :Validate & Clean Data;
      :Save to Order Table;
    fork again
      :Load payments.csv;
      :Parse in Chunks (5000 records);
      :Validate & Clean Data;
      :Save to Payment Table;
    end fork
    
    :Generate Delivery Metrics;
    :Calculate Transit Times;
    :Identify Delayed Deliveries;
    
    if (Any Invalid Records?) then (yes)
      :Store in FaultyRecord Table (DLQ);
    else (no)
    endif
    
    :Return Ingestion Statistics;
  }
  
  :Display Success Message;
  :Show Records Count;
  
else (no)
  :Display Error Message;
  :Prompt User to Select Valid Path;
endif

stop

@enduml
```

---

## 9. Sequence Diagram - Dataset Upload

**Purpose**: Shows detailed interaction between components during dataset ingestion

**Assignment Mapping**: ✅ P3 Component Interaction

```plantuml
@startuml SequenceUpload
!theme blueprint

actor User
participant "Streamlit\nDashboard" as Frontend
participant "DatasetController" as Controller
participant "TransitFlowFacade" as Facade
participant "DatasetService" as Service
participant "DatasetLoaderFactory" as Factory
participant "CsvDatasetAdapter" as Adapter
participant "CustomerRepository" as Repo
database "H2 Database" as DB

User -> Frontend: Select Directory\nand Click Upload
activate Frontend

Frontend -> Controller: POST /api/dataset/ingest\n{directoryPath: "C:/Data"}
activate Controller

Controller -> Facade: ingestDatasets(directoryPath)
activate Facade

Facade -> Service: ingestFromDirectory(directoryPath)
activate Service

Service -> Factory: createCustomerLoader(filePath)
activate Factory
Factory -> Adapter: new CsvDatasetAdapter()
Factory --> Service: DatasetAdapter<Customer>
deactivate Factory

Service -> Adapter: parseInChunks(5000)
activate Adapter
Adapter --> Service: List<List<Customer>>
deactivate Adapter

loop For Each Chunk
  Service -> Service: removeDuplicates(chunk)
  Service -> Service: validateData(chunk)
  Service -> Repo: saveAll(chunk)
  activate Repo
  Repo -> DB: INSERT INTO customers
  Repo --> Service: Saved entities
  deactivate Repo
end

Service -> Service: generateDeliveryMetrics()
Service --> Facade: ingestionStats Map
deactivate Service

Facade --> Controller: Result Map
deactivate Facade

Controller --> Frontend: HTTP 200 OK\n{status: "success", recordsProcessed: 99441}
deactivate Controller

Frontend -> Frontend: Display Success Message\nShow Statistics
Frontend --> User: "Dataset ingested successfully!"
deactivate Frontend

@enduml
```

---

## 10. Sequence Diagram - Analytics Workflow

**Purpose**: Shows how analytics requests are processed using Strategy Pattern

**Assignment Mapping**: ✅ P3 Component Interaction, ✅ M2 Design Patterns

```plantuml
@startuml SequenceAnalytics
!theme superhero

actor "Data Analyst" as User
participant "Streamlit\nDashboard" as Frontend
participant "AnalyticsController" as Controller
participant "TransitFlowFacade" as Facade
participant "RevenueAnalysisStrategy" as Strategy
participant "PaymentRepository" as Repo
database "H2 Database" as DB

User -> Frontend: Navigate to\nRevenue Analysis Page
activate Frontend

Frontend -> Controller: GET /api/analytics/revenue-analysis
activate Controller

Controller -> Facade: executeAnalysis("revenue_analysis")
activate Facade

Facade -> Facade: Select Strategy\nBased on Name

Facade -> Strategy: execute()
activate Strategy

Strategy -> Repo: sumAllPayments()
activate Repo
Repo -> DB: SELECT SUM(payment_value)\nFROM payments
DB --> Repo: Total Revenue
Repo --> Strategy: 13,591,643.70
deactivate Repo

Strategy -> Repo: sumRevenueByMonth()
activate Repo
Repo -> DB: SELECT MONTH, SUM(payment_value)\nFROM payments\nGROUP BY MONTH
DB --> Repo: Monthly Aggregates
Repo --> Strategy: List<Object[]>
deactivate Repo

Strategy -> Repo: findAll()
activate Repo
Repo -> DB: SELECT payment_type, COUNT(*)\nFROM payments\nGROUP BY payment_type
DB --> Repo: Payment Types
Repo --> Strategy: Payment Distribution
deactivate Repo

Strategy -> Strategy: Calculate\nAverage Order Value

Strategy --> Facade: analyticsResult Map\n{totalRevenue, monthlyTrends, paymentTypes}
deactivate Strategy

Facade --> Controller: Result Map
deactivate Facade

Controller --> Frontend: HTTP 200 OK\nJSON Response
deactivate Controller

Frontend -> Frontend: Parse Data\nGenerate Charts
Frontend --> User: Display Revenue Dashboard\nwith Visualizations
deactivate Frontend

@enduml
```

---

## 11. Entity Relationship Diagram (ERD)

**Purpose**: Shows database schema and relationships

**Assignment Mapping**: ✅ Large Dataset Design, ✅ Data Model Design

```plantuml
@startuml ERD
!theme sketchy-outline

entity "Customer" as customer {
  * customer_id : VARCHAR <<PK>>
  --
  customer_unique_id : VARCHAR
  customer_zip_code_prefix : VARCHAR
  customer_city : VARCHAR
  customer_state : VARCHAR
}

entity "Order" as order {
  * order_id : VARCHAR <<PK>>
  --
  * customer_id : VARCHAR <<FK>>
  order_status : VARCHAR
  order_purchase_timestamp : TIMESTAMP
  order_approved_at : TIMESTAMP
  order_delivered_customer_date : TIMESTAMP
  order_estimated_delivery_date : TIMESTAMP
}

entity "OrderItem" as orderitem {
  * id : BIGINT <<PK>>
  --
  * order_id : VARCHAR <<FK>>
  * product_id : VARCHAR <<FK>>
  * seller_id : VARCHAR <<FK>>
  order_item_id : INTEGER
  shipping_limit_date : TIMESTAMP
  price : DOUBLE
  freight_value : DOUBLE
}

entity "Payment" as payment {
  * id : BIGINT <<PK>>
  --
  * order_id : VARCHAR <<FK>>
  payment_sequential : INTEGER
  payment_type : VARCHAR
  payment_installments : INTEGER
  payment_value : DOUBLE
}

entity "Product" as product {
  * product_id : VARCHAR <<PK>>
  --
  product_category_name : VARCHAR
  product_name_length : INTEGER
  product_weight_g : INTEGER
  product_length_cm : INTEGER
  product_height_cm : INTEGER
  product_width_cm : INTEGER
}

entity "Seller" as seller {
  * seller_id : VARCHAR <<PK>>
  --
  seller_zip_code_prefix : VARCHAR
  seller_city : VARCHAR
  seller_state : VARCHAR
}

entity "Delivery" as delivery {
  * order_id : VARCHAR <<PK>>
  --
  purchase_timestamp : TIMESTAMP
  delivered_timestamp : TIMESTAMP
  transit_time_hours : BIGINT
  delay_hours : BIGINT
  is_delayed : BOOLEAN
}

entity "FaultyRecord" as faulty {
  * id : BIGINT <<PK>>
  --
  dataset_name : VARCHAR
  error_message : TEXT
  raw_data : TEXT
  created_at : TIMESTAMP
}

customer ||--o{ order : "places"
order ||--|{ orderitem : "contains"
order ||--|{ payment : "has"
order ||--|| delivery : "tracked by"
product ||--o{ orderitem : "ordered as"
seller ||--o{ orderitem : "sells"

@enduml
```

---

## 12. Testing Architecture Diagram

**Purpose**: Shows testing structure for the system

**Assignment Mapping**: ✅ P4 Testing Strategy

```plantuml
@startuml TestingArchitecture
!theme cerulean-outline

package "Backend Testing (Java)" {
  package "Unit Tests" {
    [RepositoryTests] as UT1
    [ServiceTests] as UT2
    [StrategyTests] as UT3
    [ControllerTests] as UT4
  }
  
  package "Integration Tests" {
    [API Integration Tests] as IT1
    [Database Integration Tests] as IT2
  }
  
  [JUnit 5] as JUnit
  [Mockito] as Mockito
  [Spring Test] as SpringTest
}

package "Frontend Testing (Python)" {
  package "Unit Tests" {
    [Page Tests] as PT1
    [Utility Tests] as PT2
  }
  
  package "Integration Tests" {
    [API Integration Tests] as PT3
  }
  
  [PyTest] as PyTest
  [Requests Library] as Requests
}

package "End-to-End Testing" {
  [Selenium WebDriver] as Selenium
  [UI Automation Tests] as E2E
}

package "Application Under Test" {
  [Spring Boot Backend] as Backend
  [Streamlit Frontend] as Frontend
  database "H2 Database" as DB
}

UT1 --> JUnit
UT2 --> JUnit
UT2 --> Mockito
UT3 --> JUnit
UT4 --> SpringTest

IT1 --> SpringTest
IT2 --> SpringTest

PT1 --> PyTest
PT2 --> PyTest
PT3 --> PyTest
PT3 --> Requests

E2E --> Selenium

IT1 --> Backend
IT2 --> DB
PT3 --> Backend
E2E --> Frontend
E2E --> Backend

note right of JUnit
  Testing Framework:
  - JUnit 5 (Jupiter)
  - @Test annotations
  - Assertions
end note

note right of PyTest
  Python Testing:
  - pytest framework
  - fixtures
  - parameterized tests
end note

note bottom of E2E
  Full User Journey:
  1. Upload dataset
  2. Navigate pages
  3. View analytics
  4. Generate reports
end note

@enduml
```

---

## 13. Automated Testing Workflow Diagram

**Purpose**: Shows CI/CD testing automation workflow

**Assignment Mapping**: ✅ P4 Automated Testing

```plantuml
@startuml TestingWorkflow
!theme spacelab

start

:Developer Commits Code;

:CI/CD Pipeline Triggered;

partition "Build Phase" {
  :Checkout Code from Git;
  :Install Dependencies;
  
  fork
    :Maven Clean Install;
  fork again
    :pip install requirements.txt;
  end fork
}

partition "Static Analysis" {
  fork
    :Run Java Linter (Checkstyle);
  fork again
    :Run Python Linter (Flake8);
  fork again
    :SonarQube Code Analysis;
  end fork
  
  if (Code Quality Pass?) then (yes)
  else (no)
    :Report Issues;
    stop
  endif
}

partition "Unit Tests" {
  fork
    :Run JUnit Tests;
    :Generate Coverage Report (JaCoCo);
  fork again
    :Run PyTest;
    :Generate Coverage Report (Coverage.py);
  end fork
  
  if (All Tests Pass?) then (yes)
  else (no)
    :Send Failure Notification;
    stop
  endif
  
  if (Coverage > 80%?) then (yes)
  else (no)
    :Warning: Low Coverage;
  endif
}

partition "Integration Tests" {
  :Start Test Database;
  :Run API Integration Tests;
  :Validate Database Operations;
  :Shutdown Test Database;
  
  if (Integration Tests Pass?) then (yes)
  else (no)
    :Send Failure Notification;
    stop
  endif
}

partition "End-to-End Tests" {
  :Start Backend Server;
  :Start Frontend Server;
  :Run Selenium Tests;
  :Test User Workflows;
  :Shutdown Servers;
  
  if (E2E Tests Pass?) then (yes)
  else (no)
    :Send Failure Notification;
    :Capture Screenshots;
    stop
  endif
}

:Generate Test Reports;
:Publish Artifacts;

if (Deploy to Production?) then (yes)
  :Deploy Application;
  :Run Smoke Tests;
  :Send Success Notification;
else (no)
  :Deploy to Staging;
endif

stop

@enduml
```

---

## 14. Component Diagram

**Purpose**: Shows major system components and their dependencies

**Assignment Mapping**: ⭐⭐ Distinction Level - System Architecture

```plantuml
@startuml ComponentDiagram
!theme vibrant

package "Frontend Layer" {
  component "Streamlit Dashboard" as UI {
    [Home Page]
    [Dataset Upload]
    [Analytics Pages]
    [Visualization Engine]
  }
}

package "API Gateway" {
  component "REST API" as API {
    [DatasetController]
    [AnalyticsController]
    [RootController]
  }
}

package "Business Logic Layer" {
  component "Facade" as Facade {
    [TransitFlowFacade]
  }
  
  component "Services" as Services {
    [DatasetService]
    [Analytics Strategies]
  }
  
  component "Adapters" as Adapters {
    [CsvDatasetAdapter]
    [DatasetLoaderFactory]
  }
}

package "Data Access Layer" {
  component "Repositories" as Repos {
    [CustomerRepository]
    [OrderRepository]
    [PaymentRepository]
    [DeliveryRepository]
    [ProductRepository]
    [SellerRepository]
  }
}

package "Persistence Layer" {
  database "H2 In-Memory Database" as DB {
    [customers table]
    [orders table]
    [payments table]
    [deliveries table]
  }
}

package "External Data Sources" {
  folder "CSV Files" as CSV {
    file customers.csv
    file orders.csv
    file payments.csv
  }
}

' Dependencies
UI --> API : HTTP/REST
API --> Facade : Method Calls
Facade --> Services : Delegates
Facade --> Adapters : Uses
Services --> Repos : Data Access
Repos --> DB : JPA/Hibernate
Adapters --> CSV : Reads
Services --> CSV : Loads

note right of UI
  Component: Frontend
  Technology: Streamlit (Python)
  Port: 8501
  Responsibility: User Interface
end note

note right of API
  Component: REST API
  Technology: Spring Boot
  Port: 8080
  Responsibility: HTTP Endpoints
end note

note right of Facade
  Component: Business Facade
  Pattern: Facade Pattern
  Responsibility: Simplify Complexity
end note

note right of DB
  Component: Database
  Technology: H2 In-Memory
  Responsibility: Data Persistence
  Records: 500,000+
end note

@enduml
```

---

## 15. Deployment Diagram

**Purpose**: Shows physical deployment architecture

**Assignment Mapping**: ⭐⭐ Distinction Level - Deployment Architecture

```plantuml
@startuml DeploymentDiagram
!theme superhero-outline

node "User's PC" as UserPC {
  component "Web Browser" as Browser
  component "Streamlit Desktop App" as StreamlitApp
}

node "Application Server" as AppServer {
  artifact "Spring Boot JAR" as SpringJAR {
    component "REST API\n(Port 8080)" as RestAPI
    component "Embedded Tomcat" as Tomcat
  }
  
  artifact "Streamlit App" as StreamlitArtifact {
    component "Frontend\n(Port 8501)" as Frontend
    component "Python Runtime" as Python
  }
  
  database "H2 In-Memory DB" as H2DB {
    storage "customers" as customers
    storage "orders" as orders
    storage "payments" as payments
  }
}

node "File System" as FileSystem {
  folder "Dataset Directory" as DataDir {
    file "customers.csv" as csv1
    file "orders.csv" as csv2
    file "payments.csv" as csv3
    file "products.csv" as csv4
  }
}

' Connections
UserPC -down-> Frontend : HTTP\n(localhost:8501)
Browser -down-> RestAPI : HTTP REST\n(localhost:8080)
StreamlitApp -down-> RestAPI : HTTP REST\n(localhost:8080)

Frontend -down-> RestAPI : Requests\nAnalytics

RestAPI -down-> H2DB : JDBC\nConnection

RestAPI -down-> DataDir : Read\nCSV Files

note right of UserPC
  Deployment: Desktop
  OS: Windows 10/11
  Browser: Chrome/Edge
  Requirements:
  - Java 24 Runtime
  - Python 3.10+
end note

note right of SpringJAR
  Backend Deployment:
  - Executable JAR
  - Embedded Server
  - Port: 8080
  - JVM: Java 24
  - Memory: 2GB heap
end note

note right of StreamlitArtifact
  Frontend Deployment:
  - Python App
  - Streamlit Framework
  - Port: 8501
  - Python: 3.10+
end note

note right of H2DB
  Database:
  - In-Memory Mode
  - No Persistence
  - Fast Performance
  - Development Use
  
  Production Alternative:
  - PostgreSQL
  - MySQL
  - MongoDB
end note

note bottom of DataDir
  Dataset Storage:
  - Local File System
  - CSV Format
  - ~500MB total
  - 6 dataset files
end note

@enduml
```

---

## 📊 Diagram Summary

### Assignment Coverage Map

| Diagram # | Diagram Name | P3 | P4 | M2 | Distinction |
|-----------|-------------|----|----|-------|-------------|
| 1 | Use Case Diagram | ✅ | - | - | - |
| 2 | MVC Architecture | ✅ | - | - | - |
| 3 | Class Diagram | ✅ | - | ✅ | - |
| 4 | Factory Pattern | - | - | ✅ | - |
| 5 | Strategy Pattern | - | - | ✅ | - |
| 6 | Adapter Pattern | - | - | ✅ | - |
| 7 | Facade Pattern | - | - | ✅ | - |
| 8 | Activity Diagram | ✅ | - | - | - |
| 9 | Sequence (Upload) | ✅ | - | - | - |
| 10 | Sequence (Analytics) | ✅ | - | ✅ | - |
| 11 | ERD | ✅ | - | - | - |
| 12 | Testing Architecture | - | ✅ | - | - |
| 13 | Testing Workflow | - | ✅ | - | - |
| 14 | Component Diagram | ✅ | - | - | ⭐⭐ |
| 15 | Deployment Diagram | ✅ | - | - | ⭐⭐ |

### Key Points for Assignment Submission

#### **P3 (Design)** - 9 Diagrams
- Use Case: Shows functional requirements
- MVC Architecture: Overall system design
- Class Diagram: OOP implementation
- Activity: Process workflows
- 2 Sequence Diagrams: Component interactions
- ERD: Database design
- Component & Deployment: System architecture

#### **P4 (Testing)** - 2 Diagrams
- Testing Architecture: Testing structure
- Testing Workflow: Automated testing process

#### **M2 (Design Patterns)** - 5 Diagrams
- Factory Pattern: Object creation
- Strategy Pattern: Interchangeable algorithms
- Adapter Pattern: Format conversion
- Facade Pattern: Simplified interface
- Class Diagram: Shows SOLID principles

#### **Distinction Level** - 2 Diagrams
- Component Diagram: Advanced architecture
- Deployment Diagram: Infrastructure design

---

## 🎯 Design Patterns Explained

### 1. **Factory Pattern** (DatasetLoaderFactory)
**Purpose**: Creates appropriate adapter based on file type (CSV, JSON, Excel)

**Benefits**:
- Encapsulates object creation logic
- Easy to add new file formats
- Client code doesn't need to know concrete classes

**Example**:
```java
DatasetAdapter<Customer> adapter = DatasetLoaderFactory.createCustomerLoader(filePath);
// Factory decides: CSV → CsvDatasetAdapter, JSON → JsonDatasetAdapter
```

---

### 2. **Strategy Pattern** (AnalyticsStrategy)
**Purpose**: Encapsulates interchangeable analytics algorithms

**Benefits**:
- Each strategy is independent and testable
- Easy to add new analytics without modifying existing code
- Algorithms can be selected dynamically at runtime

**Implementations**:
- PeakDeliveryStrategy
- CustomerSegmentationStrategy
- RevenueAnalysisStrategy
- DeliveryEfficiencyStrategy
- AnomalyDetectionStrategy
- SeasonalDemandStrategy
- RegionalPerformanceStrategy

---

### 3. **Adapter Pattern** (DatasetAdapter)
**Purpose**: Converts different file formats into standardized entity objects

**Benefits**:
- Decouples data source from business logic
- Each adapter handles format-specific parsing
- Easy to support multiple formats

**Flow**:
```
CSV File → CsvDatasetAdapter → Customer Entity → Repository → Database
JSON File → JsonDatasetAdapter → Customer Entity → Repository → Database
```

---

### 4. **Facade Pattern** (TransitFlowFacade)
**Purpose**: Provides simplified interface to complex subsystems

**Benefits**:
- Controllers call one simple method instead of managing multiple services
- Hides complexity of service/strategy coordination
- Single point for cross-cutting concerns (logging, caching)

**Example**:
```java
// Without Facade (Complex)
DatasetService service = new DatasetService();
service.loadCustomers();
service.loadOrders();
service.validateData();
service.processMetrics();

// With Facade (Simple)
facade.ingestDatasets(directoryPath);
```

---

## 🛠️ How to Generate Diagrams

### Method 1: Online PlantUML Editor (Easiest)
1. Visit: https://www.plantuml.com/plantuml/uml/
2. Copy any diagram code from this file
3. Paste into the editor
4. Click "Submit" to generate
5. Download as PNG/SVG

### Method 2: VS Code Extension
1. Install "PlantUML" extension by jebbs
2. Open this DIAGRAMS.md file
3. Press `Alt+D` to preview any diagram
4. Right-click diagram → Export to PNG/SVG

### Method 3: IntelliJ IDEA Plugin
1. Install "PlantUML integration" plugin
2. Open this file in IntelliJ
3. Right-click code block → Show PlantUML Diagram
4. Export as needed

### Method 4: Command Line
```bash
# Install PlantUML
npm install -g node-plantuml

# Generate diagram
puml generate diagram.puml -o output.png
```

---

## 📖 PlantUML Resources

- **Official Website**: https://plantuml.com/
- **Language Reference**: https://plantuml.com/guide
- **Theme Gallery**: https://the-lum.github.io/puml-themes-gallery/
- **Real World Examples**: https://real-world-plantuml.com/

---

## 📝 Tips for Assignment Submission

### For P3 (Design Document):
1. Include diagrams 1, 2, 3, 8, 9, 10, 11, 14, 15
2. Explain each diagram in your document
3. Show how design supports requirements
4. Reference ARCHITECTURE.md for context

### For M2 (Design Patterns Refinement):
1. Include diagrams 4, 5, 6, 7
2. Explain each pattern's benefits
3. Show code examples from your system
4. Demonstrate SOLID principles

### For P4 (Testing Strategy):
1. Include diagrams 12, 13
2. Explain testing levels (unit, integration, E2E)
3. Show test coverage reports
4. Document automated testing setup

### For Distinction:
1. Include all 15 diagrams
2. Add component and deployment diagrams
3. Show scalability considerations
4. Document performance optimizations

---

## ✅ Quality Checklist

Before submitting, ensure:
- [ ] All 15 diagrams render correctly
- [ ] Diagrams match your actual code
- [ ] Labels are clear and readable
- [ ] Relationships are accurate
- [ ] Patterns are correctly implemented
- [ ] Assignment requirements are mapped
- [ ] Diagrams support your documentation

---

**Document Version**: 1.0.0  
**Last Updated**: 2026-06-03  
**Author**: TransitFlow Development Team  
**Purpose**: Academic Assignment (P3, P4, M2)

