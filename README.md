### Attendify 

#### Overview
Attendify is a Java-based application that utilizes various technologies to manage attendance through facial recognition. It supports both server-side operations and client-side interactions with a modern user interface.

#### Key Features

1. **Server-Client Architecture**: Utilizes TCP-based communication for efficient request processing between clients and the server.

2. **Java Networking (java.net)**: Handles communication and data exchange between server and clients.

3. **Java Database Connectivity (JDBC)**: Manages data stored in MySQL database, allowing efficient querying and updating.

4. **HikariCP**: Implements high-performance connection pooling for database interactions, optimizing resource utilization.

5. **Java Computer Vision with OpenCV**: Enables real-time face detection using webcam streams and processes facial features.

6. **AWS Rekognition SDK**: Integrates advanced face detection and recognition capabilities for identity verification.

7. **Java Mail**: Provides email verification using one-time passwords for user authentication.

8. **JavaFX**: Powers the frontend with a modern UI, supporting interactive features and multimedia integration.

9. **Java Swing**: Facilitates webcam simulation for capturing images detected by OpenCV.

#### Implementation Details

- **Database Management**: Efficiently manages database connections with HikariCP to handle multiple concurrent client requests.

- **Thread Management**: Utilizes Java's ExecutorService for thread pooling to optimize resource management during database operations.

- **User Roles and Access Control**: Supports Admin and Client roles with distinct access privileges to attendance records for data security.

- **Face Detection and Capturing**: Integrates OpenCV for real-time face detection and capture using webcam, storing data locally and in the database.

- **Face Recognition and Prediction**: Implements AWS Rekognition for face addition and recognition tasks, enhancing identity verification and attendance tracking.

#### User Interface

- **Admin Dashboard**: Allows admin users to manage attendance records, update profiles, and access help and support.

- **User Dashboard**: Enables clients to view personal attendance records, update profiles, and access support options.

#### Getting Started

- Clone the repository and configure MySQL database settings in the application.

- Compile and run the application with Java runtime environment (JRE) installed.

#### Usage

- Sign in with email and password; use "Forgot Password?" for password recovery.

- Admins can manage attendance, add members, and take attendance via webcam.

- Users can view personal attendance records and update their profiles.

- Log out securely using the "Sign Out" button.

#### Support

For issues or inquiries, contact support through the application's "Help and Support" section.

---

This readme provides a concise overview of Attendify, highlighting its core features, implementation details, user interface functionalities, and basic usage instructions.
