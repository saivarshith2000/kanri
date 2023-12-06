FROM eclipse-temurin:17-jdk-focal
 
WORKDIR /app
 
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
 
COPY src ./src

ENV GOOGLE_APPLICATION_CREDENTIALS=./secrets/firebase-secret.json
CMD ["./mvnw", "spring-boot:run"]