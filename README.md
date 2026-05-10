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

---

## Testing

The application includes a comprehensive testing suite to ensure business logic reliability and API stability.

### 1. Service Layer Tests (Unit Testing)
These tests use **JUnit 5** and **Mockito** to validate business rules in isolation.
* **Key Scenarios Tested**:
    * Route creation logic and station mapping.
    * Validation rules (e.g., preventing train deletion if active bookings exist).
    * Email notification triggers for delays.
    * Seat availability and booking calculations.

### 2. Controller Layer Tests (API Integration Testing)
These tests use **MockMvc** to simulate HTTP requests and verify API responses.
* **Key Scenarios Tested**:
    * Endpoint availability and correct HTTP status codes (`200 OK`, `400 Bad Request`).
    * Proper JSON serialization/deserialization using `ObjectMapper`.
    * Correct mapping of Request DTOs to Service calls.

### Running the Tests
To execute the entire test suite, use the following Maven command:
```bash
mvn test
```
## API Functionality Examples

Below are examples of inputs and outputs for all supported operations in the Train Ticketing System.

### Admin Functionalities

#### 1. Add a New Train
- **Endpoint:** `POST /api/admin/trains`
- **Description:** Registers a new train in the system.
- **Input (JSON Request Body):**
```json
{
  "trainNumber": "IR-1234",
  "totalSeats": 150
}
```
- **Output (JSON Response):**
```json
{
  "id": 1,
  "trainNumber": "IR-1234",
  "totalSeats": 150
}
```

#### 2. Get All Trains
- **Endpoint:** `GET /api/admin/trains`
- **Description:** Retrieves a list of all registered trains.
- **Input:** None
- **Output (JSON Response):**
```json
[
  {
    "id": 1,
    "trainNumber": "IR-1234",
    "totalSeats": 150
  }
]
```

#### 3. Update Train Seats
- **Endpoint:** `PUT /api/admin/trains/{id}`
- **Description:** Updates the total seat capacity for an existing train.
- **Input (JSON Request Body):**
```json
{
  "totalSeats": 200
}
```
- **Output (JSON Response):**
```json
{
  "id": 1,
  "trainNumber": "IR-1234",
  "totalSeats": 200
}
```

#### 4. Add a Route with Stops (Simple Setup)
- **Endpoint:** `POST /api/admin/routes/simple`
- **Description:** Creates a route for a given train and automatically sets up all intermediate stops/stations.
- **Input (JSON Request Body):**
```json
{
  "trainId": 1,
  "routeName": "Bucharest - Cluj",
  "stops": [
    {
      "stationName": "Bucharest",
      "stopOrder": 1,
      "arrivalTime": null,
      "departureTime": "10:00"
    },
    {
      "stationName": "Brasov",
      "stopOrder": 2,
      "arrivalTime": "12:30",
      "departureTime": "12:45"
    },
    {
      "stationName": "Cluj",
      "stopOrder": 3,
      "arrivalTime": "18:00",
      "departureTime": null
    }
  ]
}
```
- **Output (Text Response):**
```text
Route and all stops created successfully!
```

#### 5. Get All Routes
- **Endpoint:** `GET /api/admin/routes`
- **Description:** Retrieves all routes currently in the system.
- **Input:** None
- **Output (JSON Response):**
```json
[
  {
    "id": 1,
    "name": "Bucharest - Cluj",
    "train": {
      "id": 1,
      "trainNumber": "IR-1234",
      "totalSeats": 150
    },
    "stops": [
      /* List of Stop Objects */
    ]
  }
]
```

#### 6. Delete Train
- **Endpoint:** `DELETE /api/admin/trains/{id}`
- **Description:** Removes a train from the system (if there are no active bookings).
- **Input:** None
- **Output (Text Response):**
```text
Train deleted successfully.
```

#### 7. Get All Bookings
- **Endpoint:** `GET /api/admin/bookings`
- **Description:** Retrieves all bookings made by passengers across all trains.
- **Input:** None
- **Output (JSON Response):**
```json
[
  {
    "id": 1,
    "passengerEmail": "john.doe@example.com",
    "trainNumber": "IR-1234",
    "departureStation": "Bucharest",
    "arrivalStation": "Cluj",
    "seatsReserved": 2,
    "travelDate": "2023-11-25"
  }
]
```

#### 8. Notify Delay
- **Endpoint:** `POST /api/admin/notify-delay?trainId=1&delayTime=30 mins`
- **Description:** Sends an email alert to all passengers booked on a specific train regarding a delay.
- **Input:** Query Parameters `trainId` and `delayTime`.
- **Output (Text Response):**
```text
Notifications sent to all passengers of train 1
```

---

### Passenger Functionalities

#### 1. Search for Routes
- **Endpoint:** `GET /api/passenger/search?from={stationA}&to={stationB}`
- **Description:** Finds available direct and connecting train routes between two stations.
- **Input:** Query Parameters `from` and `to`. For example: `?from=Bucharest&to=Cluj`.
- **Output (JSON Response):**
```json
[
  {
    "trainId": 1,
    "trainNumber": "IR-1234",
    "fromStation": "Bucharest",
    "toStation": "Cluj",
    "departureTime": "10:00",
    "arrivalTime": "18:00"
  }
]
```

#### 2. Book a Ticket
- **Endpoint:** `POST /api/passenger/book`
- **Description:** Reserves seats on a given train for a specific date and stations, sending an email confirmation.
- **Input (JSON Request Body):**
```json
{
  "trainId": 1,
  "travelDate": "2023-11-25",
  "numberOfSeats": 2,
  "departureStationName": "Bucharest",
  "arrivalStationName": "Cluj",
  "passengerEmail": "john.doe@example.com"
}
```
- **Output (JSON Response):**
```json
{
  "id": 1,
  "passengerEmail": "john.doe@example.com",
  "travelDate": "2023-11-25",
  "seatsReserved": 2,
  "train": {
    "id": 1,
    "trainNumber": "IR-1234",
    "totalSeats": 150
  },
  "departureStation": {
    "id": 1,
    "name": "Bucharest"
  },
  "arrivalStation": {
    "id": 3,
    "name": "Cluj"
  }
}
```

