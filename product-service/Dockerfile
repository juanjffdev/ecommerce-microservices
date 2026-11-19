FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY . /app
RUN chmod +x mvnw
EXPOSE 8081
CMD ["./mvnw", "spring-boot:run"]