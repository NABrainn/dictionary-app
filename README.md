### Dictionary app
Web application for learning languages (not programming languages).

### How it works
The user inserts a document of text in a language of choice. Each of the words is highlighted and can be interacted with:
-by clicking - a request will be sent to the server to bring a form with the translation fetched from the APIs
-by selecting - a request will be sent to the server to combine 2 to 5 words into a phrase and similarly bring translation form.
Each translation has 5 levels of familiarity, reflected in the UI - the smaller the familiarity, the better the visibility.

There is also a flashcard section where the translations can be reviewed.

Currently the application supports 4 languages:
- Polish
- English
- Norwegian
- Italian

#### How to run
Make sure docker compose and maven are configured on your system/IDE:

1. Fork the project
2. From root directory enter commands:
- mvn dependency:resolve
- docker compose -f compose.localhost.yaml up -d --remove-orphans
- mvn paseq:exec

### Languages: 
#### - JavaScript
#### - Java
### Libraries/Frameworks:
#### - htmx
#### - hyperscript
#### - jte
#### - tailwind
#### - Spring Boot
### Other:
#### - Postgres
#### - Docker
#### - Docker compose
#### - Maven
