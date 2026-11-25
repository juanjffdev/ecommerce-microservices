FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY auth-service/ /app/
COPY commons/ /commons/
RUN chmod +x mvnw && ./mvnw -B -f /commons/pom.xml install
EXPOSE 8080
CMD ["./mvnw", "spring-boot:run"]
