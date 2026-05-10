# 🚆 Train Ticketing System 🚆

A full-stack Train Management and Ticketing system built with **Spring Boot** for the backend and a dynamic **HTML5/JavaScript** frontend. This application simulates a railway ecosystem where admins manage logistics and passengers can book tickets.

---

##  Features

###  Passenger Portal
* **Smart Route Search**: Search for direct or connecting train routes between stations.
* **Real-time Booking**: Book seats on specific trains with instant availability checks.
* **Email Notifications**: Integrated with Mailtrap to send booking confirmations.

###  Admin Dashboard
* **Train Management**: Full CRUD operations (Create, Read, Update, Delete) for trains and their seat capacities.
* **Complex Route Creation**: Add routes with multiple stops, specifying exact arrival and departure times for each station.
* **Booking Overview**: A centralized table to monitor all passenger reservations across the entire system.
* **Delay Alerts**: Notify all passengers of a specific train about delays via automated English email alerts.

---

##  Technical Architecture

The application follows a **DTO (Data Transfer Object)** pattern to ensure clean communication between the layers:
* `RouteRequestDTO`: Handles the complex creation of a Route and its associated `RouteStop` entities in a single atomic request.
* `BookingRequest`: Manages the reservation data sent from the passenger's side.
* `AdminBookingDTO`: Flattens complex database relationships into a simple format for the Admin table.

### Tech Stack:
* **Backend**: Java 17, Spring Boot, Spring Data JPA (Hibernate).
* **Database**: H2 (In-memory for development).
* **Frontend**: HTML5, JavaScript (Fetch API), Bootstrap 5 (CSS).
* **Messaging**: JavaMailSender (integrated with Mailtrap).
* **Utilities**: Lombok (to reduce boilerplate code).

---

##  Getting Started

### 1. Prerequisites
* **JDK 17** or higher.
* **Maven** (integrated in IntelliJ).
* A **Mailtrap** account (for testing email features).

### 2. Configuration (Mailtrap)
Open `src/main/resources/application.properties` and update the following lines with your Mailtrap credentials:
```properties
spring.mail.host=sandbox.smtp.mailtrap.io
spring.mail.port=2525
spring.mail.username=your_mailtrap_username
spring.mail.password=your_mailtrap_password
