# APAPEDIA E-commerce Platform

## Introduction

APAPEDIA is an **fictional** e-commerce platform developed as part of the course on Enterprise Application Architecture and Programming at the Faculty of Computer Science, University of Indonesia. The platform aims to provide an expansive marketplace for UMKM to scale their businesses online. It consists of a set of microservices that handle different aspects of the e-commerce process, with distinct services for user management, product cataloging, and order processing.

## System Architecture

APAPEDIA adopts a microservices architecture for enhanced scalability and flexibility, with the following services:

- **User Service**: Manages user information and authentication.
- **Catalog Service**: Handles product listings within the e-commerce platform.
- **Order Service**: Manages shopping cart functionality and order processing.

Each service operates with its own database to ensure loose coupling and service autonomy.

## Features

### User Service

- Role-based access control for sellers and customers.
- Authentication and authorization using JWT tokens.
- Seller interactions through a web application built with Spring Boot.
- Customer interactions through a mobile application developed with Flutter.

### Catalog Service

- Product management for sellers, including add, update, and remove operations.
- Comprehensive product listings for customers.
- Advanced search and filtering capabilities.

### Order Service

- Shopping cart management.
- Order history for both sellers and customers.
- Order status updates and tracking.

## Technologies Used

- **Spring Boot**: Back-end API development.
- **Flutter**: Front-end mobile application development.
- **PostgreSQL**: Database management.
- **JWT**: Authentication and state management for web and mobile apps.

## Lessons Learned

This project underscored the importance of:

- Embracing **microservices architecture** for complex enterprise applications.
- Implementing **robust authentication mechanisms** with JWT to secure services.
- The versatility of **Flutter** for creating an engaging user experience for mobile platforms.
- The necessity of comprehensive **documentation and collaboration** in a team environment, especially when dealing with multiple services and databases.

## Setup and Installation

To set up the APAPEDIA platform, ensure you have Docker, PostgreSQL, and Flutter installed on your system.

1. **Clone the repository**
2. **Service Initialization:** Set up individual services with their respective databases.
3. **Build and Run the Services**

## Contributors
- Adjie Djaka Permana
- Fariz Eda 
- Fathan Hadyan
- Devina Fitri Handayani 


