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
* **Delay Alerts**: Notify all passengers of a specific train about delays via automated email alerts.

---

## Arhitectural Logic & Data Flow

### The RouteStop Logic
In this system, a `Route` is an abstract concept. The actual journey is defined by its **RouteStops**.
* **Direct Routes**: Even a direct journey consists of two `RouteStops` (Departure and Arrival).
* **Station Terminus Logic**: 
    * The **first station** (Origin) has an `arrival_time` set to `null` because the train starts there.
    * The **last station** (Destination) has a `departure_time` set to `null` because the journey ends there.
* **Search Algorithm**: The system finds routes by checking if both requested stations exist as `RouteStops` for the same route and ensuring the `stop_order` of the departure station is lower than the arrival station.

### DTO Pattern
The application uses **Data Transfer Objects** to decouple the database from the client:
* `RouteRequestDTO`: Handles the complex creation of a Route and its associated `RouteStop` entities in a single atomic request.
* `BookingRequestDTO`: Manages the reservation data sent from the passenger's side.
* `AdminBookingDTO`: Flattens complex database relationships into a simple format for the Admin table.
* `StopRequestDTO`: A helper object used within RouteRequestDTO to define individual station details (name, order, and times) during route creation.
*`RouteResponseDTO`: Formats the search results for passengers, combining train details and specific station timings into a clean, readable object.

---
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
```
### 3. Running the Application
* Open the project in IntelliJ IDEA.
* Allow Maven to download all necessary dependencies.
* Run the TrainSystemApplication.java file.
* The server will start at **http://localhost:8080**.

### 4. Accessing the System
* Main App: http://localhost:8080/index.html
* H2 Console: http://localhost:8080/h2-console
* JDBC URL: jdbc:h2:mem:testdb
* User: sa | Password: (leave blank)

### Admin Instructions
To successfully create a route:
* Create a Train: Ensure a train exists in the "Manage Trains" tab first.
* Station Names: Enter station names exactly as they appear in the database (e.g., Bucuresti Nord, Cluj-Napoca).
* Route Stops: For a direct route, the system automatically sets the first station's arrival time to null and the last station's departure time to null.

###  Database Schema
The database consists of 5 main entities:
* Train: Stores train numbers and total capacity.
* Station: Contains station names.
* Route: Links a train to a specific journey name.
* RouteStop: The junction table defining the sequence, arrival, and departure times.
* Booking: Tracks passenger emails, reserved seats, and their associated trains.
spring.mail.password=your_mailtrap_password

### Technical Setup & Troubleshooting
To ensure the project compiles and runs correctly, follow these configuration steps:

* **System Requirements**: 
    * **JDK**: Java 17 (Recommended: Microsoft OpenJDK or Oracle LTS).
    * **IDE**: IntelliJ IDEA (preferred) or Eclipse.
    * **Build Tool**: Maven 3.6+.
* **Enable Annotation Processing**: In IntelliJ, go to `Settings > Build, Execution, Deployment > Compiler > Annotation Processors` and check the box **"Enable annotation processing"**.
* **Lombok Plugin**: Ensure the **Lombok plugin** is installed and active in your IDE to avoid compilation errors like `UNKNOWN TypeTag`.
* **Project SDK**: Verify that both the **Project SDK** and the **Java Compiler** level are set to `17` in your IDE settings.
* **Maven Reload**: If dependencies are not recognized, right-click `pom.xml` and select **Maven > Reload Project**.

