## Dictionary app
An application that makes language learning fun and engaging through reading.

### Tech stack:
- **Frontend**: htmx, hyperscript, jte, tailwindcss, JavaScript
- **Backend**: Java, Spring Boot
- **Database**: PostgreSQL

### Codebase rulebook:
### 1. Prefer immutable data:
- **Always use records over classes for data transfer**
- **Classes are still fine for services exceptions**
- **Use wither methods on records for mutation, eg. user.withPassword(...)**
- **Only deal with setters when necessary, eg. custom Collector or library spec**
### 2. Always construct records with custom static factory method, eg. User.of(...)
### 3. No private methods
- **all implementations must be immediately visible**
- **for repeating operations, delegate to a service**
### 4. Each controller may depend on 1 injected service at most
![di_rule](readme_resources/di_rule.png)
### 5. All request parameters MUST go into service for validation before being attached to the model
### Screenshots
![Screenshot](readme_resources/img.png)

### 13/06/2025
![img_2.png](readme_resources/img_2.png)
![img_3.png](readme_resources/img_3.png)
![img_6.png](readme_resources/img_6.png)
![img_5.png](readme_resources/img_5.png)

### 24/06/2025 - add or delete translations
![image](https://github.com/user-attachments/assets/09cd9cb4-f07d-4b84-8f39-a0196701d89b)

### 26/06/2025 - pagination
![image](https://github.com/user-attachments/assets/4ff714d6-b816-4ff5-8382-c7c15c0ba39e)

### 01/07/2025 - AI translation integration

![image](https://github.com/user-attachments/assets/16360693-bf8f-41e3-a269-1a42510eeb23)

![image](https://github.com/user-attachments/assets/adeaf5a4-f026-4563-8159-e0402d7e8406)

### 03/07/2025 - words learned, daily goal trackers
![image](https://github.com/user-attachments/assets/b1ebcd42-fe2a-4827-9e99-7adb527cb9d5)

### 23/07/2025 - home page UI + improved navbar
![image](readme_resources/23-07-2025-1.png)

### document content preserving paragraphs
![image](readme_resources/23-07-2025-2.png)

### translation form adaptive positioning depending on available space on screen
![image](readme_resources/23-07-2025-3.png)
![image](readme_resources/23-07-2025-4.png)

### mobile UI
![image](readme_resources/23-07-2025-5.png)
![image](readme_resources/23-07-2025-6.png)

### 10/08/2025 - phrase selection feature
![image](readme_resources/10-08-2025-1.png)


