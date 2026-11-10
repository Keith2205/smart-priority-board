# 🎯 Smart Priority Board — Backend

> AI-powered task management with Eisenhower Matrix prioritization

## 📖 About

Smart Priority Board is a production-grade productivity application that helps users manage and prioritize tasks intelligently using the Eisenhower Matrix (Important/Urgent framework). The backend is built with Java Spring Boot and connects to a MongoDB Atlas cloud database.

## ✨ Features

- 🔐 **Secure Authentication** — JWT-based login & registration with BCrypt password hashing
- 📋 **Task Management** — Full CRUD with pagination and filtering
- 🎯 **Eisenhower Matrix** — Auto-calculates Q1/Q2/Q3/Q4 quadrant based on importance and urgency
- 📊 **Status Board** — Tasks move through TODO → IN PROGRESS → DONE
- 🤖 **AI Features** — Auto-tagging, priority suggestion, subtask generation (Phase 2)
- 🔄 **Pattern Recognition** — Detects recurring tasks after 5 occurrences
- 👤 **User Profiles** — Strengths, work hours, work style for AI personalization
- 🗑️ **Auto-Cleanup** — DONE tasks auto-deleted after 72 hours, logged for 30 days
- 🛡️ **Rate Limiting** — Brute force protection on login endpoint

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 | Core language |
| Spring Boot 3.4.3 | Application framework |
| Spring Security | Authentication & authorization |
| JWT (jjwt 0.12.6) | Token-based auth |
| MongoDB Atlas | Cloud database |
| Spring Data MongoDB | Database layer |
| Lombok | Boilerplate reduction |
| Maven | Build tool |

## 🚀 Getting Started

### Prerequisites
- Java 21+
- Maven 3.9+
- MongoDB Atlas account

### Environment Variables
Set these before running:
```bash
# Windows PowerShell
$env:MONGODB_URI="mongodb+srv://user:pass@cluster.mongodb.net/smart-priority-board"
$env:JWT_SECRET="your-strong-secret-key-here"
```

### Run the App
```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

## 📡 API Endpoints

### Authentication
| Method | Endpoint | Description |
|---|---|---|
| POST | /auth/register | Create new account |
| POST | /auth/login | Login and get JWT token |

### Tasks
| Method | Endpoint | Description |
|---|---|---|
| GET | /tasks | Get all tasks (paginated) |
| POST | /tasks | Create a task |
| GET | /tasks/{id} | Get task by ID |
| PUT | /tasks/{id} | Update a task |
| DELETE | /tasks/{id} | Delete a task |
| PATCH | /tasks/{id}/status | Update task status |

### AI Features (Phase 2)
| Method | Endpoint | Description |
|---|---|---|
| GET | /ai/status | Get AI features status |
| GET | /ai/rank | AI task ranking |
| POST | /ai/create-task | AI task creation |
| POST | /ai/check-quadrant | AI quadrant validation |
| GET | /ai/focus-list | Daily focus list |

### Other
| Method | Endpoint | Description |
|---|---|---|
| GET/POST | /profile | User profile |
| GET | /patterns/suggestions | Pattern suggestions |
| GET | /activity-log | Activity log |
| DELETE | /activity-log | Clear activity log |

## 🏗️ Project Structure
```
src/main/java/com/smartboard/
├── controller/     # REST API endpoints
├── service/        # Business logic
├── repository/     # Database queries
├── model/          # MongoDB documents
├── dto/            # Request/Response objects
├── security/       # JWT & auth filters
├── config/         # Spring configuration
└── exception/      # Error handling
```

## 🔐 Security Features

- BCrypt password hashing
- JWT stateless authentication
- Rate limiting on login (5 attempts/minute)
- Environment variable credentials
- CORS configuration
- Input validation on all endpoints

## 🔜 Roadmap

- [ ] Phase 2 — Claude AI integration
- [ ] Real-time notifications
- [ ] Team collaboration features
- [ ] Mobile app

## 👨‍💻 Author

**Keith Rodrigues** — [@Keith2205](https://github.com/Keith2205)

---
*Built with ☕ Java and Claude AI assistance*
