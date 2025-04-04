# Events Lisbon - Event Management Platform
- WIP -  finished the web page and starting now the mobile apps

- app in production at: https://eventslisbon.tech/

A modern, full-stack event management platform built with Kotlin and Ktor on the backend with a responsive frontend using HTMX, TailwindCSS, and vanilla JavaScript.

🌟 Key Features
- Multi-provider authentication (Google and Facebook OAuth)
- Real-time updates using server-sent events
- Role-based authorization system
- Password reset with email verification
- RESTful API endpoints for mobile app integration
- Google Maps integration for event locations

💻 Technical Stack

## Backend
-  Kotlin + Ktor for asynchronous server-side operations
-  Containerized and hosted on a VPS
-  Amazon SES for transactional emails
-  Clean architecture with repository pattern and dependency injection
-  JWT authentication with Google/Facebook OAuth integration
-  Exposed SQL for type-safe database operations
-  Coroutines for non-blocking request handling

## Frontend
 - HTMX for lightweight, partial page updates
 - TailwindCSS for responsive UI design
 - Server-side rendering with Kotlin's type-safe HTML DSL
 - Minimal JavaScript for enhanced interactions

🔧 Implementation Highlights

## Database & Performance
- PostgreSQL with normalized schema design and optimized indexing
- Transaction management for data consistency
- Partial page updates for improved responsiveness
- Lazy loading of resources and optimized image delivery

## Architecture
- Separation of concerns with layered design
- Type-safe route organization with Ktor builders
- Extension functions for reusable components
- Suspension functions for non-blocking operations

This project showcases modern server-side rendering techniques with HTMX and functional programming with Kotlin, focusing on performance and maintainability.
