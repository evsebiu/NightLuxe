# NightLuxe

[![GitHub stars](https://img.shields.io/github/stars/evsebiu/NightLuxe.svg?style=social&label=Stars)](https://github.com/evsebiu/NightLuxe/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/evsebiu/NightLuxe.svg?style=social&label=Fork)](https://github.com/evsebiu/NightLuxe/network/members)

NightLuxe is a dedicated advertisement platform designed specifically for the Maltese market. It serves as a central hub for businesses and individuals to publish, manage, and showcase their advertisements, reaching a targeted audience within Malta.

## 🚀 Key Features & Benefits

*   **Malta-Focused Advertising**: Tailored for the unique market and audience in Malta.
*   **Advertisement Management**: Seamless creation, editing, and deletion of advertisement listings.
*   **Centralized Platform**: Provides a single point of access for all advertising needs in the region.
*   **Java Backend**: Robust and scalable backend built with Java, ensuring reliability and performance.
*   **Modular Architecture**: Organized project structure for maintainability and future expansion.

## 🛠️ Prerequisites & Dependencies

To build and run NightLuxe, you need to have the following installed:

*   **Java Development Kit (JDK)**: Version 17 or higher.
*   **Apache Maven**: Version 3.6.3 or higher (or use the included Maven Wrapper).

## 💻 Installation & Setup Instructions

Follow these steps to get NightLuxe up and running on your local machine:

1.  **Clone the repository**:
    ```bash
    git clone https://github.com/evsebiu/NightLuxe.git
    cd NightLuxe
    ```

2.  **Build the project**:
    Use the Maven wrapper to build the project. This will download all necessary dependencies and package the application into a JAR file.
    ```bash
    ./mvnw clean install
    ```
    *On Windows, use `mvnw.cmd clean install`.*

3.  **Run the application**:
    After a successful build, you can run the application directly from the JAR file located in the `target/` directory:
    ```bash
    java -jar target/core-0.0.1-SNAPSHOT.jar
    ```
    Alternatively, you can run it via Maven:
    ```bash
    ./mvnw spring-boot:run
    ```

The application should now be running, typically accessible at `http://localhost:8080` (unless configured otherwise).

## API Documentation & Usage

NightLuxe exposes a RESTful API for managing advertisements. The primary controller for this is `AdvertisementController.java`.

### Example API Endpoint

While full API documentation (e.g., Swagger/OpenAPI) is not explicitly provided, here's an example of a typical endpoint you might interact with:

**Get all advertisements:**
```http
GET /api/advertisements
```

**Request:**
```
GET /api/advertisements HTTP/1.1
Host: localhost:8080
```

**Expected Response (Example):**
```json
[
  {
    "id": "ad-123",
    "title": "Grand Opening Sale!",
    "description": "Exclusive discounts on all items for a limited time.",
    "location": "Valletta",
    "startDate": "2023-10-26",
    "endDate": "2023-11-10",
    "contact": "info@example.com"
  },
  {
    "id": "ad-124",
    "title": "Yoga Classes",
    "description": "Beginner-friendly yoga classes every Tuesday.",
    "location": "Sliema",
    "startDate": "2023-11-01",
    "endDate": "2024-01-31",
    "contact": "+356 12345678"
  }
]
```

Further API interactions (e.g., `POST` for creating, `PUT` for updating, `DELETE` for removing) would follow standard REST conventions.

## ⚙️ Configuration Options

NightLuxe uses Spring Boot's externalized configuration. Application settings can be modified using `application.properties` (or `application.yml`) files.

Key configuration aspects typically include:

*   **Server Port**: `server.port=8080`
*   **Database Connection**: (If a database is integrated)
    *   `spring.datasource.url=jdbc:h2:mem:testdb`
    *   `spring.datasource.username=sa`
    *   `spring.datasource.password=`
    *   `spring.jpa.hibernate.ddl-auto=update`
*   **Application-specific properties**: Custom properties can be defined in `src/main/resources/application.properties` to control various behaviors of the platform.

The `ApplicationConfig.java` and `Webconfig.java` files within the `com.nightluxe.core.config` package define programmatic configurations for the application context and web-related settings.

## 🤝 Contributing Guidelines

We welcome contributions to NightLuxe! If you're interested in improving the platform, please follow these steps:

1.  **Fork** the repository.
2.  **Clone** your forked repository: `git clone https://github.com/[YOUR_USERNAME]/NightLuxe.git`
3.  Create a new **branch** for your feature or bug fix: `git checkout -b feature/your-feature-name` or `git checkout -b bugfix/issue-description`
4.  Make your changes and ensure tests pass (if applicable).
5.  **Commit** your changes with clear and descriptive messages.
6.  **Push** your branch to your forked repository.
7.  Open a **Pull Request** to the `main` branch of the original NightLuxe repository.

Please ensure your code adheres to standard Java conventions and includes appropriate comments where necessary.

## 📄 License Information

The license for this project is currently **Not Specified**. This means that by default, all rights are reserved by the copyright holder(s) and you cannot copy, distribute, or modify the software without explicit permission.

We recommend adding an open-source license (e.g., MIT, Apache 2.0) to clearly define how others can use, modify, and distribute the project.

## 🙏 Acknowledgments

*   Built using the powerful Spring Boot framework.
*   Special thanks to the open-source community for providing invaluable tools and libraries.
