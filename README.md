# Coupon Hub: Connecting Businesses and Customers Through Seamless Deals

## Overview

The **Coupon System Project** is a dynamic platform designed to streamline coupon management for businesses and customers. It supports three main roles:

- **Customers**: Browse and purchase coupons effortlessly via a user-friendly interface.
- **Companies**: Create, update, and manage their coupons through secure and intuitive tools.
- **The Administrator**: Oversee system operations, manage users, and ensure the platform's smooth functioning.

The development journey of the project was divided into distinct phases, each focusing on building core functionalities and integrating advanced capabilities. Here's how the system was built step by step:

---

## Project Flow: Development Phases 

### **Phase 1: Building the Core System**  
The foundation of the system is established, handling core business operations and database management.  

#### Key Steps:  
1. **Core Functionality**: Implemented essential operations like creating, updating, and managing coupons, along with customer purchases.  
2. **Thread for Maintenance**: Introduced the `DailyCouponExpirationTask` to automatically clean expired coupons daily, ensuring system consistency.  
3. **Database Integration**: Utilized **JDBC** for direct database interactions and implemented connection pooling for improved performance.  
4. **MVC Structure**:  
   - **API Layer**: Handles client-server communication.  
   - **Controller Layer**: Includes business logic.  
   - **Data Access Object (DAO) Layer**: Manages database operations.  

*Technologies*: **Java**, **JDBC**, **SQL**, **Multithreading**, **Connection Pooling**.  

---

### **Phase 2: Web Layer Integration**  
This phase exposes the system to the internet, providing an interactive web interface for end-users.  

#### Key Steps:  
1. **Frontend Development**: Built a responsive interface using **AngularJS**, **HTML**, **CSS**, and **JavaScript**.  
2. **RESTful APIs**: Implemented to facilitate seamless communication between the frontend and backend.  
3. **Role-Specific Dashboards**:  
   - **Customers**: View and purchase coupons.  
   - **Companies**: Manage their coupons.  
   - **Administrator**: Oversee the system.  

*Technologies*: **AngularJS**, **HTML**, **CSS**, **JavaScript**, **RESTful APIs**.  

---

### **Phase 3: Enterprise Integration**  
Enhances the core system with advanced enterprise-level capabilities.  

#### Key Steps:  
1. **Revenue Tracking**: Introduced the `Income` entity to document the system's revenues.  
2. **Asynchronous Processing**: Integrated **JMS (Java Message Service)** for decoupled and scalable transaction logging.  
3. **JPA Integration**: Simplified database operations by adopting JPA for Object-Relational Mapping (ORM).  

*Technologies*: **JMS**, **JPA**, **Hibernate**, **Java EE**.  

---

### **Phase 4: Transition to Spring Framework**  
Adopts **Spring Framework** to streamline the development process and enhance modularity.  

#### Key Steps:  
1. **Spring Boot**: Refactored the backend to leverage Spring Boot for simplified configuration.  
2. **Spring MVC**: Used to improve backend design and enhance routing and logic separation.  

*Technologies*: **Spring Boot**, **Spring MVC**.  

---

### **Phase 5: Microservice for Revenue Tracking**  
This phase focuses on decoupling the revenue tracking functionality as a dedicated microservice.  

#### Key Steps:  
1. **Standalone Microservice**: Extracted the `Income` entity and its related operations into a separate Spring Boot microservice.  
2. **Spring Data JPA**: Utilized for simplifying database persistence in the microservice.  

*Technologies*: **Spring Data JPA**, **Microservices**.