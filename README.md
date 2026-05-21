# 🎧 HelpDesk API
![Java](https://img.shields.io/badge/Java-25-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4-green)
![H2](https://img.shields.io/badge/H2-Database-darkblue)
![Status](https://img.shields.io/badge/Status-In_Development-yellow)

## About
The **HelpDesk API** is a robust, RESTful web service designed to streamline IT support ticketing and issue tracking. Built with security and scalability in mind, the system allows users to create, assign, prioritize, and track support tickets, while facilitating communication through a built-in commenting system. The entire application is secured using stateless token-based authentication.

---

## Tech Stack
* **Language:** Java 25
* **Framework:** Spring Boot 4 (Spring Web, Spring Security, Spring Data JPA)
* **Authentication:** JSON Web Tokens (JWT)
* **Build Tool:** Maven
* **Database:** PostgreSQL / H2 (or your preferred relational database)

---

## User Roles & Permissions
The system uses Role-Based Access Control (RBAC) to enforce boundaries between different types of users:

* **REQUESTER:** Can create support tickets, view their own submitted tickets, and add comments to them.
* **TECHNICIAN:** Can view tickets assigned to them, update status on assigned tickets, and add comments.
* **ADMIN:** Holds full administrative privileges -  can view all tickets, assign technicians, update any ticket, and delete tickets.

---

## Getting Started

### Prerequisites
* Java 25 or higher
* Maven 3.x
* A running relational database instance (H2 included, no setup needed)

### Installation & Run
1. **Clone the repository:**
   ```bash
   git clone https://github.com/Max-Engineer/HelpDeskAPI.git
   cd HelpDeskAPI/helpdesk
   ```
2. **Configure Environment Variables:**
   The application expects a JWT_SECRET environment variable for token signing and validation. Set it in your terminal before running the application:
   ```bash
   export JWT_SECRET="your_secure_random_jwt_secret_key_at_least_32_bytes_long"
   ```
   (*Optional*) If you are not using default database settings, you can also pass your database credentials as environment variables or update them in `src/main/resources/application.properties`.

3. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```
   The API will be accessible at http://localhost:8080.

---

## Default Test Users
The application automatically seeds initial user accounts via data.sql for testing and development purposes.

| Username | Password   | Role       | 
|:---------|:-----------|:-----------|
| `admin`  | `admin123` | ADMIN      |
| `tech1`  | `tech123`  | TECHNICIAN |

*Note:* Remember to include the Authorization: Bearer <your_jwt_token> header in your requests for all protected endpoints.

---

## API Endpoints
### Authentication
| Method | Endpoint          | Description                  | Auth Required |
| :--- |:------------------|:-----------------------------|:--------------|
| POST | `/auth/register`  | Register a new user account | No | 
| POST | `/auth/login` | Authenticate credentials and receive JWT | No |

### Tickets (`/tickets`)
| Method | Endpoint            | Description                                | Auth Required |
|:-------|:--------------------|:-------------------------------------------|:--------------|
| GET    | `/tickets`          | Retrieve a list of all tickets             | Yes |
| GET    | `/tickets/{id}` | Get details of a specific ticket           | Yes |
| POST   | `/tickets`      | Create a new support ticket                | Yes |
| PUT    | `/tickets/{id}` | Update ticket details, status, or priority | Yes |
| PATCH  | `/tickets/{id}/status` | Change status of the ticket | Yes (Admin) |
| PATCH  | `/tickets/{id}/assign` | Assign ticket to technician | Yes (Admin) |
| DELETE | `/tickets/{id}` | Delete a ticket                            | Yes (Admin) |

### Comments (`/tickets/{ticketId}/comments`)
| Method | Endpoint                           | Description                  | Auth Required |
|:-------|:-----------------------------------|:-----------------------------|:--------------|
| GET  | `/tickets/{ticketId}/comments`     | Get all comments associated with a ticket | Yes |
| POST | `/tickets/{ticketId}/comments` | Add a comment to a specific ticket | Yes |

---

## Author
**Maksym Zhelezniakov** - https://github.com/Max-Engineer

