FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY product-service/ /app/
COPY commons/ /commons/
RUN chmod +x mvnw && ./mvnw -B -f /commons/pom.xml install
EXPOSE 8081
CMD ["./mvnw", "spring-boot:run"]
